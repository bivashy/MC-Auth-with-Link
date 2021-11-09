package me.mastercapexd.auth;

import java.util.Collections;
import java.util.List;

import me.mastercapexd.auth.objects.Server;

public enum FillType {
	RANDOM {

		@Override
		public void shuffle(List<Server> servers) {
			Collections.shuffle(servers);
		}

	},
	GRADUALLY {

		@Override
		public void shuffle(List<Server> servers) {

		}

	};

	public abstract void shuffle(List<Server> servers);
}
