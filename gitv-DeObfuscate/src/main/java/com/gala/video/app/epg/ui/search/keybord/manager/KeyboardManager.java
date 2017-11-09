package com.gala.video.app.epg.ui.search.keybord.manager;

import android.annotation.SuppressLint;
import com.gala.video.app.epg.ui.search.keybord.preferece.KeyboardPreference;
import com.gala.video.app.epg.ui.search.keybord.utils.FormatUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

public class KeyboardManager {
    private static final String DUFAULT_ADDRESS = "keyboard/COUNT_1.txt";
    public static final int RECKON_NUMBER_0 = 0;
    public static final int RECKON_NUMBER_1 = 1;
    public static final int RECKON_NUMBER_2 = 2;
    private static final String TAG = "EPG/KeyboardManager";
    private static KeyboardManager mIntance = null;
    private char[][] mDefaultKeys;
    private String mFileAddress;
    private boolean mIsDefaultData = false;
    private boolean mIsLocalHave = false;
    private int mReckonNumber;
    private char[][] mkeys;

    static {
        get().initDefaultKeyboard();
    }

    public static KeyboardManager get() {
        if (mIntance == null) {
            synchronized (KeyboardManager.class) {
                if (mIntance == null) {
                    mIntance = new KeyboardManager();
                }
            }
        }
        return mIntance;
    }

    public void init() {
        CharSequence address = KeyboardPreference.getDownloadAddress(AppRuntimeEnv.get().getApplicationContext());
        if (!StringUtils.isEmpty(address) && FormatUtils.isTxtFormat(address)) {
            initKeyboardManager(address);
        }
    }

    private void initKeyboardManager(String FileAddress) {
        setKeyNumber(2);
        this.mFileAddress = FileAddress;
        readLocalFile();
    }

