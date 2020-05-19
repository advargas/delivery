package org.delivery;

import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;
import org.delivery.utils.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class FileUtilsTest {

    @Test
    public void testValidFolder() throws IOException {
        List<Delivery> deliveries = FileUtils.readDeliveries("restaurant1");
        assertTrue(deliveries != null && !deliveries.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testInvalidFolder() throws IOException {
        FileUtils.readDeliveries("folder");
    }

    @Test
    public void testWriteMonitoring() throws IOException {
        Path tempFolder = Paths.get("files" + File.separator + "temp");
        if (!Files.exists(tempFolder)) {
            Files.createDirectory(tempFolder);
        }

        List<Monitoring> monitoring = new ArrayList<>();
        List<String> positions = new ArrayList<>();
        positions.add("(-2,4) direction W");
        positions.add("(-1,-1) direction W");
        positions.add("(-1,3) direction E");

        Monitoring monitoring1 = new Monitoring("01", positions);
        monitoring.add(monitoring1);

        FileUtils.writeMonitoring("temp", monitoring);

        assertTrue(Files.exists(Paths.get("files" + File.separator + "temp" + File.separator + "output")));
        deleteFolder(Paths.get("files" + File.separator + "temp"));
    }

    private void deleteFolder(Path folder) throws IOException {
        if (Files.exists(folder)) {
            Files.walk(folder)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}
