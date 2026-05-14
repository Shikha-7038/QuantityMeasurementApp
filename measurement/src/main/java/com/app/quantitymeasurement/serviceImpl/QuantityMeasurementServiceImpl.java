package com.app.quantitymeasurement.serviceImpl;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.Quantity;
import com.app.quantitymeasurement.enums.IMeasurable;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementService;

public class QuantityMeasurementServiceImpl implements QuantityMeasurementService {
    private final QuantityMeasurementRepository repository;
    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }
    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Input DTO cannot be null");
        }
        try {
            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);

            Quantity<IMeasurable> result = quantity1.add(quantity2);

            repository.save(new QuantityMeasurementEntity(
                    "ADD",
                    q1.getValue() + " " + q1.getUnit() + ", " +
                            q2.getValue() + " " + q2.getUnit(),
                    result.getValue() + " " + result.getUnit(),
                    false
            ));

            return new QuantityDTO(result.getValue(),
                    result.getUnit().toString(),
                    q1.getMeasurementType());

        } catch (Exception e) {
            repository.save(new QuantityMeasurementEntity("ADD", e.getMessage()));
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Input DTO cannot be null");
        }
        try {
            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);

            Quantity<IMeasurable> result = quantity1.subtract(quantity2);

            return new QuantityDTO(result.getValue(),
                    result.getUnit().toString(),
                    q1.getMeasurementType());

        } catch (Exception e) {
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Input DTO cannot be null");
        }
        try {
            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);

            double result = quantity1.divide(quantity2);

            return new QuantityDTO(result,
                    q1.getUnit(),
                    q1.getMeasurementType());

        } catch (Exception e) {
            return new QuantityDTO(true, e.getMessage());
        }
    }

    // 🔥 IMPORTANT: Convert DTO → Quantity
    private Quantity<IMeasurable> buildQuantity(QuantityDTO dto) {

        try {
            Class<?> enumClass = Class.forName(
                    "QuantityMeasurementApp.enumsImplement." + dto.getMeasurementType()
            );

            @SuppressWarnings("unchecked")
            IMeasurable unit = (IMeasurable) Enum.valueOf(
                    (Class<Enum>) enumClass,
                    dto.getUnit()
            );


            return new Quantity<>(dto.getValue(), unit);

        } catch (Exception e) {
            throw new QuantityMeasurementException("Invalid unit/type: " + dto.getUnit());
        }
    }
    @Override
    public QuantityDTO convert(QuantityDTO input, String targetUnit) {
        if (input == null || targetUnit == null) {
            throw new IllegalArgumentException("Input or target unit cannot be null");
        }
        try {
            Quantity<IMeasurable> quantity = buildQuantity(input);

            Class<?> enumClass = Class.forName(
                    "QuantityMeasurementApp.enumsImplement." + input.getMeasurementType()
            );

            @SuppressWarnings("unchecked")
            IMeasurable target = (IMeasurable) Enum.valueOf(
                    (Class<Enum>) enumClass,
                    targetUnit
            );

            Quantity<IMeasurable> result = quantity.convertTo(target);

            return new QuantityDTO(
                    result.getValue(),
                    result.getUnit().toString(),
                    input.getMeasurementType()
            );

        } catch (Exception e) {
            return new QuantityDTO(true, e.getMessage());
        }
    }

    @Override
    public QuantityDTO compareEquality(QuantityDTO q1, QuantityDTO q2) {
        if (q1 == null || q2 == null) {
            throw new IllegalArgumentException("Input DTO cannot be null");
        }
        try {
            Quantity<IMeasurable> quantity1 = buildQuantity(q1);
            Quantity<IMeasurable> quantity2 = buildQuantity(q2);

            double base1 = quantity1.getUnit().convertToBaseUnit(quantity1.getValue());
            double base2 = quantity2.getUnit().convertToBaseUnit(quantity2.getValue());

            boolean isEqual = Math.abs(base1 - base2) < 1e-5;

            return new QuantityDTO(
                    isEqual ? 1.0 : 0.0,
                    String.valueOf(isEqual),
                    q1.getMeasurementType()
            );

        } catch (Exception e) {
            return new QuantityDTO(true, e.getMessage());
        }
    }
}