package me.mastercapexd.auth.link.user.info;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractLinkUserInfo implements LinkUserInfo {
	protected final Map<String, String> additionalInfo = new HashMap<>();
	protected Boolean confirmationEnabled = true;
	protected Integer linkUserId;

	public AbstractLinkUserInfo(Integer linkUserId, Boolean confirmationEnabled) {
		this.linkUserId = linkUserId;
		this.confirmationEnabled = confirmationEnabled;
	}

	public AbstractLinkUserInfo(Integer linkUserId) {
		this(linkUserId, true);
	}

	@Override
	public Map<String, String> getAdditionalInfos() {
		return Collections.unmodifiableMap(additionalInfo);
	}

	@Override
	public Integer getLinkUserId() {
		return linkUserId;
	}

	@Override
	public void setLinkUserId(Integer linkUserId) {
		this.linkUserId = linkUserId;
	}

	@Override
	public Boolean isConfirmationEnabled() {
		return confirmationEnabled;
	}

	@Override
	public void setConfirmationEnabled(Boolean confirmationEnabled) {
		this.confirmationEnabled = confirmationEnabled;
	}
}
