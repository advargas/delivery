package org.delivery;

import org.delivery.model.Delivery;
import org.delivery.validation.DeliveryValidator;
import org.delivery.validation.impl.DeliverySizeValidator;
import org.delivery.validation.impl.InvalidSymbolValidator;
import org.delivery.validation.impl.OutOfAreaValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ValidationsTest {

    private final List<DeliveryValidator> validators = new ArrayList<>();

    @Before
    public void init() {
        validators.add(new DeliverySizeValidator());
        validators.add(new InvalidSymbolValidator());
        validators.add(new OutOfAreaValidator());
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
        assertTrue(errors != null && errors.isEmpty());
    }

    @Test
    public void testInvalidDeliverySize() {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAD");
        routes.add("AAIADAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        List<String> errors = validateFull(deliveries);
        assertTrue(errors != null && !errors.isEmpty() && containsError(errors, "A maximum number of"));
    }

    @Test
    public void testInvalidSymbol() {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAHAD");
        routes.add("AFIA123");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        List<String> errors = validateFull(deliveries);
        assertTrue(errors != null && !errors.isEmpty() && containsError(errors, "only AID characters are allowed"));
    }

    @Test
    public void testOutOfArea() {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAAAAAAAAAAAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        List<String> errors = validateFull(deliveries);
        assertTrue(errors != null && !errors.isEmpty() && containsError(errors, "drone out of area"));
    }

    @Test
    public void testMixedErrors() {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAAAAAAAAAAAD");
        routes.add("AAIFDAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        List<String> errors = validateFull(deliveries);
        System.out.println(errors);
        assertTrue(errors != null && !errors.isEmpty() && containsError(errors, "A maximum number of") &&
                        containsError(errors, "only AID characters are allowed") &&
                        containsError(errors, "drone out of area"));
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

    private boolean containsError(List<String> errors, String text) {
        for (String error : errors) {
            if (error.contains(text)) {
                return true;
            }
        }
        return false;
    }
}
