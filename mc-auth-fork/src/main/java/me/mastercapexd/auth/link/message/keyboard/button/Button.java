package me.mastercapexd.auth.link.message.keyboard.button;

import me.mastercapexd.auth.function.Castable;

public interface Button extends Castable<Button> {
	String getLabel();

	String getActionData();

	ButtonColor getColor();

	ButtonAction getAction();

	public static interface ButtonBuilder extends Castable<ButtonBuilder> {
		ButtonBuilder action(ButtonAction action);

		ButtonBuilder actionData(String actionData);

		ButtonBuilder color(ButtonColor color);

		ButtonBuilder label(String label);

		Button build();
	}
}
