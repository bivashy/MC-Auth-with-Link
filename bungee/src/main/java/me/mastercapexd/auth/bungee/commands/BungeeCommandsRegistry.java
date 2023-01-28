package me.mastercapexd.auth.bungee.commands;

import java.util.Collections;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.bungee.commands.exception.BungeeExceptionHandler;
import me.mastercapexd.auth.bungee.player.BungeeProxyPlayer;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.commands.ProxyCommandActor;
import me.mastercapexd.auth.proxy.commands.ProxyCommandsRegistry;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.proxy.commands.annotations.Permission;
import me.mastercapexd.auth.proxy.commands.exception.SendComponentException;
import me.mastercapexd.auth.proxy.commands.parameters.ArgumentProxyPlayer;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;
import revxrsal.commands.bungee.core.BungeeHandler;
import revxrsal.commands.exception.SendMessageException;

public class BungeeCommandsRegistry extends ProxyCommandsRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
    public static final CommandHandler BUNGEE_COMMAND_HANDLER =
            new BungeeHandler(PLUGIN).setExceptionHandler(new BungeeExceptionHandler(PLUGIN.getConfig().getProxyMessages())).disableStackTraceSanitizing();

    public BungeeCommandsRegistry() {
        super(BUNGEE_COMMAND_HANDLER);
        PluginConfig config = PLUGIN.getConfig();
        commandHandler.registerContextResolver(ProxyPlayer.class, (context) -> {
            ProxiedPlayer selfPlayer = context.actor().as(BungeeCommandActor.class).asPlayer();
            if (selfPlayer == null)
                throw new SendMessageException(config.getProxyMessages().getStringMessage("players-only"));
            return new BungeeProxyPlayer(selfPlayer);
        });
        commandHandler.registerContextResolver(ProxyCommandActor.class, (context) -> new BungeeProxyCommandActor(context.actor().as(BungeeCommandActor.class)));
        commandHandler.registerValueResolver(ArgumentProxyPlayer.class, (context) -> {
            String value = context.pop();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(value);
            if (player == null)
                throw new SendMessageException(config.getProxyMessages().getStringMessage("player-offline"));
            return new ArgumentProxyPlayer(new BungeeProxyPlayer(player));
        });
        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!actor.as(BungeeCommandActor.class).isPlayer())
                return;
            ProxyPlayer player = new BungeeProxyPlayer(actor.as(BungeeCommandActor.class).asPlayer());
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
        commandHandler.registerContextResolver(Account.class, (context) -> {
            ProxyPlayer player = new BungeeProxyPlayer(context.actor().as(BungeeCommandActor.class).asPlayer());
            if (player.getRealPlayer() == null)
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

        commandHandler.registerAnnotationReplacer(Permission.class, (element, annotation) -> {
            CommandPermission commandPermissionAnnotation = Annotations.create(CommandPermission.class, "value", annotation.value());
            return Collections.singletonList(commandPermissionAnnotation);
        });

        commandHandler.registerExceptionHandler(SendComponentException.class,
                (actor, componentException) -> new BungeeProxyCommandActor(actor.as(BungeeCommandActor.class)).reply(componentException.getComponent()));
        registerCommands();
    }
}
