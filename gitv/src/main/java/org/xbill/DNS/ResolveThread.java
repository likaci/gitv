package org.xbill.DNS;

class ResolveThread extends Thread {
    private Object id;
    private ResolverListener listener;
    private Message query;
    private Resolver res;

    public ResolveThread(Resolver res, Message query, Object id, ResolverListener listener) {
        this.res = res;
        this.query = query;
        this.id = id;
        this.listener = listener;
    }

    public void run() {
        try {
            this.listener.receiveMessage(this.id, this.res.send(this.query));
        } catch (Exception e) {
            this.listener.handleException(this.id, e);
        }
    }
}
