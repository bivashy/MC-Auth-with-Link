package me.mastercapexd.auth.vk.buttons;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKPreviousPageButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKPreviousPageButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String afterPayload) {
		Integer page = Integer.parseInt(afterPayload.split("_")[0]);
		VKAccountsPageType pageType = VKAccountsPageType.valueOf(afterPayload.split("_")[1]);
		if (pageType == VKAccountsPageType.OWNPAGE)
			receptioner.getPlugin().getVkUtils().sendAccountsKeyboard(e.getButtonEvent().getUserID(), page - 1);
		if (pageType == VKAccountsPageType.ALLACCOUNTSPAGE || pageType == VKAccountsPageType.ALLLINKEDACCOUNTSPAGE) {
			if (!receptioner.getConfig().getVKSettings().isAdminUser(e.getButtonEvent().getUserID()))
				return;
			getAccountsByType(pageType).thenAccept(accounts -> {
				receptioner.getPlugin().getVkUtils().sendAccountsKeyboard(e.getButtonEvent().getUserID(), page - 1,
						accounts, pageType);
			});
		}
	}

	private CompletableFuture<Collection<Account>> getAccountsByType(VKAccountsPageType type) {
		if (type == VKAccountsPageType.ALLACCOUNTSPAGE)
			return receptioner.getAccountStorage().getAllAccounts();
		if (type == VKAccountsPageType.ALLLINKEDACCOUNTSPAGE)
			return receptioner.getAccountStorage().getAllLinkedAccounts();
		return null;
	}
}
