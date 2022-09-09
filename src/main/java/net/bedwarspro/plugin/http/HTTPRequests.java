package net.bedwarspro.plugin.http;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.bedwarspro.plugin.Config;
import net.bedwarspro.plugin.CustomLogin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPRequests {

	private final CustomLogin plugin;
	private String namelessAPIUrl;
	private String namelessAPIKey;
	private String bedwarsProAPIUrl;
	private String bedwarsProAPIKey;
	private boolean isDebug;

	public HTTPRequests(final CustomLogin plugin) {
		this.plugin = plugin;
		assignVariables();
	}

	private void assignVariables() {
		Config config = this.plugin.getConfiguration();
		this.namelessAPIUrl = config.getNamelessAPIUrl();
		this.namelessAPIKey = config.getNamelessAPIKey();
		this.bedwarsProAPIUrl = config.getBedwarsProAPIUrl();
		this.bedwarsProAPIKey = config.getBedwarsProAPIKey();
		this.isDebug = config.isDebug();
	}

	protected JsonObject getHTTP(API api, String route) throws IOException {
		HttpURLConnection connection = setupConnection(api, route, "GET");
		return parseResponse(connection);
	}

	protected void postHTTP(API api, String route, JsonObject body) throws IOException {
		HttpURLConnection connection = setupConnection(api, route, "POST");
		writeBody(connection, body);
		parseResponse(connection);
	}

	private HttpURLConnection setupConnection(API api, String route, String method) throws IOException {
		URL url = new URL(getURL(api, route));
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		if (api == API.NAMELESS) // Bw pro api uses the url for the key.
			connection.setRequestProperty("Authorization", "Bearer " + this.namelessAPIKey);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("charset", "utf-8");
		connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		connection.setDoOutput(true);
		logDebug("Opened '" + method + "' connecton to '" + url.toString() + "'");
		return connection;
	}

	private String getURL(API api, String route) {
		if (api == API.NAMELESS) {
			return this.namelessAPIUrl + route;
		} else if (api == API.BEDWARSPRO) {
			return this.bedwarsProAPIUrl + route + "&key=" + this.bedwarsProAPIKey;
		} else {
			throw new Error("Unknown API type.");
		}
	}

	private JsonObject parseResponse(HttpURLConnection connection) throws IOException {
		final InputStream inputStream = connection.getInputStream();
		final InputStreamReader reader = new InputStreamReader(inputStream);
		JsonObject response = new Gson().fromJson(CharStreams.toString(reader), JsonObject.class);
		logDebug("Received response '" + response + "'");
		return response;
	}

	private void writeBody(HttpURLConnection connection, JsonObject body) throws IOException {
		final OutputStream out = connection.getOutputStream();
		out.write(body.toString().getBytes(StandardCharsets.UTF_8));
		logDebug("Written body '" + body.toString() + "'");
	}

	private void logDebug(String message) {
		if (this.isDebug)
			this.plugin.getLogger().info("[DEBUG] " + message);
	}

}
