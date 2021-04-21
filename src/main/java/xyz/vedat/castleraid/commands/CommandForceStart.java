package xyz.vedat.castleraid.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidMain.GameState;

public class CommandForceStart extends CastleRaidCommand implements CommandExecutor {
    
    
    public CommandForceStart(CastleRaidMain plugin) {
        
        super("forcestart", plugin);
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (plugin.getGameState() == GameState.WAITING) {
            plugin.setforceStarted(true);
        } else {
            sender.sendMessage("This is not an appropiate time to force start a game. Try '/newcrgame' first.");
        }
        
        return true;
        
    }
    
}
