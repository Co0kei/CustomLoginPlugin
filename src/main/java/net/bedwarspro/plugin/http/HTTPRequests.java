package net.bedwarspro.plugin.http;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
	private String apiUrl;
	private String apiKey;

	public HTTPRequests(final CustomLogin plugin) {
		this.plugin = plugin;
		loadAPICredentials();
	}

	private void loadAPICredentials() {
		this.apiUrl = this.plugin.getConfiguration().getApiUrl();
		this.apiKey = this.plugin.getConfiguration().getApiKey();
	}

	/**
	 * @param route The route to perform a GET request to
	 * @return the response as json
	 */
	protected JsonObject getHTTP(String route) throws IOException {
		HttpURLConnection connection = setupConnection(route, "GET");
		return parseResponse(connection);
	}

	/**
	 * @param route The route to perform a POST request to
	 * @param body  The json body to be sent
	 */
	protected void postHTTP(String route, JsonObject body) throws IOException {
		HttpURLConnection connection = setupConnection(route, "POST");
		writeBody(connection, body);
		parseResponse(connection); // This is required for the post to succeed
	}

	private HttpURLConnection setupConnection(String route, String method) throws IOException {
		URL url = new URL(this.apiUrl + route);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Authorization", "Bearer " + this.apiKey);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("charset", "utf-8");
		connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		connection.setDoOutput(true);
		return connection;
	}

	private JsonObject parseResponse(HttpURLConnection connection) throws IOException {
		final InputStream inputStream = connection.getInputStream();
		final InputStreamReader reader = new InputStreamReader(inputStream);
		return new Gson().fromJson(CharStreams.toString(reader), JsonObject.class);
	}

	private void writeBody(HttpURLConnection connection, JsonObject body) throws IOException {
		final OutputStream out = connection.getOutputStream();
		out.write(body.toString().getBytes(StandardCharsets.UTF_8));
	}

}
