package me.mastercapexd.auth.discord.command;

import org.jetbrains.annotations.NotNull;

import com.bivashy.auth.api.config.link.command.LinkCommandPathSettings;

import me.mastercapexd.auth.config.discord.DiscordCommandArgumentSettings;
import me.mastercapexd.auth.config.discord.DiscordCommandSettings;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.jda.SlashCommandMapper;
import revxrsal.commands.jda.core.adapter.SlashCommandAdapter;

public class DiscordOptionMapper implements SlashCommandMapper {
    @Override
    public void mapSlashCommand(@NotNull SlashCommandAdapter slashCommand, @NotNull ExecutableCommand command) {
        if (!command.hasAnnotation(CommandKey.class))
            return;
        CommandKey commandKey = command.getAnnotation(CommandKey.class);
        LinkCommandPathSettings pathSettings = DiscordLinkType.getInstance().getSettings().getCommandPaths().getCommandPath(commandKey.value());
        if (!(pathSettings instanceof DiscordCommandSettings))
            return;
        DiscordCommandSettings settings = (DiscordCommandSettings) pathSettings;
        for (OptionData option : slashCommand.getOptions()) {
            DiscordCommandArgumentSettings argumentSettings = settings.getArguments().get(option.getName());
            if (argumentSettings == null)
                continue;
            option.setName(argumentSettings.getName());
            option.setDescription(argumentSettings.getDescription());
        }
    }
}
