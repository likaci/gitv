package com.android.uiautomator.core;

public class UiScrollable extends UiCollection {
    public UiScrollable(UiSelector container) {
        super((UiSelector) null);
        throw new RuntimeException("Stub!");
    }

    public UiScrollable setAsVerticalList() {
        throw new RuntimeException("Stub!");
    }

    public UiScrollable setAsHorizontalList() {
        throw new RuntimeException("Stub!");
    }

    protected boolean exists(UiSelector selector) {
        throw new RuntimeException("Stub!");
    }

    public UiObject getChildByDescription(UiSelector childPattern, String text) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public UiObject getChildByDescription(UiSelector childPattern, String text, boolean allowScrollSearch) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public UiObject getChildByInstance(UiSelector childPattern, int instance) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public UiObject getChildByText(UiSelector childPattern, String text) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public UiObject getChildByText(UiSelector childPattern, String text, boolean allowScrollSearch) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollDescriptionIntoView(String text) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollIntoView(UiObject obj) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollIntoView(UiSelector selector) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollTextIntoView(String text) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public UiScrollable setMaxSearchSwipes(int swipes) {
        throw new RuntimeException("Stub!");
    }

    public int getMaxSearchSwipes() {
        throw new RuntimeException("Stub!");
    }

    public boolean flingForward() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollForward() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollForward(int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean flingBackward() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollBackward() throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollBackward(int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollToBeginning(int maxSwipes, int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollToBeginning(int maxSwipes) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean flingToBeginning(int maxSwipes) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollToEnd(int maxSwipes, int steps) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean scrollToEnd(int maxSwipes) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public boolean flingToEnd(int maxSwipes) throws UiObjectNotFoundException {
        throw new RuntimeException("Stub!");
    }

    public double getSwipeDeadZonePercentage() {
        throw new RuntimeException("Stub!");
    }

    public UiScrollable setSwipeDeadZonePercentage(double swipeDeadZonePercentage) {
        throw new RuntimeException("Stub!");
    }
}
