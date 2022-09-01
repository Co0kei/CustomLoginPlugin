package net.bedwarspro.plugin;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import java.util.Arrays;
import java.util.List;

public class LogFilter implements Filter {

	private final List<String> filteredTerms = Arrays.asList("Disconnecting", "com.mojang.authlib.GameProfile");

	private Result checkMessage(String message) {
		for (String term : this.filteredTerms) {
			if (message.contains(term)) {
				return Result.DENY;
			}
		}
		return Result.NEUTRAL;
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
		return checkMessage(msg);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
		return checkMessage(message);
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
		return checkMessage(msg.toString());
	}

	@Override
	public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
		return checkMessage(msg.getFormattedMessage());
	}

	@Override
	public Result filter(LogEvent event) {
		return checkMessage(event.getMessage().getFormattedMessage());
	}

	@Override
	public State getState() {
		try {
			return LifeCycle.State.STARTED;
		} catch (Exception exception) {
			return null;
		}
	}

	@Override
	public void initialize() {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public boolean isStarted() {
		return true;
	}

	@Override
	public boolean isStopped() {
		return false;
	}

	@Override
	public Result getOnMismatch() {
		return Filter.Result.NEUTRAL;
	}

	@Override
	public Result getOnMatch() {
		return Filter.Result.NEUTRAL;
	}

}
