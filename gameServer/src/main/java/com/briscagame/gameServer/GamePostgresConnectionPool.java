package com.briscagame.gameServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.briscagame.httpHandlers.PostgresConnectionPool;

public class GamePostgresConnectionPool extends PostgresConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(GamePostgresConnectionPool.class);

    public static boolean sessionHasMylease(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM auth.session_has_my_lease('");
        sb.append(userId);
        sb.append("', '");
        sb.append(GameServer.getGame().getState().server);
        sb.append("');");
        String query = sb.toString();
        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = PostgresConnectionPool.dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            logger.info("Connection used: {} for query: {}", connection, query);

            boolean found_lease = false;
            if (resultSet.next()) {
                found_lease = resultSet.getBoolean(1);
            }

            return found_lease;

        } catch (SQLException e) {
            logger.error("Error querying database: {}", e);
        }
        return false;
    }
}
