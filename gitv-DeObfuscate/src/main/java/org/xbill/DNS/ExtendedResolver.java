package org.xbill.DNS;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ExtendedResolver implements Resolver {
    private static final int quantum = 5;
    private int lbStart = 0;
    private boolean loadBalance = false;
    private List resolvers;
    private int retries = 3;

    private static class Resolution implements ResolverListener {
        boolean done;
        Object[] inprogress;
        ResolverListener listener;
        int outstanding;
        Message query;
        Resolver[] resolvers;
        Message response;
        int retries;
        int[] sent;
        Throwable thrown;

        public Resolution(ExtendedResolver eres, Message query) {
            List l = ExtendedResolver.access$000(eres);
            this.resolvers = (Resolver[]) l.toArray(new Resolver[l.size()]);
            if (ExtendedResolver.access$100(eres)) {
                int nresolvers = this.resolvers.length;
                int start = ExtendedResolver.access$208(eres) % nresolvers;
                if (ExtendedResolver.access$200(eres) > nresolvers) {
                    ExtendedResolver.access$244(eres, nresolvers);
                }
                if (start > 0) {
                    Resolver[] shuffle = new Resolver[nresolvers];
                    for (int i = 0; i < nresolvers; i++) {
                        shuffle[i] = this.resolvers[(i + start) % nresolvers];
                    }
                    this.resolvers = shuffle;
                }
            }
            this.sent = new int[this.resolvers.length];
            this.inprogress = new Object[this.resolvers.length];
            this.retries = ExtendedResolver.access$300(eres);
            this.query = query;
        }

        public void send(int n) {
            int[] iArr = this.sent;
            iArr[n] = iArr[n] + 1;
            this.outstanding++;
            try {
                this.inprogress[n] = this.resolvers[n].sendAsync(this.query, this);
            } catch (Throwable t) {
                synchronized (this) {
                    this.thrown = t;
                    this.done = true;
                    if (this.listener == null) {
                        notifyAll();
                    }
                }
            }
        }

        public Message start() throws IOException {
            try {
                int[] iArr = this.sent;
                iArr[0] = iArr[0] + 1;
                this.outstanding++;
                this.inprogress[0] = new Object();
                return this.resolvers[0].send(this.query);
            } catch (Exception e) {
                handleException(this.inprogress[0], e);
                synchronized (this) {
                    while (!this.done) {
                        try {
                            wait();
                        } catch (InterruptedException e2) {
                        }
                    }
                    if (this.response != null) {
                        return this.response;
                    }
                    if (this.thrown instanceof IOException) {
                        throw ((IOException) this.thrown);
                    } else if (this.thrown instanceof RuntimeException) {
                        throw ((RuntimeException) this.thrown);
                    } else if (this.thrown instanceof Error) {
                        throw ((Error) this.thrown);
                    } else {
                        throw new IllegalStateException("ExtendedResolver failure");
                    }
                }
            }
        }

        public void startAsync(ResolverListener listener) {
            this.listener = listener;
            send(0);
        }

        public void receiveMessage(Object id, Message m) {
            if (Options.check("verbose")) {
                System.err.println("ExtendedResolver: received message");
            }
            synchronized (this) {
                if (this.done) {
                    return;
                }
                this.response = m;
                this.done = true;
                if (this.listener == null) {
                    notifyAll();
                    return;
                }
                this.listener.receiveMessage(this, this.response);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleException(java.lang.Object r7, java.lang.Exception r8) {
            /*
            r6 = this;
            r5 = 1;
            r2 = "verbose";
            r2 = org.xbill.DNS.Options.check(r2);
            if (r2 == 0) goto L_0x0023;
        L_0x000a:
            r2 = java.lang.System.err;
            r3 = new java.lang.StringBuffer;
            r3.<init>();
            r4 = "ExtendedResolver: got ";
            r3 = r3.append(r4);
            r3 = r3.append(r8);
            r3 = r3.toString();
            r2.println(r3);
        L_0x0023:
            monitor-enter(r6);
            r2 = r6.outstanding;	 Catch:{ all -> 0x0043 }
            r2 = r2 + -1;
            r6.outstanding = r2;	 Catch:{ all -> 0x0043 }
            r2 = r6.done;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0030;
        L_0x002e:
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
        L_0x002f:
            return;
        L_0x0030:
            r0 = 0;
        L_0x0031:
            r2 = r6.inprogress;	 Catch:{ all -> 0x0043 }
            r2 = r2.length;	 Catch:{ all -> 0x0043 }
            if (r0 >= r2) goto L_0x003c;
        L_0x0036:
            r2 = r6.inprogress;	 Catch:{ all -> 0x0043 }
            r2 = r2[r0];	 Catch:{ all -> 0x0043 }
            if (r2 != r7) goto L_0x0046;
        L_0x003c:
            r2 = r6.inprogress;	 Catch:{ all -> 0x0043 }
            r2 = r2.length;	 Catch:{ all -> 0x0043 }
            if (r0 != r2) goto L_0x0049;
        L_0x0041:
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            goto L_0x002f;
        L_0x0043:
            r2 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            throw r2;
        L_0x0046:
            r0 = r0 + 1;
            goto L_0x0031;
        L_0x0049:
            r1 = 0;
            r2 = r6.sent;	 Catch:{ all -> 0x0043 }
            r2 = r2[r0];	 Catch:{ all -> 0x0043 }
            if (r2 != r5) goto L_0x0058;
        L_0x0050:
            r2 = r6.resolvers;	 Catch:{ all -> 0x0043 }
            r2 = r2.length;	 Catch:{ all -> 0x0043 }
            r2 = r2 + -1;
            if (r0 >= r2) goto L_0x0058;
        L_0x0057:
            r1 = 1;
        L_0x0058:
            r2 = r8 instanceof java.io.InterruptedIOException;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0073;
        L_0x005c:
            r2 = r6.sent;	 Catch:{ all -> 0x0043 }
            r2 = r2[r0];	 Catch:{ all -> 0x0043 }
            r3 = r6.retries;	 Catch:{ all -> 0x0043 }
            if (r2 >= r3) goto L_0x0067;
        L_0x0064:
            r6.send(r0);	 Catch:{ all -> 0x0043 }
        L_0x0067:
            r2 = r6.thrown;	 Catch:{ all -> 0x0043 }
            if (r2 != 0) goto L_0x006d;
        L_0x006b:
            r6.thrown = r8;	 Catch:{ all -> 0x0043 }
        L_0x006d:
            r2 = r6.done;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0087;
        L_0x0071:
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            goto L_0x002f;
        L_0x0073:
            r2 = r8 instanceof java.net.SocketException;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0084;
        L_0x0077:
            r2 = r6.thrown;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0081;
        L_0x007b:
            r2 = r6.thrown;	 Catch:{ all -> 0x0043 }
            r2 = r2 instanceof java.io.InterruptedIOException;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x006d;
        L_0x0081:
            r6.thrown = r8;	 Catch:{ all -> 0x0043 }
            goto L_0x006d;
        L_0x0084:
            r6.thrown = r8;	 Catch:{ all -> 0x0043 }
            goto L_0x006d;
        L_0x0087:
            if (r1 == 0) goto L_0x008e;
        L_0x0089:
            r2 = r0 + 1;
            r6.send(r2);	 Catch:{ all -> 0x0043 }
        L_0x008e:
            r2 = r6.done;	 Catch:{ all -> 0x0043 }
            if (r2 == 0) goto L_0x0094;
        L_0x0092:
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            goto L_0x002f;
        L_0x0094:
            r2 = r6.outstanding;	 Catch:{ all -> 0x0043 }
            if (r2 != 0) goto L_0x00a4;
        L_0x0098:
            r2 = 1;
            r6.done = r2;	 Catch:{ all -> 0x0043 }
            r2 = r6.listener;	 Catch:{ all -> 0x0043 }
            if (r2 != 0) goto L_0x00a4;
        L_0x009f:
            r6.notifyAll();	 Catch:{ all -> 0x0043 }
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            goto L_0x002f;
        L_0x00a4:
            r2 = r6.done;	 Catch:{ all -> 0x0043 }
            if (r2 != 0) goto L_0x00aa;
        L_0x00a8:
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            goto L_0x002f;
        L_0x00aa:
            monitor-exit(r6);	 Catch:{ all -> 0x0043 }
            r2 = r6.thrown;
            r2 = r2 instanceof java.lang.Exception;
            if (r2 != 0) goto L_0x00be;
        L_0x00b1:
            r2 = new java.lang.RuntimeException;
            r3 = r6.thrown;
            r3 = r3.getMessage();
            r2.<init>(r3);
            r6.thrown = r2;
        L_0x00be:
            r3 = r6.listener;
            r2 = r6.thrown;
            r2 = (java.lang.Exception) r2;
            r3.handleException(r6, r2);
            goto L_0x002f;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.ExtendedResolver.Resolution.handleException(java.lang.Object, java.lang.Exception):void");
        }
    }

    static List access$000(ExtendedResolver x0) {
        return x0.resolvers;
    }

    static boolean access$100(ExtendedResolver x0) {
        return x0.loadBalance;
    }

    static int access$200(ExtendedResolver x0) {
        return x0.lbStart;
    }

    static int access$208(ExtendedResolver x0) {
        int i = x0.lbStart;
        x0.lbStart = i + 1;
        return i;
    }

    static int access$244(ExtendedResolver x0, int x1) {
        int i = x0.lbStart % x1;
        x0.lbStart = i;
        return i;
    }

    static int access$300(ExtendedResolver x0) {
        return x0.retries;
    }

    private void init() {
        this.resolvers = new ArrayList();
    }

    public ExtendedResolver() throws UnknownHostException {
        init();
        String[] servers = ResolverConfig.getCurrentConfig().servers();
        if (servers != null) {
            for (String simpleResolver : servers) {
                Resolver r = new SimpleResolver(simpleResolver);
                r.setTimeout(5);
                this.resolvers.add(r);
            }
            return;
        }
        this.resolvers.add(new SimpleResolver());
    }

    public ExtendedResolver(String[] servers) throws UnknownHostException {
        init();
        for (String simpleResolver : servers) {
            Resolver r = new SimpleResolver(simpleResolver);
            r.setTimeout(5);
            this.resolvers.add(r);
        }
    }

    public ExtendedResolver(Resolver[] res) throws UnknownHostException {
        init();
        for (Object add : res) {
            this.resolvers.add(add);
        }
    }

    public void setPort(int port) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setPort(port);
        }
    }

    public void setTCP(boolean flag) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setTCP(flag);
        }
    }

    public void setIgnoreTruncation(boolean flag) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setIgnoreTruncation(flag);
        }
    }

    public void setEDNS(int level) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setEDNS(level);
        }
    }

    public void setEDNS(int level, int payloadSize, int flags, List options) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setEDNS(level, payloadSize, flags, options);
        }
    }

    public void setTSIGKey(TSIG key) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setTSIGKey(key);
        }
    }

    public void setTimeout(int secs, int msecs) {
        for (int i = 0; i < this.resolvers.size(); i++) {
            ((Resolver) this.resolvers.get(i)).setTimeout(secs, msecs);
        }
    }

    public void setTimeout(int secs) {
        setTimeout(secs, 0);
    }

    public Message send(Message query) throws IOException {
        return new Resolution(this, query).start();
    }

    public Object sendAsync(Message query, ResolverListener listener) {
        Resolution res = new Resolution(this, query);
        res.startAsync(listener);
        return res;
    }

    public Resolver getResolver(int n) {
        if (n < this.resolvers.size()) {
            return (Resolver) this.resolvers.get(n);
        }
        return null;
    }

    public Resolver[] getResolvers() {
        return (Resolver[]) this.resolvers.toArray(new Resolver[this.resolvers.size()]);
    }

    public void addResolver(Resolver r) {
        this.resolvers.add(r);
    }

    public void deleteResolver(Resolver r) {
        this.resolvers.remove(r);
    }

    public void setLoadBalance(boolean flag) {
        this.loadBalance = flag;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
