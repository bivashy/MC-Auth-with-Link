package com.bivashy.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.io.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.management.LibraryManagement;
import com.bivashy.auth.api.server.ServerCore;

import me.mastercapexd.auth.BaseAuthPlugin;
import net.kyori.adventure.platform.AudienceProvider;

// Order this test first to initialize AuthPlugin.instance()
@Order(1)
@ExtendWith(MockitoExtension.class)
public class BaseAuthPluginTest {
    private static final String DUMMY_VERSION = "0.0.0";
    @Mock
    private AudienceProvider audienceProvider;
    @Mock
    private ServerCore serverCore;
    @Mock
    private LibraryManagement libraryManagement;
    @TempDir(cleanup = CleanupMode.NEVER)
    private File pluginFolder;
    private AuthPlugin plugin;

    @BeforeEach
    public void setup() {
        plugin = AuthPlugin.instance();
    }

    @Test
    public void shouldInitializeAuthPlugin() {
        AuthPlugin plugin = new BaseAuthPlugin(audienceProvider, DUMMY_VERSION, pluginFolder, serverCore, libraryManagement);

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
    public void shouldHaveValidBucketSize() {
        int expectedAuthenticationStepCount = 9;
        assertEquals(plugin.getAuthenticationStepFactoryBucket().getList().size(), expectedAuthenticationStepCount,
                "Plugin should have only '" + expectedAuthenticationStepCount + "' authentication steps. Perhaps new authentication step was added?");
        assertEquals(plugin.getAuthenticationTaskBucket().getTasks().size(), 3);
    }

    @Test
    public void shouldHaveDriver() {
        File cacheDriverFile = plugin.getConfig().getDatabaseConfiguration().getCacheDriverPath();
        assertTrue(cacheDriverFile.exists());
    }
}
