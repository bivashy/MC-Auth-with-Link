package me.mastercapexd.auth.proxy;

import com.ubivashka.configuration.ConfigurationProcessor;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.dealerships.AuthenticationStepContextFactoryDealership;
import me.mastercapexd.auth.dealerships.AuthenticationStepCreatorDealership;
import me.mastercapexd.auth.function.Castable;
import me.mastercapexd.auth.proxy.hooks.PluginHook;
import me.mastercapexd.auth.storage.AccountStorage;

public interface ProxyPlugin extends Castable<ProxyPlugin> {
	ProxyCore getCore();

	PluginConfig getConfig();

	AccountFactory getAccountFactory();

	AccountStorage getAccountStorage();

	GoogleAuthenticator getGoogleAuthenticator();

	AuthenticationStepCreatorDealership getAuthenticationStepCreatorDealership();

	AuthenticationStepContextFactoryDealership getAuthenticationContextFactoryDealership();

	ConfigurationProcessor getConfigurationProcessor();

	<T extends PluginHook> T getHook(Class<T> clazz);
	
	String getVersion();

	static ProxyPlugin instance() {
		return ProxyPluginProvider.getPluginInstance();
	}
}
