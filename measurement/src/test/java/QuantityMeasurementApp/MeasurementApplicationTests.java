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
	//18
	@Test
	void testConversion_FeetToInches() {
		assertEquals(12.0,
				QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCH),
				EPSILON);
	}
	//19
	@Test
	void testConversion_InchesToFeet() {
		assertEquals(2.0,
				QuantityLength.convert(24.0, LengthUnit.INCH, LengthUnit.FEET),
				EPSILON);
	}
	//20
	@Test
	void testConversion_YardsToInches() {
		assertEquals(36.0,
				QuantityLength.convert(1.0, LengthUnit.YARD, LengthUnit.INCH),
				EPSILON);
	}
	//21
	@Test
	void testConversion_InchesToYards() {
		assertEquals(2.0,
				QuantityLength.convert(72.0, LengthUnit.INCH, LengthUnit.YARD),
				EPSILON);
	}
	//22
	@Test
	void testConversion_CentimetersToInches() {
		assertEquals(1.0,
				QuantityLength.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCH),
				EPSILON);
	}
	//23
	@Test
	void testConversion_FeetToYard() {
		assertEquals(2.0,
				QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARD),
				EPSILON);
	}
	//24
	@Test
	void testConversion_ZeroValue() {
		assertEquals(0.0,
				QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCH),
				EPSILON);
	}
	//25
	@Test
	void testConversion_NegativeValue() {
		assertEquals(-12.0,
				QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCH),
				EPSILON);
	}
	//26
	@Test
	void testConversion_RoundTrip() {
		double original = 5.0;

		double converted = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCH);
		double back = QuantityLength.convert(converted, LengthUnit.INCH, LengthUnit.FEET);

		assertEquals(original, back, EPSILON);
	}
	//27
	@Test
	void testConversion_InvalidUnit_Throws() {
		assertThrows(IllegalArgumentException.class, () -> {
			QuantityLength.convert(1.0, null, LengthUnit.FEET);
		});
	}
	//28
	@Test
	void testConversion_NaNOrInfinite_Throws() {
		assertThrows(IllegalArgumentException.class, () -> {
			QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCH);
		});
	}
	//29
	@Test
	void testConversion_PrecisionTolerance() {
		double result = QuantityLength.convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH);
		assertEquals(0.393701, result, 1e-6);
	}

	// UC6 Test Cases------------------------------------------------------------------------
	// 1
	@Test
	void testAddition_SameUnit_FeetPlusFeet() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

		assertEquals(new QuantityLength(3.0, LengthUnit.FEET), q1.add(q2));
	}

	// 2
	@Test
	void testAddition_SameUnit_InchPlusInch() {
		QuantityLength q1 = new QuantityLength(6.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(6.0, LengthUnit.INCH);

		assertEquals(new QuantityLength(12.0, LengthUnit.INCH), q1.add(q2));
	}

	// 3
	@Test
	void testAddition_CrossUnit_FeetPlusInches() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

		assertEquals(new QuantityLength(2.0, LengthUnit.FEET), q1.add(q2));
	}

	// 4
	@Test
	void testAddition_CrossUnit_InchPlusFeet() {
		QuantityLength q1 = new QuantityLength(12.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

		assertEquals(new QuantityLength(24.0, LengthUnit.INCH), q1.add(q2));
	}

	// 5
	@Test
	void testAddition_CrossUnit_YardPlusFeet() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARD);
		QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);

		assertEquals(new QuantityLength(2.0, LengthUnit.YARD), q1.add(q2));
	}

	// 6
	@Test
	void testAddition_CrossUnit_CentimeterPlusInch() {
		QuantityLength q1 = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCH);

		QuantityLength result = q1.add(q2);

		assertEquals(5.08, result.toConvert(LengthUnit.CENTIMETERS), 1e-6);
	}

	// 7
	@Test
	void testAddition_Commutativity() {
		QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength b = new QuantityLength(12.0, LengthUnit.INCH);

		double sum1 = a.add(b).toConvert(LengthUnit.FEET);
		double sum2 = b.add(a).toConvert(LengthUnit.FEET);

		assertEquals(sum1, sum2, 1e-6);
	}

	// 8
	@Test
	void testAddition_WithZero() {
		QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(0.0, LengthUnit.INCH);

		assertEquals(new QuantityLength(5.0, LengthUnit.FEET), q1.add(q2));
	}

	// 9
	@Test
	void testAddition_NegativeValues() {
		QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(-2.0, LengthUnit.FEET);

		assertEquals(new QuantityLength(3.0, LengthUnit.FEET), q1.add(q2));
	}

	// 10
	@Test
	void testAddition_NullSecondOperand() {
		QuantityLength q1 = new QuantityLength(5.0, LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> q1.add(null));
	}

	// 11
	@Test
	void testAddition_LargeValues() {
		QuantityLength q1 = new QuantityLength(1_000_000.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(1_000_000.0, LengthUnit.FEET);

		assertEquals(new QuantityLength(2_000_000.0, LengthUnit.FEET), q1.add(q2));
	}

	// 12
	@Test
	void testAddition_SmallValues() {
		QuantityLength q1 = new QuantityLength(0.001, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(0.002, LengthUnit.FEET);

		QuantityLength result = q1.add(q2);

		assertEquals(0.003, result.toConvert(LengthUnit.FEET), 1e-6);
	}

}