package xyz.vedat.castleraid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import xyz.vedat.castleraid.classes.CastleRaidClass;

public class CommandClassPick implements CommandExecutor {
  
  CastleRaidMain plugin;
  ArrayList<String> classArguments;
  
  public CommandClassPick(CastleRaidMain plugin) {
    
    this.plugin = plugin;
    this.classArguments = new ArrayList<>();
    
    classArguments.add("alchemist");
    classArguments.add("archer");
    classArguments.add("assassin");
    classArguments.add("berserker");
    classArguments.add("builder");
    classArguments.add("juggernaut");
    classArguments.add("knight");
    classArguments.add("mage");
    classArguments.add("naturemage");
    classArguments.add("sentry");
    classArguments.add("sniper");
    classArguments.add("spy");
    classArguments.add("timewizard");
    
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
    if (args.length != 1 || !classArguments.contains(args[0])) {
      return false;
    }
    
    if (sender instanceof Player) {
      
      Player player = (Player) sender;
      
      if (plugin.getCrPlayers().get(player.getUniqueId()).getTeam().equals(CastleRaidMain.teams.SPECTATOR)) {
        return false;
      }
      
      CastleRaidClass newClass = plugin.getCrClass(args[0]);
      
      plugin.getCrPlayers().get(player.getUniqueId()).setCrClass(newClass);
      
    }
    
    return true;
    
  }
  
  public class ClassPickCompletion implements TabCompleter {
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      
      return classArguments;
      
    }
    
  }
  
}
