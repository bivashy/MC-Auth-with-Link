package me.mastercapexd.auth.messenger.commands;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.account.Account;
import com.bivashy.auth.api.account.AccountFactory;
import com.bivashy.auth.api.config.PluginConfig;
import com.bivashy.auth.api.database.AccountDatabase;
import com.bivashy.auth.api.link.LinkType;
import com.bivashy.auth.api.link.user.LinkUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ubivaska.messenger.common.file.MessengerFile;
import com.ubivaska.messenger.common.message.Message;

import me.mastercapexd.auth.link.LinkCommandActorWrapper;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.messenger.commands.annotations.ConfigurationArgumentError;
import me.mastercapexd.auth.server.commands.annotations.GoogleUse;
import me.mastercapexd.auth.util.GoogleAuthenticatorQRGenerator;
import me.mastercapexd.auth.util.RandomCodeFactory;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.Dependency;
import revxrsal.commands.orphan.OrphanCommand;

public class GoogleCommand implements OrphanCommand {
    public static final String CONFIGURATION_KEY = "google";
    @Dependency
    private AuthPlugin plugin;
    @Dependency
    private PluginConfig config;
    @Dependency
    private AccountDatabase accountStorage;

    @Default
    @GoogleUse
    @ConfigurationArgumentError("google-not-enough-arguments")
    public void linkGoogle(LinkCommandActorWrapper actorWrapper, LinkType linkType, Account account) {
        String rawKey = plugin.getGoogleAuthenticator().createCredentials().getKey();
        String nickname = account.getName();
        String randomCode = "MINECRAFT_" + RandomCodeFactory.generateCode(2);

        String totpKey = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL(nickname, randomCode, rawKey);

        LinkUser linkUser = account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElseGet(() -> {
            GoogleLinkUser googleLinkUser = new GoogleLinkUser(account, AccountFactory.DEFAULT_GOOGLE_KEY);
            account.addLinkUser(googleLinkUser);
            return googleLinkUser;
        });

        String linkUserKey = linkUser.getLinkUserInfo().getIdentificator().asString();

        if (Objects.equals(linkUserKey, AccountFactory.DEFAULT_GOOGLE_KEY) || linkUserKey.isEmpty()) {
            String rawContent = linkType.getLinkMessages()
                    .getStringMessage("google-generated", linkType.newMessageContext(account))
                    .replaceAll("(?i)%google_key%", rawKey);
            Message googleQRMessage = buildGoogleQRMessage(totpKey, rawContent, linkType);
            actorWrapper.send(googleQRMessage);
        } else {
            String rawContent = linkType.getLinkMessages()
                    .getStringMessage("google-regenerated", linkType.newMessageContext(account))
                    .replaceAll("(?i)%google_key%", rawKey);
            Message googleQRMessage = buildGoogleQRMessage(totpKey, rawContent, linkType);
            actorWrapper.send(googleQRMessage);
        }

        linkUser.getLinkUserInfo().getIdentificator().setString(rawKey);
        accountStorage.saveOrUpdateAccount(account);
    }

    private Message buildGoogleQRMessage(String key, String messageRawContent, LinkType linkType) {
        File temporaryImageFile;
        try {
            temporaryImageFile = File.createTempFile("google-qr-image", ".png");

            BitMatrix matrix = new MultiFormatWriter().encode(key, BarcodeFormat.QR_CODE, 200, 200);

            ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "PNG", temporaryImageFile);
        } catch(WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
        Message message = linkType.newMessageBuilder(messageRawContent).attachFiles(MessengerFile.of(temporaryImageFile)).build();

        temporaryImageFile.deleteOnExit();
        return message;
    }
}
