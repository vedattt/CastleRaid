package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Healer Wand")
      .setItemLore("Trusty wand of a nature mage.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a nature mage.")
      .setUnbreakable(true)
    ));
    
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_BOOTS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_LEGGINGS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.LEATHER_CHESTPLATE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.RED_ROSE )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Support class with great ranged healing")
    );
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600, 0));
    potionEffects.add(new PotionEffect(PotionEffectType.REGENERATION, 600, 2));
    
    return potionEffects;
    
  }
  
}
