package pl.droidsonroids.gif;

class GifDrawable$1 extends SafeRunnable {
    final /* synthetic */ GifDrawable this$0;

    GifDrawable$1(GifDrawable gifDrawable, GifDrawable x0) {
        this.this$0 = gifDrawable;
        super(x0);
    }

    public void doWork() {
        if (this.this$0.mNativeInfoHandle.reset()) {
            this.this$0.start();
        }
    }
}
