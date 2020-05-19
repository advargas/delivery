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
import java.util.logging.Logger;

public class DefaultDeliveryProcessor implements DeliveryProcessor {

    private final static Logger LOGGER = Logger.getLogger(DefaultDeliveryProcessor.class.getName());

    @Override
    public Optional<List<Monitoring>> process(List<Delivery> deliveries) throws ValidationException {

        // Validate deliveries
        if (validate(deliveries)) {
            RouteProcessor routeProcessor = new DefaultRouteProcessor();
            List<Monitoring> monitorings = new ArrayList<>();

            LOGGER.info("Processing deliveries ...");
            for (Delivery delivery : deliveries) {
                Optional<Monitoring> monitoring = routeProcessor.process(delivery);

                if (monitoring.isPresent()) {
                    monitorings.add(monitoring.get());
                } else {
                    throw new ValidationException(String.format("Error creating monitoring report for drone %s",
                            delivery.getDrone()));
                }
            }
            return Optional.of(monitorings);
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
