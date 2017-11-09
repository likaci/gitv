package com.gala.multiscreen.dmr.util;

import com.alibaba.fastjson.asm.Opcodes;
import com.gala.multiscreen.dmr.IGalaMSCallback;
import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.sysinput.SysInputProxy;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import org.xbill.DNS.WKSRecord.Service;

public class MSKeyUtils {
    private static final String TAG = "MSKeyUtils->";
    public static short mHomeKeyCode = UIKitConfig.CARD_TYPE_COVER_FLOW;
    private int mTryCount = 0;

    public boolean send(short type, IGalaMSExpand callback) {
        MSLog.log("MSKeyUtils->send() type=" + type + ", callback=" + callback);
        boolean onKeyChanged = onKeyChanged(type, callback);
        if (onKeyChanged) {
            MSLog.log("MSKeyUtils->onKeyChanged() return=" + onKeyChanged);
            return onKeyChanged;
        }
        boolean result = true;
        switch (type) {
            case (short) 0:
            case Service.LOCUS_CON /*127*/:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.UP);
                        break;
                    }
                }
                sendKey(KeyKind.UP);
                break;
                break;
            case (short) 1:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.DOWN);
                        break;
                    }
                }
                sendKey(KeyKind.DOWN);
                break;
                break;
            case (short) 2:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.LEFT);
                        break;
                    }
                }
                sendKey(KeyKind.LEFT);
                break;
                break;
            case (short) 3:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.RIGHT);
                        break;
                    }
                }
                sendKey(KeyKind.RIGHT);
                break;
                break;
            case (short) 49:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.HOME);
                        break;
                    }
                }
                sendKey(KeyKind.HOME);
                break;
                break;
            case (short) 50:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.CLICK);
                        break;
                    }
                }
                sendKey(KeyKind.CLICK);
                break;
                break;
            case (short) 51:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.BACK);
                        break;
                    }
                }
                sendKey(KeyKind.BACK);
                break;
                break;
            case (short) 52:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.MENU);
                        break;
                    }
                }
                sendKey(KeyKind.MENU);
                break;
                break;
            case (short) 80:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.VOLUME_UP);
                        break;
                    }
                }
                sendKey(KeyKind.VOLUME_UP);
                break;
                break;
            case Service.HOSTS2_NS /*81*/:
                if (!SysInputProxy.isEnable()) {
                    if (callback != null) {
                        callback.onKeyEvent(KeyKind.VOLUME_DOWN);
                        break;
                    }
                }
                sendKey(KeyKind.VOLUME_DOWN);
                break;
                break;
            default:
                MSLog.log("MSKeyUtils->sendCustomRemote() type=" + type + ", callback=" + callback);
                result = sendCustomRemote(type, callback);
                break;
        }
        MSLog.log("MSKeyUtils->send() return=" + result);
        return result;
    }

    private boolean onKeyChanged(short type, IGalaMSExpand callback) {
        MSLog.log("MSKeyUtils->onKeyChanged() type=" + type + ", callback=" + callback);
        boolean onKeyChanged = false;
        if (callback != null) {
            switch (type) {
                case (short) 0:
                case Service.LOCUS_CON /*127*/:
                    onKeyChanged = callback.onKeyChanged(19);
                    break;
                case (short) 1:
                    onKeyChanged = callback.onKeyChanged(20);
                    break;
                case (short) 2:
                    onKeyChanged = callback.onKeyChanged(21);
                    break;
                case (short) 3:
                    onKeyChanged = callback.onKeyChanged(22);
                    break;
                case (short) 49:
                    onKeyChanged = callback.onKeyChanged(3);
                    break;
                case (short) 50:
                    onKeyChanged = callback.onKeyChanged(23);
                    break;
                case (short) 51:
                    onKeyChanged = callback.onKeyChanged(4);
                    break;
                case (short) 52:
                    onKeyChanged = callback.onKeyChanged(82);
                    break;
                case (short) 80:
                    onKeyChanged = callback.onKeyChanged(24);
                    break;
                case Service.HOSTS2_NS /*81*/:
                    onKeyChanged = callback.onKeyChanged(25);
                    break;
                default:
                    onKeyChanged = false;
                    break;
            }
        }
        MSLog.log("MSKeyUtils->onKeyChanged() return=" + onKeyChanged);
        return onKeyChanged;
    }

    private boolean sendCustomRemote(short type, IGalaMSCallback callback) {
        if (callback == null) {
            return false;
        }
        boolean result = true;
        switch (type) {
            case (short) 53:
                callback.onSeekEvent(KeyKind.LEFT);
                break;
            case (short) 54:
                callback.onSeekEvent(KeyKind.RIGHT);
                break;
            case (short) 55:
                callback.onSeekEvent(KeyKind.UP);
                break;
            case Opcodes.FSTORE /*56*/:
                callback.onSeekEvent(KeyKind.DOWN);
                break;
            case Opcodes.DSTORE /*57*/:
                callback.onFlingEvent(KeyKind.LEFT);
                break;
            case Opcodes.ASTORE /*58*/:
                callback.onFlingEvent(KeyKind.RIGHT);
                break;
            default:
                result = false;
                break;
        }
        MSLog.log("MSKeyUtils->sendCustomRemote() return=" + result);
        return result;
    }

    protected boolean checkEnable() {
        if (!SysInputProxy.isEnable() && this.mTryCount < 2) {
            this.mTryCount++;
            SysInputProxy.reset();
        }
        return SysInputProxy.isEnable();
    }

    public boolean isSysEnable() {
        return SysInputProxy.isEnable();
    }

    public void sendKey(KeyKind keyKind) {
        short keyEvent = (short) -1;
        switch (keyKind) {
            case CLICK:
                keyEvent = (short) 28;
                break;
            case BACK:
                keyEvent = (short) 158;
                break;
            case MENU:
                keyEvent = (short) 139;
                break;
            case HOME:
                keyEvent = mHomeKeyCode;
                break;
            case RIGHT:
                keyEvent = UIKitConfig.CARD_TYPE_ONE;
                break;
            case UP:
                keyEvent = UIKitConfig.CARD_TYPE_TIME_LINE;
                break;
            case LEFT:
                keyEvent = UIKitConfig.CARD_TYPE_GRID_CARD;
                break;
            case DOWN:
                keyEvent = UIKitConfig.CARD_TYPE_CAROUSEL;
                break;
            case VOLUME_DOWN:
                keyEvent = (short) 114;
                break;
            case VOLUME_UP:
                keyEvent = (short) 115;
                break;
        }
        MSLog.log("MSKeyUtils->sendKey() keyEvent=" + keyEvent);
        if (keyEvent != (short) -1) {
            SysInputProxy.setKeyEvent(keyEvent);
        }
    }

    public static void setHomeKeyCode(short keyCode) {
        mHomeKeyCode = keyCode;
    }
}
