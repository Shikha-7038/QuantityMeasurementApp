package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enums.WeightUnit;
public class QuantityWeight {
    private static final double EPSILON = 1e-6;
    private final double value;
    private final WeightUnit unit;

    public double getValue() {
        return value;
    }

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid value");
        }
        this.value = value;
        this.unit = unit;
    }
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new QuantityWeight(converted, targetUnit);
    }
    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {

        if (other == null || targetUnit == null) {
            throw new IllegalArgumentException("Invalid operands");
        }
        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        double sumBase = base1 + base2;
        double result = targetUnit.convertFromBaseUnit(sumBase);
        return new QuantityWeight(result, targetUnit);
    }

    public double toKilogram() {
        return unit.convertToBaseUnit(value);
    }

    public double toConvert(WeightUnit targetUnit) {
        return targetUnit.convertFromBaseUnit(
                unit.convertToBaseUnit(value)
        );
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        //if (!(obj instanceof QuantityWeight)) return false;

        QuantityWeight other = (QuantityWeight) obj;

        double base1 = this.unit.convertToBaseUnit(this.value);
        double base2 = other.unit.convertToBaseUnit(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }
    @Override
    public int hashCode() {
        long base = Double.doubleToLongBits(unit.convertToBaseUnit(value));
        return (int) (base ^ (base >>> 32));
    }
    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}