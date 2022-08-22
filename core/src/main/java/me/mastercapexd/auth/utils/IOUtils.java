package me.mastercapexd.auth.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    private IOUtils() {
    }

    public static void streamToFile(InputStream stream, File file) throws IOException {
        streamToFile(stream, file, false, 1024 * 8);
    }

    public static void streamToFile(InputStream stream, File file, boolean reset) throws IOException {
        streamToFile(stream, file, reset, 1024 * 8);
    }

    public static void streamToFile(InputStream stream, File file, int bufferSize) throws IOException {
        streamToFile(stream, file, false, bufferSize);
    }

    public static void streamToFile(InputStream stream, File file, boolean reset, int bufferSize) throws IOException {
        if (!file.exists())
            file.createNewFile();
        if (reset) {
            if (!stream.markSupported())
                stream = new BufferedInputStream(stream);
            stream.reset();
        }
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while((bytesRead = stream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
    }
}
