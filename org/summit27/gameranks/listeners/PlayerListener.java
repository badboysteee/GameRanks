package org.summit27.gameranks.listeners;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.summit27.gameranks.GameRanks;

public final class PlayerListener implements Listener {
	private GameRanks plugin;
	
	public PlayerListener(GameRanks plugin) {
		this.plugin = plugin;
		for (OfflinePlayer player : plugin.getServer().getOfflinePlayers())
			if (plugin.config.usersYaml.getString("users." + player.getName()) != null)
				plugin.perm.playerRemoveOffline(player);
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (plugin.config.usersYaml.getString("users." + player.getName()) == null)
				plugin.config.addPlayer(player);
			plugin.perm.playerRemovePermissions(player.getName());
			plugin.perm.playerAddPermissions(player.getName());
		}
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.config.usersYaml.getString("users." + player.getName()) == null)
			this.plugin.config.addPlayer(player);
		this.plugin.perm.playerRemovePermissions(player.getName());
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.config.usersYaml.getString("users." + player.getName()) == null)
			this.plugin.config.addPlayer(player);
		this.plugin.perm.playerRemovePermissions(player.getName());
		this.plugin.perm.playerAddPermissions(player.getName());
	}
}