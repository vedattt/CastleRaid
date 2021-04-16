package xyz.vedat.castleraid.classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassPickerItem {
  
  public static ItemStack getClassPickerItem() {
    
    ItemStack item = new ItemStack(Material.CLOCK);
    ItemMeta meta = item.getItemMeta();
    
    meta.setDisplayName("Pick Class");
    item.setItemMeta(meta);
    
    return item;
    
  }
  
}
