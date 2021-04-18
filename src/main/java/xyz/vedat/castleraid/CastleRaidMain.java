package xyz.vedat.castleraid;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.vedat.castleraid.classes.*;
import xyz.vedat.castleraid.event.CastleRaidItemBlockEvents;
import xyz.vedat.castleraid.event.CastleRaidMageWandEvent;
import xyz.vedat.castleraid.event.CastleRaidClassPickerEvent;
import xyz.vedat.castleraid.event.CastleRaidDeathEvent;
import xyz.vedat.castleraid.event.CastleRaidHungerEvent;
import xyz.vedat.castleraid.event.CastleRaidQuickArrowEvent;
import xyz.vedat.castleraid.event.CastleRaidSentryTurretEvent;
import xyz.vedat.castleraid.event.CastleRaidSprintEvent;

public class CastleRaidMain extends JavaPlugin {
    
    private HashMap<UUID, CastleRaidPlayer> crPlayers;
    private World crGameWorld;
    private Location beaconLocation;
    private Location beaconTarget;
    public static enum teams {
        BLUE, RED, SPECTATOR, WAITING
    }
    
    @Override
    public void onEnable() {
        
        getLogger().info("Enabling...");
        
        startNewWorld();
        
        getServer().getPluginManager().registerEvents(new CastleRaidClassPickerEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSprintEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidQuickArrowEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidHungerEvent(), this);
        getServer().getPluginManager().registerEvents(new CastleRaidItemBlockEvents(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidMageWandEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSentryTurretEvent(this), this);
        
        this.getCommand("newworldcr").setExecutor(new CommandNewWorld(this));
        this.getCommand("class").setExecutor(new CommandClassPick(this));
        this.getCommand("class").setTabCompleter(new CommandClassPick(this).new ClassPickCompletion());
        this.getCommand("jointeam").setExecutor(new CommandJoinTeam(this));
        this.getCommand("jointeam").setTabCompleter(new CommandJoinTeam(this).new JoinTeamCompletion());
        
    }
    
    @Override
    public void onDisable() {
        
        getLogger().info("Disabling...");
        
    }
    
    public void startNewWorld() {
        
        if (getServer().getWorlds().size() > 1) {
            
            for (Player player : getServer().getOnlinePlayers()) {
                
                player.teleport(new Location(getServer().getWorlds().get(0), 0, 0, 0));
                
            }
            
            Bukkit.unloadWorld(getServer().getWorld("world_castleraid_temp"), false);
            
        }
        
        for (World world : getServer().getWorlds()) {
            for (File file : new File(world.getWorldFolder().getAbsolutePath(), "playerdata").listFiles()) {
                file.delete();
            }
        }
        
        try {
            FileUtils.copyDirectory(new File("world_castleraid_core"), new File("world_castleraid_temp"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        crGameWorld = Bukkit.createWorld(new WorldCreator("world_castleraid_temp"));
        
        crPlayers = new HashMap<>();
        
        for (Player player : getServer().getOnlinePlayers()) {
            
            player.teleport(new Location(crGameWorld, -520, 6, 557));
            player.getInventory().clear();
            
            crPlayers.put(player.getUniqueId(), new CastleRaidPlayer(player, null, teams.WAITING, this));
            getLogger().info("A player was added... " + player.getName());
            
        }
        
        getServer().getWorld("world_castleraid_temp").setDifficulty(Difficulty.PEACEFUL);
        
        this.beaconLocation = new Location(getGameWorld(), -427, 80, 336);
        this.beaconTarget = new Location(getGameWorld(), -427, 50, 535);
        
    }
    
    public HashMap<UUID, CastleRaidPlayer> getCrPlayers() {
        return crPlayers;
    }
    
    public boolean addCrPlayer(CastleRaidPlayer crPlayer) {
        
        crPlayers.put(crPlayer.getPlayer().getUniqueId(), crPlayer);
        return true;
        
    }
    
    public CastleRaidClass getCrClass(String classString) {
        
        CastleRaidClass newClass;
        
        switch (classString.toLowerCase()) {
            case "alchemist":
                newClass = new Alchemist();
                break;
            case "archer":
                newClass = new Archer();
                break;
            case "assassin":
                newClass = new Assassin();
                break;
            case "berserker":
                newClass = new Berserker();
                break;
            case "builder":
                newClass = new Builder();
                break;
            case "juggernaut":
                newClass = new Juggernaut();
                break;
            case "knight":
                newClass = new Knight();
                break;
            case "mage":
                newClass = new Mage();
                break;
            case "nature mage":
            case "naturemage":
                newClass = new NatureMage();
                break;
            case "sentry":
                newClass = new Sentry();
                break;
            case "sniper":
                newClass = new Sniper();
                break;
            case "spy":
                newClass = new Spy();
                break;
            case "time wizard":
            case "timewizard":
                newClass = new TimeWizard();
                break;
            default:
                newClass = new Archer();
                break;
        }
        
        return newClass;
        
    }
    
    public World getGameWorld() {
        return crGameWorld;
    }
    
    public Location getBeaconLocation() {
        return beaconLocation;
    }
    
    public Location getBeaconTarget() {
        return beaconTarget;
    }
    
}