    private void initDefaultKeyboard() {
        if (!this.mIsDefaultData) {
            this.mIsLocalHave = false;
            this.mDefaultKeys = (char[][]) Array.newInstance(Character.TYPE, new int[]{36, 7});
            this.mIsDefaultData = true;
            readAssetsFile();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "initDefaultKeyboard() ->");
            }
        }
    }

    public void onDestory() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onDestory() ->");
        }
        this.mkeys = (char[][]) null;
    }

    private void setKeyNumber(int num) {
        this.mReckonNumber = num;
        switch (num) {
            case 0:
                this.mkeys = (char[][]) Array.newInstance(Character.TYPE, new int[]{36, 7});
                return;
            case 1:
                this.mkeys = (char[][]) Array.newInstance(Character.TYPE, new int[]{1332, 7});
                return;
            case 2:
                this.mkeys = (char[][]) Array.newInstance(Character.TYPE, new int[]{47988, 7});
                return;
            default:
                return;
        }
    }

    @SuppressLint({"DefaultLocale"})
    public char[] getKeyboard(String input) {
        input = input.toUpperCase();
        int i = 0;
        while (i < input.length()) {
            if ((input.charAt(i) < '0' || input.charAt(i) > '9') && (input.charAt(i) < 'A' || input.charAt(i) > 'Z')) {
                return null;
            }
            i++;
        }
        if (!this.mIsLocalHave || this.mkeys == null) {
            return getKeys(this.mDefaultKeys, input, input.length(), 0);
        }
        char[] keys = getKeys(this.mkeys, input, input.length(), this.mReckonNumber);
        if (keys[0] == '0' && keys[1] == '0') {
            return getKeys(this.mDefaultKeys, input, input.length(), 0);
        }
        return keys;
    }

    private int pos(char abc) {
        if (abc > '<') {
            return abc - 65;
        }
        return abc - 22;
    }

    private void readLocalFile() {
        Exception e;
        Throwable th;
        FileInputStream fread = null;
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        File inputFile = new File(this.mFileAddress);
        try {
            if (inputFile.isFile() && inputFile.exists()) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "read() -> File exists");
                }
                FileInputStream fread2 = new FileInputStream(inputFile);
                try {
                    InputStreamReader read2 = new InputStreamReader(fread2, "GBK");
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(read2);
                        int i = 0;
                        try {
                            String readLine = bufferedReader2.readLine();
                            while (true) {
                                readLine = bufferedReader2.readLine();
                                if (readLine == null || i >= this.mkeys.length) {
                                    bufferedReader2.close();
                                    read2.close();
                                    fread2.close();
                                    this.mIsLocalHave = true;
                                } else {
                                    this.mkeys[i] = readLine.toCharArray();
                                    i++;
                                }
                            }
                            bufferedReader2.close();
                            read2.close();
                            fread2.close();
                            this.mIsLocalHave = true;
                            bufferedReader = bufferedReader2;
                            read = read2;
                            fread = fread2;
                        } catch (Exception e2) {
                            e = e2;
                            bufferedReader = bufferedReader2;
                            read = read2;
                            fread = fread2;
                            try {
                                if (LogUtils.mIsDebug) {
                                    LogUtils.m1571e(TAG, "readLocalFile() -> Error reading the file e:" + e);
                                }
                                this.mIsLocalHave = false;
                                if (bufferedReader != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (IOException e3) {
                                        LogUtils.m1571e(TAG, "bufferedReader:" + e3);
                                    }
                                }
                                if (read != null) {
                                    try {
                                        read.close();
                                    } catch (Exception e4) {
                                        LogUtils.m1571e(TAG, "read:" + e4);
                                    }
                                }
                                if (fread == null) {
                                    try {
                                        fread.close();
                                    } catch (Exception e42) {
                                        LogUtils.m1571e(TAG, "fread:" + e42);
                                        return;
                                    }
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                if (bufferedReader != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (IOException e32) {
                                        LogUtils.m1571e(TAG, "bufferedReader:" + e32);
                                    }
                                }
                                if (read != null) {
                                    try {
                                        read.close();
                                    } catch (Exception e422) {
                                        LogUtils.m1571e(TAG, "read:" + e422);
                                    }
                                }
                                if (fread != null) {
                                    try {
                                        fread.close();
                                    } catch (Exception e4222) {
                                        LogUtils.m1571e(TAG, "fread:" + e4222);
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            bufferedReader = bufferedReader2;
                            read = read2;
                            fread = fread2;
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            if (read != null) {
                                read.close();
                            }
                            if (fread != null) {
                                fread.close();
                            }
                            throw th;
                        }
                    } catch (Exception e5) {
                        e4222 = e5;
                        read = read2;
                        fread = fread2;
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1571e(TAG, "readLocalFile() -> Error reading the file e:" + e4222);
                        }
                        this.mIsLocalHave = false;
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (read != null) {
                            read.close();
                        }
                        if (fread == null) {
                            fread.close();
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        read = read2;
                        fread = fread2;
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        if (read != null) {
                            read.close();
                        }
                        if (fread != null) {
                            fread.close();
                        }
                        throw th;
                    }
                } catch (Exception e6) {
                    e4222 = e6;
                    fread = fread2;
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1571e(TAG, "readLocalFile() -> Error reading the file e:" + e4222);
                    }
                    this.mIsLocalHave = false;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (read != null) {
                        read.close();
                    }
                    if (fread == null) {
                        fread.close();
                    }
                } catch (Throwable th5) {
                    th = th5;
                    fread = fread2;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (read != null) {
                        read.close();
                    }
                    if (fread != null) {
                        fread.close();
                    }
                    throw th;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "readLocalFile() -> File Not Found");
            }
            this.mIsLocalHave = false;
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e322) {
                    LogUtils.m1571e(TAG, "bufferedReader:" + e322);
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (Exception e42222) {
                    LogUtils.m1571e(TAG, "read:" + e42222);
                }
            }
            if (fread != null) {
                try {
                    fread.close();
                } catch (Exception e422222) {
                    LogUtils.m1571e(TAG, "fread:" + e422222);
                }
            }
        } catch (Exception e7) {
            e422222 = e7;
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "readLocalFile() -> Error reading the file e:" + e422222);
            }
            this.mIsLocalHave = false;
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (read != null) {
                read.close();
            }
            if (fread == null) {
                fread.close();
            }
        }
    }

    private void readAssetsFile() {
        Exception e;
        Throwable th;
        InputStream fread = null;
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        try {
            fread = AppRuntimeEnv.get().getApplicationContext().getResources().getAssets().open(DUFAULT_ADDRESS);
            InputStreamReader read2 = new InputStreamReader(fread, "GBK");
            try {
                BufferedReader bufferedReader2 = new BufferedReader(read2);
                int i = 0;
                while (true) {
                    try {
                        String readLine = bufferedReader2.readLine();
                        if (readLine == null || i >= this.mDefaultKeys.length) {
                            bufferedReader2.close();
                            read2.close();
                            fread.close();
                        } else {
                            this.mDefaultKeys[i] = readLine.toCharArray();
                            i++;
                        }
                    } catch (Exception e2) {
                        e = e2;
                        bufferedReader = bufferedReader2;
                        read = read2;
                    } catch (Throwable th2) {
                        th = th2;
                        bufferedReader = bufferedReader2;
                        read = read2;
                    }
                }
                bufferedReader2.close();
                read2.close();
                fread.close();
                if (bufferedReader2 != null) {
                    try {
                        bufferedReader2.close();
                    } catch (IOException e3) {
                        LogUtils.m1571e(TAG, "readAssetsFile bufferedReader:" + e3);
                    }
                }
                if (read2 != null) {
                    try {
                        read2.close();
                    } catch (Exception e4) {
                        LogUtils.m1571e(TAG, "readAssetsFile read:" + e4);
                    }
                }
                if (fread != null) {
                    try {
                        fread.close();
                    } catch (Exception e42) {
                        LogUtils.m1571e(TAG, "readAssetsFile fread:" + e42);
                        bufferedReader = bufferedReader2;
                        read = read2;
                        return;
                    }
                }
                bufferedReader = bufferedReader2;
                read = read2;
            } catch (Exception e5) {
                e42 = e5;
                read = read2;
                try {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1571e(TAG, "readAssets() -> Error reading the file e:" + e42);
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e32) {
                            LogUtils.m1571e(TAG, "readAssetsFile bufferedReader:" + e32);
                        }
                    }
                    if (read != null) {
                        try {
                            read.close();
                        } catch (Exception e422) {
                            LogUtils.m1571e(TAG, "readAssetsFile read:" + e422);
                        }
                    }
                    if (fread != null) {
                        try {
                            fread.close();
                        } catch (Exception e4222) {
                            LogUtils.m1571e(TAG, "readAssetsFile fread:" + e4222);
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e322) {
                            LogUtils.m1571e(TAG, "readAssetsFile bufferedReader:" + e322);
                        }
                    }
                    if (read != null) {
                        try {
                            read.close();
                        } catch (Exception e42222) {
                            LogUtils.m1571e(TAG, "readAssetsFile read:" + e42222);
                        }
                    }
                    if (fread != null) {
                        try {
                            fread.close();
                        } catch (Exception e422222) {
                            LogUtils.m1571e(TAG, "readAssetsFile fread:" + e422222);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                read = read2;
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (read != null) {
                    read.close();
                }
                if (fread != null) {
                    fread.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e422222 = e6;
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "readAssets() -> Error reading the file e:" + e422222);
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (read != null) {
                read.close();
            }
            if (fread != null) {
                fread.close();
            }
        }
    }

    private char[] getKeys(char[][] mDoubleWord, String str, int now, int reckonNumber) {
        int i;
        int posLastLetter = pos(str.charAt(now - 1));
        if (now > 1 && reckonNumber > 0) {
            int j = 1;
            while (j < reckonNumber + 1 && j < now) {
                posLastLetter = (int) (((double) posLastLetter) + Math.pow(36.0d, (double) j));
                j++;
            }
            i = 1;
            while (i < reckonNumber + 1 && (now - i) - 1 >= 0) {
                posLastLetter = (int) (((double) posLastLetter) + (((double) pos(str.charAt((now - i) - 1))) * Math.pow(36.0d, (double) i)));
                i++;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1570d(TAG, "getKeys() -> posLastLetter :", Integer.valueOf(posLastLetter), "str:", str);
        }
        i = 0;
        while (i < mDoubleWord[posLastLetter].length) {
            if ((mDoubleWord[posLastLetter][i] < '0' || mDoubleWord[posLastLetter][i] > '9') && (mDoubleWord[posLastLetter][i] < 'A' || mDoubleWord[posLastLetter][i] > 'Z')) {
                try {
                    mDoubleWord[posLastLetter][i] = '0';
                } catch (Exception e) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1571e(TAG, "getKeys Exception:" + e);
                    }
                    e.printStackTrace();
                }
            }
            i++;
        }
        return mDoubleWord[posLastLetter];
    }
}
