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

		//assertTrue(q1.equals(q2));
		assertEquals(q1, q2);
	}

	//  2. Inch to Inch (Same Value)
	@Test
	void testEquality_InchToInch_SameValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.INCH);

		//assertTrue(q1.equals(q2));
		assertEquals(q1, q2);
	}

	//  3. Feet to Inch (Equivalent)
	@Test
	void testEquality_FeetToInch_EquivalentValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

		//assertTrue(q1.equals(q2));
		assertEquals(q1, q2);
	}

	//  4. Inch to Feet (Symmetry)
	@Test
	void testEquality_InchToFeet_EquivalentValue() {
		QuantityLength q1 = new QuantityLength(12.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(1.0, LengthUnit.FEET);

		//assertTrue(q1.equals(q2));
		assertEquals(q1, q2);
	}

	//  5. Feet to Feet (Different Value)
	@Test
	void testEquality_FeetToFeet_DifferentValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(2.0, LengthUnit.FEET);

		//assertFalse(q1.equals(q2));
		assertEquals(q1, q2);
	}

	//  6. Inch to Inch (Different Value)
	@Test
	void testEquality_InchToInch_DifferentValue() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.INCH);
		QuantityLength q2 = new QuantityLength(2.0, LengthUnit.INCH);

		//assertFalse(q1.equals(q2));
		assertEquals(q1, q2);
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

	// 9. Same Reference (Reflexive)
	@Test
	void testEquality_SameReference() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

		//assertTrue(q1.equals(q1));
		assertEquals(q1, q1);
	}

	// 10. Null Comparison
	@Test
	void testEquality_NullComparison() {
		QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);

		//assertFalse(q1.equals(null));
		assertEquals(q1, null);


	}
}