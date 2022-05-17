package me.mastercapexd.auth.proxy.commands.parameters;

public class RegisterPassword {
	private final String password;

	public RegisterPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
}
