package xyz.vedat.castleraid.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import xyz.vedat.castleraid.CastleRaidMain;

public class CommandForceStart extends CastleRaidCommand implements CommandExecutor {
    
    
    public CommandForceStart(CastleRaidMain plugin) {
        
        super("forcestart", plugin);
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        plugin.setforceStarted(true);
        
        return true;
        
    }
    
}
