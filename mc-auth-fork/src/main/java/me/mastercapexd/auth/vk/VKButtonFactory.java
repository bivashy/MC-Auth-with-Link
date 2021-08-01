package me.mastercapexd.auth.vk;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.buttonshandler.VKCallbackButton;

public interface VKButtonFactory {
	public VKCallbackButton createButton(String payload, VKButtonExecutor executor, List<String> aliases);

	default VKCallbackButton createButton(String payload, VKButtonExecutor executor) {
		return createButton(payload, executor, new ArrayList<>());
	}
}
