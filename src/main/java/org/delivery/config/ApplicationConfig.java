package org.delivery.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Manage application properties in file resources/application.properties
 */
public class ApplicationConfig {

    private final static String PROPS_FILE_NAME = "application.properties";

    private final static String MAX_DRONES_PROP = "max_drones";
    private final static String MAX_DELIVERIES_PROP = "max_deliveries";
    private final static String MAX_AREA_STEPS_PROP = "max_area_steps";

    private final static int DEFAULT_MAX_DRONES = 20;
    private final static int DEFAULT_MAX_DELIVERIES = 3;
    private final static int DEFAULT_MAX_AREA_STEPS = 10;

    private static Properties properties;

    public static void loadProperties() throws IOException {
        try (InputStream inputStream = ApplicationConfig.class.getClassLoader().getResourceAsStream(PROPS_FILE_NAME)) {
            properties = new Properties();
            properties.load(inputStream);
        }
    }

    public static int getMaxDrones() {
        if (properties != null) {
            return Integer.valueOf(properties.get(MAX_DRONES_PROP).toString());
        }
        return DEFAULT_MAX_DRONES;
    }

    public static int getMaxDeliveries() {
        if (properties != null) {
            return Integer.valueOf(properties.get(MAX_DELIVERIES_PROP).toString());
        }
        return DEFAULT_MAX_DELIVERIES;
    }

    public static int getMaxAreaSteps() {
        if (properties != null) {
            return Integer.valueOf(properties.get(MAX_AREA_STEPS_PROP).toString());
        }
        return DEFAULT_MAX_AREA_STEPS;
    }
}
