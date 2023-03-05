package me.mastercapexd.auth.server.adventure;

import java.util.Arrays;
import java.util.Optional;

import com.bivashy.auth.api.config.message.server.ComponentDeserializer;
import com.bivashy.auth.api.server.message.ServerComponent;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public enum BaseComponentDeserializer implements ComponentDeserializer {
    PLAIN {
        private final PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();

        @Override
        public ServerComponent deserialize(String text) {
            return new BaseAdventureServerComponent(serializer.deserialize(text));
        }
    }, GSON {
        private final GsonComponentSerializer serializer = GsonComponentSerializer.gson();

        @Override
        public ServerComponent deserialize(String text) {
            return new BaseAdventureServerComponent(serializer.deserialize(text));
        }
    }, GSON_LEGACY {
        private final GsonComponentSerializer serializer = GsonComponentSerializer.colorDownsamplingGson();

        @Override
        public ServerComponent deserialize(String text) {
            return new BaseAdventureServerComponent(serializer.deserialize(text));
        }
    }, LEGACY_AMPERSAND {
        private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand().toBuilder().hexColors().build();

        @Override
        public ServerComponent deserialize(String text) {
            return new BaseAdventureServerComponent(serializer.deserialize(text));
        }
    }, LEGACY_SECTION {
        private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacySection().toBuilder().hexColors().build();

        @Override
        public ServerComponent deserialize(String text) {
            return new BaseAdventureServerComponent(serializer.deserialize(text));
        }
    }, MINIMESSAGE {
        private final MiniMessage serializer = MiniMessage.miniMessage();

        @Override
        public ServerComponent deserialize(String text) {
            return new BaseAdventureServerComponent(serializer.deserialize(text));
        }
    };

    public static Optional<BaseComponentDeserializer> findWithName(String name) {
        return Arrays.stream(BaseComponentDeserializer.values()).filter(value -> value.name().equals(name)).findFirst();
    }

    public abstract ServerComponent deserialize(String text);
}
