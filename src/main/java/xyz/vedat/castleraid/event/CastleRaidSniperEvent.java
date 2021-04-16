package xyz.vedat.castleraid.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.classes.Sniper;

public class CastleRaidSniperEvent implements Listener {
    
    final double MAX_VELOCITY_MULTIPLIER = 6;
    final double BAD_VELOCITY_MULTIPLIER = 1.5;
    
    CastleRaidMain plugin;
    
    public CastleRaidSniperEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent event) {
        
        if (event.getEntity() instanceof Player) {
            
            Player player = (Player) event.getEntity();
            CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
            CastleRaidClass crClass = crPlayer.getCrClass();
            
            Entity rifleArrowEntity = event.getProjectile();
            double velocityMultiplier = rifleArrowEntity.getVelocity().length() > 2.85 ? MAX_VELOCITY_MULTIPLIER : BAD_VELOCITY_MULTIPLIER;;
            
            if (crClass instanceof Sniper) {
                
                plugin.getLogger().info(player.getName() + " Default Sniper Entity Velocity: " + rifleArrowEntity.getVelocity().length());
                
                rifleArrowEntity.setVelocity(rifleArrowEntity.getVelocity().multiply(velocityMultiplier));
                
                plugin.getLogger().info(player.getName() + " Modified Sniper Entity Velocity: " + rifleArrowEntity.getVelocity().length());
                
            }
            
        }
        
    }
    
}
