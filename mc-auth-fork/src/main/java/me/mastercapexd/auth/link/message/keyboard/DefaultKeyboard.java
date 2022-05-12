package me.mastercapexd.auth.link.message.keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.mastercapexd.auth.link.message.keyboard.button.Button;

public abstract class DefaultKeyboard implements IKeyboard {
	protected List<List<Button>> buttons = new ArrayList<>();
	protected KeyboardType keyboardType;

	@Override
	public List<List<Button>> getButtons() {
		return new ArrayList<>(buttons);
	}

	@Override
	public IKeyboard addRow(Button... buttons) {
		this.buttons.add(Arrays.asList(buttons));
		return this;
	}

	@Override
	public IKeyboard removeIf(Predicate<Button> buttonFilter) {
		buttons.forEach(buttonsList -> buttonsList.removeIf(buttonFilter));
		buttons.removeIf(List::isEmpty);
		return this;
	}

	@Override
	public IKeyboard ifThen(Predicate<Button> filter, Function<Button, Button> function) {
		buttons.stream().map(buttonsList -> buttonsList.stream().map(button -> {
			if (filter.test(button))
				return function.apply(button);
			return button;
		}).collect(Collectors.toList()));
		return this;
	}

	@Override
	public KeyboardType getType() {
		return keyboardType;
	}

	public static abstract class DefaultKeyboardBuilder implements IKeyboardBuilder {
		private final DefaultKeyboard keyboard;

		public DefaultKeyboardBuilder(DefaultKeyboard keyboard) {
			this.keyboard = keyboard;
		}

		@Override
		public IKeyboardBuilder button(int row, Button button) {
			while (keyboard.buttons.size() <= row)
				keyboard.buttons.add(new ArrayList<>());

			keyboard.buttons.get(row).add(button);
			return this;
		}

		@Override
		public IKeyboardBuilder buttons(List<List<Button>> buttons) {
			keyboard.buttons = buttons;
			return this;
		}

		@Override
		public IKeyboardBuilder row(Button... buttons) {
			keyboard.addRow(buttons);
			return this;
		}

		@Override
		public IKeyboardBuilder type(KeyboardType type) {
			keyboard.keyboardType = type;
			return this;
		}

		@Override
		public IKeyboard build() {
			return keyboard;
		}
	}
}
