package me.mastercapexd.auth.messenger.commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import me.mastercapexd.auth.account.Account;
import me.mastercapexd.auth.account.factories.AccountFactory;
import me.mastercapexd.auth.config.PluginConfig;
import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.LinkType;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.link.message.Message;
import me.mastercapexd.auth.link.user.LinkUser;
import me.mastercapexd.auth.proxy.ProxyPlugin;
import me.mastercapexd.auth.proxy.commands.annotations.GoogleUse;
import me.mastercapexd.auth.storage.AccountStorage;
import me.mastercapexd.auth.utils.GoogleAuthenticatorQRGenerator;
import me.mastercapexd.auth.utils.RandomCodeFactory;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class GoogleCommand implements OrphanCommand {
	@Dependency
	private ProxyPlugin plugin;
	@Dependency
	private PluginConfig config;
	@Dependency
	private AccountStorage accountStorage;

	@Default
	@GoogleUse
	public void linkGoogle(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
		String rawKey = plugin.getGoogleAuthenticator().createCredentials().getKey();
		String nickname = account.getName();
		String randomCode = "MINECRAFT_"+RandomCodeFactory.generateCode(2);
		
		String totpKey = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(nickname, randomCode, rawKey);
		
		LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElseGet(() -> {
			GoogleLinkUser googleLinkUser = new GoogleLinkUser(account, AccountFactory.DEFAULT_GOOGLE_KEY);
			account.addLinkUser(googleLinkUser);
			return googleLinkUser;
		});

		String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();

		if (linkUserKey == null || linkUserKey.equals(AccountFactory.DEFAULT_GOOGLE_KEY) || linkUserKey.isEmpty()) {
			String rawContent = linkType.getLinkMessages().getStringMessage("google-generated")
					.replaceAll("(?i)%google_key%", rawKey);
			Message googleQRMessage = buildGoogleQRMessage(totpKey, rawContent, linkType);
			actorWrapper.send(googleQRMessage);
		} else {
			String rawContent = linkType.getLinkMessages().getStringMessage("google-regenerated")
					.replaceAll("(?i)%google_key%", rawKey);
			Message googleQRMessage = buildGoogleQRMessage(totpKey, rawContent, linkType);
			actorWrapper.send(googleQRMessage);
		}

		linkUser.getLinkUserInfo().getIdentificator().setString(totpKey);
		accountStorage.saveOrUpdateAccount(account);
	}

	private Message buildGoogleQRMessage(String key, String messageRawContent, LinkType linkType) {
		File temporaryImageFile = null;
		try {
			temporaryImageFile = File.createTempFile("google-qr-image", ".png");

			BitMatrix matrix = new MultiFormatWriter().encode(key, BarcodeFormat.QR_CODE, 200, 200);

			ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "PNG", temporaryImageFile);
		} catch (WriterException | IOException e) {
			e.printStackTrace();
			return null;
		}
		Message message = linkType.newMessageBuilder().rawContent(messageRawContent).uploadPhoto(temporaryImageFile)
				.build();
		
		temporaryImageFile.deleteOnExit();
		return message;
	}
}
