package sh.brisca.gameServer;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class GameServer {

    // private static final Logger logger =
    // LoggerFactory.getLogger(GameServer.class);

    public static void main(String[] args) throws IOException {

        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Game.setTpe(tpe);
        SimpleHttpServer.start(tpe);

    }

}