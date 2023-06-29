[![GitHub tag](https://img.shields.io/github/tag/bivashy/MC-Auth-with-Link?include_prereleases=&sort=semver&color=blue)](https://github.com/bivashy/MC-Auth-with-Link/releases/)
[![License](https://img.shields.io/badge/License-_EPL--2.0_license-blue)](#license)
[![issues - MC-Auth-with-Link](https://img.shields.io/github/issues/bivashy/MC-Auth-with-Link)](https://github.com/bivashy/MC-Auth-with-Link/issues)

# MC-Auth-with-Link

## Basic features
- Authentication flow control using authentication steps
- MultiFactor authorization (VK, Telegram, GoogleAuthenticator)
- Session system

## Platforms
- [X] **BungeeCord**
- [X] **Velocity**
- [ ] Spigot

## Requirements

* **[VK-API-PluginAdapter](https://github.com/bivashy/VK-API-PluginAdapter)**
* **[TelegramBotAPI](https://github.com/bivashy/TelegramBotApi-Minecraft)**

## Documentation

<div style="text-align: center;">

[![view - Documentation](https://img.shields.io/badge/view-Documentation-blue?style=for-the-badge)](https://bivashy.gitbook.io/mcauth/)

</div>

## Developer section

Retrieve `AuthPlugin` instance:

```java
import com.bivashy.auth.api.AuthPlugin;

import me.mastercapexd.auth.bungee.BungeeAuthPluginBootstrap;
import me.mastercapexd.auth.velocity.VelocityAuthPluginBootstrap;

// Other imports
public class ExampleClass {
    public void exampleMethod() {
        // API Instance
        AuthPlugin authPlugin = AuthPlugin.instance();
        // BungeeCord Instance
        BungeeAuthPluginBootstrap bungeeAuthPlugin = BungeeAuthPluginBootstrap.getInstance();
        // Velocity Instance
        VelocityAuthPluginBootstrap velocityAuthPlugin = VelocityAuthPluginBootstrap.getInstance();

        // Also you can use builtin API
        BungeeAuthPluginBoostrap bungeeAuthPlugin = (BungeeAuthPluginBoostrap) ProxyServer.getInstance().getPluginManager().getPlugin("mcAuth");

        VelocityAuthPluginBootstrap velocityAuthPlugin = (VelocityAuthPluginBootstrap) proxyServer.getPluginManager()
                .getPlugin("mcauth")
                .orElseThrow(IllegalStateException::new)
                .getInstance()
                .orElseThrow(IllegalStateException::new);
    }
}
```

---

How to get `Account` from name:

```java
public class ExampleClass {
    public void exampleMethod() {
        AuthPlugin authPlugin; // Get auth plugin, like above 
        AccountDatabase database = authPlugin.getAccountDatabase();
        database.getAccountFromName("Notch").thenAccept(account -> {
            if (account == null || !account.isRegistered()) {
                // Account not exists, or not registered
                return;
            }
            // Do something with account
        });
    }
}
```

---

How to get `Account` from VK or Telegram id:

```java
public class ExampleClass {
    public void exampleMethod() {
        AuthPlugin authPlugin; // Get auth plugin, like above 
        AccountDatabase database = authPlugin.getAccountDatabase();

        // For example our user messenger id is 100
        LinkUserIdentificator identificator = new UserNumberIdentificator(100);
        database.getAccountsFromLinkIdentificator(identificator).thenAccept(accounts -> {
            if (accounts.isEmpty()) {
                // No accounts found
                return;
            }
            for (Account account : accounts) {
                if (account == null || !account.isRegistered())
                    continue;
                // Do something with account
            }
        });
    }
}
```

---

How to create custom `AuthenticationStep`:

```java
public class ExampleAuthenticationStep extends AuthenticationStepTemplate {
    public static final String STEP_NAME = "HTTP_CHECK";
    private static final AuthPlugin PLUGIN = AuthPlugin.instance();

    public ExampleAuthenticationStep(AuthenticationStepContext context) {
        super(STEP_NAME, context);
    }

    @Override
    public boolean shouldPassToNextStep() {
        // Executed when Account#nextAuthenticationStep called 
        return true;
    }

    @Override
    public boolean shouldSkip() {
        // Executed when this AuthenticationStep applied to the player
        // If player has session, or some plugin removed him from authentication bucket ignore
        if (!PLUGIN.getAuthenticatingAccountBucket().isAuthorizing(authenticationStepContext.getAccount()) ||
                authenticationStepContext.getAccount().isSessionActive(PLUGIN.getConfig().getSessionDurability()))
            return true;
        // Do additional validation if you want
        return false;
    }

    // Create factory, 
    public static class ExampleAuthenticationStepFactory extends AuthenticationStepFactoryTemplate {
        public ExampleAuthenticationStepFactory() {
            super(STEP_NAME);
        }

        @Override
        public AuthenticationStep createNewAuthenticationStep(AuthenticationStepContext context) {
            return new ExampleAuthenticationStep(context);
        }
    }
}
```

Now we need to register out `AuthenticationStep` and manipulate logging in account:

```java
public class ExampleClass {
    public void exampleMethod() {
        AuthPlugin plugin; // Retrieve plugin instance as above 
        plugin.getAuthenticationStepFactoryBucket().add(new ExampleAuthenticationStepFactory());
    }

    // Now when you do all validation. For example entering code on site, you should 
    // call Account#nextAuthenticationStep. Where 
    public void onCodeEnter(String playerName) {
        AuthPlugin plugin;
        Account account = getRegisteringAccount(playerName);
        account.nextAuthenticationStep(plugin.getAuthenticationContextFactoryBucket().createContext(account));
    }

    // You should take Account from bucket, not database!
    public Account getRegisteringAccount(String name) {
        AuthPlugin plugin;
        return plugin.getAuthenticatingAccountBucket().getAuthorizingAccount(PlayerIdSupplier.of(name.toLowerCase())).orElseThrow(NullPointerException::new);
    }

    // If you have ProxiedPlayer or Player instance:
    public Account getRegisteringAccount(ProxiedPlayer player) {
        AuthPlugin plugin;
        return plugin.getAuthenticatingAccountBucket().getAuthorizingAccount(plugin.getCore().wrapPlayer(player)).orElseThrow(NullPointerException::new);
    }
}
```

---

## License

Released under [EPL-2.0 license](/LICENSE) by [@bivashy](https://github.com/bivashy).
