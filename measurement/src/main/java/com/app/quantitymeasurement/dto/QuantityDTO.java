package com.app.quantitymeasurement.dto;
public class QuantityDTO {
    private double value;
    private String unit;
    private String measurementType;
    private boolean error;
    private String errorMessage;

    public QuantityDTO(double value, String unit, String measurementType){
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }
    public QuantityDTO(boolean error, String errorMessage){
        this.error = error;
        this.errorMessage = errorMessage;
    }
    public double getValue(){
        return value;
    }
    public String getUnit(){
        return unit;
    }
    public String getMeasurementType(){
        return measurementType;
    }
    public boolean hasError(){
        return error;
    }
    public String getErrorMessage(){
        return errorMessage;
    }
}
