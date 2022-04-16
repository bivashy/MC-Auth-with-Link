package me.mastercapexd.auth;

public enum KickResult {
	KICKED("kicked"), PLAYER_OFFLINE("player-offline");
	
	private final String configurationPath;
	
	KickResult(String configurationPath) {
		this.configurationPath = configurationPath;
	}

	public String getConfigurationPath() {
		return configurationPath;
	}
}
