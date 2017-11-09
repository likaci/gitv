package com.xcrash.crashreporter.utils;

import android.os.Process;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CircularLogBuffer {
    public static final int SINGLE_LOG_SIZE_LIMIT = 512;
    public boolean enabled;
    private SimpleDateFormat formatter;
    public int logSize;
    private boolean mFullBuffer;
    private int mInsertIndex;
    private List<CircularLog> mLogs;

    class CircularLog {
        String msg;
        int pid;
        String prior;
        String tag;
        int tid;
        long time;

        CircularLog() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(CircularLogBuffer.this.formatter.format(Long.valueOf(this.time)));
            sb.append(" ");
            sb.append(this.pid);
            sb.append(" ");
            sb.append(this.tid);
            sb.append(" ");
            sb.append(this.prior);
            sb.append(" ");
            sb.append(this.tag);
            sb.append(" ");
            sb.append(this.msg);
            if (sb.length() > 512) {
                return sb.toString().substring(0, 512);
            }
            sb.append("\n");
            return sb.toString();
        }
    }

    public CircularLogBuffer() {
        this.logSize = 200;
        this.mInsertIndex = 0;
        this.formatter = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
        this.mFullBuffer = false;
        this.enabled = true;
        this.mLogs = new ArrayList();
    }

    public CircularLogBuffer(int bufferSize) {
        this.logSize = 200;
        this.mInsertIndex = 0;
        this.formatter = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
        this.mFullBuffer = false;
        this.enabled = true;
        this.logSize = bufferSize;
        this.mLogs = new ArrayList();
    }

    public String toString() {
        if (this.mLogs == null || this.mLogs.size() <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int start = this.mFullBuffer ? this.mInsertIndex : 0;
        int size = this.mFullBuffer ? this.logSize : this.mLogs.size();
        for (int i = 0; i < size; i++) {
            sb.append(((CircularLog) this.mLogs.get((start + i) % size)).toString());
        }
        return sb.toString();
    }

    public synchronized void log(String tag, String prior, String msg) {
        if (this.enabled && this.mLogs != null) {
            long time = System.currentTimeMillis();
            int pid = Process.myPid();
            int tid = Process.myTid();
            if (this.mInsertIndex >= this.logSize) {
                this.mInsertIndex = 0;
                this.mFullBuffer = true;
            }
            if (!this.mFullBuffer) {
                this.mLogs.add(this.mInsertIndex, new CircularLog());
            }
            if (this.mLogs.size() > 0) {
                CircularLog log = (CircularLog) this.mLogs.get(this.mInsertIndex);
                log.tag = tag;
                log.prior = prior;
                log.msg = msg;
                log.pid = pid;
                log.tid = tid;
                log.time = time;
                this.mInsertIndex++;
            }
        }
    }
}
