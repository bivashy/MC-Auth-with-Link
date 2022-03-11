package me.mastercapexd.auth.link.vk;

import com.ubivashka.lamp.commands.vk.core.BaseVkActor;

import me.mastercapexd.auth.link.AbstractLinkCommandActorWrapper;
import me.mastercapexd.auth.link.message.Message;

public class VKCommandActorWrapper extends AbstractLinkCommandActorWrapper {

	public VKCommandActorWrapper(BaseVkActor actor) {
		super(actor);
	}

	@Override
	public void send(Message message) {
		message.sendMessage(actor.as(BaseVkActor.class).getPeerId());
	}
}
