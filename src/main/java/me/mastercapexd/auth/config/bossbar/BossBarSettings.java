package me.mastercapexd.auth.config.bossbar;

import com.ubivashka.configuration.annotations.ConfigField;
import com.ubivashka.configuration.holders.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.ConfigurationHolder;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar.Color;
import me.mastercapexd.auth.proxy.api.bossbar.ProxyBossbar.Style;
import me.mastercapexd.auth.proxy.message.ProxyComponent;

public class BossBarSettings implements ConfigurationHolder {
    @ConfigField("use")
    private Boolean enabled = false;
    @ConfigField("bar-color")
    private ProxyBossbar.Color barColor = Color.BLUE;
    @ConfigField("bar-style")
    private ProxyBossbar.Style barStyle = Style.SOLID;
    @ConfigField("bar-text")
    private ProxyComponent barText = ProxyComponent.fromPlain("[Authentication]");

    public BossBarSettings(ConfigurationSectionHolder sectionHolder) {
        ProxyPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
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
