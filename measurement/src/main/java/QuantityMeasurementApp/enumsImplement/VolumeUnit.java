package QuantityMeasurementApp.enumsImplement;
import QuantityMeasurementApp.enums.IMeasurable;
public enum VolumeUnit implements IMeasurable{
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);                 // 1 yard = 3 feet

    private final double toLitreFactor;

    VolumeUnit(double toLitreFactor) {
        this.toLitreFactor = toLitreFactor;
    }

    public double toLitre(double value) {
        return value * toLitreFactor;
    }
    public double convertToBaseUnit(double value) {
        if(!Double.isFinite(value)){
            throw new IllegalArgumentException("Invalid value");
        }
        return value * toLitreFactor;
    }
    public double convertFromBaseUnit(double baseValue) {
        if(!Double.isFinite(baseValue)){
            throw new IllegalArgumentException("Invalid value");
        }
        return baseValue / toLitreFactor;
    }
    public double getConversionFactor() {
        return toLitreFactor;
    }
    public String getUnitName() {
        return this.name();
    }
}
