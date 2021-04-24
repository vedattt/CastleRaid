package xyz.vedat.castleraid.event;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import xyz.vedat.castleraid.CastleRaidMain;
import xyz.vedat.castleraid.CastleRaidPlayer;
import xyz.vedat.castleraid.CastleRaidMain.GameState;
import xyz.vedat.castleraid.CastleRaidMain.Teams;

public class CastleRaidChatEvent implements Listener {
    
    CastleRaidMain plugin;
    
    public CastleRaidChatEvent(CastleRaidMain plugin) {
        
        this.plugin = plugin;
        
    }
    
    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        
        Player player = event.getPlayer();
        CastleRaidPlayer crPlayer = plugin.getCrPlayer(player);
        Set<Player> recipients = event.getRecipients();
        
        if (plugin.getGameState() == GameState.RUNNING && crPlayer.getTeam() != Teams.SPECTATOR) {
            
            String teamColor = ChatColor.valueOf(crPlayer.getTeam() == Teams.SPECTATOR ? "GRAY" : crPlayer.getTeam().toString()).toString();
            String teamString = teamColor + crPlayer.getTeam().toString() + ChatColor.RESET;
            String format = ChatColor.RESET + "[" + teamString + "] " + teamColor + "%1$s" + ChatColor.RESET + ": %2$s";
            
            if (event.getMessage().startsWith(",")) {
                
                event.setMessage(event.getMessage().substring(1));
                
                event.setFormat("[" + ChatColor.GOLD + "ALL" + ChatColor.RESET + "]" + format);
                
                return;
                
            }
            
            recipients.removeIf(otherPlayer -> plugin.getCrPlayer(otherPlayer).getTeam() != crPlayer.getTeam() && 
                                               plugin.getCrPlayer(otherPlayer).getTeam() != Teams.SPECTATOR);
            
            event.setFormat("[" + ChatColor.GREEN + "TEAM" + ChatColor.RESET + "]" + format);
            
        } else if (crPlayer.getTeam() == Teams.SPECTATOR) {
            event.setFormat("[" + ChatColor.GRAY + "SPECTATOR" + ChatColor.RESET + "] " + ChatColor.GRAY + "%1$s: %2$s");
        } else {
            event.setFormat("[" + ChatColor.GRAY + "LOBBY" + ChatColor.RESET + "] %1$s: " + ChatColor.GRAY + "%2$s");
        }
        
    }
    
}
