package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enums.IMeasurable;
import java.util.function.DoubleBinaryOperator;
public class Quantity<U extends IMeasurable> {
    private static final double EPSILON = 1e-5;
    private final double value;
    private final U unit;
    public Quantity(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid value");
        this.value = value;
        this.unit = unit;
    }
    public double getValue() {
        return value;
    }
    public U getUnit() {
        return unit;
    }
    // ================= ENUM WITH LAMBDA =================
    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (Math.abs(b) < EPSILON)
                throw new ArithmeticException("Division by zero");
            return a / b;
        });
        private final DoubleBinaryOperator op;
        ArithmeticOperation(DoubleBinaryOperator op){
            this.op = op;
        }
        double compute(double a, double b) {
            return op.applyAsDouble(a, b);
        }
    }
    // ================= VALIDATION =================
    private void validateArithmeticOperands(
            Quantity<?> other,
            Object targetUnit,
            boolean targetRequired) {
        if (other == null)
            throw new IllegalArgumentException("Other cannot be null");
        if (this.unit.getClass() != other.unit.getClass())
            throw new IllegalArgumentException("Different measurement categories");
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid numeric values");
        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target unit required");
    }
    // ================= CORE HELPER =================
    private double performBaseArithmetic(
            Quantity<?> other,
            Enum<?> operationEnum) {
        ArithmeticOperation operation = (ArithmeticOperation) operationEnum;
        double base1 = unit.convertToBaseUnit(value);
        double base2 = ((IMeasurable) other.unit).convertToBaseUnit(other.value);
        return operation.compute(base1, base2);
    }
    // ================= ROUNDING =================
    private double roundToTwoDecimals(double val) {
        if (Math.abs(val) < 0.01) {
            return val; // preserve precision for small values
        }
        return Math.round(val * 100.0) / 100.0;
    }
    // ================= ADD =================
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(roundToTwoDecimals(converted), targetUnit);
    }
    // ================= SUBTRACT =================
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double converted = targetUnit.convertFromBaseUnit(baseResult);
        return new Quantity<>(roundToTwoDecimals(converted), targetUnit);
    }
    // ================= DIVIDE =================
    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }
    // ================= CONVERSION =================
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target cannot be null");
        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(converted, targetUnit);
    }
    // ================= EQUALITY =================
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Quantity<?> other = (Quantity<?>) obj;
        if (this.unit.getClass() != other.unit.getClass())
            return false;
        double base1 = unit.convertToBaseUnit(value);
        double base2 = other.unit.convertToBaseUnit(other.value);
        return Math.abs(base1 - base2) < EPSILON;
    }
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(unit.convertToBaseUnit(value));
        return (int) (bits ^ (bits >>> 32));
    }
    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}