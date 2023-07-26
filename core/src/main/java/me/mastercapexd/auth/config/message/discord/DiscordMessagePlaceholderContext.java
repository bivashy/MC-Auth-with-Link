package me.mastercapexd.auth.config.message.discord;

import com.bivashy.auth.api.account.Account;

import me.mastercapexd.auth.config.message.link.context.LinkPlaceholderContext;
import me.mastercapexd.auth.link.discord.DiscordLinkType;

public class DiscordMessagePlaceholderContext extends LinkPlaceholderContext {
    public DiscordMessagePlaceholderContext(Account account) {
        super(account, DiscordLinkType.getInstance(), "discord");
    }
}
