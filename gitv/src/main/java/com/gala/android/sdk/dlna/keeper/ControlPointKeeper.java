package com.gala.android.sdk.dlna.keeper;

import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.cybergarage.util.Debug;

public class ControlPointKeeper {
    private static ControlPointKeeper instance = null;
    private static String logFileName = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/igala").append("/dlna/").append("controlpointkeeper.log").toString();
    private String controlPointUUID = null;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void setExternalFilesDir(String ExternalFilesDir) {
        if (ExternalFilesDir != null) {
            logFileName = new StringBuilder(String.valueOf(ExternalFilesDir)).append("/dlna/").append("controlpointkeeper.log").toString();
            Debug.message("ControlPointKeeper changelogFile logFileName: " + logFileName);
            return;
        }
        Debug.message("ControlPointKeeper changelogFile path error!!!!!!");
    }

    public static ControlPointKeeper getInstance() {
        if (instance == null) {
            instance = new ControlPointKeeper();
        }
        return instance;
    }

    public void Save(String uuid) {
        this.controlPointUUID = uuid;
        saveAll();
    }

    public String getUUID() {
        readAll();
        return this.controlPointUUID;
    }

    private void readAll() {
        StreamCorruptedException e;
        Throwable th;
        ObjectInputStream ois = null;
        this.lock.readLock().lock();
        try {
            ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(logFileName));
            try {
                this.controlPointUUID = (String) ois2.readObject();
                if (ois2 != null) {
                    try {
                        ois2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                this.lock.readLock().unlock();
                ois = ois2;
            } catch (StreamCorruptedException e3) {
                e = e3;
                ois = ois2;
                try {
                    e.printStackTrace();
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    this.lock.readLock().unlock();
                } catch (Throwable th2) {
                    th = th2;
                    if (ois != null) {
                        try {
                            ois.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    this.lock.readLock().unlock();
                    throw th;
                }
            } catch (FileNotFoundException e4) {
                ois = ois2;
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                    }
                }
                this.lock.readLock().unlock();
            } catch (IOException e5) {
                ois = ois2;
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                    }
                }
                this.lock.readLock().unlock();
            } catch (ClassNotFoundException e6) {
                ois = ois2;
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e222222) {
                        e222222.printStackTrace();
                    }
                }
                this.lock.readLock().unlock();
            } catch (Throwable th3) {
                th = th3;
                ois = ois2;
                if (ois != null) {
                    ois.close();
                }
                this.lock.readLock().unlock();
                throw th;
            }
        } catch (StreamCorruptedException e7) {
            e = e7;
            e.printStackTrace();
            if (ois != null) {
                ois.close();
            }
            this.lock.readLock().unlock();
        } catch (FileNotFoundException e8) {
            if (ois != null) {
                ois.close();
            }
            this.lock.readLock().unlock();
        } catch (IOException e9) {
            if (ois != null) {
                ois.close();
            }
            this.lock.readLock().unlock();
        } catch (ClassNotFoundException e10) {
            if (ois != null) {
                ois.close();
            }
            this.lock.readLock().unlock();
        }
    }

    private void saveAll() {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        if (this.controlPointUUID != null) {
            this.lock.writeLock().lock();
            File file = new File(logFileName);
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!(parent == null || parent.exists())) {
                    parent.mkdirs();
                }
                try {
                    file.createNewFile();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            ObjectOutputStream oos = null;
            try {
                ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(logFileName));
                try {
                    oos2.writeObject(this.controlPointUUID);
                    if (oos2 != null) {
                        try {
                            oos2.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                    this.lock.writeLock().unlock();
                    oos = oos2;
                } catch (FileNotFoundException e4) {
                    e2 = e4;
                    oos = oos2;
                    try {
                        e2.printStackTrace();
                        if (oos != null) {
                            try {
                                oos.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        this.lock.writeLock().unlock();
                    } catch (Throwable th2) {
                        th = th2;
                        if (oos != null) {
                            try {
                                oos.close();
                            } catch (IOException e3222) {
                                e3222.printStackTrace();
                            }
                        }
                        this.lock.writeLock().unlock();
                        throw th;
                    }
                } catch (IOException e5) {
                    e3222 = e5;
                    oos = oos2;
                    e3222.printStackTrace();
                    if (oos != null) {
                        try {
                            oos.close();
                        } catch (IOException e32222) {
                            e32222.printStackTrace();
                        }
                    }
                    this.lock.writeLock().unlock();
                } catch (Throwable th3) {
                    th = th3;
                    oos = oos2;
                    if (oos != null) {
                        oos.close();
                    }
                    this.lock.writeLock().unlock();
                    throw th;
                }
            } catch (FileNotFoundException e6) {
                e2 = e6;
                e2.printStackTrace();
                if (oos != null) {
                    oos.close();
                }
                this.lock.writeLock().unlock();
            } catch (IOException e7) {
                e32222 = e7;
                e32222.printStackTrace();
                if (oos != null) {
                    oos.close();
                }
                this.lock.writeLock().unlock();
            }
        }
    }
}
