package org.summit27.gameranks.utils;

import java.util.List;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.summit27.gameranks.GameRanks;

public class PermissionsHandler {
	
	private GameRanks plugin;
 
 	public PermissionsHandler(GameRanks plugin) {
 		this.plugin = plugin;
 	}
	
	public void addPermissions(String name, List<String> list) {
		for (World world: plugin.getServer().getWorlds())
			for(String permission: list)
				plugin.permission.playerAdd(world.getName(), name, permission);
	}
	
	public void playerAddPermissions(String name) {
		try {
			if (plugin.config.usersYaml.getString("users." + name) != null) {
				Set<String> ranks = plugin.config.configYaml.getConfigurationSection("ranks").getKeys(false);
				String rank = plugin.config.usersYaml.getString("users." + name);
				for (String classes : ranks) {
					plugin.perm.addPermissions(name, plugin.config.configYaml.getStringList("ranks." + classes + ".permissions"));
					if (classes.equals(rank))
						break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playerRemoveOffline(OfflinePlayer player) {
		try {
			if (plugin.config.usersYaml.getString("users." + player.getName()) != null) {
				Set<String> ranks = plugin.config.configYaml.getConfigurationSection("ranks").getKeys(false);
				String rank = plugin.config.usersYaml.getString("users." + player.getName());
				for (String classes : ranks) {
					plugin.perm.removePermissions(player.getName(), plugin.config.configYaml.getStringList("ranks." + classes + ".permissions"));
					if (classes.equals(rank))
						break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playerRemovePermissions(String name) {
		try {
			if (plugin.config.usersYaml.getString("users." + name) != null) {
				Set<String> ranks = plugin.config.configYaml.getConfigurationSection("ranks").getKeys(false);
				String rank = plugin.config.usersYaml.getString("users." + name);
				for (String classes : ranks) {
					plugin.perm.removePermissions(name, plugin.config.configYaml.getStringList("ranks." + classes + ".permissions"));
					if (classes.equals(rank))
						break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removePermissions(String name, List<String> list) {
		for (World world: plugin.getServer().getWorlds())
			for(String permission: list)
				plugin.permission.playerRemove(world.getName(), name, permission);
	}
}
