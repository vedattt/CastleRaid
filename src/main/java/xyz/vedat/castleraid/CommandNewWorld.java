package xyz.vedat.castleraid;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandNewWorld implements CommandExecutor {
    
    CastleRaidMain plugin;
    
    public CommandNewWorld(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length != 0) {
            return false;
        }
        
        plugin.startNewWorld();
        
        return true;
        
    }
    
}
