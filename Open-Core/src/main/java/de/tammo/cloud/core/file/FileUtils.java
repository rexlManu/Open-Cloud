/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtils {

    public static void copyDir(final File from, final File to) throws IOException {
        if (!Files.notExists(to.toPath())) {
            Files.createDirectories(to.toPath());
        }

        for (final File file : Objects.requireNonNull(from.listFiles())) {
            if (file.isDirectory()) {
                copyDir(file, new File(to.getAbsolutePath() + "//" + file.getName()));
            } else {
                Files.copy(file.toPath(), new File(to.getAbsolutePath() + "//" + file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    public static void deleteDir(final File dir) throws IOException {
        if(!Files.exists(dir.toPath())) throw new FileNotFoundException();

        for (final File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                Files.delete(file.toPath());
            }
        }
    }

}
