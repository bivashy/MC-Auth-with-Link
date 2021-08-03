package me.mastercapexd.auth.bungee.command;

import java.util.concurrent.TimeUnit;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.RandomCodeFactory;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class VKLinkCommand extends Command {
	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;

	public VKLinkCommand(AuthPlugin plugin, PluginConfig config, AccountStorage accountStorage) {
		super("addvk", null, "vkadd", "vklink", "linkvk");
		this.plugin = plugin;
		this.config = config;
		this.accountStorage = accountStorage;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(config.getBungeeMessages().getMessage("players-only"));
			return;
		}

		if (!config.getVKSettings().isEnabled()) {
			sender.sendMessage(config.getBungeeMessages().getMessage("vk-disabled"));
			return;
		}

		if (args.length == 0) {
			sender.sendMessage(config.getBungeeMessages().getMessage("vk-usage"));
			return;
		}

		String id = config.getActiveIdentifierType().getId((ProxiedPlayer) sender);

		String screen_name = args[0];

		Integer vkId = plugin.getVkUtils().getVKIdFromScreenName(screen_name);
		if (Auth.getVKConfirmationEntry(vkId) != null) {
			sender.sendMessage(config.getBungeeMessages().getMessage("vk-already-sent"));
			return;
		}
		if (vkId == null || vkId == -1) {
			sender.sendMessage(config.getBungeeMessages().getMessage("screen-name-not-exists"));
			return;
		}
		accountStorage.getAccount(id).thenAccept(account -> {
			if (account.getVKId() != null && account.getVKId() != -1) {
				sender.sendMessage(config.getBungeeMessages().getMessage("already-linked"));
				return;
			}
			String code = RandomCodeFactory.generateCode(config.getVKSettings().getConfirmationSettings().getCodeLength());
			Auth.addVKConfirmationEntry(id, vkId, code);
			startRemoveTimer(vkId);
			sender.sendMessage(
					config.getBungeeMessages().getLegacyMessage("confirmation-vk-sent").replaceAll("(?i)%code%", code));
		});
	}

	private void startRemoveTimer(Integer vkId) {
		ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
			if (Auth.getVKConfirmationEntry(vkId) != null)
				Auth.removeVKConfirmationEntry(vkId);
		}, config.getVKSettings().getConfirmationSettings().getRemoveDelay(), TimeUnit.SECONDS);
	}

}
