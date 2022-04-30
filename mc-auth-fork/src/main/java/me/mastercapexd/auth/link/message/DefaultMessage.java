package me.mastercapexd.auth.link.message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.mastercapexd.auth.link.message.keyboard.IKeyboard;

public abstract class DefaultMessage implements Message {
	protected List<File> photos = new ArrayList<>();
	protected String text = "";
	protected IKeyboard keyboard;

	public DefaultMessage(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	public List<File> getPhotos() {
		return photos;
	}

	@Override
	public void uploadPhoto(File photo) {
		photos.add(photo);
	}

	@Override
	public IKeyboard getKeyboard() {
		return keyboard;
	}

	public abstract class DefaultMessageBuilder implements MessageBuilder {
		@Override
		public MessageBuilder keyboard(IKeyboard keyboard) {
			DefaultMessage.this.keyboard = keyboard;
			return this;
		}

		@Override
		public MessageBuilder uploadPhoto(File photo) {
			DefaultMessage.this.uploadPhoto(photo);
			return this;
		}

		@Override
		public MessageBuilder text(String text) {
			DefaultMessage.this.text = text;
			return this;
		}

		@Override
		public Message build() {
			return wrap(DefaultMessage.this);
		}

		protected abstract Message wrap(DefaultMessage buildedMessage);
	}
}
