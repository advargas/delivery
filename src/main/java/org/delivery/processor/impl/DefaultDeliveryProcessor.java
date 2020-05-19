package org.delivery.processor.impl;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;
import org.delivery.processor.DeliveryProcessor;
import org.delivery.processor.RouteProcessor;
import org.delivery.validation.DeliveryValidator;
import org.delivery.validation.impl.DeliverySizeValidator;
import org.delivery.validation.impl.InvalidSymbolValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class DefaultDeliveryProcessor implements DeliveryProcessor {

    private final static Logger LOGGER = Logger.getLogger(DefaultDeliveryProcessor.class.getName());

    @Override
    public Optional<List<Monitoring>> process(List<Delivery> deliveries) throws Exception {

        // Validate deliveries
        if (validate(deliveries)) {

            LOGGER.info("Processing deliveries concurrently ...");

            // Initiates a thread pool with the number of groups (workers)
            ExecutorService executorService = Executors.newFixedThreadPool(deliveries.size());

            // Adds the tasks
            List<DefaultRouteProcessor> callableTasks = new ArrayList<>();

            for (Delivery delivery : deliveries) {
                callableTasks.add(new DefaultRouteProcessor(delivery));
            }

            // Invokes all workers and awaits for the response asynchronously
            List<Future<Monitoring>> futures = executorService.invokeAll(callableTasks);

            List<Monitoring> monitors = new ArrayList<>();
            for (Future<Monitoring> future : futures) {
                monitors.add(future.get());
            }

            // Shutdowns the ExecutorService
            executorService.shutdown();

            return Optional.of(monitors);
        }
        return Optional.empty();
    }

    private boolean validate(List<Delivery> deliveries) throws ValidationException {

        if (deliveries == null || deliveries.isEmpty()) {
            throw new ValidationException("Set of deliveries is empty");
        }

        final List<DeliveryValidator> validators = new ArrayList<>();
        validators.add(new DeliverySizeValidator());
        validators.add(new InvalidSymbolValidator());

        for (final DeliveryValidator validator : validators) {
            final Optional<List<String>> errors = validator.validate(deliveries);
            if (errors.isPresent() && !errors.get().isEmpty()) {
                throw new ValidationException(errors.get());
            }
        }
        return true;
    }
}
