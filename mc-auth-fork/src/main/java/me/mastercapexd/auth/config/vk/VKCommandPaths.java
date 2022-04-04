package me.mastercapexd.auth.config.vk;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.config.factories.ConfigurationHolderMapResolverFactory.ConfigurationHolderMap;
import me.mastercapexd.auth.proxy.ProxyPlugin;

public class VKCommandPaths implements ConfigurationHolder {

	@ConfigField
	private ConfigurationHolderMap<VKCommandPath> vkCommands = new ConfigurationHolderMap<>();

	public VKCommandPaths(ConfigurationSectionHolder sectionHolder) {
		ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
	}

	public VKCommandPath getPath(String commandKey) {
		return vkCommands.getOrDefault(commandKey, null);
	}
}
