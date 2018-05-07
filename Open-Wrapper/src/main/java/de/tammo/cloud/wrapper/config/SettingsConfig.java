/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.tammo.cloud.core.document.DocumentFile;
import de.tammo.cloud.wrapper.Wrapper;
import de.tammo.cloud.wrapper.config.settings.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SettingsConfig extends DocumentFile {

    public SettingsConfig() {
        super(new File("Open-Wrapper//config//settings.json"));
    }

    protected void load() throws IOException {
        try (final BufferedReader reader = Files.newBufferedReader(this.file.toPath())){
            Wrapper.getWrapper().setSettings(new Gson().fromJson(reader, Settings.class));
            Wrapper.getWrapper().getLogger().info("Set Settings!");
        }
    }

    protected void save() throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(this.file.toPath())){
            writer.write(new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(Wrapper.getWrapper().getSettings()));
        }
    }
}
