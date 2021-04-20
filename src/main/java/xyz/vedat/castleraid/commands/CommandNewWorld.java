package xyz.vedat.castleraid.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import xyz.vedat.castleraid.CastleRaidMain;

public class CommandNewWorld extends CastleRaidCommand implements CommandExecutor {
    
    public CommandNewWorld(CastleRaidMain plugin) {
        
        super("newcrgame", plugin);
        
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
