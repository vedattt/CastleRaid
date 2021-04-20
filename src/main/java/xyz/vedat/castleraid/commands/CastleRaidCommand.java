package xyz.vedat.castleraid.commands;

import org.bukkit.command.TabCompleter;

import xyz.vedat.castleraid.CastleRaidMain;

public class CastleRaidCommand {
    
    private String commandName;
    private TabCompleter tabCompleter;
    protected CastleRaidMain plugin;
    
    public CastleRaidCommand(String commandName, CastleRaidMain plugin) {
        
        this.commandName = commandName;
        this.plugin = plugin;
        
    }
    
    public String getCommandName() {
        return commandName;
    }
    
    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }
    
    protected void setTabCompleter(TabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }
    
}
