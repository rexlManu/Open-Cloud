/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.document;

import com.google.common.reflect.ClassPath;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;

public class DocumentHandler {

    @Getter
    private final ArrayList<DocumentFile> files = new ArrayList<>();

    public DocumentHandler(final String path) {
        try {
            for (final ClassPath.ClassInfo classInfo : ClassPath.from(this.getClass().getClassLoader()).getTopLevelClasses(path)) {
                final Class fileClass = Class.forName(classInfo.getName());
                if (fileClass.getSuperclass() == DocumentFile.class) {
                    this.files.add((DocumentFile) fileClass.newInstance());
                }
            }
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        this.loadFiles();
    }

    private void loadFiles() {
        this.files.forEach(documentFile -> {
            try {
                documentFile.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveFiles() {
        this.files.forEach(documentFile -> {
            try {
                documentFile.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
