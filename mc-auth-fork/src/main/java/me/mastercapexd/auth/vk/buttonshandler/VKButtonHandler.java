package me.mastercapexd.auth.vk.buttonshandler;

import java.util.ArrayList;
import java.util.Arrays;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class VKButtonHandler implements Listener {
	private ArrayList<VKCallbackButton> commands = new ArrayList<>();

	public void addButton(VKCallbackButton command) {
		this.commands.add(command);
	}

	public void addButton(String name, VKButtonExecutor executor) {
		this.commands.add(new VKCallbackButton(name.toLowerCase(), executor));
	}

	public void removeButton(VKCallbackButton command) {
		this.commands.remove(command);
	}

	public ArrayList<VKCallbackButton> getButtons() {
		return commands;
	}

	@EventHandler
	public void onButtonPress(VKCallbackButtonPressEvent e) {
		String payload = e.getButtonEvent().getPayload();
		if (payload == null)
			return;
		if (payload.isEmpty())
			return;
		String[] array = payload.split("_");
		String firstPayload = array[0];
		String[] args = Arrays.copyOfRange(array, 1, array.length);
		String afterPayload = String.join("_", args);
		for (VKCallbackButton button : this.commands) {
			if (button.isExecuted(firstPayload)) {
				button.execute(e, afterPayload);
				break;
			}
		}
	}
}
