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

	public abstract class DefaultKeyboardBuilder implements IKeyboardBuilder {
		@Override
		public IKeyboardBuilder button(int row, Button button) {
			while (buttons.size() <= row)
				buttons.add(new ArrayList<>());

			buttons.get(row).add(button);
			return this;
		}

		@Override
		public IKeyboardBuilder buttons(List<List<Button>> buttons) {
			DefaultKeyboard.this.buttons = buttons;
			return this;
		}

		@Override
		public IKeyboardBuilder row(Button... buttons) {
			DefaultKeyboard.this.addRow(buttons);
			return this;
		}

		@Override
		public IKeyboardBuilder type(KeyboardType type) {
			DefaultKeyboard.this.keyboardType = type;
			return this;
		}

		@Override
		public IKeyboard build() {
			return wrap(DefaultKeyboard.this);
		}

		protected abstract IKeyboard wrap(DefaultKeyboard buildedKeyboard);
	}
}
