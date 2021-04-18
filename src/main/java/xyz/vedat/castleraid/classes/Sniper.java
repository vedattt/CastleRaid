package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.vedat.castleraid.interfaces.BowArrowSpeedable;

public class Sniper extends CastleRaidClass implements BowArrowSpeedable {
  
  final double MAX_VELOCITY_MULTIPLIER = 6;
  final double BAD_VELOCITY_MULTIPLIER = 1.5;
  
  private static final int PRICE = 750;
  private static final int MAX_HP = 20;
  
  public Sniper() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BOW )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Bow")
      .setItemLore("Trusty bow of a sniper.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.ARROW )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Arrow")
      .setItemLore("Trusty arrow of a sniper.")
      .setAmount(50)
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
    
    ItemStack classSymbol = new ItemStack(Material.ARROW);
    ItemMeta symbolMeta = classSymbol.getItemMeta();
    symbolMeta.setDisplayName(ChatColor.RED + "Sniper");
    ArrayList<String> symbolLore = new ArrayList<String>();
    symbolLore.add(PRICE + " coins");
    symbolLore.add("Description of class.");
    symbolMeta.setLore(symbolLore);
    classSymbol.setItemMeta(symbolMeta);
    
    return classSymbol;
    
  }

  @Override
  public double getMaxVelocityMultiplier() {
    return MAX_VELOCITY_MULTIPLIER;
  }

  @Override
  public double getBadVelocityMultiplier() {
    return BAD_VELOCITY_MULTIPLIER;
  }
  
}
