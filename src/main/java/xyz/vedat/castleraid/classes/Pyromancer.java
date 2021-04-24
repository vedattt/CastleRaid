package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Pyromancer extends CastleRaidClass {
  
  private static final int PRICE = 1500;
  private static final int MAX_HP = 50;
  
  public Pyromancer() {
    
    super(PRICE, MAX_HP);
    
    cooldownDurations.put(CastleRaidCooldown.PYROMANCER_FLAMETHROWER, 200L);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.FLINT_AND_STEEL )
      .setItemName(ChatColor.GOLD + getClass().getSimpleName() + "'s Flamethrower")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Sprays flames in a wide area")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.GOLD + getClass().getSimpleName() + "'s Sword")
      .setItemLore(ClassItemFactory.getDescriptionColor() + "For when the maniac pauses his rage")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.FLINT_AND_STEEL )
      .setItemName(ChatColor.GOLD + getClass().getSimpleName())
      .setItemLore(ClassItemFactory.getDescriptionColor() + "Offensive class that covers wide areas \nin fire", "", ClassItemFactory.getItemNameColor() + "+ Flamethrower", ClassItemFactory.getDescriptionColor() + "Sprays flames in a wide area")
      .setItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE)
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0));
    potionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 600 * 20, 9));
    
    return potionEffects;
    
  }
  
}
