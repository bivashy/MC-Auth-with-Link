package me.mastercapexd.auth.proxy.hooks;

import me.mastercapexd.auth.function.Castable;

/**
 * Object that provides access to the api
 * 
 * @author User
 *
 */
public interface PluginHook extends Castable<PluginHook> {
	boolean canHook();
}
