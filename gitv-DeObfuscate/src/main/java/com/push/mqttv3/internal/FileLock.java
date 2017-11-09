package com.push.mqttv3.internal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileLock {
    private RandomAccessFile file;
    private Object fileLock;

    public FileLock(File lockFile) throws Exception {
        if (ExceptionHelper.isClassAvailable("java.nio.channels.FileLock")) {
            try {
                this.file = new RandomAccessFile(lockFile, "rw");
                Object channel = this.file.getClass().getMethod("getChannel", new Class[0]).invoke(this.file, new Object[0]);
                this.fileLock = channel.getClass().getMethod("tryLock", new Class[0]).invoke(channel, new Object[0]);
                this.file.close();
            } catch (NoSuchMethodException e) {
                this.fileLock = null;
            } catch (IllegalArgumentException e2) {
                this.fileLock = null;
            } catch (IllegalAccessException e3) {
                this.fileLock = null;
            }
        }
    }

    public void release() {
        try {
            if (this.fileLock != null) {
                this.fileLock.getClass().getMethod("release", new Class[0]).invoke(this.fileLock, new Object[0]);
            }
        } catch (Exception e) {
        }
        if (this.file != null) {
            try {
                this.file.close();
            } catch (IOException e2) {
            }
        }
    }
}
