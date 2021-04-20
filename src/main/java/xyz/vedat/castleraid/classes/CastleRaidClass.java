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
  protected HashMap<CastleRaidCooldown, Long> cooldownDurations;
  protected HashMap<CastleRaidCooldown, Long> activeCooldowns;
  
  public CastleRaidClass(int price, int maxHp) {
    
    this.PRICE = price;
    this.MAX_HP = maxHp;
    
    this.items = new HashMap<>();
    this.cooldownDurations = new HashMap<>();
    this.activeCooldowns = new HashMap<>();
    
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
  
  // COOLDOWN METHODS
  // Add mappings to the `cooldownDurations` HashMap from inside the constructors of subclasses.
  // The cooldowns are specified in millisecond format.
  // Example: cooldownDurations.put(CastleRaidCooldown.MAGE_WAND, 2000)
  
  /**
   * Get the cooldown's duration in milliseconds.
   * @param cooldown type of cooldown
   * @return duration of cooldown in milliseconds
   */
  public long getCooldownDuration(CastleRaidCooldown cooldown) {
    return cooldownDurations.get(cooldown);
  }
  
  /**
   * Get the cooldown's duration in seconds, rounded to two decimal places.
   * Use only for displaying duration, the number may not be accurate.
   * @param cooldown type of cooldown
   * @return duration of cooldown in seconds
   */
  public double getCooldownDurationInSecs(CastleRaidCooldown cooldown) {
    return Math.round(getCooldownDuration(cooldown) / 1000.0 * 100.0) / 100.0;
  }
  
  /**
   * Get the cooldown's duration in Minecraft ticks.
   * 20 MC ticks = 1 second
   * @param cooldown type of cooldown
   * @return duration of cooldown in ticks
   */
  public long getCooldownDurationInTicks(CastleRaidCooldown cooldown) {
    return getCooldownDuration(cooldown) / 20000;
  }
  
  /**
   * Get remaining cooldown in milliseconds.
   * @param cooldown type of cooldown
   * @return remaining duration of cooldown in milliseconds
   */
  public long getRemainingCooldown(CastleRaidCooldown cooldown) {
    
    if (isOnCooldown(cooldown)) {
      return activeCooldowns.get(cooldown) - System.currentTimeMillis();
    }
    
    return 0;
    
  }
  
  /**
   * Get the cooldown's remaining duration in seconds, rounded to two decimal places.
   * Use only for displaying duration, the number may not be accurate.
   * @param cooldown type of cooldown
   * @return remaining duration of cooldown in seconds
   */
  public double getRemainingCooldownInSecs(CastleRaidCooldown cooldown) {
    return Math.round(getRemainingCooldown(cooldown) / 1000.0 * 100.0) / 100.0;
  }
  
  /**
   * Communicates whether the queried cooldown has expired or not.
   * @param cooldown type of cooldown
   * @return {@code true} if latest cooldown duration has not expired yet
   */
  public boolean isOnCooldown(CastleRaidCooldown cooldown) {
    return activeCooldowns.containsKey(cooldown) && activeCooldowns.get(cooldown) > System.currentTimeMillis();
  }
  
  /**
   * Sets the cooldown state of the given type of cooldown.
   * @param cooldown type of cooldown
   * @param cooldownState {@code true} to set new cooldown from current time, {@code false} to remove existing cooldown
   */
  public void setOnCooldown(CastleRaidCooldown cooldown, boolean cooldownState) {
    
    if (cooldownState) {
      
      activeCooldowns.put(cooldown, getCooldownDuration(cooldown) + System.currentTimeMillis());
      
    } else {
      
      activeCooldowns.remove(cooldown);
      
    }
    
  }
  
  /**
   * Sets a cooldown starting from the current time.
   * @param cooldown type of cooldown
   */
  public void setOnCooldown(CastleRaidCooldown cooldown) {
    
    setOnCooldown(cooldown, true);
    
  }
  
  public HashMap<CastleRaidCooldown, Long> getCooldownDurations() {
      return cooldownDurations;
  }
  
}
