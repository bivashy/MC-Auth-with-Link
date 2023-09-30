package me.mastercapexd.auth.bungee;

import java.util.Collection;
import java.util.Collections;

import com.alessiodp.libby.BungeeLibraryManager;
import com.bivashy.auth.api.hook.LimboPluginHook;
import com.bivashy.messenger.vk.message.VkMessage;
import com.bivashy.messenger.vk.provider.VkApiProvider;
import com.ubivashka.vk.bungee.BungeeVkApiPlugin;

import me.mastercapexd.auth.BaseAuthPlugin;
import me.mastercapexd.auth.bungee.commands.BungeeCommandsRegistry;
import me.mastercapexd.auth.bungee.hooks.BungeeVkPluginHook;
import me.mastercapexd.auth.bungee.hooks.nanolimbo.BungeeNanoLimboPluginHook;
import me.mastercapexd.auth.bungee.listener.AuthenticationListener;
import me.mastercapexd.auth.bungee.listener.VkDispatchListener;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.management.BaseLibraryManagement;
import me.mastercapexd.auth.vk.command.VKCommandRegistry;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeAuthPluginBootstrap extends Plugin {

    private static BungeeAuthPluginBootstrap instance;
    private BungeeAudiences bungeeAudiences;
    private BaseAuthPlugin authPlugin;

    public static BungeeAuthPluginBootstrap getInstance() {
        if (instance == null)
            throw new UnsupportedOperationException("Plugin not enabled!");
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        bungeeAudiences = BungeeAudiences.create(this);
        authPlugin = new BaseAuthPlugin(bungeeAudiences, getDescription().getVersion(), getDataFolder(), BungeeProxyCore.INSTANCE,
                new BaseLibraryManagement(new BungeeLibraryManager(this)));
        initializeListener();
        initializeCommand();
        initializeLimbo();
        if (authPlugin.getConfig().getVKSettings().isEnabled())
            initializeVk();
    }

    @Override
    public void onDisable() {
        if (bungeeAudiences != null)
            bungeeAudiences.close();
    }

    private void initializeListener() {
        this.getProxy().getPluginManager().registerListener(this, new AuthenticationListener(authPlugin));
    }

    private void initializeCommand() {
        new BungeeCommandsRegistry(this, authPlugin);
    }

    private void initializeLimbo() {
        Collection<LimboPluginHook> limboPluginHooks = Collections.singleton(new BungeeNanoLimboPluginHook(authPlugin.getConfig().getLimboPortRange()));
        limboPluginHooks.stream()
                .filter(LimboPluginHook::canHook)
                .forEach(limboPluginHook -> authPlugin.putHook(LimboPluginHook.class, limboPluginHook));
    }

    private void initializeVk() {
        authPlugin.putHook(VkPluginHook.class, new BungeeVkPluginHook());

        VkMessage.setDefaultApiProvider(VkApiProvider.of(BungeeVkApiPlugin.getInstance().getVkApiProvider().getActor(),
                BungeeVkApiPlugin.getInstance().getVkApiProvider().getVkApiClient()));

        getProxy().getPluginManager().registerListener(this, new VkDispatchListener());
        new VKCommandRegistry();
    }

    public BungeeAudiences getBungeeAudiences() {
        return bungeeAudiences;
    }

}