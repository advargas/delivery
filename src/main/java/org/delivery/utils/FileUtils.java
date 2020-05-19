package org.delivery.utils;

import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileUtils {

    private final static Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    private final static String BASE_PATH = "files" + File.separator;
    private final static Pattern pattern = Pattern.compile("in(\\d+).txt");

    public static List<Delivery> readDeliveries(String folder) throws IOException {
        LOGGER.finer("Reading routes from folder: " + folder);

        List<Delivery> deliveries = new ArrayList<>();
        List<Path> files = Files.list(Paths.get(BASE_PATH + folder))
                            .filter(Files::isRegularFile)
                            .collect(Collectors.toList());

        for (Path file : files) {
            if (file.getFileName().toString().endsWith(".txt")) {
                String inputName = extractDroneName(file.getFileName().toString());
                Delivery delivery = new Delivery(inputName, Files.readAllLines(file));
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }

    private static String extractDroneName(String inputName) {
        Matcher matcher = pattern.matcher(inputName);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void writeMonitoring(String folder, List<Monitoring> monitoring) throws IOException {
        LOGGER.info("Writing results to folder: " + folder);

        Path baseFolder = Paths.get(BASE_PATH + folder + File.separator + "output");
        if (!Files.exists(baseFolder)) {
            Files.createDirectory(baseFolder);
        }

        for (Monitoring record : monitoring) {
            String outputName = BASE_PATH + folder + File.separator + "output" + File.separator + "out" + record.getDrone() + ".txt";
            Files.write(Paths.get(outputName), record.getPositions(), Charset.defaultCharset(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
