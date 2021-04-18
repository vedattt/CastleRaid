package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Berserker extends CastleRaidClass {
  
  private static final int PRICE = 1500;
  private static final int MAX_HP = 20;
  
  public Berserker() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_AXE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Axe")
      .setItemLore("Trusty axe of a berserker.")
      .setUnbreakable(true)
    ));
    
    items.put(6, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.REDSTONE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Blood")
      .setItemLore("Kill count of the berserker.")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_AXE )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
}
