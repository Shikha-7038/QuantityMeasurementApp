package com.app.quantitymeasurement.serviceImpl;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.Quantity;
import com.app.quantitymeasurement.enums.IMeasurable;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class QuantityMeasurementServiceImpl implements QuantityMeasurementService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        LOGGER.info("Initializing QuantityMeasurementService");
        this.repository = repository;
    }

    private void validateDTO(QuantityDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        if (!Double.isFinite(dto.getValue())) {
            throw new QuantityMeasurementException("Invalid numeric value");
        }
    }
    private void validateSameCategory(QuantityDTO q1, QuantityDTO q2) {
        if (!q1.getMeasurementType().equals(q2.getMeasurementType())) {
            throw new QuantityMeasurementException("Cross category operation not allowed");
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("ADD operation started");
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        try {
            validateDTO(q1);
            validateDTO(q2);
            validateSameCategory(q1, q2);

            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);
            Quantity<IMeasurable> result = quantity1.add(quantity2);

            LOGGER.info("ADD successful: {} {}", result.getValue(), result.getUnit());
            repository.save(new QuantityMeasurementEntity(
                    "ADD",
                    q1.getValue() + " " + q1.getUnit() + ", " +
                            q2.getValue() + " " + q2.getUnit(),
                    result.getValue() + " " + result.getUnit(),
                    false
            ));

            return new QuantityDTO(
                    result.getValue(),
                    result.getUnit().toString(),
                    q1.getMeasurementType()
            );

        } catch (Exception e) {
            LOGGER.error("ADD failed: {}", e.getMessage(), e);
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("SUBTRACT operation started");
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        try {
            validateDTO(q1);
            validateDTO(q2);
            validateSameCategory(q1, q2);

            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);
            Quantity<IMeasurable> result = quantity1.subtract(quantity2);

            LOGGER.info("SUBTRACT successful: {} {}", result.getValue(), result.getUnit());
            return new QuantityDTO(
                    result.getValue(),
                    result.getUnit().toString(),
                    q1.getMeasurementType()
            );

        } catch (Exception e) {
            LOGGER.error("SUBTRACT failed: {}", e.getMessage(), e);
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("DIVIDE operation started");
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        try {
            validateDTO(q1);
            validateDTO(q2);
            validateSameCategory(q1, q2);
            if (q2.getValue() == 0) {
                throw new ArithmeticException("Division by zero");
            }
            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);
            double result = quantity1.divide(quantity2);

            LOGGER.info("DIVIDE successful: {}", result);
            return new QuantityDTO(
                    result,
                    q1.getUnit(),
                    q1.getMeasurementType()
            );
        } catch (Exception e) {
            LOGGER.error("DIVIDE failed: {}", e.getMessage(), e);
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO input, String targetUnit) {
        LOGGER.info("CONVERT operation started");
        try {
            validateDTO(input);
            Quantity<IMeasurable> quantity = buildQuantity(input);
            Class<?> enumClass = Class.forName(
                    "com.app.quantitymeasurement.enumsImplement." + input.getMeasurementType()
            );
            IMeasurable target = (IMeasurable) Enum.valueOf((Class<Enum>) enumClass, targetUnit);
            Quantity<IMeasurable> result = quantity.convertTo(target);
            LOGGER.info("CONVERT successful: {} {}", result.getValue(), result.getUnit());
            return new QuantityDTO(
                    result.getValue(),
                    result.getUnit().toString(),
                    input.getMeasurementType()
            );
        } catch (Exception e) {
            LOGGER.error("CONVERT failed: {}", e.getMessage(), e);
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO compareEquality(QuantityDTO q1, QuantityDTO q2) {
        LOGGER.info("COMPARE operation started");
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("DTO cannot be null");
        }
        try {
            validateDTO(q1);
            validateDTO(q2);
            validateSameCategory(q1, q2);

            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);

            double base1 = quantity1.getUnit().convertToBaseUnit(quantity1.getValue());
            double base2 = quantity2.getUnit().convertToBaseUnit(quantity2.getValue());

            boolean isEqual =
                    Math.round(base1 * 1_000_000) ==
                            Math.round(base2 * 1_000_000);

            LOGGER.info("COMPARE result: {}", isEqual);
            return new QuantityDTO(
                    isEqual ? 1.0 : 0.0,
                    q1.getUnit(),
                    q1.getMeasurementType()
            );

        } catch (Exception e) {
            LOGGER.error("COMPARE failed: {}", e.getMessage(), e);
            return new QuantityDTO(true, e.getMessage());
        }
    }

    private Quantity<IMeasurable> buildQuantity(QuantityDTO dto) {
        try {
            Class<?> enumClass = Class.forName(
                    "com.app.quantitymeasurement.enumsImplement." + dto.getMeasurementType()
            );
            IMeasurable unit =
                    (IMeasurable) Enum.valueOf((Class<Enum>) enumClass, dto.getUnit());
            return new Quantity<>(dto.getValue(), unit);
        } catch (Exception e) {
            LOGGER.error("Invalid unit/type: {}", dto.getUnit(), e);
            throw new QuantityMeasurementException(
                    "Invalid unit/type: " + dto.getUnit()
            );
        }
    }
}