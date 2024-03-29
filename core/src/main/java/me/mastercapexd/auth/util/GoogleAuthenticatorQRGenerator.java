package me.mastercapexd.auth.util;

import org.apache.http.client.utils.URIBuilder;

/**
 * Source:
 * <a href="https://github.com/wstrange/GoogleAuth/blob/master/src/main/java/com/warrenstrange/googleauth/GoogleAuthenticatorQRGenerator.java">https://github.com/wstrange/GoogleAuth/blob/master/src/main/java/com/warrenstrange/googleauth/GoogleAuthenticatorQRGenerator.java</a>
 */
public final class GoogleAuthenticatorQRGenerator {
    /**
     * The label is used to identify which account a key is associated with. It
     * contains an account name, which is a URI-encoded string, optionally prefixed
     * by an issuer string identifying the provider or service managing that
     * account. This issuer prefix can be used to prevent collisions between
     * different accounts with different providers that might be identified using
     * the same account name, e.g. the user's email address. The issuer prefix and
     * account name should be separated by a literal or url-encoded colon, and
     * optional spaces may precede the account name. Neither issuer nor account name
     * may themselves contain a colon. Represented in ABNF according to RFC 5234:
     * <p>
     * label = accountname / issuer (“:” / “%3A”) *”%20” accountname
     *
     * @see <a href=
     * "<a href="https://code.google.com/p/google-authenticator/wiki/KeyUriFormat">https://code.google.com/p/google-authenticator/wiki/KeyUriFormat</a>">Google
     * Authenticator - KeyUriFormat</a>
     */
    private static String formatLabel(String issuer, String accountName) {
        if (accountName == null || accountName.trim().length() == 0) {
            throw new IllegalArgumentException("Account name must not be empty.");
        }

        StringBuilder sb = new StringBuilder();

        if (issuer != null) {
            if (issuer.contains(":")) {
                throw new IllegalArgumentException("Issuer cannot contain the ':' character.");
            }

            sb.append(issuer);
            sb.append(":");
        }

        sb.append(accountName);

        return sb.toString();
    }

    /**
     * Returns the basic otpauth TOTP URI. This URI might be sent to the user via
     * email, QR code or some other method. Use a secure transport since this URI
     * contains the secret.
     * <p>
     * The current implementation supports the following features:
     * <ul>
     * <li>Label, made up of an optional issuer and an account name.</li>
     * <li>Secret parameter.</li>
     * <li>Issuer parameter.</li>
     * </ul>
     *
     * @param issuer      The issuer name. This parameter cannot contain the colon
     *                    (:) character. This parameter can be null.
     * @param accountName The account name. This parameter shall not be null.
     * @return an otpauth scheme URI for loading into a client application.
     * @see <a href=
     * "<a href="https://github.com/google/google-authenticator/wiki/Key-Uri-Format">https://github.com/google/google-authenticator/wiki/Key-Uri-Format</a>">Google
     * Authenticator - KeyUriFormat</a>
     */
    public static String getOtpAuthTotpURL(String issuer, String accountName, String secret) {
        URIBuilder uri = new URIBuilder().setScheme("otpauth").setHost("totp").setPath("/" + formatLabel(issuer, accountName)).setParameter("secret", secret);

        if (issuer != null) {
            if (issuer.contains(":")) {
                throw new IllegalArgumentException("Issuer cannot contain the ':' character.");
            }

            uri.setParameter("issuer", issuer);
        }

        return uri.toString();
    }
}
