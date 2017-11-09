package com.gala.video.lib.framework.core.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class FastCacheLoader<T> implements IFastCache<T> {
    private static final String TAG = "FastCacheLoader";
    private final String mSavePath;
    private T result;

    public FastCacheLoader(String path) {
        this.mSavePath = path;
    }

    public synchronized T save(String data) {
        Class entityClass = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (!StringUtils.isEmpty((CharSequence) data)) {
            try {
                this.result = JSON.parseObject(data, entityClass);
            } catch (Exception e) {
                LogUtils.m1568d(TAG, " json parse exception : " + e);
            }
            writeDataToLocalCache(data, this.mSavePath);
        }
        LogUtils.m1568d(TAG, "save failed !!!,path = " + this.mSavePath);
        return this.result;
    }

    public synchronized T get() {
        if (this.result == null) {
            byte[] raw = readDataFromLocalCache(this.mSavePath);
            if (raw != null) {
                try {
                    this.result = JSON.parseObject(raw, (Type) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0], new Feature[0]);
                } catch (Exception e) {
                    LogUtils.m1568d(TAG, " json parse exception : " + e);
                }
            }
        }
        return this.result;
    }

    public void save(String data, String path) {
        if (StringUtils.isEmpty((CharSequence) data)) {
            LogUtils.m1568d(TAG, "save failed, data is null!!!,path = " + path);
        } else {
            writeDataToLocalCache(data, path);
        }
    }

    public byte[] get(String path) {
        return readDataFromLocalCache(path);
    }

    public synchronized byte[] readDataFromLocalCache(String path) {
        IOException e;
        Throwable th;
        Exception e1;
        RandomAccessFile rFile = null;
        FileChannel fc = null;
        byte[] buffer = null;
        try {
            RandomAccessFile rFile2 = new RandomAccessFile(new File(path), "r");
            try {
                fc = rFile2.getChannel();
                buffer = new byte[((int) rFile2.length())];
                fc.map(MapMode.READ_ONLY, 0, rFile2.length()).get(buffer);
                if (fc != null) {
                    try {
                        fc.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        rFile = rFile2;
                    } catch (Throwable th2) {
                        th = th2;
                        rFile = rFile2;
                        throw th;
                    }
                }
                if (rFile2 != null) {
                    rFile2.close();
                }
                rFile = rFile2;
            } catch (IOException e3) {
                e2 = e3;
                rFile = rFile2;
                try {
                    LogUtils.m1572e(TAG, "read from local cache exception，path = " + path, e2);
                    if (fc != null) {
                        try {
                            fc.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        } catch (Throwable th3) {
                            th = th3;
                            throw th;
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    return buffer;
                } catch (Throwable th4) {
                    th = th4;
                    if (fc != null) {
                        try {
                            fc.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            throw th;
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e1 = e4;
                rFile = rFile2;
                LogUtils.m1572e(TAG, "read from local cache exception，path = " + path, e1);
                if (fc != null) {
                    try {
                        fc.close();
                    } catch (IOException e2222) {
                        e2222.printStackTrace();
                        return buffer;
                    }
                }
                if (rFile != null) {
                    rFile.close();
                }
                return buffer;
            } catch (Throwable th5) {
                th = th5;
                rFile = rFile2;
                if (fc != null) {
                    fc.close();
                }
                if (rFile != null) {
                    rFile.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e2222 = e5;
            LogUtils.m1572e(TAG, "read from local cache exception，path = " + path, e2222);
            if (fc != null) {
                fc.close();
            }
            if (rFile != null) {
                rFile.close();
            }
            return buffer;
        } catch (Exception e6) {
            e1 = e6;
            LogUtils.m1572e(TAG, "read from local cache exception，path = " + path, e1);
            if (fc != null) {
                fc.close();
            }
            if (rFile != null) {
                rFile.close();
            }
            return buffer;
        }
    }

    public void writeDataToLocalCache(String raw, String path) {
        IOException e;
        Throwable th;
        Exception e1;
        RandomAccessFile rFile = null;
        FileChannel mFc = null;
        try {
            RandomAccessFile rFile2 = new RandomAccessFile(new File(path), "rws");
            try {
                mFc = rFile2.getChannel();
                rFile2.setLength((long) raw.getBytes().length);
                mFc.map(MapMode.READ_WRITE, 0, (long) raw.getBytes().length).put(raw.getBytes());
                if (mFc != null) {
                    try {
                        mFc.close();
                    } catch (IOException e2) {
                        LogUtils.m1569d(TAG, "write data to disk failed", e2);
                        rFile = rFile2;
                        return;
                    }
                }
                if (rFile2 != null) {
                    rFile2.close();
                }
                rFile = rFile2;
            } catch (IOException e3) {
                e2 = e3;
                rFile = rFile2;
                try {
                    LogUtils.m1569d(TAG, "write data to disk failed", e2);
                    if (mFc != null) {
                        try {
                            mFc.close();
                        } catch (IOException e22) {
                            LogUtils.m1569d(TAG, "write data to disk failed", e22);
                            return;
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (mFc != null) {
                        try {
                            mFc.close();
                        } catch (IOException e222) {
                            LogUtils.m1569d(TAG, "write data to disk failed", e222);
                            throw th;
                        }
                    }
                    if (rFile != null) {
                        rFile.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e1 = e4;
                rFile = rFile2;
                LogUtils.m1569d(TAG, "write data to disk failed", e1);
                if (mFc != null) {
                    try {
                        mFc.close();
                    } catch (IOException e2222) {
                        LogUtils.m1569d(TAG, "write data to disk failed", e2222);
                        return;
                    }
                }
                if (rFile != null) {
                    rFile.close();
                }
            } catch (Throwable th3) {
                th = th3;
                rFile = rFile2;
                if (mFc != null) {
                    mFc.close();
                }
                if (rFile != null) {
                    rFile.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e2222 = e5;
            LogUtils.m1569d(TAG, "write data to disk failed", e2222);
            if (mFc != null) {
                mFc.close();
            }
            if (rFile != null) {
                rFile.close();
            }
        } catch (Exception e6) {
            e1 = e6;
            LogUtils.m1569d(TAG, "write data to disk failed", e1);
            if (mFc != null) {
                mFc.close();
            }
            if (rFile != null) {
                rFile.close();
            }
        }
    }

    public void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
