package me.mastercapexd.auth.link.message.vk;

import java.util.Arrays;
import java.util.Random;

import com.ubivashka.vk.api.providers.VkApiProvider;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.link.message.AbstractMessage;
import me.mastercapexd.auth.link.message.LinkUserSendMessageResult;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import me.mastercapexd.auth.link.user.LinkUser;

public class VKMessage extends AbstractMessage {
	private static final Integer[] DISABLED_MESSAGE_ERROR_CODES = { 900, 901, 902 };

	private static final VkApiProvider VK_API_PROVIDER = BungeeVkApiPlugin.getInstance().getVkApiProvider();
	private static final VkApiClient VK_API = VK_API_PROVIDER.getVkApiClient();
	private static final GroupActor ACTOR = VK_API_PROVIDER.getActor();
	private static final Random RANDOM = new Random();

	private VKMessage(String rawContent) {
		super(rawContent);
	}

	public static VKMessageBuilder newBuilder(String rawContent) {
		return new VKMessage(rawContent).new VKMessageBuilder();
	}

	@Override
	public LinkUserSendMessageResult sendMessage(LinkUser user) {
		return sendMessage(user.getLinkUserInfo().getIdentificator().asNumber());
	}

	@Override
	public LinkUserSendMessageResult sendMessage(Integer peerId) {
		if (rawContent == null)
			throw new NullPointerException("Raw content of message cannot be null!");
		MessagesSendQuery messageSendQuery = VK_API.messages().send(ACTOR).randomId(RANDOM.nextInt());

		messageSendQuery.message(rawContent);

		if (keyboard != null)
			messageSendQuery.keyboard(keyboard.as(VKKeyboard.class).buildKeyboard());

		messageSendQuery.peerId(peerId);

		try {
			messageSendQuery.execute();
		} catch (ApiException e) {
			e.printStackTrace();

			if (Arrays.stream(DISABLED_MESSAGE_ERROR_CODES).anyMatch(errorCode -> errorCode == e.getCode()))
				return LinkUserSendMessageResult.USER_DISABLED_MESSAGES;

			return LinkUserSendMessageResult.ERROR_OCCURED;
		} catch (ClientException e) {
			e.printStackTrace();
			return LinkUserSendMessageResult.ERROR_OCCURED;
		}

		return LinkUserSendMessageResult.SENDED;
	}

	public class VKMessageBuilder implements MessageBuilder {
		private VKMessageBuilder() {
		}

		@Override
		public MessageBuilder keyboard(IKeyboard keyboard) {
			VKMessage.this.setKeyboard(keyboard);
			return this;
		}

		@Override
		public MessageBuilder rawContent(String rawContent) {
			VKMessage.this.rawContent = rawContent;
			return this;
		}

		public VKMessage build() {
			return VKMessage.this;
		}

	}

}
