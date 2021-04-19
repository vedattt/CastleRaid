package xyz.vedat.castleraid.event;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.Builder;
import xyz.vedat.castleraid.classes.Builder.Claymore;
import xyz.vedat.castleraid.classes.Builder.ClaymoreType;

public class CastleRaidBuilderClaymoreEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidBuilderClaymoreEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onTrapSteppedOn(PlayerInteractEvent event) {
        
        if (event.getAction() != Action.PHYSICAL || plugin.getBuilderClaymores().containsKey(event.getClickedBlock().getLocation())) {
            return;
        }
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        Claymore claymore = plugin.getBuilderClaymores().get(event.getClickedBlock().getLocation());
        
        if (claymore.CR_PLAYER.getTeam() == crPlayer.getTeam() || crPlayer.getTeam() == Teams.SPECTATOR) {
            return;
        }
        
        if (claymore.TYPE == ClaymoreType.EXPLOSIVE) {
            
            player.getWorld().createExplosion(player.getLocation(), 0F, false);
            ((Damageable) player).damage(35, claymore.CR_PLAYER.getPlayer());
            
        } else if (claymore.TYPE == ClaymoreType.TOXIC) {
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (7.5 * 20), 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (7.5 * 20), 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) (7.5 * 20), 2));
            
        }
        
    }
    
    @EventHandler
    public void onTrapPlaced(BlockPlaceEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (event.getBlock().getType() != Material.WOOD_PLATE || event.getBlock().getType() != Material.IRON_PLATE || !(crPlayer.getCrClass() instanceof Builder)) {
            return;
        }
        
        ClaymoreType type = event.getBlock().getType() == Material.WOOD_PLATE ? ClaymoreType.TOXIC : ClaymoreType.EXPLOSIVE;
        
        plugin.getBuilderClaymores().put(event.getBlock().getLocation(), new Claymore(crPlayer, type));
        
    }
    
}
