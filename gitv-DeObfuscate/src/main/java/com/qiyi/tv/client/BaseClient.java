package com.qiyi.tv.client;

import android.content.Context;
import com.qiyi.tv.client.impl.p035a.C2000c;

public class BaseClient {
    protected Context mContext;

    class ProxyListener implements ConnectionListener {
        private /* synthetic */ BaseClient f2054a;
        private ConnectionListener f2055a;

        public ProxyListener(BaseClient baseClient, ConnectionListener listener) {
            this.f2054a = baseClient;
            this.f2055a = listener;
        }

        public void onError(int code) {
            this.f2054a.onError(code);
            if (this.f2055a != null) {
                this.f2055a.onError(code);
            }
        }

        public void onDisconnected() {
            this.f2054a.onDisconnected();
            if (this.f2055a != null) {
                this.f2055a.onDisconnected();
            }
        }

        public void onConnected() {
            this.f2054a.onConnected();
            if (this.f2055a != null) {
                this.f2055a.onConnected();
            }
        }

        public void onAuthSuccess() {
            this.f2054a.onAuthSuccess();
            if (this.f2055a != null) {
                this.f2055a.onAuthSuccess();
            }
        }
    }

    protected BaseClient() {
    }

    public void initialize(Context context, String signature) {
        initialize(context, signature, null);
    }

    public void initialize(Context context, String signature, String servicePackageName) {
        this.mContext = context.getApplicationContext();
        C2000c.m1650a(this.mContext, signature, servicePackageName);
    }

    public void release() {
        onRelease();
        C2000c.m1643a();
    }

    public boolean isConnected() {
        return C2000c.m1643a().m1643a();
    }

    public boolean isAuthSuccess() {
        return C2000c.m1643a().m1666b();
    }

    public void setListener(ConnectionListener listener) {
        C2000c.m1643a().m1664a(new ProxyListener(this, listener));
    }

    public void connect() {
        C2000c.m1643a().m1666b();
    }

    public void disconnect() {
        C2000c.m1643a().m1661d();
    }

    public void authenticate() {
        C2000c.m1643a().m1660c();
    }

    protected void onConnected() {
    }

    protected void onAuthSuccess() {
    }

    protected void onDisconnected() {
    }

    protected void onError(int i) {
    }

    protected void onInitlized() {
    }

    protected void onRelease() {
    }
}
