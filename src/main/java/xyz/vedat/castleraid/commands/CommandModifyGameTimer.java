package xyz.vedat.castleraid.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import xyz.vedat.castleraid.CastleRaidMain;

public class CommandModifyGameTimer extends CastleRaidCommand {
    
    public CommandModifyGameTimer(CastleRaidMain plugin) {
        
        super("modifygametimer", plugin);
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length != 1 && args[0].length() > 0 && StringUtils.isNumeric(args[0])) {
            return false;
        }
        
        plugin.setCountdownModifier(Integer.valueOf(args[0]));
        
        return true;
        
    }
    
}
