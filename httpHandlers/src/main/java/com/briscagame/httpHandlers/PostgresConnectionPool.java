package com.briscagame.httpHandlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
            String username = null;
            while (resultSet.next()) {
                token = resultSet.getString("token");
                refreshBy = resultSet.getTimestamp("refreshby");
                username = resultSet.getString("username");
                if (token == null || refreshBy == null || username == null) {
                    continue;
                }
                new Session(token, refreshBy, username);
            }

        } catch (SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }
    }

    public static String createGuestSession(Session current, String username) {
        String query = "SELECT * FROM auth.create_guest_session('" + username + "');";
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

    public static Session getSession(String userId) {
        Session session = null;
        String query = "SELECT * FROM auth.get_session('" + userId + "');";
        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Connection used: " + connection + " for query: " + query);

            String token = null;
            Timestamp refreshBy = null;
            String username = null;
            if (resultSet.next()) {

                System.out.println("Found something.");

                token = resultSet.getString("token");
                refreshBy = resultSet.getTimestamp("refreshby");
                username = resultSet.getString("username");

                System.out.println("token, refreshby, username " +
                        token + ", " +
                        refreshBy.toString() + ", " +
                        username + ", " +
                        ";");

                if (token == null || refreshBy == null || username == null) {
                    return session;
                }

                session = new Session(token, refreshBy, username);

                return session;
            }

        } catch (SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }

        return session;
    }

    public static boolean refreshSession(Session session) {
        String token = session.getUserId();
        String old = session.getRefreshBy().toString();
        String query = "SELECT * FROM auth.refresh_session('" + session.getUserId() + "');";
        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Connection used: " + connection + " for query: " + query);

            Timestamp refreshBy = null;
            if (resultSet.next()) {
                refreshBy = resultSet.getTimestamp("refreshby");
                String newRefresh = refreshBy.toString();
                session.setRefreshBy(refreshBy);
                System.out.println("Session refreshed for userId: " + token + " old: " + old + " new:" + newRefresh);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }

        return false;
    }
}
