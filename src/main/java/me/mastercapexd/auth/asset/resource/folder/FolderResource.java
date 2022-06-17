package me.mastercapexd.auth.asset.resource.folder;

import me.mastercapexd.auth.asset.resource.DefaultResource;
import me.mastercapexd.auth.asset.resource.Resource;
import me.mastercapexd.auth.asset.resource.ResourceReader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FolderResource extends DefaultResource {
    private final ClassLoader classLoader;

    public FolderResource(String name, ClassLoader classLoader) {
        super(name, null);
        this.classLoader = classLoader;
    }

    public List<Resource> getResources() throws IOException, URISyntaxException {
        URI folderUri = classLoader.getResource(getName()).toURI();
        Path myPath;
        if (folderUri.getScheme().equals("jar")) {
            FileSystem fileSystem;
            try {
                fileSystem = FileSystems.newFileSystem(folderUri, Collections.emptyMap());
            } catch(FileSystemAlreadyExistsException ignored) {
                fileSystem = FileSystems.getFileSystem(folderUri);
            }
            myPath = fileSystem.getPath(getName());
        } else {
            myPath = Paths.get(folderUri);
        }
        try (Stream<Path> pathStream = Files.walk(myPath, 1)) {
            return pathStream.filter(path -> !path.toString().equals(getName())).map(path -> path.toString().replaceFirst("[\\/\\\\]", "")).map(resourcePath -> ResourceReader.defaultReader(classLoader, resourcePath).read()).collect(Collectors.toList());
        }
    }
}
