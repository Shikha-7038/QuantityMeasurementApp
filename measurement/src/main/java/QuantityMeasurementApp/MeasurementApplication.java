package QuantityMeasurementApp;
//import org.springframework.boot.SpringApplication;
import QuantityMeasurementApp.enums.LengthUnit;
import QuantityMeasurementApp.model.QuantityLength;
public class MeasurementApplication {
	public static void main(String[] args) {
		//SpringApplication.run(MeasurementApplication.class,args);
		QuantityLength q1 = new QuantityLength(1, LengthUnit.FEET);
		QuantityLength q2 = new QuantityLength(12, LengthUnit.INCH);

		System.out.println(q1 + " and " + q2 + " Equal (" + q1.equals(q2) + ")");

		QuantityLength q3 = new QuantityLength(1, LengthUnit.INCH);
		QuantityLength q4 = new QuantityLength(1, LengthUnit.INCH);

		System.out.println(q3 + " and " + q4 + " Equal (" + q3.equals(q4) + ")");
	}
}
