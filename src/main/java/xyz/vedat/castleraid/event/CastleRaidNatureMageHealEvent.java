package xyz.vedat.castleraid.event;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
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
import xyz.vedat.castleraid.classes.NatureMage;

public class CastleRaidNatureMageHealEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidNatureMageHealEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onMageWand(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
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
        
        new BukkitRunnable() {
            
            Vector direction = player.getEyeLocation().getDirection();
            Location initialLoc = player.getEyeLocation();
            Location nextLocation = initialLoc.clone().add(direction.clone().multiply(1));
            ArrayList<Damageable> damageablePlayers;
            
            @Override
            public void run() {
                
                for (int j = 0; j < 15; j++) {
                    
                    boolean hasImpact = false;
                    
                    nextLocation = nextLocation.clone().add(direction.clone().multiply(0.8));
                    
                    if (plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid()) {
                        hasImpact = true;
                    }
                    
                    for (CastleRaidPlayer otherCrPlayer : plugin.getCrPlayers().values()) {
                        
                        if (nextLocation.distance(otherCrPlayer.getPlayer().getLocation()) < 1.5 && !crPlayer.equals(otherCrPlayer)) {
                            
                            hasImpact = true;
                            
                            if (damageablePlayers == null) {
                                damageablePlayers = new ArrayList<>();
                            }
                            
                            damageablePlayers.add((Damageable) otherCrPlayer);
                            
                        }
                        
                    } 
                    
                    for (int i = 0; i < 3; i++) {
                        
                        plugin.getGameWorld().spigot().playEffect(nextLocation.clone().add(Math.random(), Math.random(), Math.random()), Effect.HEART, 0, 1, 0, 0, 0, 0, 0, 32);
                        // Maybe change the heart to be the "explosion" effect, make the trail some other particle.
                    }
                    
                    if (initialLoc.distance(nextLocation) > 24 || hasImpact) {
                        
                        plugin.getGameWorld().playEffect(nextLocation, Effect.WITCH_MAGIC, 0); // Should change this for sure.
                        
                        if (damageablePlayers != null) {
                            
                            for (Damageable healedPlayer : damageablePlayers) {
                                
                                double newHealth = healedPlayer.getHealth() + 10;
                                
                                healedPlayer.setHealth(newHealth > healedPlayer.getMaxHealth() ? healedPlayer.getMaxHealth() : newHealth);
                                
                            }
                            
                        }
                        
                        cancel();
                        return;
                        
                    }
                    
                }
                
            }
            
        }.runTaskTimer(plugin, 0L, 0L);
        
    }
    
}
