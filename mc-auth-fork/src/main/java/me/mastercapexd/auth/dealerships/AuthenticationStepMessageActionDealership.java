package me.mastercapexd.auth.dealerships;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.mastercapexd.auth.actions.MessageAction;

public class AuthenticationStepMessageActionDealership implements MapDealership<String, MessageAction>{

	private final Map<String,MessageAction> messageActions = new HashMap<>();
		
	@Override
	public Map<String, MessageAction> getMap() {
		return Collections.unmodifiableMap(messageActions);
	}

	@Override
	public MessageAction put(String key, MessageAction value) {
		return messageActions.put(key, value);
	}

	@Override
	public MessageAction remove(String key) {
		return messageActions.remove(key);
	}

	@Override
	public MessageAction get(Object key) {
		return messageActions.get(key);
	}

	@Override
	public MessageAction getOrDefault(Object key, MessageAction def) {
		return messageActions.get(key);
	}

	@Override
	public boolean containsKey(Object key) {
		return messageActions.containsKey(key);
	}
	
}