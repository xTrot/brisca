package com.briscagame.serverBrowser;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerBrowser {

    public static void main(String[] args) throws IOException {

        ThreadPoolExecutor tpe = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        BrowserHttpServer.start(tpe);

    }

}
