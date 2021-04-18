package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import xyz.vedat.castleraid.interfaces.SprintAccelerable;

public class Knight extends CastleRaidClass implements SprintAccelerable {
  
  public static final float MAX_WALK_SPEED = 0.58f;
  public static final float DEFAULT_WALK_SPEED = 0.2f;
  public static final double ACCELERATION_RATE = 1.04;
  
  public static final int PRICE = 0;
  public static final int MAX_HP = 50;
  
  public Knight() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a knight.")
      .setUnbreakable(true)
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_BOOTS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_LEGGINGS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_CHESTPLATE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
  @Override
  public float getMaxSpeed() {
    return MAX_WALK_SPEED;
  }
  
  @Override
  public float getDefaultSpeed() {
    return DEFAULT_WALK_SPEED;
  }
  
  @Override
  public double getAccelerationRate() {
    return ACCELERATION_RATE;
  }
  
}
