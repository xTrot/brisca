package sh.brisca.gameServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownCleanUp extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpServer.class);
    
    @Override
    public void run() {

        logger.info("Shutdown CleanUp started.");
        SimpleHttpServer.removeStatusContext();
        SimpleHttpServer.removeMakeGameContext();

        int games = Game.getGames().size();
        logger.info("Waiting for games to finish: {}", games);
        while (games != 0) {
            
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                logger.error("Unknown Error:", e);
            }

            games = Game.getGames().size();
            logger.info("Games still running: {}");

        }

        logger.info("Shutting down HTTP server.");
        SimpleHttpServer.stop();

        logger.info("Goodbye!");

    }

}
