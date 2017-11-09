package com.gala.imageprovider.private;

import android.content.Context;
import com.gala.download.base.FileRequest;
import com.gala.sdk.player.IMediaPlayer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

final class p {
    private String a;
    private Map<String, String> f301a;

    public interface a {
        private /* synthetic */ int a;
        private /* synthetic */ p f304a;
        private /* synthetic */ String f305a;

        a(p pVar, String str, int i) {
            this.f304a = pVar;
            this.f305a = str;
            this.a = i;
        }

        final void a() {
            if (G.a) {
                G.a("IDownloader/CacheFile", ">>>>>afinal - neatFilesAsync() --- deleteFiles --- onCompleted()");
            }
            this.f304a.a(this.f305a, this.a);
        }
    }

    protected p() {
        this.a = null;
        this.f301a = new HashMap();
        this.a = "/data/data/com.gala.video/files/galaimages/";
        a(this.a, (int) IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
    }

    public final void a(Context context) {
        if (context != null) {
            this.a = "/data/data/" + context.getPackageName() + "/files/galaimages/";
            a(this.a, (int) IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
        }
    }

    private void a(String str, final int i) {
        int i2 = 1;
        File file = new File(str);
        if (file.exists() && file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            if (listFiles != null) {
                if (listFiles.length < i) {
                    i2 = 0;
                } else {
                    final a aVar = new a(this, str, i);
                    Thread thread = new Thread(new Runnable() {
                        public final void run() {
                            try {
                                int length = listFiles.length / 2;
                                if (length > i) {
                                    length = (listFiles.length - i) - (i / 2);
                                }
                                for (int i = 0; i < length; i++) {
                                    File file = listFiles[i];
                                    if (file.exists() && file.isFile()) {
                                        file.delete();
                                    }
                                }
                                aVar.a();
                            } catch (Exception e) {
                                aVar.a();
                            }
                        }
                    });
                    thread.setPriority(1);
                    thread.start();
                }
                if (i2 == 0) {
                    a(listFiles);
                }
            }
        }
    }

    private void a(File[] fileArr) {
        for (File file : fileArr) {
            if (file.isFile() && !this.f301a.containsKey(file.getName())) {
                this.f301a.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    protected final String a(FileRequest fileRequest) {
        String a = b.a(fileRequest);
        if (this.f301a.containsKey(a)) {
            return (String) this.f301a.get(a);
        }
        return null;
    }

    protected final String a(FileRequest fileRequest, byte[] bArr) {
        String savePath = fileRequest.getSavePath();
        String a = b.a(fileRequest);
        if (savePath == null || savePath.equals("")) {
            return a(this.a, a, bArr);
        }
        return a(savePath, a, bArr);
    }

    private String a(String str, String str2, byte[] bArr) {
        String str3 = null;
        if (str != null) {
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(str + str2);
            try {
                if (file2.exists()) {
                    file2.delete();
                }
                if (file2.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    fileOutputStream.write(bArr);
                    fileOutputStream.close();
                    this.f301a.put(str2, str + str2);
                }
                str3 = str + str2;
            } catch (Throwable e) {
                G.a("IDownloader/CacheFile", ">>>>> writeFile: exception happened", e);
                if (file2.exists()) {
                    file2.delete();
                }
            }
        }
        return str3;
    }
}
