package me.mastercapexd.auth.asset.resource.folder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import me.mastercapexd.auth.asset.resource.DefaultResource;
import me.mastercapexd.auth.asset.resource.Resource;
import me.mastercapexd.auth.asset.resource.ResourceReader;

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
			FileSystem fileSystem = null;
			try {
				fileSystem = FileSystems.newFileSystem(folderUri, Collections.emptyMap());
			} catch (FileSystemAlreadyExistsException ignored) {
				fileSystem = FileSystems.getFileSystem(folderUri);
			}
			myPath = fileSystem.getPath(getName());
		} else {
			myPath = Paths.get(folderUri);
		}
		return Files.walk(myPath, 1).filter(path -> !path.toString().equals(getName()))
				.map(path -> path.toString().substring(1))
				.map(resourcePath -> ResourceReader.defaultReader(classLoader, resourcePath).read())
				.collect(Collectors.toList());
	}
}
