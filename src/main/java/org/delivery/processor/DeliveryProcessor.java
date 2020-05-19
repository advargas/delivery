package org.delivery.processor;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;
import org.delivery.validation.DeliveryValidator;
import org.delivery.validation.impl.DeliverySizeValidator;
import org.delivery.validation.impl.InvalidSymbolValidator;
import org.delivery.validation.impl.OutOfAreaValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Processor for the full set of drones.
 */
@FunctionalInterface
public interface DeliveryProcessor {

    default boolean validate(List<Delivery> deliveries) throws ValidationException {

        if (deliveries == null || deliveries.isEmpty()) {
            throw new ValidationException("Set of deliveries is empty");
        }

        final List<DeliveryValidator> validators = new ArrayList<>();
        validators.add(new DeliverySizeValidator());
        validators.add(new InvalidSymbolValidator());
        validators.add(new OutOfAreaValidator());

        for (final DeliveryValidator validator : validators) {
            final Optional<List<String>> errors = validator.validate(deliveries);
            if (errors.isPresent() && !errors.get().isEmpty()) {
                throw new ValidationException(errors.get());
            }
        }
        return true;
    }

    Optional<List<Monitoring>> process(List<Delivery> deliveries) throws Exception;
}
