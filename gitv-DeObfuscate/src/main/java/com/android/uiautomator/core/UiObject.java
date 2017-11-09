package com.android.uiautomator.core;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityNodeInfo;

public class UiObject {
    protected static final int SWIPE_MARGIN_LIMIT = 5;
    protected static final long WAIT_FOR_EVENT_TMEOUT = 3000;
    protected static final long WAIT_FOR_SELECTOR_POLL = 1000;
    protected static final long WAIT_FOR_SELECTOR_TIMEOUT = 10000;
    protected static final long WAIT_FOR_WINDOW_TMEOUT = 5500;

    public UiObject(UiSelector selector) {
        throw new RuntimeException("Stub!");
    }

    public final UiSelector getSelector() {
        throw new RuntimeException("Stub!");
    }

    public UiObject getChild(UiSelector selector) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public UiObject getFromParent(UiSelector selector) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public int getChildCount() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    protected AccessibilityNodeInfo findAccessibilityNodeInfo(long timeout) {
        throw new RuntimeException("Stub!");
    }

    public boolean swipeUp(int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean swipeDown(int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean swipeLeft(int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean swipeRight(int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean click() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean clickAndWaitForNewWindow() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean clickAndWaitForNewWindow(long timeout) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean clickTopLeft() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean longClickBottomRight() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean clickBottomRight() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean longClick() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean longClickTopLeft() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public String getText() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public String getContentDescription() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean setText(String text) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public void clearTextField() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isChecked() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isSelected() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isCheckable() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isEnabled() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isClickable() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isFocused() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isFocusable() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isScrollable() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean isLongClickable() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public String getPackageName() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public Rect getVisibleBounds() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public Rect getBounds() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean waitForExists(long timeout) {
        throw new RuntimeException("Stub!");
    }

    public boolean waitUntilGone(long timeout) {
        throw new RuntimeException("Stub!");
    }

    public boolean exists() {
        throw new RuntimeException("Stub!");
    }
}
