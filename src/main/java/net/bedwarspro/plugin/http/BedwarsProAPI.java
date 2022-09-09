package net.bedwarspro.plugin.http;

import com.google.gson.JsonObject;
import net.bedwarspro.plugin.CustomLogin;

import java.io.IOException;
import java.util.UUID;

public class BedwarsProAPI {

	private final HTTPRequests httpRequests;
	private final String GET_USER_BY_UUID_ROUTE = "/player?uuid=";

	public BedwarsProAPI(final CustomLogin plugin) {
		this.httpRequests = plugin.getHttpRequests();
	}

	public boolean hasPlayerPlayedBefore(UUID uuid) {
		try {
			return checkPlayerPlayed(uuid);
		} catch (IOException ioException) {
			return handleError(ioException);
		}
	}

	private boolean checkPlayerPlayed(UUID uuid) throws IOException {
		JsonObject response = this.httpRequests.getHTTP(API.BEDWARSPRO, GET_USER_BY_UUID_ROUTE + trimUUID(uuid));
		if (response.has("success") && response.get("success").getAsBoolean()) {
			return doesPlayerExist(response);
		} else {
			// Error
			return false;
		}
	}

	private boolean doesPlayerExist(JsonObject response) {
		if (!response.get("player").isJsonNull()) {
			return true;
		} else {
			return false;
		}
	}

	private String trimUUID(UUID uuid) {
		return uuid.toString().replace("-", "");
	}

	private boolean handleError(IOException ioException) {
		ioException.printStackTrace();
		return false;
	}


}
