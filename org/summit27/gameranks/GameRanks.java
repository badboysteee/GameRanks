package org.summit27.gameranks;

import java.io.IOException;
import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.summit27.gameranks.listeners.Commands;
import org.summit27.gameranks.listeners.Metrics;
import org.summit27.gameranks.listeners.PlayerListener;
import org.summit27.gameranks.utils.ConfigHandler;
import org.summit27.gameranks.utils.PermissionsHandler;
import org.summit27.gameranks.utils.Utils;

public final class GameRanks extends JavaPlugin {
	
	public ConfigHandler config = null;
	public Commands command = null;
	public Permission permission = null;
	public Economy economy = null;
	public PermissionsHandler perm = null;
	public Utils utils = null;
	public Chat chat = null;

	public void onEnable() {
		setupPermissions();
		setupEconomy();
		setupChat();
		if (this.permission == null)
			getLogger().log(Level.SEVERE, "Plugin requires Vault with a supported permissions plugin, plugin will disable itself.");
		if (this.economy == null)
			getLogger().log(Level.SEVERE, "Plugin requires Vault with a supported economy plugin, plugin will disable itself.");
		if (this.chat == null)
			getLogger().log(Level.SEVERE, "Plugin requires Vault with a supported chat plugin, plugin will disable itself.");
		if ((this.economy == null) || (this.permission == null) || (this.chat == null)) {
			setEnabled(false);
			return;
		}
		this.utils = new Utils(this);
		this.config = new ConfigHandler(this);
		this.perm = new PermissionsHandler(this);
		this.command = new Commands(this);
 
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
 
		getCommand("rank").setExecutor(new Commands(this));
		getCommand("ranks").setExecutor(new Commands(this));
		getCommand("rankup").setExecutor(new Commands(this));
		getCommand("rankdown").setExecutor(new Commands(this));
		getCommand("gr").setExecutor(new Commands(this));
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void onDisable() {
		if (this.perm != null)
			for (Player player : getServer().getOnlinePlayers()) {
				if (this.config.usersYaml.getString("users." + player.getName()) == null)
					this.config.addPlayer(player);
				this.perm.playerRemovePermissions(player.getName());
			}
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null)
			this.permission = ((Permission)permissionProvider.getProvider());
		return this.permission != null;
	}
	 private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null)
			chat = chatProvider.getProvider();
		return (chat != null);
	 }
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null)
			this.economy = ((Economy)economyProvider.getProvider());
		return this.economy != null;
	}
}