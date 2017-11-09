package pl.droidsonroids.gif;

class GifDrawable$3 extends SafeRunnable {
    final /* synthetic */ GifDrawable this$0;
    final /* synthetic */ int val$frameIndex;

    GifDrawable$3(GifDrawable gifDrawable, GifDrawable x0, int i) {
        this.this$0 = gifDrawable;
        this.val$frameIndex = i;
        super(x0);
    }

    public void doWork() {
        this.this$0.mNativeInfoHandle.seekToFrame(this.val$frameIndex, this.this$0.mBuffer);
        this.this$0.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0);
    }
}
