package com.quyet.superapp.enums;

public enum BloodComponentType {
    RED_BLOOD_CELLS(84, "Hồng cầu"),
    PLASMA(28, "Huyết tương"),
    PLATELETS(14, "Tiểu cầu"),
    WHOLE_BLOOD(84, "Toàn phần");

    private final int recoveryDays;
    private final String description;

    BloodComponentType(int recoveryDays, String description) {
        this.recoveryDays = recoveryDays;
        this.description = description;
    }

    public int getRecoveryDays() {
        return recoveryDays;
    }

    public String getDescription() {
        return description;
    }
}
