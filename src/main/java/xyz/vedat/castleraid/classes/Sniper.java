package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.vedat.castleraid.interfaces.BowArrowSpeedable;

public class Sniper extends CastleRaidClass implements BowArrowSpeedable {
  
  final double MAX_VELOCITY_MULTIPLIER = 8.5;
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
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Bow")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Incredible range and damage, but needs \nto be drawn fully to become deadly")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.ARROW )
      .setAmount(50)
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.ARROW )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Very long range burst damage", "", ClassItemFactory.getItemNameColor() + "+ Sniper Rifle", ClassItemFactory.getDescriptionColor() + "Incredible range and damage, but needs \nto be drawn fully to become deadly")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0));
    
    return potionEffects;
    
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
