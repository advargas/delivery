package org.delivery;

import org.delivery.model.Delivery;
import org.delivery.validation.DeliveryValidator;
import org.delivery.validation.impl.DeliverySizeValidator;
import org.delivery.validation.impl.InvalidSymbolValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ValidationsTest {

    private final List<DeliveryValidator> validators = new ArrayList<>();

    @Before
    public void init() {
        validators.add(new DeliverySizeValidator());
        validators.add(new InvalidSymbolValidator());
    }

    @Test
    public void testFullValidRecord() {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        List<String> routes2 = new ArrayList<>();
        routes2.add("DAAIAAA");
        routes2.add("IAADAAI");
        routes2.add("AAIADAD");
        Delivery delivery2 = new Delivery("02", routes2);
        deliveries.add(delivery2);

        List<String> errors = validateFull(deliveries);
        System.out.println(errors);
        assertTrue(errors != null && errors.isEmpty());
    }

    private List<String> validateFull(List<Delivery> deliveries) {
        List<String> lstErrors = new ArrayList<>();
        for (final DeliveryValidator validator : validators) {
            final Optional<List<String>> errors = validator.validate(deliveries);
            if (errors.isPresent() && !errors.get().isEmpty()) {
                lstErrors.addAll(errors.get());
            }
        }
        return lstErrors;
    }
}
