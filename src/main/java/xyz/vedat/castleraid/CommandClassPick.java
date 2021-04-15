package xyz.vedat.castleraid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import xyz.vedat.castleraid.classes.Archer;
import xyz.vedat.castleraid.classes.CastleRaidClass;
import xyz.vedat.castleraid.classes.Knight;

public class CommandClassPick implements CommandExecutor {
  
  CastleRaidMain plugin;
  
  public CommandClassPick(CastleRaidMain plugin) {
    
    this.plugin = plugin;
    
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
    if (sender instanceof Player) {
      
      Player player = (Player) sender;
      CastleRaidClass newClass;
      
      if (args[0].equals("knight")) {
        newClass = new Knight();
      } else {
        newClass = new Archer();
      }
      
      plugin.getCrPlayers().get(player.getUniqueId()).setCrClass(newClass);
      
    }
    
    return true;
    
  }
  
  public class ClassPickCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      
      ArrayList<String> classArguments = new ArrayList<>();
      
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
      
      return classArguments;
      
    }
    
  }
  
}
