package xyz.vedat.castleraid.event;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.interfaces.CanBackstab;

public class CastleRaidBackstabEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidBackstabEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onBackstab(EntityDamageByEntityEvent event) {
        
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        Player stabbedPlayer = (Player) event.getEntity();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof CanBackstab) || !player.getItemInHand().isSimilar( ((CanBackstab) crPlayer.getCrClass()).getBackstabItem() )) {
            return;
        }
        
        Location pDirection = player.getEyeLocation().clone();
        Location spDirection = stabbedPlayer.getLocation().clone(); // Possibly change to eyelocation later?
        
        pDirection.setPitch(0);
        spDirection.setPitch(0);
        
        double angle = pDirection.getDirection().angle(spDirection.getDirection());
        
        plugin.getLogger().info("### Backstab Angle: " + angle);
        plugin.getLogger().info("Pyaw: " + pDirection.getYaw());
        plugin.getLogger().info("Tyaw: " + spDirection.getYaw());
        
        if (angle < Math.toRadians(60)) {
            plugin.getLogger().info("Backstab occurrance: " + player.getDisplayName() + " backstabs " + stabbedPlayer.getDisplayName());
            event.setDamage( 200 /*((Damageable)event.getEntity()).getMaxHealth()*/ );
            player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
        }
        
    }
    
}
