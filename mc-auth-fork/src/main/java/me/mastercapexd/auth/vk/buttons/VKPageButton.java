package me.mastercapexd.auth.vk.buttons;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.builders.AccountsMessageBuilder;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKPageButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKPageButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String afterPayload) {
		Integer page = Integer.parseInt(afterPayload.split("_")[0])
				+ ((e.getButtonEvent().getPayload().startsWith("nextpage")) ? 1 : -1);
		VKAccountsPageType pageType = VKAccountsPageType.valueOf(afterPayload.split("_")[1]);
		if (pageType == VKAccountsPageType.ALLACCOUNTSPAGE || pageType == VKAccountsPageType.ALLLINKEDACCOUNTSPAGE)
			if (!receptioner.getConfig().getVKSettings().isAdminUser(e.getButtonEvent().getUserID()))
				return;
		getAccountsByType(pageType, e.getButtonEvent().getUserID()).thenAccept(accounts -> {
			new AccountsMessageBuilder(e.getButtonEvent().getUserID(), page, pageType, accounts, receptioner).execute();
		});
	}

	private CompletableFuture<Collection<Account>> getAccountsByType(VKAccountsPageType type, Integer userId) {
		switch (type) {
		case ALLACCOUNTSPAGE:
			return receptioner.getAccountStorage().getAllAccounts();
		case ALLLINKEDACCOUNTSPAGE:
			return receptioner.getAccountStorage().getAllLinkedAccounts();
		case OWNPAGE:
			return receptioner.getAccountStorage().getAccountsByVKID(userId);
		default:
			return null;
		}
	}
}
