package com.quyet.superapp.enums;

public enum RhType {
    POSITIVE("+"),
    NEGATIVE("-");

    private final String symbol;

    RhType(String symbol) {
        this.symbol = symbol;
    }

}
