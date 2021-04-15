package xyz.vedat.castleraid;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.vedat.castleraid.classes.Knight;
import xyz.vedat.castleraid.event.CastleRaidSprintEvent;

public class CastleRaidMain extends JavaPlugin {
    
    private HashMap<UUID, CastleRaidPlayer> crPlayers;
    
    @Override
    public void onEnable() {
        
        getLogger().info("Enabling...");
        
        getServer().getPluginManager().registerEvents(new CastleRaidSprintEvent(this), this);
        this.getCommand("class").setExecutor(new CommandClassPick(this));
        
        crPlayers = new HashMap<>();
        for (Player player : getServer().getOnlinePlayers()) {
            
            crPlayers.put(player.getUniqueId(), new CastleRaidPlayer(player, new Knight()));
            
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
    
}
