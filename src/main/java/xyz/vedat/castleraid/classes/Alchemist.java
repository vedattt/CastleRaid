package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Alchemist extends CastleRaidClass {
  
  public static final int PRICE = 1500;
  public static final int MAX_HP = 30;
  
  private long alchemistCooldown;
  
  public Alchemist() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.STICK )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Wither Wand")
      .setItemLore("Trusty wand of an alchemist.")
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
      new ClassItemFactory.ClassItemData( Material.POTION )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
  @Override
  public long getCooldownDuration(CastleRaidCooldown cooldown) {
    if (cooldown == CastleRaidCooldown.MAGE_WAND) {
      return alchemistCooldown;
    }
    return 0;
  }
  
  @Override
  public boolean isOnCooldown(CastleRaidCooldown cooldown) {
    if (cooldown == CastleRaidCooldown.MAGE_WAND) {
      return System.currentTimeMillis() < alchemistCooldown;
    }
    return false;
  }
  
  @Override
  public void setOnCooldown(CastleRaidCooldown cooldown, boolean cooldownState) {
    if (cooldown == CastleRaidCooldown.MAGE_WAND) {
      this.alchemistCooldown = System.currentTimeMillis() + alchemistCooldown;
    }
  }
  
}
