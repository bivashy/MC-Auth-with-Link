package me.mastercapexd.auth.config.bossbar;

import com.ubivashka.configuration.ConfigurationHolder;
import com.ubivashka.configuration.annotation.ConfigField;
import com.ubivashka.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar.Color;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar.Style;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class BossBarSettings implements ConfigurationHolder {
    @ConfigField("use")
    private boolean enabled = false;
    @ConfigField("bar-color")
    private ProxyBossbar.Color barColor = Color.BLUE;
    @ConfigField("bar-style")
    private ProxyBossbar.Style barStyle = Style.SOLID;
    @ConfigField("bar-text")
    private ProxyComponent barText = ProxyComponent.fromPlain("[Authentication]");

    public BossBarSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BossBarSettings() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ProxyBossbar createBossBar() {
        if (!enabled)
            return null;
        return ProxyPlugin.instance().getCore().createBossbar(barText).color(barColor).style(barStyle).update();
    }
}
