package me.mastercapexd.auth.proxy.commands.exception;

import me.mastercapexd.auth.config.messages.AbstractMessages;
import me.mastercapexd.auth.config.messages.Messages;
import revxrsal.commands.bungee.exception.BungeeExceptionAdapter;
import revxrsal.commands.bungee.exception.InvalidPlayerException;
import revxrsal.commands.bungee.exception.SenderNotConsoleException;
import revxrsal.commands.bungee.exception.SenderNotPlayerException;
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

public class CustomExceptionHandler extends BungeeExceptionAdapter {
	private final Messages<?> messages;

	public CustomExceptionHandler(AbstractMessages<?> messagees) {
		this.messages = messagees;
	}

	@Override
	public void senderNotPlayer(CommandActor actor, SenderNotPlayerException exception) {
	}

	@Override
	public void senderNotConsole(CommandActor actor, SenderNotConsoleException exception) {
	}

	@Override
	public void invalidPlayer(CommandActor actor, InvalidPlayerException exception) {
		actor.reply(messages.getStringMessage("player-offline").replaceAll("%player%", exception.getInput()));
	}

	@Override
	public void missingArgument(CommandActor actor, MissingArgumentException exception) {
		actor.reply(messages.getStringMessage("unresolved-argument").replaceAll("%argument_name%",
				exception.getParameter().getName()));
	}

	@Override
	public void invalidEnumValue(CommandActor actor, EnumNotFoundException exception) {
	}

	@Override
	public void invalidNumber(CommandActor actor, InvalidNumberException exception) {
		actor.reply(messages.getStringMessage("expected-number").replaceAll("%value%", exception.getInput()));
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
		actor.reply(messages.getStringMessage("no-permission"));
	}

	@Override
	public void argumentParse(CommandActor actor, ArgumentParseException exception) {
		actor.reply(messages.getStringMessage("command-invocation"));
	}

	@Override
	public void commandInvocation(CommandActor actor, CommandInvocationException exception) {
		actor.reply(messages.getStringMessage("command-invocation"));
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
		actor.reply(messages.getStringMessage("command-invocation"));
		throwable.printStackTrace();
	}

}
