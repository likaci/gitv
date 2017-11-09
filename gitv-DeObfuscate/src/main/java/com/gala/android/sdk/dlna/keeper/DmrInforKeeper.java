package com.gala.android.sdk.dlna.keeper;

import android.os.Environment;
import android.text.TextUtils;
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

public class DmrInforKeeper {
    private static DmrInforKeeper instance = null;
    public static String logFileName = (logPath + "dmrkeeper.log");
    public static String logPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getPath())).append("/igala").append("/dlna/").toString();
    private DmrInfor dmrInfor = null;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            logPath = path;
            logFileName = logPath + "dmrkeeper.log";
        }
    }

    public static DmrInforKeeper getInstance() {
        if (instance == null) {
            instance = new DmrInforKeeper();
        }
        return instance;
    }

    public void SaveDmrInfor(DmrInfor infor, String packagename) {
        if (infor != null) {
            this.dmrInfor = infor;
            saveAll(packagename);
        }
    }

    public DmrInfor getDmrInfor(String packagename) {
        readAll(packagename);
        if (this.dmrInfor == null) {
            return null;
        }
        return this.dmrInfor;
    }

    private void readAll(String packagename) {
        StreamCorruptedException e;
        Throwable th;
        ObjectInputStream ois = null;
        this.lock.readLock().lock();
        try {
            ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(logFileName + packagename));
            try {
                this.dmrInfor = (DmrInfor) ois2.readObject();
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

    private void saveAll(String packagename) {
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        if (this.dmrInfor == null) {
            this.dmrInfor = new DmrInfor();
            return;
        }
        this.lock.writeLock().lock();
        File file = new File(logFileName + packagename);
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
            ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(logFileName + packagename));
            try {
                oos2.writeObject(this.dmrInfor);
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
