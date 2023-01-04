package me.mastercapexd.auth.asset.resource;

public interface ResourceReader<T extends Resource> {
    static ResourceReader<Resource> defaultReader(ClassLoader classLoader, String resourceName) {
        return () -> new DefaultResource(resourceName, classLoader.getResourceAsStream(resourceName));
    }

    T read();
}
