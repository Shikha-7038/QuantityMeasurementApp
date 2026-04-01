package QuantityMeasurementApp.enums;
public enum LengthUnit{

    FEET(1.0),
    INCH(1.0 / 12);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double toFeet(double value) {
        return value * toFeetFactor;
    }
}