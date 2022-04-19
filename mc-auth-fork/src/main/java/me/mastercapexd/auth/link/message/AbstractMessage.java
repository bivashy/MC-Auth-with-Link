package me.mastercapexd.auth.link.message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.link.message.keyboard.IKeyboard;

public abstract class AbstractMessage implements Message {
	protected final List<File> photos = new ArrayList<>();
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
	public void uploadPhoto(File photo) {
		photos.add(photo);
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
