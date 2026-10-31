package org.flowdigger.parsers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class TestResourceDirectoryProvider {

    private TestResourceDirectoryProvider() {
    }

    public static List<File> getSqlFilesInDirectory(final File directory) {
        try {
            return Files.walk(directory.toPath())
                    .filter(path -> path.toString().endsWith(".sql"))
                    .map(Path::toFile)
                    .sorted(Comparator.comparing(File::getAbsolutePath))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            return List.of();
        }
    }
}

