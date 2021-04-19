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
import xyz.vedat.castleraid.classes.ClassItemFactory;

public class CastleRaidCoreEvents implements Listener {
    
    CastleRaidMain plugin;
    BukkitTask countdownTask;
    Integer maxPlayersToStart = 6;
    
    public CastleRaidCoreEvents(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
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
        plugin.getCrPlayers().put(player.getUniqueId(), new CastleRaidPlayer(player, null, CastleRaidMain.Teams.WAITING, this));
        switch (plugin.currentState) {
            case RUNNING:
                // TODO: Assign to random team when joined
                player.teleport(new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0));
            case RESETING:
                player.teleport(new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0));
            case WAITING:
                player.teleport(new Location(plugin.getGameWorld(), -520, 6, 557));
                if (plugin.getServer().getOnlinePlayers().size() >= 6) {
                    countdownTask = new BukkitRunnable() {

                        @Override
                        public void run() {

                            if (player.isSprinting()) {

                                player.setWalkSpeed( (float) Math.min(accelerable.getMaxSpeed(), player.getWalkSpeed() * accelerable.getAccelerationRate()) );

                                plugin.getLogger().info(player.getName() + " walk speed increased to " + player.getWalkSpeed() + " by task " + getTaskId());

                                if (plugin.getServer().getOnlinePlayers().size() >= 6) {
                                    setOngoingCountdownEvent(null);
                                }

                            }

                        }

                    }.runTaskTimer(plugin, 0L, 1L);

                    setOngoingCountdownEvent(countdownTask);
                }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    public void setOngoingCountdownEvent(BukkitTask sprintTask) {

        if (sprintTask == null && this.countdownTask != null) {
            this.countdownTask.cancel();
        }

        this.countdownTask = sprintTask;

    }
    
}
