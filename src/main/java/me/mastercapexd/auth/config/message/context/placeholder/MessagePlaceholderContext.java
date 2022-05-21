package me.mastercapexd.auth.config.message.context.placeholder;

import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.config.message.context.MessageContext;

public abstract class MessagePlaceholderContext implements MessageContext {
	private final List<PlaceholderProvider> placeholderProviders = new ArrayList<>();

	public MessagePlaceholderContext registerPlaceholderProvider(PlaceholderProvider supplier) {
		placeholderProviders.add(supplier);
		return this;
	}

	@Override
	public String apply(String rawString) {
		for (PlaceholderProvider placeholderProvider : placeholderProviders)
			if (placeholderProvider.containsPlaceholder(rawString))
				rawString = placeholderProvider.replaceAll(rawString);
		return rawString;
	}
}
