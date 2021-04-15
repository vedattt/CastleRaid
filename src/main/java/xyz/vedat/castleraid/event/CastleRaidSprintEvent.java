package xyz.vedat.castleraid.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.interfaces.ClassAccelerable;

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
        ClassAccelerable accelerable;
        
        if (crClass instanceof ClassAccelerable) {
            
            accelerable = (ClassAccelerable) crClass;
            
            if (event.isSprinting() && !crPlayer.hasOngoingSprintEvent()) {
                
                sprintTask = new BukkitRunnable() {
                    
                    @Override
                    public void run() {
                        
                        if (player.isSprinting()) {
                            
                            player.setWalkSpeed( (float) Math.min(accelerable.getMaxSpeed(), player.getWalkSpeed() * accelerable.getAccelerationRate()) );
                            
                            plugin.getLogger().info(player.getName() + " walk speed increased to " + player.getWalkSpeed() + " by task " + getTaskId());
                            
                            if (player.getWalkSpeed() == accelerable.getMaxSpeed()) {
                                crPlayer.setOngoingSprintEvent(null);
                            }
                            
                        }
                        
                    }
                    
                }.runTaskTimer(plugin, 0L, 20L);
                
                crPlayer.setOngoingSprintEvent(sprintTask);
                
            } else {
                
                crPlayer.setOngoingSprintEvent(null);
                
                player.setWalkSpeed(accelerable.getDefaultSpeed());
                
                plugin.getLogger().info(player.getName() + " walk speed reset to " + player.getWalkSpeed());
                
            }
            
        }
        
    }
    
}
