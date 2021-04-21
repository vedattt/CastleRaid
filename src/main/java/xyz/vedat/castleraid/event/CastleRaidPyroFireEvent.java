package xyz.vedat.castleraid.event;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.Pyromancer;

public class CastleRaidPyroFireEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidPyroFireEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPyroFire(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if ( !(crPlayer.getCrClass() instanceof Pyromancer) || event.getItem() == null ||
             !event.getItem().isSimilar(crPlayer.getCrClass().getClassItems().get(0)) || 
             (event.getAction() != Action.RIGHT_CLICK_AIR && 
             event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        
        Pyromancer pyromancer = (Pyromancer) crPlayer.getCrClass();
        ArrayList<FallingBlock> fallingFire = new ArrayList<>();
        
        if (pyromancer.isOnCooldown(CastleRaidCooldown.PYROMANCER_FLAMETHROWER)) return;
        else pyromancer.setOnCooldown(CastleRaidCooldown.PYROMANCER_FLAMETHROWER);
        
        for (int i = 0; i < 5; i++) {
            
            fallingFire.add(player.getWorld().spawnFallingBlock(
                player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(1)).add(Vector.getRandom().multiply(1.2).subtract(new Vector(0.6, 0.6, 0.6))), 
                Material.FIRE, (byte) 0
            ));
            fallingFire.get(i).setVelocity(player.getLocation().getDirection().normalize().multiply(1.3).add(Vector.getRandom().multiply(0.4).subtract(new Vector(0.2, 0.2, 0.2))));
            fallingFire.get(i).setHurtEntities(true);
            
        }
        
        new BukkitRunnable() {
            
            @Override
            public void run() {
                
                for (int i = fallingFire.size() - 1; i >= 0; i--) {
                    FallingBlock fallingBlock = fallingFire.get(i);
                    if (fallingBlock.isOnGround() || !fallingBlock.isValid()) {
                        fallingFire.remove(i);
                        
                        if (Math.random() > 0.35 || fallingBlock.getLocation().getBlock().getRelative(0, -1, 0).getType() == Material.LEAVES)
                            fallingBlock.getLocation().getBlock().setType(Material.AIR);
                    } else {
                        plugin.getGameWorld().getNearbyEntities(fallingFire.get(i).getLocation(), 1, 1, 1).forEach(
                            entity -> {

                                if (entity instanceof Damageable && entity.getUniqueId() != player.getUniqueId()) {
                                    ((Damageable) entity).damage(3, player);
                                }

                            });
                    }
                    
                }
                
                if (fallingFire.size() == 0) {
                    cancel();
                }
                
            }
            
        }.runTaskTimer(plugin, 0L, 1L);
        
    }
    
}
