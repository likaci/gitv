package defpackage;

import java.net.InetAddress;

class jnamed$3 implements Runnable {
    private final jnamed this$0;
    private final InetAddress val$addr;
    private final int val$port;

    jnamed$3(jnamed jnamed, InetAddress inetAddress, int i) {
        this.this$0 = jnamed;
        this.val$addr = inetAddress;
        this.val$port = i;
    }

    public void run() {
        this.this$0.serveUDP(this.val$addr, this.val$port);
    }
}
