package xyz.vedat.castleraid.event;

import org.bukkit.Effect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.Spy;

public class CastleRaidSpySmokeEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidSpySmokeEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onSpySmokeEvent(ProjectileHitEvent event) {
        
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        
        Player player = (Player) event.getEntity().getShooter();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Spy)) {
            return;
        }
        
        //Spy spy = (Spy) crPlayer.getCrClass();
        
        Projectile entity = event.getEntity();
        
        if (entity.getType() == EntityType.SNOWBALL) {
            
            new BukkitRunnable(){
                
                int i = 0;
                
                @Override
                @SuppressWarnings("deprecation")
                public void run() {
                    
                    for (CastleRaidPlayer otherCrPlayer : plugin.getCrPlayers().values()) {
                        
                        if (otherCrPlayer.getTeam() == Teams.SPECTATOR || otherCrPlayer.getPlayer().getUniqueId() == player.getUniqueId()) {
                            
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(0, 3, 0), Effect.EXPLOSION_LARGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(3, 3, 3), Effect.EXPLOSION_LARGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(3, 3, -3), Effect.EXPLOSION_LARGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(-3, 3, 3), Effect.EXPLOSION_LARGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(-3, 3, -3), Effect.EXPLOSION_LARGE, 0);
                            
                        } else {
                            
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(0, 3, 0), Effect.EXPLOSION_HUGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(3, 3, 3), Effect.EXPLOSION_HUGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(3, 3, -3), Effect.EXPLOSION_HUGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(-3, 3, 3), Effect.EXPLOSION_HUGE, 0);
                            otherCrPlayer.getPlayer().playEffect(entity.getLocation().add(-3, 3, -3), Effect.EXPLOSION_HUGE, 0);
                            
                        }
                        
                    }
                    
                    i++;
                    if (i == 65) {
                        cancel();
                    }
                    
                }
                
            }.runTaskTimer(plugin, 0L, 5L);
            
            
            
        }
        
    }
    
}
