package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enumsImplement.WeightUnit;
public class QuantityWeight {
    private final Quantity<WeightUnit> delegate;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.delegate = new Quantity<>(value, unit);
    }

    public double getValue() {
        return delegate.getValue();
    }

    public WeightUnit getUnit() {
        return delegate.getUnit();
    }

    // Legacy conversion helpers
    public double toKilogram() {
        return delegate.convertTo(WeightUnit.KILOGRAM).getValue();
    }

    public double toGram() {
        return delegate.convertTo(WeightUnit.GRAM).getValue();
    }

    public double toPound() {
        return delegate.convertTo(WeightUnit.POUND).getValue();
    }

    // Conversion
    public QuantityWeight convert(WeightUnit targetUnit) {
        Quantity<WeightUnit> converted = delegate.convertTo(targetUnit);
        return new QuantityWeight(converted.getValue(), converted.getUnit());
    }

    // Addition
    public QuantityWeight add(QuantityWeight other) {
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }
        Quantity<WeightUnit> result = delegate.add(other.delegate, this.getUnit());
        return new QuantityWeight(result.getValue(), result.getUnit());
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Operands and target unit cannot be null");
        }
        Quantity<WeightUnit> result = delegate.add(other.delegate, targetUnit);
        return new QuantityWeight(result.getValue(), result.getUnit());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuantityWeight other) {
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