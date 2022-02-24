package me.mastercapexd.auth.link.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mastercapexd.auth.link.message.keyboard.button.Button;
import me.mastercapexd.auth.link.user.LinkUser;

public abstract class AbstractMessage<T extends LinkUser> implements Message<T> {
	protected final Map<String, String> additionalInfos = new HashMap<>();
	protected String rawContent = "";
	protected List<Button> buttons = new ArrayList<>();

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

	/**
	 * @return Unmodifable list instances of {@link Button}
	 */
	@Override
	public List<Button> getButtons() {
		return Collections.unmodifiableList(buttons);
	}

	@Override
	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	/**
	 * @return Unmodifable map of additional info
	 */
	@Override
	public Map<String, String> getAdditionalInfo() {
		return Collections.unmodifiableMap(additionalInfos);
	}

}
