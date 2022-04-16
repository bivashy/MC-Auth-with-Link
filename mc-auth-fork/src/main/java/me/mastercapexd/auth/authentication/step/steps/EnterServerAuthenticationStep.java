package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.events.LoginEvent;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.proxy.server.Server;

public class EnterServerAuthenticationStep extends AbstractAuthenticationStep {
	private static final ProxyPlugin PLUGIN = ProxyPlugin.instance();
	public static final String STEP_NAME = "ENTER_SERVER";

	public EnterServerAuthenticationStep(AuthenticationStepContext context) {
		super(STEP_NAME, context);
	}

	@Override
	public boolean shouldPassToNextStep() {
		return true;
	}

	@Override
	public boolean shouldSkip() {
		enterServer();
		return true;
	}

	@SuppressWarnings("deprecation")
	public void enterServer() {
		Account account = authenticationStepContext.getAccount();
		String accountId = account.getId();
		ProxyPlayer player = account.getPlayer().get();
		LoginEvent loginEvent = new LoginEvent(account);
		ProxyPlugin.instance().getCore().callEvent(loginEvent);

		if (loginEvent.isCancelled())
			return;

		account.setLastIpAddress(player.getRemoteAddress().getHostString());
		account.setLastSessionStart(System.currentTimeMillis());
		PLUGIN.getAccountStorage().saveOrUpdateAccount(account);
		Auth.removeAccount(accountId);
		Server connectServer = PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getGameServers()).asProxyServer();
		player.sendTo(connectServer);
	}

	public static class EnterServerAuthenticationStepCreator extends AbstractAuthenticationStepCreator {
		public EnterServerAuthenticationStepCreator() {
			super(STEP_NAME);
		}

		@Override
		public EnterServerAuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
			return new EnterServerAuthenticationStep(context);
		}
	}

}
