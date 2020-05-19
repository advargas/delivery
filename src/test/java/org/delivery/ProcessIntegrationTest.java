package org.delivery;

import org.delivery.exception.ValidationException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class ProcessIntegrationTest {

    @Test
    public void testValidProcess() throws Exception {

        DeliveryApp.processDeliveries("restaurant2");
        Path outputFolder = Paths.get("files" + File.separator+ "restaurant2" + File.separator + "output");
        assertTrue(Files.exists(outputFolder));
    }

    @Test(expected = ValidationException.class)
    public void testInvalidIDeliveries() throws Exception {
        DeliveryApp.processDeliveries("restaurant3");
    }

    @Test(expected = IOException.class)
    public void testInvalidInputFolder() throws Exception {
        DeliveryApp.processDeliveries("restaurant4");
    }

}
