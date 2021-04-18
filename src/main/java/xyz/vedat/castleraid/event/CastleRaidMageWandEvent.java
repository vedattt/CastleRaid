package xyz.vedat.castleraid.event;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.Mage;

public class CastleRaidMageWandEvent implements Listener {
    
    CastleRaidMain plugin;
        
    public CastleRaidMageWandEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    @SuppressWarnings("deprecation")
    public void onMageWand(PlayerInteractEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
        
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            return;
        }
        
        if (!(crPlayer.getCrClass() instanceof Mage) || event.getItem() == null || !event.getItem().isSimilar(crPlayer.getCrClass().getClassItems().get(0))) {
            return;
        }
        
        Mage mage = (Mage) crPlayer.getCrClass();
        
        if (mage.isOnCooldown(CastleRaidCooldown.MAGE_WAND)) {
            plugin.getLogger().info("Mage " + player.getDisplayName() + " is on cooldown.");
            return;
        } else {
            mage.setOnCooldown(CastleRaidCooldown.MAGE_WAND);
        }
        
        player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_LAUNCH, 1, 1);
        
        new BukkitRunnable() {
            
            Vector direction = player.getEyeLocation().getDirection();
            Location initialLoc = player.getEyeLocation();
            Location nextLocation = initialLoc.clone().add(direction.clone().multiply(1));
            ArrayList<CastleRaidPlayer> damageablePlayers;
            
            @Override
            public void run() {
                
                for (int j = 0; j < 15; j++) {
                    
                    boolean hasImpact = false;
                    
                    nextLocation = nextLocation.clone().add(direction.clone().multiply(0.8));
                    
                    if (plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid()) {
                        hasImpact = true;
                    }
                    
                    for (CastleRaidPlayer otherCrPlayer : plugin.getCrPlayers().values()) {
                        
                        if (nextLocation.distance(otherCrPlayer.getPlayer().getLocation()) < 1.5 && !crPlayer.equals(otherCrPlayer)) {
                            hasImpact = true;
                            
                            if (damageablePlayers == null) {
                                damageablePlayers = new ArrayList<>();
                            }
                            
                            damageablePlayers.add(otherCrPlayer);
                            
                        }
                        
                    } 
                    
                    for (int i = 0; i < 6; i++) {
                        
                        plugin.getGameWorld().spigot().playEffect(nextLocation.clone().add(Math.random(), Math.random(), Math.random()), Effect.FIREWORKS_SPARK, 0, 1, 0, 0, 0, 0.4f, 0, 32);
                        
                    }
                    
                    //plugin.getLogger().info("MageProjectile at " + nextLocation.toString());
                    
                    if (initialLoc.distance(nextLocation) > 24 || hasImpact) {
                        
                        plugin.getGameWorld().createExplosion(nextLocation, 0F, false);
                        
                        ArrayList<Block> blockList = new ArrayList<>();
                        ArrayList<FallingBlock> fallingBlockList = new ArrayList<>();
                        
                        for (int y = 1; y >= -1; y--) {
                            
                            for (int x = -1; x <= 1; x++) {
                                
                                for (int z = -1; z <= 1; z++) {
                                    
                                    if (nextLocation.getBlock().getRelative(x, y, z).getType() != Material.AIR /*&&
                                        nextLocation.distance(nextLocation.getBlock().getRelative(x, y, z).getLocation()) <= 3*/) {
                                        blockList.add(nextLocation.getBlock().getRelative(x, y, z));
                                    }
                                    
                                }
                                
                            }
                            
                        }
                        
                        if (damageablePlayers != null) {
                            
                            for (int i = 0; i < damageablePlayers.size(); i++) {
                                
                                ((Damageable) damageablePlayers.get(i).getPlayer()).damage(20, player);
                                
                            }
                            
                        }
                        
                        Material blockType;
                        byte blockData;
                        
                        for (int i = 0; i < blockList.size(); i++) {
                            
                            if (Math.random() > 0.68) {
                                
                                blockType = blockList.get(i).getType();
                                blockData = blockList.get(i).getData();
                                
                                blockList.get(i).setType(Material.AIR);
                                
                                fallingBlockList.add(plugin.getGameWorld().spawnFallingBlock(blockList.get(i).getLocation(), blockType, blockData));
                                fallingBlockList.get(fallingBlockList.size() - 1).setVelocity(blockList.get(i).getLocation().subtract(nextLocation).toVector().normalize().multiply(0.7).add(new Vector(0, 0.5, 0)));
                                fallingBlockList.get(fallingBlockList.size() - 1).setHurtEntities(false);
                                fallingBlockList.get(fallingBlockList.size() - 1).setDropItem(Math.random() > 0.6 ? true : false);
                                
                            } else {
                                
                                if (Math.random() > 0.65) {
                                    
                                    blockList.get(i).setType(Material.AIR);
                                    
                                }
                                
                            }
                            
                        }
                        
                        new BukkitRunnable(){
                            
                            @Override
                            public void run() {
                                
                                for (int i = fallingBlockList.size() - 1; i >= 0; i--) {
                                    FallingBlock fallingBlock = fallingBlockList.get(i);
                                    if (fallingBlock.isOnGround() || !fallingBlock.isValid()) {
                                        fallingBlock.getLocation().getBlock().setType(Material.AIR);
                                        fallingBlockList.remove(i);
                                    }
                                }
                                
                                if (fallingBlockList.size() == 0) {
                                    cancel();
                                }
                                
                            }
                            
                        }.runTaskTimer(plugin, 0L, 1L);
                        
                        cancel();
                        return;
                    }
                    
                }
                
            }
            
        }.runTaskTimer(plugin, 0L, 0L);
        
    }
    
}
