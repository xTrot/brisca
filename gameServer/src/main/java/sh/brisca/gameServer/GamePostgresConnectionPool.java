package sh.brisca.gameServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.common.PostgresConnectionPool;

public class GamePostgresConnectionPool extends PostgresConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(GamePostgresConnectionPool.class);

    public static boolean registerGameServer(String hostname) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM auth.register_game_server('");
        sb.append(hostname);
        sb.append("');");
        String query = sb.toString();

        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = PostgresConnectionPool.dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            logger.info("Connection used: {} for query: {}", connection, query);

            boolean success = false;
            if (resultSet.next()) {
                success = resultSet.getBoolean(1);
            }

            return success;

        } catch (SQLException e) {
            logger.error("Error querying database: {}", e);
        }

        return false;

    }

}
