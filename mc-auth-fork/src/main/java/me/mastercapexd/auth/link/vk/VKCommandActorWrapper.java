package me.mastercapexd.auth.link.vk;

import com.ubivashka.lamp.commands.vk.VkActor;
import com.ubivashka.lamp.commands.vk.core.BaseVkActor;
import com.vk.api.sdk.objects.messages.Conversation;
import com.vk.api.sdk.objects.messages.ConversationPeerType;
import com.vk.api.sdk.objects.users.UserFull;

import me.mastercapexd.auth.link.AbstractLinkCommandActorWrapper;
import me.mastercapexd.auth.link.message.Message;

public class VKCommandActorWrapper extends AbstractLinkCommandActorWrapper<BaseVkActor> implements VkActor {

	public VKCommandActorWrapper(BaseVkActor actor) {
		super(actor);
	}

	@Override
	public void send(Message message) {
		message.sendMessage(actor.as(BaseVkActor.class).getPeerId());
	}

	@Override
	public Integer userId() {
		return actor.getAuthorId();
	}

	@Override
	public com.vk.api.sdk.objects.messages.Message getMessage() {
		return actor.getMessage();
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
	public String getMessageText() {
		return actor.getMessageText();
	}

	@Override
	public String getMessagePayload() {
		return actor.getMessagePayload();
	}

	@Override
	public Integer getConversationMessageId() {
		return actor.getConversationMessageId();
	}

	@Override
	public Integer getAuthorId() {
		return userId();
	}

	@Override
	public Integer getPeerId() {
		return actor.getPeerId();
	}

}
