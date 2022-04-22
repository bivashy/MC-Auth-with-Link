package me.mastercapexd.auth.link.message.keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

public abstract class AbstractKeyboard implements IKeyboard {
	protected static Integer rowLimit = 5;
	protected List<List<Button>> buttons = new ArrayList<>();
	protected boolean inline;

	@Override
	public void attach(Message message) {
		message.setKeyboard(this);
	}

	@Override
	public void addButton(int row, Button button) {
		if (row >= rowLimit)
			throw new IllegalArgumentException();
		while (buttons.size() <= row)
			buttons.add(new ArrayList<>());

		buttons.get(row).add(button);
	}

	@Override
	public boolean isInline() {
		return inline;
	}

	@Override
	public void setInline(boolean inline) {
		this.inline = inline;
	}

	@Override
	public Button[][] getButtons() {
		return buttons.stream().map(list -> list.toArray(new Button[0])).toArray(Button[][]::new);
	}

	@Override
	public void removeIf(Predicate<Button> filter) {
		buttons.forEach(buttonsList -> buttonsList.removeIf(filter));
		buttons.removeIf(List::isEmpty);
	}

	@Override
	public void replaceIf(Predicate<Button> filter, Function<Button, Button> replaceFunction) {
		buttons = buttons.stream()
				.map(list -> list.stream().filter(filter).map(replaceFunction).collect(Collectors.toList()))
				.collect(Collectors.toList());
	}

}
