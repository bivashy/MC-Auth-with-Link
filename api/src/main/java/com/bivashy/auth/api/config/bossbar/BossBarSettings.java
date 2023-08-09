package com.bivashy.auth.api.config.bossbar;

import java.text.SimpleDateFormat;

import com.bivashy.auth.api.server.bossbar.ServerBossbar;
import com.bivashy.auth.api.server.message.ServerComponent;

public interface BossBarSettings {
    ServerComponent getTitle();

    SimpleDateFormat getDurationPlaceholderFormat();

    boolean isEnabled();

    ServerBossbar createBossbar();
}
