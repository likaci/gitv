package com.gala.android.dlna.sdk.controlpoint.qimohttpserver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRunner {
    private static final Logger LOG = Logger.getLogger(ServerRunner.class.getName());

    public static void executeInstance(NanoHTTPD server) {
        try {
            server.start();
        } catch (IOException ioe) {
            System.err.println("++++httpserver Couldn't start server:\n" + ioe);
            System.exit(-1);
        }
        System.out.println("++++httpserver Server started, Hit Enter to stop.\n");
        try {
            System.in.read();
        } catch (Throwable th) {
        }
        server.stop();
        System.out.println("++++httpserver Server stopped.\n");
    }

    public static <T extends NanoHTTPD> void run(Class<T> serverClass) {
        try {
            executeInstance((NanoHTTPD) serverClass.newInstance());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "++++httpserver Cound nor create server", e);
        }
    }
}
