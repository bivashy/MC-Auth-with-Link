package me.mastercapexd.auth.link.message.vk;

import java.util.concurrent.ThreadLocalRandom;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.link.message.DefaultMessage;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.vk.utils.VKUtils;

public class VKMessage extends DefaultMessage {
	private static final VkPluginHook VK_HOOK = ProxyPlugin.instance().getHook(VkPluginHook.class);
	private static final VkApiClient VK_API = VK_HOOK.getClient();
	private static final GroupActor ACTOR = VK_HOOK.getActor();

	public VKMessage(String text) {
		super(text);
	}

	@Override
	public void send(LinkUser user) {
		send(user.getLinkUserInfo().getIdentificator().asNumber());
	}

	@Override
	public void send(Integer peerId) {
		if (text == null)
			throw new NullPointerException("Raw content of message cannot be null!");
		MessagesSendQuery messageSendQuery = VK_API.messages().send(ACTOR).randomId(ThreadLocalRandom.current().nextInt()).message(text)
				.peerId(peerId);

		if (keyboard != null && keyboard.safeAs(VKKeyboard.class).isPresent())
			messageSendQuery.keyboard(keyboard.as(VKKeyboard.class).build());

		if (!photos.isEmpty()) {
			String[] imageIdentificators = photos.stream().map(VKUtils::getPhotoAttachment).toArray(String[]::new);
			messageSendQuery.attachment(String.join(",", imageIdentificators));
		}

		try {
			messageSendQuery.execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
	}

	public class VKMessageBuilder extends DefaultMessageBuilder {

		public VKMessageBuilder() {
			super(VKMessage.this);
		}
	}
}
