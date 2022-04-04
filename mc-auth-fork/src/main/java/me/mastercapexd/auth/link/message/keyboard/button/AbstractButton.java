package me.mastercapexd.auth.link.message.keyboard.button;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractButton implements Button {
	protected final Map<String, String> additionalInfo = new HashMap<>();
	protected String label = "";

	public AbstractButton(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Map<String, String> getAdditionalInfo() {
		return Collections.unmodifiableMap(additionalInfo);
	}

	@Override
	public void putAdditionalInfo(String key, String value) {
		additionalInfo.put(key, value);
	}

}
