package QuantityMeasurementApp;

import QuantityMeasurementApp.enums.LengthUnit;
import QuantityMeasurementApp.enums.WeightUnit;
import QuantityMeasurementApp.model.QuantityLength;
import QuantityMeasurementApp.model.QuantityWeight;

public class MeasurementApplication {

    public static void main(String[] args) {

        // ===== LENGTH =====
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCH);

        System.out.println(l1.add(l2)); // 2 feet

        // ===== WEIGHT =====
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        // Equality
        System.out.println("1 kg == 1000 g: " + w1.equals(w2));

        // Conversion
        System.out.println("1 kg to pounds: " + w1.convertTo(WeightUnit.POUND));

        // Addition
        System.out.println("1 kg + 1000 g: " + w1.add(w2));

        // Explicit target
        System.out.println("1 kg + 1000 g in grams: " +
                w1.add(w2, WeightUnit.GRAM));
    }
}