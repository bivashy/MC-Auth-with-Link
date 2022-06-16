package me.mastercapexd.auth.config.bossbar;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;
import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import net.md_5.bungee.api.ChatColor;

public class BossBarSettings implements ConfigurationHolder {
    @ConfigField("use")
    private Boolean enabled = false;
    @ConfigField("bar-color")
    private ProxyBossbar.Color barColor = null;
    @ConfigField("bar-style")
    private ProxyBossbar.Style barStyle = null;
    @ConfigField("bar-text")
    private String barText = null; // ProxyMessage implementation refactor

    public BossBarSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ProxyBossbar createBossBar() {
        if (!enabled)
            return null;
        return ProxyPlugin.instance().getCore().createBossbar(ChatColor.translateAlternateColorCodes('&', barText));
    }
}
