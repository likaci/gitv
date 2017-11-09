package com.gala.imageprovider.p000private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IGifCallback;
import java.io.File;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;

public final class C0150u extends C0148v {
    private IGifCallback f603a;

    public C0150u(FileRequest fileRequest, IGifCallback iGifCallback) {
        super(fileRequest);
        this.f603a = iGifCallback;
    }

    public final void mo670a(String str) {
        GifDrawable gifDrawable;
        try {
            gifDrawable = new GifDrawable(new File(str));
        } catch (IOException e) {
            e.printStackTrace();
            gifDrawable = null;
        }
        this.f603a.onSuccess(m375a(), gifDrawable);
    }

    public final void mo669a(Exception exception) {
        this.f603a.onFailure(m375a(), exception);
    }
}
