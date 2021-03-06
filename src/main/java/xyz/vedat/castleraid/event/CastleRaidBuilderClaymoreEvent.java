package xyz.vedat.castleraid.event;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
// import org.bukkit.scheduler.BukkitRunnable;

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
        
        if (event.getAction() != Action.PHYSICAL || !plugin.getAllBuilderClaymores().containsKey(event.getClickedBlock().getLocation())) {
            return;
        }
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        Claymore claymore = plugin.getAllBuilderClaymores().get(event.getClickedBlock().getLocation());
        Location loc = event.getClickedBlock().getLocation();
        
        if (claymore.CR_PLAYER.getTeam() == crPlayer.getTeam() || crPlayer.getTeam() == Teams.SPECTATOR) {
            return;
        }
        
        if (claymore.TYPE == ClaymoreType.EXPLOSIVE) {
            
            player.getWorld().createExplosion(player.getLocation(), 0F, false);
            player.damage(130, claymore.CR_PLAYER.getPlayer());
            
            plugin.getGameWorld().createExplosion(event.getClickedBlock().getLocation(), 1.4f, false);
            
        } else if (claymore.TYPE == ClaymoreType.TOXIC) {
            
            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, (int) (7.5 * 20), 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (7.5 * 20), 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) (7.5 * 20), 2));
            
            plugin.getGameWorld().playEffect(player.getLocation(), Effect.PARTICLE_SMOKE, 1);
            
        }
        
        // new BukkitRunnable() {
            
        //     @Override
        //     public void run() {
                event.setCancelled(true);
                plugin.getGameWorld().getBlockAt(loc).setType(Material.AIR);
                plugin.getGameWorld().playSound(loc, Sound.CLICK, 1f, 1f);
        //     }
            
        // }.runTaskLater(plugin, 10L);
        
    }
    
    @EventHandler
    public void onTrapPlaced(BlockPlaceEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if ((event.getBlock().getType() != Material.WOOD_PLATE && event.getBlock().getType() != Material.STONE_PLATE) || !(crPlayer.getCrClass() instanceof Builder)) {
            return;
        }
        
        if (!player.getItemInHand().isSimilar(crPlayer.getCrClass().getClassItems().get(3)) &&
            !player.getItemInHand().isSimilar(crPlayer.getCrClass().getClassItems().get(4))) {
            return;
        }
        
        ClaymoreType type = event.getBlock().getType() == Material.WOOD_PLATE ? ClaymoreType.TOXIC : ClaymoreType.EXPLOSIVE;
        
        plugin.getLogger().info("Builder3: " + event.getBlock().getLocation());
        plugin.getAllBuilderClaymores().put(event.getBlock().getLocation(), new Claymore(crPlayer, type));
        
    }
    
}
