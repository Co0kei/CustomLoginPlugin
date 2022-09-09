package net.bedwarspro.plugin.http;

import com.google.gson.JsonObject;
import net.bedwarspro.plugin.CustomLogin;
import net.bedwarspro.plugin.model.Code;

import java.io.IOException;
import java.util.UUID;

public class NamelessAPI {

	private final HTTPRequests httpRequests;
	private JsonObject latestUserResponse;

	private final String CUSTOM_LOGIN_ROUTE = "/customlogin";
	private final String GET_CODE_ROUTE = CUSTOM_LOGIN_ROUTE + "/get-code";
	private final String POST_CODE_ROUTE = CUSTOM_LOGIN_ROUTE + "/post-code";
	private final String GET_USER_BY_UUID_ROUTE = "/users/integration_id:minecraft:";
	private final String HTTP_RESPONSE_400 = "Server returned HTTP response code: 400";
	private final String ERROR = "error";

	public NamelessAPI(final CustomLogin plugin) {
		this.httpRequests = plugin.getHttpRequests();
	}

	public boolean isPlayerRegistered(UUID uuid) {
		try {
			return checkRegistered(uuid);
		} catch (IOException ioException) {
			return handleRegisteredException(ioException);
		}
	}

	private boolean checkRegistered(UUID uuid) throws IOException {
		this.latestUserResponse = this.httpRequests.getHTTP(API.NAMELESS, GET_USER_BY_UUID_ROUTE + trimUUID(uuid));
		return true;
	}

	private String trimUUID(UUID uuid) {
		return uuid.toString().replace("-", "");
	}

	private boolean handleRegisteredException(IOException ioException) {
		if (ioException.getMessage().startsWith(HTTP_RESPONSE_400)) {
			// This error message will be shown if they are not yet registered. No need to log.
		} else {
			ioException.printStackTrace();
		}
		return false;
	}

	public Code requestCodeFor(String minecraftName) {
		try {
			return requestCode(minecraftName);
		} catch (IOException ioException) {
			return handleRequestCodeException(ioException);
		}
	}

	private Code requestCode(String minecraftName) throws IOException {
		JsonObject response = this.httpRequests.getHTTP(API.NAMELESS, GET_CODE_ROUTE + "&minecraft_name=" + minecraftName);
		return parseCodeFromResponse(response);
	}

	private Code parseCodeFromResponse(JsonObject response) {
		if (response.has(ERROR)) {
			return new Code();
		} else {
			return new Code(response);
		}
	}

	private Code handleRequestCodeException(IOException ioException) {
		ioException.printStackTrace();
		return new Code();
	}

	public void postCode(Code code) {
		try {
			this.httpRequests.postHTTP(API.NAMELESS, POST_CODE_ROUTE, code.getAsJson());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public long getLastConnectedRegisteredTimestamp() {
		return this.latestUserResponse.get("registered_timestamp").getAsLong();
	}

}
