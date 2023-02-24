package me.mastercapexd.auth.bungee.commands;

import java.util.Collections;

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

public class BungeeCommandsRegistry extends ProxyCommandsRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
    public static final CommandHandler BUNGEE_COMMAND_HANDLER = new BungeeHandler(PLUGIN).setExceptionHandler(
            new BungeeExceptionHandler(PLUGIN.getConfig().getProxyMessages())).disableStackTraceSanitizing();

    public BungeeCommandsRegistry() {
        super(BUNGEE_COMMAND_HANDLER);
        PluginConfig config = PLUGIN.getConfig();
        commandHandler.registerContextResolver(ProxyPlayer.class, (context) -> {
            ProxiedPlayer selfPlayer = context.actor().as(BungeeCommandActor.class).asPlayer();
            if (selfPlayer == null)
                throw new SendComponentException(config.getProxyMessages().getMessage("players-only"));
            return new BungeeProxyPlayer(selfPlayer);
        });
        commandHandler.registerContextResolver(ProxyCommandActor.class, (context) -> new BungeeProxyCommandActor(context.actor().as(BungeeCommandActor.class)));
        commandHandler.registerValueResolver(ArgumentProxyPlayer.class, (context) -> {
            String value = context.pop();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(value);
            if (player == null) {
                throw new SendComponentException(config.getProxyMessages().getMessage("player-offline"));
            }
            return new ArgumentProxyPlayer(new BungeeProxyPlayer(player));
        });
        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!actor.as(BungeeCommandActor.class).isPlayer())
                return;
            ProxyPlayer player = new BungeeProxyPlayer(actor.as(BungeeCommandActor.class).asPlayer());
            if (!plugin.getAuthenticatingAccountBucket().isAuthorizing(player))
                return;
            if (!command.hasAnnotation(AuthenticationStepCommand.class))
                return;
            Account account = plugin.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(player);
            if (account.getCurrentAuthenticationStep() == null)
                return;
            String stepName = command.getAnnotation(AuthenticationStepCommand.class).stepName();
            if (account.getCurrentAuthenticationStep().getStepName().equals(stepName))
                return;
            throw new SendComponentException(config.getProxyMessages()
                    .getSubMessages("authentication-step-usage")
                    .getMessage(account.getCurrentAuthenticationStep().getStepName()));
        });
        commandHandler.registerContextResolver(Account.class, (context) -> {
            ProxyPlayer player = new BungeeProxyPlayer(context.actor().as(BungeeCommandActor.class).asPlayer());
            if (player.getRealPlayer() == null)
                throw new SendComponentException(config.getProxyMessages().getMessage("players-only"));
            if (!plugin.getAuthenticatingAccountBucket().isAuthorizing(player))
                throw new SendComponentException(config.getProxyMessages().getMessage("already-logged-in"));

            Account account = plugin.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(player);
            if (!account.isRegistered() && !context.parameter().hasAnnotation(AuthenticationAccount.class))
                throw new SendComponentException(config.getProxyMessages().getMessage("account-not-found"));
            if (account.isRegistered() && context.parameter().hasAnnotation(AuthenticationAccount.class))
                throw new SendComponentException(config.getProxyMessages().getMessage("account-exists"));
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
