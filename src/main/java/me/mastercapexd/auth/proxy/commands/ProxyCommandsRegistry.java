package me.mastercapexd.auth.proxy.commands;

import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.proxy.commands.annotations.TelegramUse;
import me.mastercapexd.auth.proxy.commands.annotations.VkUse;
import me.mastercapexd.auth.proxy.commands.parameters.DoublePassword;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import me.mastercapexd.auth.proxy.commands.parameters.RegisterPassword;
import me.mastercapexd.auth.storage.AccountStorage;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.exception.SendMessageException;

public abstract class ProxyCommandsRegistry {
	protected ProxyPlugin plugin = ProxyPlugin.instance();
	protected CommandHandler commandHandler;

	public ProxyCommandsRegistry(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;

		registerCommandContexts();
		registerDependencies();
	}

	private void registerCommandContexts() {
		PluginConfig config = plugin.getConfig();

		commandHandler.registerValueResolver(DoublePassword.class, (context) -> {
			ArgumentStack arguments = context.arguments();
			String oldPassword = arguments.pop();
			if (arguments.isEmpty())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("enter-new-password"));
			String newPassword = arguments.pop();
			DoublePassword password = new DoublePassword(oldPassword, newPassword);
			if (oldPassword.equals(newPassword))
				throw new SendMessageException(config.getProxyMessages().getStringMessage("nothing-to-change"));

			if (newPassword.length() < config.getPasswordMinLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-short"));

			if (newPassword.length() > config.getPasswordMaxLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-long"));
			return password;
		});

		commandHandler.registerValueResolver(NewPassword.class, context -> {
			String newRawPassword = context.pop();
			if (newRawPassword.length() < config.getPasswordMinLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-short"));

			if (newRawPassword.length() > config.getPasswordMaxLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-long"));
			return new NewPassword(newRawPassword);
		});

		commandHandler.registerValueResolver(RegisterPassword.class, context -> {
			ArgumentStack arguments = context.arguments();
			String registerPassword = arguments.pop();

			if (config.isPasswordConfirmationEnabled()) {
				if (arguments.isEmpty())
					throw new SendMessageException(config.getProxyMessages().getStringMessage("confirm-password"));
				String confirmationPassword = arguments.pop();
				if (!confirmationPassword.equals(registerPassword))
					throw new SendMessageException(config.getProxyMessages().getStringMessage("confirm-failed"));
			}

			if (registerPassword.length() < config.getPasswordMinLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-short"));

			if (registerPassword.length() > config.getPasswordMaxLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-long"));
			return new RegisterPassword(registerPassword);
		});

		commandHandler.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(GoogleUse.class))
				return;
			if (!config.getGoogleAuthenticatorSettings().isEnabled())
				throw new SendMessageException(
						config.getProxyMessages().getSubMessages("google").getStringMessage("disabled"));
		});

		commandHandler.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(VkUse.class))
				return;
			if (!config.getVKSettings().isEnabled())
				throw new SendMessageException(
						config.getProxyMessages().getSubMessages("vk").getStringMessage("disabled"));
		});
		
		commandHandler.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(TelegramUse.class))
				return;
			if (!config.getTelegramSettings().isEnabled())
				throw new SendMessageException(
						config.getProxyMessages().getSubMessages("telegram").getStringMessage("disabled"));
		});
	}

	private void registerDependencies() {
		commandHandler.registerDependency(PluginConfig.class, plugin.getConfig());
		commandHandler.registerDependency(AccountStorage.class, plugin.getAccountStorage());
		commandHandler.registerDependency(ProxyPlugin.class, plugin);
	}

	protected void registerCommands() {
		commandHandler.register(new AuthCommand(), new LoginCommand(), new RegisterCommand(),
				new ChangePasswordCommand(), new GoogleCodeCommand(), new GoogleCommand(), new GoogleUnlinkCommand(),
				new LogoutCommand());

		if (plugin.getConfig().getVKSettings().isEnabled())
			commandHandler.register(new VKLinkCommand());
		if (plugin.getConfig().getTelegramSettings().isEnabled())
			commandHandler.register(new TelegramLinkCommand());
	}

}
