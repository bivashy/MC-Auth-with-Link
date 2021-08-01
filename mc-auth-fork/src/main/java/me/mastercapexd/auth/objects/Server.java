package me.mastercapexd.auth.objects;

import com.google.common.base.Preconditions;

public class Server {
	private final String id;
	private final int maxPlayers;

	public Server(String stringFormat) {
		String[] args = stringFormat.split(":");
		Preconditions.checkArgument(args.length >= 2,
				String.format("Wrong server format in config.yml: %s.", stringFormat));
		this.id = args[0];
		this.maxPlayers = Integer.parseInt(args[1]);
	}

	public Server(String id, int maxPlayers) {
		this.id = id;
		this.maxPlayers = maxPlayers;
	}

	public String getId() {
		return id;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
}