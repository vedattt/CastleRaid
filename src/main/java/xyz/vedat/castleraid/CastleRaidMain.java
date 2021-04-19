package xyz.vedat.castleraid;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.vedat.castleraid.classes.*;
import xyz.vedat.castleraid.event.*;

public class CastleRaidMain extends JavaPlugin {
    
    private HashMap<UUID, CastleRaidPlayer> crPlayers;
    private World crGameWorld;
    private Location beaconLocation;
    private Location beaconTarget;

    private HashMap<Location, Builder.Claymore> builderClaymores;
    private GameState currentGameState;
    
    public static enum Teams {
        BLUE, RED, SPECTATOR, WAITING
    }

    public static enum GameState {
        WAITING, // When players are waiting in the lobby
        RUNNING, // When a game is currently running
        STANDBY // When a game has ended and the world currently resets
    }
    
    /*
        TODO
        game starts with minimum 6 players in the lobby
        60 seconds timer when satisfied, then game starts
        // have command to force start a game
        
        10 min real game duration
        beacon captures -> game ends -> world etc. is reset
        
        when player dies (no items dropped on death) -> reset back to class items -> 
        
        
    */
    
    @Override
    public void onEnable() {
        
        getLogger().info("Enabling...");
        
        startNewWorld();
        
        getServer().getPluginManager().registerEvents(new CastleRaidClassPickerEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSprintEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidQuickArrowEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidHungerEvent(), this);
        getServer().getPluginManager().registerEvents(new CastleRaidCoreEvents(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidDeathEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidMageWandEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSentryTurretEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidNatureMageHealEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidPyroFireEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidJuggernautBlockEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidBerserkKillEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidBackstabEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidAssassinEvents(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidAlchemistWitherEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidTimeWizardEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidSpySmokeEvent(this), this);
        getServer().getPluginManager().registerEvents(new CastleRaidBuilderClaymoreEvent(this), this);
        
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
        
        if (getServer().getScheduler().getPendingTasks().size() != 0) {
            getServer().getScheduler().cancelAllTasks();
        }
        
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
            
            crPlayers.put(player.getUniqueId(), new CastleRaidPlayer(player, null, Teams.WAITING, this));
            getLogger().info("A player was added... " + player.getName());
            
        }
        
        getServer().getWorld("world_castleraid_temp").setDifficulty(Difficulty.NORMAL);
        
        this.beaconLocation = new Location(getGameWorld(), -427, 80, 336);
        this.beaconTarget = new Location(getGameWorld(), -427, 50, 535);
        
        this.builderClaymores = new HashMap<>();
        
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
            case "pyromancer":
                newClass = new Pyromancer();
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
    
    public Map<UUID, CastleRaidPlayer> getPlayersOfTeam(Teams team) {
        
        return crPlayers.entrySet().stream()
            .filter(entry -> entry.getValue().getTeam() == team)
            .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        
    }
    
    public HashMap<Location, Builder.Claymore> getBuilderClaymores() {
        return builderClaymores;
    }
    
    public boolean isBeaconGrabbed() {
        
        return crPlayers.values().stream().anyMatch(crPlayer -> crPlayer.isCarryingBeacon());
        
    }
    
    public GameState getGameState() {
        return currentGameState;
    }
    
    public void setGameState(GameState currentGameState) {
        this.currentGameState = currentGameState;
    }
    
    public void splitWaitersIntoTeams() {
        
        getPlayersOfTeam(Teams.WAITING).forEach(new BiConsumer<UUID, CastleRaidPlayer>(){
            
            Map<UUID, CastleRaidPlayer> redTeam = getPlayersOfTeam(Teams.RED);
            Map<UUID, CastleRaidPlayer> blueTeam = getPlayersOfTeam(Teams.BLUE);
            
            @Override
            public void accept(UUID uuid, CastleRaidPlayer crPlayer) {
                
                if (crPlayer.getTeam() == Teams.WAITING) {
                    
                    if (redTeam.size() > blueTeam.size()) {
                        crPlayer.setTeam(Teams.BLUE);
                    } else if (redTeam.size() < blueTeam.size()) {
                        crPlayer.setTeam(Teams.RED);
                    } else {
                        crPlayer.setTeam(Math.random() > 0.5 ? Teams.RED : Teams.BLUE);
                    }
                    
                }
                
            }
            
        });
        
    }
    
    public void announceInChat(String message) {
        
        for (CastleRaidPlayer crPlayer : crPlayers.values()) {
            
            crPlayer.getPlayer().sendMessage(message);
            
        }
        
    }
    
    public void announceWinningTeam(Teams team) {
        
        startNewWorld();
        announceInChat(team + " has won the game.");
        
    }
    
}
