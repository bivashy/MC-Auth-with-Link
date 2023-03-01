package com.bivashy.auth.api.server.message;

import com.bivashy.auth.api.server.player.ServerPlayer;

public interface SelfHandledServerComponent extends ServerComponent {
    SelfHandledServerComponent NULL_COMPONENT = new SelfHandledServerComponent() {
        @Override
        public void send(ServerPlayer player) {
        }

        @Override
        public String jsonText() {
            return null;
        }

        @Override
        public String legacyText() {
            return null;
        }

        @Override
        public String plainText() {
            return null;
        }
    };

    void send(ServerPlayer player);
}
