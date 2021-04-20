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
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.vedat.castleraid.classes.*;
import xyz.vedat.castleraid.commands.CastleRaidCommand;
import xyz.vedat.castleraid.commands.CommandClassPick;
import xyz.vedat.castleraid.commands.CommandForceStart;
import xyz.vedat.castleraid.commands.CommandJoinTeam;
import xyz.vedat.castleraid.commands.CommandNewWorld;
import xyz.vedat.castleraid.event.*;

public class CastleRaidMain extends JavaPlugin {
    
    private HashMap<UUID, CastleRaidPlayer> crPlayers;
    private World crGameWorld;
    private Location beaconLocation;
    private Location beaconTarget;
    
    private HashMap<Location, Builder.Claymore> builderClaymores;
    private GameState currentGameState;
    private BukkitTask waitingTask;
    private BukkitTask runningTask;
    private boolean isBeaconCaptured;
    protected boolean isforceStarted;
    
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
        
        Listener[] eventListeners = new Listener[] {
            new CastleRaidClassPickerEvent(this),
            new CastleRaidSprintEvent(this),
            new CastleRaidQuickArrowEvent(this),
            new CastleRaidHungerEvent(),
            new CastleRaidCoreEvents(this),
            new CastleRaidDeathEvent(this),
            new CastleRaidMageWandEvent(this),
            new CastleRaidSentryTurretEvent(this),
            new CastleRaidNatureMageHealEvent(this),
            new CastleRaidPyroFireEvent(this),
            new CastleRaidJuggernautBlockEvent(this),
            new CastleRaidBerserkKillEvent(this),
            new CastleRaidBackstabEvent(this),
            new CastleRaidAssassinEvents(this),
            new CastleRaidAlchemistWitherEvent(this),
            new CastleRaidTimeWizardEvent(this),
            new CastleRaidSpySmokeEvent(this),
            new CastleRaidBuilderClaymoreEvent(this)
        };
        
        CastleRaidCommand[] commands = new CastleRaidCommand[] {
            new CommandNewWorld(this),
            new CommandClassPick(this),
            new CommandJoinTeam(this),
            new CommandForceStart(this),
        };
        
        for (Listener listener : eventListeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
        
        for (CastleRaidCommand command : commands) {
            getLogger().info("Command: " + command.getCommandName());
            this.getCommand(command.getCommandName()).setExecutor((CommandExecutor) command);
            if (command instanceof TabCompleter) {
                this.getCommand(command.getCommandName()).setTabCompleter((TabCompleter) command);
            }
        }
        
    }
    
    @Override
    public void onDisable() {
        
        getLogger().info("Disabling...");
        
    }
    
