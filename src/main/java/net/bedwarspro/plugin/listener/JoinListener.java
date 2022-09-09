package net.bedwarspro.plugin.listener;

import net.bedwarspro.plugin.CustomLogin;
import net.bedwarspro.plugin.http.BedwarsProAPI;
import net.bedwarspro.plugin.http.NamelessAPI;
import net.bedwarspro.plugin.model.Code;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

public class JoinListener implements Listener {

	private final NamelessAPI namelessAPI;
	private final BedwarsProAPI bedwarsProAPI;
	private final Logger logger;

	public JoinListener(CustomLogin plugin) {
		this.namelessAPI = plugin.getNamelessAPI();
		this.bedwarsProAPI = plugin.getBedwarsProAPI();
		this.logger = plugin.getLogger();
	}

	@EventHandler
	public void onPlayerConnect(AsyncPlayerPreLoginEvent event) {
		String message;
		try {
			message = handleConnection(event);
		} catch (Exception exception) {
			message = handleConnectionError(exception);
		}
		event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, translateColors(message));
	}

	private String handleConnection(AsyncPlayerPreLoginEvent event) {
		String name = event.getName();
		UUID uuid = event.getUniqueId();

		this.logger.info("Player '" + name + "' has connected");

		if (!hasPlayerPlayedBefore(uuid)) {
			return getNotPlayedBeforeMessage(name);
		}

		String message;
		if (this.namelessAPI.isPlayerRegistered(uuid)) {
			message = getAlreadyRegisteredMessage(name);
		} else {
			String ip = event.getAddress().getHostAddress();
			message = getRegisterMessage(name, ip);
		}
		return message;
	}

	private boolean hasPlayerPlayedBefore(UUID uuid) {
		return this.bedwarsProAPI.hasPlayerPlayedBefore(uuid);
	}

	private String getNotPlayedBeforeMessage(String name) {
		return "\n&3Hey &b" + name + "&3,\n\n" +
				"&3You must have played on Bedwars Pro before to register.\n\n" +
				"Quickly connect to the Minecraft IP &bbedwarspro.net&3, then come back here!\n\n";
	}

	private String getAlreadyRegisteredMessage(String name) {
		Date registeredDate = getRegisteredDate();
		String formattedDate = getFormattedDate(registeredDate);

		return "\n&3Hey &b" + name + "&3,\n\n" +
				"&3You already have a registered website account\n\n" +
				"on &bbedwarspro.net &3since &6" + formattedDate + "&3!\n\n";
	}

	private Date getRegisteredDate() {
		long unixSeconds = this.namelessAPI.getLastConnectedRegisteredTimestamp();
		return new Date(unixSeconds * 1000L);
	}

	private String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
		return sdf.format(date);
	}

	private String getRegisterMessage(String name, String ip) {
		String codeToUse;
		Code requestedCode = this.namelessAPI.requestCodeFor(name);
		if (requestedCode.exists()) {
			codeToUse = requestedCode.getCode();
		} else {
			Code newCode = generateAndPostNewCode(name, ip);
			codeToUse = newCode.getCode();
		}

		return "\n&3Hey &b" + name + "&3,\n\n" +
				"&bYour website verification code is &6" + codeToUse + "\n\n" +
				"&3Head over to &bbedwarspro.net &3and register your account!\n\n";
	}

	private Code generateAndPostNewCode(String name, String ip) {
		Code newCode = new Code(name, "" + generateRandomNumber(), ip);
		this.namelessAPI.postCode(newCode);
		return newCode;
	}

	private int generateRandomNumber() {
		int minNumber = 000001;
		int maxNumber = 999999;
		int code = (int) (Math.random() * (maxNumber - minNumber + 1) + minNumber);
		return code;
	}

	private String handleConnectionError(Exception exception) {
		exception.printStackTrace();
		return "&cAn error occurred.";
	}

	private String translateColors(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
