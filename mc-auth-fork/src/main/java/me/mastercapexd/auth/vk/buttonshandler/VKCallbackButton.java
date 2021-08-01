package me.mastercapexd.auth.vk.buttonshandler;

import java.util.ArrayList;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

public class VKCallbackButton {
	private final String payload;
	private final VKButtonExecutor executor;
	private ArrayList<String> aliases = new ArrayList<>();

	public VKCallbackButton(String payload, VKButtonExecutor executor) {
		this.payload = payload;
		this.executor = executor;
	}

	public String getPayload() {
		return payload;
	}

	public VKButtonExecutor getExecutor() {
		return executor;
	}

	public void execute(VKCallbackButtonPressEvent event, String afterPayload) {
		this.executor.execute(event, afterPayload);
	}

	public boolean isExecuted(String payload) {
		if (payload.equalsIgnoreCase(this.payload))
			return true;
		for (String alias : aliases)
			if (payload.equalsIgnoreCase(alias))
				return true;

		return false;
	}

	public void setAliases(ArrayList<String> aliases) {
		this.aliases = aliases;
	}
}
