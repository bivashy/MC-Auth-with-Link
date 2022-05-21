package me.mastercapexd.auth.config.message.proxy;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.message.context.account.DefaultAccountPlaceholderContext;

public class ProxyMessageContext extends DefaultAccountPlaceholderContext {
	public ProxyMessageContext(Account account) {
		super(account);
	}
}
