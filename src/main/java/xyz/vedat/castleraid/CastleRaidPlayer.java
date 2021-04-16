package xyz.vedat.castleraid;

import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import xyz.vedat.castleraid.CastleRaidMain.teams;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.classes.ClassPickerItem;
import xyz.vedat.castleraid.interfaces.HealthModifiable;

public class CastleRaidPlayer {
  
  private Player player;
  private CastleRaidClass crClass;
  private BukkitTask sprintTask;
  private CastleRaidMain.teams team;
  
  public CastleRaidPlayer(Player player, CastleRaidClass crClass, CastleRaidMain.teams team) {
    
    this.player = player;
    setCrClass(crClass);
    this.sprintTask = null;
    this.team = team;
    
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public CastleRaidClass getCrClass() {
    return crClass;
  }
  
  public boolean setCrClass(CastleRaidClass crClass) {
    
    if (crClass == null) {
      return false;
    }
    
    this.crClass = crClass;
    
    ItemStack headWool = player.getInventory().getHelmet();
    
    player.getInventory().clear();
    
    player.getInventory().setHelmet(headWool);
    
    crClass.getItems().forEach(new BiConsumer<Integer, ItemStack>(){
      
      @Override
      public void accept(Integer index, ItemStack item) {
        
        player.getInventory().setItem(index, item);
        
      }
      
    });
    
    player.getInventory().setItem(8, ClassPickerItem.getClassPickerItem());
    
    ((Attributable) player).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(((HealthModifiable) crClass).getHp());
    
    player.setHealth(((HealthModifiable) crClass).getHp());
    
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
  
  public CastleRaidMain.teams getTeam() {
    return team;
  }
  
  public boolean setTeam(CastleRaidMain.teams team) {
    
    this.team = team;
    this.crClass = null;
    
    String woolKey = (team == teams.SPECTATOR ? "WHITE" : team.toString()) + "_WOOL";
    
    player.getInventory().setHelmet(new ItemStack(Material.valueOf(woolKey)));
    
    return true;
    
  }
  
}
