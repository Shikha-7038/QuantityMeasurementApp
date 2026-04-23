package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enumsImplement.LengthUnit;

public class QuantityLength {
    private final Quantity<LengthUnit> delegate;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.delegate = new Quantity<>(value, unit);
    }

    public double getValue() {
        return delegate.getValue();
    }

    public LengthUnit getUnit() {
        return delegate.getUnit();
    }

    // Legacy conversion helpers
    public double toFeet() {
        return delegate.convertTo(LengthUnit.FEET).getValue();
    }

    public double toInch() {
        return delegate.convertTo(LengthUnit.INCH).getValue();
    }

    public double toYard() {
        return delegate.convertTo(LengthUnit.YARD).getValue();
    }

    public double toCentimeter() {
        return delegate.convertTo(LengthUnit.CENTIMETERS).getValue();
    }

    public double toConvert(LengthUnit target) {
        return delegate.convertTo(target).getValue();
    }

    // Conversion
    public QuantityLength convert(LengthUnit targetUnit) {
        Quantity<LengthUnit> converted = delegate.convertTo(targetUnit);
        return new QuantityLength(converted.getValue(), converted.getUnit());
    }

    // Addition
    public QuantityLength add(QuantityLength other) {
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }
        Quantity<LengthUnit> result = delegate.add(other.delegate, this.getUnit());
        return new QuantityLength(result.getValue(), result.getUnit());
    }

    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Operands and target unit cannot be null");
        }
        Quantity<LengthUnit> result = delegate.add(other.delegate, targetUnit);
        return new QuantityLength(result.getValue(), result.getUnit());
    }

    // Static helper for legacy tests
    public static double convert(double value, LengthUnit from, LengthUnit to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        return new Quantity<>(value, from).convertTo(to).getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuantityLength other) {
            return delegate.equals(other.delegate);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}