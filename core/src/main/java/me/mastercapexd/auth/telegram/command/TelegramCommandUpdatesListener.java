package me.mastercapexd.auth.telegram.command;

import java.util.List;

import com.bivashy.auth.api.config.link.command.LinkCustomCommandSettings;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.command.context.CustomCommandExecutionContext;
import com.google.gson.Gson;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.ubivashka.lamp.telegram.TelegramActor;
import com.ubivashka.lamp.telegram.core.TelegramHandler;
import com.ubivashka.lamp.telegram.dispatch.CallbackQueryDispatchSource;
import com.ubivashka.lamp.telegram.dispatch.DispatchSource;
import com.ubivashka.lamp.telegram.dispatch.MessageDispatchSource;
import com.ubivashka.messenger.telegram.message.keyboard.TelegramKeyboard;
import com.ubivaska.messenger.common.identificator.Identificator;
import com.ubivaska.messenger.common.message.Message;
import com.ubivaska.messenger.common.message.Message.MessageBuilder;

import me.mastercapexd.auth.link.telegram.TelegramCommandActorWrapper;
import me.mastercapexd.auth.link.telegram.TelegramLinkType;
import me.mastercapexd.auth.messenger.commands.custom.BaseCustomCommandExecutionContext;
import me.mastercapexd.auth.telegram.command.listener.TelegramUpdatesListener;
import revxrsal.commands.command.ArgumentStack;

public class TelegramCommandUpdatesListener extends TelegramUpdatesListener {
    private static final Gson GSON = new Gson();
    private static final LinkType LINK_TYPE = TelegramLinkType.getInstance();

    @Override
    public void processValidUpdates(List<Update> updates) {
        for (Update update : updates)
            processUpdate(update);
    }

    private void processUpdate(Update update) {
        if (update.message() != null)
            processMessageUpdate(update);
        if (update.callbackQuery() != null)
            processCallbackUpdate(update);
    }

    private void processMessageUpdate(Update update) {
        com.pengrad.telegrambot.model.Message message = update.message();
        TelegramHandler.getInstances().forEach(handler -> {
            handleCommandDispatch(handler, new MessageDispatchSource(message));
            LINK_TYPE.getSettings().getCustomCommands().execute(new BaseCustomCommandExecutionContext(message.text())).forEach(customCommand -> {
                Message customCommandMessageResponse = createMessageResponse(customCommand);
                customCommandMessageResponse.send(Identificator.of(message.chat().id()));
            });
        });
    }

    private void processCallbackUpdate(Update update) {
        CallbackQuery callbackQuery = update.callbackQuery();
        TelegramHandler.getInstances().forEach(handler -> {
            handleCommandDispatch(handler, new CallbackQueryDispatchSource(callbackQuery));
            if (callbackQuery.message() == null)
                return;
            CustomCommandExecutionContext executionContext = new BaseCustomCommandExecutionContext(callbackQuery.data());
            executionContext.setButtonExecution(true);
            LINK_TYPE.getSettings()
                    .getCustomCommands()
                    .execute(executionContext)
                    .forEach(customCommand -> {
                        Message customCommandMessageResponse = createMessageResponse(customCommand);
                        customCommandMessageResponse.send(Identificator.of(callbackQuery.message().chat().id()));
                    });
        });
    }

    private void handleCommandDispatch(TelegramHandler handler, DispatchSource dispatchSource) {
        TelegramActor actor = new TelegramCommandActorWrapper(TelegramActor.wrap(handler, dispatchSource));
        ArgumentStack argumentStack = handler.parseArguments(dispatchSource.getExecutionText());
        if (argumentStack.isEmpty())
            return;
        handler.dispatch(actor, argumentStack);
    }

    private Message createMessageResponse(LinkCustomCommandSettings customCommand) {
        MessageBuilder builder = LINK_TYPE.newMessageBuilder(customCommand.getAnswer());
        if (customCommand.getSectionHolder().contains("keyboard"))
            builder.keyboard(new TelegramKeyboard(GSON.fromJson(customCommand.getSectionHolder().getString("keyboard"), InlineKeyboardMarkup.class)));
        return builder.build();
    }
}
