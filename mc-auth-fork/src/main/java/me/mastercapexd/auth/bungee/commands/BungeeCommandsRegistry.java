package me.mastercapexd.auth.bungee.commands;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.bungee.commands.annotations.GoogleUse;
import me.mastercapexd.auth.bungee.commands.annotations.OtherPlayer;
import me.mastercapexd.auth.bungee.commands.annotations.Password;
import me.mastercapexd.auth.bungee.commands.annotations.RegisterAccount;
import me.mastercapexd.auth.bungee.commands.annotations.VkUse;
import me.mastercapexd.auth.bungee.commands.exception.CustomExceptionHandler;
import me.mastercapexd.auth.config.BungeePluginConfig;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.config.messages.Messages;
import me.mastercapexd.auth.config.messages.bungee.BungeeMessageContext;
import me.mastercapexd.auth.storage.AccountStorage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.core.BungeeHandler;
import revxrsal.commands.exception.SendMessageException;

public class BungeeCommandsRegistry {
	private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
	public static final BungeeHandler BUNGEE_COMMAND_HANDLER = new BungeeHandler(PLUGIN);

	public BungeeCommandsRegistry() {
		register();
	}

	private void register() {
		BUNGEE_COMMAND_HANDLER.setExceptionHandler(new CustomExceptionHandler(PLUGIN.getConfig().getBungeeMessages()));

		registerCommandContexts();
		registerDependencies();
		registerCommands();
	}

	private void registerCommandContexts() {
		BungeePluginConfig config = PLUGIN.getConfig();

		BUNGEE_COMMAND_HANDLER.registerParameterValidator(String.class, (value, parameter, actor) -> {
			if (!parameter.hasAnnotation(Password.class))
				return;
			if (value.length() < config.getPasswordMinLength())
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("password-too-short"));

			if (value.length() > config.getPasswordMaxLength())
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("password-too-long"));

			if (!config.getPasswordPattern().matcher(value).matches())
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("illegal-password-chars"));

		});

		Messages<BaseComponent[], BungeeMessageContext> authenticationStepUsageMessages = config.getBungeeMessages()
				.getSubMessages("authentication-step-usage");

		BUNGEE_COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
			if (!actor.as(BungeeCommandActor.class).isPlayer())
				return;
			ProxiedPlayer player = actor.as(BungeeCommandActor.class).asPlayer();
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
			throw new SendMessageException(authenticationStepUsageMessages.getStringMessage(account.getCurrentAuthenticationStep().getStepName()));
		});

		BUNGEE_COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(GoogleUse.class))
				return;
			if (!config.getGoogleAuthenticatorSettings().isEnabled())
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("google-disabled"));
		});

		BUNGEE_COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
			if (!command.hasAnnotation(VkUse.class))
				return;
			if (!config.getVKSettings().isEnabled())
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("vk-disabled"));
		});

		BUNGEE_COMMAND_HANDLER.registerValueResolver(CommandSender.class,
				context -> context.actor().as(BungeeCommandActor.class).getSender());

		BUNGEE_COMMAND_HANDLER.registerValueResolver(ProxiedPlayer.class, (context) -> {
			if (!context.parameter().hasAnnotation(OtherPlayer.class)) {
				ProxiedPlayer selfPlayer = context.actor().as(BungeeCommandActor.class).asPlayer();
				if (selfPlayer == null)
					throw new SendMessageException(config.getBungeeMessages().getStringMessage("players-only"));
				return selfPlayer;
			}
			String value = context.arguments().pop();
			ProxiedPlayer player = ProxyServer.getInstance().getPlayer(value);
			if (player == null)
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("player-offline"));
			return player;
		});

		BUNGEE_COMMAND_HANDLER.registerValueResolver(Account.class, (context) -> {
			ProxiedPlayer player = context.actor().as(BungeeCommandActor.class).asPlayer();
			if (player == null)
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("players-only"));
			String id = config.getActiveIdentifierType().getId(player);
			if (!Auth.hasAccount(id))
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("already-logged-in"));

			Account account = Auth.getAccount(id);
			if (!account.isRegistered() && !context.parameter().hasAnnotation(RegisterAccount.class))
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("account-not-found"));
			if (account.isRegistered() && context.parameter().hasAnnotation(RegisterAccount.class))
				throw new SendMessageException(config.getBungeeMessages().getStringMessage("account-exists"));
			return account;
		});
	}

	private void registerDependencies() {
		BUNGEE_COMMAND_HANDLER.registerDependency(PluginConfig.class, PLUGIN.getConfig());
		BUNGEE_COMMAND_HANDLER.registerDependency(AccountStorage.class, PLUGIN.getAccountStorage());
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
		BUNGEE_COMMAND_HANDLER.register(new VKLinkCommand());
	}

}
