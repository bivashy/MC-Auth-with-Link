package me.mastercapexd.auth.link.message.keyboard.button;

import me.mastercapexd.auth.function.Castable;

public interface ButtonColor extends Castable<ButtonColor> {
	public static interface ButtonColorBuilder {
		ButtonColor red();

		ButtonColor blue();

		ButtonColor green();

		ButtonColor white();

		ButtonColor grey();
	}
}
