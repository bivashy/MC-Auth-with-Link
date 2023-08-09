package me.mastercapexd.auth.discord.listener;

import java.util.Collection;
import java.util.Optional;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.event.AccountLinkEvent;
import com.bivashy.auth.api.event.AccountStepChangeEvent;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.bivashy.auth.api.link.user.info.LinkUserIdentificator;
import com.bivashy.auth.api.link.user.info.LinkUserInfo;
import com.bivashy.auth.api.server.player.ServerPlayer;

import io.github.revxrsal.eventbus.SubscribeEvent;
import me.mastercapexd.auth.config.discord.DiscordConfirmationSettings;
import me.mastercapexd.auth.config.discord.RoleModificationSettings;
import me.mastercapexd.auth.config.discord.RoleModificationSettings.Type;
import me.mastercapexd.auth.hooks.DiscordHook;
import me.mastercapexd.auth.link.discord.DiscordLinkType;
import me.mastercapexd.auth.step.impl.link.DiscordLinkAuthenticationStep;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class DiscordLinkRoleModifierListener {
    private final DiscordHook discordHook = AuthPlugin.instance().getHook(DiscordHook.class);
    private final DiscordConfirmationSettings discordConfirmationSettings = ((DiscordConfirmationSettings) DiscordLinkType.getInstance()
            .getSettings()
            .getConfirmationSettings());
    private final Collection<RoleModificationSettings> roleModificationSettings = discordConfirmationSettings.getRoleModificationSettings().values();
    private final long guildId = discordConfirmationSettings.getGuildId();

    @SubscribeEvent
    public void onDiscordAuthenticationStep(AccountStepChangeEvent e) {
        if (discordConfirmationSettings.shouldUpdateRoles())
            return;
        if (!e.getOldStep().getStepName().equals(DiscordLinkAuthenticationStep.STEP_NAME))
            return;
        Account account = e.getAccount();
        account.findFirstLinkUser(DiscordLinkType.LINK_USER_FILTER)
                .filter(linkUser -> !linkUser.isIdentifierDefaultOrNull())
                .map(LinkUser::getLinkUserInfo)
                .map(LinkUserInfo::getIdentificator)
                .map(LinkUserIdentificator::asNumber)
                .ifPresent(discordId -> updateAccountDiscordRoles(account, discordId));
    }

    @SubscribeEvent
    public void onDiscordLink(AccountLinkEvent e) {
        Account account = e.getAccount();
        LinkType linkType = e.getLinkType();
        if (linkType != DiscordLinkType.getInstance())
            return;
        updateAccountDiscordRoles(account, e.getIdentificator().asNumber());
    }

    private void updateAccountDiscordRoles(Account account, long discordId) {
        if (roleModificationSettings.isEmpty())
            return;
        Optional<ServerPlayer> playerOptional = account.getPlayer();

        Guild guild = discordHook.getJDA().getGuildById(guildId);
        if (guild == null)
            throw new IllegalArgumentException("Cannot find guild by id '" + guildId + "', check if guild id is valid");
        guild.retrieveMemberById(discordId).queue(foundMember -> {
            for (RoleModificationSettings roleModification : roleModificationSettings) {
                boolean hasPermissionCheck = !roleModification.getHavePermission().isEmpty() || !roleModification.getAbsentPermission().isEmpty();
                if (hasPermissionCheck && !playerOptional.isPresent())
                    continue;

                if (playerOptional.isPresent()) {
                    ServerPlayer player = playerOptional.get();
                    if (roleModification.getHavePermission().stream().anyMatch(permission -> !player.hasPermission(permission)))
                        continue;
                    if (roleModification.getAbsentPermission().stream().anyMatch(player::hasPermission))
                        continue;
                }

                Role role = guild.getRoleById(roleModification.getRoleId());

                if (role == null)
                    throw new IllegalArgumentException("Cannot find role by id '" + roleModification.getRoleId() + "', check if role id is valid");

                if (roleModification.getType() == Type.GIVE_ROLE)
                    guild.addRoleToMember(foundMember, role).queue();
                if (roleModification.getType() == Type.REMOVE_ROLE)
                    guild.removeRoleFromMember(foundMember, role).queue();
            }
        });
    }
}
