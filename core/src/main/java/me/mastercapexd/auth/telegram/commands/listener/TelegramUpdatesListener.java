package me.mastercapexd.auth.telegram.commands.listener;

import java.util.List;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

public abstract class TelegramUpdatesListener implements UpdatesListener {
    private long lastUpdateId;

    @Override
    public int process(List<Update> updates) {
        updates.stream().findFirst().ifPresent(update -> {
            if (lastUpdateId == update.updateId())
                return;
            lastUpdateId = update.updateId();
            processValidUpdates(updates);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    protected abstract void processValidUpdates(List<Update> updates);
}
