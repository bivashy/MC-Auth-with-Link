package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.proxy.message.ProxyComponent;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;

@Command({ "googlecode", "gcode" })
public class GoogleCodeCommand {
	private static final Messages<ProxyComponent> GOOGLE_MESSAGES = ProxyPlugin.instance().getConfig()
			.getProxyMessages().getSubMessages("google");
	@Dependency
	private ProxyPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	public void defaultCommand(ProxyPlayer player) {
		player.sendMessage(GOOGLE_MESSAGES.getStringMessage("code-not-enough-arguments"));
	}

	@GoogleUse
	@Command({ "googlecode", "gcode", "google code" })
	public void googleCode(ProxyPlayer player, Integer code) {
		String playerId = config.getActiveIdentifierType().getId(player);
		accountStorage.getAccount(playerId).thenAccept(account -> {
			if (account == null || !account.isRegistered()) {
				player.sendMessage(config.getProxyMessages().getStringMessage("account-not-found"));
				return;
			}
			LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(null);
			if (linkUser == null || linkUser.getLinkUserInfo().getIdentificator().asString().isEmpty()) {
				player.sendMessage(GOOGLE_MESSAGES.getStringMessage("code-not-exists"));
				return;
			}
			if (!Auth.hasGoogleAuthAccount(playerId)) {
				player.sendMessage(GOOGLE_MESSAGES.getStringMessage("code-not-need-enter"));
				return;
			}
			if (plugin.getGoogleAuthenticator().authorize(linkUser.getLinkUserInfo().getIdentificator().asString(),
					code)) {
				player.sendMessage(GOOGLE_MESSAGES.getStringMessage("code-entered"));
				Auth.removeGoogleAuthAccount(playerId);
				Auth.removeAccount(account.getId());
				player.sendTo(config.findServerInfo(config.getGameServers()).asProxyServer());
				return;
			}
			player.sendMessage(GOOGLE_MESSAGES.getStringMessage("code-wrong-code"));
		});
	}
}
