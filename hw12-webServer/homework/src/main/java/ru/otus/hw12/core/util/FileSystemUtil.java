package ru.otus.hw12.core.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@UtilityClass
public class FileSystemUtil {

    public static String toAbsolutePath(String uri) {
        String path = null;
        File file = new File(String.format("./%s", uri));
        if (file.exists()) {
            path = URLDecoder.decode(file.toURI().getPath(), StandardCharsets.UTF_8);
        }
        if (path == null) {
            path = Optional.ofNullable(FileSystemUtil.class.getClassLoader().getResource(uri))
                    .orElseThrow(() -> new RuntimeException(String.format("Resource \"%s\" not found", uri)))
                    .toExternalForm();
        }
        return path;
    }
}
