package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.repositoryImpl.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementService;
import com.app.quantitymeasurement.serviceImpl.QuantityMeasurementServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeasurementApplication {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MeasurementApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Measurement Application Started");

        QuantityMeasurementRepository repository =
                new QuantityMeasurementDatabaseRepository();
        QuantityMeasurementService service =
                new QuantityMeasurementServiceImpl(repository);
        QuantityMeasurementController controller =
                new QuantityMeasurementController(service);

        LOGGER.info("Performing ADD operation");
        QuantityDTO result = controller.add(
                1.0,
                "FEET",
                "LengthUnit",
                12.0,
                "INCH"
        );

        if (result.hasError()) {
            LOGGER.error( "Operation failed: {}", result.getErrorMessage() );
        } else {
            LOGGER.info( "Addition Result = {} {}", result.getValue(), result.getUnit() );
        }

        LOGGER.info( "Database Count = {}", repository.getTotalCount() );

        LOGGER.info("Measurement Application Finished");
    }
}
