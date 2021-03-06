package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Juggernaut extends CastleRaidClass {
  
  private static final int PRICE = 750;
  private static final int MAX_HP = 80;
  
  public Juggernaut() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_SWORD )
      .setItemName(ChatColor.AQUA + getClass().getSimpleName() + "'s Broadsword")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Block to become immune to almost all kinds of damage")
      .setUnbreakable(true)
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_BOOTS )
      .setItemName(ChatColor.AQUA + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_LEGGINGS )
      .setItemName(ChatColor.AQUA + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_CHESTPLATE )
      .setItemName(ChatColor.AQUA + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_CHESTPLATE )
      .setItemName(ChatColor.AQUA + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Melee Tank with incredible defense", "", ClassItemFactory.getItemNameColor() + "+ Broadsword", ClassItemFactory.getDescriptionColor() + "Block to become immune to almost all kinds of damage")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    return potionEffects;
    
  }
  
}
