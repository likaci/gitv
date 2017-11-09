package pl.droidsonroids.gif;

import java.io.IOException;
import pl.droidsonroids.gif.annotations.Beta;

@Beta
public class GifTexImage2D {
    private final GifInfoHandle mGifInfoHandle;

    public GifTexImage2D(InputSource inputSource, GifOptions options) throws IOException {
        if (options == null) {
            options = new GifOptions();
        }
        this.mGifInfoHandle = inputSource.open();
        this.mGifInfoHandle.setOptions(options.inSampleSize, options.inIsOpaque);
        this.mGifInfoHandle.initTexImageDescriptor();
    }

    public int getFrameDuration(int index) {
        return this.mGifInfoHandle.getFrameDuration(index);
    }

    public void seekToFrame(int index) {
        this.mGifInfoHandle.seekToFrameGL(index);
    }

    public int getNumberOfFrames() {
        return this.mGifInfoHandle.getNumberOfFrames();
    }

    public void glTexImage2D(int target, int level) {
        this.mGifInfoHandle.glTexImage2D(target, level);
    }

    public void glTexSubImage2D(int target, int level) {
        this.mGifInfoHandle.glTexSubImage2D(target, level);
    }

    public void startDecoderThread() {
        this.mGifInfoHandle.startDecoderThread();
    }

    public void stopDecoderThread() {
        this.mGifInfoHandle.stopDecoderThread();
    }

    public void recycle() {
        if (this.mGifInfoHandle != null) {
            this.mGifInfoHandle.recycle();
        }
    }

    public int getWidth() {
        return this.mGifInfoHandle.getWidth();
    }

    public int getHeight() {
        return this.mGifInfoHandle.getHeight();
    }

    public int getDuration() {
        return this.mGifInfoHandle.getDuration();
    }

    protected final void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }
}
