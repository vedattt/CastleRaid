package xyz.vedat.castleraid.event;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
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
import xyz.vedat.castleraid.CastleRaidMain.Teams;
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
            
            Vector direction = player.getEyeLocation().getDirection();
            Location initialLoc = player.getEyeLocation();
            Location nextLocation = initialLoc.clone().add(direction.clone().multiply(1));
            
            for (int j = 0; j < 10000; j++) { // Not really a great way... Maybe while (true)? Or something else?
                
                nextLocation = nextLocation.clone().add(direction.clone().multiply(0.8));
                
                boolean hasImpact = plugin.getGameWorld().getBlockAt(nextLocation).getType().isSolid() ||
                    !plugin.getGameWorld().getNearbyEntities(nextLocation, 1.5, 1.5, 1.5)
                        .stream().collect(ArrayList::new, new BiConsumer<ArrayList<Entity>, Entity>() {
                            @Override
                            public void accept(ArrayList<Entity> list, Entity entity) {
                                if (entity.getUniqueId() != player.getUniqueId() && entity instanceof Damageable) {
                                    list.add(entity);
                                }
                            }
                        }, ArrayList::addAll).isEmpty();
                
                for (int i = 0; i < 6; i++) {
                    
                    plugin.getGameWorld().spigot().playEffect(
                        nextLocation.clone().add(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5), // Particle Spawn Location
                        Effect.FIREWORKS_SPARK, 0, 1, // Effect Type - ID - Databit
                        (float) Math.random() - 0.5f, (float) Math.random() - 0.5f, (float) Math.random() - 0.5f, // Offset (x,y,z)
                        0.1f, 0, 32 // Speed - Particle Count - Radius
                    );
                    
                }
                
                //plugin.getLogger().info("MageProjectile at " + nextLocation.toString());
                
                if (initialLoc.distance(nextLocation) > 24 || hasImpact) {
                    
                    plugin.getGameWorld().createExplosion(nextLocation, 0F, false);
                    
                    ArrayList<Block> blockList = new ArrayList<>();
                    ArrayList<FallingBlock> fallingBlockList = new ArrayList<>();
                    
                    for (int y = 1; y >= -1; y--) {
                        
                        for (int x = -1; x <= 1; x++) {
                            
                            for (int z = -1; z <= 1; z++) {
                                
                                if (nextLocation.getBlock().getRelative(x, y, z).getType() != Material.AIR &&
                                    !nextLocation.getBlock().getRelative(x, y, z).isLiquid()) {
                                    blockList.add(nextLocation.getBlock().getRelative(x, y, z));
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                    for (Entity damagedEntity : plugin.getGameWorld().getNearbyEntities(nextLocation, 1.5, 1.5, 1.5)) {
                        
                        boolean ownTeam = false;
                        Teams team;
                        
                        if (damagedEntity instanceof Player) {
                            
                            team = plugin.getCrPlayers().get(damagedEntity.getUniqueId()).getTeam();
                            
                            if (team == crPlayer.getTeam() || team == Teams.SPECTATOR) {
                                ownTeam = true;
                            }
                            
                        }
                        
                        if (damagedEntity instanceof Damageable && !ownTeam) {
                            ((Damageable) damagedEntity).damage(20, player);
                        }
                        
                    }
                    
                    Material blockType;
                    byte blockData;

                    for (Block block : blockList) {

                        if (Math.random() > 0.68) {

                            blockType = block.getType();
                            blockData = block.getData();

                            block.setType(Material.AIR);

                            fallingBlockList.add(plugin.getGameWorld()
                                .spawnFallingBlock(block.getLocation(), blockType, blockData));
                            fallingBlockList.get(fallingBlockList.size() - 1).setVelocity(
                                block.getLocation().subtract(nextLocation).toVector().normalize()
                                    .multiply(0.7).add(new Vector(0, 0.5, 0)));
                            fallingBlockList.get(fallingBlockList.size() - 1)
                                .setHurtEntities(false);
                            fallingBlockList.get(fallingBlockList.size() - 1)
                                .setDropItem(Math.random() > 0.6);

                        } else {

                            if (Math.random() > 0.65) {

                                block.setType(Material.AIR);

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
                    
                    return;
                    
                }
                
            }
            
        
        
    }
    
}
