package org.summit27.gameranks.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.summit27.gameranks.GameRanks;

public class ConfigHandler {
	private GameRanks plugin;
	public File configFile;
	public File usersFile;
	public FileConfiguration configYaml;
	public FileConfiguration usersYaml;
	
	public ConfigHandler(GameRanks plugin) {
		this.plugin = plugin;
		this.configFile = new File(plugin.getDataFolder(), "config.yml");
		this.usersFile = new File(plugin.getDataFolder(), "users.yml");
		try {
			onConfig();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void onConfig() {
		try {
			if (!this.configFile.exists()) {
				this.configFile.getParentFile().mkdirs();
				copy(this.plugin.getResource("config.yml"), this.configFile);
				this.plugin.getLogger().warning("Can't find required \"config.yml\", will create a new one.");
			}
			if (!this.usersFile.exists()) {
				this.usersFile.getParentFile().mkdirs();
				copy(this.plugin.getResource("users.yml"), this.usersFile);
				this.plugin.getLogger().warning("Can't find required \"users.yml\", will create a new one.");
			}
			this.configYaml = YamlConfiguration.loadConfiguration(this.configFile);
			this.usersYaml = YamlConfiguration.loadConfiguration(this.usersFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkConfig() {
		try {
			if (!this.configFile.exists()) {
				return false;
			}
			if (!this.usersFile.exists()) {
				return false;
			}
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList ArrayHandler() {
		ArrayList<ArrayList> array = new ArrayList();
		Set<String> ranks = this.configYaml.getConfigurationSection("ranks").getKeys(false);
		for (String classes : ranks) {
			ArrayList<Object> data = new ArrayList();ArrayList<Object> description = new ArrayList();ArrayList<Object> permissions = new ArrayList();
			for (String line : this.configYaml.getStringList("ranks." + classes + ".description")) {
				description.add(this.plugin.utils.replaceColors(line));
			}
			for (String line : this.configYaml.getStringList("ranks." + classes + ".permissions")) {
				permissions.add(line);
			}
			data.add(this.plugin.utils.replaceColors(this.configYaml.get("ranks." + classes + ".name").toString()));
			data.add(description);
			data.add(permissions);
			data.add(classes);
			data.add(Integer.valueOf(this.configYaml.getInt("ranks." + classes + ".price")));
			data.add(Integer.valueOf(this.configYaml.getInt("ranks." + classes + ".refund")));
			array.add(data);
		}
		return array;
	}
	
	public void setGroup(String name, String group) {
		try {
			this.usersYaml.set("users." + name, group);
			this.usersYaml.save(this.usersFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			this.configYaml.save(this.configFile);
			this.usersYaml.save(this.usersFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addPlayer(Player player) {
		if (this.usersYaml.getString("users." + player.getName()) == null) {
			String name = player.getName();
			this.usersYaml.set("users." + name, getDefault());
			try {
				this.usersYaml.save(this.plugin.config.usersFile);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getDefault() {
		return this.configYaml.getString("default");
	}
	
	public String getFirst() {
		String first = null;
		Set<String> ranks = this.configYaml.getConfigurationSection("ranks").getKeys(false);
		for(String rank: ranks) {
			first = rank;
			return first;
		}
		return first;
	}
}