package me.mastercapexd.auth.discord.command;

import com.bivashy.auth.api.AuthPlugin;

import me.mastercapexd.auth.hooks.DiscordHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.discord.DiscordCommandActorWrapper;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.shared.commands.DiscordLinkCommand;
import me.mastercapexd.auth.shared.commands.MessengerLinkCommandTemplate;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.jda.JDACommandHandler;

public class DiscordCommandRegistry extends MessengerCommandRegistry {
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final CommandHandler COMMAND_HANDLER = JDACommandHandler.create(PLUGIN.getHook(DiscordHook.class).getJDA(), "");

    public DiscordCommandRegistry() {
        super(COMMAND_HANDLER, DiscordLinkType.getInstance());
        COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class, context -> new DiscordCommandActorWrapper(context.actor()));

        registerCommands();
    }

    @Override
    protected MessengerLinkCommandTemplate createLinkCommand() {
        return new DiscordLinkCommand(DiscordLinkType.getInstance().getLinkMessages());
    }
}
