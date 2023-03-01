package com.bivashy.auth.api.asset.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import com.ubivashka.functions.Castable;

public interface Resource extends Castable<Resource> {
    String getName();

    InputStream getStream();

    default Resource write(File file) throws IOException {
        write(file.toPath());
        return this;
    }

    default Resource write(Path path) throws IOException {
        Files.copy(getStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return this;
    }

    default Resource close() throws IOException {
        getStream().close();
        return this;
    }
}
