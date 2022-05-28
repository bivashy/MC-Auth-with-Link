package me.mastercapexd.auth.hooks;

import com.pengrad.telegrambot.TelegramBot;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.hooks.PluginHook;

public interface TelegramPluginHook extends PluginHook {

	static final ProxyPlugin PLUGIN = ProxyPlugin.instance();

	@Override
	default boolean canHook() {
		return PLUGIN.getConfig().getTelegramSettings().isEnabled();
	}

	TelegramBot getTelegramBot();
}
