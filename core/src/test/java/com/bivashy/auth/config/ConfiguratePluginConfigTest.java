package com.bivashy.auth.config;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;

import org.junit.jupiter.api.*;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import com.bivashy.auth.api.AuthPlugin;
import com.bivashy.auth.api.config.link.command.LinkCommandPathSettings;
import com.bivashy.auth.api.config.message.Messages;
import com.bivashy.auth.api.config.message.server.ServerMessages;
import com.bivashy.auth.api.database.DatabaseConnectionProvider;
import com.bivashy.auth.api.server.message.ServerComponent;
import com.bivashy.configuration.configurate.holder.ConfigurationNodeHolder;
import com.bivashy.configuration.holder.ConfigurationSectionHolder;

import me.mastercapexd.auth.config.PluginConfigTemplate;
import me.mastercapexd.auth.server.adventure.BaseComponentDeserializer;

public class ConfiguratePluginConfigTest extends PluginConfigTemplate {
    private static final String CONFIGURATION_FILE_PATH = "/example-config.yml";

    public ConfiguratePluginConfigTest() {
        super(AuthPlugin.instance());
    }

    @Override
    protected ConfigurationSectionHolder createConfiguration(AuthPlugin plugin) {
        try {
            return new ConfigurationNodeHolder(YamlConfigurationLoader.builder()
                    .source(() -> new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(CONFIGURATION_FILE_PATH))))
                    .build()
                    .load());
        } catch(ConfigurateException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void testExampleConfigurationValues() {
        assertFalse(isAutoMigrateConfigEnabled());

        assertNotNull(getStorageType());
        assertEquals(DatabaseConnectionProvider.MYSQL, getStorageType());

        assertNotNull(getBossBarSettings());
        assertFalse(getBossBarSettings().isEnabled());

        assertNotNull(getAllowedCommands());
        assertEquals(6, getAllowedCommands().size());

        assertEquals(Duration.ofHours(4).toMillis(), getSessionDurability());

        LinkCommandPathSettings firstCommandPath = getTelegramSettings().getCommandPaths().getCommandPath("first");
        assertEquals(firstCommandPath.getCommandPath(), "/first");
        String[] aliases = firstCommandPath.getAliases();
        assertArrayEquals(firstCommandPath.getAliases(), new String[]{"/first-alias"});
        assertArrayEquals(firstCommandPath.getCommandPaths(), new String[]{"/first-alias", "/first"});
    }

    @Test
    void testExampleConfigurationMessages() {
        ServerMessages messages = getServerMessages();
        assertNotNull(messages);

        assertEquals(BaseComponentDeserializer.LEGACY_AMPERSAND, messages.getDeserializer());

        assertNull(messages.getStringMessage("null-message", (String) null));
        assertNull(messages.getSubMessages("null-submessage"));
        assertNotNull(messages.getMessage("test-message"));
        assertEquals("Hello", messages.getStringMessage("test-message"));

        ServerComponent simpleColoredMessage = messages.getMessage("colored-message");
        assertEquals("{\"color\":\"red\",\"text\":\"test\"}", simpleColoredMessage.jsonText());

        Messages<ServerComponent> submessages = messages.getSubMessages("test-submessage");
        assertNotNull(submessages);
        assertNull(submessages.getStringMessage("null-message", (String) null));
        assertEquals("hi", submessages.getStringMessage("message"));

        String jsonMessage = "{\"extra\":[{\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/test\"},\"text\":\"Clickable\"},\" and \",{\"hoverEvent\":{\"action\":\"show_text\",\"contents\":\"Hi\"}," +
                "\"extra\":[{\"color\":\"#FF0000\",\"text\":\"r\"},{\"color\":\"#FFDA00\",\"text\":\"a\"},{\"color\":\"#48FF00\",\"text\":\"i\"},{\"color\":\"#00FF91\",\"text\":\"n\"},{\"color\":\"#0091FF\",\"text\":\"b\"}" +
                ",{\"color\":\"#4800FF\",\"text\":\"o\"},{\"color\":\"#FF00DA\",\"text\":\"w\"}],\"text\":\"Hoverable with \"}],\"text\":\"\"}";
        assertEquals(jsonMessage, messages.getMessage("complex-minimessage").jsonText());
        assertEquals("Clickable and Hoverable with rainbow", messages.getMessage("complex-minimessage").plainText());
    }
}
