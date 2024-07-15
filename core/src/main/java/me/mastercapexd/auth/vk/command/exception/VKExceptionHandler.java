package me.mastercapexd.auth.vk.command.exception;

import com.bivashy.auth.api.link.LinkType;
import com.vk.api.sdk.exceptions.ApiMessagesDenySendException;
import me.mastercapexd.auth.messenger.commands.exception.MessengerExceptionHandler;
import revxrsal.commands.command.CommandActor;

public class VKExceptionHandler extends MessengerExceptionHandler {

    public VKExceptionHandler(LinkType linkType) {
        super(linkType);
    }

    @Override
    public void onUnhandledException(CommandActor actor, Throwable throwable) {
        if (throwable instanceof ApiMessagesDenySendException)
            return;
        super.onUnhandledException(actor, throwable);
    }

}
