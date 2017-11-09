package com.gala.video.lib.framework.core.network.download.core;

import android.util.Log;
import com.gala.video.lib.framework.core.network.download.GalaDownloadException;
import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GalaImageDownloader extends GalaBaseDownloader {
    private boolean mIsSaveFileSuccessful = true;

    public /* bridge */ /* synthetic */ void callAsync(IGalaDownloadListener iGalaDownloadListener) {
        super.callAsync(iGalaDownloadListener);
    }

    public /* bridge */ /* synthetic */ void callSync(IGalaDownloadListener iGalaDownloadListener) {
        super.callSync(iGalaDownloadListener);
    }

    public /* bridge */ /* synthetic */ IGalaDownloadParameter getDownloadParameter() {
        return super.getDownloadParameter();
    }

    public /* bridge */ /* synthetic */ boolean isDownloading() {
        return super.isDownloading();
    }

    protected void callAsync() {
    }

    protected void callSync() {
    }

    protected String makeSavePath() {
        return null;
    }

    public void cancel() {
        Log.w("Downloader", "ImageDownloader doesn't provide cancel task!");
    }

    private Callback createCallback(final String savePath) {
        return new Callback() {
            public void onFailure(Call call, IOException e) {
                GalaImageDownloader.this.onDownloadFailure(e);
            }

            public void onResponse(Call call, Response response) throws IOException {
                GalaImageDownloader.this.checkResult(response, savePath);
            }
        };
    }

    private void checkResult(Response response, String savePath) {
        byte[] result = getResult(response, savePath);
        if (result == null) {
            return;
        }
        if (this.mIsSaveFileSuccessful) {
            this.mDownloadListener.onSuccess(result, savePath);
        } else {
            this.mDownloadListener.onSuccess(result, null);
        }
    }

    private byte[] getResult(Response response, String savePath) {
        IOException e;
        Throwable th;
        if (!(savePath == null || savePath.isEmpty())) {
            File file = new File(savePath);
            if (file.exists()) {
                file.delete();
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] bytes = new byte[1024];
                inputStream = response.body().byteStream();
                OutputStream outputStream2 = new FileOutputStream(file);
                while (true) {
                    try {
                        int read = inputStream.read(bytes);
                        if (read == -1) {
                            break;
                        }
                        outputStream2.write(bytes, 0, read);
                    } catch (IOException e2) {
                        e = e2;
                        outputStream = outputStream2;
                    } catch (Throwable th2) {
                        th = th2;
                        outputStream = outputStream2;
                    }
                }
                outputStream2.flush();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e3) {
                        this.mIsSaveFileSuccessful = false;
                        this.mDownloadListener.onError(new GalaDownloadException(5, e3.getMessage()));
                    }
                }
                if (outputStream2 != null) {
                    outputStream2.close();
                }
            } catch (IOException e4) {
                e3 = e4;
                try {
                    this.mIsSaveFileSuccessful = false;
                    this.mDownloadListener.onError(new GalaDownloadException(5, e3.getMessage()));
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e32) {
                            this.mIsSaveFileSuccessful = false;
                            this.mDownloadListener.onError(new GalaDownloadException(5, e32.getMessage()));
                        }
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    return response.body().bytes();
                } catch (Throwable th3) {
                    th = th3;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e322) {
                            this.mIsSaveFileSuccessful = false;
                            this.mDownloadListener.onError(new GalaDownloadException(5, e322.getMessage()));
                            throw th;
                        }
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    throw th;
                }
            }
        }
        try {
            return response.body().bytes();
        } catch (IOException e3222) {
            e3222.printStackTrace();
            return null;
        }
    }
}
