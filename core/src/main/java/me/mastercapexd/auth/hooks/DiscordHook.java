package me.mastercapexd.auth.hooks;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.hook.PluginHook;

import net.dv8tion.jda.api.JDA;

public interface DiscordHook extends PluginHook {
    AuthPlugin PLUGIN = AuthPlugin.instance();

    @Override
    default boolean canHook() {
        return PLUGIN.getConfig().getDiscordSettings().isEnabled();
    }

    JDA getJDA();
}
