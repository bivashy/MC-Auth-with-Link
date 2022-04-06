package me.mastercapexd.auth.proxy.message;

import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public interface ProxyComponent extends Castable<ProxyComponent>{
	static ProxyComponent fromString(String text) {
		return ProxyPlugin.instance().getCore().component(text);
	}

	/**
	 * Returns component as raw type, return type depends on platform, but default
	 * is json
	 * 
	 * @return component converted to raw type
	 */
	String jsonText();
	
	/**
	 * Returns text with color codes, and hex (if possibly)
	 * 
	 * @return
	 */
	String legacyText();
	
	/**
	 * Returns plain text of component. Removes all color codes and other things.
	 * 
	 * @return
	 */
	String plainText();
}
