package me.mastercapexd.auth.hooks;

import com.pengrad.telegrambot.TelegramBot;

public class DefaultTelegramPluginHook implements TelegramPluginHook {
	private TelegramBot telegramBot = new TelegramBot(PLUGIN.getConfig().getTelegramSettings().getBotToken());

	@Override
	public TelegramBot getTelegramBot() {
		return telegramBot;
	}

}
