package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO performCompare(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO performConvert(QuantityDTO q, String targetUnit);
    QuantityMeasurementDTO performAdd(QuantityDTO q1, QuantityDTO q2, String targetUnit);
    QuantityMeasurementDTO performSubtract(QuantityDTO q1, QuantityDTO q2, String targetUnit);
    QuantityMeasurementDTO performMultiply(QuantityDTO q, double multiplier);
    QuantityMeasurementDTO performDivide(QuantityDTO q, double divisor);

    List<QuantityMeasurementDTO> getAllHistory();
    List<QuantityMeasurementDTO> getHistoryByOperation(String op);
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String type);
    List<QuantityMeasurementDTO> getErrorHistory();
    long getOperationCount(String op);

}