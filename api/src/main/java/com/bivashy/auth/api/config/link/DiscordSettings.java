package com.bivashy.auth.api.config.link;

public interface DiscordSettings extends LinkSettings {
    String getBotToken();

    boolean isAllowedChannel(String channelId);
}
