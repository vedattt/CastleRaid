package xyz.vedat.castleraid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import xyz.vedat.castleraid.CastleRaidMain.Teams;

public class CommandJoinTeam implements CommandExecutor {
    
    CastleRaidMain plugin;
    ArrayList<String> teams;
    
    public CommandJoinTeam(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        this.teams = new ArrayList<>();
        
        for (Teams team : CastleRaidMain.Teams.values()) {
            this.teams.add(team.toString().toUpperCase());
        }
        
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length != 1 || !teams.contains(args[0].toUpperCase())) {
            return false;
        }
        
        if (sender instanceof Player) {
            
            Player player = (Player) sender;
            CastleRaidPlayer crPlayer = plugin.getCrPlayers().get(player.getUniqueId());
            
            crPlayer.setTeam(CastleRaidMain.Teams.valueOf(args[0].toUpperCase()));
            
        }
        
        return true;
        
    }
    
    public class JoinTeamCompletion implements TabCompleter {
        
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            
            return teams;
            
        }
        
    }
    
    
}
