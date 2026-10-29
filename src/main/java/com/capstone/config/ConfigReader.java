// ABOUTME: Reads environment-specific properties from classpath resource files.
// ABOUTME: Selects qa.properties or uat.properties based on the -Denv system property.

package com.capstone.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    static {
        String env = System.getProperty("env", "qa");
        String fileName = env + ".properties";
        properties = new Properties();

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Property file not found: " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load property file: " + fileName, e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property not found: " + key);
        }
        return value.trim();
    }

    public static String getGorestBaseUrl() {
        return getProperty("gorest.baseUrl");
    }

    public static String getGorestToken() {
        return getProperty("gorest.token");
    }

    public static long getResponseTimeout() {
        return Long.parseLong(getProperty("response.timeout"));
    }
}
