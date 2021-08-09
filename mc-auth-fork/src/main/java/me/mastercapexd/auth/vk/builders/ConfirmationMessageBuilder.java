package me.mastercapexd.auth.vk.builders;

import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.objects.IPInfoResponse;
import me.mastercapexd.auth.utils.GeoUtils;
import me.mastercapexd.auth.utils.ListUtils;
import me.mastercapexd.auth.vk.accounts.VKEntryAccount;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.utils.VKUtils;

public class ConfirmationMessageBuilder extends MessageBuilder {
	private final VKEntryAccount entryAccount;
	private final ListUtils listUtils;
	private final PluginConfig config;
	private final GeoUtils geoUtils;
	private final VKUtils vkUtils;

	public ConfirmationMessageBuilder(VKEntryAccount entryAccount, VKReceptioner receptioner) {
		this.entryAccount = entryAccount;
		this.vkUtils = receptioner.getPlugin().getVkUtils();
		this.geoUtils = receptioner.getPlugin().getGeoUtils();
		this.listUtils = receptioner.getPlugin().getListUtils();
		this.config = receptioner.getConfig();

	}

	@Override
	public MessagesSendQuery build() {
		MessagesSendQuery sendQuery = vk.messages().send(actor).randomId(random.nextInt())
				.userId(entryAccount.getVkId());
		sendQuery.keyboard(createKeyboard());
		IPInfoResponse ipInfoAnswer = geoUtils.getIPInfo(entryAccount.getAccount().getLastIpAddress());
		sendQuery.message(
				ipInfoAnswer.setInfo(config.getVKMessages().getMessage("enter-message", entryAccount.getAccount())));
		return sendQuery;
	}

	private Keyboard createKeyboard() {
		Keyboard enterKeyboard = new Keyboard();
		enterKeyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(createConfirmButton());
		buttons.add(createDeclineButton());
		enterKeyboard.setButtons(listUtils.chopList(buttons, 2));
		return enterKeyboard;
	}

	private KeyboardButton createConfirmButton() {
		return vkUtils.buildCallbackButton("enter-confirm", entryAccount.getAccount(),
				"enterserver_confirm_" + entryAccount.getButtonUuid(), KeyboardButtonColor.POSITIVE);
	}

	private KeyboardButton createDeclineButton() {
		return vkUtils.buildCallbackButton("enter-decline", entryAccount.getAccount(),
				"enterserver_decline_" + entryAccount.getButtonUuid(), KeyboardButtonColor.NEGATIVE);
	}

}
