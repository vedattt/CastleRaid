package xyz.vedat.castleraid.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Archer extends CastleRaidClass {
  
  private static ItemStack sword;
  
  public Archer() {
    
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
  
  
}
