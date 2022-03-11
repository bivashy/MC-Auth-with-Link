package me.mastercapexd.auth.link.message.keyboard.button;

public interface ButtonAction {
	void apply(Button button);

	default <T extends ButtonAction> T as(Class<T> clazz) {
		return clazz.cast(this);
	}

	public interface ButtonActionBuilder {
		ButtonAction callback(String id);

		ButtonAction link(String url);

		default <T extends ButtonActionBuilder> T as(Class<T> clazz) {
			return clazz.cast(this);
		}
	}
}
