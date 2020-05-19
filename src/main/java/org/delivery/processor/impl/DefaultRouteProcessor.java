package org.delivery.processor.impl;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;
import org.delivery.processor.RouteProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class DefaultRouteProcessor implements RouteProcessor {

    private final static Logger LOGGER = Logger.getLogger(DefaultRouteProcessor.class.getName());

    private final static String[] DIRECTIONS = new String[]{"N","E","S","W"};

    @Override
    public Optional<Monitoring> process(Delivery delivery) throws ValidationException {

        StringBuilder instructionText = new StringBuilder("DRONE " + delivery.getDrone() + " WORKING\n");

        List<String> positions = new ArrayList<>();
        int numDelivery = 1;

        for (String route : delivery.getRoutes()) {

            int x = 0;
            int y = 0;
            int direction = 0;

            instructionText.append("DELIVERY NUMBER " + numDelivery + "\n");

            for (String instruction : route.split("(?!^)")) {

                switch (instruction.toUpperCase()) {
                    case "A":
                        if (direction == 0) {
                            y++;
                        } else if (direction == 1) {
                            x++;
                        } else if (direction == 2) {
                            y--;
                        } else {
                            x--;
                        }
                        instructionText.append("Moving forward\n");
                        break;
                    case "D":
                        int rightDirection = direction + 1;
                        if (rightDirection == DIRECTIONS.length) {
                            rightDirection = rightDirection - DIRECTIONS.length;
                        }
                        direction = rightDirection;
                        instructionText.append("Turning right to direction " + DIRECTIONS[direction] + "\n");
                        break;
                    case "I":
                        int leftDirection = direction - 1;
                        if (leftDirection < 0) {
                            leftDirection = leftDirection + DIRECTIONS.length;
                        }
                        direction = leftDirection;
                        instructionText.append("Turning left to direction " + DIRECTIONS[direction] + "\n");
                        break;
                }
            }
            positions.add(String.format("(%d,%d) direction %s", x, y, DIRECTIONS[direction]));
            numDelivery++;
        }
        LOGGER.fine(instructionText.toString());

        Monitoring monitoring = new Monitoring(delivery.getDrone(), positions);
        return Optional.of(monitoring);
    }

}
