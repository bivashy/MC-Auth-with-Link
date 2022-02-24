package me.mastercapexd.auth.link.message.vk;

import java.util.Arrays;
import java.util.Random;

import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.link.message.AbstractMessage;
import me.mastercapexd.auth.link.message.LinkUserSendMessageResult;
import me.mastercapexd.auth.link.vk.VKLinkUser;

public class VKMessage extends AbstractMessage<VKLinkUser> {
	private static final Integer[] DISABLED_MESSAGE_ERROR_CODES = { 900, 901, 902 };

	private static final VkApiClient VK_API = VKAPI.getInstance().getVK();
	private static final GroupActor ACTOR = VKAPI.getInstance().getActor();
	private static final Random RANDOM = new Random();

	private Keyboard keyboard;

	private VKMessage(String rawContent) {
		super(rawContent);
	}

	public static VKMessageBuilder newBuilder(String rawContent) {
		return new VKMessage(rawContent).new VKMessageBuilder();
	}

	@Override
	public LinkUserSendMessageResult sendMessage(VKLinkUser user) {
		if (rawContent == null)
			throw new NullPointerException("Raw content of message cannot be null!");
		MessagesSendQuery messageSendQuery = VK_API.messages().send(ACTOR).randomId(RANDOM.nextInt());

		messageSendQuery.message(rawContent);

		if (keyboard != null)
			messageSendQuery.keyboard(keyboard);

		messageSendQuery.userId(user.getLinkUserInfo().getLinkUserId());

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

	public Keyboard getKeyboard() {
		return keyboard;
	}

	public class VKMessageBuilder {
		private VKMessageBuilder() {
		}

		public VKMessageBuilder setKeyboard(Keyboard keyboard) {
			VKMessage.this.keyboard = keyboard;
			return this;
		}

		public VKMessage build() {
			return VKMessage.this;
		}
	}

}
