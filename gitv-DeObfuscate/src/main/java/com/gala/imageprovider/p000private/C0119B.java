package com.gala.imageprovider.p000private;

import android.os.Handler;
import android.os.Looper;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IGifCallback;
import java.io.File;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;

public final class C0119B extends C0114C {
    private Handler f531a = new Handler(Looper.getMainLooper());
    private IGifCallback f532a;

    public C0119B(FileRequest fileRequest, IGifCallback iGifCallback) {
        super(fileRequest);
        this.f532a = iGifCallback;
    }

    public final void mo632a(String str) {
        try {
            final GifDrawable gifDrawable = new GifDrawable(new File(str));
            this.f519a.getSameTaskQueue().m385a(str);
            this.f531a.post(new Runnable(this) {
                private /* synthetic */ C0119B f525a;

                public final void run() {
                    this.f525a.f532a.onSuccess(this.f525a.m266a(), gifDrawable);
                }
            });
        } catch (final IOException e) {
            C0123G.m282b("ImageProvider/GifHttpTask", ">>>>> callback-onSuccess,bug handle gif error");
            this.f531a.post(new Runnable(this) {
                private /* synthetic */ C0119B f527a;

                public final void run() {
                    this.f527a.f532a.onFailure(this.f527a.m266a(), e);
                }
            });
        }
    }

    public final void mo631a(final Exception exception) {
        this.f531a.post(new Runnable(this) {
            private /* synthetic */ C0119B f529a;

            public final void run() {
                this.f529a.f532a.onFailure(this.f529a.m266a(), exception);
            }
        });
        this.f519a.getSameTaskQueue().m384a(exception);
    }
}
