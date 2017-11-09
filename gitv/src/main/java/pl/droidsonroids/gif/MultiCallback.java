package pl.droidsonroids.gif;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiCallback implements Callback {
    private final CopyOnWriteArrayList<CallbackWeakReference> mCallbacks;
    private final boolean mUseViewInvalidate;

    static final class CallbackWeakReference extends WeakReference<Callback> {
        CallbackWeakReference(Callback r) {
            super(r);
        }

        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (get() != ((CallbackWeakReference) o).get()) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            Callback callback = (Callback) get();
            return callback != null ? callback.hashCode() : 0;
        }
    }

    public MultiCallback() {
        this(false);
    }

    public MultiCallback(boolean useViewInvalidate) {
        this.mCallbacks = new CopyOnWriteArrayList();
        this.mUseViewInvalidate = useViewInvalidate;
    }

    public void invalidateDrawable(Drawable who) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference callbackWeakReference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback = (Callback) callbackWeakReference.get();
            if (callback == null) {
                this.mCallbacks.remove(callbackWeakReference);
            } else if (this.mUseViewInvalidate && (callback instanceof View)) {
                ((View) callback).invalidate();
            } else {
                callback.invalidateDrawable(who);
            }
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference callbackWeakReference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback = (Callback) callbackWeakReference.get();
            if (callback != null) {
                callback.scheduleDrawable(who, what, when);
            } else {
                this.mCallbacks.remove(callbackWeakReference);
            }
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference callbackWeakReference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback = (Callback) callbackWeakReference.get();
            if (callback != null) {
                callback.unscheduleDrawable(who, what);
            } else {
                this.mCallbacks.remove(callbackWeakReference);
            }
        }
    }

    public void addView(Callback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference callbackWeakReference = (CallbackWeakReference) this.mCallbacks.get(i);
            if (((Callback) callbackWeakReference.get()) == null) {
                this.mCallbacks.remove(callbackWeakReference);
            }
        }
        this.mCallbacks.addIfAbsent(new CallbackWeakReference(callback));
    }

    public void removeView(Callback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference callbackWeakReference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback2 = (Callback) callbackWeakReference.get();
            if (callback2 == null || callback2 == callback) {
                this.mCallbacks.remove(callbackWeakReference);
            }
        }
    }
}
