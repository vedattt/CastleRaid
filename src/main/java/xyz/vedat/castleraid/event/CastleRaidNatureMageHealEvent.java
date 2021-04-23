package xyz.vedat.castleraid.event;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.NatureMage;

public class CastleRaidNatureMageHealEvent implements Listener {
    
    CastleRaidMain plugin;
        
    public CastleRaidNatureMageHealEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    //@SuppressWarnings("deprecation")
    public void onMageWand(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        
        if (!(crPlayer.getCrClass() instanceof NatureMage) || event.getItem() == null || !event.getItem().isSimilar(crPlayer.getCrClass().getClassItems().get(0))) {
            return;
        }
        
        NatureMage natureMage = (NatureMage) crPlayer.getCrClass();
        
        if (natureMage.isOnCooldown(CastleRaidCooldown.NATUREMAGE_WAND)) {
            plugin.getLogger().info("NatureMage " + player.getDisplayName() + " is on cooldown.");
            return;
        } else {
            natureMage.setOnCooldown(CastleRaidCooldown.NATUREMAGE_WAND);
        }
        
        player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
        
        Vector direction = player.getEyeLocation().getDirection();
        Location initialLoc = player.getEyeLocation();
        Location nextLocation = initialLoc.clone().add(direction.clone().multiply(1));
        
        for (int j = 0; j < 10000; j++) { // Not really a great way... Maybe while (true)? Or something else?
            
            nextLocation = nextLocation.clone().add(direction.clone().multiply(0.8));
            
            boolean hasImpact = plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid() ||
                !plugin.getGameWorld().getNearbyEntities(nextLocation, 1.5, 1.5, 1.5)
                    .stream().collect(ArrayList::new,
                        (BiConsumer<ArrayList<Entity>, Entity>) (list, entity) -> {
                            if (entity.getUniqueId() != player.getUniqueId() && entity instanceof Damageable) {
                                list.add(entity);
                            }
                        }, ArrayList::addAll).isEmpty();
            
            for (int i = 0; i < 6; i++) {
                
                plugin.getGameWorld().spigot().playEffect(
                    nextLocation.clone().add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5), // Particle Spawn Location
                    Effect.HAPPY_VILLAGER, 0, 1, // Effect Type - ID - Databit
                    (float) Math.random() - 0.5f, (float) Math.random() - 0.5f, (float) Math.random() - 0.5f, // Offset (x,y,z)
                    0.1f, 0, 32 // Speed - Particle Count - Radius
                );
                
            }
            
            //plugin.getLogger().info("MageProjectile at " + nextLocation.toString());
            
            if (initialLoc.distance(nextLocation) > 24 || hasImpact) {
                
                for (int i = 0; i < 15; i++) {
                    
                    plugin.getGameWorld().spigot().playEffect(
                        nextLocation.clone().add((Math.random() - 0.5) * 3, (Math.random() - 0.5) * 3, (Math.random() - 0.5) * 3), // Particle Spawn Location
                        Effect.HEART, 0, 1, // Effect Type - ID - Databit
                        (float) Math.random() - 0.5f, (float) Math.random() - 0.5f, (float) Math.random() - 0.5f, // Offset (x,y,z)
                        0.1f, 0, 32 // Speed - Particle Count - Radius
                    );
                    
                }
                
                for (Entity healedEntity : plugin.getGameWorld().getNearbyEntities(nextLocation, 1.5, 1.5, 1.5)) {
                    
                    boolean ownTeam = false;
                    Teams team;
                    
                    if (healedEntity instanceof Player) {
                        
                        team = plugin.getCrPlayers().get(healedEntity.getUniqueId()).getTeam();
                        
                        if (team == crPlayer.getTeam()) {
                            ownTeam = true;
                        }
                        
                    }
                    
                    plugin.getLogger().info("Healed entity: " + healedEntity.getName());
                    
                    if (healedEntity instanceof Damageable && ownTeam) {
                        ((Damageable) healedEntity).setHealth( Math.min(((Damageable) healedEntity).getMaxHealth(), ((Damageable) healedEntity).getHealth() + 20) );
                    }
                    
                }
                
                return;
                
            }
            
        }
        
    }
    
}
