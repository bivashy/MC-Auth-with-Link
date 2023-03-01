package com.bivashy.auth.api.config.bossbar;

import com.bivashy.auth.api.server.bossbar.ServerBossbar;

public interface BossBarSettings {
    boolean isEnabled();

    ServerBossbar createBossbar();
}