    public void startNewWorld() {

        setGameState(GameState.STANDBY);

        stopGameEvents();
        
        if (getServer().getWorlds().size() > 1) {
            
            for (Player player : getServer().getOnlinePlayers()) {
                
                player.teleport(new Location(getServer().getWorld("world_default"), 0, 5, 0));
                
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
            
            crPlayers.put(player.getUniqueId(), new CastleRaidPlayer(player, Teams.WAITING, this));
            
            getCrPlayer(player).spawnPlayer();
            
            getLogger().info("A player was added... " + player.getName());
            
        }
        
        getServer().getWorld("world_castleraid_temp").setDifficulty(Difficulty.NORMAL);
        
        this.beaconLocation = new Location(getGameWorld(), -427, 80, 336);
        this.beaconTarget = new Location(getGameWorld(), -427, 50, 535);
        this.isBeaconCaptured = false;
        
        this.builderClaymores = new HashMap<>();
        this.isforceStarted = false;
        
        setGameState(GameState.WAITING);
        
        if (crPlayers.size() > 0) {
            startGameEvents();
        }
        
    }
    
    public void startGameEvents() {
        
        CastleRaidMain plugin = this;
        
        BukkitRunnable runningGameEvents = new BukkitRunnable(){
            
            int countdownInGame = 600;
            
            @Override
            public void run() {
                
                countdownInGame--;
                
                if (countdownInGame == 60) {
                    announceInChat("1 minute left!");
                } else if (countdownInGame % 60 == 0) {
                    announceInChat(countdownInGame / 60 + " minutes left..." );
                }
                
                if (countdownInGame == 0 || isBeaconCaptured) {
                    
                    if (isBeaconCaptured) {
                        announceWinningTeam(Teams.RED);
                    } else {
                        announceWinningTeam(Teams.BLUE);
                    }
                    
                    startNewWorld();
                    
                }
                
            }
            
        };
        
        BukkitRunnable waitingGameEvents = new BukkitRunnable(){
            
            int countdownGameStarting = 60;
            
            @Override
            public void run() {
                
                if (crPlayers.size() >= 6 || isforceStarted) {
                    
                    countdownGameStarting--;
                    
                    if (countdownGameStarting == 0 || isforceStarted) {
                        
                        setGameState(GameState.RUNNING);
                        
                        runningTask = runningGameEvents.runTaskTimer(plugin, 0L, 20L);
                        
                        splitWaitersIntoTeams();
                        
                        for (CastleRaidPlayer crPlayer : crPlayers.values()) {
                            
                            crPlayer.spawnPlayer();
                            
                        }
                        
                        cancel();
                        
                    } else if (countdownGameStarting % 5 == 0) {
                        announceInChat(countdownGameStarting + " seconds until game starts...");
                    }
                    
                } else {
                    countdownGameStarting = 60;
                }
                
            }
            
        };
        
        for (CastleRaidPlayer crPlayer : crPlayers.values()) {
            crPlayer.spawnPlayer();
        }
        
        waitingTask = waitingGameEvents.runTaskTimer(this, 0L, 20L);
        
    }
    
    public void stopGameEvents() {
        
        if (getServer().getScheduler().getPendingTasks().size() != 0) {
            getServer().getScheduler().cancelAllTasks();
        }
        
    }
    
    public HashMap<UUID, CastleRaidPlayer> getCrPlayers() {
        return crPlayers;
    }
    
    public boolean addCrPlayer(CastleRaidPlayer crPlayer) {
        
        crPlayers.put(crPlayer.getPlayer().getUniqueId(), crPlayer);
        return true;
        
    }
    
    public CastleRaidPlayer getCrPlayer(Player player) {
        return crPlayers.get(player.getUniqueId());
    }
    
    public void removeCrPlayer(CastleRaidPlayer crPlayer) {
        crPlayers.remove(crPlayer.getPlayer().getUniqueId());
    }
    
    public CastleRaidClass buildCrClassObject(String classString) {
        
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
    
    public HashMap<Location, Builder.Claymore> getAllBuilderClaymores() {
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
        
        getPlayersOfTeam(Teams.WAITING).forEach(new BiConsumer<UUID, CastleRaidPlayer>() {
            
            Map<UUID, CastleRaidPlayer> redTeam;
            Map<UUID, CastleRaidPlayer> blueTeam;
            
            @Override
            public void accept(UUID uuid, CastleRaidPlayer crPlayer) {
                
                redTeam = getPlayersOfTeam(Teams.RED);
                blueTeam = getPlayersOfTeam(Teams.BLUE);
                
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
        
        announceInChat(team + " has won the game.");
        startNewWorld();
        
    }
    
    public Location getStandbyWorldLocation() {
        return new Location(getServer().getWorld("world_default"), 0, 5, 0);
    }
    
    public void setBeaconCaptured(boolean isBeaconCaptured) {
        this.isBeaconCaptured = isBeaconCaptured;
    }
    
    public boolean isBeaconCaptured() {
        return isBeaconCaptured;
    }
    
    public boolean isIsforceStarted() {
        return isforceStarted;
    }
    
    public void setforceStarted(boolean isforceStarted) {
        this.isforceStarted = isforceStarted;
    }
    
    public Location getAnySpawnLocation(Teams team) {
        
        final Location[] SPAWN_RED = new Location[] {
            new Location(crGameWorld, -427, 51, 535, 180, 0),
            new Location(crGameWorld, -436, 51, 535, -90, 0),
            new Location(crGameWorld, -418, 51, 538, 180, 0),
            new Location(crGameWorld, -422, 51, 546, 90, 0)
        };
        final Location[] SPAWN_BLUE = new Location[] {
            new Location(crGameWorld, -418, 96, 362),
            new Location(crGameWorld, -436, 96, 362),
            new Location(crGameWorld, -427, 91, 342),
            new Location(crGameWorld, -395, 81, 404),
            new Location(crGameWorld, -477, 81, 404),
            new Location(crGameWorld, -451, 79, 384),
            new Location(crGameWorld, -417, 81, 384)
        };
        final Location SPAWN_LOBBY = new Location(crGameWorld, -520, 6, 557, 90, 0);
        final Location SPAWN_SPECTATOR = new Location(crGameWorld, -428, 75, 440, 180, 0);
        final Location SPAWN_DEFAULT = new Location(getServer().getWorld("world_default"), 0, 5, 0);
        
        Location spawnLocation = SPAWN_DEFAULT;
        
        if (getGameState() == GameState.STANDBY) {
            
            spawnLocation = SPAWN_DEFAULT;
            
        } else if (team == Teams.SPECTATOR) {
            
            //player.setFlying(true);
            spawnLocation = SPAWN_SPECTATOR;
            
        } else if (team == Teams.RED) {
            
            spawnLocation = SPAWN_RED[(int) Math.floor(Math.random() * SPAWN_RED.length)];
            
        } else if (team == Teams.BLUE) {
            
            spawnLocation = SPAWN_BLUE[(int) Math.floor(Math.random() * SPAWN_BLUE.length)];
            
        } else if (team == Teams.WAITING) {
            
            spawnLocation = SPAWN_LOBBY;
            
        }
        
        return spawnLocation;
        
    }
    
    public BukkitTask getRunningTask() {
        return runningTask;
    }
    
    public BukkitTask getWaitingTask() {
        return waitingTask;
    }
    
}
