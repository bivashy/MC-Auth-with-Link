package me.mastercapexd.auth.link;

public abstract class AbstractLinkType implements LinkType {
	protected final String linkTypeName;

	public AbstractLinkType(String linkTypeName) {
		this.linkTypeName = linkTypeName;
	}

	@Override
	public String getLinkName() {
		return linkTypeName;
	}
}
