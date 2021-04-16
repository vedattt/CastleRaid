package xyz.vedat.castleraid.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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
      Alchemist.getClassSymbol(),
      Archer.getClassSymbol(),
      Assassin.getClassSymbol(),
      Berserker.getClassSymbol(),
      Builder.getClassSymbol(),
      Juggernaut.getClassSymbol(),
      Knight.getClassSymbol(),
      Mage.getClassSymbol(),
      NatureMage.getClassSymbol(),
      Sentry.getClassSymbol(),
      Sniper.getClassSymbol(),
      Spy.getClassSymbol(),
      TimeWizard.getClassSymbol()
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
    
    if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && event.getItem().isSimilar(ClassPickerItem.getClassPickerItem()) &&
        !crPlayer.getTeam().equals(CastleRaidMain.teams.SPECTATOR)) {
      
      player.openInventory(classPickerInventory);
      
    }
    
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    
    if (classPickerInventory != null && event.getClickedInventory().equals(classPickerInventory)) {
      
      event.setCancelled(true);
      
      if (event.getCurrentItem() != null) {
        
        CastleRaidClass newClass = plugin.getCrClass(event.getCurrentItem().getItemMeta().getDisplayName());
        
        plugin.getCrPlayers().get(event.getWhoClicked().getUniqueId()).setCrClass(newClass);
        
        event.getWhoClicked().closeInventory();
        
      }
      
    }
    
  }
  
  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    
    if (classPickerInventory != null && event.getInventory().equals(classPickerInventory)) {
      
      event.setCancelled(true);
      
    }
    
  }
  
}
