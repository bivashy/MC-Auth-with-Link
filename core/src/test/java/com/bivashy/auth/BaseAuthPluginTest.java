package com.bivashy.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.asset.resource.ResourceReader;
import com.bivashy.auth.api.server.ServerCore;

import me.mastercapexd.auth.BaseAuthPlugin;
import me.mastercapexd.auth.server.adventure.BaseAdventureServerComponent;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;

// Order this test first to initialize AuthPlugin.instance()
@Order(1)
@ExtendWith(MockitoExtension.class)
public class BaseAuthPluginTest {
    private static final String DUMMY_VERSION = "0.0.0";
    @Mock
    private AudienceProvider audienceProvider;
    @Mock
    private ServerCore serverCore;
    @TempDir
    private File pluginFolder;
    private AuthPlugin plugin;

    @BeforeEach
    public void setup() throws IOException {
        plugin = AuthPlugin.instance();
        if (plugin != null)
            createCachedJdbcDriver();
    }

    @Test
    public void testInit() {
        AuthPlugin plugin = new BaseAuthPlugin(audienceProvider, DUMMY_VERSION, pluginFolder, serverCore);

        assertNotNull(plugin.getCore());
        assertNotNull(plugin.getAudienceProvider());
        assertNotNull(plugin.getConfig());
        assertNotNull(plugin.getAccountFactory());
        assertNotNull(plugin.getAccountDatabase());
        if (plugin.getConfig().getGoogleAuthenticatorSettings().isEnabled())
            assertNotNull(plugin.getGoogleAuthenticator());
        assertNotNull(plugin.getAuthenticationStepFactoryBucket());
        assertNotNull(plugin.getAuthenticationContextFactoryBucket());
        assertNotNull(plugin.getConfigurationProcessor());
        assertNotNull(plugin.getLoginManagement());
        assertNotNull(plugin.getLinkTypeProvider());
        assertNotNull(plugin.getEventBus());
        assertNotNull(plugin.getAuthenticationTaskBucket());
        assertNotNull(plugin.getAuthenticatingAccountBucket());
        assertNotNull(plugin.getLinkConfirmationBucket());
        assertNotNull(plugin.getLinkEntryBucket());
        assertNotNull(plugin.getCryptoProviderBucket());
        assertNotNull(plugin.getVersion());
        assertNotNull(plugin.getFolder());
    }

    @Test
    public void validateBucketSizes() {
        assertEquals(plugin.getAuthenticationStepFactoryBucket().getList().size(), 8);
        assertEquals(plugin.getAuthenticationTaskBucket().getTasks().size(), 3);
    }

    @Test
    public void validateDriver() {
        File cacheDriverFile = plugin.getConfig().getDatabaseConfiguration().getCacheDriverPath();
        assertTrue(cacheDriverFile.exists());

    }

    private void createCachedJdbcDriver() throws IOException {
        File cacheDriverPath = plugin.getConfig().getDatabaseConfiguration().getCacheDriverPath();
        if (!cacheDriverPath.exists()) {
            cacheDriverPath.mkdirs();
            cacheDriverPath.createNewFile();
            ResourceReader.defaultReader(getClass().getClassLoader(), "sqlite-jdbc-3.36.0.3.jar").read().write(cacheDriverPath);
        }
    }
}
