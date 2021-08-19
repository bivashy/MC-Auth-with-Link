package me.mastercapexd.auth.vk.commandhandler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.function.Consumer;

import com.ubivashka.vk.bungee.VKAPI;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.PluginConfig;
import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.vk.ButtonFactory;
import me.mastercapexd.auth.vk.CommandFactory;
import me.mastercapexd.auth.vk.VKButtonFactory;
import me.mastercapexd.auth.vk.VKCommandFactory;
import me.mastercapexd.auth.vk.buttons.VKAccountButton;
import me.mastercapexd.auth.vk.buttons.VKAllAccountsButton;
import me.mastercapexd.auth.vk.buttons.VKAllLinkedAccountsButton;
import me.mastercapexd.auth.vk.buttons.VKEnterButton;
import me.mastercapexd.auth.vk.buttons.VKKickButton;
import me.mastercapexd.auth.vk.buttons.VKPageButton;
import me.mastercapexd.auth.vk.buttons.VKRestoreButton;
import me.mastercapexd.auth.vk.buttons.VKReturnButton;
import me.mastercapexd.auth.vk.buttons.VKUnlinkButton;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonHandler;
import me.mastercapexd.auth.vk.buttonshandler.VKCallbackButton;
import me.mastercapexd.auth.vk.commands.VKAccountsCommand;
import me.mastercapexd.auth.vk.commands.VKAdminPanelCommand;
import me.mastercapexd.auth.vk.commands.VKChangePasswordCommand;
import me.mastercapexd.auth.vk.commands.VKEnterAcceptCommand;
import me.mastercapexd.auth.vk.commands.VKEnterDeclineCommand;
import me.mastercapexd.auth.vk.commands.VKKickCommand;
import me.mastercapexd.auth.vk.commands.VKLinkCommand;
import me.mastercapexd.auth.vk.commands.VKRestoreCommand;
import me.mastercapexd.auth.vk.commands.VKUnlinkCommand;

public class VKReceptioner {
	private static final VkApiClient vk = VKAPI.getInstance().getVK();
	private static final GroupActor actor = VKAPI.getInstance().getActor();
	private static final Random random = new Random();
	private final VKCommandFactory commandFactory = new CommandFactory();
	private final VKButtonFactory buttonFactory = new ButtonFactory();
	private final AuthPlugin plugin;
	private final PluginConfig config;
	private final AccountStorage accountStorage;
	private final VKCommandHandler commandHandler;
	private final VKButtonHandler buttonHandler;

	public VKReceptioner(AuthPlugin plugin, PluginConfig config, AccountStorage accountStorage,
			VKCommandHandler commandHandler, VKButtonHandler buttonHandler) {
		this.plugin = plugin;
		this.config = config;
		this.accountStorage = accountStorage;
		this.commandHandler = commandHandler;
		this.buttonHandler = buttonHandler;
		registerCommands();
		registerButtons();
	}

	private void registerButtons() {
		addButton(buttonFactory.createButton("nextpage", new VKPageButton(this), Arrays.asList("previouspage")));
		addButton(buttonFactory.createButton("account", new VKAccountButton(this)));
		addButton(buttonFactory.createButton("kick", new VKKickButton(this)));
		addButton(buttonFactory.createButton("unlink", new VKUnlinkButton(this)));
		addButton(buttonFactory.createButton("return", new VKReturnButton(this)));
		addButton(buttonFactory.createButton("restore", new VKRestoreButton(this)));
		addButton(buttonFactory.createButton("enterserver", new VKEnterButton(this)));
		addButton(buttonFactory.createButton("allAccounts", new VKAllAccountsButton(this)));
		addButton(buttonFactory.createButton("allLinkedAccounts", new VKAllLinkedAccountsButton(this)));
	}

	private void registerCommands() {
		addCommand(commandFactory.createCommand("/принять", new VKEnterAcceptCommand(this)));
		addCommand(commandFactory.createCommand("/отклонить", new VKEnterDeclineCommand(this)));
		addCommand(commandFactory.createCommand("/пароль", new VKChangePasswordCommand(this)));
		addCommand(commandFactory.createCommand("/отвязать", new VKUnlinkCommand(this)));
		addCommand(commandFactory.createCommand("/аккаунты", new VKAccountsCommand(this)));
		addCommand(commandFactory.createCommand("/кик", new VKKickCommand(this)));
		addCommand(commandFactory.createCommand("/восстановить", new VKRestoreCommand(this)));
		addCommand(commandFactory.createCommand("/code", new VKLinkCommand(this), Arrays.asList("/код")));
		addCommand(commandFactory.createCommand("/админ-панель", new VKAdminPanelCommand(this),
				Arrays.asList("/админпанель", "/админ", "/панель", "/admin-panel", "/adminpanel", "/admin", "/panel")));
	}

	public VKCommandFactory getCommandFactory() {
		return commandFactory;
	}

	public VKButtonFactory getButtonFactory() {
		return buttonFactory;
	}

	public void addButton(VKCallbackButton button) {
		buttonHandler.addButton(button);
	}

	public void addCommand(VKCommand command) {
		commandHandler.addCommand(command);
	}

	public void actionWithAccount(Integer userId, String accountName, Consumer<Account> action) {
		Consumer<Collection<Account>> accountsConsumer = (accounts -> {
			Account findedAccount = null;
			for (Account account : accounts)
				if (account.getName().equalsIgnoreCase(accountName)) {
					findedAccount = account;
					break;
				}
			if (findedAccount == null) {
				try {
					vk.messages().send(actor).randomId(random.nextInt()).userId(userId)
							.message(config.getVKMessages().getLegacyMessage("not-your-account")).execute();
				} catch (ApiException | ClientException e1) {
					e1.printStackTrace();
				}
				return;
			}
			action.accept(findedAccount);
		});
		if (config.getVKSettings().isAdminUser(userId)) {
			accountStorage.getAccount(config.getActiveIdentifierType().fromRawString(accountName))
					.thenAccept(account -> {
						accountsConsumer.accept(Arrays.asList(account));
					});
		} else {
			accountStorage.getAccountsByVKID(userId).thenAccept(accounts -> {
				accountsConsumer.accept(accounts);
			});
		}
	}

	public PluginConfig getConfig() {
		return config;
	}

	public AccountStorage getAccountStorage() {
		return accountStorage;
	}

	public AuthPlugin getPlugin() {
		return plugin;
	}
}
