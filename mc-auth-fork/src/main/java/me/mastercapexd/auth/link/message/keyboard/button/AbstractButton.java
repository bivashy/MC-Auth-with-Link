package me.mastercapexd.auth.link.message.keyboard.button;

public abstract class AbstractButton implements Button {
	protected String label = "";

	public AbstractButton(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return label;
	}
}
