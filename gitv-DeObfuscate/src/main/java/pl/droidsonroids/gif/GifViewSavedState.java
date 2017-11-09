package pl.droidsonroids.gif;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View.BaseSavedState;

class GifViewSavedState extends BaseSavedState {
    public static final Creator<GifViewSavedState> CREATOR = new C22121();
    final long[][] mStates;

    static class C22121 implements Creator<GifViewSavedState> {
        C22121() {
        }

        public final GifViewSavedState createFromParcel(Parcel in) {
            return new GifViewSavedState(in);
        }

        public final GifViewSavedState[] newArray(int size) {
            return new GifViewSavedState[size];
        }
    }

    GifViewSavedState(Parcelable superState, Drawable... drawables) {
        super(superState);
        this.mStates = new long[drawables.length][];
        for (int i = 0; i < drawables.length; i++) {
            Drawable drawable = drawables[i];
            if (drawable instanceof GifDrawable) {
                this.mStates[i] = ((GifDrawable) drawable).mNativeInfoHandle.getSavedState();
            } else {
                this.mStates[i] = null;
            }
        }
    }

    private GifViewSavedState(Parcel in) {
        super(in);
        this.mStates = new long[in.readInt()][];
        for (int i = 0; i < this.mStates.length; i++) {
            this.mStates[i] = in.createLongArray();
        }
    }

    GifViewSavedState(Parcelable superState, long[] savedState) {
        super(superState);
        this.mStates = new long[1][];
        this.mStates[0] = savedState;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mStates.length);
        for (long[] writeLongArray : this.mStates) {
            dest.writeLongArray(writeLongArray);
        }
    }

    void restoreState(Drawable drawable, int i) {
        if (this.mStates[i] != null && (drawable instanceof GifDrawable)) {
            GifDrawable gifDrawable = (GifDrawable) drawable;
            gifDrawable.startAnimation((long) gifDrawable.mNativeInfoHandle.restoreSavedState(this.mStates[i], gifDrawable.mBuffer));
        }
    }
}
