package me.mastercapexd.auth.asset.resource;

import com.ubivashka.functions.Castable;
import me.mastercapexd.auth.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Resource extends Castable<Resource> {
    String getName();

    InputStream getStream();

    default Resource write(File file) throws IOException {
        IOUtils.streamToFile(getStream(), file);
        return this;
    }

    default Resource write(Path path) throws IOException {
        Files.copy(getStream(), path);
        return this;
    }

    default Resource close() throws IOException {
        getStream().close();
        return this;
    }
}
