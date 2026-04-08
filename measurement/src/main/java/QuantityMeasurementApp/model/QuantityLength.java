package QuantityMeasurementApp.model;
import QuantityMeasurementApp.enums.LengthUnit;
public class QuantityLength{

    private static final double EPSILON = 1e-6;

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if(unit == null) throw new IllegalArgumentException("Unit shouldn't be null");
        this.value = value;
        this.unit = unit;
    }
    public double toFeet() {
        return unit.toFeet(value);
    }
    public double toConvert(LengthUnit targetUnit){

        return convert(this.value, this.unit, targetUnit);
    }

    public static double convert(double value, LengthUnit sourceUnit, LengthUnit targetUnit) {
        if (sourceUnit == null || targetUnit == null) {
            throw new IllegalArgumentException("Units shouldn't be empty!!");
        }
        if(!Double.isFinite(value)){
            throw new IllegalArgumentException("Invalid numeric value!");
        }
        double valueInFeet = sourceUnit.toFeet(value);
        return targetUnit.fromFeet(valueInFeet);
    }

    public double toBaseUnit(){
        return unit.toFeet(value);
    }
    // ================= UC7 =================
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Second quantity and targetUnit must not be null");
        if (!Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid value in other quantity");

        double thisFeet = this.toBaseUnit();
        double otherFeet = other.toBaseUnit();

        double sumFeet = thisFeet + otherFeet;

        double result = targetUnit.fromFeet(sumFeet);

        return new QuantityLength(result, targetUnit);
    }

    public QuantityLength add(QuantityLength other){
        return add(other, this.unit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        QuantityLength other = (QuantityLength) obj;
        double thisInFeet = this.unit.toFeet(this.value);
        double otherInFeet = other.unit.toFeet(other.value);

        return Math.abs(thisInFeet - otherInFeet) < EPSILON;
    }
}
