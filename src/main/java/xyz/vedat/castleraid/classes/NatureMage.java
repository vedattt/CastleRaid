package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NatureMage extends CastleRaidClass {
  
  private static final int PRICE = 750;
  private static final int MAX_HP = 50;
  
  public NatureMage() {
    
    super(PRICE, MAX_HP);
    
    cooldownDurations.put(CastleRaidCooldown.NATUREMAGE_WAND, 2000L);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BONE )
      .setItemName(ChatColor.GREEN + getClass().getSimpleName() + "'s Healer Wand")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Heals enemies and himself")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.GREEN + getClass().getSimpleName() + "'s Sword")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Not the most effective weapon")
      .setUnbreakable(true)
    ));
    
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.GREEN + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.GREEN + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.GREEN + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.RED_ROSE )
      .setItemName(ChatColor.GREEN + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Support class with great ranged healing", "", ClassItemFactory.getItemNameColor() + "+ Nature Wand", ClassItemFactory.getDescriptionColor() + "Can heal enemies and himself")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0));
    potionEffects.add(new PotionEffect(PotionEffectType.REGENERATION, 600 * 20, 2));
    
    return potionEffects;
    
  }
  
}
