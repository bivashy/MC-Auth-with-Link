package com.bivashy.auth.api.asset.resource.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bivashy.auth.api.asset.resource.Resource;
import com.bivashy.auth.api.asset.resource.ResourceReader;

public class FolderResource extends DefaultResource {
    private final ClassLoader classLoader;

    public FolderResource(String name, ClassLoader classLoader) {
        super(name, null);
        this.classLoader = classLoader;
    }

    public List<Resource> getResources() throws IOException, URISyntaxException {
        URL folderUrl = classLoader.getResource(getName());
        if (folderUrl == null)
            throw new NullPointerException("Folder resource not found: " + getName());
        URI folderUri = folderUrl.toURI();
        Path myPath;
        if (folderUri.getScheme().equals("jar")) {
            FileSystem fileSystem;
            try {
                fileSystem = FileSystems.newFileSystem(folderUri, Collections.emptyMap());
            } catch(FileSystemAlreadyExistsException ignored) {
                fileSystem = FileSystems.getFileSystem(folderUri);
            }
            myPath = fileSystem.getPath(getName());
            fileSystem.close();
        } else {
            myPath = Paths.get(folderUri);
        }
        try (Stream<Path> pathStream = Files.walk(myPath, 1)) {
            return pathStream.filter(path -> !path.toString().equals(getName()))
                    .map(path -> path.toString().replaceAll("^[/\\\\]", ""))
                    .map(resourcePath -> ResourceReader.defaultReader(classLoader, resourcePath).read())
                    .collect(Collectors.toList());
        }
    }
}
