package me.mastercapexd.auth.premium;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.premium.PremiumException;
import com.bivashy.auth.api.premium.PremiumProvider;
import com.bivashy.auth.api.util.ThrowableFunction;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BasePremiumProvider implements PremiumProvider {
    private final Cache<String, UUID> userCache;
    private final List<ThrowableFunction<String, UUID, PremiumException>> fetchers;
    private final AuthPlugin plugin;

    public BasePremiumProvider(AuthPlugin plugin) {
        this.plugin = plugin;
        userCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();

        fetchers = new ArrayList<>(2);

        fetchers.add(this::getPremiumUUIDFromMojang);
        fetchers.add(this::getPremiumUUIDFromPlayerDB);
    }

    @Override
    public UUID getPremiumUUIDForName(String name) throws PremiumException {
        name = name.toLowerCase();

        PremiumException[] ex = new PremiumException[1];

        UUID result = userCache.get(name, x -> {
            for (int i = 0; i < fetchers.size(); i++) {
                ThrowableFunction<String, UUID, PremiumException> fetcher = fetchers.get(i);

                try {
                    return fetcher.apply(x);
                } catch (PremiumException e) {
                    if (i == 0 && e.getIssue() == PremiumException.Issue.UNDEFINED) {
                        ex[0] = e;
                        break;
                    }

                    if (i == fetchers.size() - 1) {
                        ex[0] = e;
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    if (i == fetchers.size() - 1) {
                        ex[0] = new PremiumException(PremiumException.Issue.UNDEFINED, e);
                    }
                }
            }
            return null;
        });

        if (ex[0] != null) {
            throw ex[0];
        }

        return result;
    }

    @Nullable
    private UUID getPremiumUUIDFromMojang(String name) throws PremiumException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            switch (connection.getResponseCode()) {
                case 429:
                    throw new PremiumException(PremiumException.Issue.THROTTLED, readInput(connection.getErrorStream()));
                case 204:
                case 404:
                    return null;
                default:
                    throw new PremiumException(PremiumException.Issue.UNDEFINED, readInput(connection.getErrorStream()));
                case 200:
                    JsonObject data = plugin.getGson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);

                    String id = data.get("id").getAsString();
                    Object demo = data.get("demo");

                    return demo != null ? null : fromUnDashedUUID(id);
                case 500:
                    throw new PremiumException(PremiumException.Issue.SERVER_EXCEPTION, readInput(connection.getErrorStream()));
            }
        } catch (SocketTimeoutException te) {
            throw new PremiumException(PremiumException.Issue.THROTTLED, "Mojang API timed out");
        } catch (IOException e) {
            throw new PremiumException(PremiumException.Issue.UNDEFINED, e);
        }
    }

    @Nullable
    private UUID getPremiumUUIDFromPlayerDB(String name) throws PremiumException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://playerdb.co/api/player/minecraft/" + name).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            switch (connection.getResponseCode()) {
                case 200:
                    JsonObject data = plugin.getGson().fromJson(new InputStreamReader(connection.getInputStream()), JsonObject.class);

                    String id = data.get("data").getAsJsonObject().get("player").getAsJsonObject().get("id").getAsString();

                    return UUID.fromString(id);
                case 400:
                    return null;
                default:
                    throw new PremiumException(PremiumException.Issue.UNDEFINED, readInput(connection.getErrorStream()));
            }
        } catch (IOException e) {
            throw new PremiumException(PremiumException.Issue.SERVER_EXCEPTION, e);
        }
    }

    private static String readInput(InputStream inputStream) throws IOException {
        return new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private static UUID fromUnDashedUUID(String uuid) {
        return uuid == null ? null : new UUID(
                new BigInteger(uuid.substring(0, 16), 16).longValue(),
                new BigInteger(uuid.substring(16, 32), 16).longValue()
        );
    }
}
