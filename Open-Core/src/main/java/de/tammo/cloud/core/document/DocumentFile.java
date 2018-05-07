/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.core.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class DocumentFile {

    protected final File file;

    public DocumentFile(final File file) {
        this.file = file;

        if (Files.notExists(this.file.getParentFile().toPath())) {
            try {
                Files.createDirectories(this.file.getParentFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Files.notExists(this.file.toPath())) {
            try {
                this.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void load() throws IOException;

    protected abstract void save() throws IOException;

}
