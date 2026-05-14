package QuantityMeasurementApp.repository;
import QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import java.util.List;
public interface QuantityMeasurementRepository {
    void save(QuantityMeasurementEntity entity);
    List<QuantityMeasurementEntity> getAllMeasurements();
    void deleteAll();
    int getTotalCount();
}