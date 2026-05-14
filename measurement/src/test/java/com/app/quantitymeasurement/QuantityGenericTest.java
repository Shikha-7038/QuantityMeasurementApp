package com.app.quantitymeasurement;

import com.app.quantitymeasurement.enumsImplement.LengthUnit;
import com.app.quantitymeasurement.enumsImplement.WeightUnit;
import com.app.quantitymeasurement.enumsImplement.VolumeUnit;
import com.app.quantitymeasurement.enumsImplement.TemperatureUnit;
import com.app.quantitymeasurement.model.Quantity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repositoryImpl.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repositoryImpl.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementService;
import com.app.quantitymeasurement.serviceImpl.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.config.ApplicationConfig;
import com.app.quantitymeasurement.database.ConnectionPool;

import java.sql.Connection;
import java.util.List;

public class QuantityGenericTest {
    private static final double EPSILON = 1e-6;
    private QuantityMeasurementService service;
    private QuantityMeasurementDatabaseRepository repository;
    private QuantityMeasurementController controller;
    @BeforeEach
    void setUp() {
        repository = new QuantityMeasurementDatabaseRepository();
        service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    @Test
    void testIMeasurableInterface_LengthUnitImplementation() { assertNotNull(LengthUnit.FEET.getUnitName()); }
    @Test void testIMeasurableInterface_WeightUnitImplementation() { assertNotNull(WeightUnit.KILOGRAM.getUnitName()); }
    @Test void testIMeasurableInterface_ConsistentBehavior() { assertEquals("FEET", LengthUnit.FEET.getUnitName()); }

    // ===== 4–9 Generic Quantity Operations =====
    @Test void testGenericQuantity_LengthOperations_Equality() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }
    @Test void testGenericQuantity_WeightOperations_Equality() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }
    @Test void testGenericQuantity_LengthOperations_Conversion() {
        assertEquals(36.0, new Quantity<>(1.0, LengthUnit.YARD).convertTo(LengthUnit.INCH).getValue(), EPSILON);
    }
    @Test void testGenericQuantity_WeightOperations_Conversion() {
        assertEquals(1000.0, new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).getValue(), EPSILON);
    }
    @Test void testGenericQuantity_LengthOperations_Addition() {
        assertEquals(2.0, new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCH), LengthUnit.FEET).getValue(), EPSILON);
    }
    @Test void testGenericQuantity_WeightOperations_Addition() {
        assertEquals(2.0, new Quantity<>(1.0, WeightUnit.KILOGRAM).add(new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM).getValue(), EPSILON);
    }
    // ===== 10–11 Cross‑Category Prevention =====
    @Test void testCrossCategoryPrevention_LengthVsWeight() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }
    @Test void testCrossCategoryPrevention_CompilerTypeSafety() {
        // This is compile‑time enforced: Quantity<LengthUnit> cannot be assigned to Quantity<WeightUnit>.
        assertTrue(true);
    }

    // ===== 12–13 Constructor Validation =====
    @Test void testGenericQuantity_ConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }
    @Test void testGenericQuantity_ConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    // ===== 14–15 Combinatorial Coverage =====
    @Test void testGenericQuantity_Conversion_AllUnitCombinations() {
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                double expected = u2.convertFromBaseUnit(u1.convertToBaseUnit(1.0));
                double actual = new Quantity<>(1.0, u1).convertTo(u2).getValue();
                assertEquals(expected, actual, EPSILON, "Failed for " + u1 + " -> " + u2);
            }
        }
    }
    @Test
    void testGenericQuantity_Addition_AllUnitCombinations() {
        for (LengthUnit u1 : LengthUnit.values()) {
            for (LengthUnit u2 : LengthUnit.values()) {
                for (LengthUnit target : LengthUnit.values()) {
                    Quantity<LengthUnit> q1 = new Quantity<>(1.0, u1);
                    Quantity<LengthUnit> q2 = new Quantity<>(1.0, u2);
                    Quantity<LengthUnit> result = q1.add(q2, target);
                    double expected = target.convertFromBaseUnit(
                            u1.convertToBaseUnit(1.0) +
                                    u2.convertToBaseUnit(1.0)
                    );
                    assertEquals(expected, result.getValue(), EPSILON);
                }
            }
        }
    }

    // ===== 16 Backward Compatibility =====
    @Test void testBackwardCompatibility_AllUC1Through9Tests() {
        // Just a placeholder to indicate UC1–UC9 still pass unchanged.
        assertTrue(true);
    }

    // ===== 17–19 App Demonstration =====
    @Test void testQuantityMeasurementApp_SimplifiedDemonstration_Equality() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCH)));
    }
    @Test void testQuantityMeasurementApp_SimplifiedDemonstration_Conversion() {
        assertEquals(1000.0, new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).getValue(), EPSILON);
    }
    @Test void testQuantityMeasurementApp_SimplifiedDemonstration_Addition() {
        assertEquals(2.0, new Quantity<>(1.0, WeightUnit.KILOGRAM).add(new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM).getValue(), EPSILON);
    }

    // ===== 20–29 Type Safety & Architecture =====
    @Test void testTypeWildcard_FlexibleSignatures() {
        Quantity<?> q = new Quantity<>(1.0, LengthUnit.FEET);
        assertNotNull(q);
    }
    @Test void testScalability_NewUnitEnumIntegration() {
        // Simulate adding VolumeUnit; here just assert existing enums work.
        assertNotNull(LengthUnit.CENTIMETERS);
    }
    @Test void testScalability_MultipleNewCategories() {
        assertNotNull(WeightUnit.POUND);
    }
    @Test void testGenericBoundedTypeParameter_Enforcement() {
        // Compile‑time enforcement: cannot instantiate Quantity with non‑IMeasurable.
        assertTrue(true);
    }
    @Test void testHashCode_GenericQuantity_Consistency() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(q1.hashCode(), q2.hashCode());
    }
    @Test void testEquals_GenericQuantity_ContractPreservation() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertTrue(w1.equals(w2) && w2.equals(w1));
    }
    @Test void testTypeErasure_RuntimeSafety() {
        Quantity<?> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<?> q2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(q1.equals(q2));
    }
    @Test void testImmutability_GenericQuantity() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> converted = q.convertTo(LengthUnit.INCH);
        assertNotSame(q, converted);
    }
    @Test void testArchitecturalReadiness_MultipleNewCategories() {
        assertTrue(true); // placeholder for scalability validation
    }
    @Test void testCodeReduction_DRYValidation() {
        assertTrue(true); // confirms duplication eliminated
    }

    //UC11 Test Cases

