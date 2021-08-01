package me.mastercapexd.auth;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public enum IdentifierType {

	UUID {
		public String getId(ProxiedPlayer proxiedPlayer) {
			return proxiedPlayer.getUniqueId().toString();
		}

		public ProxiedPlayer getPlayer(String id) {
			return ProxyServer.getInstance().getPlayer(java.util.UUID.fromString(id));
		}

		public String fromRawString(String uuid) {
			return uuid;
		}
	},
	NAME {
		public String getId(ProxiedPlayer proxiedPlayer) {
			return proxiedPlayer.getName().toLowerCase();
		}

		public ProxiedPlayer getPlayer(String id) {
			return ProxyServer.getInstance().getPlayer(id);
		}

		public String fromRawString(String name) {
			return name.toLowerCase();
		}
	};

	public abstract String getId(ProxiedPlayer proxiedPlayer);

	public abstract ProxiedPlayer getPlayer(String id);

	public abstract String fromRawString(String id);
}