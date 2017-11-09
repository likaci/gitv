package com.gala.imageprovider.p000private;

import android.content.Context;
import com.gala.download.base.FileRequest;
import com.gala.sdk.player.IMediaPlayer;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

final class C0141p {
    private String f581a;
    private Map<String, String> f582a;

    public interface C0140a {
        private /* synthetic */ int f578a;
        private /* synthetic */ C0141p f579a;
        private /* synthetic */ String f580a;

        C0140a(C0141p c0141p, String str, int i) {
            this.f579a = c0141p;
            this.f580a = str;
            this.f578a = i;
        }

        final void m355a() {
            if (C0123G.f541a) {
                C0123G.m279a("IDownloader/CacheFile", ">>>>>afinal - neatFilesAsync() --- deleteFiles --- onCompleted()");
            }
            this.f579a.m358a(this.f580a, this.f578a);
        }
    }

    protected C0141p() {
        this.f581a = null;
        this.f582a = new HashMap();
        this.f581a = "/data/data/com.gala.video/files/galaimages/";
        m358a(this.f581a, (int) IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
    }

    public final void m362a(Context context) {
        if (context != null) {
            this.f581a = "/data/data/" + context.getPackageName() + "/files/galaimages/";
            m358a(this.f581a, (int) IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
        }
    }

    private void m358a(String str, final int i) {
        int i2 = 1;
        File file = new File(str);
        if (file.exists() && file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            if (listFiles != null) {
                if (listFiles.length < i) {
                    i2 = 0;
                } else {
                    final C0140a c0140a = new C0140a(this, str, i);
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
                                c0140a.m355a();
                            } catch (Exception e) {
                                c0140a.m355a();
                            }
                        }
                    });
                    thread.setPriority(1);
                    thread.start();
                }
                if (i2 == 0) {
                    m359a(listFiles);
                }
            }
        }
    }

    private void m359a(File[] fileArr) {
        for (File file : fileArr) {
            if (file.isFile() && !this.f582a.containsKey(file.getName())) {
                this.f582a.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    protected final String m360a(FileRequest fileRequest) {
        String a = C0126b.m300a(fileRequest);
        if (this.f582a.containsKey(a)) {
            return (String) this.f582a.get(a);
        }
        return null;
    }

    protected final String m361a(FileRequest fileRequest, byte[] bArr) {
        String savePath = fileRequest.getSavePath();
        String a = C0126b.m300a(fileRequest);
        if (savePath == null || savePath.equals("")) {
            return m356a(this.f581a, a, bArr);
        }
        return m356a(savePath, a, bArr);
    }

    private String m356a(String str, String str2, byte[] bArr) {
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
                    this.f582a.put(str2, str + str2);
                }
                str3 = str + str2;
            } catch (Throwable e) {
                C0123G.m280a("IDownloader/CacheFile", ">>>>> writeFile: exception happened", e);
                if (file2.exists()) {
                    file2.delete();
                }
            }
        }
        return str3;
    }
}
