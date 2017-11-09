package defpackage;

import java.net.Socket;

class jnamed$1 implements Runnable {
    private final jnamed this$0;
    private final Socket val$s;

    jnamed$1(jnamed jnamed, Socket socket) {
        this.this$0 = jnamed;
        this.val$s = socket;
    }

    public void run() {
        this.this$0.TCPclient(this.val$s);
    }
}
