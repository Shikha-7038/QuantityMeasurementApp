package QuantityMeasurementApp.enumsImplement;

public class Quantity<U extends IMeasurable> {

    private static final double EPSILON = 1e-6;

    private final double value;
    private final U unit;

    // ===== Constructor =====
    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        this.value = value;
        this.unit = unit;
    }

    // ===== Getters =====
    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // ===== Convert =====
    public Quantity<U> convertTo(U targetUnit) {
        if (!unit.getClass().equals(targetUnit.getClass())) {
            throw new IllegalArgumentException("Incompatible unit types");
        }

        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(convertedValue, targetUnit);
    }

    // ===== Add (same unit as this) =====
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    // ===== Add (explicit target unit) =====
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (!unit.getClass().equals(other.unit.getClass())) {
            throw new IllegalArgumentException("Incompatible unit types");
        }

        double sumBase =
                this.unit.convertToBaseUnit(this.value) +
                        other.unit.convertToBaseUnit(other.value);

        double result = targetUnit.convertFromBaseUnit(sumBase);

        return new Quantity<>(result, targetUnit);
    }

    // ===== Equals =====
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Quantity<?> other)) return false;

        // Cross-category check
        if (!this.unit.getClass().equals(other.unit.getClass())) {
            return false;
        }

        double thisBase = unit.convertToBaseUnit(value);
        double otherBase = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisBase - otherBase) < EPSILON;
    }

    // ===== HashCode =====
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(unit.convertToBaseUnit(value));
        return (int) (bits ^ (bits >>> 32));
    }

    // ===== toString =====
    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}