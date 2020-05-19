package org.delivery.validation.impl;

import org.delivery.config.ApplicationConfig;
import org.delivery.model.Delivery;
import org.delivery.validation.DeliveryValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator to check routes out of area.
 */
public class OutOfAreaValidator implements DeliveryValidator {

    private String wrongArea = "";

    public OutOfAreaValidator() {
        int maxAreaSteps = ApplicationConfig.getMaxAreaSteps();
        for(int i = 0; i <= maxAreaSteps; i++){
            wrongArea += "A";
        }
    }

    @Override
    public Optional<List<String>> validate(List<Delivery> deliveries) {

        List<String> errors = new ArrayList<>();
        if (deliveries != null && !deliveries.isEmpty()) {
            for (Delivery delivery : deliveries) {
                for (String route : delivery.getRoutes()) {
                    if (route.toUpperCase().contains(wrongArea)) {
                        errors.add(String.format("Invalid route [%s], drone out of area", route));
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
