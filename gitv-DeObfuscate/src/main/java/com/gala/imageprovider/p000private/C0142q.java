package com.gala.imageprovider.p000private;

import android.content.Context;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.download.base.IGifCallback;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import pl.droidsonroids.gif.LibraryLoader;

public final class C0142q implements IDownloader {
    private static C0142q f583a;
    private int f584a;
    private C0138o f585a = C0138o.m350a();
    private C0144r f586a = new C0144r();
    private boolean f587a = true;
    private boolean f588b;

    static {
        C0138o.m350a();
    }

    public static synchronized C0142q m363a() {
        C0142q c0142q;
        synchronized (C0142q.class) {
            if (f583a == null) {
                f583a = new C0142q();
            }
            c0142q = f583a;
        }
        return c0142q;
    }

    private C0142q() {
    }

    public final void initialize(Context context) {
        C0120D.m274a();
        C0120D.m275a(context);
        this.f585a.m354a(context);
        LibraryLoader.initialize(context);
    }

    public final void initialize(Context context, String str) {
        C0120D.m274a();
        C0120D.m275a(context);
        this.f585a.m354a(context);
        LibraryLoader.initialize(context);
    }

    public final void m364a() {
        C0138o c0138o = this.f585a;
        C0138o.m350a();
    }

    public final void loadFile(FileRequest imageRequest, IFileCallback fileCallback) {
        if (FileRequest.checkRequestValid(imageRequest)) {
            List arrayList = new ArrayList();
            arrayList.add(imageRequest);
            loadFiles(arrayList, fileCallback);
            return;
        }
        if (C0123G.f541a) {
            C0123G.m282b("GalaDownloader", ">>>>> loadFile: invalid request: " + imageRequest);
        }
        if (fileCallback != null) {
            fileCallback.onFailure(imageRequest, new C0153y("Params is wrong!"));
        }
    }

    public final void loadFiles(List<FileRequest> imageRequestList, IFileCallback fileCallback) {
        if (!(imageRequestList == null || imageRequestList.isEmpty())) {
            Iterator it = imageRequestList.iterator();
            while (it.hasNext()) {
                it.next();
            }
        }
        this.f586a.m372a((List) imageRequestList, fileCallback);
    }

    public final void stopAllTasks() {
        C0123G.m279a("GalaDownloader", ">>>>> stopAllTasks");
    }

    public final void m365a(C0114C c0114c) {
        this.f586a.m370a(c0114c);
    }

    public final String getLocalPath(FileRequest request) {
        return this.f585a.m352a(request);
    }

    public final void m366a(Runnable runnable) {
        this.f586a.m371a(runnable);
    }

    public final void m367b(Runnable runnable) {
        this.f586a.m374b(runnable);
    }

    public final void setEnableFastSave(boolean enable) {
        this.f587a = enable;
    }

    public final boolean isEnableFastSave() {
        return this.f587a;
    }

    public final void setEnableFullPathCacheKey(boolean enable) {
        this.f588b = enable;
    }

    public final boolean isEnableFullPathCacheKey() {
        return this.f588b;
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
        C0123G.m282b("GalaDownloader", ">>>>> loadFile: invalid request: " + request);
        if (gifCallback != null) {
            gifCallback.onFailure(request, new C0153y("Params is wrong!"));
        }
    }

    public final void loadGifs(List<FileRequest> requests, IGifCallback gifCallback) {
        if (requests != null && !requests.isEmpty()) {
            for (FileRequest fileRequest : requests) {
                if (fileRequest == null) {
                    break;
                } else if (this.f584a > 0 && fileRequest.getLimitSize() == 0) {
                    fileRequest.setLimitSize(this.f584a);
                }
            }
        }
        this.f586a.m373a((List) requests, gifCallback);
    }

    public final void setGifLimitSize(int fileLimitSize) {
        this.f584a = fileLimitSize;
    }
}
