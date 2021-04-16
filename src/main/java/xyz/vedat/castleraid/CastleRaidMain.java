package xyz.vedat.castleraid;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.vedat.castleraid.classes.*;
import xyz.vedat.castleraid.event.CastleRaidClassPickerEvent;
import xyz.vedat.castleraid.event.CastleRaidHungerEvent;
import xyz.vedat.castleraid.event.CastleRaidSniperEvent;
import xyz.vedat.castleraid.event.CastleRaidSprintEvent;

public class CastleRaidMain extends JavaPlugin {
    
    private HashMap<UUID, CastleRaidPlayer> crPlayers;
    public static enum teams {
        BLUE, RED, SPECTATOR
    }
    
    @Override
    public void onEnable() {
        
        getLogger().info("Enabling...");
        
        getServer().getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
        
        getServer().getPluginManager().registerEvents(new CastleRaidClassPickerEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSprintEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSniperEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidHungerEvent(), this);
        
        this.getCommand("class").setExecutor(new CommandClassPick(this));
        this.getCommand("class").setTabCompleter(new CommandClassPick(this).new ClassPickCompletion());
        this.getCommand("jointeam").setExecutor(new CommandJoinTeam(this));
        this.getCommand("jointeam").setTabCompleter(new CommandJoinTeam(this).new JoinTeamCompletion());
        
        crPlayers = new HashMap<>();
        
        for (Player player : getServer().getOnlinePlayers()) {
            
            crPlayers.put(player.getUniqueId(), new CastleRaidPlayer(player, null, teams.SPECTATOR));
            
        }
        
        
    }
    
    @Override
    public void onDisable() {
        
        getLogger().info("Disabling...");
        
    }
    
    public Logger getLogger() {
        return super.getLogger();
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
    
}
