package net.bedwarspro.plugin;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	private final CustomLogin plugin;
	private String namelessAPIUrl;
	private String namelessAPIKey;
	private String bedwarsProAPIUrl;
	private String bedwarsProAPIKey;
	private boolean debug;

	public Config(final CustomLogin plugin) {
		this.plugin = plugin;
		initialiseConfig();
		assignConstants();
	}

	private void initialiseConfig() {
		copyDefaultConfig();
		saveDefaults();
	}

	private void copyDefaultConfig() {
		plugin.getConfig().options().copyDefaults();
	}

	private void saveDefaults() {
		plugin.saveDefaultConfig();
	}

	private void assignConstants() {
		final FileConfiguration configuration = plugin.getConfig();
		this.namelessAPIUrl = configuration.getString("nameless-api-url");
		this.namelessAPIKey = configuration.getString("nameless-api-key");
		this.bedwarsProAPIUrl = configuration.getString("bedwarspro-api-url");
		this.bedwarsProAPIKey = configuration.getString("bedwarspro-api-key");
		this.debug = configuration.getBoolean("debug");
	}

	public String getNamelessAPIUrl() {
		return this.namelessAPIUrl;
	}

	public String getNamelessAPIKey() {
		return this.namelessAPIKey;
	}

	public String getBedwarsProAPIUrl() {
		return this.bedwarsProAPIUrl;
	}

	public String getBedwarsProAPIKey() {
		return bedwarsProAPIKey;
	}

	public boolean isDebug() {
		return this.debug;
	}

}
