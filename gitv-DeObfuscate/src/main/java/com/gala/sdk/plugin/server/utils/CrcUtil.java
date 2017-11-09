package com.gala.sdk.plugin.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CrcUtil {
    public static int CalCRC(String strFile) {
        IOException e;
        FileNotFoundException e1;
        Throwable th;
        int nResult = 0;
        File file = new File(strFile);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                FileInputStream fis2 = new FileInputStream(file);
                try {
                    byte[] buff = new byte[1024];
                    CRC crc = new CRC();
                    crc.Init();
                    int nRead;
                    do {
                        nRead = fis2.read(buff);
                        if (nRead > 0) {
                            crc.Update(buff, 0, nRead);
                            continue;
                        }
                    } while (nRead >= 1024);
                    nResult = crc.GetDigest();
                    if (fis2 != null) {
                        try {
                            fis2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                            fis = fis2;
                        }
                    }
                    fis = fis2;
                } catch (FileNotFoundException e3) {
                    e1 = e3;
                    fis = fis2;
                    try {
                        e1.printStackTrace();
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        return nResult;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fis != null) {
                            try {
                                fis.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e222 = e4;
                    fis = fis2;
                    e222.printStackTrace();
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                        }
                    }
                    return nResult;
                } catch (Throwable th3) {
                    th = th3;
                    fis = fis2;
                    if (fis != null) {
                        fis.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e5) {
                e1 = e5;
                e1.printStackTrace();
                if (fis != null) {
                    fis.close();
                }
                return nResult;
            } catch (IOException e6) {
                e2222 = e6;
                e2222.printStackTrace();
                if (fis != null) {
                    fis.close();
                }
                return nResult;
            }
            return nResult;
        }
        System.out.println("文件：" + strFile + "不存在！");
        return 0;
    }
}
