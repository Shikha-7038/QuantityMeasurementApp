package QuantityMeasurementApp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
class MeasurementApplicationTests {

	@Test
	void testFeetEquality_SameValue() {
		MeasurementApplication.Feet f1=new MeasurementApplication.Feet(89.6);
		MeasurementApplication.Feet f2=new MeasurementApplication.Feet(89.6);

		assertEquals(f1,f2);
	}
	@Test
	void testFeetInEquality_DifferValue(){
		MeasurementApplication.Feet f1=new MeasurementApplication.Feet(89.6);
		MeasurementApplication.Feet f2=new MeasurementApplication.Feet(34.6);

		assertNotEquals(f1,f2);
	}

	@Test
	void testFeetNullable_NullValue(){
		MeasurementApplication.Feet f1=new MeasurementApplication.Feet(55.0);
		assertFalse(f1.equals(null));
	}

	@Test
	void testFeetNullEquality_ClassComparison(){
		MeasurementApplication.Feet f1=new MeasurementApplication.Feet(89.6);
		assertFalse(f1.equals("Some String"));

	}

	@Test
	void testFeetNullEquality_SameReference(){
		MeasurementApplication.Feet f1=new MeasurementApplication.Feet(89.6);
		assertTrue(f1.equals(f1));

	}

	// ===== Inches Tests =====
	@Test
	void testInchesEquality_SameValue() {
		MeasurementApplication.Inches i1 = new MeasurementApplication.Inches(12.0);
		MeasurementApplication.Inches i2 = new MeasurementApplication.Inches(12.0);

		assertEquals(i1, i2);
	}

	@Test
	void testInchesInEquality_DifferValue() {
		MeasurementApplication.Inches i1 = new MeasurementApplication.Inches(12.0);
		MeasurementApplication.Inches i2 = new MeasurementApplication.Inches(10.0);

		assertNotEquals(i1, i2);
	}

	@Test
	void testInchesNullable_NullValue() {
		MeasurementApplication.Inches i1 = new MeasurementApplication.Inches(15.0);
		assertFalse(i1.equals(null));
	}

	@Test
	void testInchesNullEquality_ClassComparison() {
		MeasurementApplication.Inches i1 = new MeasurementApplication.Inches(20.0);
		assertFalse(i1.equals("Some String"));
	}

	@Test
	void testInchesNullEquality_SameReference() {
		MeasurementApplication.Inches i1 = new MeasurementApplication.Inches(25.0);
		assertTrue(i1.equals(i1));
	}

}