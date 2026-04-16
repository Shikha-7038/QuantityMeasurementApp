package QuantityMeasurementApp.enums;
public enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double toKgFactor;

    WeightUnit(double toKgFactor) {
        this.toKgFactor = toKgFactor;
    }

    public double getConversionFactor() {
        return toKgFactor;
    }

    // Convert value → kilograms (base unit)
    public double convertToBaseUnit(double value) {
        return value * toKgFactor;
    }

    // Convert kilograms → this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toKgFactor;
    }
}