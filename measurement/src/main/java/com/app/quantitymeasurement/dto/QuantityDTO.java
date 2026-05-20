package com.app.quantitymeasurement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {
    private double value;
    private String unit;
    private String measurementType;
    private boolean error;
    private String errorMessage;

    // Constructor for success case
    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
        this.error = false;
        this.errorMessage = null;
    }

    // Constructor for error case
    public QuantityDTO(boolean error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return error;
    }
}
