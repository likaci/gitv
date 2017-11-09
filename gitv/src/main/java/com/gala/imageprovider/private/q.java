package com.gala.imageprovider.private;

import android.content.Context;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pl.droidsonroids.gif.LibraryLoader;

public final class q implements IDownloader {
    private static q a;
    private int f282a;
    private o f283a = o.a();
    private r f284a = new r();
    private boolean f285a = true;
    private boolean b;

    static {
        o.a();
    }

    public static synchronized q a() {
        q qVar;
        synchronized (q.class) {
            if (a == null) {
                a = new q();
            }
            qVar = a;
        }
        return qVar;
    }

    private q() {
    }

    public final void initialize(Context context) {
        D.a();
        D.a(context);
        this.f283a.a(context);
        LibraryLoader.initialize(context);
    }

    public final void initialize(Context context, String str) {
        D.a();
        D.a(context);
        this.f283a.a(context);
        LibraryLoader.initialize(context);
    }

    public final void m68a() {
        o oVar = this.f283a;
        o.a();
    }

    public final void loadFile(FileRequest imageRequest, IFileCallback fileCallback) {
        if (FileRequest.checkRequestValid(imageRequest)) {
            List arrayList = new ArrayList();
            arrayList.add(imageRequest);
            loadFiles(arrayList, fileCallback);
            return;
        }
        if (G.a) {
            G.b("GalaDownloader", ">>>>> loadFile: invalid request: " + imageRequest);
        }
        if (fileCallback != null) {
            fileCallback.onFailure(imageRequest, new y("Params is wrong!"));
        }
    }

    public final void loadFiles(List<FileRequest> imageRequestList, IFileCallback fileCallback) {
        if (!(imageRequestList == null || imageRequestList.isEmpty())) {
            Iterator it = imageRequestList.iterator();
            while (it.hasNext()) {
                it.next();
            }
        }
        this.f284a.a((List) imageRequestList, fileCallback);
    }

    public final void stopAllTasks() {
        G.a("GalaDownloader", ">>>>> stopAllTasks");
    }

    public final void a(C c) {
        this.f284a.a(c);
    }

    public final String getLocalPath(FileRequest request) {
        return this.f283a.a(request);
    }

    public final void a(Runnable runnable) {
        this.f284a.a(runnable);
    }

    public final void b(Runnable runnable) {
        this.f284a.b(runnable);
    }

    public final void setEnableFastSave(boolean enable) {
        this.f285a = enable;
    }

    public final boolean isEnableFastSave() {
        return this.f285a;
    }

    public final void setEnableFullPathCacheKey(boolean enable) {
        this.b = enable;
    }

    public final boolean isEnableFullPathCacheKey() {
        return this.b;
    }

    public final void setDiskCacheSize(int i) {
    }

    public final void setThreadSize(int i) {
    }

    public final void setDiskCacheCount(int i) {
    }

    public final void loadGif(FileRequest request, IGifCallback gifCallback) {
        if (FileRequest.checkRequestValid(request)) {
            List arrayList = new ArrayList();
            arrayList.add(request);
            loadGifs(arrayList, gifCallback);
            return;
        }
        G.b("GalaDownloader", ">>>>> loadFile: invalid request: " + request);
        if (gifCallback != null) {
            gifCallback.onFailure(request, new y("Params is wrong!"));
        }
    }

    public final void loadGifs(List<FileRequest> requests, IGifCallback gifCallback) {
        if (requests != null && !requests.isEmpty()) {
            for (FileRequest fileRequest : requests) {
                if (fileRequest == null) {
                    break;
                } else if (this.f282a > 0 && fileRequest.getLimitSize() == 0) {
                    fileRequest.setLimitSize(this.f282a);
                }
            }
        }
        this.f284a.a((List) requests, gifCallback);
    }

    public final void setGifLimitSize(int fileLimitSize) {
        this.f282a = fileLimitSize;
    }
}
