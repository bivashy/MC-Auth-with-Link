package me.mastercapexd.auth.link.discord;

import org.jetbrains.annotations.NotNull;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.messenger.common.message.Message;
import com.bivashy.messenger.discord.message.DiscordMessage;

import me.mastercapexd.auth.discord.command.actor.BaseJDAButtonActor;
import me.mastercapexd.auth.link.LinkCommandActorWrapperTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.jda.JDAActor;
import revxrsal.commands.jda.actor.MessageJDAActor;
import revxrsal.commands.jda.actor.SlashCommandJDAActor;
import revxrsal.commands.jda.exception.GuildOnlyCommandException;
import revxrsal.commands.jda.exception.PrivateMessageOnlyCommandException;

public class DiscordCommandActorWrapper extends LinkCommandActorWrapperTemplate<JDAActor> implements JDAActor {
    public DiscordCommandActorWrapper(JDAActor actor) {
        super(actor);
    }

    @Override
    public void send(Message message) {
        if (!message.safeAs(DiscordMessage.class).isPresent())
            return;
        DiscordMessage discordMessage = message.as(DiscordMessage.class);
        if (actor instanceof SlashCommandJDAActor) {
            SlashCommandJDAActor slashCommandJDAActor = actor.as(SlashCommandJDAActor.class);
            SlashCommandInteractionEvent event = slashCommandJDAActor.getSlashEvent();
            sendInteractionMessage(discordMessage, event, event.getHook());
        }
        if (actor instanceof BaseJDAButtonActor) {
            BaseJDAButtonActor buttonActor = actor.as(BaseJDAButtonActor.class);
            ButtonInteractionEvent event = buttonActor.getButtonEvent();
            sendInteractionMessage(discordMessage, event, event.getHook());
        }
        if (actor instanceof MessageJDAActor)
            discordMessage.send(getChannel());
    }

    @Override
    public void reply(String message) {
        if (actor instanceof SlashCommandJDAActor) {
            SlashCommandJDAActor slashCommandJDAActor = actor.as(SlashCommandJDAActor.class);
            SlashCommandInteractionEvent event = slashCommandJDAActor.getSlashEvent();
            sendInteractionMessage(message, event, event.getHook());
            return;
        }
        if (actor instanceof BaseJDAButtonActor) {
            BaseJDAButtonActor buttonActor = actor.as(BaseJDAButtonActor.class);
            ButtonInteractionEvent event = buttonActor.getButtonEvent();
            sendInteractionMessage(message, event, event.getHook());
            return;
        }
        actor.reply(message);
    }

    @Override
    public void error(String message) {
        reply(message);
    }

    private void sendInteractionMessage(String message, IReplyCallback replyCallback, InteractionHook interactionHook) {
        interactionHook.setEphemeral(true);
        if (interactionHook.getInteraction().isAcknowledged()) {
            interactionHook.editOriginal(message).queue();
            return;
        }
        replyCallback.reply(message).queue();
    }

    private void sendInteractionMessage(DiscordMessage message, IReplyCallback replyCallback, InteractionHook interactionHook) {
        interactionHook.setEphemeral(true);
        message.send(builder -> {
            if (interactionHook.getInteraction().isAcknowledged()) {
                interactionHook.sendMessage(builder.build()).queue();
                return;
            }
            replyCallback.reply(builder.build()).queue();
        });
    }

    @Override
    public LinkUserIdentificator userId() {
        return new UserNumberIdentificator(getIdLong());
    }

    @Override
    public long getIdLong() {
        return actor.getIdLong();
    }

    @Override
    public @NotNull String getId() {
        return actor.getId();
    }

    @Override
    public @NotNull User getUser() {
        return actor.getUser();
    }

    @Override
    public @NotNull Event getGenericEvent() {
        return actor.getGenericEvent();
    }

    @Override
    public @NotNull MessageChannel getChannel() {
        return actor.getChannel();
    }

    @Override
    public boolean isGuildEvent() {
        return actor.isGuildEvent();
    }

    @Override
    public @NotNull Guild getGuild() {
        return actor.getGuild();
    }

    @Override
    public @NotNull Member getMember() {
        return actor.getMember();
    }

    @Override
    public JDAActor checkInGuild(ExecutableCommand executableCommand) throws GuildOnlyCommandException {
        return actor.checkInGuild(executableCommand);
    }

    @Override
    public JDAActor checkNotInGuild(ExecutableCommand executableCommand) throws PrivateMessageOnlyCommandException {
        return actor.checkNotInGuild(executableCommand);
    }
}
