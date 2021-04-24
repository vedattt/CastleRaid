package xyz.vedat.castleraid.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.vedat.castleraid.CastleRaidMain;

public class CommandSuicide extends CastleRaidCommand {
    
    public CommandSuicide(CastleRaidMain plugin) {
        
        super("suicide", plugin);
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length != 0) {
            return false;
        }
        
        ((Player) sender).setHealth(0);
        
        return true;
        
    }
    
}
