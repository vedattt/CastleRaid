package xyz.vedat.castleraid.event;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.Alchemist;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;

public class CastleRaidAlchemistWitherEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidAlchemistWitherEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onAlchemistWand(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        
        if (!(crPlayer.getCrClass() instanceof Alchemist) || event.getItem() == null || !event.getItem().isSimilar(crPlayer.getCrClass().getClassItems().get(0))) {
            return;
        }
        
        Alchemist alchemist = (Alchemist) crPlayer.getCrClass();
        
        if (alchemist.isOnCooldown(CastleRaidCooldown.ALCHEMIST_WAND)) {
            plugin.getLogger().info("Alchemist " + player.getDisplayName() + " is on cooldown.");
            return;
        } else {
            alchemist.setOnCooldown(CastleRaidCooldown.ALCHEMIST_WAND);
        }
        
        player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);
        
        WitherSkull witherSkull = player.launchProjectile(WitherSkull.class, player.getLocation().getDirection().normalize().multiply(0.25));
        
        witherSkull.setShooter(player);
        
    }
    
    @EventHandler
    public void onAlchemistWandHit(ProjectileHitEvent event) {
        
        if (!(event.getEntity().getShooter() instanceof Player)) return;
        
        Player player = (Player) event.getEntity().getShooter();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        
        if (!(crPlayer.getCrClass() instanceof Alchemist)) {
            return;
        }
        
        //Alchemist alchemist = (Alchemist) crPlayer.getCrClass();
        
        Projectile entity = event.getEntity();
        
        entity.getWorld().getNearbyEntities(entity.getLocation(), 1.5, 1.5, 1.5).forEach(
            entity1 -> {

                if (entity1 instanceof Player && entity1.getUniqueId() != player.getUniqueId()) {

                    Player player1 = (Player) entity1;

                    player1.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 50, 2));
                    player1.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 40, 1));

                } else if (entity1.getUniqueId() == player.getUniqueId()) {

                    player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 2, 2));

                }

            });
        
    }
    
}
