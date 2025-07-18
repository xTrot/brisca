package sh.brisca.serverBrowser;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerBrowser {
    // private static final Logger logger =
    // LoggerFactory.getLogger(JoinPrivateGameHandler.class);

    public static LeasingOffice leasingOffice;

    public static void main(String[] args) throws IOException {

        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        BrowserHttpServer.start(tpe);
        new GameServerPool(tpe);
        leasingOffice = new LeasingOffice(tpe);

    }

}
