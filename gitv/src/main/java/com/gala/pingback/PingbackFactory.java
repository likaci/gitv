package com.gala.pingback;

public class PingbackFactory implements IPingbackFactory {
    private static IPingbackFactory sFactory;
    private IPingbackFactory mImpl;

    public static synchronized void initliaze(IPingbackFactory factory) {
        synchronized (PingbackFactory.class) {
            if (sFactory == null) {
                sFactory = new PingbackFactory(factory);
            }
        }
    }

    public static synchronized void release() {
        synchronized (PingbackFactory.class) {
            sFactory = null;
        }
    }

    public static IPingbackFactory instance() {
        return sFactory;
    }

    private PingbackFactory(IPingbackFactory factory) {
        this.mImpl = factory;
    }

    public IPingback createPingback(int type) {
        return this.mImpl.createPingback(type);
    }
}
