package xyz.vedat.castleraid.classes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.vedat.castleraid.interfaces.HealthModifiable;

public class Sniper extends CastleRaidClass implements HealthModifiable {
  
  private static final int PRICE = 750;
  private static final int HP = 20;
  
  private static ItemStack sniperRifle;
  private static ItemStack chestplate;
  private static ItemStack leggings;
  private static ItemStack boots;
  
  public Sniper() {
    
    super();
    
    sniperRifle = new ItemStack(Material.BOW);
    ItemMeta sniperRifleMeta = sniperRifle.getItemMeta();
    sniperRifleMeta.setUnbreakable(true);
    sniperRifleMeta.setDisplayName(ChatColor.DARK_RED + "Sniper Rifle");
    ArrayList<String> sniperRifleLore = new ArrayList<String>();
    sniperRifleLore.add("Shoots pretty fast arrows.");
    sniperRifleMeta.setLore(sniperRifleLore);
    sniperRifle.setItemMeta(sniperRifleMeta);
    
    chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
    ItemMeta chestplateMeta = chestplate.getItemMeta();
    chestplateMeta.setUnbreakable(true);
    chestplateMeta.setDisplayName(ChatColor.RED + getClass().getSimpleName() + "'s Chestplate");
    chestplate.setItemMeta(chestplateMeta);
    
    leggings = new ItemStack(Material.LEATHER_LEGGINGS);
    ItemMeta leggingsMeta = leggings.getItemMeta();
    leggingsMeta.setUnbreakable(true);
    leggingsMeta.setDisplayName(ChatColor.RED + getClass().getSimpleName() + "'s Leggings");
    leggings.setItemMeta(leggingsMeta);
    
    boots = new ItemStack(Material.LEATHER_BOOTS);
    ItemMeta bootsMeta = boots.getItemMeta();
    bootsMeta.setUnbreakable(true);
    bootsMeta.setDisplayName(ChatColor.RED + getClass().getSimpleName() + "'s Boots");
    boots.setItemMeta(bootsMeta);
    
    items.put(0, sniperRifle);
    items.put(1, new ItemStack(Material.ARROW, 50));
    items.put(36, boots);
    items.put(37, leggings);
    items.put(38, chestplate);
    
  }
  
  public static ItemStack getClassSymbol() {
    
    ItemStack classSymbol = new ItemStack(Material.ARROW);
    ItemMeta symbolMeta = classSymbol.getItemMeta();
    symbolMeta.setDisplayName("Sniper");
    ArrayList<String> symbolLore = new ArrayList<String>();
    symbolLore.add(PRICE + " coins");
    symbolLore.add("Description of class.");
    symbolMeta.setLore(symbolLore);
    classSymbol.setItemMeta(symbolMeta);
    
    return classSymbol;
    
  }
  
  public int getPrice() {
    return Sniper.PRICE;
  }
  
  public int getHp() {
    return Sniper.HP;
  }
  
}
