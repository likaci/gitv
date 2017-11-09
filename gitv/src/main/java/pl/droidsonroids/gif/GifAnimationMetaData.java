package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Locale;
import org.xbill.DNS.Message;
import pl.droidsonroids.gif.annotations.Beta;

public class GifAnimationMetaData implements Parcelable, Serializable {
    public static final Creator<GifAnimationMetaData> CREATOR = new Creator<GifAnimationMetaData>() {
        public final GifAnimationMetaData createFromParcel(Parcel source) {
            return new GifAnimationMetaData(source);
        }

        public final GifAnimationMetaData[] newArray(int size) {
            return new GifAnimationMetaData[size];
        }
    };
    private static final long serialVersionUID = 5692363926580237325L;
    private final int mDuration;
    private final int mHeight;
    private final int mImageCount;
    private final int mLoopCount;
    private final long mMetadataBytesCount;
    private final long mPixelsBytesCount;
    private final int mWidth;

    public GifAnimationMetaData(Resources res, int id) throws NotFoundException, IOException {
        this(res.openRawResourceFd(id));
    }

    public GifAnimationMetaData(AssetManager assets, String assetName) throws IOException {
        this(assets.openFd(assetName));
    }

    public GifAnimationMetaData(String filePath) throws IOException {
        this(new GifInfoHandle(filePath));
    }

    public GifAnimationMetaData(File file) throws IOException {
        this(file.getPath());
    }

    public GifAnimationMetaData(InputStream stream) throws IOException {
        this(new GifInfoHandle(stream));
    }

    public GifAnimationMetaData(AssetFileDescriptor afd) throws IOException {
        this(new GifInfoHandle(afd));
    }

    public GifAnimationMetaData(FileDescriptor fd) throws IOException {
        this(new GifInfoHandle(fd));
    }

    public GifAnimationMetaData(byte[] bytes) throws IOException {
        this(new GifInfoHandle(bytes));
    }

    public GifAnimationMetaData(ByteBuffer buffer) throws IOException {
        this(new GifInfoHandle(buffer));
    }

    public GifAnimationMetaData(ContentResolver resolver, Uri uri) throws IOException {
        this(GifInfoHandle.openUri(resolver, uri));
    }

    private GifAnimationMetaData(GifInfoHandle gifInfoHandle) {
        this.mLoopCount = gifInfoHandle.getLoopCount();
        this.mDuration = gifInfoHandle.getDuration();
        this.mWidth = gifInfoHandle.getWidth();
        this.mHeight = gifInfoHandle.getHeight();
        this.mImageCount = gifInfoHandle.getNumberOfFrames();
        this.mMetadataBytesCount = gifInfoHandle.getMetadataByteCount();
        this.mPixelsBytesCount = gifInfoHandle.getAllocationByteCount();
        gifInfoHandle.recycle();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getNumberOfFrames() {
        return this.mImageCount;
    }

    public int getLoopCount() {
        return this.mLoopCount;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public boolean isAnimated() {
        return this.mImageCount > 1 && this.mDuration > 0;
    }

    public long getAllocationByteCount() {
        return this.mPixelsBytesCount;
    }

    @Beta
    @SuppressLint({"NewApi"})
    public long getDrawableAllocationByteCount(GifDrawable oldDrawable, int sampleSize) {
        if (sampleSize <= 0 || sampleSize > Message.MAXLENGTH) {
            throw new IllegalStateException("Sample size " + sampleSize + " out of range <1, ￿>");
        }
        long j;
        int i = sampleSize * sampleSize;
        if (oldDrawable == null || oldDrawable.mBuffer.isRecycled()) {
            j = (long) (((this.mWidth * this.mHeight) << 2) / i);
        } else if (VERSION.SDK_INT >= 19) {
            j = (long) oldDrawable.mBuffer.getAllocationByteCount();
        } else {
            j = (long) oldDrawable.getFrameByteCount();
        }
        return j + (this.mPixelsBytesCount / ((long) i));
    }

    public long getMetadataAllocationByteCount() {
        return this.mMetadataBytesCount;
    }

    public String toString() {
        String num = this.mLoopCount == 0 ? "Infinity" : Integer.toString(this.mLoopCount);
        num = String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, loops: %s, duration: %d", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), Integer.valueOf(this.mImageCount), num, Integer.valueOf(this.mDuration)});
        return isAnimated() ? "Animated " + num : num;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.mLoopCount);
        dest.writeInt(this.mDuration);
        dest.writeInt(this.mHeight);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mImageCount);
        dest.writeLong(this.mMetadataBytesCount);
        dest.writeLong(this.mPixelsBytesCount);
    }

    private GifAnimationMetaData(Parcel in) {
        this.mLoopCount = in.readInt();
        this.mDuration = in.readInt();
        this.mHeight = in.readInt();
        this.mWidth = in.readInt();
        this.mImageCount = in.readInt();
        this.mMetadataBytesCount = in.readLong();
        this.mPixelsBytesCount = in.readLong();
    }
}
