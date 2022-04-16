package me.mastercapexd.auth.vk.builders;

import java.util.ArrayList;
import java.util.List;

import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.KeyboardButton;
import com.vk.api.sdk.objects.messages.KeyboardButtonColor;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.bungee.events.EntryConfirmationStartEvent;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.messages.vk.VKMessageContext;
import me.mastercapexd.auth.link.entryuser.LinkEntryUser;
import me.mastercapexd.auth.objects.IPInfoResponse;
import me.mastercapexd.auth.utils.CollectionUtils;
import me.mastercapexd.auth.utils.GeoUtils;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;
import me.mastercapexd.auth.vk.utils.VKUtils;
import net.md_5.bungee.api.ProxyServer;

public class ConfirmationMessageBuilder extends MessageBuilder {
	private final LinkEntryUser linkEntryUser;
	private final PluginConfig config;
	private final GeoUtils geoUtils;
	private final VKUtils vkUtils;

	public ConfirmationMessageBuilder(LinkEntryUser linkEntryUser, VKReceptioner receptioner) {
		this.linkEntryUser = linkEntryUser;
		this.vkUtils = receptioner.getPlugin().getVKUtils();
		this.geoUtils = receptioner.getPlugin().getGeoUtils();
		this.config = receptioner.getConfig();
		EntryConfirmationStartEvent confirmationStartEvent = new EntryConfirmationStartEvent(
				linkEntryUser.getLinkUserInfo().getIdentificator().asNumber(), linkEntryUser.getAccount());
		ProxyServer.getInstance().getPluginManager().callEvent(confirmationStartEvent);
	}

	@Override
	public MessagesSendQuery build() {
		MessagesSendQuery sendQuery = CLIENT.messages().send(ACTOR).randomId(RANDOM.nextInt())
				.userId(linkEntryUser.getLinkUserInfo().getIdentificator().asNumber());
		sendQuery.keyboard(createKeyboard());
		IPInfoResponse ipInfoAnswer = geoUtils.getIPInfo(linkEntryUser.getAccount().getLastIpAddress());

		VKMessageContext messageContext = new VKMessageContext(linkEntryUser.getLinkUserInfo().getIdentificator().asNumber(), linkEntryUser.getAccount());
		sendQuery.message(ipInfoAnswer
				.setInfo(config.getVKSettings().getMessages().getMessage("enter-message", messageContext)));
		return sendQuery;
	}

	private Keyboard createKeyboard() {
		Keyboard enterKeyboard = new Keyboard();
		enterKeyboard.setInline(true);
		List<KeyboardButton> buttons = new ArrayList<>();
		buttons.add(createConfirmButton());
		buttons.add(createDeclineButton());
		enterKeyboard.setButtons(CollectionUtils.chopList(buttons, 2));
		return enterKeyboard;
	}

	private KeyboardButton createConfirmButton() {
		return vkUtils.buildCallbackButton("enter-confirm", linkEntryUser.getAccount(), "enterserver_confirm",
				KeyboardButtonColor.POSITIVE);
	}

	private KeyboardButton createDeclineButton() {
		return vkUtils.buildCallbackButton("enter-decline", linkEntryUser.getAccount(), "enterserver_decline",
				KeyboardButtonColor.NEGATIVE);
	}

}
