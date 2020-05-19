package org.delivery.validation.impl;

import org.delivery.config.ApplicationConfig;
import org.delivery.model.Delivery;
import org.delivery.validation.DeliveryValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvalidSymbolValidator implements DeliveryValidator {

    private final static Pattern pattern = Pattern.compile("[aAiIdD]+");

    @Override
    public Optional<List<String>> validate(List<Delivery> deliveries) {

        List<String> errors = new ArrayList<>();
        if (deliveries != null && !deliveries.isEmpty()) {
            for (Delivery delivery : deliveries) {
                for (String route : delivery.getRoutes()) {
                    Matcher matcher = pattern.matcher(route);
                    if (!matcher.matches()) {
                        errors.add(String.format("Invalid route [%s], only AID characters are allowed", route));
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
