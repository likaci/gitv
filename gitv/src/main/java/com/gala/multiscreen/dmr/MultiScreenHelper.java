package com.gala.multiscreen.dmr;

import android.content.Context;
import com.gala.multiscreen.dmr.logic.MSHelper;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.multiscreen.dmr.logic.NetworkReceiver;
import com.gala.multiscreen.dmr.logic.listener.MSCallbacks;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.DlnaMessage;
import com.gala.multiscreen.dmr.util.ContextProfile;
import com.gala.multiscreen.dmr.util.MSKeyUtils;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSUtils;

public class MultiScreenHelper {
    public static final String DEVICE_TYPE_ONLY_QIMO = MSHelper.DEVICE_TYPE_ONLY_QIMO;
    public static final String DEVICE_TYPE_QIMO_AND_STANDARD = "urn:schemas-upnp-org:device:MediaRenderer:1";
    private static final MultiScreenHelper gMultiScreenManager = new MultiScreenHelper();
    private MSHelper mGalaDlna = MSHelper.get();
    private MSKeyUtils mGalaKeyDispatch = new MSKeyUtils();
    private boolean mIsStart = false;
    private NetworkReceiver mNetworkReceiver = new NetworkReceiver();

    private MultiScreenHelper() {
    }

    public static MultiScreenHelper getInstance() {
        return gMultiScreenManager;
    }

    public boolean startAsync(Context context) {
        if (context == null) {
            return false;
        }
        ContextProfile.setContext(context);
        this.mNetworkReceiver.register(context);
        this.mGalaDlna.startAsync();
        this.mIsStart = true;
        return true;
    }

    public boolean stop() {
        this.mNetworkReceiver.unregister();
        this.mGalaDlna.stop();
        this.mIsStart = false;
        return true;
    }

    public void setName(String name) {
        this.mGalaDlna.setName(name);
    }

    public void changeName(String name) {
        this.mGalaDlna.changeName(name);
    }

    public void setDeviceId(String deviceId) {
        this.mGalaDlna.setDeviceId(deviceId);
    }

    public void setPackageName(String packageName) {
        this.mGalaDlna.setPackageName(packageName);
    }

    public void setGalaDevice(int device) {
        this.mGalaDlna.setGalaDevice(device);
    }

    public void setDeviceType(String deviceType) {
        this.mGalaDlna.setDeviceType(deviceType);
    }

    public void setTvVersionString(String version) {
        this.mGalaDlna.setTvVersionString(version);
    }

    public void addIcon(MSIcon icon) {
        this.mGalaDlna.addIcon(icon);
    }

    public void registerGalaMSCallback(IGalaMSExpand callback) {
        MSCallbacks.registerGalaMS(callback);
    }

    public void unregisterGalaMSCallback() {
        MSCallbacks.unregisterGalaMS();
    }

    public void registerStandardMSCallback(IStandardMSCallback callback) {
        MSCallbacks.registerStandardMS(callback);
    }

    public void unregisterStandardMSCallback() {
        MSCallbacks.unregisterStandardMS();
    }

    public void sendMessage(DlnaMessage dlnaMessage) {
        if (dlnaMessage != null) {
            this.mGalaDlna.sendMessage(dlnaMessage);
        }
    }

    public void setMute(boolean isMute) {
        this.mGalaDlna.getStandardDlna().setMute(isMute);
    }

    public void setVolume(int volume) {
        this.mGalaDlna.getStandardDlna().setVolume(volume);
    }

    public void setSeek(long seek) {
        this.mGalaDlna.getStandardDlna().setSeek(seek);
    }

    public void sendKeyCode(KeyKind kind) {
        this.mGalaKeyDispatch.send(MSUtils.getKeyCode(kind), MSCallbacks.getGalaMS());
    }

    public boolean isKeySysEnable() {
        return this.mGalaKeyDispatch.isSysEnable();
    }

    public void setDlnaLogEnabled(boolean isEnabled) {
        MSLog.setDlnaLogEnabled(isEnabled);
    }

    public boolean isStartDlnaServer() {
        return this.mIsStart;
    }

    public void onSeekFinish() {
        SeekCounter.getSeekCounter().finishSeek();
    }
}
