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

public class Assassin extends CastleRaidClass implements CanBackstab {
  
  private static final int PRICE = 2250;
  private static final int MAX_HP = 16;
  
  public Assassin() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_SWORD )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Dagger")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Hit enemies in the back for an instant kill")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.FISHING_ROD )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Grappling Hook")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "The ultimate mobility item")
      .setUnbreakable(true)
    ));
    
    items.put(2, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.GLOWSTONE_DUST )
      .setAmount(10)
      .setItemName(ChatColor.GRAY + getClass().getSimpleName() + "'s Invisibility Dust")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Used when you crouch, turning you completely invisible")
      .setUnbreakable(true)
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
      new ClassItemFactory.ClassItemData( Material.FISHING_ROD )
      .setItemName(ChatColor.GRAY + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "High mobility stealth class", "", ClassItemFactory.getItemNameColor() + "+ Dagger", ClassItemFactory.getDescriptionColor() + "Hit enemies in the back for an instant kill", ClassItemFactory.getItemNameColor() + "+ Grappling Hook", ClassItemFactory.getDescriptionColor() + "The ultimate mobility item", ClassItemFactory.getItemNameColor() + "+ Invisibility Dust", ClassItemFactory.getDescriptionColor() + "Used when you crouch, turning you completely invisible")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }

  @Override
  public ItemStack getBackstabItem() {
    return getClassItems().get(0);
  }

  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0, false, false));
    
    return potionEffects;
    
  }
  
}
