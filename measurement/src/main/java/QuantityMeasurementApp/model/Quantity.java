package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enums.IMeasurable;
public class Quantity<U extends IMeasurable> {

    private static final double EPSILON = 1e-6;
    private final double value;
    private final U unit;
    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite and not NaN");
        }
        this.value = value;
        this.unit = unit;
    }
    public double getValue() {
        return value;
    }
    public U getUnit() {
        return unit;
    }
    // Convert to target unit
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(converted, targetUnit);
    }

    // Add (implicit unit)
    public Quantity<U> add(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Second operand cannot be null");
        }
        return add(other, this.unit);
    }

    // Add (explicit target unit)
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Operands and target unit cannot be null");
        }
        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        double sum = base1 + base2;
        return new Quantity<>(targetUnit.convertFromBaseUnit(sum), targetUnit);
    }
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateOperation(other, targetUnit);

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        double diff = base1 - base2;
        double result = targetUnit.convertFromBaseUnit(diff);

        return new Quantity<>(result, targetUnit);
    }

    // 🔹 DIVISION (UC12)

    public double divide(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }

        // Category check
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Cannot divide different measurement categories");
        }

        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        if (Math.abs(base2) < EPSILON) {
            throw new ArithmeticException("Division by zero");
        }

        return base1 / base2;
    }
    private void validateOperation(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Measurement categories do not match");
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            return false;
        }
        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.convertToBaseUnit(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}