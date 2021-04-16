package xyz.vedat.castleraid.classes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.vedat.castleraid.interfaces.ClassAccelerable;
import xyz.vedat.castleraid.interfaces.HealthModifiable;

public class Knight extends CastleRaidClass implements ClassAccelerable, HealthModifiable {
  
  public static final float MAX_WALK_SPEED = 0.58f;
  public static final float DEFAULT_WALK_SPEED = 0.2f;
  public static final double ACCELERATION_RATE = 1.2;
  private static final int PRICE = 0;
  private static final int HP = 50;
  
  private static ItemStack sword;
  private static ItemStack chestplate;
  private static ItemStack leggings;
  private static ItemStack boots;
  
  public Knight() {
    
    super();
    
    sword = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta swordMeta = sword.getItemMeta();
    swordMeta.setUnbreakable(true);
    swordMeta.setDisplayName(ChatColor.RED + "Knight's Sword");
    ArrayList<String> swordLore = new ArrayList<String>();
    swordLore.add("Trusty sword of a knight.");
    swordMeta.setLore(swordLore);
    sword.setItemMeta(swordMeta);
    
    chestplate = new ItemStack(Material.IRON_CHESTPLATE);
    ItemMeta chestplateMeta = chestplate.getItemMeta();
    chestplateMeta.setUnbreakable(true);
    chestplateMeta.setDisplayName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate");
    chestplate.setItemMeta(chestplateMeta);
    
    leggings = new ItemStack(Material.IRON_LEGGINGS);
    ItemMeta leggingsMeta = leggings.getItemMeta();
    leggingsMeta.setUnbreakable(true);
    leggingsMeta.setDisplayName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings");
    leggings.setItemMeta(leggingsMeta);
    
    boots = new ItemStack(Material.IRON_BOOTS);
    ItemMeta bootsMeta = boots.getItemMeta();
    bootsMeta.setUnbreakable(true);
    bootsMeta.setDisplayName(ChatColor.RED + getClass().getSimpleName() + "'s Boots");
    boots.setItemMeta(bootsMeta);
    
    items.put(0, sword);
    items.put(36, boots);
    items.put(37, leggings);
    items.put(38, chestplate);
    
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
  
  public static ItemStack getClassSymbol() {
    
    ItemStack classSymbol = new ItemStack(Material.DIAMOND_SWORD);
    ItemMeta symbolMeta = classSymbol.getItemMeta();
    symbolMeta.setDisplayName("Knight");
    ArrayList<String> symbolLore = new ArrayList<String>();
    symbolLore.add(PRICE + " coins");
    symbolLore.add("Description of class.");
    symbolMeta.setLore(symbolLore);
    classSymbol.setItemMeta(symbolMeta);
    
    return classSymbol;
    
  }
  
  public int getPrice() {
    return Knight.PRICE;
  }
  
  public int getHp() {
    return Knight.HP;
  }
  
}