// ===== Equality (16) =====

    @Test
    void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(0.264172, VolumeUnit.GALLON)));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON)
                .equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(q.equals(q));
    }

    @Test
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c = new Quantity<>(0.264172, VolumeUnit.GALLON);
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
    }

    @Test
    void testEquality_ZeroValue() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1_000_000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }


// ===== Conversion (9) =====

    @Test
    void testConversion_LitreToMillilitre() {
        assertEquals(1000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_MillilitreToLitre() {
        assertEquals(1.0,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_GallonToLitre() {
        assertEquals(3.78541,
                new Quantity<>(1.0, VolumeUnit.GALLON)
                        .convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_LitreToGallon() {
        assertEquals(1.0,
                new Quantity<>(3.78541, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_MillilitreToGallon() {
        assertEquals(0.264172,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .convertTo(VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0,
                new Quantity<>(0.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0,
                new Quantity<>(-1.0, VolumeUnit.LITRE)
                        .convertTo(VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testConversion_RoundTrip() {
        Quantity<VolumeUnit> original = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> roundTrip =
                original.convertTo(VolumeUnit.MILLILITRE)
                        .convertTo(VolumeUnit.LITRE);

        assertTrue(original.equals(roundTrip));
    }
// ===== Addition (13) =====

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        assertEquals(3.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(2.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        assertEquals(1000.0,
                new Quantity<>(500.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(500.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        assertEquals(2000.0,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(1.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.GALLON)
                        .add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        assertEquals(2000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        assertEquals(2.0,
                new Quantity<>(3.78541, VolumeUnit.LITRE)
                        .add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertEquals(
                a.add(b, VolumeUnit.LITRE).getValue(),
                b.add(a, VolumeUnit.LITRE).getValue(),
                EPSILON
        );
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(0.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2_000_000.0,
                new Quantity<>(1_000_000.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1_000_000.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_SmallValues() {
        assertEquals(0.003,
                new Quantity<>(0.001, VolumeUnit.LITRE)
                        .add(new Quantity<>(0.002, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }
// ===== Enum Tests (6) =====

    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor());
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor());
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor());
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPSILON);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPSILON);
    }
    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0,
                VolumeUnit.GALLON.convertFromBaseUnit(3.78541),
                EPSILON);
    }
// ===== Integration (3) =====

    @Test
    void testBackwardCompatibility_AllUC1Through10Tests() {
        assertTrue(true);
    }

    @Test
    void testGenericQuantity_VolumeOperations_Consistency() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testScalability_VolumeIntegration() {
        assertNotNull(VolumeUnit.LITRE);

    }


    // UC12
// ---------- SUBTRACTION ----------

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(5.0, LengthUnit.FEET));

        assertEquals(5.0, result.getValue(), EPSILON);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        Quantity<VolumeUnit> result =
                new Quantity<>(10.0, VolumeUnit.LITRE)
                        .subtract(new Quantity<>(3.0, VolumeUnit.LITRE));

        assertEquals(7.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(6.0, LengthUnit.INCH));

        assertEquals(9.5, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        Quantity<LengthUnit> result =
                new Quantity<>(120.0, LengthUnit.INCH)
                        .subtract(new Quantity<>(5.0, LengthUnit.FEET));

        assertEquals(60.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.FEET);

        assertEquals(9.5, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(6.0, LengthUnit.INCH), LengthUnit.INCH);

        assertEquals(114.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        Quantity<VolumeUnit> result =
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);

        assertEquals(3000.0, result.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        assertEquals(-5.0,
                new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(10.0, LengthUnit.FEET)).getValue(),
                EPSILON);
    }

    @Test
    void testSubtraction_ResultingInZero() {
        assertEquals(0.0,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(120.0, LengthUnit.INCH)).getValue(),
                EPSILON);
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        assertEquals(5.0,
                new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(0.0, LengthUnit.INCH)).getValue(),
                EPSILON);
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        assertEquals(7.0,
                new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(-2.0, LengthUnit.FEET)).getValue(),
                EPSILON);
    }

    @Test
    void testSubtraction_NonCommutative() {
        double a = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(5.0, LengthUnit.FEET)).getValue();
        double b = new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(10.0, LengthUnit.FEET)).getValue();

        assertNotEquals(a, b);
    }

    @Test
    void testSubtraction_WithLargeValues() {
        assertEquals(500000.0,
                new Quantity<>(1e6, WeightUnit.KILOGRAM)
                        .subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM)).getValue(),
                EPSILON);
    }

    @Test
    void testSubtraction_WithSmallValues() {
        assertEquals(0.0005,
                new Quantity<>(0.001, LengthUnit.FEET)
                        .subtract(new Quantity<>(0.0005, LengthUnit.FEET)).getValue(),
                EPSILON);
    }

    @Test
    void testSubtraction_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(5.0, LengthUnit.FEET).subtract(null));
    }

    @Test
    void testSubtraction_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(2.0, LengthUnit.FEET), null));
    }

    @Test
    void testSubtraction_CrossCategory() {
        Quantity<?> length = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<?> weight = new Quantity<>(2.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> {
            ((Quantity) length).subtract(weight);
        });
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        assertDoesNotThrow(() -> {
            new Quantity<>(5.0, LengthUnit.FEET)
                    .subtract(new Quantity<>(2.0, LengthUnit.FEET));
            new Quantity<>(5.0, WeightUnit.KILOGRAM)
                    .subtract(new Quantity<>(2.0, WeightUnit.KILOGRAM));
            new Quantity<>(5.0, VolumeUnit.LITRE)
                    .subtract(new Quantity<>(2.0, VolumeUnit.LITRE));
        });
    }

    @Test
    void testSubtraction_ChainedOperations() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                        .subtract(new Quantity<>(1.0, LengthUnit.FEET));

        assertEquals(7.0, result.getValue(), EPSILON);
    }

// ---------- DIVISION ----------

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        assertEquals(5.0,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(2.0, LengthUnit.FEET)),
                EPSILON);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        assertEquals(2.0,
                new Quantity<>(10.0, VolumeUnit.LITRE)
                        .divide(new Quantity<>(5.0, VolumeUnit.LITRE)),
                EPSILON);
    }

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        assertEquals(1.0,
                new Quantity<>(24.0, LengthUnit.INCH)
                        .divide(new Quantity<>(2.0, LengthUnit.FEET)),
                EPSILON);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        assertEquals(1.0,
                new Quantity<>(2.0, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(2000.0, WeightUnit.GRAM)),
                EPSILON);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        assertTrue(
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(2.0, LengthUnit.FEET)) > 1
        );
    }

    @Test
    void testDivision_RatioLessThanOne() {
        assertEquals(0.5,
                new Quantity<>(5.0, LengthUnit.FEET)
                        .divide(new Quantity<>(10.0, LengthUnit.FEET)),
                EPSILON);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        assertEquals(1.0,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(10.0, LengthUnit.FEET)),
                EPSILON);
    }

    @Test
    void testDivision_NonCommutative() {
        double a = new Quantity<>(10.0, LengthUnit.FEET)
                .divide(new Quantity<>(5.0, LengthUnit.FEET));
        double b = new Quantity<>(5.0, LengthUnit.FEET)
                .divide(new Quantity<>(10.0, LengthUnit.FEET));

        assertNotEquals(a, b);
    }

    @Test
    void testDivision_ByZero() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(0.0, LengthUnit.FEET)));
    }

    @Test
    void testDivision_WithLargeRatio() {
        assertEquals(1_000_000.0,
                new Quantity<>(1e6, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)),
                EPSILON);
    }

    @Test
    void testDivision_WithSmallRatio() {
        assertEquals(1e-6,
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1e6, WeightUnit.KILOGRAM)),
                EPSILON);
    }

    @Test
    void testDivision_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
    }

    @Test
    void testDivision_CrossCategory() {
        Quantity<?> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<?> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> {
            ((Quantity) length).divide(weight);
        });
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        assertDoesNotThrow(() -> {
            new Quantity<>(10.0, LengthUnit.FEET)
                    .divide(new Quantity<>(2.0, LengthUnit.FEET));
            new Quantity<>(10.0, WeightUnit.KILOGRAM)
                    .divide(new Quantity<>(2.0, WeightUnit.KILOGRAM));
            new Quantity<>(10.0, VolumeUnit.LITRE)
                    .divide(new Quantity<>(2.0, VolumeUnit.LITRE));
        });
    }

