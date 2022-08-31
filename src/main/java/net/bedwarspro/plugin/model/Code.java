package net.bedwarspro.plugin.model;

import com.google.gson.JsonObject;

public class Code {

	private final String minecraft_name;
	private final String code;
	private final String ip;

	public Code(String minecraft_name, String code, String ip) {
		this.minecraft_name = minecraft_name;
		this.code = code;
		this.ip = ip;
	}

	public Code(JsonObject response) {
		this.minecraft_name = response.get("minecraft_name").getAsString();
		this.code = response.get("code").getAsString();
		this.ip = response.get("ip").getAsString();
	}

	public JsonObject getAsJson() {
		JsonObject json = new JsonObject();
		addAllFields(json);
		return json;
	}

	private void addAllFields(JsonObject json) {
		json.addProperty("minecraft_name", this.minecraft_name);
		json.addProperty("code", this.code);
		json.addProperty("ip", this.ip);
	}
	
	public String getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return "Code{" +
				"minecraft_name='" + minecraft_name + '\'' +
				", code='" + code + '\'' +
				", ip='" + ip + '\'' +
				'}';
	}

}
