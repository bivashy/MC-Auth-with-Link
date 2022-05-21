package me.mastercapexd.auth.config.message.context;

import java.util.Map;
import java.util.Map.Entry;

public interface MessageContext {
	Map<String, String> getPlaceholders();

	default String formatString(String rawString) {
		if (rawString == null)
			return rawString;

		for (Entry<String, String> entry : getPlaceholders().entrySet())
			rawString = rawString.replaceAll(entry.getKey(), entry.getValue());

		return rawString;
	}
}
