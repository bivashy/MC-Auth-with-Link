package me.mastercapexd.auth.objects;

public class VKConfirmationEntry {
	private final String id;
	private final String code;

	public VKConfirmationEntry(String id, String code) {
		this.id = id;
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

}
