package me.mastercapexd.auth.vk.builders;

import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.utils.CollectionUtils;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.utils.VKUtils;

public class AdminPanelBuilder extends MessageBuilder {
	private final Integer userId;
	private final VKUtils vkUtils;
	private final PluginConfig config;

	public AdminPanelBuilder(Integer userId, VKReceptioner receptioner) {
		this.userId = userId;
		this.vkUtils = receptioner.getPlugin().getVKUtils();
		this.config = receptioner.getConfig();
	}

	@Override
	public MessagesSendQuery build() {
		MessagesSendQuery sendQuery = CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt()).userId(userId);
		sendQuery.keyboard(buildKeyboard()).message(config.getVKSettings().getVKMessages().getMessage("admin-panel"));
		return sendQuery;
	}

	private Keyboard buildKeyboard() {
		Keyboard keyboard = new Keyboard();
		keyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(createAllAccountsButton());
		buttons.add(createAllLinkedAccountsButton());
		keyboard.setButtons(CollectionUtils.chopList(buttons, 3));
		return keyboard;
	}

	private KeyboardButton createAllAccountsButton() {
		return vkUtils.buildCallbackButton("admin-panel-all-accounts", "allAccounts", KeyboardButtonColor.PRIMARY);
	}

	private KeyboardButton createAllLinkedAccountsButton() {
		return vkUtils.buildCallbackButton("admin-panel-all-linked-accounts", "allLinkedAccounts",
				KeyboardButtonColor.PRIMARY);
	}
}
