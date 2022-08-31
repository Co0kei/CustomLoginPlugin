package net.bedwarspro.plugin.listener;

import net.bedwarspro.plugin.CustomLogin;
import net.bedwarspro.plugin.http.NamelessAPI;
import net.bedwarspro.plugin.model.Code;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class JoinListener implements Listener {

	private final NamelessAPI namelessAPI;

	public JoinListener(CustomLogin plugin) {
		this.namelessAPI = plugin.getNamelessAPI();
	}

	@EventHandler
	public void onPlayerConnect(AsyncPlayerPreLoginEvent event) {
		String name = event.getName();
		UUID uuid = event.getUniqueId();

		String message;
		if (this.namelessAPI.isPlayerRegistered(uuid)) {
			message = getAlreadyRegisteredMessage(name);
		} else {
			String ip = event.getAddress().getHostAddress();
			message = getRegisterMessage(name, ip);
		}

		event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, translateColors(message));
	}

	private String getAlreadyRegisteredMessage(String name) {
		Date registeredDate = getRegisteredDate();
		String formattedDate = getFormattedDate(registeredDate);

		return "\n&3Hey &b" + name + "&3,\n\n" +
				"&3You already have a registered website account\n\n" +
				"on &bbedwarspro.net &3since &6" + formattedDate + "&3!\n\n";
	}

	private Date getRegisteredDate() {
		long unixSeconds = this.namelessAPI.getPlayerRegisteredTime();
		return new Date(unixSeconds * 1000L);
	}

	private String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
		return sdf.format(date);
	}

	private String getRegisterMessage(String name, String ip) {
		String codeToUse;
		Code existingCode = this.namelessAPI.requestCodeFor(name);
		if (existingCode == null) {
			Code newCode = generateAndPostNewCode(name, ip);
			codeToUse = newCode.getCode();
		} else {
			codeToUse = existingCode.getCode();
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

	private String translateColors(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}

}
