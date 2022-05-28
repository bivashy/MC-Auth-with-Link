package me.mastercapexd.auth.link.telegram;

import com.ubivashka.lamp.telegram.TelegramActor;
import com.ubivashka.lamp.telegram.dispatch.DispatchSource;
import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.message.Message;

import me.mastercapexd.auth.link.AbstractLinkCommandActorWrapper;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class TelegramCommandActorWrapper extends AbstractLinkCommandActorWrapper<TelegramActor>
		implements TelegramActor {

	public TelegramCommandActorWrapper(TelegramActor actor) {
		super(actor);
	}

	@Override
	public void send(Message message) {
		message.send(Identificator.fromObject(actor.getDispatchSource().getSourceIdentificator().asObject()));
	}

	@Override
	public LinkUserIdentificator userId() {
		return new UserNumberIdentificator(actor.getId());
	}

	@Override
	public DispatchSource getDispatchSource() {
		return actor.getDispatchSource();
	}

}
