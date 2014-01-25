package org.summit27.gameranks.listeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.summit27.gameranks.GameRanks;
import org.summit27.gameranks.utils.GameRanksHandler;
import org.summit27.gameranks.utils.RanksHandler;

public class Commands implements CommandExecutor {

	private GameRanks plugin;
	
	public Commands(GameRanks plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to access that command.");
			return true;
		}
		switch (cmd.getName().toLowerCase()) {
		
		case "rankup": { 
			new RanksHandler(plugin).onRankUp(sender, args);
			return true;
		}
		
		case "rankdown": { 
			new RanksHandler(plugin).onRankDown(sender, args);
			return true;
		}
		
		case "rank": { 
			new RanksHandler(plugin).onRank(sender);
			return true;
		}
		
		case "ranks": { 
			new RanksHandler(plugin).onRanks(sender, args);
			return true;
		}
		
		case "gr": {
			if (args.length == 0) {
				new GameRanksHandler(this.plugin).onHelp(sender);
				return true;
			} else if (args.length == 1) {
				switch (args[0].toLowerCase()) {
				
				case "help": {
					new GameRanksHandler(this.plugin).onHelp(sender);
					return true;
				}
				
				case "version": {
					if (((Player)sender).hasPermission("gameranks.version"))
						new GameRanksHandler(this.plugin).onVersion(sender);
					else
						((Player)sender).sendRawMessage(ChatColor.RED + "You don't have access to view the version of Game Ranks.");
					return true;
				}
				
				case "reload": {
					if (((Player)sender).hasPermission("gameranks.admin"))
						new GameRanksHandler(this.plugin).onReloadConfig(sender);
					else
						((Player)sender).sendRawMessage(ChatColor.RED + "You don't have access to the admin function reload.");
					return true;
				}
				
				}
			}
		}
		
		}
		return false;
	}
}