package com.briscagame.serverBrowser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.briscagame.httpHandlers.PostgresConnectionPool;

public class BrowserPostgresConnectionPool {

    public static boolean newGameLease(String userId, String server) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM auth.new_game_lease('");
        sb.append(userId);
        sb.append("', '");
        sb.append(server);
        sb.append("');");
        String query = sb.toString();
        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = PostgresConnectionPool.dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Connection used: " + connection + " for query: " + query);

            boolean found_lease = false;
            if (resultSet.next()) {
                found_lease = resultSet.getBoolean(1);
            }

            return found_lease;

        } catch (SQLException e) {
            System.err.println("Error querying database: " + e.getMessage());
        }
        return false;
    }

}
