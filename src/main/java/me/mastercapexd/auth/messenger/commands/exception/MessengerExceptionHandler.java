package me.mastercapexd.auth.messenger.commands.exception;

import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.*;

public class MessengerExceptionHandler extends DefaultExceptionHandler {
    private final LinkType linkType;

    public MessengerExceptionHandler(LinkType linkType) {
        this.linkType = linkType;
    }

    @Override
    public void missingArgument(CommandActor actor, MissingArgumentException exception) {
        if (exception.getCommand().hasAnnotation(ConfigurationArgumentError.class)) {
            actor.reply(linkType.getLinkMessages().getStringMessage(exception.getCommand().getAnnotation(ConfigurationArgumentError.class).value()));
            return;
        }
        actor.reply(linkType.getLinkMessages().getStringMessage("unresolved-argument").replaceAll("%argument_name%", exception.getParameter().getName()));
    }

    @Override
    public void invalidEnumValue(CommandActor actor, EnumNotFoundException exception) {
    }

    @Override
    public void invalidNumber(CommandActor actor, InvalidNumberException exception) {
        actor.reply(linkType.getLinkMessages().getStringMessage("unresolved-number").replaceAll("%input%", exception.getInput()));
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
    }

    @Override
    public void argumentParse(CommandActor actor, ArgumentParseException exception) {
        actor.reply(linkType.getLinkMessages().getStringMessage("command-invocation"));
    }

    @Override
    public void commandInvocation(CommandActor actor, CommandInvocationException exception) {
        actor.reply(linkType.getLinkMessages().getStringMessage("command-invocation"));
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
        throwable.printStackTrace();
        actor.reply(linkType.getLinkMessages().getStringMessage("command-invocation"));
    }
}
