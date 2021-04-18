package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import xyz.vedat.castleraid.interfaces.Cooldownable;

public class Sentry extends CastleRaidClass implements Cooldownable {
  
  private static final int PRICE = 2250;
  private static final int MAX_HP = 10;
  
  private Block turretBlock;
  private long sentryTurretCooldown;
  
  public Sentry() {
    
    super(PRICE, MAX_HP);
    
    this.turretBlock = null;
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.FENCE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Turret")
      .setItemLore("Trusty turret of a sentry.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.TNT )
      .setAmount(30)
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s TNT")
      .setItemLore("Trusty TNT of a sentry.")
      .setUnbreakable(true)
    ));
    
    items.put(2, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.ARROW )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Arrow")
      .setItemLore("Trusty arrow of a sentry.")
      .addEnchantment(Enchantment.ARROW_INFINITE, 10)
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
      new ClassItemFactory.ClassItemData( Material.MINECART )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }
  
  public Block getTurretBlock() {
      return turretBlock;
  }
  
  public void setTurretBlock(Block turretBlock) {
      this.turretBlock = turretBlock;
  }

  @Override
  public long getCooldown(CastleRaidCooldown cooldown) {
    if (cooldown == CastleRaidCooldown.SENTRY_TURRET) {
      return sentryTurretCooldown - System.currentTimeMillis();
    }
    return 0;
  }

  @Override
  public boolean isOnCooldown(CastleRaidCooldown cooldown) {
    if (cooldown == CastleRaidCooldown.SENTRY_TURRET) {
      return System.currentTimeMillis() < sentryTurretCooldown;
    }
    return false;
  }

  @Override
  public void setCooldown(CastleRaidCooldown cooldown, long cooldownDuration) {
    if (cooldown == CastleRaidCooldown.SENTRY_TURRET) {
      sentryTurretCooldown = System.currentTimeMillis() + cooldownDuration;
    }
  }
}