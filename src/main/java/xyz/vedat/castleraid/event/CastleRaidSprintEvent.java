package xyz.vedat.castleraid.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.interfaces.SprintAccelerable;

public class CastleRaidSprintEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidSprintEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        CastleRaidClass crClass = crPlayer.getCrClass();
        BukkitTask sprintTask;
        SprintAccelerable accelerable;
        
        if (crClass instanceof SprintAccelerable) {
            
            accelerable = (SprintAccelerable) crClass;
            
            if (event.isSprinting() && !accelerable.hasOngoingSprintEvent()) {
                
                sprintTask = new BukkitRunnable() {
                    
                    @Override
                    public void run() {
                        
                        if (player.isSprinting()) {
                            
                            player.setWalkSpeed( (float) Math.min(accelerable.getMaxSpeed(), player.getWalkSpeed() * accelerable.getAccelerationRate()) );
                            
                            plugin.getLogger().info(player.getName() + " walk speed increased to " + player.getWalkSpeed() + " by task " + getTaskId());
                            
                            if (player.getWalkSpeed() == accelerable.getMaxSpeed()) {
                                accelerable.setOngoingSprintEvent(null);
                            }
                            
                        }
                        
                    }
                    
                }.runTaskTimer(plugin, 0L, 1L);
                
                accelerable.setOngoingSprintEvent(sprintTask);
                
            } else {
                
                accelerable.setOngoingSprintEvent(null);
                
                player.setWalkSpeed(accelerable.getDefaultSpeed());
                
                plugin.getLogger().info(player.getName() + " walk speed reset to " + player.getWalkSpeed());
                
            }
            
        }
        
    }
    
    @EventHandler
    public void onSprinterDamaged(EntityDamageEvent event) {
        
        // Should the knight's acceleration be stopped when damaged by nonplayers? (i.e., fall damage)
        
        if (event.getEntity() instanceof Player) {
            
            Player player = (Player) event.getEntity();
            CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
            CastleRaidClass crClass = crPlayer.getCrClass();
            SprintAccelerable accelerable;
            
            if (crClass instanceof SprintAccelerable) {
                
                accelerable = (SprintAccelerable) crClass;
                
                if (accelerable.hasOngoingSprintEvent() || player.getWalkSpeed() == accelerable.getMaxSpeed()) {
                    
                    // Should the knight's acceleration be stopped completely until he starts sprinting anew after getting damaged?
                    // crPlayer.setOngoingSprintEvent(null);
                    player.setWalkSpeed(accelerable.getDefaultSpeed());
                    
                }
                
            }
            
        }
        
    }
    
}
