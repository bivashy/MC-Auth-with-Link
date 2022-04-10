package me.mastercapexd.auth.vk.commands;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ubivashka.vk.bungee.events.VKMessageEvent;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.photos.responses.PhotoUploadResponse;
import com.vk.api.sdk.objects.photos.responses.SaveMessagesPhotoResponse;

import me.mastercapexd.auth.bungee.AuthPlugin;
import me.mastercapexd.auth.link.google.GoogleLinkType;
import me.mastercapexd.auth.link.google.GoogleLinkUser;
import me.mastercapexd.auth.utils.RandomCodeFactory;
import me.mastercapexd.auth.vk.commandhandler.VKCommandExecutor;
import me.mastercapexd.auth.vk.commandhandler.VKReceptioner;

public class VKGoogleCommand extends VKCommandExecutor {
	private final VKReceptioner receptioner;

	public VKGoogleCommand(VKReceptioner receptioner) {
		this.receptioner = receptioner;
	}

	@Override
	public void execute(VKMessageEvent e, String[] args) {
		if (!receptioner.getConfig().getGoogleAuthenticatorSettings().isEnabled()) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-disabled"));
			return;
		}
		if (args.length == 0) {
			sendMessage(e.getPeer(),
					receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-not-enough-arguments"));
			return;
		}
		String playerName = args[0];
		receptioner.actionWithAccount(e.getUserId(), playerName, account -> {
			String key = receptioner.getPlugin().getGoogleAuthenticator().createCredentials().getKey();
			account.findFirstLinkUser(GoogleLinkType.LINK_USER_FILTER).orElse(new GoogleLinkUser(account, key))
					.getLinkUserInfo().getIdentificator().setString(playerName);
			receptioner.getAccountStorage().saveOrUpdateAccount(account);
			String googleUrl = "otpauth://totp/Minecraft?secret=" + key + "&issuer=" + account.getName();
			try {
				BufferedImage qrCodeImage = convertTextToQR(googleUrl);
				String image = toImage(qrCodeImage);
				vk.messages().send(actor).randomId(random.nextInt()).peerId(e.getPeer()).attachment(image)
						.message("Ключ:" + key).execute();
			} catch (WriterException | ApiException | ClientException | IOException ex) {
				ex.printStackTrace();
				sendMessage(e.getPeer(),
						receptioner.getConfig().getVKSettings().getVKMessages().getMessage("google-error"));
			}
		});
	}

	private String toImage(BufferedImage bufferedImage) throws ApiException, ClientException, IOException {
		File out = new File(
				AuthPlugin.getInstance().getDataFolder() + File.separator + RandomCodeFactory.generateCode(6) + ".jpg");
		ImageIO.write(bufferedImage, "PNG", out);
		String URL = vk.photos().getMessagesUploadServer(actor).execute().getUploadUrl().toString();
		PhotoUploadResponse uploadPhoto = vk.upload().photo(URL, out).execute();
		SaveMessagesPhotoResponse photo = vk.photos().saveMessagesPhoto(actor, uploadPhoto.getPhoto())
				.server(uploadPhoto.getServer()).hash(uploadPhoto.getHash()).execute().get(0);
		out.delete();
		return "photo" + photo.getOwnerId() + "_" + photo.getId();
	}

	private BufferedImage convertTextToQR(String text) throws UnsupportedEncodingException, WriterException {
		String charset = "UTF-8";
		BitMatrix matrix = new MultiFormatWriter().encode(new String(text.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, 200, 200);
		return MatrixToImageWriter.toBufferedImage(matrix);

	}

	@Override
	public String getKey() {
		return "google";
	}

}
