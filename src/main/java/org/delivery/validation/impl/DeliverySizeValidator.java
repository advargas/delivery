package org.delivery.validation.impl;

import org.delivery.config.ApplicationConfig;
import org.delivery.model.Delivery;
import org.delivery.validation.DeliveryValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeliverySizeValidator implements DeliveryValidator {

    @Override
    public Optional<List<String>> validate(List<Delivery> deliveries) {

        List<String> errors = new ArrayList<>();
        int maxDrones = ApplicationConfig.getMaxDrones();
        int maxDeliveries = ApplicationConfig.getMaxDeliveries();

        if (deliveries != null && !deliveries.isEmpty()) {
            if (deliveries.size() > maxDrones) {
                errors.add(String.format("A maximum number of %d drones is allowed", maxDrones));
            } else {
               for (Delivery delivery : deliveries) {
                   if (delivery != null && delivery.getRoutes() != null && !delivery.getRoutes().isEmpty()) {
                       if (delivery.getRoutes().size() > maxDeliveries) {
                           errors.add(String.format("A maximum number of %d deliveries is allowed per dron", maxDeliveries));
                       }
                   } else {
                       errors.add("Set of routes in delivery file is empty");
                   }
               }
            }
        } else {
            errors.add("Set of deliveries is empty");
        }
        if (errors.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(errors);
        }
    }
}
