package xyz.vedat.castleraid.event;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.*;

public class CastleRaidClassPickerEvent implements Listener {
  
  CastleRaidMain plugin;
  ItemStack[] classSymbols;
  HashMap<CastleRaidPlayer, Inventory> crPlayerInventories;
  
  public CastleRaidClassPickerEvent(CastleRaidMain plugin) {
      
    this.plugin = plugin;
    this.classSymbols = new ItemStack[] {
      new Alchemist().getClassSymbolItem(),
      new Archer().getClassSymbolItem(),
      new Assassin().getClassSymbolItem(),
      new Berserker().getClassSymbolItem(),
      new Builder().getClassSymbolItem(),
      new Juggernaut().getClassSymbolItem(),
      new Knight().getClassSymbolItem(),
      new Mage().getClassSymbolItem(),
      new NatureMage().getClassSymbolItem(),
      new Pyromancer().getClassSymbolItem(),
      new Sentry().getClassSymbolItem(),
      new Sniper().getClassSymbolItem(),
      new Spy().getClassSymbolItem(),
      new TimeWizard().getClassSymbolItem()
    };
    this.crPlayerInventories = new HashMap<>();
    
  }
  
  public Inventory generateClassPickerInventory(CastleRaidPlayer crPlayer) {
    
    Inventory classPickerInventory = Bukkit.createInventory(null, 3 * 9, "Pick Class");
    
    for (int i = 0; i < classSymbols.length * 2; i += 2) {
      
      classPickerInventory.setItem(i, classSymbols[i / 2]);
      
      if (!crPlayer.hasBoughtClass(ChatColor.stripColor(classSymbols[i / 2].getItemMeta().getDisplayName()))) {
        
        ItemStack classItem = classSymbols[i / 2].clone();
        ItemMeta classItemMeta = classItem.getItemMeta();
        List<String> classLore = classItemMeta.getLore();
        
        int classPrice = plugin.buildCrClassObject(ChatColor.stripColor(classSymbols[i / 2].getItemMeta().getDisplayName())).getPrice();
        
        classLore.add("");
        classLore.add(ChatColor.GOLD + "Price: " + (classPrice <= crPlayer.getBalance() ? ChatColor.GREEN : ChatColor.RED) + classPrice);
        classItemMeta.setLore(classLore);
        classItem.setItemMeta(classItemMeta);
        
        classPickerInventory.setItem(i, classItem);
        
      } else if ((crPlayer.getCrClassUponRespawn() == null && crPlayer.getCrClass() != null && crPlayer.getCrClass().getClassSymbolItem().isSimilar(classSymbols[i / 2])) ||
          (crPlayer.getCrClassUponRespawn() != null && crPlayer.getCrClassUponRespawn().getClassSymbolItem().isSimilar(classSymbols[i / 2]))) {
        
        ItemStack selectedClass = classSymbols[i / 2].clone();
        ItemMeta selectedClassMeta = selectedClass.getItemMeta();
        List<String> selectedLore = selectedClassMeta.getLore();
        
        selectedLore.add("");
        selectedLore.add(ChatColor.GREEN + "-> SELECTED <-");
        selectedClassMeta.setLore(selectedLore);
        selectedClass.setItemMeta(selectedClassMeta);
        selectedClass.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        
        if (selectedClass.getType() == Material.POTION) {
          PotionMeta selectedPotionMeta = (PotionMeta) selectedClassMeta;
          selectedPotionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 2000, 1), true);
          selectedClass.setItemMeta(selectedPotionMeta);
        }
        
        classPickerInventory.setItem(i, selectedClass);
        
      }
      
    }
    
    crPlayerInventories.put(crPlayer, classPickerInventory);
    
    return classPickerInventory;
    
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    
    Action action = event.getAction();
    
    Player player = event.getPlayer();
    CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
    //CastleRaidClass crClass = crPlayer.getCrClass();
    
    if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && 
        event.getItem() != null && event.getItem().isSimilar(ClassItemFactory.getClassPickerItem())) {
      
      player.openInventory(generateClassPickerInventory(crPlayer));
      
    }
    
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    
    CastleRaidPlayer crPlayer = plugin.getCrPlayer((Player) event.getWhoClicked());
    
    if (crPlayerInventories.get(crPlayer) != null && crPlayerInventories.get(crPlayer).equals(event.getClickedInventory())) {
      
      event.setCancelled(true);
      
      if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
        
        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        String className = ChatColor.stripColor(itemName);
        CastleRaidClass newClass = plugin.buildCrClassObject(className);
        
        if (crPlayer.hasBoughtClass(className)) {
          
          plugin.getLogger().info("Player had already bought class");
          crPlayer.getPlayer().sendMessage("You will be playing as " + itemName + ChatColor.RESET + " the next time you spawn.");
          crPlayer.setCrClassUponRespawn(newClass);
          
        } else if (crPlayer.getBalance() >= newClass.getPrice()) {
          
          plugin.getLogger().info("Player has just bought class");
          crPlayer.getPlayer().sendMessage("You have just bought " + itemName + ChatColor.RESET + ".");
          crPlayer.setPurchaseStatus(className, true);
          crPlayer.addBalance(-1 * newClass.getPrice());
          crPlayer.setCrClassUponRespawn(newClass);
          
        } else {
          
          plugin.getLogger().info("Player couldn't buy class");
          crPlayer.getPlayer().sendMessage(ChatColor.RED + "Your balance is not enough to buy " + ChatColor.RESET + itemName + ChatColor.RED + ".");
          
        }
        
        event.getWhoClicked().closeInventory();
        
      }
      
    }
    
  }
  
}
