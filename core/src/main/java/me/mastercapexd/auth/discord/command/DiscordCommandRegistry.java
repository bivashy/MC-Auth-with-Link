package me.mastercapexd.auth.discord.command;

import com.bivashy.auth.api.AuthPlugin;

import me.mastercapexd.auth.discord.listener.JDACommandListener;
import me.mastercapexd.auth.hooks.DiscordHook;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.discord.DiscordCommandActorWrapper;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.messenger.commands.MessengerCommandRegistry;
import me.mastercapexd.auth.shared.commands.DiscordLinkCommand;
import me.mastercapexd.auth.shared.commands.MessengerLinkCommandTemplate;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.jda.JDAActor;
import revxrsal.commands.jda.JDACommandHandler;

public class DiscordCommandRegistry extends MessengerCommandRegistry {

    private static final AuthPlugin PLUGIN = AuthPlugin.instance();
    private static final DiscordHook DISCORD_HOOK = PLUGIN.getHook(DiscordHook.class);
    private static final JDACommandHandler COMMAND_HANDLER = JDACommandHandler.create(DISCORD_HOOK.getJDA(), "");

    public DiscordCommandRegistry() {
        super(COMMAND_HANDLER, DiscordLinkType.getInstance());
        COMMAND_HANDLER.disableStackTraceSanitizing();
        COMMAND_HANDLER.registerContextResolver(LinkCommandActorWrapper.class, context -> new DiscordCommandActorWrapper(context.actor()));

        COMMAND_HANDLER.registerCondition((actor, command, arguments) -> {
            DiscordCommandActorWrapper actorWrapper = actor.as(DiscordCommandActorWrapper.class);
            if (actorWrapper.isGuildEvent() && !DiscordLinkType.getInstance().getSettings().isAllowedChannel(actorWrapper.getChannel().getId()))
                throw new CommandErrorException(DiscordLinkType.getInstance().getLinkMessages().getMessage("forbidden-channel"));
        });

        DiscordCommandParameterMapper parameterMapper = new DiscordCommandParameterMapper();
        COMMAND_HANDLER.registerSlashCommandMapper(parameterMapper);
        COMMAND_HANDLER.setParameterNamingStrategy(parameterMapper);
        replaceNativeListener();

        registerCommands();

        COMMAND_HANDLER.registerSlashCommands();
    }

    private void replaceNativeListener() {
        DISCORD_HOOK.getJDA()
                .getRegisteredListeners()
                .stream()
                .filter(listener -> listener.getClass().getName().equals("revxrsal.commands.jda.core.JDACommandListener"))
                .forEach(listener -> DISCORD_HOOK.getJDA().removeEventListener(listener));
        DISCORD_HOOK.getJDA().addEventListener(new JDACommandListener(COMMAND_HANDLER, this::wrapActor));
    }

    @Override
    protected LinkCommandActorWrapper wrapActor(CommandActor actor) {
        return new DiscordCommandActorWrapper(actor.as(JDAActor.class));
    }

    @Override
    protected MessengerLinkCommandTemplate createLinkCommand() {
        return new DiscordLinkCommand(DiscordLinkType.getInstance().getLinkMessages());
    }

}
