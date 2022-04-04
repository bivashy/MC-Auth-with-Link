package me.mastercapexd.auth;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public enum IdentifierType {

	UUID {
		public String getId(ProxyPlayer player) {
			return player.getUniqueId().toString();
		}

		public ProxyPlayer getPlayer(String id) {
			return ProxyPlugin.instance().getCore().getPlayer(java.util.UUID.fromString(id));
		}

		public String fromRawString(String uuid) {
			return uuid;
		}
	},
	NAME {
		public String getId(ProxyPlayer player) {
			return player.getNickname().toLowerCase();
		}

		public ProxyPlayer getPlayer(String id) {
			return ProxyPlugin.instance().getCore().getPlayer(id);
		}

		public String fromRawString(String name) {
			return name.toLowerCase();
		}
	};

	public abstract String getId(ProxyPlayer proxiedPlayer);

	public abstract ProxyPlayer getPlayer(String id);

	public abstract String fromRawString(String id);
}