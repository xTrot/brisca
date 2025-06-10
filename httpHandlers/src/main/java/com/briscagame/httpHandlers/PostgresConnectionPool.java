package com.briscagame.httpHandlers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class PostgresConnectionPool {

    public static HikariDataSource dataSource;

    public static void initDataSource() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://auth-db:5432/brisca-auth");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(1000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName("MyPostgresPool");

        config.setDriverClassName("org.postgresql.Driver");

        dataSource = new HikariDataSource(config);
        System.out.println("HikariCP DataSource initialized successfully.");
    }

    public static void shutdownDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("HikariCP DataSource shut down.");
        }
    }

    public static void getActiveSessions() {
        String query = "SELECT * FROM auth.get_active_sessions();";
        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Connection used: " + connection + " for query: " + query);

            String token = null;
            Timestamp refreshBy = null;
            while (resultSet.next()) {
                token = resultSet.getString("token");
                refreshBy = resultSet.getTimestamp("refreshby");
                new Session(token, refreshBy);
            }

        } catch (SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }
    }

    public static String createGuestSession(Session current) {
        String query = "SELECT * FROM auth.create_guest_session();";
        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Connection used: " + connection + " for query: " + query);

            String token = null;
            Timestamp refreshBy = null;
            if (resultSet.next()) {
                token = resultSet.getString("token");
                refreshBy = resultSet.getTimestamp("refreshby");
                current.setRefreshBy(refreshBy);
                return token;
            }

        } catch (SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }

        return null;
    }
}
