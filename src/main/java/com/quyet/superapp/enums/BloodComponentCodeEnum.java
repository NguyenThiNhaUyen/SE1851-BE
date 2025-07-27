package com.quyet.superapp.enums;

public enum BloodComponentCodeEnum {
    RBC("HỒNG CẦU"),
    PLAS("HUYẾT TƯƠNG"),
    PLT("TIỂU CẦU");

    private final String name;

    BloodComponentCodeEnum(String name) {
        this.name = name;
    }

    public static BloodComponentCodeEnum fromName(String name) {
        for (BloodComponentCodeEnum value : values()) {
            if (value.name.equalsIgnoreCase(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy mã cho thành phần máu: " + name);
    }
}
