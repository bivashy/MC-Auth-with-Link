package me.mastercapexd.auth.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class DownloadUtil {
    private DownloadUtil() {
    }

    public static void downloadFile(URL url, File destinationFile) throws IOException {
        File absoluteFile = destinationFile.getAbsoluteFile();
        if (!destinationFile.exists()) {
            File parentFile = absoluteFile.getParentFile();
            if (parentFile != null && !parentFile.exists())
                parentFile.mkdirs();
            absoluteFile.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(absoluteFile);
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        fileOutputStream.close();
    }

    public static boolean checkSum(URL mappedUrl, String fileCheckSum) {
        return checkSum(mappedUrl, fileCheckSum, false);
    }

    public static boolean checkSum(URL mappedUrl, String fileCheckSum, boolean catchInvalidUrl) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mappedUrl.openStream()));
            String result;
            while((result = reader.readLine()) != null) {
                stringBuilder.append(result);
            }
        } catch(IOException e) {
            if (e instanceof FileNotFoundException && catchInvalidUrl)
                return false;
            e.printStackTrace();
            return false;
        }
        return stringBuilder.toString().equals(fileCheckSum);
    }
}
