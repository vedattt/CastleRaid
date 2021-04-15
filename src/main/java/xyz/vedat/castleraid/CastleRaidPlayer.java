package xyz.vedat.castleraid;

import java.util.function.BiConsumer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import xyz.vedat.castleraid.classes.CastleRaidClass;

public class CastleRaidPlayer {
  
  private Player player;
  private CastleRaidClass crClass;
  private BukkitTask sprintTask;
  
  public CastleRaidPlayer(Player player, CastleRaidClass crClass) {
    
    this.player = player;
    setCrClass(crClass);
    this.sprintTask = null;
    
    
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public CastleRaidClass getCrClass() {
    return crClass;
  }
  
  public boolean setCrClass(CastleRaidClass crClass) {
    
    this.crClass = crClass;
    
    crClass.getItems().forEach(new BiConsumer<Integer, ItemStack>(){
      
      @Override
      public void accept(Integer index, ItemStack item) {
        
        player.getInventory().setItem(index, item);
        
      }
      
    });
    
    return true;
    
  }
  
  public boolean hasOngoingSprintEvent() {
    return sprintTask != null;
  }
  
  public void setOngoingSprintEvent(BukkitTask sprintTask) {
    
    if (sprintTask == null && this.sprintTask != null) {
      this.sprintTask.cancel();
    }
    
    this.sprintTask = sprintTask;
    
  }
  
}
