package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.MessageContext;
import com.bivashy.auth.api.crypto.HashInput;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.server.player.ServerPlayer;
import me.mastercapexd.auth.config.message.context.misc.TimePlaceholderContext;
import me.mastercapexd.auth.config.message.server.ServerMessageContext;
import me.mastercapexd.auth.shared.commands.annotation.CommandCooldown;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.AutoComplete;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Command({"license", "premium", "authlicense"})
public class LicenseCommand {

    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @DefaultFor({"license", "premium", "authlicense"})
    @AutoComplete("* @onOff")
    @CommandCooldown(CommandCooldown.DEFAULT_VALUE)
    public void changePlayerPassword(ServerPlayer sender, @Named("пароль") String password, @Named("Вкл/Выкл") String onOff) {
        String[] onVariants = new String[]{"on", "yes", "y", "enable"};
        String[] offVariants = new String[]{"off", "no", "n", "disable"};

        if (!Arrays.asList(onVariants).contains(onOff.toLowerCase()) && !Arrays.asList(offVariants).contains(onOff.toLowerCase())) {
            sender.sendMessage(config.getServerMessages().getMessage("must-be-on-or-off",
                    MessageContext.of("%argument%", onOff)));
            return;
        }

        String id = config.getActiveIdentifierType().getId(sender);
        accountStorage.getAccount(id).thenAcceptAsync(account -> {
            if (account == null || !account.isRegistered()) {
                sender.sendMessage(config.getServerMessages().getMessage("account-not-found"));
                return;
            }

            boolean isWrongPassword = !account.getCryptoProvider().matches(HashInput.of(password), account.getPasswordHash());
            if (isWrongPassword) {
                sender.sendMessage(config.getServerMessages().getMessage("wrong-confirmation-password"));
                return;
            }

            if (Arrays.asList(onVariants).contains(onOff.toLowerCase())) {
                if (account.isPremium()) {
                    sender.sendMessage(config.getServerMessages().getMessage("already-premium"));
                    return;
                }

                plugin.getPendingPremiumAccountBucket().addPendingPremiumAccount(account);
                sender.disconnect(config.getServerMessages().getMessage("rejoin-from-license-kick",
                        new TimePlaceholderContext(config, TimeUnit.MILLISECONDS.toSeconds(config.getLicenseVerifyTimeMillis()))));
            } else {
                if (!account.isPremium()) {
                    sender.sendMessage(config.getServerMessages().getMessage("not-premium-yet", new ServerMessageContext(account)));
                    return;
                }

                account.setIsPremium(false);
                account.logout(config.getSessionDurability());
                accountStorage.saveOrUpdateAccount(account);
                sender.sendMessage(config.getServerMessages().getMessage("license-disabled-successfully-chat"));
                plugin
                        .getCore()
                        .createTitle(config.getServerMessages().getMessage("license-disabled-successfully-title"))
                        .subtitle(config.getServerMessages().getMessage("license-disabled-successfully-subtitle"))
                        .stay(80)
                        .fadeOut(10)
                        .send(sender);
            }
        });
    }

}