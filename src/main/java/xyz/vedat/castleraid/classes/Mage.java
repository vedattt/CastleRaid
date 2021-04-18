package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Mage extends CastleRaidClass {
  
  private static final int PRICE = 2250;
  private static final int MAX_HP = 10;
  
  public Mage() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BLAZE_ROD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Magical Wand")
      .setItemLore("Trusty wand of a mage.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a mage.")
      .setUnbreakable(true)
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BLAZE_ROD )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
  
  
}
