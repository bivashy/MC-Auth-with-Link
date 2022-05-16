package me.mastercapexd.auth.link.vk;

import com.ubivashka.lamp.commands.vk.VkActor;
import com.ubivashka.lamp.commands.vk.core.BaseVkActor;
import com.ubivashka.lamp.commands.vk.message.DispatchSource;
import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.message.Message;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeerType;
import com.vk.api.sdk.objects.users.UserFull;

import me.mastercapexd.auth.link.AbstractLinkCommandActorWrapper;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class VKCommandActorWrapper extends AbstractLinkCommandActorWrapper<BaseVkActor> implements VkActor {

	private final VkActor vkActor;

	public VKCommandActorWrapper(BaseVkActor actor) {
		super(actor);
		vkActor = actor.as(VkActor.class);
	}

	@Override
	public void send(Message message) {
		message.send(Identificator.of(vkActor.getPeerId()));
	}

	@Override
	public LinkUserIdentificator userId() {
		return new UserNumberIdentificator(actor.getAuthorId());
	}

	@Override
	public DispatchSource getDispatchSource() {
		return actor.getDispatchSource();
	}

	@Override
	public UserFull getUser() {
		return actor.getUser();
	}

	@Override
	public Conversation getConversation() {
		return actor.getConversation();
	}

	@Override
	public ConversationPeerType getConversationType() {
		return actor.getConversationType();
	}

	@Override
	public String getText() {
		return actor.getText();
	}

	@Override
	public String getMessagePayload() {
		return actor.getMessagePayload();
	}

	@Override
	public Integer getConversationId() {
		return actor.getConversationId();
	}

	@Override
	public Integer getAuthorId() {
		return actor.getAuthorId();
	}

	@Override
	public Integer getPeerId() {
		return actor.getPeerId();
	}

}
