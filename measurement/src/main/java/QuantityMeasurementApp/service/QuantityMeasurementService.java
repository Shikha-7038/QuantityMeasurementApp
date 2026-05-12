package QuantityMeasurementApp.service;
import QuantityMeasurementApp.dto.QuantityDTO;
public interface QuantityMeasurementService {

    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO divide(QuantityDTO q1, QuantityDTO q2);
    QuantityDTO convert(QuantityDTO input, String targetUnit);
    QuantityDTO compareEquality(QuantityDTO q1, QuantityDTO q2);
}