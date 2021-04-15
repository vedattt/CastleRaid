package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class CastleRaidClass {
  
  protected HashMap<Integer, ItemStack> items;
  
  public CastleRaidClass() {
    
    items = new HashMap<>();
    
  }
  
  public HashMap<Integer, ItemStack> getItems() {
    return items;
  }
  
}
