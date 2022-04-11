package com.aadhikat.springrsocket.dto.error;

public enum StatusCode {

    EC001 ("given number is not in range"),
    EC002 ("usage limit exceeded");

    private final String description;

    StatusCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
