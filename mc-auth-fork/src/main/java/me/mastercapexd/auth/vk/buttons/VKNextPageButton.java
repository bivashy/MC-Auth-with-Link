package me.mastercapexd.auth.vk.buttons;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.ubivashka.vk.bungee.events.VKCallbackButtonPressEvent;

import me.mastercapexd.auth.Account;
import me.mastercapexd.auth.vk.VKAccountsPageType;
import me.mastercapexd.auth.vk.buttonshandler.VKButtonExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKNextPageButton implements VKButtonExecutor {
	private final VKReceptioner receptioner;

	public VKNextPageButton(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKCallbackButtonPressEvent e, String afterPayload) {
		Integer page = Integer.parseInt(afterPayload.split("_")[0])
				+ ((e.getButtonEvent().getPayload().startsWith("nextpage")) ? 1 : -1);
		VKAccountsPageType pageType = VKAccountsPageType.valueOf(afterPayload.split("_")[1]);
		if (pageType == VKAccountsPageType.OWNPAGE)
			receptioner.getPlugin().getVkUtils().sendAccountsKeyboard(e.getButtonEvent().getUserID(), page);
		if (pageType == VKAccountsPageType.ALLACCOUNTSPAGE || pageType == VKAccountsPageType.ALLLINKEDACCOUNTSPAGE) {
			if (!receptioner.getConfig().getVKSettings().isAdminUser(e.getButtonEvent().getUserID()))
				return;
			getAccountsByType(pageType).thenAccept(accounts -> {
				receptioner.getPlugin().getVkUtils().sendAccountsKeyboard(e.getButtonEvent().getUserID(), page,
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
