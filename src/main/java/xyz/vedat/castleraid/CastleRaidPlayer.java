package xyz.vedat.castleraid;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.vedat.castleraid.CastleRaidMain.GameState;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.Berserker;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.ClassItemFactory;
import xyz.vedat.castleraid.classes.Knight;
import xyz.vedat.castleraid.classes.Sentry;

public class CastleRaidPlayer {
  
  final private Player player;
  private CastleRaidClass crClass;
  private CastleRaidClass crClassUponRespawn;
  private CastleRaidMain.Teams team;
  final private CastleRaidMain plugin;
  private boolean carriesBeacon;
  private int balance;
  private Document dbDocument;
  
  public CastleRaidPlayer(Player player, CastleRaidMain.Teams team, CastleRaidMain plugin) {
    
    this.plugin = plugin;
    this.player = player;
    this.carriesBeacon = false;
    setTeam(team);
    
    refreshDBData();
    
  }
  
  public void addBalance(int balanceAddition) {
    
    this.balance += balanceAddition;
    
    plugin.getMongoCrPlayers().updateOne(Filters.eq("playerUUID", player.getUniqueId().toString()), Updates.inc("balance", balanceAddition));
    
  }
  
  public void refreshDBData() {
    
    dbDocument = plugin.getMongoCrPlayers().find(Filters.eq("playerUUID", player.getUniqueId().toString())).first();
    balance = dbDocument.getInteger("balance");
    crClass = plugin.buildCrClassObject(dbDocument.getString("class"));
    
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
    
    if (crClass instanceof Sentry && ((Sentry) crClass).getTurret() != null) {
      ((Sentry) crClass).restoreTurret(player, ((Sentry) crClass).getTurret());
    } else if (crClass instanceof Knight) {
      ((Knight) crClass).setOngoingSprintEvent(null);
    } else if (crClass instanceof Berserker) {
      ((Berserker) crClass).resetKillCount();
    }
    
    this.crClass = crClass;
    /*
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
    */
    
    // if (this.crClass != null && plugin.getGameState() == GameState.RUNNING) {
    //   spawnPlayer();
    // }
    
    plugin.getMongoCrPlayers().updateOne(Filters.eq("playerUUID", player.getUniqueId().toString()), Updates.set("class", crClass.getClass().getSimpleName()));
    
    return true;
    
  }
  
  public Teams getTeam() {
    return team;
  }
  
  public Location spawnPlayer() {
    
    Location spawnLocation = plugin.getAnySpawnLocation(team);
    
    if (crClassUponRespawn != null) {
      setCrClass(crClassUponRespawn);
      crClassUponRespawn = null;
    }
    
    //plugin.getLogger().info("Spawned player " + player.getName() + "...");
    
    player.teleport(spawnLocation);
    
    player.getInventory().clear();
    player.getInventory().setArmorContents(null);
    player.setItemOnCursor(null);
    
    player.setWalkSpeed(0.2f);
    player.setFoodLevel(20);
    
    if (plugin.getGameState() == GameState.STANDBY) {
      return spawnLocation;
    }
    
    for (PotionEffect potionEffect : player.getActivePotionEffects()) {
      player.removePotionEffect(potionEffect.getType());
    }
    
    if (crClass != null) {
      
      if (crClass instanceof Sentry && ((Sentry) crClass).getTurret() != null) {
        ((Sentry) crClass).restoreTurret(player, ((Sentry) crClass).getTurret());
      } else if (crClass instanceof Knight) {
        ((Knight) crClass).setOngoingSprintEvent(null);
      } else if (crClass instanceof Berserker) {
        ((Berserker) crClass).resetKillCount();
      }
      
      for (CastleRaidCooldown cooldown : crClass.getCooldownDurations().keySet().toArray(new CastleRaidCooldown[0])) {
        crClass.setOnCooldown(cooldown, false);
      }
      
      player.setMaxHealth(crClass.getMaxHP());
      player.setHealth(crClass.getMaxHP());
      
      if (team == Teams.SPECTATOR || team == Teams.WAITING) {
        player.setMaxHealth(20);
        player.setHealth(20);
      } else {
        
        crClass.getClassItems().forEach((index, item) -> player.getInventory().setItem(index, item));
        
        new BukkitRunnable() {
          
          @Override
          public void run() {
            player.addPotionEffects(crClass.getClassPotionEffects());
          }
          
        }.runTaskLater(plugin, 5L);
        
      }
      
    } else {
      player.setMaxHealth(20);
      player.setHealth(20);
    }
    
    refreshDBData();
    setHeadBlock();
    
    if (team == Teams.WAITING || team == Teams.SPECTATOR) {
      player.getInventory().setItem(4, ClassItemFactory.getClassPickerItem());
    } else {
      player.getInventory().setItem(8, ClassItemFactory.getClassPickerItem());
    }
    
    if (team == Teams.SPECTATOR) {
      player.getInventory().setItem(8, ClassItemFactory.getTrackerCompass());
    } else if (team != Teams.WAITING) {
      player.getInventory().setItem(7, ClassItemFactory.getTrackerCompass());
    }
    
    player.getInventory().setItem(22, ClassItemFactory.getBalanceItem(balance));
    
    return spawnLocation;
    
  }
  
  public boolean setTeam(Teams team) {
    
    this.team = team;
    
    //String woolKey = (team == teams.SPECTATOR ? "WHITE" : team.toString());
    
    //player.getInventory().setArmorContents(null);
    
    if (team != Teams.SPECTATOR) {
      
      player.setAllowFlight(false);
      player.spigot().setCollidesWithEntities(true);
      
      if (team != Teams.WAITING) {
        setHeadBlock();
        
        plugin.getScoreboardTeam(team).addEntry(player.getDisplayName());
        
        // if (crClass == null) {
        //   crClass = plugin.buildCrClassObject(Math.random() > 0.5 ? "Archer" : "Knight");
        // }
        
      } else {
        plugin.getScoreboardTeam(Teams.SPECTATOR).addEntry(player.getDisplayName());
        // player.setMaxHealth(20);
        // player.setHealth(20);
        // player.getInventory().clear();
      }
      
      for (Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
        otherPlayer.showPlayer(this.player);
      }
      
    } else {
      
      plugin.getScoreboardTeam(Teams.SPECTATOR).addEntry(player.getDisplayName());
      
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
    } else if (team != Teams.WAITING && team != Teams.SPECTATOR) {
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
  
  public void setCrClassUponRespawn(CastleRaidClass crClassUponRespawn) {
      this.crClassUponRespawn = crClassUponRespawn;
      plugin.getMongoCrPlayers().updateOne(Filters.eq("playerUUID", player.getUniqueId().toString()), Updates.set("class", crClassUponRespawn.getClass().getSimpleName()));
  }
  
  public CastleRaidClass getCrClassUponRespawn() {
      return crClassUponRespawn;
  }
  
}
