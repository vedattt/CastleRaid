package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
      .setItemName(ChatColor.LIGHT_PURPLE + getClass().getSimpleName() + "'s Knife")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Hit enemies in the back for an instant kill")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.SNOW_BALL )
      .setAmount(16)
      .setItemName(ChatColor.LIGHT_PURPLE + getClass().getSimpleName() + "'s Smoke Grenade")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Creates a large area of smoke, obscuring enemy vision, while preserving the spy's own.")
      .setUnbreakable(true)
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.AIR )
      .setItemName(ChatColor.LIGHT_PURPLE + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.AIR)
      .setItemName(ChatColor.LIGHT_PURPLE + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.AIR )
      .setItemName(ChatColor.LIGHT_PURPLE + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.LIGHT_PURPLE + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Stealth/Support class with smoke \ngrenades", "", ClassItemFactory.getItemNameColor() + "+ Backstabbing Knife", ClassItemFactory.getDescriptionColor() + "Hit enemies in the back for an instant kill", ClassItemFactory.getItemNameColor() + "+ Smoke Grenade", ClassItemFactory.getDescriptionColor() + "Creates a large area of smoke, obscuring enemy vision, while preserving the spy's own.")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 2));
    
    return potionEffects;
    
  }
  
  @Override
  public ItemStack getBackstabItem() {
    return getClassItems().get(0);
  }
  
}
