package me.mastercapexd.auth.velocity;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.server.ServerCore;
import com.ubivashka.messenger.vk.message.VkMessage;
import com.ubivashka.messenger.vk.provider.VkApiProvider;
import com.ubivashka.vk.velocity.VelocityVkApiPlugin;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import io.github.revxrsal.eventbus.EventBusBuilder;
import me.mastercapexd.auth.BaseAuthPlugin;
import me.mastercapexd.auth.hooks.VkPluginHook;
import me.mastercapexd.auth.velocity.adventure.VelocityAudienceProvider;
import me.mastercapexd.auth.velocity.commands.VelocityCommandRegistry;
import me.mastercapexd.auth.velocity.hooks.VelocityVkPluginHook;
import me.mastercapexd.auth.velocity.listener.AuthenticationListener;
import me.mastercapexd.auth.velocity.listener.VkDispatchListener;
import me.mastercapexd.auth.vk.command.VKCommandRegistry;
import net.kyori.adventure.platform.AudienceProvider;

public class VelocityAuthPluginBootstrap {
    private static VelocityAuthPluginBootstrap instance;
    private final AudienceProvider audienceProvider;
    private final ServerCore core;
    private final File dataFolder;
    private final ProxyServer proxyServer;
    private BaseAuthPlugin authPlugin;

    @Inject
    public VelocityAuthPluginBootstrap(ProxyServer proxyServer, @DataDirectory Path dataDirectory) {
        instance = this;
        this.proxyServer = proxyServer;
        this.dataFolder = dataDirectory.toFile();
        this.core = new VelocityProxyCore(proxyServer);
        this.audienceProvider = new VelocityAudienceProvider(proxyServer);
    }

    public static VelocityAuthPluginBootstrap getInstance() {
        return instance;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.authPlugin = new BaseAuthPlugin(audienceProvider,
                proxyServer.getPluginManager().fromInstance(this).map(PluginContainer::getDescription).flatMap(PluginDescription::getVersion).orElse("unknown"),
                dataFolder, core).eventBus(EventBusBuilder.methodHandles().executor(Executors.newFixedThreadPool(4)).build());
        initializeListener();
        initializeCommand();
        if (authPlugin.getConfig().getVKSettings().isEnabled())
            initializeVk();
    }

    private void initializeVk() {
        authPlugin.putHook(VkPluginHook.class, new VelocityVkPluginHook());
        VkMessage.setDefaultApiProvider(VkApiProvider.of(VelocityVkApiPlugin.getInstance().getVkApiProvider().getActor(),
                VelocityVkApiPlugin.getInstance().getVkApiProvider().getVkApiClient()));

        proxyServer.getEventManager().register(this, new VkDispatchListener());
        new VKCommandRegistry();
    }

    private void initializeListener() {
        proxyServer.getEventManager().register(this, new AuthenticationListener(authPlugin));
    }

    private void initializeCommand() {
        new VelocityCommandRegistry(this, authPlugin);
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }

    public AuthPlugin getAuthPlugin() {
        return authPlugin;
    }
}
