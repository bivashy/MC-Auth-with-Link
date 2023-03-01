package com.bivashy.auth.api.asset.resource.impl;

import com.bivashy.auth.api.asset.resource.ResourceReader;

public class FolderResourceReader implements ResourceReader<FolderResource> {
    private final ClassLoader classLoader;
    private final String resourceName;

    public FolderResourceReader(ClassLoader classLoader, String resourceName) {
        this.classLoader = classLoader;
        this.resourceName = resourceName;
    }

    @Override
    public FolderResource read() {
        return new FolderResource(resourceName, classLoader);
    }
}
