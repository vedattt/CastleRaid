package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TimeWizard extends CastleRaidClass {
  
  private static final int PRICE = 3000;
  private static final int MAX_HP = 30;
  
  public TimeWizard() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BLAZE_POWDER )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Wand")
      .setItemLore("Trusty wand of a time wizard.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a time wizard.")
      .setUnbreakable(true)
    ));
    
    items.put(2, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.COAL )
      .setAmount(15)
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Coal")
      .setItemLore("Trusty coal? of a time wizard.")
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
      new ClassItemFactory.ClassItemData( Material.WATCH )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
}