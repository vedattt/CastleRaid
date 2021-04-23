package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Berserker extends CastleRaidClass {
  
  private static final int PRICE = 1500;
  private static final int MAX_HP = 20;
  
  private int killCount;
  
  public Berserker() {
    
    super(PRICE, MAX_HP);
    
    this.killCount = 0;
    
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
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.AIR )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.AIR )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.AIR )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_AXE )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Becomes stronger the more he kills")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  public int getKillCount() {
    return killCount;
  }
  
  public void incrementKillCount() {
    this.killCount++;
  }
  
  public void resetKillCount() {
    this.killCount = 0;
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    return potionEffects;
    
  }
  
}
