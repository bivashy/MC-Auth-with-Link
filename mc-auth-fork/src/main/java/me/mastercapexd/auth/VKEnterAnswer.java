package me.mastercapexd.auth;

public enum VKEnterAnswer {
	CONFIRM("confirm"), DECLINE("decline");

	private String payload;

	private VKEnterAnswer(String payload) {
		this.payload = payload;
	}

	public static VKEnterAnswer getByPayload(String payload) {
		for (VKEnterAnswer answer : values()) 
			if (answer.payload.equals(payload))
				return answer;
		return null;
	}
}
