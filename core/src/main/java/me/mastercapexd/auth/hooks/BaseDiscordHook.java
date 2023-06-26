package me.mastercapexd.auth.hooks;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BaseDiscordHook implements DiscordHook {
    private JDA jda = JDABuilder.createDefault(PLUGIN.getConfig().getDiscordSettings().getBotToken()).build();

    @Override
    public JDA getJDA() {
        return jda;
    }
}
