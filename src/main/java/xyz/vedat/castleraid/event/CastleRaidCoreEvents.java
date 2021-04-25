package xyz.vedat.castleraid.event;

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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.GameState;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.ClassItemFactory;

public class CastleRaidCoreEvents implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidCoreEvents(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        
        if (event.getEntity() instanceof Player) {
            
            ((Player) event.getEntity()).setFoodLevel(20);
            event.setFoodLevel(20);
            event.setCancelled(true);
            
        }
        
    }
    
    @EventHandler
    public void preventExperienceDrop(EntityDeathEvent event) {
        
        event.setDroppedExp(0);
        
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
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (crPlayer == null) {
            return;
        }
        
        if (!plugin.isBeaconCaptured() && crPlayer.isCarryingBeacon() && event.getTo().getBlock().getLocation().equals(plugin.getBeaconTarget())) {
            
            crPlayer.addBalance(20);
            
            plugin.getLogger().info("Won the game");
            plugin.setBeaconCaptured(true);
            
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
        
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(event.getPlayer());
        
        if (crPlayer.getCrClass().getClassItems().values().stream().anyMatch(crItem -> event.getItemDrop().getItemStack().isSimilar(crItem)) ||
            event.getItemDrop().getItemStack().isSimilar(ClassItemFactory.getClassPickerItem()) ||
            event.getItemDrop().getItemStack().isSimilar(ClassItemFactory.getTrackerCompass())) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        
        if (event.getCurrentItem() == null) {
            return;
        }
        
        // Prevent armor slots and class picker modifications
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR) || event.getCurrentItem().isSimilar(ClassItemFactory.getClassPickerItem()) ||
            (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName() &&
            event.getCurrentItem().getItemMeta().getDisplayName().equals(ClassItemFactory.getBalanceItem(0).getItemMeta().getDisplayName()))) {
            event.setCancelled(true);
        }
        
        // Prevent putting stuff into chests, etc.
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        
        plugin.getAllBuilderClaymores().remove(event.getBlock().getLocation());
        
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
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (crPlayer == null) {
            return;
        }
        
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && crPlayer.getTeam() == CastleRaidMain.Teams.RED && event.getClickedBlock().getType() == Material.BEACON) {
            plugin.getLogger().info("Dude grabbed beacon");
            plugin.announceInChat(player.getDisplayName() + " has stolen the beacon");
            player.getWorld().playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 10000, 0.5f);
            event.getClickedBlock().setType(Material.AIR);
            crPlayer.setCarriesBeacon(true);
            plugin.setSpyBeaconGrabber(crPlayer);
        } else {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        
        final Material[] UNDROPPING_ITEMS = new Material[] {
            Material.SEEDS, Material.MINECART
        };
        
        for (Material material : UNDROPPING_ITEMS) {
            if (event.getEntity().getItemStack().getType() == material) {
                event.setCancelled(true);
                return;
            }
        }
        
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        Player player = event.getPlayer();
        
        if (plugin.getGameState() == GameState.STANDBY) {
            
            plugin.getLogger().info("Player is teleported during STANDBY...");
            player.teleport(plugin.getStandbyWorldLocation());
            
        } else if (plugin.getGameState() == GameState.RUNNING) {
            
            plugin.getLogger().info("Adding and spawning newly joined spectator: " + player.getName());
            plugin.addCrPlayer(player, Teams.SPECTATOR);
            
            plugin.getCrPlayer(player).spawnPlayer();
            
        } else { // GameState WAITING
            
            plugin.getLogger().info("Adding and spawning newly joined CR player: " + player.getName());
            plugin.addCrPlayer(player, Teams.WAITING);
            
            new BukkitRunnable(){
                
                @Override
                public void run() {
                    
                    if (plugin.getCrPlayers().size() == 1 && plugin.getWaitingTask() == null) {
                        plugin.getLogger().info("Game now has at least one player, starting game events...");
                        plugin.startGameEvents();
                    }
                    
                }
                
            }.runTaskLater(plugin, 20L);
            
            plugin.getCrPlayer(player).spawnPlayer();
            
        }
        
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        
        Player player = event.getPlayer();
        
        plugin.getCrPlayers().remove(player.getUniqueId());
        
        if (plugin.getGameState() != GameState.RUNNING) {
            return;
        }
        
        if (plugin.getCrPlayers().size() == 0) {
            
            plugin.getLogger().info("Game does not have any players, starting new CR world in 2 seconds...");
            new BukkitRunnable(){
                
                @Override
                public void run() {
                    plugin.startNewWorld();
                }
                
            }.runTaskLater(plugin, 40L);
            
        } else if (plugin.getPlayersOfTeam(CastleRaidMain.Teams.RED).size() == 0) {
            
            plugin.getLogger().info("All red team players have left.");
            plugin.announceWinningTeam(CastleRaidMain.Teams.BLUE);
            
            new BukkitRunnable(){
                
                @Override
                public void run() {
                    plugin.startNewWorld();
                }
                
            }.runTaskLater(plugin, 40L);
            
        } else if (plugin.getPlayersOfTeam(CastleRaidMain.Teams.BLUE).size() == 0) {
            
            plugin.getLogger().info("All blue team player have left.");
            plugin.announceWinningTeam(CastleRaidMain.Teams.RED);
            
            new BukkitRunnable(){
                
                @Override
                public void run() {
                    plugin.startNewWorld();
                }
                
            }.runTaskLater(plugin, 40L);
            
        }
        
    }
    
}
