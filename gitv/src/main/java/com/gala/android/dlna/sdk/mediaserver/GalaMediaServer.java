package com.gala.android.dlna.sdk.mediaserver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import org.cybergarage.upnp.std.av.server.MediaServer;

public abstract class GalaMediaServer extends Service {
    private static final String TAG = "GalaMediaServer";
    private String mDefaultRootDir = "/http";
    private LocalBinder mLocalBinder = new LocalBinder();
    private MediaServer mMediaServer = null;
    private WifiStateReceiver mWifiStateListener = new WifiStateReceiver();

    class LocalBinder extends Binder implements IMediaServerBinder {
        LocalBinder() {
        }

        public GalaMediaServer getDigitalMediaServer() {
            return GalaMediaServer.this;
        }
    }

    class WifiStateReceiver extends BroadcastReceiver {
        WifiStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction())) {
                NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (info != null) {
                    State netState = info.getState();
                    if (netState == State.DISCONNECTING || netState == State.DISCONNECTED) {
                        GalaMediaServer.this.mediaServerStop();
                    } else if (netState == State.CONNECTED) {
                        GalaMediaServer.this.mediaServerRestart();
                    }
                }
            }
        }
    }

    protected abstract String getRootDir();

    protected abstract int getWorkingState();

    protected abstract void setupMediaServer(MediaServer mediaServer);

    public IBinder onBind(Intent arg0) {
        return this.mLocalBinder;
    }

    public void onCreate() {
        super.onCreate();
        try {
            mediaServerInit();
            mediaServerStart();
            registerWifiStateReceiver();
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
    }

    private void mediaServerInit() throws IOException {
        String rootDir = getRootDir();
        if (TextUtils.isEmpty(rootDir)) {
            rootDir = this.mDefaultRootDir;
        }
        File file = new File(Environment.getExternalStorageDirectory() + rootDir);
        if (!file.exists()) {
            file.mkdir();
        } else if (file.isFile()) {
            throw new IOException("file exists, and it's not a directory! ");
        }
        this.mMediaServer = new MediaServer();
        this.mMediaServer.setWorkingState(getWorkingState());
        setupMediaServer(this.mMediaServer);
        this.mMediaServer.setServerRootDir(rootDir);
    }

    private void registerWifiStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.NETWORK_IDS_CHANGED");
        registerReceiver(this.mWifiStateListener, filter);
    }

    private void unregisterWifiStateReveiver() {
        unregisterReceiver(this.mWifiStateListener);
    }

    private void mediaServerStart() {
        new Thread(new Runnable() {
            public void run() {
                GalaMediaServer.this.mMediaServer.start();
            }
        }).start();
    }

    private void mediaServerRestart() {
        new Thread(new Runnable() {
            public void run() {
                GalaMediaServer.this.mMediaServer.restart();
            }
        }).start();
    }

    private void mediaServerStop() {
        new Thread(new Runnable() {
            public void run() {
                GalaMediaServer.this.mMediaServer.stop();
            }
        }).start();
    }

    public MediaServer getMeidaServer() {
        return this.mMediaServer;
    }

    public void onDestroy() {
        super.onDestroy();
        mediaServerStop();
        unregisterWifiStateReveiver();
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return 1;
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
