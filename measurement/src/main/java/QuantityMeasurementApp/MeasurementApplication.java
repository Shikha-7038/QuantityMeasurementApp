package QuantityMeasurementApp;
//import org.springframework.boot.SpringApplication;
import QuantityMeasurementApp.enums.LengthUnit;
import QuantityMeasurementApp.model.QuantityLength;
public class MeasurementApplication {
	public static void main(String[] args) {
		//SpringApplication.run(MeasurementApplication.class,args);
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        QuantityLength converted = q1.convert(LengthUnit.INCH);
        System.out.println("1 foot in inches: " + converted);

        QuantityLength sum = q1.add(q2);
        System.out.println("1 ft + 12 in = " + sum);

        System.out.println("1 ft == 12 in: " + q1.equals(q2));

	}
}
