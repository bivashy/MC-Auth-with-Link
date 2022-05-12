package me.mastercapexd.auth.vk.commands;

import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import com.ubivashka.vk.bungee.events.VKMessageEvent;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

public class VKCustomCommand {
	private final String command, answer;
	private boolean chat = false, isRegex = false;
	private String chatAnswer = null;
	private String jsonKeyboard = null;

	public VKCustomCommand(String command, ConfigurationSectionHolder sectionHolder) {
		if (sectionHolder.contains("regex")) {
			this.command = sectionHolder.getString("regex");
			isRegex = true;
		} else {
			this.command = command;
		}
		answer = sectionHolder.getString("answer");
		if (sectionHolder.contains("chat"))
			chat = sectionHolder.getBoolean("chat");
		if (!chat)
			chatAnswer = sectionHolder.getString("chat-answer");
		if (sectionHolder.contains("keyboard"))
			jsonKeyboard = sectionHolder.getString("keyboard");

	}

	public VKCustomCommand(String command, String answer, boolean chat, String chatAnswer) {
		this(command, answer, chat);
		if (chat)
			this.chatAnswer = chatAnswer;
	}

	public VKCustomCommand(String command, String answer, boolean chat) {
		this(command, answer);
		this.chat = chat;
	}

	public VKCustomCommand(String command, String answer) {
		this.command = command;
		this.answer = answer;
	}

	public String getCommand() {
		return command;
	}

	public String getAnswer() {
		return answer;
	}

	public boolean isChat() {
		return chat;
	}

	public void setChat(boolean chat) {
		this.chat = chat;
	}

	public String getChatAnswer() {
		return chatAnswer;
	}

	public void setChatAnswer(String chatAnswer) {
		this.chatAnswer = chatAnswer;
	}

	public void execute(VKMessageEvent e, String[] args) {
//		if (isChat(e.getPeer())) {
//			if (!isChat() && chatAnswer != null && !chatAnswer.isEmpty())
//				sendMessage(e.getPeer(), getChatAnswer());
//
//			return;
//		}
//		sendMessage(e.getPeer(), getAnswer());
	}

	public boolean sendMessage(Integer peerId, String message) {
//		try {
//			MessagesSendQuery sendQuery = vk.messages().send(actor).randomId(random.nextInt()).peerId(peerId)
//					.message(message);
//			if (jsonKeyboard != null)
//				sendQuery.unsafeParam("keyboard", jsonKeyboard);
//			sendQuery.execute();
//			return true;
//		} catch (ApiException | ClientException e) {
//			e.printStackTrace();
//			return false;
//		}
		return false;
	}

	public boolean isRegex() {
		return isRegex;
	}
}
