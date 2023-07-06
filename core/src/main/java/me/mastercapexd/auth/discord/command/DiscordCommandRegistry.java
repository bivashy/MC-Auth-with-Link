package me.mastercapexd.auth.discord.command;

import java.util.Collections;

import com.bivashy.auth.api.AuthPlugin;

import me.mastercapexd.auth.hooks.DiscordHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.discord.DiscordCommandActorWrapper;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.messenger.commands.annotation.RenameTo;
import me.mastercapexd.auth.shared.commands.DiscordLinkCommand;
import me.mastercapexd.auth.shared.commands.MessengerLinkCommandTemplate;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import revxrsal.commands.annotation.dynamic.Annotations;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.jda.JDAActor;
import revxrsal.commands.jda.JDACommandHandler;
import revxrsal.commands.jda.annotation.OptionData;

public class DiscordCommandRegistry extends MessengerCommandRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final JDACommandHandler COMMAND_HANDLER = JDACommandHandler.create(PLUGIN.getHook(DiscordHook.class).getJDA(), "");

    public DiscordCommandRegistry() {
        super(COMMAND_HANDLER, DiscordLinkType.getInstance());
        COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class, context -> new DiscordCommandActorWrapper(context.actor()));
        COMMAND_HANDLER.registerAnnotationReplacer(RenameTo.class, (element, parameter) -> Collections.singletonList(
                Annotations.create(OptionData.class, "value", OptionType.valueOf(parameter.type()), "name", parameter.value())));

        registerCommands();
    }

    @Override
    protected MessengerLinkCommandTemplate createLinkCommand() {
        return new DiscordLinkCommand(DiscordLinkType.getInstance().getLinkMessages());
    }
}
