package me.mastercapexd.auth.asset.resource;

public interface ResourceReader<T extends Resource> {
	T read();

	static ResourceReader<Resource> defaultReader(ClassLoader classLoader, String resourceName) {
		return new ResourceReader<Resource>() {
			@Override
			public Resource read() {
				return new DefaultResource(resourceName, classLoader.getResourceAsStream(resourceName));
			}
		};
	}

	static ResourceReader<Resource> defaultReader(String resourceName) {
		return defaultReader(Thread.currentThread().getContextClassLoader(), resourceName);
	}
}
