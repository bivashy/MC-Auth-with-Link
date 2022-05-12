package me.mastercapexd.auth.link.message.keyboard.button;

public abstract class DefaultButton implements Button {
	protected String label, actionData;
	protected ButtonColor color;
	protected ButtonAction action;

	public DefaultButton(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getActionData() {
		return actionData;
	}

	@Override
	public ButtonColor getColor() {
		return color;
	}

	@Override
	public ButtonAction getAction() {
		return action;
	}

	public static abstract class DefaultButtonBuilder implements ButtonBuilder {
		private final DefaultButton button;

		public DefaultButtonBuilder(DefaultButton button) {
			this.button = button;
		}

		@Override
		public ButtonBuilder action(ButtonAction action) {
			button.action = action;
			return this;
		}

		@Override
		public ButtonBuilder actionData(String actionData) {
			button.actionData = actionData;
			return this;
		}

		@Override
		public ButtonBuilder color(ButtonColor color) {
			button.color = color;
			return this;
		}

		@Override
		public ButtonBuilder label(String label) {
			button.label = label;
			return this;
		}

		@Override
		public Button build() {
			return button;
		}
	}
}
