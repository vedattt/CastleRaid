package xyz.vedat.castleraid.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;

public class CastleRaidDeathEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidDeathEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onPlayerRespawns(PlayerRespawnEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        plugin.getLogger().info("Player " + player.getName() + " respawned.");
        event.setRespawnLocation(crPlayer.spawnPlayer());
        
    }
    
    @EventHandler
    public void onPlayerDies(PlayerDeathEvent event) {
        
        Player player = event.getEntity();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        event.setKeepInventory(true);
        
        if (crPlayer == null) {
            return;
        }
        
        plugin.getLogger().info("Player " + player.getName() + " died.");
        
        if (crPlayer.isCarryingBeacon()) {
            crPlayer.setCarriesBeacon(false);
            plugin.getGameWorld().getBlockAt(plugin.getBeaconLocation()).setType(Material.BEACON);
            plugin.getLogger().info("Returned beacon to place.");
            plugin.announceInChat("The beacon was returned...");
        }
        
        if (player.getKiller() != null) {
            plugin.getCrPlayer(player.getKiller()).addBalance(3);
        }
        
        new BukkitRunnable(){

            @Override
            public void run() {
                player.spigot().respawn();
            }
            
        }.runTaskLater(plugin, 300L);
        
    }
    
}
