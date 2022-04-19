package me.mastercapexd.auth.link.message.vk;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.message.AbstractMessage;
import me.mastercapexd.auth.link.message.LinkUserSendMessageResult;
import me.mastercapexd.auth.link.message.keyboard.IKeyboard;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.vk.utils.VKUtils;

public class VKMessage extends AbstractMessage {
	private static final Integer[] DISABLED_MESSAGE_ERROR_CODES = { 900, 901, 902 };

	private static final VkPluginHook VK_HOOK = ProxyPlugin.instance().getHook(VkPluginHook.class);
	private static final VkApiClient VK_API = VK_HOOK.getClient();
	private static final GroupActor ACTOR = VK_HOOK.getActor();
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

		if (!photos.isEmpty()) {
			String[] imageIdentificators = photos.stream().map(VKUtils::getPhotoAttachment).toArray(String[]::new);
			messageSendQuery.attachment(String.join(",", imageIdentificators));
		}

		messageSendQuery.peerId(peerId);

		try {
			messageSendQuery.execute();
		} catch (ApiException e) {
			e.printStackTrace();

			if (Arrays.stream(DISABLED_MESSAGE_ERROR_CODES).anyMatch(errorCode -> errorCode == e.getCode()))
				return LinkUserSendMessageResult.USER_DISABLED_MESSAGES;

		} catch (ClientException e) {
			e.printStackTrace();
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
		public MessageBuilder uploadPhoto(File photo) {
			VKMessage.this.uploadPhoto(photo);
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
