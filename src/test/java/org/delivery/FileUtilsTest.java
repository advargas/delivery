package org.delivery;

import org.delivery.model.Delivery;
import org.delivery.utils.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilsTest {

    @Test
    public void testValidFolder() throws IOException {
        List<Delivery> deliveries = FileUtils.readDeliveries("restaurant1");

        for (Delivery de : deliveries) {
            System.out.println(de.getDrone());
            System.out.println(de.getRoutes());
        }
        assertTrue(deliveries != null && !deliveries.isEmpty());
    }
}
