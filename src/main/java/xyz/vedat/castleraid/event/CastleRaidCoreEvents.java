package xyz.vedat.castleraid.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.ClassItemFactory;

public class CastleRaidCoreEvents implements Listener {
    
    CastleRaidMain plugin;
    BukkitTask countdownTask;
    Integer maxPlayersToStart = 6;
    Integer countdownToStart = 30;
    Integer runningTimeInMinutes = 10;
    Integer currentCountdown;
    
    public CastleRaidCoreEvents(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        
        if (event.getEntity() instanceof Player && plugin.getCrPlayers().get(event.getEntity().getUniqueId()).getTeam() == Teams.WAITING) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (crPlayer == null) {
            return;
        }
        
        if (crPlayer.isCarryingBeacon() && event.getTo().getBlock().getLocation().equals(plugin.getBeaconTarget())) {
            plugin.getLogger().info("Won the game");
            plugin.startNewWorld();
        }
        
    }
    
    @EventHandler
    public void onBlockInteracted(PlayerInteractEvent event) {
        
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        
        if (event.getClickedBlock().getLocation().equals(plugin.getBeaconLocation())) {
            plugin.getLogger().info("Beacon was interacted");
            onBeaconGrabbed(event);
        }
        
    }
    
    @EventHandler
    public void onItemThrown(PlayerDropItemEvent event) {
        
        event.setCancelled(true);
        
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        
        if (event.getCurrentItem() == null) {
            return;
        }
        
        // Prevent armor slots and class picker modifications
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR) || event.getCurrentItem().isSimilar(ClassItemFactory.getClassPickerItem())) {
            event.setCancelled(true);
        }
        
        // Prevent putting stuff into chests, etc.
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        
        plugin.getBuilderClaymores().remove(event.getBlock().getLocation());
        
    }
    
    @EventHandler
    public void onHealthGained(EntityRegainHealthEvent event) {
        
        if (event.getRegainReason() == RegainReason.SATIATED && event.getEntity() instanceof Player) event.setCancelled(true);
        
    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        
        if (event.getSpawnReason() == SpawnReason.NATURAL && event.getEntity() instanceof Monster) event.setCancelled(true);
        
    }
    
    /**
     * Not an actual event, it gets called by the event listener though.
     * @param event The interaction event
     */
    public void onBeaconGrabbed(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (crPlayer == null) {
            return;
        }
        
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && crPlayer.getTeam() == CastleRaidMain.Teams.RED && event.getClickedBlock().getType() == Material.BEACON) {
            plugin.getLogger().info("Dude grabbed beacon");
            plugin.announceInChat(player.getDisplayName() + " has stolen the beacon");
            player.getWorld().playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 10000, 0.5f);
            event.getClickedBlock().setType(Material.AIR);
            crPlayer.setCarriesBeacon(true);
        } else {
            event.setCancelled(true);
        }
        
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        plugin.getCrPlayers().put(player.getUniqueId(), new CastleRaidPlayer(player, null, CastleRaidMain.Teams.WAITING, plugin));
        switch (plugin.getGameState()) {
            case RUNNING:
                plugin.splitWaitersIntoTeams();
                player.teleport(new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0));
            case STANDBY:
                player.teleport(new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0));
            case WAITING:
                player.teleport(new Location(plugin.getGameWorld(), -520, 6, 557));
                startCountdown();
        }
    }

    public void startCountdown() {
        if (!hasOngoingCountdownEvent()) {
            if (plugin.getServer().getOnlinePlayers().size() >= maxPlayersToStart) {

                currentCountdown = 0;
                countdownTask = new BukkitRunnable() {

                    @Override public void run() {

                        if (plugin.getServer().getOnlinePlayers().size() < maxPlayersToStart) {
                            plugin.getLogger().info("Countdown stopped! Not enough players to start");
                            plugin.announceInChat("Countdown stopped! Not enough players to start");
                            setOngoingCountdownEvent(null);
                            return;
                        }
                        if (currentCountdown % 5 == 0) {
                            plugin.announceInChat(currentCountdown + " seconds until game start");
                        }
                        currentCountdown++;

                        if (currentCountdown >= countdownToStart) {
                            plugin.setGameState(CastleRaidMain.GameState.RUNNING);

                            cancel();
                            currentCountdown = 0;
                            countdownTask = new BukkitRunnable() {

                                @Override public void run() {

                                    currentCountdown++;
                                    int timeLeft = currentCountdown - runningTimeInMinutes;
                                    if (timeLeft <= 0) {
                                        cancel();
                                        plugin.announceWinningTeam(CastleRaidMain.Teams.BLUE);
                                        setOngoingCountdownEvent(null);
                                        startCountdown();
                                    }
                                    if (timeLeft == 1) {
                                        plugin.announceInChat("1 minutes left!");
                                    }
                                    else {
                                        plugin.announceInChat(timeLeft + " minutes left...");
                                    }

                                }

                            }.runTaskTimer(plugin, 0L, 60L);

                            setOngoingCountdownEvent(countdownTask);

                            plugin.announceInChat("Game started! Good Luck!");
                            plugin.splitWaitersIntoTeams();
                        }


                    }

                }.runTaskTimer(plugin, 0L, 1L);

                setOngoingCountdownEvent(countdownTask);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getInventory().clear();
        plugin.getCrPlayers().remove(player.getUniqueId());
        // TODO: Teleport player to default
        if (plugin.getServer().getOnlinePlayers().size() == 0) {
            plugin.startNewWorld();
        }
        else if (plugin.getPlayersOfTeam(CastleRaidMain.Teams.RED).size() == 0) {
            plugin.announceWinningTeam(CastleRaidMain.Teams.BLUE);
            setOngoingCountdownEvent(null);
            startCountdown();
        }
        else if (plugin.getPlayersOfTeam(CastleRaidMain.Teams.BLUE).size() == 0) {
            plugin.announceWinningTeam(CastleRaidMain.Teams.RED);
            setOngoingCountdownEvent(null);
            startCountdown();
        }
    }

    public void setOngoingCountdownEvent(BukkitTask sprintTask) {

        if (sprintTask == null && this.countdownTask != null) {
            this.countdownTask.cancel();
        }

        this.countdownTask = sprintTask;

    }

    public boolean hasOngoingCountdownEvent() {
        return countdownTask != null;
    }
    
}
