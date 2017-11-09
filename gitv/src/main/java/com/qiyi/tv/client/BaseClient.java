package com.qiyi.tv.client;

import android.content.Context;
import com.qiyi.tv.client.impl.a.c;

public class BaseClient {
    protected Context mContext;

    class ProxyListener implements ConnectionListener {
        private /* synthetic */ BaseClient a;
        private ConnectionListener f826a;

        public ProxyListener(BaseClient baseClient, ConnectionListener listener) {
            this.a = baseClient;
            this.f826a = listener;
        }

        public void onError(int code) {
            this.a.onError(code);
            if (this.f826a != null) {
                this.f826a.onError(code);
            }
        }

        public void onDisconnected() {
            this.a.onDisconnected();
            if (this.f826a != null) {
                this.f826a.onDisconnected();
            }
        }

        public void onConnected() {
            this.a.onConnected();
            if (this.f826a != null) {
                this.f826a.onConnected();
            }
        }

        public void onAuthSuccess() {
            this.a.onAuthSuccess();
            if (this.f826a != null) {
                this.f826a.onAuthSuccess();
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
        c.a(this.mContext, signature, servicePackageName);
    }

    public void release() {
        onRelease();
        c.a();
    }

    public boolean isConnected() {
        return c.a().a();
    }

    public boolean isAuthSuccess() {
        return c.a().b();
    }

    public void setListener(ConnectionListener listener) {
        c.a().a(new ProxyListener(this, listener));
    }

    public void connect() {
        c.a().b();
    }

    public void disconnect() {
        c.a().d();
    }

    public void authenticate() {
        c.a().c();
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
