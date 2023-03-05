package me.mastercapexd.auth.server.commands;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.config.message.server.ServerMessages;
import com.bivashy.auth.api.database.AccountDatabase;

import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import me.mastercapexd.auth.server.commands.annotations.TelegramUse;
import me.mastercapexd.auth.server.commands.annotations.VkUse;
import me.mastercapexd.auth.server.commands.exception.SendComponentException;
import me.mastercapexd.auth.server.commands.parameters.DoublePassword;
import me.mastercapexd.auth.server.commands.parameters.NewPassword;
import me.mastercapexd.auth.server.commands.parameters.RegisterPassword;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.command.ArgumentStack;

public abstract class ProxyCommandsRegistry {
    protected AuthPlugin plugin = AuthPlugin.instance();
    protected CommandHandler commandHandler;

    public ProxyCommandsRegistry(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;

        registerCommandContexts();
        registerDependencies();
    }

    private void registerCommandContexts() {
        PluginConfig config = plugin.getConfig();

        commandHandler.registerValueResolver(DoublePassword.class, (context) -> {
            ArgumentStack arguments = context.arguments();
            String oldPassword = arguments.pop();
            if (arguments.isEmpty())
                throw new SendComponentException(config.getServerMessages().getMessage("enter-new-password"));
            String newPassword = arguments.pop();
            DoublePassword password = new DoublePassword(oldPassword, newPassword);
            if (oldPassword.equals(newPassword))
                throw new SendComponentException(config.getServerMessages().getMessage("nothing-to-change"));

            if (newPassword.length() < config.getPasswordMinLength())
                throw new SendComponentException(config.getServerMessages().getMessage("password-too-short"));

            if (newPassword.length() > config.getPasswordMaxLength())
                throw new SendComponentException(config.getServerMessages().getMessage("password-too-long"));
            return password;
        });

        commandHandler.registerValueResolver(NewPassword.class, context -> {
            String newRawPassword = context.pop();
            if (newRawPassword.length() < config.getPasswordMinLength())
                throw new SendComponentException(config.getServerMessages().getMessage("password-too-short"));

            if (newRawPassword.length() > config.getPasswordMaxLength())
                throw new SendComponentException(config.getServerMessages().getMessage("password-too-long"));
            return new NewPassword(newRawPassword);
        });

        commandHandler.registerValueResolver(RegisterPassword.class, context -> {
            ArgumentStack arguments = context.arguments();
            String registerPassword = arguments.pop();

            if (config.isPasswordConfirmationEnabled()) {
                if (arguments.isEmpty())
                    throw new SendComponentException(config.getServerMessages().getMessage("confirm-password"));
                String confirmationPassword = arguments.pop();
                if (!confirmationPassword.equals(registerPassword))
                    throw new SendComponentException(config.getServerMessages().getMessage("confirm-failed"));
            }

            if (registerPassword.length() < config.getPasswordMinLength())
                throw new SendComponentException(config.getServerMessages().getMessage("password-too-short"));

            if (registerPassword.length() > config.getPasswordMaxLength())
                throw new SendComponentException(config.getServerMessages().getMessage("password-too-long"));
            return new RegisterPassword(registerPassword);
        });

        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!command.hasAnnotation(GoogleUse.class))
                return;
            if (!config.getGoogleAuthenticatorSettings().isEnabled())
                throw new SendComponentException(config.getServerMessages().getSubMessages("google").getMessage("disabled"));
        });

        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!command.hasAnnotation(VkUse.class))
                return;
            if (!config.getVKSettings().isEnabled())
                throw new SendComponentException(config.getServerMessages().getSubMessages("vk").getMessage("disabled"));
        });

        commandHandler.registerCondition((actor, command, arguments) -> {
            if (!command.hasAnnotation(TelegramUse.class))
                return;
            if (!config.getTelegramSettings().isEnabled())
                throw new SendComponentException(config.getServerMessages().getSubMessages("telegram").getMessage("disabled"));
        });
    }

    private void registerDependencies() {
        commandHandler.registerDependency(PluginConfig.class, plugin.getConfig());
        commandHandler.registerDependency(ServerMessages.class, plugin.getConfig().getServerMessages());
        commandHandler.registerDependency(AccountDatabase.class, plugin.getAccountDatabase());
        commandHandler.registerDependency(AuthPlugin.class, plugin);
    }

    protected void registerCommands() {
        commandHandler.register(new AuthCommand(), new LoginCommand(), new RegisterCommand(), new ChangePasswordCommand(), new GoogleCodeCommand(),
                new GoogleCommand(), new GoogleUnlinkCommand(), new LogoutCommand());

        if (plugin.getConfig().getVKSettings().isEnabled())
            commandHandler.register(new VKLinkCommand());
        if (plugin.getConfig().getTelegramSettings().isEnabled())
            commandHandler.register(new TelegramLinkCommand());
    }
}
