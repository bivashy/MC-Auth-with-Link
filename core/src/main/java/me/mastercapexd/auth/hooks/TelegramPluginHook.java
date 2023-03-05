package me.mastercapexd.auth.hooks;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.hook.PluginHook;
import com.pengrad.telegrambot.TelegramBot;

public interface TelegramPluginHook extends PluginHook {
    AuthPlugin PLUGIN = AuthPlugin.instance();

    @Override
    default boolean canHook() {
        return PLUGIN.getConfig().getTelegramSettings().isEnabled();
    }

    TelegramBot getTelegramBot();
}
