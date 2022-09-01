package net.bedwarspro.plugin;

import net.bedwarspro.plugin.http.HTTPRequests;
import net.bedwarspro.plugin.http.NamelessAPI;
import net.bedwarspro.plugin.listener.JoinListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomLogin extends JavaPlugin {

	private Config configuration;
	private NamelessAPI namelessAPI;
	private HTTPRequests httpRequests;

	@Override
	public void onEnable() {
		announcePluginStart();
		addLogFilter();
		loadConfig();
		setupHTTPRequests();
		setupNamelessAPI();
		registerEvents();
	}

	private void announcePluginStart() {
		this.getLogger().info("Plugin online!");
	}

	private void addLogFilter() {
		((Logger) LogManager.getRootLogger()).addFilter(new LogFilter());
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
