package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import xyz.vedat.castleraid.interfaces.CanBackstab;

public class Spy extends CastleRaidClass implements CanBackstab {
  
  private static final int PRICE = 3000;
  private static final int MAX_HP = 20;
  
  public Spy() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a spy.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.SNOW_BALL )
      .setAmount(4)
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Smoke Grenade")
      .setItemLore("Trusty grenade of a spy.")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
  @Override
  public ItemStack getBackstabItem() {
    return getClassItems().get(0);
  }
  
}
