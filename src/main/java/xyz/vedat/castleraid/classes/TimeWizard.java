package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TimeWizard extends CastleRaidClass {
  
  private static final int PRICE = 3000;
  private static final int MAX_HP = 30;
  
  public TimeWizard() {
    
    super(PRICE, MAX_HP);
    
    cooldownDurations.put(CastleRaidCooldown.TIMEWIZARD_TELEPORT, 10000L);
    cooldownDurations.put(CastleRaidCooldown.TIMEWIZARD_INVINCIBILITY, 3000L);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.EYE_OF_ENDER )
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName() + "'s Eye of Teleportation")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Teleports to where you are looking")
      .setUnbreakable(true)
    ));
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_SWORD )
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName() + "'s Sword")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "The weapon of choice for the wizard")
      .setUnbreakable(true)
    ));
    
    items.put(2, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.COAL )
      .setAmount(15)
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName() + "'s Antimatter")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Makes you invincible for 3 seconds")
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WATCH )
      .setItemName(ChatColor.DARK_AQUA + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Melee ambush class with high mobility", "", ClassItemFactory.getItemNameColor() + "+ Eye of Teleportation", ClassItemFactory.getDescriptionColor() + "Teleport to where you are looking", ClassItemFactory.getItemNameColor() + "+ Antimatter", ClassItemFactory.getDescriptionColor() + "Makes you invincible for 3 seconds")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0));
    
    return potionEffects;
    
  }
  
}
