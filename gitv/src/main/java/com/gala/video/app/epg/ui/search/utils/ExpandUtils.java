package com.gala.video.app.epg.ui.search.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.tvos.apps.utils.DateUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({"SimpleDateFormat"})
public class ExpandUtils {
    private static final String TAG = "Log-Record";
    private static ExpandUtils mLogUtils;
    private double mAverageTime;
    private long mBeginInputTime = 0;
    private int mDeleteTimes;
    private long mDoubleInputIntervalTotal;
    private long mEacheCharInputedTime;
    private int mInputCharCount;
    private String mInputedChars;
    private String mKeyBoardType;
    private int mKeyPressCount;
    private String mSelectedWord;
    private int mSelectedWordIndex;
    private double mTotalTime;

    public static synchronized ExpandUtils getInstance() {
        ExpandUtils expandUtils;
        synchronized (ExpandUtils.class) {
            if (mLogUtils == null) {
                mLogUtils = new ExpandUtils();
            }
            expandUtils = mLogUtils;
        }
        return expandUtils;
    }

    public void setKeyBoardType(String keyBoardType) {
        this.mKeyBoardType = keyBoardType;
    }

    public void addInputCharCount(String inputChars) {
        if (!TextUtils.isEmpty(inputChars)) {
            this.mInputCharCount = inputChars.length();
            this.mInputedChars = inputChars;
        }
        Log.e(TAG, "输入字符数：" + this.mInputCharCount);
    }

    public void addKeyPressCount() {
        if (this.mBeginInputTime == 0) {
            this.mBeginInputTime = System.currentTimeMillis();
        }
        this.mKeyPressCount++;
        Log.e(TAG, "总按键次数:" + this.mKeyPressCount);
    }

    public void countDoubleInputInteral(long eacheCharInputedTime) {
        if (this.mEacheCharInputedTime == 0) {
            this.mEacheCharInputedTime = eacheCharInputedTime;
            return;
        }
        long interval = eacheCharInputedTime - this.mEacheCharInputedTime;
        this.mEacheCharInputedTime = eacheCharInputedTime;
        this.mDoubleInputIntervalTotal += interval;
        this.mAverageTime = this.mInputCharCount + -1 <= 0 ? 0.0d : (double) (this.mDoubleInputIntervalTotal / ((long) ((this.mInputCharCount - 1) * 1000)));
        if (this.mAverageTime > 0.0d) {
            this.mAverageTime = ((double) (((int) this.mAverageTime) * 10)) / 10.0d;
            Log.e(TAG, "平均两次字符输入之间的时间:" + this.mAverageTime);
        }
    }

    public void addDeleteTimes() {
        this.mDeleteTimes++;
        Log.e(TAG, "删除次数:" + this.mDeleteTimes);
    }

    public void countTotalTime() {
        this.mTotalTime = (double) ((System.currentTimeMillis() - this.mBeginInputTime) / 1000);
        this.mTotalTime = ((double) (((int) this.mTotalTime) * 10)) / 10.0d;
        Log.e(TAG, "总耗时:" + this.mTotalTime);
    }

    public void reset() {
        this.mInputCharCount = 0;
        this.mKeyPressCount = 0;
        this.mDeleteTimes = 0;
        this.mBeginInputTime = 0;
        this.mAverageTime = 0.0d;
        this.mTotalTime = 0.0d;
        this.mEacheCharInputedTime = 0;
        this.mDoubleInputIntervalTotal = 0;
        this.mInputedChars = "";
        this.mSelectedWord = "";
        this.mSelectedWordIndex = 0;
    }

    public static String getStringDate() {
        return new SimpleDateFormat(DateUtil.PATTERN_STANDARD19H).format(new Date());
    }

    public void showLog(File file) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getStringDate()).append("=====================").append("\n\n");
        buffer.append("使用键盘：").append(this.mKeyBoardType == null ? "" : this.mKeyBoardType).append("\n");
        buffer.append("输入字符:").append(this.mInputedChars).append("\n\n");
        buffer.append("输入字符数：").append(this.mInputCharCount).append("\n");
        buffer.append("总按键次数:").append(this.mKeyPressCount).append("\n");
        buffer.append("点击suggest:").append(this.mSelectedWord).append("\n");
        buffer.append("点击suggest序号:").append(this.mSelectedWordIndex).append("\n");
        buffer.append("平均两次字符输入之间的时间:").append(this.mAverageTime).append("\n");
        buffer.append("删除次数:").append(this.mDeleteTimes).append("\n");
        buffer.append("总耗时:").append(this.mTotalTime).append("\n");
        buffer.append("=====================").append("\n\n");
        String logString = buffer.toString();
        if (file != null) {
            saveToFile(file, logString);
        }
        Log.e(TAG, logString);
    }

    public void showLog() {
        showLog(null);
    }

    public static void saveToFile(final File file, final String content) {
        new Thread8K(new Runnable() {
            public void run() {
                ExpandUtils.writeFile(file, content);
            }
        }, "ExpandUtils#").start();
    }

    public static void writeFile(File file, String content) {
        Exception e;
        Throwable th;
        BufferedWriter out = null;
        try {
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            try {
                out2.write(content);
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        out = out2;
                        return;
                    }
                }
                out = out2;
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    e.printStackTrace();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (out != null) {
                out.close();
            }
        }
    }

    public static String getRelationString(String indexStr, char[] chars) {
        for (char c : chars) {
            if (c == '\u0000') {
                LogUtils.e(TAG, ">>>>> chars is invalid --- has empty data");
                return indexStr;
            }
        }
        return indexStr + String.valueOf(chars);
    }
}
