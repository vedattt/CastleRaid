package xyz.vedat.castleraid.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.Assassin;

public class CastleRaidAssassinEvents implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidAssassinEvents(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    
    // Events firing;
    // normal: Fishing -> projlaunch -> projhit xxxx -> fishing
    // retracted midair: fishing -> projlaunch -> fishing
    
    // PlayerFishEvent.State.FISHING will consistently be given each time the hook is recasted
    // Check for both PlayerFishEvent.State.IN_GROUND and event.getHook().isOnGround() on pull
    // if check passes, set player velocity such that it launches player to hook's location
    
    /*
    if (event.getState() == PlayerFishEvent.State.FISHING) {
        plugin.getLogger().info("Fishing!!");
    } else if (event.getState() == PlayerFishEvent.State.IN_GROUND) {
        plugin.getLogger().info("in ground!!: " + event.getHook().getLocation().toString());
    }
    */
    
    @EventHandler
    public void onHookLaunched(PlayerFishEvent event) {
        
        Player player = (Player) event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Assassin)) {
            return;
        }
        
        //Assassin assassin = (Assassin) crPlayer.getCrClass();
        
        if (event.getState() == PlayerFishEvent.State.IN_GROUND || event.getHook().isOnGround()) {
            
            player.setVelocity(
                event.getHook().getLocation()
                .subtract(player.getLocation())
                .toVector().normalize()
                .multiply(new Vector(3, 1.2, 3))
                .add(new Vector(0, 1.1, 0))
            );
            
        } else if (event.getState() == PlayerFishEvent.State.FISHING) {
            event.getHook().setVelocity(event.getHook().getVelocity().multiply(1.5));
        }
        
    }
    
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        
        Player player = (Player) event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Assassin) || crPlayer.isCarryingBeacon()) {
            return;
        }
        
        Assassin assassin = (Assassin) crPlayer.getCrClass();
        
        if (event.isSneaking() && player.getInventory().containsAtLeast(assassin.getClassItems().get(2), 1)) {
            
            plugin.getLogger().info("Assassin just hid.");
            
            int glowstoneIndex = player.getInventory().first(assassin.getClassItems().get(2).getType());
            ItemStack glowstoneItem = player.getInventory().getItem(glowstoneIndex);
            
            glowstoneItem.setAmount(glowstoneItem.getAmount() - 1);
            
            player.getInventory().setItem(glowstoneIndex, glowstoneItem);
            
            for (CastleRaidPlayer otherCrPlayer : plugin.getPlayersOfTeam(crPlayer.getTeam() == Teams.BLUE ? Teams.RED : Teams.BLUE).values()) {
                
                otherCrPlayer.getPlayer().hidePlayer(player);
                
            }
            
        } else if (!event.isSneaking()) {
            
            plugin.getLogger().info("Assassin stopped sneaking.");
            
            assassinReveal(player);
            
        }
        
    }
    
    public void assassinReveal(Player player) {
        
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        plugin.getLogger().info("Assassin lost invisibility.");
        
        for (CastleRaidPlayer otherCrPlayer : plugin.getPlayersOfTeam(crPlayer.getTeam() == Teams.BLUE ? Teams.RED : Teams.BLUE).values()) {
            
            otherCrPlayer.getPlayer().showPlayer(player);
            
        }
        
    }
    
    @EventHandler
    public void onAssassinDamaged(EntityDamageEvent event) {
        
        if (!(event.getEntity() instanceof Player)) return;
        
        Player player = (Player) event.getEntity();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Assassin)) {
            return;
        }
        
        if (event.getCause() == DamageCause.FALL) {
            event.setDamage(0);
        }
        
        assassinReveal(player);
        
    }
    
    @EventHandler
    public void onAssassinDamager(EntityDamageByEntityEvent event) {
        
        if (!(event.getDamager() instanceof Player)) return;
        
        Player player = (Player) event.getDamager();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Assassin)) {
            return;
        }
        
        assassinReveal(player);
        
    }
    
}
