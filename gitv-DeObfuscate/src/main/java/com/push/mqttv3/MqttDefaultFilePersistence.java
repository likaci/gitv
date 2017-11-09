package com.push.mqttv3;

import com.push.mqttv3.internal.FileLock;
import com.push.mqttv3.internal.MqttPersistentData;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Vector;

public class MqttDefaultFilePersistence implements MqttClientPersistence {
    private static final FilenameFilter FILE_FILTER = new C19641();
    private static final String LOCK_FILENAME = ".lck";
    private static final String MESSAGE_BACKUP_FILE_EXTENSION = ".bup";
    private static final String MESSAGE_FILE_EXTENSION = ".msg";
    private File clientDir;
    private File dataDir;
    private FileLock fileLock;

    static class C19641 implements FilenameFilter {
        C19641() {
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(MqttDefaultFilePersistence.MESSAGE_FILE_EXTENSION);
        }
    }

    class C19652 implements FileFilter {
        C19652() {
        }

        public boolean accept(File f) {
            return f.getName().endsWith(MqttDefaultFilePersistence.MESSAGE_BACKUP_FILE_EXTENSION);
        }
    }

    public MqttDefaultFilePersistence() throws MqttPersistenceException {
        this(System.getProperty("user.dir"));
    }

    public MqttDefaultFilePersistence(String directory) throws MqttPersistenceException {
        this.clientDir = null;
        this.fileLock = null;
        this.dataDir = new File(directory);
    }

    public void open(String clientId, String theConnection) throws MqttPersistenceException {
        if (!this.dataDir.exists() || this.dataDir.isDirectory()) {
            int i;
            char c;
            if (this.dataDir.exists()) {
                if (!this.dataDir.canWrite()) {
                    throw new MqttPersistenceException();
                }
            } else if (!this.dataDir.mkdirs()) {
                throw new MqttPersistenceException();
            }
            StringBuffer keyBuffer = new StringBuffer();
            for (i = 0; i < clientId.length(); i++) {
                c = clientId.charAt(i);
                if (isSafeChar(c)) {
                    keyBuffer.append(c);
                }
            }
            keyBuffer.append("-");
            for (i = 0; i < theConnection.length(); i++) {
                c = theConnection.charAt(i);
                if (isSafeChar(c)) {
                    keyBuffer.append(c);
                }
            }
            this.clientDir = new File(this.dataDir, keyBuffer.toString());
            if (!this.clientDir.exists()) {
                this.clientDir.mkdir();
            }
            try {
                this.fileLock = new FileLock(new File(this.clientDir, LOCK_FILENAME));
                restoreBackups(this.clientDir);
                return;
            } catch (Exception e) {
                throw new MqttPersistenceException(32200);
            }
        }
        throw new MqttPersistenceException();
    }

    private void checkIsOpen() throws MqttPersistenceException {
        if (this.clientDir == null) {
            throw new MqttPersistenceException();
        }
    }

    public void close() throws MqttPersistenceException {
        checkIsOpen();
        if (this.fileLock != null) {
            this.fileLock.release();
            File lockFile = new File(this.clientDir, LOCK_FILENAME);
            if (lockFile.exists()) {
                lockFile.delete();
            }
        }
        if (this.clientDir != null && this.clientDir.listFiles(FILE_FILTER).length == 0) {
            this.clientDir.delete();
        }
        this.clientDir = null;
    }

    public void put(String key, MqttPersistable message) throws MqttPersistenceException {
        checkIsOpen();
        File file = new File(this.clientDir, key + MESSAGE_FILE_EXTENSION);
        File backupFile = new File(this.clientDir, key + MESSAGE_FILE_EXTENSION + MESSAGE_BACKUP_FILE_EXTENSION);
        if (file.exists() && !file.renameTo(backupFile)) {
            backupFile.delete();
            file.renameTo(backupFile);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(message.getHeaderBytes(), message.getHeaderOffset(), message.getHeaderLength());
            if (message.getPayloadBytes() != null) {
                fos.write(message.getPayloadBytes(), message.getPayloadOffset(), message.getPayloadLength());
            }
            fos.getFD().sync();
            fos.close();
            if (backupFile.exists()) {
                backupFile.delete();
            }
            if (backupFile.exists() && !backupFile.renameTo(file)) {
                file.delete();
                backupFile.renameTo(file);
            }
        } catch (Throwable ex) {
            throw new MqttPersistenceException(ex);
        } catch (Throwable th) {
            if (backupFile.exists() && !backupFile.renameTo(file)) {
                file.delete();
                backupFile.renameTo(file);
            }
        }
    }

    public MqttPersistable get(String key) throws MqttPersistenceException {
        checkIsOpen();
        try {
            FileInputStream fis = new FileInputStream(new File(this.clientDir, key + MESSAGE_FILE_EXTENSION));
            int size = fis.available();
            byte[] data = new byte[size];
            for (int read = 0; read < size; read += fis.read(data, read, size - read)) {
            }
            fis.close();
            return new MqttPersistentData(key, data, 0, data.length, null, 0, 0);
        } catch (Throwable ex) {
            throw new MqttPersistenceException(ex);
        }
    }

    public void remove(String key) throws MqttPersistenceException {
        checkIsOpen();
        File file = new File(this.clientDir, key + MESSAGE_FILE_EXTENSION);
        if (file.exists()) {
            file.delete();
        }
    }

    public Enumeration keys() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = getFiles();
        Vector result = new Vector(files.length);
        for (File name : files) {
            String filename = name.getName();
            result.addElement(filename.substring(0, filename.length() - MESSAGE_FILE_EXTENSION.length()));
        }
        return result.elements();
    }

    private File[] getFiles() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = this.clientDir.listFiles(FILE_FILTER);
        if (files != null) {
            return files;
        }
        throw new MqttPersistenceException();
    }

    private boolean isSafeChar(char c) {
        return Character.isJavaIdentifierPart(c) || c == '-';
    }

    private void restoreBackups(File dir) throws MqttPersistenceException {
        File[] files = dir.listFiles(new C19652());
        for (int i = 0; i < files.length; i++) {
            File originalFile = new File(dir, files[i].getName().substring(0, files[i].getName().length() - MESSAGE_BACKUP_FILE_EXTENSION.length()));
            if (!files[i].renameTo(originalFile)) {
                originalFile.delete();
                files[i].renameTo(originalFile);
            }
        }
    }

    public boolean containsKey(String key) throws MqttPersistenceException {
        checkIsOpen();
        return new File(this.clientDir, key + MESSAGE_FILE_EXTENSION).exists();
    }

    public void clear() throws MqttPersistenceException {
        checkIsOpen();
        File[] files = getFiles();
        for (File delete : files) {
            delete.delete();
        }
    }
}
