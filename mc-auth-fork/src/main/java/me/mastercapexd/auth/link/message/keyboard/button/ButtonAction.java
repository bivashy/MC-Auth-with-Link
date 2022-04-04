package me.mastercapexd.auth.link.message.keyboard.button;

import me.mastercapexd.auth.function.Castable;

public interface ButtonAction extends Castable<ButtonAction> {
	void apply(Button button);

	public interface ButtonActionBuilder extends Castable<ButtonActionBuilder> {
		ButtonAction callback(String id);

		ButtonAction link(String url);
	}
}
