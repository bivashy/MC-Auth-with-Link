package com.bivashy.auth.api.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bivashy.auth.api.config.server.ConfigurationServer;

public enum FillType {
    RANDOM {
        @Override
        public List<ConfigurationServer> shuffle(List<ConfigurationServer> servers) {
            List<ConfigurationServer> modifableServers = new ArrayList<>(servers);
            Collections.shuffle(modifableServers);
            return modifableServers;
        }

    }, GRADUALLY {
        @Override
        public List<ConfigurationServer> shuffle(List<ConfigurationServer> servers) {
            return servers;
        }

    };

    public abstract List<ConfigurationServer> shuffle(List<ConfigurationServer> servers);
}
