package me.mastercapexd.auth.link.message.keyboard.button;

import me.mastercapexd.auth.function.Castable;

public interface ButtonAction extends Castable<ButtonAction> {
	public static interface ButtonActionBuilder {
		ButtonAction callback();
		
		ButtonAction link();
		
		ButtonAction reply();
	}
}
