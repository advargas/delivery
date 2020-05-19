package org.delivery.validation;

import org.delivery.model.Delivery;

import java.util.List;
import java.util.Optional;

/**
 * Validators contract.
 */
@FunctionalInterface
public interface DeliveryValidator {

    Optional<List<String>> validate(List<Delivery> deliveries);
}
