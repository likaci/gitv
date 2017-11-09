package com.push.mqttv3.internal.trace;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TraceFileDestination implements TraceDestination {
    private ByteArrayOutputStream buffer;
    private int currentFileSize = 0;
    private DataOutputStream dos;
    private boolean enabled = false;
    private int fileCount;
    private int fileIndex = 0;
    private int maxFileSize;
    private FileOutputStream out;
    private File traceDirectory;
    private Properties traceProperties = new Properties();
    private File tracePropertiesFile;
    private long tracePropertiesUpdate = 0;

    public TraceFileDestination() {
        String tracePropertiesFilename = System.getProperty("org.eclipse.paho.client.mqttv3.trace");
        if (tracePropertiesFilename == null) {
            this.tracePropertiesFile = new File(System.getProperty("user.dir"), "mqtt-trace.properties");
        } else {
            this.tracePropertiesFile = new File(tracePropertiesFilename);
        }
        updateTraceProperties();
    }

    private boolean updateTraceProperties() {
        if (!this.tracePropertiesFile.exists()) {
            this.traceProperties.clear();
            this.tracePropertiesUpdate = 0;
            this.enabled = false;
            return false;
        } else if (this.tracePropertiesFile.lastModified() == this.tracePropertiesUpdate) {
            return this.enabled;
        } else {
            try {
                this.traceProperties.load(new FileInputStream(this.tracePropertiesFile));
                this.tracePropertiesUpdate = this.tracePropertiesFile.lastModified();
                this.traceDirectory = new File(this.traceProperties.getProperty("org.eclipse.paho.client.mqttv3.trace.outputName", System.getProperty("user.dir")));
                if (this.traceDirectory.exists()) {
                    this.fileCount = Integer.parseInt(this.traceProperties.getProperty("org.eclipse.paho.client.mqttv3.trace.count", "1"));
                    this.maxFileSize = Integer.parseInt(this.traceProperties.getProperty("org.eclipse.paho.client.mqttv3.trace.limit", "5000000"));
                    initialiseFile();
                    if (this.out == null) {
                        this.traceProperties.clear();
                        this.tracePropertiesUpdate = 0;
                        this.enabled = false;
                        return false;
                    }
                    this.buffer = new ByteArrayOutputStream();
                    this.dos = new DataOutputStream(this.buffer);
                    this.enabled = true;
                    return true;
                }
                this.traceProperties.clear();
                this.tracePropertiesUpdate = 0;
                this.enabled = false;
                return false;
            } catch (Exception e) {
                this.traceProperties.clear();
                this.tracePropertiesUpdate = 0;
                this.enabled = false;
                return false;
            }
        }
    }

    public boolean isEnabled(String resource) {
        return this.enabled && ("on".equalsIgnoreCase(this.traceProperties.getProperty("org.eclipse.paho.client.mqttv3.trace.client.*.status")) || "on".equalsIgnoreCase(this.traceProperties.getProperty("org.eclipse.paho.client.mqttv3.trace.client." + resource + ".status")));
    }

    public void initialiseFile() {
        if (this.out != null) {
            try {
                this.out.close();
            } catch (IOException e) {
            }
            this.out = null;
        }
        this.currentFileSize = 0;
        File traceFile = new File(this.traceDirectory, "mqtt-" + this.fileIndex + ".trc");
        if (traceFile.exists()) {
            traceFile.delete();
        }
        try {
            this.out = new FileOutputStream(traceFile);
        } catch (FileNotFoundException e2) {
            this.enabled = false;
            this.out = null;
        }
    }

    public synchronized void write(TracePoint point) {
        try {
            int i;
            this.dos.writeShort(point.source);
            this.dos.writeLong(point.timestamp);
            byte meta = point.type;
            if (point.inserts != null && point.inserts.length > 0) {
                meta = (byte) (meta | 32);
            }
            if (point.throwable != null) {
                meta = (byte) (meta | 64);
            }
            this.dos.writeByte(meta);
            this.dos.writeShort(point.id);
            this.dos.writeUTF(point.threadName);
            if (point.inserts != null && point.inserts.length > 0) {
                this.dos.writeShort(point.inserts.length);
                for (i = 0; i < point.inserts.length; i++) {
                    if (point.inserts[i] != null) {
                        this.dos.writeUTF(point.inserts[i].toString());
                    } else {
                        this.dos.writeUTF("null");
                    }
                }
            }
            if (point.throwable != null) {
                StackTraceElement[] stack = point.throwable.getStackTrace();
                this.dos.writeShort(stack.length + 1);
                this.dos.writeUTF(point.throwable.toString());
                for (StackTraceElement stackTraceElement : stack) {
                    this.dos.writeUTF(stackTraceElement.toString());
                }
            }
            if (this.fileCount > 1 && this.currentFileSize + this.buffer.size() > this.maxFileSize) {
                this.fileIndex++;
                if (this.fileIndex == this.fileCount) {
                    this.fileIndex = 0;
                }
                initialiseFile();
            }
            if (this.out != null) {
                this.currentFileSize += this.buffer.size();
                this.buffer.writeTo(this.out);
                this.out.flush();
            }
            this.buffer.reset();
        } catch (Exception e) {
            this.enabled = false;
        }
        return;
    }
}
