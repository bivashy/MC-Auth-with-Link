package me.mastercapexd.auth.config.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FillType {
	RANDOM {

		@Override
		public List<ConfigurationServer> shuffle(List<ConfigurationServer> servers) {
			List<ConfigurationServer> modifableServers = new ArrayList<>(servers); // Method argument can pass
																					// unmodifable list
			Collections.shuffle(modifableServers);
			return modifableServers;
		}

	},
	GRADUALLY {

		@Override
		public List<ConfigurationServer> shuffle(List<ConfigurationServer> servers) {
			return servers;
		}

	};

	public abstract List<ConfigurationServer> shuffle(List<ConfigurationServer> servers);
}
