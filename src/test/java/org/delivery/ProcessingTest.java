package org.delivery;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;
import org.delivery.processor.DeliveryProcessor;
import org.delivery.processor.RouteProcessor;
import org.delivery.processor.impl.DefaultDeliveryProcessor;
import org.delivery.processor.impl.DefaultRouteProcessor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ProcessingTest {

    private DeliveryProcessor processor = new DefaultDeliveryProcessor();
    private RouteProcessor routeProcessor = new DefaultRouteProcessor();

    @Test
    public void testValidProcess() throws Exception {

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

        Optional<List<Monitoring>> monitoring = processor.process(deliveries);
        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().size() == 2);
    }

    @Test
    public void testValidPosition() throws Exception {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        Optional<List<Monitoring>> monitoring = processor.process(deliveries);
        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().get(0).getPositions() != null &&
                        !monitoring.get().get(0).getPositions().isEmpty() &&
                        monitoring.get().get(0).getPositions().get(0).equals("(-2,4) direction W"));
    }

    @Test(expected = ValidationException.class)
    public void testInvalidDelivery() throws Exception {
        processor.process(null);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidDeliverySize() throws Exception {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAD");
        routes.add("AAIADAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        processor.process(deliveries);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidSymbol() throws Exception {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAHAD");
        routes.add("AFIA123");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        processor.process(deliveries);
    }

    @Test(expected = ValidationException.class)
    public void testOutOfArea() throws Exception {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAAAAAAAAAAAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        processor.process(deliveries);
    }

    @Test(expected = ValidationException.class)
    public void testMixedErrors() throws Exception {

        List<Delivery> deliveries = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        routes.add("DDDAIAAAAAAAAAAAD");
        routes.add("AAIFDAD");
        routes.add("AAIADAD");
        Delivery delivery = new Delivery("01", routes);
        deliveries.add(delivery);

        processor.process(deliveries);
    }

    @Test
    public void testValidRoute() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("AAAAIAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(-2,4) direction W"));
    }

    @Test
    public void testValidRoute2() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("ADAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(2,1) direction E"));
    }

    @Test
    public void testValidRoute3() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("AAAAAIAAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(-3,5) direction W"));
    }

    @Test
    public void testValidRoute4() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("IAAAIAAAAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(-3,-5) direction S"));
    }

    @Test
    public void testValidRoute5() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("AAAAAAAAAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(0,10) direction N"));
    }

    @Test
    public void testValidRoute6() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("DDAAAAAAAAAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(0,-10) direction S"));
    }

    @Test
    public void testValidRoute7() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("DAAAAAAAAAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(10,0) direction E"));
    }

    @Test
    public void testValidRoute8() throws Exception {

        List<String> routes = new ArrayList<>();
        routes.add("IAAAAAAAAAA");
        Delivery delivery = new Delivery("01", routes);

        Optional<Monitoring> monitoring = routeProcessor.process(delivery);

        assertTrue(monitoring != null && monitoring.isPresent() && monitoring.get().getPositions() != null &&
                !monitoring.get().getPositions().isEmpty() &&
                monitoring.get().getPositions().get(0).equals("(-10,0) direction W"));
    }
}
