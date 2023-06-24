package me.mastercapexd.auth.link.discord;

import org.jetbrains.annotations.NotNull;

import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.impl.UserNumberIdentificator;
import com.bivashy.messenger.common.message.Message;
import com.bivashy.messenger.discord.message.DiscordMessage;

import me.mastercapexd.auth.link.LinkCommandActorWrapperTemplate;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.Event;
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
        if (actor instanceof SlashCommandJDAActor)
            discordMessage.send(builder -> actor.as(SlashCommandJDAActor.class).getSlashEvent().reply(builder.build()).queue());
        if (actor instanceof MessageJDAActor)
            discordMessage.send(getChannel());
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
