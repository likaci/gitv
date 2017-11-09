package com.gala.video.lib.share.uikit.view.widget.coverflow;

import android.view.KeyEvent;

public class KeyHandler {
    public static final int CHECK_DOWN = 8;
    public static final int CHECK_LEFT = 1;
    public static final int CHECK_RIGHT = 2;
    public static final int CHECK_UP = 4;
    private static final int KEY_SCAN_INTERVAL = 90;
    private static final String TAG = "KeyHolder";
    private int mHandleMode = 0;
    private KeyInfo mKey = new KeyInfo();

    private static class KeyInfo {
        int code;
        int mCount;
        long mLastDownTime;

        private KeyInfo() {
            this.mCount = 0;
            this.code = 0;
        }

        boolean executeKeyAction(KeyEvent event) {
            int action = event.getAction();
            if (this.code != event.getKeyCode() || action == 1) {
                this.code = event.getKeyCode();
                this.mCount = 0;
                this.mLastDownTime = -1;
                return false;
            } else if (action != 0) {
                return false;
            } else {
                long nowTime = event.getEventTime();
                this.mCount++;
                if (nowTime - this.mLastDownTime < 90) {
                    return true;
                }
                this.mLastDownTime = nowTime;
                return false;
            }
        }

        final boolean isHold() {
            return this.mCount > 1;
        }
    }

    public KeyHandler(int mode) {
        this.mHandleMode = mode;
    }

    public final boolean isKeyLongPress() {
        return this.mKey.isHold();
    }

    public boolean executeKeyEvent(KeyEvent event) {
        boolean bRet = false;
        switch (event.getKeyCode()) {
            case 19:
                if ((this.mHandleMode & 4) == 4) {
                    bRet = true;
                    break;
                }
                break;
            case 20:
                if ((this.mHandleMode & 8) == 8) {
                    bRet = true;
                    break;
                }
                break;
            case 21:
                if ((this.mHandleMode & 1) == 1) {
                    bRet = true;
                    break;
                }
                break;
            case 22:
                if ((this.mHandleMode & 2) == 2) {
                    bRet = true;
                    break;
                }
                break;
        }
        if (bRet) {
            return this.mKey.executeKeyAction(event);
        }
        return bRet;
    }
}
