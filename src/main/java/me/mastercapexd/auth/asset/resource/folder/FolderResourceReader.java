package me.mastercapexd.auth.asset.resource.folder;

import me.mastercapexd.auth.asset.resource.ResourceReader;

public class FolderResourceReader implements ResourceReader<FolderResource> {
	private final ClassLoader classLoader;
	private final String resourceName;

	public FolderResourceReader(ClassLoader classLoader, String resourceName) {
		this.classLoader = classLoader;
		this.resourceName = resourceName;
	}

	public FolderResourceReader(String resourceName) {
		this(Thread.currentThread().getContextClassLoader(), resourceName);
	}

	@Override
	public FolderResource read() {
		return new FolderResource(resourceName, classLoader);
	}
}
