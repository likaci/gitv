package com.gala.video.app.epg.home.controller;

import com.gala.video.app.epg.home.controller.UIEvent.UICallback;

public class Tactic {
    public static final int HIGH_PRIORITY_10 = 10;
    public static final int HIGH_PRIORITY_7 = 7;
    public static final int HIGH_PRIORITY_8 = 8;
    public static final int HIGH_PRIORITY_9 = 9;
    public static final int LOW_PRIORITY_1 = 1;
    private static Tactic mSingleton = new Tactic();
    public boolean mAnimating;
    private int mCurrentEvent;
    public int mPriority;
    private UIController mUIController;
    private UIEvent mUIEvent;
    private Object mcurrentarg;

    static class C06171 implements UICallback {
        C06171() {
        }

        public boolean onMessage(int event, Object arg) {
            Tactic.onUIEvent(event, arg);
            return false;
        }
    }

    private Tactic() {
    }

    public static void setUIController(UIController uiController) {
        mSingleton.mUIController = uiController;
    }

    public static void setUIEvnet(UIEvent uievent) {
        mSingleton.mUIEvent = uievent;
        if (uievent != null) {
            mSingleton.mUIEvent.register(new C06171());
        }
    }

    public static void onUIEvent(int eventtype, Object arg) {
        mSingleton.mCurrentEvent = eventtype;
        mSingleton.mcurrentarg = arg;
        calcPriority();
    }

    public static void calcPriority() {
        int oldmpriority = mSingleton.mPriority;
        switch (mSingleton.mCurrentEvent) {
            case 257:
            case 513:
            case 769:
                if (mSingleton.mUIController != null) {
                    mSingleton.mAnimating = true;
                } else {
                    mSingleton.mAnimating = true;
                }
                return;
            case 258:
            case 514:
            case 770:
                if (mSingleton.mUIController != null) {
                    mSingleton.mAnimating = false;
                } else {
                    mSingleton.mAnimating = false;
                }
                return;
            case 260:
                mSingleton.mPriority = 1;
                return;
            case UIEventType.BUILD_UI_PAUSE /*268435457*/:
                if (mSingleton.mUIController == null) {
                    return;
                }
                return;
            case UIEventType.BUILD_UI_RESUME /*268435458*/:
                if (mSingleton.mUIController == null) {
                    return;
                }
                return;
            default:
                return;
        }
    }

    public static int getPriority() {
        return mSingleton.mPriority;
    }

    public static boolean getAnimating() {
        return mSingleton.mAnimating;
    }
}
