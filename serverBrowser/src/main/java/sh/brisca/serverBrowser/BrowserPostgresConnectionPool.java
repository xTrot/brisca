package sh.brisca.serverBrowser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.common.PostgresConnectionPool;
import sh.brisca.serverBrowser.handlers.JoinPrivateGameHandler;

public class BrowserPostgresConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    public static LinkedHashSet<String> getAllGameServers() {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM auth.get_game_servers();");
        String query = sb.toString();

        try ( // Auto-Closing try/catch closes resources inside parenthesis.
                Connection connection = PostgresConnectionPool.dataSource.getConnection();
                Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(query)) {
            logger.debug("Connection used: {} for query: {}", connection, query);

            LinkedHashSet<String> rtn = new LinkedHashSet<String>();
            while (resultSet.next()) {
                rtn.add(resultSet.getString(1));
            }
            return rtn;

        } catch (SQLException e) {
            logger.error("Error querying database: {}", e);
        }

        return null;

    }

    public static boolean removeGameServer(String server) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM auth.remove_game_server('");
        sb.append(server);
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
