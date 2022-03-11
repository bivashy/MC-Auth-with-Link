package me.mastercapexd.auth.link.message.keyboard.button;

import java.util.Map;

public interface Button {
	String getLabel();

	Map<String, String> getAdditionalInfo();

	default <T extends Button> T as(Class<T> clazz) {
		return clazz.cast(this);
	}

	public interface ButtonBuilder {
		ButtonBuilder label(String label);

		Button build();

		default <T extends ButtonBuilder> T as(Class<T> clazz) {
			return clazz.cast(this);
		}
	}
}
