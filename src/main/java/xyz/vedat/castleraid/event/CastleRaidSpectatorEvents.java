package xyz.vedat.castleraid.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidMain.Teams;

public class CastleRaidSpectatorEvents implements Listener {
    
    private CastleRaidMain plugin;
    
    public CastleRaidSpectatorEvents(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        
        if (plugin.getCrPlayer(event.getPlayer()).getTeam() == Teams.SPECTATOR) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        
        if (plugin.getCrPlayer(event.getPlayer()).getTeam() == Teams.SPECTATOR) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onItemDropped(PlayerDropItemEvent event) {
        
        if (plugin.getCrPlayer(event.getPlayer()).getTeam() == Teams.SPECTATOR) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onItemDropped(PlayerPickupItemEvent event) {
        
        if (plugin.getCrPlayer(event.getPlayer()).getTeam() == Teams.SPECTATOR) {
            event.setCancelled(true);
        }
        
    }
    
}
