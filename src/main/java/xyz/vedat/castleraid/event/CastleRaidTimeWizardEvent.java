package xyz.vedat.castleraid.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
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
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
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
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof TimeWizard)) {
            return;
        }
        
        TimeWizard timeWizard = (TimeWizard) crPlayer.getCrClass();
        
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        
        if (event.getMaterial() == timeWizard.getClassItems().get(0).getType()) {
            
            if (timeWizard.isOnCooldown(CastleRaidCooldown.TIMEWIZARD_TELEPORT)) {
                return;
            } else {
                timeWizard.setOnCooldown(CastleRaidCooldown.TIMEWIZARD_TELEPORT);
            }
            
            Vector direction = player.getEyeLocation().getDirection();
            Location initialLoc = player.getEyeLocation();
            Location nextLocation = initialLoc.clone().add(direction.clone().multiply(1));

            while (true) {

                nextLocation = nextLocation.clone().add(direction.clone().multiply(0.8));

                if (initialLoc.distance(nextLocation) > 50 || plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid()) {

                    nextLocation = nextLocation.getWorld().getHighestBlockAt(nextLocation).getLocation();
                    
                    player.teleport(nextLocation);

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
