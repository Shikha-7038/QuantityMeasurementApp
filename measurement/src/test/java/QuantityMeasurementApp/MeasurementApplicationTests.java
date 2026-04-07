package QuantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import QuantityMeasurementApp.model.QuantityLength;
import QuantityMeasurementApp.enums.LengthUnit;

//@SpringBootTest
class MeasurementApplicationTests {
	private static final double EPSILON = 1e-6;
	// 1
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

}