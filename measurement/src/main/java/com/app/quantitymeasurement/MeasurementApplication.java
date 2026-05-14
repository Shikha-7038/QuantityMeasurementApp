package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.repositoryImpl.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementService;
import com.app.quantitymeasurement.serviceImpl.QuantityMeasurementServiceImpl;

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
