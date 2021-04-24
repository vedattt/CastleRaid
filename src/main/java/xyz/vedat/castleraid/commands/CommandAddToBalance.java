package xyz.vedat.castleraid.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import xyz.vedat.castleraid.CastleRaidMain;

public class CommandAddToBalance extends CastleRaidCommand {
    
    public CommandAddToBalance(CastleRaidMain plugin) {
        
        super("addtobalance", plugin);
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length != 1 && args[0].length() > 0 && StringUtils.isNumeric(args[0])) {
            return false;
        }
        
        plugin.getCrPlayer((Player) sender).addBalance(Integer.valueOf(args[0]));
        
        return true;
        
    }
    
}
