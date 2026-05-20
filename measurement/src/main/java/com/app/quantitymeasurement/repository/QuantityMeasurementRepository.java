package com.app.quantitymeasurement.repository;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import java.util.List;
public interface QuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();
    void deleteAll();
    int getTotalCount();
}
