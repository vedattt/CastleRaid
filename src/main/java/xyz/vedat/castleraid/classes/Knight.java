package xyz.vedat.castleraid.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.vedat.castleraid.interfaces.ClassAccelerable;

public class Knight extends CastleRaidClass implements ClassAccelerable {
  
  public static final float MAX_WALK_SPEED = 0.58f;
  public static final float DEFAULT_WALK_SPEED = 0.2f;
  public static final double ACCELERATION_RATE = 1.2;
  
  private static ItemStack sword;
  
  public Knight() {
    
    super();
    
    sword = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta swordMeta = sword.getItemMeta();
    swordMeta.setUnbreakable(true);
    swordMeta.setDisplayName("Knight's Sword");
    ArrayList<String> swordLore = new ArrayList<String>();
    swordLore.add("Trusty sword of a knight.");
    swordMeta.setLore(swordLore);
    sword.setItemMeta(swordMeta);
    
    items.put(0, sword);
    items.put(2, new ItemStack(Material.BOW));
    
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
  
  
  
}
