package me.mastercapexd.auth.proxy.message;

import me.mastercapexd.auth.proxy.player.ProxyPlayer;

public interface SelfHandledProxyComponent extends ProxyComponent {
    SelfHandledProxyComponent NULL_COMPONENT = new SelfHandledProxyComponent() {
        @Override
        public void send(ProxyPlayer player) {
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

    void send(ProxyPlayer player);
}
