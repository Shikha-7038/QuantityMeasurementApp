package QuantityMeasurementApp;

import QuantityMeasurementApp.enumsImplement.LengthUnit;
import QuantityMeasurementApp.enumsImplement.WeightUnit;
import QuantityMeasurementApp.model.Quantity;
import QuantityMeasurementApp.enums.IMeasurable;

public class MeasurementApplication {

    public static void main(String[] args) {

        // ===== LENGTH =====
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCH);

        demonstrateAddition(l1, l2);  // 2 feet
        demonstrateEquality(l1, l2);
        demonstrateConversion(l1, LengthUnit.INCH);

        // ===== WEIGHT =====
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        demonstrateEquality(w1, w2);
        demonstrateConversion(w1, WeightUnit.POUND);
        demonstrateAddition(w1, w2);

        // Explicit target
        System.out.println("Explicit target:");
        System.out.println(w1 + " + " + w2 + " = " +
                w1.add(w2, WeightUnit.GRAM));
    }

    // ===== Generic Methods =====

    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> q1, Quantity<U> q2) {

        System.out.println("Comparing: " + q1 + " and " + q2);
        System.out.println("Equal? " + q1.equals(q2));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> q, U targetUnit) {

        System.out.println("Converting: " + q + " -> " +
                q.convertTo(targetUnit));
        System.out.println();
    }

    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> q1, Quantity<U> q2) {

        System.out.println("Adding: " + q1 + " + " + q2);
        System.out.println("Result: " + q1.add(q2));
        System.out.println();
    }
}