package org.delivery.processor;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;

import java.util.Optional;

@FunctionalInterface
public interface RouteProcessor {

    Optional<Monitoring> process(Delivery delivery) throws ValidationException;
}
