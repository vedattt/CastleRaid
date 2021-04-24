package xyz.vedat.castleraid.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.TimeWizard;

public class CastleRaidTimeWizardEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidTimeWizardEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onTimeWizardAttacked(EntityDamageEvent event) {
        
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (!(crPlayer.getCrClass() instanceof TimeWizard)) {
            return;
        }
        
        TimeWizard timeWizard = (TimeWizard) crPlayer.getCrClass();
        
        if (timeWizard.isOnCooldown(CastleRaidCooldown.TIMEWIZARD_INVINCIBILITY)) {
            event.setCancelled(true);
        }
        
    }
    
    @EventHandler
    public void onTimeWizardItem(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (!(crPlayer.getCrClass() instanceof TimeWizard) || crPlayer.isCarryingBeacon()) {
            return;
        }
        
        TimeWizard timeWizard = (TimeWizard) crPlayer.getCrClass();
        
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        
        if (event.getMaterial() == timeWizard.getClassItems().get(1).getType()) {
            
            if (timeWizard.isOnCooldown(CastleRaidCooldown.TIMEWIZARD_TELEPORT)) {
                return;
            } else {
                timeWizard.setOnCooldown(CastleRaidCooldown.TIMEWIZARD_TELEPORT);
                
                new BukkitRunnable(){
                    
                    @Override
                    public void run() {
                        
                        if (timeWizard.getRemainingCooldownInSecs(CastleRaidCooldown.TIMEWIZARD_TELEPORT) < 1) {
                            plugin.getLogger().info("Time wizard cooldown is over, task cancelled.");
                            cancel();
                            return;
                        }
                        
                        int enderIndex = player.getInventory().first(Material.EYE_OF_ENDER);
                        ItemStack enderItem = player.getInventory().getItem(enderIndex);
                        enderItem.setAmount((int) Math.ceil(timeWizard.getRemainingCooldownInSecs(CastleRaidCooldown.TIMEWIZARD_TELEPORT)));
                        player.getInventory().setItem(enderIndex, enderItem);
                        
                    }
                    
                }.runTaskTimer(plugin, 0L, 20L);
                
            }
            
            player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
            
            Vector direction = player.getEyeLocation().getDirection();
            Location initialLoc = player.getEyeLocation();
            Location nextLocation = initialLoc.clone().add(direction.clone().multiply(1));
            
            while (true) {
                
                nextLocation = nextLocation.clone().add(direction.clone().multiply(0.8));

                if (initialLoc.distance(nextLocation) > 50 || plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid()) {
                    
                    if (initialLoc.distance(nextLocation) > 50) {
                        
                        nextLocation.setY(nextLocation.getY() + 1);
                        
                        player.teleport(nextLocation);
                        
                    } else if (plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid()) {
                        
                        int highestY = nextLocation.getWorld().getHighestBlockYAt(nextLocation);
                        nextLocation.setY(highestY + 1);
                        
                        player.teleport(nextLocation);
                        
                    }
                    
                    
                    player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    
                    return;
                    
                }

            }
            
        } else if (event.getMaterial() == timeWizard.getClassItems().get(2).getType() && player.getInventory().contains(Material.COAL)) {
            
            timeWizard.setOnCooldown(CastleRaidCooldown.TIMEWIZARD_INVINCIBILITY);
            
            int antimatterIndex = player.getInventory().first(Material.COAL);
            ItemStack antimatterItem = player.getInventory().getItem(antimatterIndex);
            antimatterItem.setAmount(antimatterItem.getAmount() - 1);
            player.getInventory().setItem(antimatterIndex, antimatterItem);
            
        }
        
    }
    
}
