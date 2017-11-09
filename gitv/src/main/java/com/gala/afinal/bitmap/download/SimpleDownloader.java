package com.gala.afinal.bitmap.download;

import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class SimpleDownloader implements Downloader {
    private static final int IO_BUFFER_SIZE = 32768;
    private static final String TAG = SimpleDownloader.class.getSimpleName();

    public class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long j = 0;
            while (j < n) {
                long skip = this.in.skip(n - j);
                if (skip == 0) {
                    if (read() < 0) {
                        break;
                    }
                    skip = 1;
                }
                j = skip + j;
            }
            return j;
        }
    }

    public byte[] download(String urlString) {
        if (urlString == null) {
            return null;
        }
        if (urlString.trim().toLowerCase().startsWith("http")) {
            return getFromHttp(urlString);
        }
        File file;
        if (urlString.trim().toLowerCase().startsWith("file:")) {
            try {
                file = new File(new URI(urlString));
                if (file.exists() && file.canRead()) {
                    return getFromFile(file);
                }
                return null;
            } catch (URISyntaxException e) {
                Log.e(TAG, "Error in read from file - " + urlString + " : " + e);
                return null;
            }
        }
        file = new File(urlString);
        if (file.exists() && file.canRead()) {
            return getFromFile(file);
        }
        return null;
    }

    private byte[] getFromFile(File file) {
        FileInputStream fileInputStream;
        Object e;
        Throwable th;
        byte[] bArr = null;
        if (file != null) {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr2 = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr2);
                        if (read == -1) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr2, 0, read);
                    }
                    bArr = byteArrayOutputStream.toByteArray();
                    try {
                        fileInputStream.close();
                    } catch (IOException e2) {
                    }
                } catch (Exception e3) {
                    e = e3;
                    try {
                        Log.e(TAG, "Error in read from file - " + file + " : " + e);
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e4) {
                            }
                        }
                        return bArr;
                    } catch (Throwable th2) {
                        th = th2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                }
            } catch (Exception e6) {
                e = e6;
                fileInputStream = bArr;
                Log.e(TAG, "Error in read from file - " + file + " : " + e);
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                return bArr;
            } catch (Throwable th3) {
                fileInputStream = bArr;
                th = th3;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        }
        return bArr;
    }

    private byte[] getFromHttp(String urlString) {
        FlushedInputStream flushedInputStream;
        HttpURLConnection httpURLConnection;
        IOException iOException;
        FlushedInputStream flushedInputStream2;
        Object obj;
        Throwable th;
        FlushedInputStream flushedInputStream3 = null;
        try {
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(urlString).openConnection();
            try {
                httpURLConnection2.setConnectTimeout(10000);
                httpURLConnection2.setReadTimeout(10000);
                flushedInputStream = new FlushedInputStream(new BufferedInputStream(httpURLConnection2.getInputStream(), 32768));
            } catch (IOException e) {
                httpURLConnection = httpURLConnection2;
                iOException = e;
                flushedInputStream2 = flushedInputStream3;
                try {
                    Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + obj);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (flushedInputStream2 != null) {
                        try {
                            flushedInputStream2.close();
                        } catch (IOException iOException2) {
                            Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + iOException2);
                        }
                    }
                    return flushedInputStream3;
                } catch (Throwable th2) {
                    th = th2;
                    flushedInputStream3 = flushedInputStream2;
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    if (flushedInputStream3 != null) {
                        try {
                            flushedInputStream3.close();
                        } catch (IOException e2) {
                            Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + e2);
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                httpURLConnection = httpURLConnection2;
                th = th3;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (flushedInputStream3 != null) {
                    flushedInputStream3.close();
                }
                throw th;
            }
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int read = flushedInputStream.read();
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(read);
                }
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                try {
                    flushedInputStream.close();
                } catch (IOException iOException22) {
                    Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + iOException22);
                }
                return toByteArray;
            } catch (IOException e3) {
                IOException iOException3 = e3;
                flushedInputStream2 = flushedInputStream;
                httpURLConnection = httpURLConnection2;
                obj = iOException3;
                Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + obj);
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (flushedInputStream2 != null) {
                    flushedInputStream2.close();
                }
                return flushedInputStream3;
            } catch (Throwable th4) {
                Throwable th5 = th4;
                flushedInputStream3 = flushedInputStream;
                httpURLConnection = httpURLConnection2;
                th = th5;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (flushedInputStream3 != null) {
                    flushedInputStream3.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            obj = e4;
            flushedInputStream2 = flushedInputStream3;
            Object obj2 = flushedInputStream3;
            Log.e(TAG, "Error in downloadBitmap - " + urlString + " : " + obj);
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (flushedInputStream2 != null) {
                flushedInputStream2.close();
            }
            return flushedInputStream3;
        } catch (Throwable th6) {
            th = th6;
            httpURLConnection = flushedInputStream3;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (flushedInputStream3 != null) {
                flushedInputStream3.close();
            }
            throw th;
        }
    }
}
