package xyz.vedat.castleraid.event;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
//import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.Sentry;

public class CastleRaidSentryTurretEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidSentryTurretEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    public void restoreSentryTurret(Player player, Sentry sentry, Vehicle minecart) {
        
        if (minecart != null) {
            minecart.remove();
        }
        
        if (minecart.isInsideVehicle()) {
            minecart.getVehicle().remove();
        }
        
        sentry.getTurretBlock().setType(Material.AIR);
        sentry.setTurretBlock(null);
        
        int turretIndex = -1;
        
        for (int i = 0; i < player.getInventory().getContents().length; i++) {
            
            if (player.getInventory().getContents()[i].isSimilar(new ItemStack(Material.IRON_HOE))) {
                turretIndex = i;
                break;
            }
            
        }
        
        player.getInventory().setItem(turretIndex, sentry.getClassItems().get(0));
        
        sentry.setCooldown(CastleRaidCooldown.SENTRY_TURRET, 10000);
        
    }
    
    @EventHandler
    public void onSentryPlaceTurret(BlockPlaceEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Sentry)) {
            return;
        }
        
        Sentry sentry = (Sentry) crPlayer.getCrClass();
        
        if (sentry.getTurretBlock() != null || event.getBlockPlaced().getRelative(0, 1, 0).getType() != Material.AIR || 
            event.getBlockPlaced().getRelative(0, 2, 0).getType() != Material.AIR ||
            event.getBlockPlaced().getType() == Material.TNT) {
            
            event.setCancelled(true);
            return;
            
        }
        
        if (sentry.isOnCooldown(CastleRaidCooldown.SENTRY_TURRET)) {
            long number = sentry.getCooldown(CastleRaidCooldown.SENTRY_TURRET) / 1000;
            plugin.getLogger().info("Sentry " + player.getDisplayName() + " is on cooldown. (" + number + " Seconds)");
            event.setCancelled(true);
            return;
        }
        
        if (event.getBlockPlaced().getType() == Material.FENCE && !sentry.isOnCooldown(CastleRaidCooldown.SENTRY_TURRET)) {
            
            sentry.setTurretBlock(event.getBlockPlaced());
            
            Minecart turret = (Minecart) plugin.getGameWorld().spawnEntity(event.getBlockPlaced().getLocation().add(0.5, 1.5, 0.5), EntityType.MINECART);
            
            ArmorStand armorStand = (ArmorStand) plugin.getGameWorld().spawnEntity(event.getBlockPlaced().getLocation().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
            
            armorStand.setPassenger(turret);
            armorStand.setGravity(false);
            
            turret.setPassenger(player);
            
            int turretIndex = -1;
            
            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                
                if (player.getInventory().getContents()[i].isSimilar(sentry.getClassItems().get(0))) {
                    turretIndex = i;
                    break;
                }
                
            }
            
            player.getInventory().setItem(turretIndex, new ItemStack(Material.IRON_HOE));
            
        }
        
    }
    
    @EventHandler 
    public void onSentryLeaveTurret(VehicleExitEvent event) {
        
        if (!(event.getExited() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getExited();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Sentry)) {
            return;
        }
        
        Sentry sentry = (Sentry) crPlayer.getCrClass();
        
        if (sentry.getTurretBlock() == null) {
            return;
        }
        
        restoreSentryTurret(player, sentry, event.getVehicle());
        
    }
    
    @EventHandler
    public void onSentryFireGun(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Sentry)) {
            return;
        }
        
        Sentry sentry = (Sentry) crPlayer.getCrClass();
        
        if ((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) &&
            player.getInventory().contains(Material.TNT, 1) && player.getItemInHand().isSimilar(new ItemStack(Material.IRON_HOE))) {
            
            int turretIndex = -1;
            ItemStack newTnt = sentry.getClassItems().get(1);
            
            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                
                if (player.getInventory().getContents()[i].isSimilar(newTnt)) {
                    turretIndex = i;
                    break;
                }
                
            }
            
            newTnt.setAmount(player.getInventory().getItem(turretIndex).getAmount() - 1);
            
            player.getInventory().setItem(turretIndex, newTnt);
            
            TNTPrimed tnt = (TNTPrimed) plugin.getGameWorld().spawnEntity(player.getEyeLocation(), EntityType.PRIMED_TNT);
            
            tnt.setVelocity(player.getLocation().getDirection().multiply(2));
            
            new BukkitRunnable() {
                
                @Override
                public void run() {
                    
                    if (tnt.isOnGround() || !tnt.isValid()) {
                        tnt.setFuseTicks(0);
                        cancel();
                    }
                    
                }
                
            }.runTaskTimer(plugin, 0L, 1L);
            
            player.getWorld().playSound(player.getLocation(), Sound.FUSE, 1, 1);
            
        } else if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                    player.getItemInHand().isSimilar(new ItemStack(Material.IRON_HOE))) {
            
            Arrow arrow = (Arrow) plugin.getGameWorld().spawnEntity(player.getEyeLocation().add(player.getEyeLocation().getDirection().normalize().multiply(
                player.getEyeLocation().getPitch() > 50 ? 2.5 : 1.5
            )), EntityType.ARROW);
            
            arrow.spigot().setDamage(16);
            arrow.setVelocity(player.getLocation().getDirection().multiply(6));
            arrow.setShooter(player);
            
            player.getWorld().playSound(player.getLocation(), Sound.SHOOT_ARROW, 1, 1);
            
        }
        
    }
    
    @EventHandler
    public void onSentryTurretStandShot(EntityDamageEvent event) {
        
        if (event.getEntityType() == EntityType.ARMOR_STAND && event.getEntity().getPassenger() != null && event.getEntity().getPassenger().getType() == EntityType.MINECART &&
            plugin.getCrPlayers().get(event.getEntity().getPassenger().getPassenger().getUniqueId()).getCrClass() instanceof Sentry /*&&
            ((Damageable) event.getEntity()).getHealth() == 0*/) { // The commented part was aiming to let the armor stand be hit more than once before it's broken
            
            restoreSentryTurret(
                plugin.getCrPlayers().get(event.getEntity().getPassenger().getPassenger().getUniqueId()).getPlayer(), 
                (Sentry) plugin.getCrPlayers().get(event.getEntity().getPassenger().getPassenger().getUniqueId()).getCrClass(), 
                (Vehicle) event.getEntity().getPassenger()
            );
            
        }
        
    }
    
    @EventHandler
    public void onSentryDeath(PlayerDeathEvent event) {
        
        Player player = event.getEntity();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!(crPlayer.getCrClass() instanceof Sentry)) {
            return;
        }
        
        restoreSentryTurret(player, (Sentry) crPlayer.getCrClass(), (Vehicle) player.getVehicle());
        
    }
    
}
