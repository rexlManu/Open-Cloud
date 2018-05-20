/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.file;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        if (!Files.exists(dir.toPath())) throw new FileNotFoundException();

        for (final File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                Files.delete(file.toPath());
            }
        }
    }

    public static void packZip(final Path folder, final Path zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile.toFile()); ZipOutputStream zos = new ZipOutputStream(fos)) {
            Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {

                public FileVisitResult visitFile(final Path file, final BasicFileAttributes basicFileAttributes) throws IOException {
                    zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes basicFileAttributes) throws IOException {
                    zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

}