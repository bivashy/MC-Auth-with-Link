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
	
	public abstract class DefaultButtonBuilder implements ButtonBuilder {
		@Override
		public ButtonBuilder action(ButtonAction action) {
			DefaultButton.this.action = action;
			return this;
		}

		@Override
		public ButtonBuilder actionData(String actionData) {
			DefaultButton.this.actionData = actionData;
			return this;
		}

		@Override
		public ButtonBuilder color(ButtonColor color) {
			DefaultButton.this.color = color;
			return this;
		}

		@Override
		public ButtonBuilder label(String label) {
			DefaultButton.this.label = label;
			return this;
		}

		@Override
		public Button build() {
			return wrap(DefaultButton.this);
		}

		protected abstract Button wrap(DefaultButton buildedButton);
	}
}
