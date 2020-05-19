package org.delivery.processor;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;

import java.util.Optional;

/**
 * Processor of the route plan for a single drone.
 */
@FunctionalInterface
public interface RouteProcessor {

    Optional<Monitoring> process(Delivery delivery);
}
