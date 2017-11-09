package defpackage;

import java.net.InetAddress;

class jnamed$2 implements Runnable {
    private final jnamed this$0;
    private final InetAddress val$addr;
    private final int val$port;

    jnamed$2(jnamed jnamed, InetAddress inetAddress, int i) {
        this.this$0 = jnamed;
        this.val$addr = inetAddress;
        this.val$port = i;
    }

    public void run() {
        this.this$0.serveTCP(this.val$addr, this.val$port);
    }
}
