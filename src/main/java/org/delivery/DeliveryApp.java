package org.delivery;

import org.delivery.config.ApplicationConfig;
import org.delivery.config.LoggingConfig;
import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;
import org.delivery.processor.DeliveryProcessor;
import org.delivery.processor.impl.DefaultDeliveryProcessor;
import org.delivery.utils.FileUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DeliveryApp {

    private final static Logger LOGGER = Logger.getLogger(DeliveryApp.class.getName());

    private static final String DEFAULT_RESTAURANT = "restaurant1";

    private static void processDeliveries(String restaurant) throws ValidationException, IOException {

        // Read deliveries
        List<Delivery> deliveries = FileUtils.readDeliveries(restaurant);

        // Process deliveries
        DeliveryProcessor processor = new DefaultDeliveryProcessor();
        Optional<List<Monitoring>> monitorings = processor.process(deliveries);

        if (monitorings.isPresent()) {
            // Write monitoring positions
            FileUtils.writeMonitoring(restaurant, monitorings.get());
        } else {
            LOGGER.severe("Error processing delivery routes for restaurant " + restaurant);
        }
    }

    public static void main(String[] args) {

        // Logging configuration
        LoggingConfig.config();

        // Read restaurant to process
        String restaurant = DEFAULT_RESTAURANT;
        if (args != null && args.length > 0) {
            restaurant = args[0];
        }

        try {
            // Read application properties
            ApplicationConfig.loadProperties();

            // Process deliveries
            processDeliveries(restaurant);

        } catch (ValidationException e) {
            LOGGER.severe("Validation errors in delivery files");
            e.getErrors().forEach(err -> LOGGER.severe(err));
            e.printStackTrace();

        } catch (IOException e) {
            LOGGER.severe("Error reading delivery input files in restaurant: " + restaurant);
            e.printStackTrace();
        }
    }
}
