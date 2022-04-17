package me.mastercapexd.auth.link.message;

import me.mastercapexd.auth.link.message.keyboard.IKeyboard;

public abstract class AbstractMessage implements Message {
	protected String rawContent = "";
	protected IKeyboard keyboard;

	public AbstractMessage(String rawContent) {
		this.rawContent = rawContent;
	}

	@Override
	public String getRawContent() {
		return rawContent;
	}

	@Override
	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;

	}

	@Override
	public IKeyboard getKeyboard() {
		return keyboard;
	}

	@Override
	public void setKeyboard(IKeyboard keyboard) {
		this.keyboard = keyboard;
	}
}
