package me.mastercapexd.auth.discord.command;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.bivashy.auth.api.config.link.command.LinkCommandPathSettings;

import me.mastercapexd.auth.config.discord.DiscordCommandArgumentSettings;
import me.mastercapexd.auth.config.discord.DiscordCommandSettings;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.messenger.commands.annotation.CommandKey;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import revxrsal.commands.command.CommandParameter;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.jda.SlashCommandMapper;
import revxrsal.commands.jda.core.adapter.SlashCommandAdapter;
import revxrsal.commands.process.ParameterNamingStrategy;

public class DiscordCommandParameterMapper implements SlashCommandMapper, ParameterNamingStrategy {
    @Override
    public void mapSlashCommand(@NotNull SlashCommandAdapter slashCommand, @NotNull ExecutableCommand command) {
        Optional<DiscordCommandSettings> settingsOptional = getCommandSettings(command);
        if (!settingsOptional.isPresent())
            return;
        for (OptionData option : slashCommand.getOptions()) {
            DiscordCommandArgumentSettings argumentSettings = settingsOptional.get().getArguments().get(option.getName());
            if (argumentSettings == null)
                continue;
            option.setDescription(argumentSettings.getDescription());
        }
    }

    @Override
    public @NotNull String getName(@NotNull CommandParameter parameter) {
        Optional<DiscordCommandSettings> settingsOptional = getCommandSettings(parameter.getDeclaringCommand());
        if (!settingsOptional.isPresent())
            return parameter.getName();
        DiscordCommandArgumentSettings settings = settingsOptional.get().getArguments().get(parameter.getName());
        if (settings == null)
            return parameter.getName();
        return settings.getName();
    }

    private Optional<DiscordCommandSettings> getCommandSettings(ExecutableCommand command) {
        if (!command.hasAnnotation(CommandKey.class))
            return Optional.empty();
        CommandKey commandKey = command.getAnnotation(CommandKey.class);
        LinkCommandPathSettings pathSettings = DiscordLinkType.getInstance().getSettings().getCommandPaths().getCommandPath(commandKey.value());
        if (!(pathSettings instanceof DiscordCommandSettings))
            return Optional.empty();
        return Optional.of((DiscordCommandSettings) pathSettings);
    }
}
