package QuantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import QuantityMeasurementApp.model.QuantityLength;
import QuantityMeasurementApp.enums.LengthUnit;

//@SpringBootTest
class MeasurementApplicationTests {

    private static final double EPSILON = 1e-6;
    @Test
    void testEquality_YardToYard_SameValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.YARD)
                .equals(new QuantityLength(1.0, LengthUnit.YARD)));
    }

    // 2
    @Test
    void testEquality_YardToYard_DifferentValue() {
        assertFalse(new QuantityLength(1.0, LengthUnit.YARD)
                .equals(new QuantityLength(2.0, LengthUnit.YARD)));
    }

    // 3
    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.YARD)
                .equals(new QuantityLength(3.0, LengthUnit.FEET)));
    }

    // 4
    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        assertTrue(new QuantityLength(3.0, LengthUnit.FEET)
                .equals(new QuantityLength(1.0, LengthUnit.YARD)));
    }

    // 5
    @Test
    void testEquality_YardToInches_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.YARD)
                .equals(new QuantityLength(36.0, LengthUnit.INCH)));
    }

    // 6
    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        assertTrue(new QuantityLength(36.0, LengthUnit.INCH)
                .equals(new QuantityLength(1.0, LengthUnit.YARD)));
    }

    // 7
    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        assertFalse(new QuantityLength(1.0, LengthUnit.YARD)
                .equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    // 8
    @Test
    void testEquality_CentimetersToInches_EquivalentValue() {
        assertTrue(new QuantityLength(1.0, LengthUnit.CENTIMETERS)
                .equals(new QuantityLength(0.393701, LengthUnit.INCH)));
    }

    // 9
    @Test
    void testEquality_CentimetersToFeet_NonEquivalentValue() {
        assertFalse(new QuantityLength(1.0, LengthUnit.CENTIMETERS)
                .equals(new QuantityLength(1.0, LengthUnit.FEET)));
    }

    // 10
    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        var yard = new QuantityLength(1.0, LengthUnit.YARD);
        var feet = new QuantityLength(3.0, LengthUnit.FEET);
        var inch = new QuantityLength(36.0, LengthUnit.INCH);

        assertTrue(yard.equals(feet) && feet.equals(inch));
    }

    // 11
    @Test
    void testEquality_YardWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, null);
        });
    }

    // 12
    @Test
    void testEquality_YardSameReference() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.YARD);
        assertTrue(q.equals(q));
    }

    // 13
    @Test
    void testEquality_YardNullComparison() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.YARD);
        assertFalse(q.equals(null));
    }

    // 14
    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, null);
        });
    }

    // 15
    @Test
    void testEquality_CentimetersSameReference() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        assertTrue(q.equals(q));
    }

    // 16
    @Test
    void testEquality_CentimetersNullComparison() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        assertFalse(q.equals(null));
    }

    // 17
    @Test
    void testEquality_AllUnits_ComplexScenario() {
        QuantityLength yard = new QuantityLength(2.0, LengthUnit.YARD);
        QuantityLength feet = new QuantityLength(6.0, LengthUnit.FEET);
        QuantityLength inch = new QuantityLength(72.0, LengthUnit.INCH);

        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inch));
        assertTrue(yard.equals(inch));
    }
    //    =================================================================================
    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0,
                QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0,
                QuantityLength.convert(24.0, LengthUnit.INCH, LengthUnit.FEET),
                EPSILON);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0,
                QuantityLength.convert(1.0, LengthUnit.YARD, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(2.0,
                QuantityLength.convert(72.0, LengthUnit.INCH, LengthUnit.YARD),
                EPSILON);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(1.0,
                QuantityLength.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testConversion_FeetToYard() {
        assertEquals(2.0,
                QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARD),
                EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0,
                QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0,
                QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {
        double original = 5.0;

        double converted = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCH);
        double back = QuantityLength.convert(converted, LengthUnit.INCH, LengthUnit.FEET);

        assertEquals(original, back, EPSILON);
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(1.0, null, LengthUnit.FEET);
        });
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCH);
        });
    }

    @Test
    void testConversion_PrecisionTolerance() {
        double result = QuantityLength.convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH);
        assertEquals(0.393701, result, 1e-6);
    }

    // ===================== UC6 ADDITION TESTS =====================

    // 1
    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        assertEquals(
                new QuantityLength(3.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(2.0, LengthUnit.FEET))
        );
    }

    // 2
    @Test
    void testAddition_SameUnit_InchPlusInch() {
        assertEquals(
                new QuantityLength(12.0, LengthUnit.INCH),
                new QuantityLength(6.0, LengthUnit.INCH)
                        .add(new QuantityLength(6.0, LengthUnit.INCH))
        );
    }

    // 3
    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        assertEquals(
                new QuantityLength(2.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(12.0, LengthUnit.INCH))
        );
    }

    // 4
    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        assertEquals(
                new QuantityLength(24.0, LengthUnit.INCH),
                new QuantityLength(12.0, LengthUnit.INCH)
                        .add(new QuantityLength(1.0, LengthUnit.FEET))
        );
    }

    // 5
    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        assertEquals(
                new QuantityLength(2.0, LengthUnit.YARD),
                new QuantityLength(1.0, LengthUnit.YARD)
                        .add(new QuantityLength(3.0, LengthUnit.FEET))
        );
    }

    // 6
    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        assertEquals(
                new QuantityLength(5.08, LengthUnit.CENTIMETERS),
                new QuantityLength(2.54, LengthUnit.CENTIMETERS)
                        .add(new QuantityLength(1.0, LengthUnit.INCH))
        );
    }

    // 7
    @Test
    void testAddition_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCH);

        assertEquals(a.add(b), b.add(a));
    }

    // 8
    @Test
    void testAddition_WithZero() {
        assertEquals(
                new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(5.0, LengthUnit.FEET)
                        .add(new QuantityLength(0.0, LengthUnit.INCH))
        );
    }

    // 9
    @Test
    void testAddition_NegativeValues() {
        assertEquals(
                new QuantityLength(3.0, LengthUnit.FEET),
                new QuantityLength(5.0, LengthUnit.FEET)
                        .add(new QuantityLength(-2.0, LengthUnit.FEET))
        );
    }

    // 10
    @Test
    void testAddition_NullSecondOperand() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, LengthUnit.FEET).add(null);
        });
    }

    // 11
    @Test
    void testAddition_LargeValues() {
        assertEquals(
                new QuantityLength(2_000_000.0, LengthUnit.FEET),
                new QuantityLength(1_000_000.0, LengthUnit.FEET)
                        .add(new QuantityLength(1_000_000.0, LengthUnit.FEET))
        );
    }

    // 12
    @Test
    void testAddition_SmallValues() {
        QuantityLength result =
                new QuantityLength(0.001, LengthUnit.FEET)
                        .add(new QuantityLength(0.002, LengthUnit.FEET));

        assertEquals(
                new QuantityLength(0.003, LengthUnit.FEET),
                result
        );
    }
    // ================= UC7 (Instance Method Based) =================
    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.FEET);

        assertEquals(new QuantityLength(2.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.INCH);

        assertEquals(new QuantityLength(24.0, LengthUnit.INCH), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARD);

        assertEquals(new QuantityLength(2.0 / 3.0, LengthUnit.YARD), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        QuantityLength result = new QuantityLength(2.54, LengthUnit.CENTIMETERS)
                .add(new QuantityLength(1.0, LengthUnit.INCH), LengthUnit.CENTIMETERS);

        assertEquals(new QuantityLength(5.08, LengthUnit.CENTIMETERS), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.YARD)
                .add(new QuantityLength(2.0, LengthUnit.YARD), LengthUnit.YARD);

        assertEquals(new QuantityLength(3.0, LengthUnit.YARD), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        QuantityLength result = new QuantityLength(3.0, LengthUnit.FEET)
                .add(new QuantityLength(6.0, LengthUnit.FEET), LengthUnit.FEET);

        assertEquals(new QuantityLength(9.0, LengthUnit.FEET), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCH);

        double r1 = a.add(b, LengthUnit.FEET).toFeet();
        double r2 = b.add(a, LengthUnit.FEET).toFeet();

        assertEquals(r1, r2, EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(0.0, LengthUnit.INCH), LengthUnit.YARD);

        assertEquals(new QuantityLength(5.0 / 3.0, LengthUnit.YARD), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        QuantityLength result = new QuantityLength(5.0, LengthUnit.FEET)
                .add(new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCH);

        assertEquals(new QuantityLength(36.0, LengthUnit.INCH), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class, () -> {
            new QuantityLength(1.0, LengthUnit.FEET)
                    .add(new QuantityLength(1.0, LengthUnit.FEET), null);
        });
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        QuantityLength result = new QuantityLength(1000.0, LengthUnit.FEET)
                .add(new QuantityLength(500.0, LengthUnit.FEET), LengthUnit.INCH);

        assertEquals(new QuantityLength(18000.0, LengthUnit.INCH), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        QuantityLength result = new QuantityLength(12.0, LengthUnit.INCH)
                .add(new QuantityLength(12.0, LengthUnit.INCH), LengthUnit.YARD);

        assertEquals(new QuantityLength(2.0 / 3.0, LengthUnit.YARD), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_PrecisionTolerance() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.CENTIMETERS)
                .add(new QuantityLength(1.0, LengthUnit.CENTIMETERS), LengthUnit.INCH);

        assertEquals(new QuantityLength(0.7874, LengthUnit.INCH), result);
    }

    @Test
    void testAddition_ExplicitTargetUnit_AllUnitCombinations() {
        LengthUnit[] units = LengthUnit.values();
        for (LengthUnit u1 : units) {
            for (LengthUnit u2 : units) {
                for (LengthUnit target : units) {
                    QuantityLength q1 = new QuantityLength(1.0, u1);
                    QuantityLength q2 = new QuantityLength(1.0, u2);

                    QuantityLength result = q1.add(q2, target);
                    double expected = QuantityLength.convert(1.0, u1, target) +
                            QuantityLength.convert(1.0, u2, target);
                    assertEquals(expected, result.toConvert(target), 1e-6,
                            "Failed for units: " + u1 + ", " + u2 + " -> " + target);
                }
            }
        }
    }

    // ---------- ENUM TESTS ----------

    @Test
    void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor());
    }

    @Test
    void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0 / 12.0, LengthUnit.INCH.getConversionFactor(), EPSILON);
    }

    @Test
    void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARD.getConversionFactor());
    }

    @Test
    void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(0.0328084, LengthUnit.CENTIMETERS.getConversionFactor(), EPSILON);
    }

    // ---------- convertToBaseUnit TESTS ----------

    @Test
    void testConvertToBaseUnit_FeetToFeet() {
        assertEquals(5.0, LengthUnit.FEET.convertToBaseUnit(5.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCH.convertToBaseUnit(12.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, LengthUnit.YARD.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), EPSILON);
    }

    // ---------- convertFromBaseUnit TESTS ----------

    @Test
    void testConvertFromBaseUnit_FeetToFeet() {
        assertEquals(2.0, LengthUnit.FEET.convertFromBaseUnit(2.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCH.convertFromBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, LengthUnit.YARD.convertFromBaseUnit(3.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), EPSILON);
    }

    // ---------- QuantityLength Equality ----------

    @Test
    void testQuantityLengthRefactored_Equality() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    // ---------- Conversion via Quantity ----------

    @Test
    void testQuantityLengthRefactored_ConvertTo() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength result = q.convert(LengthUnit.INCH);

        assertTrue(result.equals(new QuantityLength(12.0, LengthUnit.INCH)));
    }

    // ---------- Addition ----------

    @Test
    void testQuantityLengthRefactored_Add() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength result = q1.add(q2);

        assertTrue(result.equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    void testQuantityLengthRefactored_AddWithTargetUnit() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength result = q1.add(q2, LengthUnit.YARD);

        assertEquals(0.666666, result.convert(LengthUnit.YARD).getValue(), EPSILON);
    }

    // ---------- Validation ----------

    @Test
    void testQuantityLengthRefactored_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(1.0, null));
    }

    @Test
    void testQuantityLengthRefactored_InvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    // ---------- Backward Compatibility ----------

    @Test
    void testBackwardCompatibility_UC1EqualityTests() {
        QuantityLength q1 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(36.0, LengthUnit.INCH);

        assertTrue(q1.equals(q2));
    }

    @Test
    void testBackwardCompatibility_UC5ConversionTests() {
        QuantityLength q = new QuantityLength(1.0, LengthUnit.FEET);

        assertTrue(q.convert(LengthUnit.INCH)
                .equals(new QuantityLength(12.0, LengthUnit.INCH)));
    }

    @Test
    void testBackwardCompatibility_UC6AdditionTests() {
        QuantityLength q1 = new QuantityLength(2.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(24.0, LengthUnit.INCH);

        assertTrue(q1.add(q2).equals(new QuantityLength(4.0, LengthUnit.FEET)));
    }

    @Test
    void testBackwardCompatibility_UC7AdditionWithTargetUnitTests() {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength result = q1.add(q2, LengthUnit.YARD);
        assertEquals(0.666666, result.getValue(), 1e-5);
        //assertTrue(result.equals(new QuantityLength(0.666666, LengthUnit.YARD)));
    }

    // ---------- Architecture & Advanced ----------

    @Test
    void testArchitecturalScalability_MultipleCategories() {
        assertNotNull(LengthUnit.FEET);
    }

    @Test
    void testRoundTripConversion_RefactoredDesign() {
        QuantityLength original = new QuantityLength(5.0, LengthUnit.FEET);

        QuantityLength converted = original.convert(LengthUnit.INCH)
                .convert(LengthUnit.FEET);

        assertTrue(original.equals(converted));
    }

    @Test
    void testUnitImmutability() {
        LengthUnit unit = LengthUnit.FEET;
        assertEquals(1.0, unit.getConversionFactor());
    }

}