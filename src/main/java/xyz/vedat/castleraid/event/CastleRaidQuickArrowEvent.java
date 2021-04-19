package xyz.vedat.castleraid.event;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.interfaces.BowArrowSpeedable;

public class CastleRaidQuickArrowEvent implements Listener {
    
    final double GOOD_DRAW_THRESHOLD = 2.85;
    
    CastleRaidMain plugin;
    
    public CastleRaidQuickArrowEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onPlayerShootBow(EntityShootBowEvent event) {
        
        if (event.getEntity() instanceof Player) {
            
            Player player = (Player) event.getEntity();
            CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
            CastleRaidClass crClass = crPlayer.getCrClass();
            BowArrowSpeedable speedable;
            
            Entity rifleArrowEntity = event.getProjectile();
            
            if (crClass instanceof BowArrowSpeedable) {
                
                speedable = (BowArrowSpeedable) crClass;
                
                plugin.getLogger().info(player.getName() + " Default Sniper Entity Velocity: " + rifleArrowEntity.getVelocity().length());
                
                rifleArrowEntity.setVelocity(rifleArrowEntity.getVelocity().multiply(
                    event.getForce() * 3 > GOOD_DRAW_THRESHOLD ? 
                    speedable.getMaxVelocityMultiplier() : speedable.getBadVelocityMultiplier()
                ));
                
                plugin.getLogger().info(player.getName() + " Modified Sniper Entity Velocity: " + rifleArrowEntity.getVelocity().length());
                
            }

            if (event.getForce() * 3 > GOOD_DRAW_THRESHOLD) {
                
                for (float i = 1; i < 5; i++) {
                    //nextLocation.clone().add(direction.clone().multiply(0.8))
                    player.getWorld().spigot().playEffect(player.getEyeLocation().add(player.getEyeLocation().getDirection().clone().normalize().multiply(i)), 
                        Effect.SMOKE, 0, 0, 0, 0.8f, 0, 0, 0, 32);
                    
                }
                
            }
            
        }
        
    }
    
}
