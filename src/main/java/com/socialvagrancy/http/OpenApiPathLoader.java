//===================================================================
// OpenApiPathLoader
//      Description:
//          This class loads an OpenAPI YAML file from a provided file path
//          and maps operationIds to endpoint paths. It provides a static
//          get() method to return full URLs, and supports basic parameter
//          replacement with standard Java string methods.
//
// Created by etruyj
//===================================================================

package com.socialvagrancy.utils.http;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class OpenApiPathLoader {
    private static final Map<String, String> operationMap = new HashMap<>();

    public static void load(String pathToYaml) {
        operationMap.clear();

        try (InputStream inputStream = new FileInputStream(pathToYaml)) {
            Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
            Map<String, Object> openApiDoc = yaml.load(inputStream);

            Map<String, Object> paths = (Map<String, Object>) openApiDoc.get("paths");

            for (Map.Entry<String, Object> entry : paths.entrySet()) {
                String path = entry.getKey();
                Map<String, Object> methods = (Map<String, Object>) entry.getValue();

                for (Map.Entry<String, Object> methodEntry : methods.entrySet()) {
                    Map<String, Object> operation = (Map<String, Object>) methodEntry.getValue();
                    String operationId = (String) operation.get("operationId");

                    if (operationId != null) {
                        operationMap.put(operationId, path);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load OpenAPI YAML from " + pathToYaml + ": " + e.getMessage(), e);
        }
    }

    public static String get(String baseUrl, String operationId) {
        String path = operationMap.get(operationId);
        if (path == null) {
            throw new IllegalArgumentException("Operation ID not found: " + operationId);
        }

        return baseUrl.endsWith("/") ? baseUrl + path.substring(1) : baseUrl + path;
    }
}

