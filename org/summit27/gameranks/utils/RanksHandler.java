package org.summit27.gameranks.utils;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.summit27.gameranks.GameRanks;

public class RanksHandler {
	private GameRanks plugin;
	
	public RanksHandler(GameRanks plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onRanks(CommandSender sender, String[] args) {
		Player player = (Player)sender;
		ArrayList array = this.plugin.config.ArrayHandler();
		if (array.isEmpty()) {
			player.sendRawMessage(ChatColor.RED + "Sorry, no ranks are configured. You should tell staff immediately.");
			return;
		}
		for (int i = 0; i < array.size(); i++) {
			ArrayList data = (ArrayList)array.get(i);
			String name = (String)data.get(0);
			if ((args.length != 1) || name.toLowerCase().equals(args[0].toLowerCase())) {
				ArrayList<String> description = (ArrayList)data.get(1);
				
				player.sendRawMessage(ChatColor.YELLOW + "--------------- " + ChatColor.WHITE + name + ChatColor.YELLOW + " ---------------");
				
				String edit = ChatColor.GOLD + "Description " + ChatColor.WHITE + ": " + (String)description.get(0);
				
				description.set(0, edit);
				for (String line : description)
					player.sendRawMessage(line);
					
				player.sendRawMessage(ChatColor.GOLD + "Price " + ChatColor.WHITE + ": " + getSymbol() + data.get(4));
			}
		}
	}
	
	public String getSymbol() {
		String symbol = this.plugin.config.configYaml.getString("symbol");
		if (symbol == null)
			symbol = "";
		return symbol;
	}
	
	public static boolean StringComparison(String inputString, String[] items) {
		for (int i = 0; i < items.length; i++) {
			if (inputString.contains(items[i])) {
				return true;
			}
		}
		return false;
	}
	
	public void onRankUp(CommandSender sender, String[] args) throws NullPointerException {
		Player player = (Player)sender;
		Set<String> ranks = this.plugin.config.configYaml.getConfigurationSection("ranks").getKeys(false);
		String rank = this.plugin.config.usersYaml.getString("users." + player.getName());String newRank = "";
		for (String classes : ranks) {
			if (classes.equals(rank)) {
				newRank = "1";
			} else if (newRank.equals("1")) {
				newRank = classes;
				break;
			}
		}
		if (newRank == "1") {
			player.sendRawMessage(ChatColor.RED + "You are at the max rank, you can't rank up anymore.");
			return;
		}
		if ((!this.plugin.economy.isEnabled()) || (this.plugin.economy == null)) {
			player.sendRawMessage(ChatColor.RED + "No Economy plugin is enabled or working, it must be supported by Vault. Is this an error?");
			return;
		}
		int price = this.plugin.config.configYaml.getInt("ranks." + newRank + ".price");
		if (!this.plugin.economy.has(player.getName(), price)) {
			player.sendRawMessage(ChatColor.RED + "You don't have the required price to purchase the next rank.");
			return;
		}
		player.sendRawMessage(ChatColor.GREEN + "You have been ranked up to " + this.plugin.utils.replaceColors(this.plugin.config.configYaml.getString(new StringBuilder("ranks.").append(newRank).append(".name").toString())));
		this.plugin.economy.withdrawPlayer(player.getName(), price);
		this.plugin.config.setGroup(player.getName(), newRank);
		updateChat(player, newRank);
		updateGroup(player, rank, newRank);
		this.plugin.perm.addPermissions(player.getName(), this.plugin.config.configYaml.getStringList("ranks." + newRank + ".permissions"));
	}
	
	private void updateGroup(Player player, String rank, String newRank) {
		try {
			String group = this.plugin.config.configYaml.getString("ranks." + rank + ".group");
			String newGroup = this.plugin.config.configYaml.getString("ranks." + newRank + ".group");
			
			if(group == null)
				return;
			if(newGroup == null)
				return;
			
			for(org.bukkit.World world: plugin.getServer().getWorlds())
				plugin.permission.playerRemoveGroup(world, player.getName(), group);
			
			if(newGroup.equals(""))
				return;
			
			for(org.bukkit.World world: plugin.getServer().getWorlds())
				plugin.permission.playerAddGroup(world, player.getName(), newGroup);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateChat(Player player, String newRank) {
		try {
			String prefix = this.plugin.config.configYaml.getString("ranks." + newRank + ".prefix");
			String suffix = this.plugin.config.configYaml.getString("ranks." + newRank + ".suffix");
			
			if(prefix == null)
				return;
			if(suffix == null)
				return;
			if(prefix.trim().equals(""))
				prefix = "";
			if(suffix.trim().equals(""))
				suffix = "";
			
			for(org.bukkit.World world: plugin.getServer().getWorlds())
					this.plugin.chat.setPlayerPrefix(world, player.getName(), plugin.utils.replaceColors(prefix));
			
			for(org.bukkit.World world: plugin.getServer().getWorlds())
					this.plugin.chat.setPlayerSuffix(world, player.getName(), plugin.utils.replaceColors(suffix));	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRankDown(CommandSender sender, String[] args) {
		Player player = (Player)sender;
		Set<String> ranks = this.plugin.config.configYaml.getConfigurationSection("ranks").getKeys(false);
		String rank = this.plugin.config.usersYaml.getString("users." + player.getName());
		String newRank = this.plugin.config.getFirst();
		if (rank.equals(this.plugin.config.getFirst())) {
			player.sendRawMessage(ChatColor.RED + "You are at the lowest rank, you can't rank down anymore.");
			return;
		}
		for (String classes : ranks) {
			if (classes.equals(rank))
				break;
			newRank = classes;
		}
		if(this.plugin.config.getFirst() == null) {
			player.sendRawMessage(ChatColor.RED + "Configuration error, contact staff.");
			return;
		}
		if ((!this.plugin.economy.isEnabled()) || (this.plugin.economy == null)) {
			player.sendRawMessage(ChatColor.RED + "No Economy plugin is enabled or working, it must be supported by Vault. Is this an error?");
			return;
		}
		int price = this.plugin.config.configYaml.getInt("ranks." + rank + ".refund");
		player.sendRawMessage(ChatColor.GREEN + "You have been ranked down to " + this.plugin.utils.replaceColors(this.plugin.config.configYaml.getString(new StringBuilder("ranks.").append(newRank).append(".name").toString())));
		this.plugin.economy.depositPlayer(player.getName(), price);
		this.plugin.config.setGroup(player.getName(), newRank);
		updateChat(player, newRank);
		updateGroup(player, rank, newRank);
		this.plugin.perm.removePermissions(player.getName(), this.plugin.config.configYaml.getStringList("ranks." + rank + ".permissions"));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onRank(CommandSender sender) {
		Player player = (Player)sender;
		String group = this.plugin.config.usersYaml.getString("users." + player.getName());
		ArrayList array = this.plugin.config.ArrayHandler();
		if (array.isEmpty()) {
			player.sendRawMessage(ChatColor.RED + "Sorry, no ranks are configured. You should tell staff immediately.");
			return;
		}
		for (int i = 0; i < array.size(); i++) {
			ArrayList data = (ArrayList)array.get(i);
			String name = (String)data.get(0);
			if (data.get(3).equals(group)) {
				ArrayList<String> description = (ArrayList)data.get(1);
				
				player.sendRawMessage(ChatColor.YELLOW + "--------------- " + ChatColor.WHITE + name + ChatColor.YELLOW + " ---------------");
				
				String edit = ChatColor.GOLD + "Description " + ChatColor.WHITE + ": " + (String)description.get(0);
				
				description.set(0, edit);
				for (String line : description)
					player.sendRawMessage(line);
				
				player.sendRawMessage(ChatColor.GOLD + "Refund " + ChatColor.WHITE + ": " + getSymbol() + data.get(5));
			}
		}
	}
}