package sh.brisca.serverBrowser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sh.brisca.serverBrowser.handlers.JoinPrivateGameHandler;

public class HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    public static boolean check(String gameServer,
            String retries, String retryInterval, String timeout) {

        logger.info("Healthcheck");
        String command = EnvironmentVariable.BROWSER_GAME_HEALTHCHECK + " " +
                gameServer + " " +
                retries + " " +
                retryInterval + " " +
                timeout;

        Script result = Script.run(command, null);

        return result.exitCode == 0;

    }
}
