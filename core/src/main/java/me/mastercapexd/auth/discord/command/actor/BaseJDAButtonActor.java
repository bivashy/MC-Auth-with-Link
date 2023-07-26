package me.mastercapexd.auth.discord.command.actor;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.jda.core.actor.BaseActorJDA;

public class BaseJDAButtonActor extends BaseActorJDA {
    public BaseJDAButtonActor(ButtonInteractionEvent event, CommandHandler handler) {
        super(event, handler);
    }

    @Override
    public void reply(@NotNull String message) {
        getButtonEvent().reply(getCommandHandler().getMessagePrefix() + message).queue();
    }

    @Override
    public void error(@NotNull String message) {
        getButtonEvent().reply(getCommandHandler().getMessagePrefix() + message).queue();
    }

    @Override
    public @NotNull User getUser() {
        return getButtonEvent().getUser();
    }

    @Override
    public @NotNull MessageChannel getChannel() {
        return getButtonEvent().getChannel();
    }

    @Override
    public boolean isGuildEvent() {
        return getButtonEvent().isFromGuild();
    }

    @Override
    public @NotNull Guild getGuild() {
        return getButtonEvent().getGuild();
    }

    @Override
    public @NotNull Member getMember() {
        return getButtonEvent().getMember();
    }

    public ButtonInteractionEvent getButtonEvent() {
        return (ButtonInteractionEvent) getGenericEvent();
    }
}
