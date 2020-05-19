package org.delivery.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggingConfig {

    private static final String LOGGING_FILE_PATH = "/logging.properties";

    /**
     * Configs logging properties file src/main/resources/logging.properties.
     * Update with level (INFO, FINE, FINER, FINEST, SEVERE, WARNING, etc.):
     * .level= FINER
     * java.util.logging.ConsoleHandler.level = FINER
     */
    public static void config() {
        InputStream inputStream = LoggingConfig.class.getResourceAsStream(LOGGING_FILE_PATH);
        if (null != inputStream) {
            try {
                LogManager.getLogManager().readConfiguration(inputStream);
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE, "Failed init logging system", e);
            }
        }
    }
}
