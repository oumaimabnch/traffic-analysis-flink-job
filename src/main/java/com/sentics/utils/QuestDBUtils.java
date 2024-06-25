package com.sentics.utils;

import com.sentics.entities.NearMissResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestDBUtils {
    private static final String URL = "jdbc:questdb://localhost:8812/qdb";
    private static final String USER = "admin";
    private static final String PASSWORD = "quest";

    static {
        try {
            Class.forName("org.questdb.jdbc.JDBCFactory");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load QuestDB JDBC driver", e);
        }
    }

    private Connection connection;
    public QuestDBUtils() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public void insertNearMissResult(NearMissResult result) throws SQLException {
        String query = "INSERT INTO near_misses (timestamp, vehicle_id, speed, distance, class, is_near_miss) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, result.getTimestamp());
            statement.setLong(2, result.getVehicleId());
            statement.setDouble(3, result.getSpeed());
            statement.setDouble(4, result.getDistance());
            statement.setString(5, result.getClassType());
            statement.setBoolean(6, result.isNearMiss());
            statement.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

