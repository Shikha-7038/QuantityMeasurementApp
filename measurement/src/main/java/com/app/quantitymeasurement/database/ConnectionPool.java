package com.app.quantitymeasurement.database;
import com.app.quantitymeasurement.config.ApplicationConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Queue;
public class ConnectionPool {
    private final Queue<Connection> pool = new LinkedList<>();
    public ConnectionPool() {
        try {
            Class.forName(
                    ApplicationConfig.getProperty("DB_DRIVER")
            );
            int size = Integer.parseInt(
                    ApplicationConfig.getProperty("POOL_SIZE")
            );
            for (int i = 0; i < size; i++) {
                Connection connection = DriverManager.getConnection(
                        ApplicationConfig.getProperty("DB_URL"),
                        ApplicationConfig.getProperty("DB_USERNAME"),
                        ApplicationConfig.getProperty("DB_PASSWORD")
                );
                pool.add(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized Connection getConnection() {
        return pool.poll();
    }
    public synchronized void releaseConnection(Connection connection) {
        pool.add(connection);
    }
}