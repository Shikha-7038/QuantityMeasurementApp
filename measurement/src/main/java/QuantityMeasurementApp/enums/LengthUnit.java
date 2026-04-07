package QuantityMeasurementApp.enums;

public enum LengthUnit {

    FEET(1.0),
    INCH(1.0 / 12),
    YARD(3.0),                 // 1 yard = 3 feet
    CENTIMETERS(0.0328084);     // 1 cm = 0.0328084 feet

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double toFeet(double value) {
        return value * toFeetFactor;
    }
    public double fromFeet(double value){
        return value / toFeetFactor;
    }
}
