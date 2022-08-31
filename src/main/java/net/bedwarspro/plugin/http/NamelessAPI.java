package net.bedwarspro.plugin.http;

import com.google.gson.JsonObject;
import net.bedwarspro.plugin.CustomLogin;
import net.bedwarspro.plugin.model.Code;

import java.io.IOException;
import java.util.UUID;

public class NamelessAPI {

	private final HTTPRequests httpRequests;
	private JsonObject latestUserResponse = null;
	private final String CUSTOM_LOGIN_ROUTE = "/customlogin";
	private final String GET_CODE_ROUTE = CUSTOM_LOGIN_ROUTE + "/get-code";
	private final String POST_CODE_ROUTE = CUSTOM_LOGIN_ROUTE + "/post-code";
	private final String GET_USER_BY_UUID_ROUTE = "/users/integration_id:minecraft:";
	private final String HTTP_RESPONSE_400 = "Server returned HTTP response code: 400";

	public NamelessAPI(final CustomLogin plugin) {
		this.httpRequests = plugin.getHttpRequests();
	}

	/**
	 * @param uuid The player's minecraft uuid
	 * @return true if there is a nameless user with that uuid
	 */
	public boolean isPlayerRegistered(UUID uuid) {
		try {
			// Player registered.
			this.latestUserResponse = this.httpRequests.getHTTP(GET_USER_BY_UUID_ROUTE + trimUUID(uuid));
			return true;

		} catch (IOException ioException) {

			if (ioException.getMessage().startsWith(HTTP_RESPONSE_400)) {
				// This error message will be shown if they are not yet registered.
			} else {
				// Unknown connection issue.
				ioException.printStackTrace();
			}
			return false;
		}
	}

	private String trimUUID(UUID uuid) {
		return uuid.toString().replace("-", "");
	}

	/**
	 * @param minecraftName The player's minecraft name
	 * @return A valid non-expired code or null
	 */
	public Code requestCodeFor(String minecraftName) {
		try {
			// Has active code generate.
			JsonObject response = this.httpRequests.getHTTP(GET_CODE_ROUTE + "&minecraft_name=" + minecraftName);
			return new Code(response);

		} catch (IOException ioException) {

			if (ioException.getMessage().startsWith(HTTP_RESPONSE_400)) {
				// This error message will be shown if they have no code.
			} else {
				// Unknown connection issue.
				ioException.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * @param code The newly generated code
	 */
	public void postCode(Code code) {
		try {
			// Successful post.
			this.httpRequests.postHTTP(POST_CODE_ROUTE, code.getAsJson());

		} catch (IOException ioException) {

			if (ioException.getMessage().startsWith(HTTP_RESPONSE_400)) {
				// This error message will be shown if a user already has a code genereated.
			} else {
				// Unknown connection issue.
				ioException.printStackTrace();
			}
		}
	}

	/**
	 * Returns the latest obtained users registered timestamp. This must be called immediately after {@link NamelessAPI#isPlayerRegistered(UUID)}
	 *
	 * @return The timestamp at which the latest connection registered on our website
	 */
	public long getPlayerRegisteredTime() {
		return this.latestUserResponse.get("registered_timestamp").getAsLong();
	}

}
