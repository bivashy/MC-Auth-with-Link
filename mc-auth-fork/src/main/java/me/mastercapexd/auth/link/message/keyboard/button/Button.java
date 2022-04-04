package me.mastercapexd.auth.link.message.keyboard.button;

import java.util.Map;

import me.mastercapexd.auth.function.Castable;

public interface Button extends Castable<Button> {
	String getLabel();

	Map<String, String> getAdditionalInfo();

	void putAdditionalInfo(String key, String value);

	public interface ButtonBuilder extends Castable<ButtonBuilder> {
		ButtonBuilder label(String label);

		ButtonBuilder color(ButtonColor color);

		ButtonBuilder action(ButtonAction action);

		ButtonBuilder customId(String id);

		Button build();
	}
}
