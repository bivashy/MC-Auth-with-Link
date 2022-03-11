package me.mastercapexd.auth.authentication.step.steps;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.authentication.step.AbstractAuthenticationStep;
import me.mastercapexd.auth.authentication.step.context.AuthenticationStepContext;
import me.mastercapexd.auth.authentication.step.creators.AbstractAuthenticationStepCreator;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.events.LoginEvent;
import me.mastercapexd.auth.utils.Connector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class EnterServerAuthenticationStep extends AbstractAuthenticationStep {
	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
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
		ProxiedPlayer player = account.getIdentifierType().getPlayer(accountId);
		LoginEvent loginEvent = new LoginEvent(account);
		ProxyServer.getInstance().getPluginManager().callEvent(loginEvent);	
		
		if (loginEvent.isCancelled())
			return;

		account.setLastIpAddress(player.getAddress().getHostString());
		account.setLastSessionStart(System.currentTimeMillis());
		PLUGIN.getAccountStorage().saveOrUpdateAccount(account);
		Auth.removeAccount(accountId);
		ServerInfo connectServer = PLUGIN.getConfig().findServerInfo(PLUGIN.getConfig().getGameServers());
		Connector.connectOrKick(player, connectServer,
				PLUGIN.getConfig().getBungeeMessages().getMessage("game-servers-connection-refused"));
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
