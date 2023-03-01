package com.bivashy.auth.api.asset.resource;

import com.bivashy.auth.api.asset.resource.impl.DefaultResource;

public interface ResourceReader<T extends Resource> {
    static ResourceReader<Resource> defaultReader(ClassLoader classLoader, String resourceName) {
        return () -> new DefaultResource(resourceName, classLoader.getResourceAsStream(resourceName));
    }

    T read();
}
