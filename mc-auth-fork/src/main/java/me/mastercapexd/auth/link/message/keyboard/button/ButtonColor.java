package me.mastercapexd.auth.link.message.keyboard.button;

public interface ButtonColor {
	void apply(Button button);

	default <T extends ButtonColor> T as(Class<T> clazz) {
		return clazz.cast(this);
	}

	public interface ButtonColorBuilder {
		ButtonColor red();

		ButtonColor blue();

		ButtonColor green();

		ButtonColor white();

		ButtonColor grey();

		default <T extends ButtonColorBuilder> T as(Class<T> clazz) {
			return clazz.cast(this);
		}
	}
}
