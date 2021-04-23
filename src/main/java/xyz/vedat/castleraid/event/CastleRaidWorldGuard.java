package xyz.vedat.castleraid.event;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import xyz.vedat.castleraid.CastleRaidMain;

public class CastleRaidWorldGuard implements Listener {
    
    private CastleRaidMain plugin;
    private BoundingBox[] guardedBoxes;
    private BoundingBox forestBoundingBox;
    
    public CastleRaidWorldGuard(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    public BoundingBox[] getGuardedBoxes() {
        return guardedBoxes;
    }
    
    public void updateBoxCache() {
        
        guardedBoxes = new BoundingBox[] {
            this.new BoundingBox(-422, 83, 331,   -432, 76, 341), // beacon room
            this.new BoundingBox(-395, 72, 512,   -447, 35, 559), // red camp
            this.new BoundingBox(-508, 18, 569,   -532, 1, 545), // lobby
            this.new BoundingBox(-532, 1, 251,    -318, 1, 569), // bedrock floor
            this.new BoundingBox(-396, 79, 404,   -395, 82, 403), // blue spawns from hereon
            this.new BoundingBox(-418, 80, 383,   -417, 83, 384),
            this.new BoundingBox(-478, 79, 404,   -477, 82, 403),
            this.new BoundingBox(-451, 78, 383,   -452, 80, 384),
            this.new BoundingBox(-428, 89, 341,   -427, 92, 342),
            this.new BoundingBox(-436, 94, 362,   -437, 97, 361),
            this.new BoundingBox(-419, 94, 361,   -418, 97, 362)
        };
        
        forestBoundingBox = new BoundingBox(-318, 51, 486,   -532, 65, 569);
        
    }
    
    @EventHandler
    public void onForestIgnited(BlockIgniteEvent event) {
        
        if (forestBoundingBox.containsBlock(event.getBlock())) {
            event.setCancelled(true);
            return;
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(EntityChangeBlockEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock()) && 
                !event.getBlock().getLocation().equals(new Location(plugin.getGameWorld(), -427, 83, 336))) { // Beacon cobble wall can be broken, so.
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(EntityBlockFormEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockBreakEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockPlaceEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(EntityExplodeEvent event) {
        
        for (Block block : event.blockList()) {
            
            for (BoundingBox boundingBox : guardedBoxes) {
            
                if (boundingBox.containsBlock(block)) {
                    event.setCancelled(true);
                    return;
                }
                
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockBurnEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockDamageEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockExplodeEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockFromToEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockIgniteEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    @EventHandler
    public void onBlockBroken(BlockSpreadEvent event) {
        
        for (BoundingBox boundingBox : guardedBoxes) {
            if (boundingBox.containsBlock(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
            
        }
        
    }
    
    public class BoundingBox {
        
        private Location startingLocation;
        private Location endingLocation;
        
        double minX, minY, minZ, maxX, maxY, maxZ;
        
        public BoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
            
            startingLocation = new Location(plugin.getGameWorld(), x1, y1, z1);
            endingLocation = new Location(plugin.getGameWorld(), x2, y2, z2);
            
            minX = Math.min(startingLocation.getX(), endingLocation.getX());
            minY = Math.min(startingLocation.getY(), endingLocation.getY());
            minZ = Math.min(startingLocation.getZ(), endingLocation.getZ());
            
            maxX = Math.max(startingLocation.getX(), endingLocation.getX());
            maxY = Math.max(startingLocation.getY(), endingLocation.getY());
            maxZ = Math.max(startingLocation.getZ(), endingLocation.getZ());
            
        }
        
        
        public boolean containsBlock(Block block) {
            
            Location blockLoc = block.getLocation();
            
            return minX <= blockLoc.getX() && blockLoc.getX() <= maxX &&
                   minY <= blockLoc.getY() && blockLoc.getY() <= maxY &&
                   minZ <= blockLoc.getZ() && blockLoc.getZ() <= maxZ;
            
        }
        
    }
    
}
