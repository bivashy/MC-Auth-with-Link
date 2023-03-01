package com.bivashy.auth.api.hook;

import com.bivashy.auth.api.util.Castable;

/**
 * Object that provides access to the api
 *
 * @author User
 */
public interface PluginHook extends Castable<PluginHook> {
    boolean canHook();
}
