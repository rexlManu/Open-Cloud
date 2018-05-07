/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.tammo.cloud.core.document.DocumentFile;
import de.tammo.cloud.master.Master;
import de.tammo.cloud.master.network.wrapper.WrapperMeta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class WrapperConfig extends DocumentFile {

    public WrapperConfig() {
        super(new File("Open-Master//config//wrappers.json"));
    }

    protected void load() throws IOException {
        try (final BufferedReader reader = Files.newBufferedReader(this.file.toPath())) {
            Master.getMaster().getNetworkHandler().createWrappers(new Gson().fromJson(reader, new TypeToken<ArrayList<WrapperMeta>>(){}.getType()));
        }
    }

    protected void save() throws IOException {
        try (final BufferedWriter writer = Files.newBufferedWriter(this.file.toPath())){
            final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            writer.write(gson.toJson(Master.getMaster().getNetworkHandler().getWrapperMetas()));
        }
    }
}
