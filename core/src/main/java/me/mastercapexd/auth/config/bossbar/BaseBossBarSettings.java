package me.mastercapexd.auth.config.bossbar;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.bossbar.BossBarSettings;
import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.bossbar.ServerBossbar.Color;
import com.bivashy.auth.api.server.bossbar.ServerBossbar.Style;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.configuration.ConfigurationHolder;
import com.bivashy.configuration.annotation.ConfigField;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

public class BaseBossBarSettings implements ConfigurationHolder, BossBarSettings {
    @ConfigField("use")
    private boolean enabled = false;
    @ConfigField("bar-color")
    private ServerBossbar.Color barColor = Color.BLUE;
    @ConfigField("bar-style")
    private ServerBossbar.Style barStyle = Style.SOLID;
    @ConfigField("bar-text")
    private ServerComponent barText = ServerComponent.fromPlain("[Authentication]");

    public BaseBossBarSettings(ConfigurationSectionHolder sectionHolder) {
        AuthPlugin.instance().getConfigurationProcessor().resolve(sectionHolder, this);
    }

    public BaseBossBarSettings() {
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public ServerBossbar createBossbar() {
        if (!enabled)
            return null;
        return AuthPlugin.instance().getCore().createBossbar(barText).color(barColor).style(barStyle).update();
    }
}
