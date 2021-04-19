package xyz.vedat.castleraid;

import java.util.function.BiConsumer;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
// import org.bukkit.Material;
// import org.bukkit.attribute.Attributable;
// import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.chat.BaseComponent;
import xyz.vedat.castleraid.CastleRaidMain.GameState;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.classes.ClassItemFactory;

public class CastleRaidPlayer {
  
  private Player player;
  private CastleRaidClass crClass;
  private BukkitTask sprintTask;
  private CastleRaidMain.Teams team;
  private CastleRaidMain plugin;
  private boolean carriesBeacon;
  
  public CastleRaidPlayer(Player player, CastleRaidClass crClass, CastleRaidMain.Teams team, CastleRaidMain plugin) {
    
    this.plugin = plugin;
    this.player = player;
    setCrClass(crClass);
    this.sprintTask = null;
    this.carriesBeacon = false;
    setTeam(team);
    
  }
  
  public Player getPlayer() {
    return player;
  }
  
  public CastleRaidClass getCrClass() {
    return crClass;
  }
  
  public boolean setCrClass(CastleRaidClass crClass) {
    
    if (crClass == null) {
      return false;
    }
    
    this.crClass = crClass;
    
    player.getInventory().clear();
    player.setItemOnCursor(null);
    
    setHeadBlock();
    
    crClass.getClassItems().forEach(new BiConsumer<Integer, ItemStack>(){
      
      @Override
      public void accept(Integer index, ItemStack item) {
        
        player.getInventory().setItem(index, item);
        
      }
      
    });
    
    player.getInventory().setItem(8, ClassItemFactory.getClassPickerItem());
    
    player.setMaxHealth(crClass.getMaxHP());
    player.setHealth(crClass.getMaxHP());
    
    return true;
    
  }
  
  public boolean hasOngoingSprintEvent() {
    return sprintTask != null;
  }
  
  public void setOngoingSprintEvent(BukkitTask sprintTask) {
    
    if (sprintTask == null && this.sprintTask != null) {
      this.sprintTask.cancel();
    }
    
    this.sprintTask = sprintTask;
    
  }
  
  public CastleRaidMain.Teams getTeam() {
    return team;
  }
  
  public Location spawnPlayer() {
    
    final Location[] SPAWN_RED = new Location[] {
      new Location(plugin.getGameWorld(), -427, 51, 535, 180, 0),
      new Location(plugin.getGameWorld(), -436, 51, 535, -90, 0),
      new Location(plugin.getGameWorld(), -418, 51, 538, 180, 0),
      new Location(plugin.getGameWorld(), -422, 51, 546, 90, 0)
    };
    final Location[] SPAWN_BLUE = new Location[] {
      new Location(plugin.getGameWorld(), -418, 96, 362),
      new Location(plugin.getGameWorld(), -436, 96, 362),
      new Location(plugin.getGameWorld(), -427, 91, 342),
      new Location(plugin.getGameWorld(), -395, 81, 404),
      new Location(plugin.getGameWorld(), -477, 81, 404),
      new Location(plugin.getGameWorld(), -451, 79, 384),
      new Location(plugin.getGameWorld(), -417, 81, 384)
    };
    final Location SPAWN_LOBBY = new Location(plugin.getGameWorld(), -520, 6, 557, 90, 0);
    final Location SPAWN_SPECTATOR = new Location(plugin.getGameWorld(), -428, 75, 440, 180, 0);
    final Location SPAWN_DEFAULT = new Location(plugin.getServer().getWorld("world_default"), 0, 0, 0);
    
    Location spawnLocation = SPAWN_DEFAULT;
    
    if (plugin.getGameState() == GameState.STANDBY) {
      
      spawnLocation = SPAWN_DEFAULT;
      
    } else if (this.team == Teams.SPECTATOR) {
      
      player.setFlying(true);
      spawnLocation = SPAWN_SPECTATOR;
      
    } else if (this.team == Teams.RED) {
      
      spawnLocation = SPAWN_RED[(int) Math.floor(Math.random() * SPAWN_RED.length)];
      
    } else if (this.team == Teams.BLUE) {
      
      spawnLocation = SPAWN_BLUE[(int) Math.floor(Math.random() * SPAWN_BLUE.length)];
      
    } else if (this.team == Teams.WAITING) {
      
      spawnLocation = SPAWN_LOBBY;
      
    }
    
    player.teleport(spawnLocation);
    
    if (crClass != null) {
      this.setCrClass(crClass);
    }
    
    return spawnLocation;
    
  }
  
  public boolean setTeam(CastleRaidMain.Teams team) {
    
    this.team = team;
    this.crClass = null;
    
    //String woolKey = (team == teams.SPECTATOR ? "WHITE" : team.toString());
    
    player.getInventory().setArmorContents(null);
    
    if (team != Teams.SPECTATOR) {
      
      player.setAllowFlight(false);
      player.spigot().setCollidesWithEntities(true);
      
      if (team != Teams.WAITING) {
        setHeadBlock();
      } else {
        player.setMaxHealth(20);
        player.setHealth(20);
        player.getInventory().clear();
      }
      
      for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
        otherPlayer.showPlayer(this.player);
      }
      
    } else {
      
      player.setAllowFlight(true);
      player.spigot().setCollidesWithEntities(false);
      
      for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
      
        if (plugin.getCrPlayers().get(otherPlayer.getUniqueId()).getTeam() != Teams.SPECTATOR) {
          otherPlayer.hidePlayer(this.player);
        }
        
      }
      
    }
    
    // player.getInventory().setHelmet(new ItemStack(Material.valueOf(woolKey)));
    
    spawnPlayer();
    
    return true;
    
  }
  
  public void setHeadBlock() {
    
    if (carriesBeacon) {
      player.getInventory().setHelmet(new ItemStack(Material.BEACON));
    } else {
      player.getInventory().setHelmet(new Wool(DyeColor.valueOf(team.toString())).toItemStack(1));
    }
    
  }
  
  public boolean isCarryingBeacon() {
      return carriesBeacon;
  }
  
  public void setCarriesBeacon(boolean carriesBeacon) {
      this.carriesBeacon = carriesBeacon;
      setHeadBlock();
  }
  
}
