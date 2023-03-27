package me.mastercapexd.auth.velocity.commands.exception;

import org.jetbrains.annotations.NotNull;

import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.server.message.ServerComponent;

import me.mastercapexd.auth.velocity.commands.VelocityServerCommandActor;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.ArgumentParseException;
import revxrsal.commands.exception.CommandInvocationException;
import revxrsal.commands.exception.CooldownException;
import revxrsal.commands.exception.EnumNotFoundException;
import revxrsal.commands.exception.InvalidBooleanException;
import revxrsal.commands.exception.InvalidCommandException;
import revxrsal.commands.exception.InvalidHelpPageException;
import revxrsal.commands.exception.InvalidNumberException;
import revxrsal.commands.exception.InvalidSubcommandException;
import revxrsal.commands.exception.InvalidURLException;
import revxrsal.commands.exception.InvalidUUIDException;
import revxrsal.commands.exception.MissingArgumentException;
import revxrsal.commands.exception.NoPermissionException;
import revxrsal.commands.exception.NoSubcommandSpecifiedException;
import revxrsal.commands.exception.NumberNotInRangeException;
import revxrsal.commands.exception.SendableException;
import revxrsal.commands.exception.TooManyArgumentsException;
import revxrsal.commands.velocity.VelocityCommandActor;
import revxrsal.commands.velocity.exception.VelocityExceptionAdapter;

public class VelocityExceptionHandler extends VelocityExceptionAdapter {
    private final Messages<ServerComponent> messages;

    public VelocityExceptionHandler(Messages<ServerComponent> messages) {
        this.messages = messages;
    }

    @Override
    public void senderNotPlayer(@NotNull CommandActor actor, revxrsal.commands.velocity.exception.@NotNull SenderNotPlayerException exception) {
    }

    @Override
    public void senderNotConsole(@NotNull CommandActor actor, revxrsal.commands.velocity.exception.@NotNull SenderNotConsoleException exception) {
    }

    @Override
    public void invalidPlayer(@NotNull CommandActor actor, revxrsal.commands.velocity.exception.@NotNull InvalidPlayerException exception) {
        sendComponent(actor, messages.getMessage("player-offline", MessageContext.of("%player%", exception.getInput())));
    }

    @Override
    public void missingArgument(CommandActor actor, MissingArgumentException exception) {
        sendComponent(actor, messages.getMessage("unresolved-argument", MessageContext.of("%argument_name%", exception.getParameter().getName())));
    }

    @Override
    public void invalidEnumValue(CommandActor actor, EnumNotFoundException exception) {
    }

    @Override
    public void invalidNumber(CommandActor actor, InvalidNumberException exception) {
        sendComponent(actor, messages.getMessage("unresolved-number", MessageContext.of("%input%", exception.getInput())));
    }

    @Override
    public void invalidUUID(CommandActor actor, InvalidUUIDException exception) {
    }

    @Override
    public void invalidURL(CommandActor actor, InvalidURLException exception) {
    }

    @Override
    public void invalidBoolean(CommandActor actor, InvalidBooleanException exception) {
    }

    @Override
    public void noPermission(CommandActor actor, NoPermissionException exception) {
        sendComponent(actor, messages.getMessage("no-permission"));
    }

    @Override
    public void argumentParse(CommandActor actor, ArgumentParseException exception) {
        sendComponent(actor, messages.getMessage("command-invocation"));
    }

    @Override
    public void commandInvocation(CommandActor actor, CommandInvocationException exception) {
        sendComponent(actor, messages.getMessage("command-invocation"));
        exception.getCause().printStackTrace();
    }

    @Override
    public void tooManyArguments(CommandActor actor, TooManyArgumentsException exception) {
    }

    @Override
    public void invalidCommand(CommandActor actor, InvalidCommandException exception) {
    }

    @Override
    public void invalidSubcommand(CommandActor actor, InvalidSubcommandException exception) {
    }

    @Override
    public void noSubcommandSpecified(CommandActor actor, NoSubcommandSpecifiedException exception) {
    }

    @Override
    public void cooldown(CommandActor actor, CooldownException exception) {
    }

    @Override
    public void invalidHelpPage(CommandActor actor, InvalidHelpPageException exception) {
    }

    @Override
    public void sendableException(CommandActor actor, SendableException exception) {
        exception.sendTo(actor);
    }

    @Override
    public void numberNotInRange(CommandActor actor, NumberNotInRangeException exception) {
    }

    @Override
    public void onUnhandledException(CommandActor actor, Throwable throwable) {
        sendComponent(actor, messages.getMessage("command-invocation"));
        throwable.printStackTrace();
    }

    private void sendComponent(CommandActor actor, ServerComponent component) {
        new VelocityServerCommandActor(actor.as(VelocityCommandActor.class)).reply(component);
    }
}
