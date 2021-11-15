package me.mastercapexd.auth.vk.buttonshandler;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(() -> {
			String payload = e.getButtonEvent().getPayload();
			if (payload == null)
				return;
			if (payload.isEmpty())
				return;
			String[] array = payload.split("_");
			String firstPayload = array[0];
			String afterPayload = payload.substring(payload.indexOf("_") + 1);
			for (VKCallbackButton button : this.commands) {
				if (button.isExecuted(firstPayload)) {
					button.execute(e, afterPayload);
					break;
				}
			}
		});
		executorService.shutdown();
	}
}
