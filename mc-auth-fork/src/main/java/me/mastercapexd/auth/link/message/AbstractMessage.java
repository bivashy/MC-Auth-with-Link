package me.mastercapexd.auth.link.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.mastercapexd.auth.link.message.keyboard.IKeyboard;

public abstract class AbstractMessage implements Message {
	protected final Map<String, String> additionalInfos = new HashMap<>();
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

	/**
	 * @return Unmodifable map of additional info
	 */
	@Override
	public Map<String, String> getAdditionalInfo() {
		return Collections.unmodifiableMap(additionalInfos);
	}

}
