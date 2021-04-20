package xyz.vedat.castleraid.classes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassItemFactory {
  
  public static ItemStack getClassPickerItem() {
    
    return ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WATCH )
      .setItemName(ChatColor.RED + "Class Picker")
    );
    
  }
  
  public static ItemStack getBuiltItem(ClassItemData itemData) {
    
    if (itemData.getItemMaterial() == Material.AIR) {
      return null;
    }
    
    ItemStack builtItem = new ItemStack(itemData.getItemMaterial(), itemData.getAmount());
    ItemMeta builtItemMeta = builtItem.getItemMeta();
    
    if (itemData.getItemName() != null) {
      builtItemMeta.setDisplayName(itemData.getItemName());
    }
    
    if (itemData.getItemLore() != null) {
      builtItemMeta.setLore(Arrays.asList(itemData.getItemLore()));
    }
    
    if (itemData.getItemFlags() != null) {
      builtItemMeta.addItemFlags(itemData.getItemFlags());
    }
    
    if (itemData.isUnbreakable()) {
      builtItemMeta.spigot().setUnbreakable(true);
    }
    
    builtItem.setItemMeta(builtItemMeta);
    
    if (itemData.getEnchantments() != null) {
      builtItem.addUnsafeEnchantments(itemData.getEnchantments());
    }
    
    if (itemData.getDurability() != -1) {
      builtItem.setDurability((short) itemData.getDurability());
    }
    
    return builtItem;
    
  }
  
  public static class ClassItemData {
    
    private final Material ITEM_MATERIAL;
    
    private int amount;
    private String itemName;
    private String[] itemLore;
    private Map<Enchantment, Integer> enchantments;
    private ItemFlag[] itemFlags;
    private boolean isUnbreakable;
    private int durability;
    
    public ClassItemData(Material itemMaterial) {
      
      this.ITEM_MATERIAL = itemMaterial;
      this.amount = 1;
      this.isUnbreakable = false;
      this.durability = -1;
      
    }
    
    public Material getItemMaterial() {
      return ITEM_MATERIAL;
    }
    
    public int getAmount() {
      return amount;
    }
    
    public String getItemName() {
      return itemName;
    }
    
    public String[] getItemLore() {
      return itemLore;
    }
    
    public Map<Enchantment, Integer> getEnchantments() {
      return enchantments;
    }
    
    public ItemFlag[] getItemFlags() {
      return itemFlags;
    }
    
    public boolean isUnbreakable() {
      return isUnbreakable;
    }
    
    public int getDurability() {
      return durability;
    }
    
    // SETTERS
    
    public ClassItemData setAmount(int amount) {
      this.amount = amount;
      return this;
    }
    
    public ClassItemData setItemName(String itemName) {
      this.itemName = itemName;
      return this;
    }
    
    public ClassItemData setItemLore(String... itemLore) {
      this.itemLore = itemLore;
      return this;
    }
    
    public ClassItemData setEnchantments(Map<Enchantment, Integer> enchantments) {
      this.enchantments = enchantments;
      return this;
    }
    
    public ClassItemData setItemFlags(ItemFlag[] itemFlags) {
      this.itemFlags = itemFlags;
      return this;
    }
    
    public ClassItemData setUnbreakable(boolean isUnbreakable) {
      this.isUnbreakable = isUnbreakable;
      return this;
    }
    
    public ClassItemData setDurability(int durability) {
      this.durability = durability;
      return this;
    }
    
    public ClassItemData addEnchantment(Enchantment enchantment, int level) {
      
      if (enchantments == null) {
        enchantments = new HashMap<>();
      }
      
      enchantments.put(enchantment, level);
      return this;
      
    }
    
  }
  
}
