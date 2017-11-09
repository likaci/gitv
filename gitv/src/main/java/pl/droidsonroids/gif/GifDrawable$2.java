package pl.droidsonroids.gif;

class GifDrawable$2 extends SafeRunnable {
    final /* synthetic */ GifDrawable this$0;
    final /* synthetic */ int val$position;

    GifDrawable$2(GifDrawable gifDrawable, GifDrawable x0, int i) {
        this.this$0 = gifDrawable;
        this.val$position = i;
        super(x0);
    }

    public void doWork() {
        this.this$0.mNativeInfoHandle.seekToTime(this.val$position, this.this$0.mBuffer);
        this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0);
    }
}