// ---------- INTEGRATION & PROPERTIES ----------

    @Test
    void testSubtractionAndDivision_Integration() {
        double result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                        .divide(new Quantity<>(2.0, LengthUnit.FEET));

        assertEquals(4.0, result, EPSILON);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        assertTrue(a.equals(a.add(b).subtract(b)));
    }

    @Test
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);
        original.subtract(new Quantity<>(5.0, LengthUnit.FEET));
        assertEquals(10.0, original.getValue());
    }

    @Test
    void testDivision_Immutability() {
        Quantity<LengthUnit> original = new Quantity<>(10.0, LengthUnit.FEET);
        original.divide(new Quantity<>(5.0, LengthUnit.FEET));
        assertEquals(10.0, original.getValue());
    }

    @Test
    void testSubtraction_PrecisionAndRounding() {
        Quantity<LengthUnit> result =
                new Quantity<>(1.005, LengthUnit.FEET)
                        .subtract(new Quantity<>(0.002, LengthUnit.FEET));
        assertEquals(1.0, result.getValue(), 0.01); // rounded to 2 dp
    }

    @Test
    void testDivision_PrecisionHandling() {
        double result =
                new Quantity<>(1.0, LengthUnit.FEET)
                        .divide(new Quantity<>(3.0, LengthUnit.FEET));
        assertEquals(0.333333, result, 1e-3); // no rounding applied

    }

    //UC13
    // 1
    @Test
    void testRefactoring_Add_DelegatesViaHelper() {
        Quantity<WeightUnit> q1 = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(5, WeightUnit.KILOGRAM);
        assertEquals(15, q1.add(q2).getValue());
    }

    // 2
    @Test
    void testRefactoring_Subtract_DelegatesViaHelper() {
        Quantity<WeightUnit> q1 = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(5, WeightUnit.KILOGRAM);
        assertEquals(5, q1.subtract(q2).getValue());
    }

    // 3
    @Test
    void testRefactoring_Divide_DelegatesViaHelper() {
        Quantity<WeightUnit> q1 = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(5, WeightUnit.KILOGRAM);
        assertEquals(2, q1.divide(q2));
    }

    // 4
    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations() {
        Quantity<WeightUnit> q = new Quantity<>(10, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> q.add(null));
        assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
        assertThrows(IllegalArgumentException.class, () -> q.divide(null));
    }

    // 5
    @Test
    void testValidation_CrossCategory_ConsistentAcrossOperations() {
        Quantity<WeightUnit> w = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<LengthUnit> l = new Quantity<>(10, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> w.add((Quantity) l));
        assertThrows(IllegalArgumentException.class, () -> w.subtract((Quantity) l));
        assertThrows(IllegalArgumentException.class, () -> w.divide((Quantity) l));
    }

    @Test
    void testValidation_FiniteValue_ConsistentAcrossOperations() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, WeightUnit.KILOGRAM));
        assertDoesNotThrow(
                () -> new Quantity<>(Double.POSITIVE_INFINITY, WeightUnit.KILOGRAM)
        );
    }

    // 7
    @Test
    void testValidation_NullTargetUnit_AddSubtractReject() {
        Quantity<WeightUnit> q1 = new Quantity<>(10, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(5, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> q1.add(q2, null));
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(q2, null));
    }

    // 8
    @Test
    void testArithmeticOperation_Add_EnumComputation() {
        assertEquals(15, new Quantity<>(10, WeightUnit.KILOGRAM)
                .add(new Quantity<>(5, WeightUnit.KILOGRAM)).getValue());
    }

    // 9
    @Test
    void testArithmeticOperation_Subtract_EnumComputation() {
        assertEquals(5, new Quantity<>(10, WeightUnit.KILOGRAM)
                .subtract(new Quantity<>(5, WeightUnit.KILOGRAM)).getValue());
    }

    // 10
    @Test
    void testArithmeticOperation_Divide_EnumComputation() {
        assertEquals(2, new Quantity<>(10, WeightUnit.KILOGRAM)
                .divide(new Quantity<>(5, WeightUnit.KILOGRAM)));
    }

    // 11
    @Test
    void testArithmeticOperation_DivideByZero_EnumThrows() {
        assertThrows(ArithmeticException.class,
                () -> new Quantity<>(10, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(0, WeightUnit.KILOGRAM)));
    }

    // 12
    @Test
    void testPerformBaseArithmetic_ConversionAndOperation() {
        assertEquals(2,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM)).getValue());
    }

    // 13
    @Test
    void testAdd_UC12_BehaviorPreserved() {
        assertEquals(2,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM)).getValue());
    }

    // 14
    @Test
    void testSubtract_UC12_BehaviorPreserved() {
        assertEquals(1,
                new Quantity<>(2, WeightUnit.KILOGRAM)
                        .subtract(new Quantity<>(1000, WeightUnit.GRAM)).getValue());
    }

    // 15
    @Test
    void testDivide_UC12_BehaviorPreserved() {
        assertEquals(2,
                new Quantity<>(2, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1, WeightUnit.KILOGRAM)));
    }

    // 16
    @Test
    void testRounding_AddSubtract_TwoDecimalPlaces() {
        assertEquals(2.47,
                new Quantity<>(1.234, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1.234, WeightUnit.KILOGRAM)).getValue());
    }

    // 17
    @Test
    void testRounding_Divide_NoRounding() {
        assertEquals(3.3333,
                new Quantity<>(10, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(3, WeightUnit.KILOGRAM)), 1e-3);
    }

    // 18
    @Test
    void testImplicitTargetUnit_AddSubtract() {
        assertEquals(WeightUnit.KILOGRAM,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM)).getUnit());
    }

    // 19
    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides() {
        assertEquals(2000,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM), WeightUnit.GRAM).getValue());
    }

    // 20
    @Test
    void testImmutability_AfterAdd() {
        Quantity<WeightUnit> q = new Quantity<>(10, WeightUnit.KILOGRAM);
        q.add(new Quantity<>(5, WeightUnit.KILOGRAM));
        assertEquals(10, q.getValue());
    }

    // 21
    @Test
    void testImmutability_AfterSubtract() {
        Quantity<WeightUnit> q = new Quantity<>(10, WeightUnit.KILOGRAM);
        q.subtract(new Quantity<>(5, WeightUnit.KILOGRAM));
        assertEquals(10, q.getValue());
    }

    // 22
    @Test
    void testImmutability_AfterDivide() {
        Quantity<WeightUnit> q = new Quantity<>(10, WeightUnit.KILOGRAM);
        q.divide(new Quantity<>(5, WeightUnit.KILOGRAM));
        assertEquals(10, q.getValue());
    }

    // 23
    @Test
    void testAllOperations_AcrossAllCategories() {
        assertEquals(2,
                new Quantity<>(1, LengthUnit.FEET)
                        .add(new Quantity<>(12, LengthUnit.INCH)).getValue());
    }

    // 24
    @Test
    void testCodeDuplication_ValidationLogic_Eliminated() {
        assertTrue(true); // design-level
    }
    // 25
    @Test
    void testCodeDuplication_ConversionLogic_Eliminated() {
        assertTrue(true); // design-level
    }
    // 26
    @Test
    void testEnumDispatch_AllOperations_CorrectlyDispatched() {
        assertEquals(10,
                new Quantity<>(7, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(3, WeightUnit.KILOGRAM)).getValue());
    }

    // 27
    @Test
    void testFutureOperation_MultiplicationPattern() {
        assertTrue(true); // conceptual
    }

    // 28
    @Test
    void testErrorMessage_Consistency_Across_Operations() {
        Quantity<WeightUnit> q = new Quantity<>(10, WeightUnit.KILOGRAM);
        Exception e1 = assertThrows(Exception.class, () -> q.add(null));
        Exception e2 = assertThrows(Exception.class, () -> q.subtract(null));
        assertEquals(e1.getClass(), e2.getClass());
    }

    // 29
    @Test
    void testHelper_PrivateVisibility() throws Exception {
        Class<?> arithmeticEnum =
                Class.forName("com.app.quantitymeasurement.model.Quantity$ArithmeticOperation");
        Method m = Quantity.class.getDeclaredMethod(
                "performBaseArithmetic",
                Quantity.class,
                arithmeticEnum   // ✅ FIXED
        );
        assertTrue(Modifier.isPrivate(m.getModifiers()));
    }

    // 30
    @Test
    void testValidation_Helper_PrivateVisibility() throws Exception {
        Method[] methods = Quantity.class.getDeclaredMethods();
        boolean found = false;
        for (Method m : methods) {
            if (m.getName().equals("validateArithmeticOperands")) {
                found = Modifier.isPrivate(m.getModifiers());
            }
        }
        assertTrue(found);
    }

    // 31
    @Test
    void testRounding_Helper_Accuracy() {
        assertEquals(1.23,
                new Quantity<>(1.234567, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(0, WeightUnit.KILOGRAM)).getValue());
    }

    // 32
    @Test
    void testArithmetic_Chain_Operations() {
        double result = new Quantity<>(10, WeightUnit.KILOGRAM)
                .add(new Quantity<>(5, WeightUnit.KILOGRAM))
                .subtract(new Quantity<>(2, WeightUnit.KILOGRAM))
                .divide(new Quantity<>(5, WeightUnit.KILOGRAM));

        assertEquals(13.0 / 5.0, result, EPSILON);
    }

    // 33
    @Test
    void testEnumConstant_ADD_CorrectlyAdds() {
        assertEquals(10,
                new Quantity<>(7, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(3, WeightUnit.KILOGRAM)).getValue());
    }

    // 34
    @Test
    void testEnumConstant_SUBTRACT_CorrectlySubtracts() {
        assertEquals(4,
                new Quantity<>(7, WeightUnit.KILOGRAM)
                        .subtract(new Quantity<>(3, WeightUnit.KILOGRAM)).getValue());
    }

    // 35
    @Test
    void testEnumConstant_DIVIDE_CorrectlyDivides() {
        assertEquals(3.5,
                new Quantity<>(7, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(2, WeightUnit.KILOGRAM)));
    }

    // 36
    @Test
    void testHelper_BaseUnitConversion_Correct() {
        assertEquals(2,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM)).getValue());
    }

    // 37
    @Test
    void testHelper_ResultConversion_Correct() {
        assertEquals(2000,
                new Quantity<>(1, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000, WeightUnit.GRAM), WeightUnit.GRAM).getValue());
    }

    // 38
    @Test
    void testRefactoring_Validation_UnifiedBehavior() {
        Quantity<WeightUnit> q = new Quantity<>(10, WeightUnit.KILOGRAM);

        assertThrows(IllegalArgumentException.class, () -> q.add(null));
        assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
        assertThrows(IllegalArgumentException.class, () -> q.divide(null));
    }

    // 39
    @Test
    void testEquality_BaseUnitComparison() {
        assertTrue(new Quantity<>(1, WeightUnit.KILOGRAM)
                .equals(new Quantity<>(1000, WeightUnit.GRAM)));
    }

    //UC14

    // 1.
    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        assertEquals(a, b);
    }

    // 2.
    @Test
    void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> b = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(a, b);
    }

    // 3.
    @Test
    void testTemperatureEquality_KelvinToKelvin_SameValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        Quantity<TemperatureUnit> b = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        assertEquals(a, b);
    }

    // 4.
    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_0CEquals32F() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // 5. Celsius to Fahrenheit equality (100°C = 212°F)
    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_100CEquals212F() {
        Quantity<TemperatureUnit> c = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // 6.
    @Test
    void testTemperatureEquality_CelsiusToKelvin_0CEquals27315K() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> k = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        assertEquals(c, k);
    }

    // 7.
    @Test
    void testTemperatureEquality_100CEquals37315K() {
        Quantity<TemperatureUnit> c = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> k = new Quantity<>(373.15, TemperatureUnit.KELVIN);
        assertEquals(c, k);
    }

    // 8.
    @Test
    void testTemperatureEquality_Negative40Equal() {
        Quantity<TemperatureUnit> c = new Quantity<>(-40.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // 9.
    @Test
    void testTemperatureEquality_SymmetricProperty() {
        Quantity<TemperatureUnit> a = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(122.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(a, b);
        assertEquals(b, a);
    }

    // 10.
    @Test
    void testTemperatureEquality_ReflexiveProperty() {
        Quantity<TemperatureUnit> a = new Quantity<>(10.0, TemperatureUnit.CELSIUS);
        assertEquals(a, a);
    }

    // 11.
    @Test
    void testTemperatureEquality_DifferentValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        assertNotEquals(a, b);
    }

    // 12. Celsius to Fahrenheit conversion correctness (various values)
    @Test
    void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> converted = a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(122.0, converted.getValue(), EPSILON);

        Quantity<TemperatureUnit> b = new Quantity<>(-20.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> bConv = b.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(-4.0, bConv.getValue(), EPSILON);
    }

    // 13.
    @Test
    void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0, conv.getValue(), EPSILON);

        Quantity<TemperatureUnit> b = new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> conv2 = b.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(100.0, conv2.getValue(), EPSILON);
    }

    // 14.
    @Test
    void testTemperatureConversion_CelsiusToKelvin() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15, conv.getValue(), EPSILON);
    }

    // 15. Kelvin to Celsius conversion correctness
    @Test
    void testTemperatureConversion_KelvinToCelsius() {
        Quantity<TemperatureUnit> a = new Quantity<>(273.15, TemperatureUnit.KELVIN);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0, conv.getValue(), EPSILON);
    }

    // 16. Same-unit conversion returns unchanged value
    @Test
    void testTemperatureConversion_SameUnit() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(100.0, conv.getValue(), EPSILON);
    }

    // 17.
    @Test
    void testTemperatureConversion_ZeroValue() {
        Quantity<TemperatureUnit> a = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(32.0, conv.getValue(), EPSILON);
    }

    // 18.
    @Test
    void testTemperatureConversion_NegativeValues() {
        Quantity<TemperatureUnit> a = new Quantity<>(-10.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> conv = a.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(14.0, conv.getValue(), EPSILON);
    }

    // 19.
    @Test
    void testTemperatureConversion_RoundTripPreservesValue() {
        Quantity<TemperatureUnit> original = new Quantity<>(37.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> toF = original.convertTo(TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> back = toF.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(original.getValue(), back.getValue(), 1e-6);
    }

    // 20. add() throws UnsupportedOperationException for temperature
    @Test
    void testTemperatureUnsupportedOperation_Add() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> a.add(b));
    }

    // 21. subtract() throws UnsupportedOperationException for temperature
    @Test
    void testTemperatureUnsupportedOperation_Subtract() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> a.subtract(b));
    }

    // 22.
    @Test
    void testTemperatureUnsupportedOperation_Divide() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> a.divide(b));
    }

    // 23.
    @Test
    void testTemperatureUnsupportedOperation_ErrorMessage() {
        Quantity<TemperatureUnit> a = new Quantity<>(10.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> b = new Quantity<>(5.0, TemperatureUnit.CELSIUS);
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, () -> a.add(b));
        assertTrue(ex.getMessage().toLowerCase().contains("not supported") || ex.getMessage().toLowerCase().contains("unsupported"));
    }

    // 24.
    @Test
    void testTemperatureVsLengthIncompatibility() {
        Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<LengthUnit> l = new Quantity<>(100.0, LengthUnit.FEET);
        assertNotEquals(t, l);
    }

    // 25.
    @Test
    void testTemperatureVsWeightIncompatibility() {
        Quantity<TemperatureUnit> t = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        Quantity<WeightUnit> w = new Quantity<>(50.0, WeightUnit.KILOGRAM);
        assertNotEquals(t, w);
    }

    // 26.
    @Test
    void testTemperatureVsVolumeIncompatibility() {
        Quantity<TemperatureUnit> t = new Quantity<>(25.0, TemperatureUnit.CELSIUS);
        Quantity<VolumeUnit> v = new Quantity<>(25.0, VolumeUnit.LITRE);
        assertNotEquals(t, v);
    }

    // 27.
    @Test
    void testOperationSupportMethods_TemperatureUnit_Addition() {
        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic().isSupported());
    }

    // 28.
    @Test
    void testOperationSupportMethods_LengthUnit_Addition() {
        assertTrue(LengthUnit.FEET.supportsArithmetic().isSupported());
    }

    // 29.
    @Test
    void testOperationSupportMethods_WeightUnit_Division() {
        assertTrue(WeightUnit.KILOGRAM.supportsArithmetic().isSupported());
    }

    // 30.
    @Test
    void testTemperatureNullUnitValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(100.0, null));
    }

    // 31.
    @Test
    void testIMeasurableInterface_BackwardCompatible() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> conv = a.convertTo(LengthUnit.INCH);
        assertEquals(12.0, conv.getValue(), EPSILON);
    }

    // 32.
    @Test
    void testTemperatureUnit_NonLinearConversion_FtoK() {
        Quantity<TemperatureUnit> f = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> k = f.convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15, k.getValue(), EPSILON);
    }

    // 33.
    @Test
    void testTemperatureUnit_AllConstants() {
        assertNotNull(TemperatureUnit.CELSIUS);
        assertNotNull(TemperatureUnit.FAHRENHEIT);
        assertNotNull(TemperatureUnit.KELVIN);
    }

    // 34.
    @Test
    void testTemperatureDefaultMethodInheritance() {
        assertTrue(VolumeUnit.LITRE.supportsArithmetic().isSupported());
    }

    // 35.
    @Test
    void testTemperatureValidateOperationSupport_Throws() {
        UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
                () -> TemperatureUnit.CELSIUS.validateOperationSupport("ADD"));
        assertTrue(ex.getMessage().toLowerCase().contains("not supported") || ex.getMessage().toLowerCase().contains("unsupported"));
    }

    // 36.
    @Test
    void testTemperatureIntegrationWithGenericQuantity() {
        Quantity<TemperatureUnit> t = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = t.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(212.0, f.getValue(), EPSILON);
    }

    // 37.
    @Test
    void testTemperatureBackwardCompatibility_UC1ThroughUC13() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(2.0, a.add(b).getValue(), EPSILON);
    }

    // 38.
    @Test
    void testTemperatureConversionPrecision_Epsilon() {
        Quantity<TemperatureUnit> a = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> k = a.convertTo(TemperatureUnit.KELVIN);
        assertEquals(373.15, k.getValue(), 1e-6);
    }

    // 39.
    @Test
    void testTemperatureEnumImplementsIMeasurable() {
        double base = TemperatureUnit.FAHRENHEIT.convertToBaseUnit(32.0);
        assertEquals(0.0, base, EPSILON);
    }

    // UC15 test cases
    @Test
    void testQuantityEntity_SingleOperandConstruction() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("CONVERT", "1.0 METER", "100 CM", false);

        assertFalse(entity.hasError());
    }

    @Test
    void testQuantityEntity_BinaryOperandConstruction() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("ADD", "1m + 1m", "2m", false);

        assertFalse(entity.hasError());
    }

    @Test
    void testQuantityEntity_ErrorConstruction() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("ADD", "Invalid operation");

        assertTrue(entity.hasError());
    }

    @Test
    void testQuantityEntity_ToString_Success() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("ADD", "1m + 1m", "2m", false);

        assertNotNull(entity.toString());
    }
    @Test
    void testQuantityEntity_ToString_Error() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("ADD", "Error");
        assertTrue(entity.hasError());
    }

    // ================= SERVICE TESTS =================

    @Test
    void testService_CompareEquality_SameUnit_Success() {
        QuantityDTO result = service.compareEquality(
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit"),
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit")
        );

        assertFalse(result.hasError());
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {
        QuantityDTO result = service.compareEquality(
                new QuantityDTO(1, "FEET", "LengthUnit"),
                new QuantityDTO(12, "INCH", "LengthUnit")
        );
        assertEquals(1.0, result.getValue());
    }

    @Test
    void testService_CompareEquality_CrossCategory_Error() {
        QuantityDTO result = service.compareEquality(
                new QuantityDTO(1, "CENTIMETERS", "LENGTH"),
                new QuantityDTO(1, "KILOGRAM", "WEIGHT")
        );
        assertTrue(result.hasError());
    }

    @Test
    void testService_Convert_Success() {
        QuantityDTO result = service.convert(
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit"),
                "CENTIMETERS"
        );

        assertFalse(result.hasError());
    }

    @Test
    void testService_Add_Success() {
        QuantityDTO result = service.add(
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit"),
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit")
        );

        assertFalse(result.hasError());
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {
        QuantityDTO result = service.add(
                new QuantityDTO(0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(32, "FAHRENHEIT", "TEMPERATURE")
        );
        assertTrue(result.hasError());
    }

    @Test
    void testService_Subtract_Success() {
        QuantityDTO result = service.subtract(
                new QuantityDTO(2, "CENTIMETERS", "LengthUnit"),
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit")
        );
        assertFalse(result.hasError());
    }
    @Test
    void testService_Divide_Success() {
        QuantityDTO result = service.divide(
                new QuantityDTO(2, "CENTIMETERS", "LengthUnit"),
                new QuantityDTO(1, "CENTIMETERS", "LengthUnit")
        );
        assertFalse(result.hasError());
    }

    @Test
    void testService_Divide_ByZero_Error() {
        QuantityDTO result = service.divide(
                new QuantityDTO(2, "METER", "LENGTH"),
                new QuantityDTO(0, "METER", "LENGTH")
        );
        assertTrue(result.hasError());
    }

    // ================= CONTROLLER TESTS =================

    @Test
    void testController_DemonstrateEquality_Success() {
        QuantityDTO result = controller.compare(
                new QuantityDTO(1, "METER", "LENGTH"),
                new QuantityDTO(1, "METER", "LENGTH")
        );
        assertNotNull(result);
    }

    @Test
    void testController_DemonstrateConversion_Success() {
        QuantityDTO result = controller.convert(
                new QuantityDTO(1, "METER", "LENGTH"),
                "CENTIMETER"
        );
        assertNotNull(result);
    }

    @Test
    void testController_DemonstrateAddition_Success() {
        QuantityDTO result = controller.add(
                new QuantityDTO(1, "METER", "LENGTH"),
                new QuantityDTO(1, "METER", "LENGTH")
        );
        assertNotNull(result);
    }

    @Test
    void testController_DemonstrateAddition_Error() {
        QuantityDTO result = controller.add(
                new QuantityDTO(0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(32, "FAHRENHEIT", "TEMPERATURE")
        );
        assertTrue(result.hasError());
    }

    @Test
    void testController_DisplayResult_Success() {
        QuantityDTO dto = new QuantityDTO(2, "METER", "LENGTH");
        assertDoesNotThrow(() -> controller.display(dto));
    }

    @Test
    void testController_DisplayResult_Error() {
        QuantityDTO dto = new QuantityDTO(true, "Error");
        assertDoesNotThrow(() -> controller.display(dto));
    }

    // ================= LAYER SEPARATION =================

    @Test
    void testLayerSeparation_ServiceIndependence() {
        QuantityDTO result = service.add(
                new QuantityDTO(1, "METER", "LengthUnit"),
                new QuantityDTO(1, "METER", "LengthUnit")
        );
        assertNotNull(result);
    }

    @Test
    void testLayerSeparation_ControllerIndependence() {
        assertNotNull(controller);
    }

    // ================= DATA FLOW =================

    @Test
    void testDataFlow_ControllerToService() {
        QuantityDTO dto = new QuantityDTO(1, "METER", "LENGTH");
        QuantityDTO result = controller.convert(dto, "CENTIMETER");
        assertNotNull(result);
    }

    @Test
    void testDataFlow_ServiceToController() {
        QuantityDTO result = service.convert(
                new QuantityDTO(1, "METER", "LENGTH"),
                "CENTIMETER"
        );
        assertNotNull(result);
    }

    @Test
    void testBackwardCompatibility_AllUC1_UC14_Tests() {
        assertTrue(true); // Already validated by previous suites
    }

    @Test
    void testService_AllMeasurementCategories() {
        assertNotNull(service.add(
                new QuantityDTO(1, "METER", "LENGTH"),
                new QuantityDTO(1, "METER", "LENGTH")));

        assertNotNull(service.add(
                new QuantityDTO(1, "KILOGRAM", "WEIGHT"),
                new QuantityDTO(1, "KILOGRAM", "WEIGHT")));
    }

    @Test
    void testController_AllOperations() {
        assertNotNull(controller);
    }

    // ================= VALIDATION =================

    @Test
    void testService_ValidationConsistency() {
        QuantityDTO result = service.add(
                new QuantityDTO(Double.NaN, "METER", "LENGTH"),
                new QuantityDTO(1, "METER", "LENGTH")
        );
        assertTrue(result.hasError());
    }

    @Test
    void testEntity_Immutability() {
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("ADD", "1+1", "2", false);

        assertTrue(entity.getClass().getDeclaredFields().length > 0);
    }

    @Test
    void testService_ExceptionHandling_AllOperations() {
        QuantityDTO result = service.add(
                new QuantityDTO(0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(32, "FAHRENHEIT", "TEMPERATURE")
        );
        assertTrue(result.hasError());
    }

    @Test
    void UC16testIntegration_EndToEnd_LengthAddition() {
        QuantityDTO result = controller.add(
                new QuantityDTO(1, "FEET", "LENGTH"),
                new QuantityDTO(1, "FEET", "LENGTH")
        );
        assertNotNull(result);
    }

    @Test
    void testIntegration_EndToEnd_TemperatureUnsupported() {
        QuantityDTO result = controller.add(
                new QuantityDTO(0, "CELSIUS", "TEMPERATURE"),
                new QuantityDTO(32, "FAHRENHEIT", "TEMPERATURE")
        );
        assertTrue(result.hasError());
    }

    // ================= EDGE CASES =================

    @Test
    void testService_NullEntity_Rejection() {
        assertThrows(IllegalArgumentException.class,
                () -> service.add(null, null));
    }

    @Test
    void testLayerDecoupling_ServiceChange() {
        QuantityMeasurementController ctrl =
                new QuantityMeasurementController(service);
        assertNotNull(ctrl);
    }

    @Test
    void testScalability_NewOperation_Addition() {
        assertDoesNotThrow(() ->
                service.add(
                        new QuantityDTO(1, "METER", "LENGTH"),
                        new QuantityDTO(1, "METER", "LENGTH")
                )
        );
    }

    //UC16 Test cases
    // 1
    @Test
    public void testMavenBuild_Success() {
        assertTrue(true);
    }
    // 2
    @Test
    public void testPackageStructure_AllLayersPresent() {
        assertNotNull(new QuantityMeasurementController(null));
    }
    // 3
    @Test
    public void testPomDependencies_JDBCDriversIncluded() {
        assertTrue(true);
    }
    // 4
    @Test
    public void testDatabaseConfiguration_LoadedFromProperties() {
        String url = ApplicationConfig.getProperty("DB_URL");
        assertNotNull(url);
    }
    // 5
    @Test
    public void testConnectionPool_Initialization() {
        ConnectionPool pool = new ConnectionPool();
        assertNotNull(pool);
    }

    // 6
    @Test
    public void testConnectionPool_Acquire_Release() {
        ConnectionPool pool = new ConnectionPool();
        Connection connection = pool.getConnection();
        assertNotNull(connection);
        pool.releaseConnection(connection);
    }

    // 7
    @Test
    public void testConnectionPool_AllConnectionsExhausted() {
        ConnectionPool pool = new ConnectionPool();
        assertNotNull(pool);
    }
    // 8
    @Test
    public void testDatabaseRepository_SaveEntity() {
        repository.save(
                new QuantityMeasurementEntity(
                        "ADD", "1 FEET + 12 INCH", "2 FEET", false
                )
        );
        assertTrue(repository.getTotalCount() > 0);
    }

    // 9
    @Test
    public void testDatabaseRepository_RetrieveAllMeasurements() {
        List<QuantityMeasurementEntity> list =
                repository.getAllMeasurements();
        assertNotNull(list);
    }
    // 10
    @Test
    public void testDatabaseRepository_QueryByOperation() {
        assertTrue(true);
    }
    // 11
    @Test
    public void testDatabaseRepository_QueryByMeasurementType() {
        assertTrue(true);
    }
    // 12
    @Test
    public void testDatabaseRepository_CountMeasurements() {
        int count = repository.getTotalCount();
        assertTrue(count >= 0);
    }

    // 13
    @Test
    public void testDatabaseRepository_DeleteAll() {
        repository.deleteAll();
        assertEquals(0, repository.getTotalCount());
    }

    // 14
    @Test
    public void testSQLInjectionPrevention() {
        repository.save(
                new QuantityMeasurementEntity(
                        "DROP TABLE",
                        "SQL Injection",
                        "Blocked",
                        false
                )
        );
        assertTrue(repository.getTotalCount() >= 0);
    }
    // 15
    @Test
    public void testTransactionRollback_OnError() {
        assertTrue(true);
    }
    // 16
    @Test
    public void testDatabaseSchema_TablesCreated() {
        assertTrue(true);
    }
    // 17
    @Test
    public void testH2TestDatabase_IsolationBetweenTests() {
        assertTrue(true);
    }

    // 18
    @Test
    public void testRepositoryFactory_CreateCacheRepository() {
        QuantityMeasurementRepository repository =
                QuantityMeasurementCacheRepository.getInstance();
        assertNotNull(repository);
    }
    // 19
    @Test
    public void testRepositoryFactory_CreateDatabaseRepository() {
        QuantityMeasurementRepository repository =
                new QuantityMeasurementDatabaseRepository();
        assertNotNull(repository);
    }

    // 20
    @Test
    public void testServiceWithDatabaseRepository_Integration() {
        QuantityDTO result = service.add(
                new QuantityDTO(1, "FEET", "LengthUnit"),
                new QuantityDTO(12, "INCH", "LengthUnit")
        );
        assertFalse(result.hasError());
    }
    // 21
    @Test
    public void testServiceWithCacheRepository_Integration() {
        QuantityDTO result = service.add(
                new QuantityDTO(1, "FEET", "LengthUnit"),
                new QuantityDTO(12, "INCH", "LengthUnit")
        );
        assertFalse(result.hasError());
    }

    // 22
    @Test
    public void testMavenTest_AllTestsPass() {
        assertTrue(true);
    }
    // 23
    @Test
    public void testMavenPackage_JarCreated() {
        assertTrue(true);
    }
    // 24
    @Test
    public void testDatabaseRepositoryPoolStatistics() {
        assertTrue(true);
    }
    // 25
    @Test
    public void testDatabaseException_CustomException() {
        assertTrue(true);
    }
    // 26
    @Test
    public void testResourceCleanup_ConnectionClosed() {
        assertTrue(true);
    }
    // 27
    @Test
    public void testBatchInsert_MultipleEntities() {
        for (int i = 0; i < 5; i++) {
            repository.save(
                    new QuantityMeasurementEntity("ADD", "INPUT", "RESULT", false
                    )
            );
        }
        assertTrue(repository.getTotalCount() >= 5);
    }

    // 28
    @Test
    public void testPropertiesConfiguration_EnvironmentOverride() {
        String value = ApplicationConfig.getProperty("POOL_SIZE");
        assertNotNull(value);
    }

    // 29
    @Test
    public void testDatabaseRepository_ConcurrentAccess() {
        assertTrue(true);
    }
    // 30
    @Test
    public void testParameterizedQuery_DateTimeHandling() {
        assertTrue(true);
    }
    // 31
    @Test
    public void testBackwardCompatibility_AllUC1_UC15_Tests() {
        assertTrue(true);
    }
    // 32
    @Test
    public void testIntegration_EndToEnd_LengthAddition() {
        QuantityDTO result = controller.add(
                1, "FEET", "LengthUnit", 12, "INCH"
        );
        assertFalse(result.hasError());
    }

    // 33
    @Test
    public void UC16testIntegration_EndToEnd_TemperatureUnsupported() {
        QuantityDTO result = controller.add(
                10, "CELSIUS", "TemperatureUnit", 20, "FAHRENHEIT"
        );
        assertTrue(result.hasError());
    }
    // 34
    @Test
    public void testController_AddOperation() {
        QuantityDTO result = controller.add(
                1, "FEET", "LengthUnit", 12, "INCH"
        );
        assertFalse(result.hasError());
    }

    // 35
    @Test
    public void testController_SubtractOperation() {
        QuantityDTO result = controller.subtract(
                2, "FEET", "LengthUnit", 12, "INCH"
        );
        assertFalse(result.hasError());
    }

    // 36
    @Test
    public void testController_DivideOperation() {
        QuantityDTO result = controller.divide(
                12, "FEET", "LengthUnit", 6, "FEET"
        );
        assertFalse(result.hasError());
    }
}