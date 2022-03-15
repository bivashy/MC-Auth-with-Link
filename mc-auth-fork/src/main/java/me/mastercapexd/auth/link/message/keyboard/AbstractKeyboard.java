package me.mastercapexd.auth.link.message.keyboard;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.message.keyboard.button.Button;

public abstract class AbstractKeyboard implements IKeyboard {
	protected static Integer rowLimit = 5;
	protected final List<List<Button>> buttons = new ArrayList<>();
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
}
