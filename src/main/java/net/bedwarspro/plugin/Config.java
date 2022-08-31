package net.bedwarspro.plugin;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	private final CustomLogin plugin;
	private String apiUrl;
	private String apiKey;

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
		this.apiUrl = configuration.getString("api-url");
		this.apiKey = configuration.getString("api-key");
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public String getApiKey() {
		return apiKey;
	}
	
}
