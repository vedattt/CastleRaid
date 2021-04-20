package xyz.vedat.castleraid.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.classes.*;

public class CastleRaidClassPickerEvent implements Listener {
  
  CastleRaidMain plugin;
  Inventory classPickerInventory;
  ItemStack[] classSymbols;
  
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
    
    classPickerInventory = Bukkit.createInventory(null, 3 * 9, "Pick Class");
    
    for (int i = 0; i < classSymbols.length * 2; i += 2) {
      
      classPickerInventory.setItem(i, classSymbols[i / 2]);
      
    }
    
  }
  
  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    
    Action action = event.getAction();
    
    Player player = event.getPlayer();
    CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
    //CastleRaidClass crClass = crPlayer.getCrClass();
    
    if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().isSimilar(ClassItemFactory.getClassPickerItem()) &&
        !crPlayer.getTeam().equals(CastleRaidMain.Teams.SPECTATOR)) {
      
      player.openInventory(classPickerInventory);
      
    }
    
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    
    if (classPickerInventory != null && event.getClickedInventory() != null && event.getClickedInventory().equals(classPickerInventory)) {
      
      event.setCancelled(true);
      
      if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getWhoClicked() != null) {
        
        CastleRaidClass newClass = plugin.buildCrClassObject(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
        
        plugin.getCrPlayers().get(event.getWhoClicked().getUniqueId()).setCrClass(newClass);
        
        event.getWhoClicked().closeInventory();
        
      }
      
    }
    
  }
  
}
