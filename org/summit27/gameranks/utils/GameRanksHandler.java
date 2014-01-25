package org.summit27.gameranks.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.summit27.gameranks.GameRanks;

public class GameRanksHandler {
	
	private GameRanks plugin;
	
	public GameRanksHandler(GameRanks plugin) {
		this.plugin = plugin;
	}

	public void onHelp(CommandSender sender) {
		Player player = (Player)sender;
		player.sendRawMessage(ChatColor.YELLOW + "--------------- " + ChatColor.WHITE + "Game Ranks Help" + ChatColor.YELLOW + " ---------------");
		player.sendRawMessage(ChatColor.GOLD + "/rank " + ChatColor.WHITE + ": This command shows you the rank statistics.");
		player.sendRawMessage(ChatColor.GOLD + "/ranks " + ChatColor.WHITE + ": This command shows you the avaliable ranks!");
		player.sendRawMessage(ChatColor.GOLD + "/rankup " + ChatColor.WHITE + ": This command will purchase your next rank.");
		player.sendRawMessage(ChatColor.GOLD + "/rankdown " + ChatColor.WHITE + ": This command will refund your previous rank for by repaying you a set amount.");
	}
	
	public void onVersion(CommandSender sender) {
		Player player = (Player)sender;
		player.sendRawMessage("Game Ranks - Version " + this.plugin.getDescription().getVersion());
	}
	
	public void onReloadConfig(CommandSender sender) {
		try {
			for (Player player : this.plugin.getServer().getOnlinePlayers()) {
				if (this.plugin.config.usersYaml.getString("users." + player.getName()) == null) {
					this.plugin.config.addPlayer(player);
				}
				this.plugin.perm.playerRemovePermissions(player.getName());
			}
			this.plugin.config.configYaml.load(this.plugin.config.configFile);
			for (Player player : this.plugin.getServer().getOnlinePlayers()) {
				if (this.plugin.config.usersYaml.getString("users." + player.getName()) == null) {
					this.plugin.config.addPlayer(player);
				}
				this.plugin.perm.playerAddPermissions(player.getName());
			}
			((Player)sender).sendRawMessage(ChatColor.GREEN + "Reloaded configuration file correctly.");
		}
		catch (Exception e) {
			((Player)sender).sendRawMessage(ChatColor.RED + "Couldn't reload configuration correctly!");
			e.printStackTrace();
		}
	}
}