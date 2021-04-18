package xyz.vedat.castleraid.event;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.Berserker;

public class CastleRaidBerserkKillEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidBerserkKillEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onBerserkKill(EntityDamageByEntityEvent event) {
        
        if (!(event.getDamager() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Berserker) || !(event.getEntity() instanceof Player)) {
            return;
        }
        
        Berserker berserker = (Berserker) crPlayer.getCrClass();
        
        plugin.getLogger().info("Berserk damaged " + (event.getFinalDamage() + berserker.getKillCount() * 2) + " from original " + event.getDamage());
        
        event.setDamage(event.getFinalDamage() + berserker.getKillCount() * 2);
        
        if (((Damageable) event.getEntity()).getHealth() <= event.getDamage()) {
            
            plugin.getLogger().info("Berserk killcount increased to " + berserker.getKillCount());
            
            berserker.incrementKillCount();
            
            if (player.getWalkSpeed() < 0.64) {
                player.setWalkSpeed(player.getWalkSpeed() + 0.05f);
            }
            
            if (player.getHealth() < 80) {
                player.setMaxHealth(player.getMaxHealth() + 4);
            }
            
            player.setHealth(player.getMaxHealth());
            
        }
        
    }
    
}
