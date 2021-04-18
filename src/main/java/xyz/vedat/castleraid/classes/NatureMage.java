package xyz.vedat.castleraid.classes;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.vedat.castleraid.interfaces.Cooldownable;

public class NatureMage extends CastleRaidClass implements Cooldownable {
  
  private static final int PRICE = 750;
  private static final int MAX_HP = 50;
  private long natureWandCooldown;
  
  public NatureMage() {
    
    super(PRICE, MAX_HP);
    
  }
  
  @Override
  public HashMap<Integer, ItemStack> getClassItems() {
    
    items.put(0, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.BONE )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Healer Wand")
      .setItemLore("Trusty wand of a nature mage.")
      .setUnbreakable(true)
    ));
    
    items.put(1, ClassItemFactory.getBuiltItem(
      new ClassItemFactory.ClassItemData( Material.WOOD_SWORD )
      .setItemName(ChatColor.RED + getClass().getSimpleName() + "'s Sword")
      .setItemLore("Trusty sword of a nature mage.")
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
      new ClassItemFactory.ClassItemData( Material.RED_ROSE )
      .setItemName(ChatColor.RED + getClass().getSimpleName())
      .setItemLore(PRICE + " coins.", "Description of class.")
    );
    
  }

  @Override
  public long getCooldown(CastleRaidCooldown cooldown) {
    if (cooldown == CastleRaidCooldown.NATURE_WAND) {
      return natureWandCooldown;
    }
    return 0;
  }

  @Override
  public boolean isOnCooldown(CastleRaidCooldown cooldown) {
    if (cooldown == CastleRaidCooldown.NATURE_WAND) {
      return System.currentTimeMillis() < natureWandCooldown;
    }
    return false;
  }

  @Override
  public void setCooldown(CastleRaidCooldown cooldown, long cooldownDuration) {
    if (cooldown == CastleRaidCooldown.NATURE_WAND) {
      natureWandCooldown = System.currentTimeMillis() + cooldownDuration;
    }
  }
}