package QuantityMeasurementApp;

import QuantityMeasurementApp.controller.QuantityMeasurementController;
import QuantityMeasurementApp.dto.QuantityDTO;
import QuantityMeasurementApp.repositoryImpl.QuantityMeasurementDatabaseRepository;
import QuantityMeasurementApp.repository.QuantityMeasurementRepository;
import QuantityMeasurementApp.service.QuantityMeasurementService;
import QuantityMeasurementApp.serviceImpl.QuantityMeasurementServiceImpl;

public class MeasurementApplication {
    public static void main(String[] args) {
        QuantityMeasurementRepository repository =
                new QuantityMeasurementDatabaseRepository();

        QuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);

        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        QuantityDTO result = controller.add(
                1.0,
                "FEET",
                "LengthUnit",
                12.0,
                "INCH"
        );
        if (result.hasError()) {
            System.out.println(result.getErrorMessage());
        } else {
            System.out.println(
                    result.getValue() + " " + result.getUnit()
            );
        }

        System.out.println(
                "Database Count = " +
                        repository.getTotalCount()
        );
    }
}
//package QuantityMeasurementApp;
//
//import QuantityMeasurementApp.controller.QuantityMeasurementController;
//import QuantityMeasurementApp.dto.QuantityDTO;
//import QuantityMeasurementApp.repository.QuantityMeasurementRepository;
//import QuantityMeasurementApp.service.QuantityMeasurementService;
//import QuantityMeasurementApp.serviceImpl.QuantityMeasurementServiceImpl;
//
//public class MeasurementApplication {
//
//    public static void main(String[] args) {
//        QuantityMeasurementRepository repo =
//                entity -> System.out.println(entity);
//        QuantityMeasurementService service =
//                new QuantityMeasurementServiceImpl(repo);
//        QuantityMeasurementController controller =
//                new QuantityMeasurementController(service);
//        QuantityDTO result = controller.add(
//                1.0, "FEET", "LengthUnit",
//                12.0, "INCH"
//        );
//        if (result.hasError())
//            System.out.println("Error: " + result.getErrorMessage());
//        else
//            System.out.println(result.getValue() + " " + result.getUnit());
//    }
//}