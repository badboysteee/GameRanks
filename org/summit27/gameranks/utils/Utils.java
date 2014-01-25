package org.summit27.gameranks.utils;

import org.summit27.gameranks.GameRanks;

public class Utils {
	@SuppressWarnings("unused")
	private GameRanks plugin;
	
	public Utils(GameRanks plugin) {
		this.plugin = plugin;
	}
	
	public String replaceColors(String string) {
		return string.replaceAll("(?i)&([a-k0-9])", "§$1");
	}
}