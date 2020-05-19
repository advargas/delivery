package org.delivery.model;

import java.util.List;

/**
 * Output monitoring model.
 */
public class Monitoring {

    private String drone;
    private List<String> positions;

    public Monitoring(String drone, List<String> positions) {
        this.drone = drone;
        this.positions = positions;
    }

    public String getDrone() {
        return drone;
    }

    public List<String> getPositions() {
        return positions;
    }
}
