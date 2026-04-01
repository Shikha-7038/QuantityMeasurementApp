package QuantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import QuantityMeasurementApp.model.QuantityLength;
import QuantityMeasurementApp.enums.LengthUnit;
//@SpringBootTest
class MeasurementApplicationTests {

	@Test
	void testEquality_FeetToFeet_SameValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

		assertTrue(q1.equals(q2));

	}
	//  2. Inch to Inch (Same Value)
	@Test
	void testEquality_InchToInch_SameValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCH);

		assertTrue(q1.equals(q2));
	}

	//  3. Feet to Inch (Equivalent)
	@Test
	void testEquality_FeetToInch_EquivalentValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

		assertTrue(q1.equals(q2));

	}

	//  4. Inch to Feet (Symmetry)
	@Test
	void testEquality_InchToFeet_EquivalentValue() {
		QuantityLength q1 = new QuantityLength(12.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

		assertTrue(q1.equals(q2));

	}

	// 5. Feet to Feet (Different Value)
	@Test
	void testEquality_FeetToFeet_DifferentValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

		assertFalse(q1.equals(q2));

	}

	//  6. Inch to Inch (Different Value)
	@Test
	void testEquality_InchToInch_DifferentValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(2.0, LengthUnit.INCH);

		assertFalse(q1.equals(q2));

	}

	//  7. Invalid Unit (Handled by Enum → compile-time safety)
	// Instead we test null unit
	@Test
	void testEquality_InvalidUnit() {
		assertThrows(IllegalArgumentException.class, () -> {
			new QuantityLength(1.0, null);
		});
	}

	//  8. Null Unit Handling
	@Test
	void testEquality_NullUnit() {
		assertThrows(IllegalArgumentException.class, () -> {
			new QuantityLength(5.0, null);
		});
	}

	//  9. Same Reference (Reflexive)
	@Test
	void testEquality_SameReference() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

		assertTrue(q1.equals(q1));
	}

	//  10. Null Comparison
	@Test
	void testEquality_NullComparison() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

		assertFalse(q1.equals(null));

	}
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
		var q = new QuantityLength(1.0, LengthUnit.YARD);
		assertTrue(q.equals(q));
	}

	// 13
	@Test
	void testEquality_YardNullComparison() {
		var q = new QuantityLength(1.0, LengthUnit.YARD);
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
		var q = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
		assertTrue(q.equals(q));
	}

	// 16
	@Test
	void testEquality_CentimetersNullComparison() {
		var q = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
		assertFalse(q.equals(null));
	}

	// 17
	@Test
	void testEquality_AllUnits_ComplexScenario() {
		var yard = new QuantityLength(2.0, LengthUnit.YARD);
		var feet = new QuantityLength(6.0, LengthUnit.FEET);
		var inch = new QuantityLength(72.0, LengthUnit.INCH);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inch));
		assertTrue(yard.equals(inch));
	}


}