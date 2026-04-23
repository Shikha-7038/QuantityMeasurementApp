package QuantityMeasurementApp.enumsImplement;
import QuantityMeasurementApp.enums.IMeasurable;
public enum WeightUnit implements IMeasurable {

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
    public double convertToBaseUnit(double value) {
        return value * toKgFactor;
    }
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toKgFactor;
    }
    public String getUnitName() {
        return this.name();
    }
}