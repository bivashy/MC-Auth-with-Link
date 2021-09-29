package me.mastercapexd.auth.vk.commands;

import com.ubivashka.vk.bungee.events.VKMessageEvent;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import net.md_5.bungee.config.Configuration;

public class VKCustomCommand extends VKCommandExecutor {
	private final String command, answer;
	private boolean chat = false, isRegex = false;
	private String chatAnswer = null;
	private String jsonKeyboard = null;

	public VKCustomCommand(String command, Configuration section) {
		if (section.contains("regex")) {
			this.command = section.getString("regex");
			isRegex = true;
		} else {
			this.command = command;
		}
		answer = section.getString("answer");
		if (section.contains("chat"))
			chat = section.getBoolean("chat");
		if (!chat)
			chatAnswer = section.getString("chat-answer");
		if (section.contains("keyboard"))
			jsonKeyboard = section.getString("keyboard");

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

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (isChat(e.getPeer())) {
			if (!isChat() && chatAnswer != null && !chatAnswer.isEmpty())
				sendMessage(e.getPeer(), getChatAnswer());

			return;
		}
		sendMessage(e.getPeer(), getAnswer());
	}

	@Override
	public boolean sendMessage(Integer peerId, String message) {
		try {
			MessagesSendQuery sendQuery = vk.messages().send(actor).randomId(random.nextInt()).peerId(peerId)
					.message(message);
			if (jsonKeyboard != null)
				sendQuery.unsafeParam("keyboard", jsonKeyboard);
			sendQuery.execute();
			return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isRegex() {
		return isRegex;
	}

	@Override
	public String getKey() {
		return "custom-command";
	}
}
