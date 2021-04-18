package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public abstract class CastleRaidClass {
  
  public static enum Classes {
    ALCHEMIST, ARCHER, ASSASSIN, BERSERKER,
    BUILDER, JUGGERNAUT, KNIGHT, MAGE,
    NATUREMAGE, SENTRY, SNIPER, SPY,
    TIMEWIZARD
  }
  
  protected final int PRICE;
  protected final int MAX_HP;
  
  protected HashMap<Integer, ItemStack> items;
  
  public CastleRaidClass(int price, int maxHp) {
    
    this.PRICE = price;
    this.MAX_HP = maxHp;
    
    this.items = new HashMap<>();
    
  }
  
  public abstract HashMap<Integer, ItemStack> getClassItems();
  public abstract ItemStack getClassSymbolItem();
  
  public int getPrice() {
    return PRICE;
  }
  
  public int getMaxHP() {
    return MAX_HP;
  }
  
  public void setBoots(ItemStack armor) {
    items.put(36, armor);
  }
  
  public void setLeggings(ItemStack armor) {
    items.put(37, armor);
  }
  
  public void setChestplate(ItemStack armor) {
    items.put(38, armor);
  }
  
}
