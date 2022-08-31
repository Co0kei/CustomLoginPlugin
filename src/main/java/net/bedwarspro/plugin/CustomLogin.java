package net.bedwarspro.plugin;

import net.bedwarspro.plugin.http.HTTPRequests;
import net.bedwarspro.plugin.http.NamelessAPI;
import net.bedwarspro.plugin.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class CustomLogin extends JavaPlugin {

	private Config configuration;
	private NamelessAPI namelessAPI;
	private HTTPRequests httpRequests;

	@Override
	public void onEnable() {
		announcePluginStart();
		loadConfig();
		setupHTTPRequests();
		setupNamelessAPI();
		registerEvents();
	}

	private void announcePluginStart() {
		this.getLogger().log(Level.INFO, "Plugin online!");
	}

	private void loadConfig() {
		this.configuration = new Config(this);
	}

	private void setupHTTPRequests() {
		this.httpRequests = new HTTPRequests(this);
	}

	private void setupNamelessAPI() {
		this.namelessAPI = new NamelessAPI(this);
	}

	private void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
	}

	public Config getConfiguration() {
		return this.configuration;
	}

	public HTTPRequests getHttpRequests() {
		return this.httpRequests;
	}

	public NamelessAPI getNamelessAPI() {
		return this.namelessAPI;
	}

}
