package xyz.vedat.castleraid.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import xyz.vedat.castleraid.interfaces.HealthModifiable;

public class NatureMage extends CastleRaidClass implements HealthModifiable {
  
  private static final int PRICE = 750;
  private static final int HP = 50;
  
  private static ItemStack sword;
  
  public NatureMage() {
    
    super();
    
    sword = new ItemStack(Material.WOODEN_SWORD);
    ItemMeta swordMeta = sword.getItemMeta();
    swordMeta.setUnbreakable(true);
    swordMeta.setDisplayName("Archer's Sword");
    ArrayList<String> swordLore = new ArrayList<String>();
    swordLore.add("Mediocre sword of an archer.");
    swordMeta.setLore(swordLore);
    sword.setItemMeta(swordMeta);
    
    items.put(0, sword);
    items.put(1, new ItemStack(Material.BOW));
    items.put(2, new ItemStack(Material.ARROW, 64));
    
    
  }
  
  public static ItemStack getClassSymbol() {
    
    ItemStack classSymbol = new ItemStack(Material.POPPY);
    ItemMeta symbolMeta = classSymbol.getItemMeta();
    symbolMeta.setDisplayName("Nature Mage");
    ArrayList<String> symbolLore = new ArrayList<String>();
    symbolLore.add(PRICE + " coins");
    symbolLore.add("Description of class.");
    symbolMeta.setLore(symbolLore);
    classSymbol.setItemMeta(symbolMeta);
    
    return classSymbol;
    
  }
  
  public int getPrice() {
    return NatureMage.PRICE;
  }
  
  public int getHp() {
    return NatureMage.HP;
  }
  
}
