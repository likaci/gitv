package com.android.uiautomator.core;

import android.graphics.Point;
import android.os.RemoteException;
import java.io.File;

public class UiDevice {
    UiDevice() {
        throw new RuntimeException("Stub!");
    }

    public static UiDevice getInstance() {
        throw new RuntimeException("Stub!");
    }

    public String getProductName() {
        throw new RuntimeException("Stub!");
    }

    public String getLastTraversedText() {
        throw new RuntimeException("Stub!");
    }

    public void clearLastTraversedText() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressMenu() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressBack() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressHome() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressSearch() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressDPadCenter() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressDPadDown() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressDPadUp() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressDPadLeft() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressDPadRight() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressDelete() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressEnter() {
        throw new RuntimeException("Stub!");
    }

    public boolean pressKeyCode(int keyCode) {
        throw new RuntimeException("Stub!");
    }

    public boolean pressKeyCode(int keyCode, int metaState) {
        throw new RuntimeException("Stub!");
    }

    public boolean pressRecentApps() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public int getDisplayWidth() {
        throw new RuntimeException("Stub!");
    }

    public int getDisplayHeight() {
        throw new RuntimeException("Stub!");
    }

    public boolean click(int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public boolean swipe(int startX, int startY, int endX, int endY, int steps) {
        throw new RuntimeException("Stub!");
    }

    public boolean swipe(Point[] segments, int segmentSteps) {
        throw new RuntimeException("Stub!");
    }

    public void waitForIdle() {
        throw new RuntimeException("Stub!");
    }

    public void waitForIdle(long timeout) {
        throw new RuntimeException("Stub!");
    }

    @Deprecated
    public String getCurrentActivityName() {
        throw new RuntimeException("Stub!");
    }

    public String getCurrentPackageName() {
        throw new RuntimeException("Stub!");
    }

    public void registerWatcher(String name, UiWatcher watcher) {
        throw new RuntimeException("Stub!");
    }

    public void removeWatcher(String name) {
        throw new RuntimeException("Stub!");
    }

    public void runWatchers() {
        throw new RuntimeException("Stub!");
    }

    public void resetWatcherTriggers() {
        throw new RuntimeException("Stub!");
    }

    public boolean hasWatcherTriggered(String watcherName) {
        throw new RuntimeException("Stub!");
    }

    public boolean hasAnyWatcherTriggered() {
        throw new RuntimeException("Stub!");
    }

    public boolean isNaturalOrientation() {
        throw new RuntimeException("Stub!");
    }

    public int getDisplayRotation() {
        throw new RuntimeException("Stub!");
    }

    public void freezeRotation() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void unfreezeRotation() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void setOrientationLeft() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void setOrientationRight() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void setOrientationNatural() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void wakeUp() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public boolean isScreenOn() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void sleep() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public void dumpWindowHierarchy(String fileName) {
        throw new RuntimeException("Stub!");
    }

    public boolean waitForWindowUpdate(String packageName, long timeout) {
        throw new RuntimeException("Stub!");
    }

    public boolean takeScreenshot(File storePath) {
        throw new RuntimeException("Stub!");
    }

    public boolean takeScreenshot(File storePath, float scale, int quality) {
        throw new RuntimeException("Stub!");
    }
}
