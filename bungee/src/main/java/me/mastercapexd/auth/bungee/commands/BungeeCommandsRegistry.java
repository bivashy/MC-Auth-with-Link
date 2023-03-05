package me.mastercapexd.auth.bungee.commands;

import java.util.Collections;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.server.command.ServerCommandActor;
import com.bivashy.auth.api.server.player.ServerPlayer;

import me.mastercapexd.auth.bungee.BungeeAuthPluginBootstrap;
import me.mastercapexd.auth.bungee.commands.exception.BungeeExceptionHandler;
import me.mastercapexd.auth.bungee.player.BungeeServerPlayer;
import me.mastercapexd.auth.server.commands.ProxyCommandsRegistry;
import me.mastercapexd.auth.server.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.server.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.server.commands.annotations.Permission;
import me.mastercapexd.auth.server.commands.exception.SendComponentException;
import me.mastercapexd.auth.server.commands.parameters.ArgumentProxyPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.bungee.BungeeCommandActor;
import revxrsal.commands.bungee.annotation.CommandPermission;
import revxrsal.commands.bungee.core.BungeeHandler;

public class BungeeCommandsRegistry extends ProxyCommandsRegistry {
    public BungeeCommandsRegistry(BungeeAuthPluginBootstrap pluginBootstrap, AuthPlugin authPlugin) {
        super(new BungeeHandler(pluginBootstrap).setExceptionHandler(
                new BungeeExceptionHandler(authPlugin.getConfig().getServerMessages())).disableStackTraceSanitizing());
        PluginConfig config = authPlugin.getConfig();
        commandHandler.registerContextResolver(ServerPlayer.class, (context) -> {
            ProxiedPlayer selfPlayer = context.actor().as(BungeeCommandActor.class).asPlayer();
            if (selfPlayer == null)
                throw new SendComponentException(config.getServerMessages().getMessage("players-only"));
            return new BungeeServerPlayer(selfPlayer);
        });
        commandHandler.registerContextResolver(ServerCommandActor.class,
                (context) -> new BungeeProxyCommandActor(context.actor().as(BungeeCommandActor.class)));
        commandHandler.registerValueResolver(ArgumentProxyPlayer.class, (context) -> {
            String value = context.pop();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(value);
            if (player == null) {
                throw new SendComponentException(config.getServerMessages().getMessage("player-offline"));
            }
            return new ArgumentProxyPlayer(new BungeeServerPlayer(player));
        });
        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!actor.as(BungeeCommandActor.class).isPlayer())
                return;
            ServerPlayer player = new BungeeServerPlayer(actor.as(BungeeCommandActor.class).asPlayer());
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
            throw new SendComponentException(config.getServerMessages()
                    .getSubMessages("authentication-step-usage")
                    .getMessage(account.getCurrentAuthenticationStep().getStepName()));
        });
        commandHandler.registerContextResolver(Account.class, (context) -> {
            ServerPlayer player = new BungeeServerPlayer(context.actor().as(BungeeCommandActor.class).asPlayer());
            if (player.getRealPlayer() == null)
                throw new SendComponentException(config.getServerMessages().getMessage("players-only"));
            if (!plugin.getAuthenticatingAccountBucket().isAuthorizing(player))
                throw new SendComponentException(config.getServerMessages().getMessage("already-logged-in"));

            Account account = plugin.getAuthenticatingAccountBucket().getAuthorizingAccountNullable(player);
            if (!account.isRegistered() && !context.parameter().hasAnnotation(AuthenticationAccount.class))
                throw new SendComponentException(config.getServerMessages().getMessage("account-not-found"));
            if (account.isRegistered() && context.parameter().hasAnnotation(AuthenticationAccount.class))
                throw new SendComponentException(config.getServerMessages().getMessage("account-exists"));
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
