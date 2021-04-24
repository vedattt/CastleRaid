package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Archer extends CastleRaidClass {
  
  private static final int PRICE = 0;
  private static final int MAX_HP = 20;
  
  public Archer() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName() + "'s Sword")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Mediocre sword for a mediocre swordsman.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BOW )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName() + "'s Bow")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Simple, yet packs a punch. Great for knocking enemies out of position.")
      .addEnchantment(Enchantment.ARROW_INFINITE, 10)
      .setUnbreakable(true)
      .addEnchantment(Enchantment.ARROW_KNOCKBACK, 2)
    ));
    
    items.put(2, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.ARROW )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName() + "'s Arrow")
      .setItemLore(ChatColor.ITALIC + "Mysterious...")
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BOW )
      .setItemName(ChatColor.YELLOW + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Ranged crowd control class", "", ClassItemFactory.getItemNameColor() + "+ Archer's Bow", ClassItemFactory.getDescriptionColor() + "Simple, yet packs a punch. Great for knocking enemies out of position.")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }

  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0));
    
    return potionEffects;
    
  }
  
}
