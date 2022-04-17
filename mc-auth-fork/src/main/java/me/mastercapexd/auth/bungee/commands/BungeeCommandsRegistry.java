package me.mastercapexd.auth.bungee.commands;

import java.util.Arrays;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.exception.BungeeExceptionHandler;
import me.mastercapexd.auth.bungee.config.BungeePluginConfig;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer.BungeeProxyPlayerFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.AuthCommand;
import me.mastercapexd.auth.proxy.commands.ChangePasswordCommand;
import me.mastercapexd.auth.proxy.commands.GoogleCodeCommand;
import me.mastercapexd.auth.proxy.commands.GoogleCommand;
import me.mastercapexd.auth.proxy.commands.GoogleUnlinkCommand;
import me.mastercapexd.auth.proxy.commands.LoginCommand;
import me.mastercapexd.auth.proxy.commands.LogoutCommand;
import me.mastercapexd.auth.proxy.commands.RegisterCommand;
import me.mastercapexd.auth.proxy.commands.VKLinkCommand;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.proxy.commands.annotations.Permission;
import me.mastercapexd.auth.proxy.commands.annotations.VkUse;
import me.mastercapexd.auth.proxy.commands.parameters.ArgumentProxyPlayer;
import me.mastercapexd.auth.proxy.commands.parameters.DoublePassword;
import me.mastercapexd.auth.proxy.commands.parameters.NewPassword;
import me.mastercapexd.auth.proxy.commands.parameters.RegisterPassword;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;
import revxrsal.commands.bungee.core.BungeeHandler;
import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.exception.SendMessageException;

public class BungeeCommandsRegistry {
	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
	public static final CommandHandler BUNGEE_COMMAND_HANDLER = new BungeeHandler(PLUGIN).disableStackTraceSanitizing();

	public BungeeCommandsRegistry() {
		register();
	}

	private void register() {
		BUNGEE_COMMAND_HANDLER.setExceptionHandler(new BungeeExceptionHandler(PLUGIN.getConfig().getProxyMessages()));

		registerCommandContexts();
		registerDependencies();
		registerCommands();
	}

	private void registerCommandContexts() {
		BungeePluginConfig config = PLUGIN.getConfig();

		BUNGEE_COMMAND_HANDLER.registerValueResolver(DoublePassword.class, (context) -> {
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

		BUNGEE_COMMAND_HANDLER.registerValueResolver(NewPassword.class, context -> {
			String newRawPassword = context.pop();
			if (newRawPassword.length() < config.getPasswordMinLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-short"));

			if (newRawPassword.length() > config.getPasswordMaxLength())
				throw new SendMessageException(config.getProxyMessages().getStringMessage("password-too-long"));
			return new NewPassword(newRawPassword);
		});

		BUNGEE_COMMAND_HANDLER.registerValueResolver(RegisterPassword.class, context -> {
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

		BUNGEE_COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
			if (!actor.as(BungeeCommandActor.class).isPlayer())
				return;
			ProxyPlayer player = BungeeProxyPlayerFactory.wrapPlayer(actor.as(BungeeCommandActor.class).asPlayer());
			String accountId = config.getActiveIdentifierType().getId(player);
			if (!Auth.hasAccount(accountId))
				return;
			if (!command.hasAnnotation(AuthenticationStepCommand.class))
				return;
			Account account = Auth.getAccount(accountId);
			if (account.getCurrentAuthenticationStep() == null)
				return;
			String stepName = command.getAnnotation(AuthenticationStepCommand.class).stepName();
			if (account.getCurrentAuthenticationStep().getStepName().equals(stepName))
				return;
			throw new SendMessageException(config.getProxyMessages().getSubMessages("authentication-step-usage")
					.getStringMessage(account.getCurrentAuthenticationStep().getStepName()));
		});

		BUNGEE_COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(GoogleUse.class))
				return;
			if (!config.getGoogleAuthenticatorSettings().isEnabled())
				throw new SendMessageException(
						config.getProxyMessages().getSubMessages("google").getStringMessage("disabled"));
		});

		BUNGEE_COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(VkUse.class))
				return;
			if (!config.getVKSettings().isEnabled())
				throw new SendMessageException(
						config.getProxyMessages().getSubMessages("vk").getStringMessage("disabled"));
		});

		BUNGEE_COMMAND_HANDLER.registerContextResolver(ProxyPlayer.class, (context) -> {
			ProxiedPlayer selfPlayer = context.actor().as(BungeeCommandActor.class).asPlayer();
			if (selfPlayer == null)
				throw new SendMessageException(config.getProxyMessages().getStringMessage("players-only"));
			return BungeeProxyPlayerFactory.wrapPlayer(selfPlayer);
		});

		BUNGEE_COMMAND_HANDLER.registerValueResolver(ArgumentProxyPlayer.class, (context) -> {
			String value = context.pop();
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(value);
			if (player == null)
				throw new SendMessageException(config.getProxyMessages().getStringMessage("player-offline"));
			return new ArgumentProxyPlayer(BungeeProxyPlayerFactory.wrapPlayer(player));
		});

		BUNGEE_COMMAND_HANDLER.registerContextResolver(Account.class, (context) -> {
			ProxyPlayer player = BungeeProxyPlayerFactory
					.wrapPlayer(context.actor().as(BungeeCommandActor.class).asPlayer());
			if (player == null)
				throw new SendMessageException(config.getProxyMessages().getStringMessage("players-only"));
			String id = config.getActiveIdentifierType().getId(player);
			if (!Auth.hasAccount(id))
				throw new SendMessageException(config.getProxyMessages().getStringMessage("already-logged-in"));

			Account account = Auth.getAccount(id);
			if (!account.isRegistered() && !context.parameter().hasAnnotation(AuthenticationAccount.class))
				throw new SendMessageException(config.getProxyMessages().getStringMessage("account-not-found"));
			if (account.isRegistered() && context.parameter().hasAnnotation(AuthenticationAccount.class))
				throw new SendMessageException(config.getProxyMessages().getStringMessage("account-exists"));
			return account;
		});

		BUNGEE_COMMAND_HANDLER.registerAnnotationReplacer(Permission.class, (element, annotation) -> {
			CommandPermission commandPermissionAnnotation = Annotations.create(CommandPermission.class, "value",
					annotation.value());
			return Arrays.asList(commandPermissionAnnotation);
		});
	}

	private void registerDependencies() {
		BUNGEE_COMMAND_HANDLER.registerDependency(PluginConfig.class, PLUGIN.getConfig());
		BUNGEE_COMMAND_HANDLER.registerDependency(AccountStorage.class, PLUGIN.getAccountStorage());
		BUNGEE_COMMAND_HANDLER.registerDependency(ProxyPlugin.class, PLUGIN);
	}

	private void registerCommands() {
		BUNGEE_COMMAND_HANDLER.register(new AuthCommand());
		BUNGEE_COMMAND_HANDLER.register(new LoginCommand());
		BUNGEE_COMMAND_HANDLER.register(new RegisterCommand());
		BUNGEE_COMMAND_HANDLER.register(new ChangePasswordCommand());
		BUNGEE_COMMAND_HANDLER.register(new GoogleCodeCommand());
		BUNGEE_COMMAND_HANDLER.register(new GoogleCommand());
		BUNGEE_COMMAND_HANDLER.register(new GoogleUnlinkCommand());
		BUNGEE_COMMAND_HANDLER.register(new LogoutCommand());

		if (PLUGIN.getConfig().getVKSettings().isEnabled())
			BUNGEE_COMMAND_HANDLER.register(new VKLinkCommand());
	}

}
