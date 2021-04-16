package xyz.vedat.castleraid.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class CastleRaidHungerEvent implements Listener {
    
    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        
        event.setFoodLevel(20);
        event.setCancelled(true);
        
    }
    
}
