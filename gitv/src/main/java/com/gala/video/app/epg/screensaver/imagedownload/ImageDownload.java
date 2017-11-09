package com.gala.video.app.epg.screensaver.imagedownload;

import android.util.Log;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ImageDownload {
    private static final int RESPONSE_OK = 200;
    private static final String TAG = "ImageDownload";

    public void deleteImage(String imagePath) {
        File file = new File(imagePath);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public boolean downloadImage(String urlPath, String fileName) {
        OutOfMemoryError e;
        Throwable th;
        int tryCount = 0;
        boolean isSuccess = false;
        do {
            try {
                HttpURLConnection conn = new HttpUtil().getHttpURLConnection(urlPath);
                if (200 == conn.getResponseCode()) {
                    File file = null;
                    InputStream inputStream = null;
                    FileOutputStream fileOutputStream = null;
                    try {
                        inputStream = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(fileName);
                        try {
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int len = inputStream.read(buffer);
                                if (len == -1) {
                                    break;
                                }
                                fos.write(buffer, 0, len);
                            }
                            isSuccess = true;
                            if (fos != null) {
                                fos.flush();
                                fos.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                        } catch (OutOfMemoryError e2) {
                            e = e2;
                            fileOutputStream = fos;
                        } catch (Throwable th2) {
                            th = th2;
                            fileOutputStream = fos;
                            if (fileOutputStream != null) {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            throw th;
                        }
                    } catch (OutOfMemoryError e3) {
                        e = e3;
                        if (file != null) {
                            try {
                                file.delete();
                            } catch (Throwable th3) {
                                th = th3;
                                if (fileOutputStream != null) {
                                    fileOutputStream.flush();
                                    fileOutputStream.close();
                                }
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                throw th;
                            }
                        }
                        Log.e(TAG, e.getMessage());
                        isSuccess = false;
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return isSuccess;
                    }
                    return isSuccess;
                }
                tryCount++;
                conn.disconnect();
            } catch (Exception e4) {
                tryCount++;
                LogUtils.d(TAG, "try times:" + tryCount + ". " + e4.getMessage());
            }
        } while (tryCount <= 3);
        return isSuccess;
    }
}
