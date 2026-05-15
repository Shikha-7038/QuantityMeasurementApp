package com.app.quantitymeasurement.repositoryImpl;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.database.ConnectionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDatabaseRepository
        implements QuantityMeasurementRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    QuantityMeasurementDatabaseRepository.class
            );

    private final ConnectionPool pool;

    public QuantityMeasurementDatabaseRepository() {
        LOGGER.info("Initializing Database Repository");
        pool = new ConnectionPool();
        createTable();
    }

    private void createTable() {
        LOGGER.info("Creating table if not exists");
        String sql = """
                CREATE TABLE IF NOT EXISTS quantity_measurement_entity (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    operation VARCHAR(50),
                    input VARCHAR(255),
                    result VARCHAR(255),
                    error BOOLEAN
                )
                """;
        Connection connection = null;
        try {
            connection = pool.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            LOGGER.info("Table created successfully");
        } catch (Exception e) {
            LOGGER.error( "Table creation failed: {}", e.getMessage(), e );
            throw new DatabaseException("Table Creation Failed", e );
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        LOGGER.info("Saving entity to database");
        String sql =
                "INSERT INTO quantity_measurement_entity " +
                        "(operation,input,result,error) VALUES(?,?,?,?)";
        Connection connection = null;
        try {
            connection = pool.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.setString(1, entity.getOperation());
            statement.setString(2, entity.getInput());
            statement.setString(3, entity.getResult());
            statement.setBoolean(4, entity.hasError());
            statement.executeUpdate();
            LOGGER.info("Entity saved successfully");
        } catch (Exception e) {
            LOGGER.error( "Database save failed: {}", e.getMessage(), e );
            throw new DatabaseException( "Database Save Failed", e );
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        LOGGER.info("Fetching all measurements");
        List<QuantityMeasurementEntity> list =
                new ArrayList<>();
        String sql = "SELECT * FROM quantity_measurement_entity";
        Connection connection = null;
        try {
            connection = pool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                QuantityMeasurementEntity entity =
                        new QuantityMeasurementEntity(
                                resultSet.getString("operation"),
                                resultSet.getString("input"),
                                resultSet.getString("result"),
                                resultSet.getBoolean("error")
                        );
                list.add(entity);
            }
            LOGGER.info( "Fetched {} measurements", list.size());
        } catch (Exception e) {
            LOGGER.error( "Fetch failed: {}", e.getMessage(), e );
            throw new DatabaseException( "Fetch Failed", e );
        } finally {
            pool.releaseConnection(connection);
        }
        return list;
    }

    @Override
    public void deleteAll() {
        LOGGER.info("Deleting all measurements");
        String sql = "DELETE FROM quantity_measurement_entity";
        Connection connection = null;
        try {
            connection = pool.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            LOGGER.info("All measurements deleted");
        } catch (Exception e) {
            LOGGER.error( "Delete failed: {}", e.getMessage(), e );
            throw new DatabaseException("Delete Failed", e );
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Override
    public int getTotalCount() {
        LOGGER.info("Fetching total count");
        String sql = "SELECT COUNT(*) FROM quantity_measurement_entity";
        Connection connection = null;
        try {
            connection = pool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                LOGGER.info( "Total count fetched: {}", count );
                return count;
            }
        } catch (Exception e) {
            LOGGER.error( "Count fetch failed: {}", e.getMessage(), e );
            throw new DatabaseException( "Count Failed", e );
        } finally {
            pool.releaseConnection(connection);
        }
        return 0;
    }
}