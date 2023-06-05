package com.bivashy.auth.asset.resource;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;

import com.bivashy.auth.api.asset.resource.Resource;
import com.bivashy.auth.api.asset.resource.ResourceReader;
import com.bivashy.auth.api.asset.resource.impl.FolderResource;
import com.bivashy.auth.api.asset.resource.impl.FolderResourceReader;

public abstract class ResourcesTest {
    private final ClassLoader classLoader;

    public ResourcesTest(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Test
    public void shouldBeNotNullFile() {
        ResourceReader<Resource> resourceReader = ResourceReader.defaultReader(classLoader, "hello.yml");
        Resource resource = resourceReader.read();
        assertNotNull(resource.getStream(), "Expected nonnull stream when reading file hello.yml");
        assertNotNull(resource.getName(), "Expected nonnull name when reading file hello.yml");
    }

    @Test
    public void shouldReturnValidResources() throws IOException, URISyntaxException {
        FolderResourceReader folderResourceReader = new FolderResourceReader(classLoader, "test");
        testFolderResourceReader(folderResourceReader, Arrays.asList("test/bar.txt", "test/foo.txt"));

        FolderResourceReader subfolderResourceReader = new FolderResourceReader(classLoader, "test/subfolder");
        testFolderResourceReader(subfolderResourceReader, Arrays.asList("test/subfolder/text.txt"));
    }

    private void testFolderResourceReader(FolderResourceReader reader, List<String> expectedNames) throws IOException, URISyntaxException {
        FolderResource folderResource = reader.read();
        for (Resource resource : folderResource.getResources())
            assertNotNull(resource.getStream(), "Expected not null from resource " + resource.getName());

        List<String> resourceNames = folderResource.getResources().stream().map(Resource::getName).sorted().collect(Collectors.toList());
        assertEquals(expectedNames.size(), folderResource.getResources().size(),
                "Invalid folderResource resources size in " + resourceNames);

        Collections.sort(expectedNames); // Sort for having same order

        assertEquals(expectedNames, resourceNames, "Invalid resource names");
    }
}
