package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class Alchemist extends CastleRaidClass {
  
  public static final int PRICE = 1500;
  public static final int MAX_HP = 30;
  
  public Alchemist() {
    
    super(PRICE, MAX_HP);
    
    cooldownDurations.put(CastleRaidCooldown.ALCHEMIST_WAND, 700L);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.STICK )
      .setItemName(ChatColor.DARK_GREEN + getClass().getSimpleName() + "'s Wither Wand")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Shoot a skull that withers, slows, and weakens enemies")
    ));
    
    Potion potion = new Potion(1);
    potion.setSplash(true);
    potion.setType(PotionType.INSTANT_HEAL);
    potion.setLevel(2);
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.POTION )
      .setAmount(10)
      .setItemName(ChatColor.DARK_GREEN + getClass().getSimpleName() + "'s Healing Potion")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Heal allies and yourself")
    ));
    
    potion.apply(items.get(1));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.DARK_GREEN + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.DARK_GREEN + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.DARK_GREEN + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  @Override
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.POTION )
      .setItemName(ChatColor.DARK_GREEN + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Support/Damage that can apply debuffs", "", ClassItemFactory.getItemNameColor() + "+ Wither Wand", ClassItemFactory.getDescriptionColor() + "Shoot a skull that withers, slows, and weakens enemies", ClassItemFactory.getItemNameColor() + "+ Alchemy Potion", ClassItemFactory.getDescriptionColor() + "Heal allies and yourself")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS)
    );
    
  }

  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20 * 20, 0));
    
    return potionEffects;
    
  }
  
}
