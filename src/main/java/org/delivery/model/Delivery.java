package org.delivery.model;

import java.util.List;

public class Delivery {

    private String drone;
    private List<String> routes;

    public Delivery(String drone, List<String> routes) {
        this.drone = drone;
        this.routes = routes;
    }

    public String getDrone() {
        return drone;
    }

    public List<String> getRoutes() {
        return routes;
    }
}
