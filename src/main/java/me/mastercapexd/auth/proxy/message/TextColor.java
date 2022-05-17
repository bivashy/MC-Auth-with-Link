package me.mastercapexd.auth.proxy.message;

public abstract class TextColor {
	private static TextColor instance;

	public TextColor() {
		instance = this;
	}

	protected abstract String colorText(char colorCharacter, String text);

	public static String colorText(String text) {
		if (text == null)
			throw new IllegalArgumentException("Cannot color null text");
		return instance.colorText('&', text);
	}
}
