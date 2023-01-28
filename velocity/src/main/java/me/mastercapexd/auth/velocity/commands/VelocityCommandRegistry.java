package me.mastercapexd.auth.velocity.commands;

import java.util.Collections;
import java.util.Optional;

import com.velocitypowered.api.proxy.Player;

import me.mastercapexd.auth.Auth;
import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.proxy.commands.ProxyCommandActor;
import me.mastercapexd.auth.proxy.commands.ProxyCommandsRegistry;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationAccount;
import me.mastercapexd.auth.proxy.commands.annotations.AuthenticationStepCommand;
import me.mastercapexd.auth.proxy.commands.annotations.Permission;
import me.mastercapexd.auth.proxy.commands.exception.SendComponentException;
import me.mastercapexd.auth.proxy.commands.parameters.ArgumentProxyPlayer;
import me.mastercapexd.auth.proxy.player.ProxyPlayer;
import me.mastercapexd.auth.velocity.AuthPlugin;
import me.mastercapexd.auth.velocity.commands.exception.VelocityExceptionHandler;
import me.mastercapexd.auth.velocity.player.VelocityProxyPlayer;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.exception.SendMessageException;
import revxrsal.commands.velocity.VelocityCommandActor;
import revxrsal.commands.velocity.annotation.CommandPermission;
import revxrsal.commands.velocity.core.VelocityHandler;

public class VelocityCommandRegistry extends ProxyCommandsRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.getInstance();
    public static final CommandHandler VELOCITY_COMMAND_HANDLER =
            new VelocityHandler(PLUGIN, PLUGIN.getProxyServer()).setExceptionHandler(new VelocityExceptionHandler(PLUGIN.getConfig().getProxyMessages()))
                    .disableStackTraceSanitizing();

    public VelocityCommandRegistry() {
        super(VELOCITY_COMMAND_HANDLER);
        PluginConfig config = PLUGIN.getConfig();
        commandHandler.registerContextResolver(ProxyPlayer.class, (context) -> {
            Player player = context.actor().as(VelocityCommandActor.class).getAsPlayer();
            if (player == null)
                throw new SendMessageException(config.getProxyMessages().getStringMessage("players-only"));
            return new VelocityProxyPlayer(player);
        });
        commandHandler.registerContextResolver(ProxyCommandActor.class,
                (context) -> new VelocityProxyCommandActor(context.actor().as(VelocityCommandActor.class)));
        commandHandler.registerValueResolver(ArgumentProxyPlayer.class, (context) -> {
            String value = context.pop();
            Optional<ProxyPlayer> player = PLUGIN.getCore().getPlayer(value);
            if (!player.isPresent())
                throw new SendMessageException(config.getProxyMessages().getStringMessage("player-offline"));
            return new ArgumentProxyPlayer(player.get());
        });
        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!actor.as(VelocityCommandActor.class).isPlayer())
                return;
            ProxyPlayer player = new VelocityProxyPlayer(actor.as(VelocityCommandActor.class).getAsPlayer());
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
            ProxyPlayer player = new VelocityProxyPlayer(context.actor().as(VelocityCommandActor.class).getAsPlayer());
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
                (actor, componentException) -> new VelocityProxyCommandActor(actor.as(VelocityCommandActor.class)).reply(componentException.getComponent()));
        registerCommands();
    }
}
