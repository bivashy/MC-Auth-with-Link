package me.mastercapexd.auth.config.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FillType {
	RANDOM {

		@Override
		public List<Server> shuffle(List<Server> servers) {
			List<Server> modifableServers = new ArrayList<>(servers); // Method argument can pass unmodifable list
			Collections.shuffle(modifableServers);
			return modifableServers;
		}

	},
	GRADUALLY {

		@Override
		public List<Server> shuffle(List<Server> servers) {
			return servers;
		}

	};

	public abstract List<Server> shuffle(List<Server> servers);
}
