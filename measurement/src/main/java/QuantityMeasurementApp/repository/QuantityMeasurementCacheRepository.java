package QuantityMeasurementApp.repository;
import QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import java.util.ArrayList;
import java.util.List;
public class QuantityMeasurementCacheRepository implements QuantityMeasurementRepository {
    private static QuantityMeasurementCacheRepository instance;
    private final List<QuantityMeasurementEntity> storage = new ArrayList<>();
    private QuantityMeasurementCacheRepository() {}

    public static QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository();
        }
        return instance;
    }
    @Override
    public void save(QuantityMeasurementEntity entity) {
        storage.add(entity);
    }
    public List<QuantityMeasurementEntity> getAll() {
        return storage;
    }
}