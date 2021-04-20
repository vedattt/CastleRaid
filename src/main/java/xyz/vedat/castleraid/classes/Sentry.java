package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.ItemStack;

public class Sentry extends CastleRaidClass {
  
  private static final int PRICE = 2250;
  private static final int MAX_HP = 10;
  
  private Block turretBlock;
  private Minecart turret;
  
  public Sentry() {
    
    super(PRICE, MAX_HP);
    
    this.turretBlock = null;
    
    cooldownDurations.put(CastleRaidCooldown.SENTRY_TURRET, 10000L);
    
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
  
  public void restoreTurret(Player player, Vehicle minecart) {
          
      if (minecart != null) {
          minecart.remove();
      }
      
      if (minecart.isInsideVehicle()) {
          minecart.getVehicle().remove();
      }
      
      getTurretBlock().setType(Material.AIR);
      setTurretBlock(null);
      setTurret(null);
      
      int turretIndex = -1;
      
      for (int i = 0; i < player.getInventory().getContents().length; i++) {
          
          if (player.getInventory().getContents()[i].isSimilar(new ItemStack(Material.IRON_HOE))) {
              turretIndex = i;
              break;
          }
          
      }
      
      player.getInventory().setItem(turretIndex, getClassItems().get(0));
      
      setOnCooldown(CastleRaidCooldown.SENTRY_TURRET);
      
  }
  
  public void setTurret(Minecart turret) {
    this.turret = turret;
  }
  
  public Minecart getTurret() {
      return turret;
  }
  
}
