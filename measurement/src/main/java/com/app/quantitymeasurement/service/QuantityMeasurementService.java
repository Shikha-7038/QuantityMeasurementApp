
package com.app.quantitymeasurement.service;
import com.app.quantitymeasurement.dto.QuantityDTO;
public interface QuantityMeasurementService {

    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO divide(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO convert(QuantityDTO input, String targetUnit);
    QuantityDTO compareEquality(QuantityDTO q1, QuantityDTO q2);
}