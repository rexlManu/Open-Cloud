/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.masterapi.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@RequiredArgsConstructor
public class ModuleLoader {

    @Getter
    private ArrayList<Module> modules = new ArrayList<>();

    private final File moduleFolder;

    public static void main(String[] args) {
        try {
            new ModuleLoader(new File("folder")).loadModules();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void loadModules() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (Files.notExists(this.moduleFolder.toPath())) {
            Files.createDirectories(this.moduleFolder.toPath());
        }

        final File[] files = this.moduleFolder.listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".jar"));
        if (files == null) {
            return;
        }


        final URL[] urls = new URL[files.length];

        for (int i = 0; i < files.length; i++) {
            urls[i] = files[i].toURI().toURL();
            System.out.println(urls[i].toString());
        }

        final URLClassLoader classLoader = new URLClassLoader(urls);

        for (final File file : files) {
            final JarFile jarFile = new JarFile(file);
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    System.out.println(jarEntry.getName().replace("/", ".").substring(0, jarEntry.getName().length() - 6));
                    Class<?> targetClass = classLoader.loadClass(jarEntry.getName().replace("/", ".").substring(0, jarEntry.getName().length() - 6));
                    if (Module.class.isAssignableFrom(targetClass)) {
                        final Module module = (Module) targetClass.newInstance();
                        this.modules.add(module);
                    }
                }
            }
        }

        this.modules.forEach(Module::onLoad);
    }

}
