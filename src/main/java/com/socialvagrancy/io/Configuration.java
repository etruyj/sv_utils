//======================================================
// Configuration
//      Description:
//          Static, generic configuration loader that reads YAML into
//          a strongly-typed config object (defined at load time) and 
//          exposes it globally via type-safe access.
//
// Created by Sean Snyder
//======================================================
package com.socialvagrancy.utils.io;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

public final class Configuration<T> {
    private static Object config;
    private static Class<?> configType;

    private Configuration() {}

    public static <T> void create(String path, T obj, Class<T> clazz) throws Exception {
        Yaml yaml = new Yaml();

        try (FileWriter writer = new FileWriter(path)) {
            yaml.dump(obj, writer);
        }
    }

    public static synchronized <T> void load(String path, Class<T> clazz) throws Exception {
        if (config != null) {
            throw new IllegalStateException("Config already loaded.");
        }

        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream(path)) {
            config = yaml.loadAs(in, clazz);
            configType = clazz;
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> T get() {
        if (config == null) {
            throw new IllegalStateException("Configuration not loaded.");
        }
        return (T) config;
    }

    public static synchronized boolean isLoaded() {
        return config != null;
    }
}

