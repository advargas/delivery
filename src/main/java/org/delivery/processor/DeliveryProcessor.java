package org.delivery.processor;

import org.delivery.exception.ValidationException;
import org.delivery.model.Delivery;
import org.delivery.model.Monitoring;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface DeliveryProcessor {

    Optional<List<Monitoring>> process(List<Delivery> deliveries) throws Exception;
}
