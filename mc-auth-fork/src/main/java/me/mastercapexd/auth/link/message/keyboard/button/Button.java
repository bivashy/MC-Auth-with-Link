package me.mastercapexd.auth.link.message.keyboard.button;

import java.util.Map;

public interface Button {
	String getLabel();

	Map<String, String> getAdditionalInfo();
	
	void putAdditionalInfo(String key,String value);
	
	default <T extends Button> T as(Class<T> clazz) {
		return clazz.cast(this);
	}

	public interface ButtonBuilder {
		ButtonBuilder label(String label);

		ButtonBuilder color(ButtonColor color);
		
		ButtonBuilder action(ButtonAction action);
		
		ButtonBuilder customId(String id);
		
		Button build();

		default <T extends ButtonBuilder> T as(Class<T> clazz) {
			return clazz.cast(this);
		}
	}
}
