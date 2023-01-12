package me.mastercapexd.auth.link.telegram;

import com.ubivashka.lamp.telegram.TelegramActor;
import com.ubivashka.lamp.telegram.dispatch.DispatchSource;
import com.ubivashka.messenger.telegram.message.TelegramMessage;
import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.message.Message;

import me.mastercapexd.auth.link.LinkCommandActorWrapperTemplate;
import me.mastercapexd.auth.link.user.info.identificator.LinkUserIdentificator;
import me.mastercapexd.auth.link.user.info.identificator.UserNumberIdentificator;

public class TelegramCommandActorWrapper extends LinkCommandActorWrapperTemplate<TelegramActor> implements TelegramActor {

    public TelegramCommandActorWrapper(TelegramActor actor) {
        super(actor);
    }

    @Override
    public void send(Message message) {
        if (!message.safeAs(TelegramMessage.class).isPresent())
            return;
        message.as(TelegramMessage.class).send(Identificator.fromObject(actor.getDispatchSource().getChatIdentficator().asObject()),
                TelegramMessage.getDefaultApiProvider(), response -> {
            if (!response.isOk())
                TelegramLinkType.getInstance().newMessageBuilder("Error occured " + response.description() + ". Error code: " + response.errorCode()).build().send(Identificator.fromObject(actor.getDispatchSource().getChatIdentficator().asObject()));
        });
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
