package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of an archer.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BOW )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Bow")
      .setItemLore("Trusty bow of an archer.")
      .addEnchantment(Enchantment.ARROW_INFINITE, 10)
      .setUnbreakable(true)
    ));
    
    items.put(2, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.ARROW )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Arrow")
      .setItemLore("Trusty arrow of an archer.")
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
      new ClassItemFactory.ClassItemData( Material.BOW )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Ranged crowd control class")
    );
    
  }

  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600, 0));
    
    return potionEffects;
    
  }
  
}
