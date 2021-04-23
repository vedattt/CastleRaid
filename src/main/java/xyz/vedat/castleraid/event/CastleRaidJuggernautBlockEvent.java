package xyz.vedat.castleraid.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.Juggernaut;


public class CastleRaidJuggernautBlockEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidJuggernautBlockEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onJuggernautBlocking(EntityDamageEvent event) {
        
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (!(crPlayer.getCrClass() instanceof Juggernaut) || !player.isBlocking()) {
            return;
        }
        
        if (event.getCause() == DamageCause.VOID || event.getCause() == DamageCause.FALL) {
            return;
        }
        
        event.setDamage(0);
        
    }
    
}
