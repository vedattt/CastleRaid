package xyz.vedat.castleraid;

import java.util.function.BiConsumer;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.vedat.castleraid.CastleRaidMain.GameState;
import xyz.vedat.castleraid.CastleRaidMain.Teams;
import xyz.vedat.castleraid.classes.Berserker;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.classes.CastleRaidCooldown;
import xyz.vedat.castleraid.classes.ClassItemFactory;
import xyz.vedat.castleraid.classes.Knight;
import xyz.vedat.castleraid.classes.Sentry;

public class CastleRaidPlayer {
  
  private Player player;
  private CastleRaidClass crClass;
  private CastleRaidMain.Teams team;
  private CastleRaidMain plugin;
  private boolean carriesBeacon;
  
  public CastleRaidPlayer(Player player, CastleRaidMain.Teams team, CastleRaidMain plugin) {
    
    this.plugin = plugin;
    this.player = player;
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
    spawnPlayer();
    
    return true;
    
  }
  
  public Teams getTeam() {
    return team;
  }
  
  public Location spawnPlayer() {
    
    Location spawnLocation = plugin.getAnySpawnLocation(team);
    
    player.teleport(spawnLocation);
    
    player.getInventory().clear();
    player.setItemOnCursor(null);
    
    player.setWalkSpeed(0.2f);
    
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
      
      crClass.getClassItems().forEach(new BiConsumer<Integer, ItemStack>(){
      
        @Override
        public void accept(Integer index, ItemStack item) {
          
          player.getInventory().setItem(index, item);
          
        }
        
      });
      
    } else {
      player.setMaxHealth(20);
      player.setHealth(20);
    }
    
    setHeadBlock();
    
    player.getInventory().setItem(8, ClassItemFactory.getClassPickerItem());
    
    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 600 * 20, 1));
    
    return spawnLocation;
    
  }
  
  public boolean setTeam(Teams team) {
    
    this.team = team;
    this.crClass = null;
    
    //String woolKey = (team == teams.SPECTATOR ? "WHITE" : team.toString());
    
    //player.getInventory().setArmorContents(null);
    
    if (team != Teams.SPECTATOR) {
      
      player.setAllowFlight(false);
      player.spigot().setCollidesWithEntities(true);
      
      if (team != Teams.WAITING) {
        setHeadBlock();
      } else {
        // player.setMaxHealth(20);
        // player.setHealth(20);
        // player.getInventory().clear();
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
  
}
