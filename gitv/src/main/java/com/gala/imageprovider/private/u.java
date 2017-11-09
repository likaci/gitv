package com.gala.imageprovider.private;

import com.gala.download.base.FileRequest;
import com.gala.download.base.IGifCallback;
import java.io.File;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;

public final class u extends v {
    private IGifCallback a;

    public u(FileRequest fileRequest, IGifCallback iGifCallback) {
        super(fileRequest);
        this.a = iGifCallback;
    }

    public final void a(String str) {
        GifDrawable gifDrawable;
        try {
            gifDrawable = new GifDrawable(new File(str));
        } catch (IOException e) {
            e.printStackTrace();
            gifDrawable = null;
        }
        this.a.onSuccess(a(), gifDrawable);
    }

    public final void a(Exception exception) {
        this.a.onFailure(a(), exception);
    }
}
