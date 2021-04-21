package xyz.vedat.castleraid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import xyz.vedat.castleraid.interfaces.SprintAccelerable;

public class Knight extends CastleRaidClass implements SprintAccelerable {
  
  public static final float MAX_WALK_SPEED = 0.58f;
  public static final float DEFAULT_WALK_SPEED = 0.2f;
  public static final double ACCELERATION_RATE = 1.04;
  
  public static final int PRICE = 0;
  public static final int MAX_HP = 50;
  
  private BukkitTask sprintTask;
  
  public Knight() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a knight.")
      .setUnbreakable(true)
    ));
    
    setBoots(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_BOOTS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Boots")
      .setUnbreakable(true)
    ));
    
    setLeggings(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_LEGGINGS )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings")
      .setUnbreakable(true)
    ));
    
    setChestplate(ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.IRON_CHESTPLATE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate")
      .setUnbreakable(true)
    ));
    
    return items;
    
  }
  
  public ItemStack getClassSymbolItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.DIAMOND_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Very fast with good defense")
    );
    
  }
  
  @Override
  public float getMaxSpeed() {
    return MAX_WALK_SPEED;
  }
  
  @Override
  public float getDefaultSpeed() {
    return DEFAULT_WALK_SPEED;
  }
  
  @Override
  public double getAccelerationRate() {
    return ACCELERATION_RATE;
  }
  
  @Override
  public boolean hasOngoingSprintEvent() {
    return sprintTask != null;
  }
  
  @Override
  public void setOngoingSprintEvent(BukkitTask sprintTask) {
    
    if (sprintTask == null && this.sprintTask != null) {
      this.sprintTask.cancel();
    }
    
    this.sprintTask = sprintTask;
    
  }
  
  @Override
  public ArrayList<PotionEffect> getClassPotionEffects() {
    
    potionEffects.add(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 0));
    
    return potionEffects;
    
  }
  
}
