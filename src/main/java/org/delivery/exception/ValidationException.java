package org.delivery.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {

    private List<String> errors;

    public ValidationException(String errorMessage) {
        super(errorMessage);
        errors = new ArrayList<>();
        errors.add(errorMessage);
    }

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
