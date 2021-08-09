package me.mastercapexd.auth.vk.builders;

import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.utils.ListUtils;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.utils.VKUtils;

public class AdminPanelBuilder extends MessageBuilder {
	private final Integer userId;
	private final VKUtils vkUtils;
	private final ListUtils listUtils;
	private final PluginConfig config;

	public AdminPanelBuilder(Integer userId, VKReceptioner receptioner) {
		this.userId = userId;
		this.vkUtils = receptioner.getPlugin().getVkUtils();
		this.listUtils = receptioner.getPlugin().getListUtils();
		this.config = receptioner.getConfig();
	}

	@Override
	public MessagesSendQuery build() {
		MessagesSendQuery sendQuery = vk.messages().send(actor).randomId(random.nextInt()).userId(userId);
		sendQuery.keyboard(buildKeyboard()).message(config.getVKMessages().getLegacyMessage("admin-panel"));
		return sendQuery;
	}

	private Keyboard buildKeyboard() {
		Keyboard keyboard = new Keyboard();
		keyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(createAllAccountsButton());
		buttons.add(createAllLinkedAccountsButton());
		keyboard.setButtons(listUtils.chopList(buttons, 3));
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
