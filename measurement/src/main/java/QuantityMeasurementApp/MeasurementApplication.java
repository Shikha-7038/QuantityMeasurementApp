package QuantityMeasurementApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

public class MeasurementApplication {


	// Inner class representing Feet measurement
	static class Feet {
		private final double value;


		public Feet(double value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			// Reflexive check
			if (this == obj) {
				return true;
			}

			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}

			Feet other = (Feet) obj;
			return Double.compare(this.value, other.value) == 0;
		}

	}

	// Inner class representing Inches measurement
	static class Inches {
		private final double value;

		public Inches(double value) {
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			Inches other = (Inches) obj;
			return Double.compare(this.value, other.value) == 0;
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(MeasurementApplication.class,args);

		Feet f1 = new Feet(34.5);
		Feet f2 = new Feet(34.5);

		System.out.println(f1.equals(f2));

		Inches i1 = new Inches(12.0);
		Inches i2 = new Inches(12.0);

		System.out.println("Inches comparison: " + i1.equals(i2));

	}
}