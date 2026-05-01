package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enums.IMeasurable;
import java.util.function.DoubleBinaryOperator;
import java.util.Objects;
public class Quantity<U extends IMeasurable> {
    private static final double EPSILON = 1e-6;
    private static final double SMALL_ROUND_THRESHOLD = 0.01;
    private static final int SMALL_ROUND_DECIMALS = 9;
    private static final int LARGE_ROUND_DECIMALS = 2;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (Double.isNaN(value)) throw new IllegalArgumentException("Value cannot be NaN");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public U getUnit() { return unit; }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        if (!unit.getClass().equals(targetUnit.getClass()))
            throw new IllegalArgumentException("Cross-category conversion not allowed");
        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(converted, targetUnit);
    }

    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (Math.abs(b) < EPSILON) throw new ArithmeticException("Division by zero");
            return a / b;
        });

        private final DoubleBinaryOperator op;
        ArithmeticOperation(DoubleBinaryOperator op) { this.op = op; }
        double compute(double a, double b) { return op.applyAsDouble(a, b); }
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetRequired, String operation) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (other.unit == null) throw new IllegalArgumentException("Operand unit cannot be null");
        if (!unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Cross-category operation not allowed");
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
            throw new IllegalArgumentException("Values must be finite");
        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target unit required");
        if (targetUnit != null && !unit.getClass().equals(targetUnit.getClass()))
            throw new IllegalArgumentException("Target unit must be same category");

        // UC14: validate operation support on involved units (may throw UnsupportedOperationException)
        unit.validateOperationSupport(operation);
        other.unit.validateOperationSupport(operation);
        if (targetUnit != null) targetUnit.validateOperationSupport(operation);
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation op) {
        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        return op.compute(base1, base2);
    }

    private double roundToNDecimals(double v, int n) {
        double factor = Math.pow(10.0, n);
        return Math.round(v * factor) / factor;
    }

    private double applyRoundingPolicy(double converted) {
        double abs = Math.abs(converted);
        if (abs >= SMALL_ROUND_THRESHOLD) {
            return roundToNDecimals(converted, LARGE_ROUND_DECIMALS);
        } else {
            return roundToNDecimals(converted, SMALL_ROUND_DECIMALS);
        }
    }

    public Quantity<U> add(Quantity<U> other) {
        validateArithmeticOperands(other, null, false, "ADD");
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double converted = unit.convertFromBaseUnit(baseResult);
        return new Quantity<>(applyRoundingPolicy(converted), unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true, "ADD");
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(converted, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        validateArithmeticOperands(other, null, false, "SUBTRACT");
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double converted = unit.convertFromBaseUnit(baseResult);
        return new Quantity<>(applyRoundingPolicy(converted), unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true, "SUBTRACT");
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(converted, targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false, "DIVIDE");
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?>)) return false;
        Quantity<?> other = (Quantity<?>) obj;
        if (!unit.getClass().equals(other.unit.getClass())) return false;
        double base1 = unit.convertToBaseUnit(value);
        double base2 = ((IMeasurable) other.unit).convertToBaseUnit(other.value);
        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        double base = unit.convertToBaseUnit(value);
        long bits = Double.doubleToLongBits(roundToNDecimals(base, LARGE_ROUND_DECIMALS));
        return Objects.hash(unit.getClass(), bits);
    }

    @Override
    public String toString() {
        return "Quantity{" + "value=" + value + ", unit=" + unit + '}';
    }
}