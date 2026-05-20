package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementSpringDataRepository;
import com.app.quantitymeasurement.serviceImpl.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.repositoryImpl.QuantityMeasurementCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuantityMeasurementSpringService implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementSpringDataRepository repository;

    private final QuantityMeasurementService uc16Service = new QuantityMeasurementServiceImpl(
            QuantityMeasurementCacheRepository.getInstance()
    );

    private QuantityMeasurementDTO saveToRepository(String operation, String measurementType,
                                                    QuantityDTO thisDto, QuantityDTO thatDto,
                                                    Double resultValue, String resultUnit,
                                                    boolean errorFlag, String errorMessage) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setOperation(operation);
        entity.setMeasurementType(measurementType);
        entity.setInputValue(thisDto != null ? thisDto.getValue() : null);
        entity.setInputUnit(thisDto != null ? thisDto.getUnit() : null);
        entity.setSecondValue(thatDto != null ? thatDto.getValue() : null);
        entity.setSecondUnit(thatDto != null ? thatDto.getUnit() : null);
        entity.setResultValue(resultValue);
        entity.setResultUnit(resultUnit);
        entity.setError(errorFlag);
        entity.setErrorMessage(errorMessage);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setModifiedAt(LocalDateTime.now());

        return QuantityMeasurementDTO.fromEntity(repository.save(entity));
    }

    @Override
    public QuantityMeasurementDTO performAdd(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        try {
            QuantityDTO result = uc16Service.add(q1, q2);
            return saveToRepository("ADD", q1.getMeasurementType(), q1, q2,
                    result.getValue(), targetUnit, false, null);
        } catch (Exception e) {
            return saveToRepository("ADD", q1.getMeasurementType(), q1, q2,
                    null, targetUnit, true, e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementDTO performSubtract(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        try {
            QuantityDTO result = uc16Service.subtract(q1, q2);
            return saveToRepository("SUBTRACT", q1.getMeasurementType(), q1, q2,
                    result.getValue(), targetUnit, false, null);
        } catch (Exception e) {
            return saveToRepository("SUBTRACT", q1.getMeasurementType(), q1, q2,
                    null, targetUnit, true, e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementDTO performCompare(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = uc16Service.compareEquality(q1, q2);
            return saveToRepository("COMPARE", q1.getMeasurementType(), q1, q2,
                    result.getValue(), "BOOLEAN", false, null);
        } catch (Exception e) {
            return saveToRepository("COMPARE", q1.getMeasurementType(), q1, q2,
                    null, null, true, e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementDTO performConvert(QuantityDTO q, String targetUnit) {
        try {
            QuantityDTO result = uc16Service.convert(q, targetUnit);
            return saveToRepository("CONVERT", q.getMeasurementType(), q, null,
                    result.getValue(), targetUnit, false, null);
        } catch (Exception e) {
            return saveToRepository("CONVERT", q.getMeasurementType(), q, null,
                    null, targetUnit, true, e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementDTO performMultiply(QuantityDTO q, double multiplier) {
        try {
            double result = q.getValue() * multiplier;
            return saveToRepository("MULTIPLY", q.getMeasurementType(), q, null,
                    result, q.getUnit(), false, null);
        } catch (Exception e) {
            return saveToRepository("MULTIPLY", q.getMeasurementType(), q, null,
                    null, null, true, e.getMessage());
        }
    }

    @Override
    public QuantityMeasurementDTO performDivide(QuantityDTO q, double divisor) {
        try {
            if (divisor == 0) throw new ArithmeticException("Division by zero");
            QuantityDTO divisorDto = new QuantityDTO(divisor, q.getUnit(), q.getMeasurementType());
            QuantityDTO result = uc16Service.divide(q, divisorDto);
            return saveToRepository("DIVIDE", q.getMeasurementType(), q, null,
                    result.getValue(), q.getUnit(), false, null);
        } catch (Exception e) {
            return saveToRepository("DIVIDE", q.getMeasurementType(), q, null,
                    null, null, true, e.getMessage());
        }
    }

    @Override
    public List<QuantityMeasurementDTO> getAllHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findAll());
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String op) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(op));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String type) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByMeasurementType(type));
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByIsErrorTrue());
    }

    @Override
    public long getOperationCount(String op) {
        return repository.countByOperationAndIsErrorFalse(op);
    }
}