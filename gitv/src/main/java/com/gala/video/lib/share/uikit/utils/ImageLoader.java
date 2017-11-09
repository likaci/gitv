package com.gala.video.lib.share.uikit.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IGifCallback;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ImageType;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.utils.TraceEx;
import pl.droidsonroids.gif.GifDrawable;

public class ImageLoader {
    private static Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private IGifCallback mGifCallback = new IGifCallback() {
        public void onSuccess(final FileRequest fileRequest, final GifDrawable gifDrawable) {
            if (ImageLoader.this.mGifListener == null || fileRequest == null) {
                LogUtils.d("mGifCallback", "loadImage onSuccess return mGifListener==" + ImageLoader.this.mGifListener + "," + "fileRequest=" + fileRequest);
            } else if (!TextUtils.equals(ImageLoader.this.mLastRequestUrl, fileRequest.getUrl())) {
                LogUtils.d("mGifCallback", "loadImage onSuccess return mLastRequestUrl=" + ImageLoader.this.mLastRequestUrl + ",url=" + fileRequest.getUrl());
            } else if (ThreadUtils.isUIThread()) {
                ImageLoader.this.mGifListener.onSuccess(gifDrawable);
                ImageLoader.this.mIsRecycled = false;
            } else {
                ImageLoader.mMainThreadHandler.post(new Runnable() {
                    public void run() {
                        AnonymousClass2.this.onSuccess(fileRequest, gifDrawable);
                    }
                });
            }
        }

        public void onFailure(final FileRequest fileRequest, final Exception e) {
            if (ImageLoader.this.mGifListener != null && fileRequest != null && TextUtils.equals(ImageLoader.this.mLastRequestUrl, fileRequest.getUrl())) {
                if (ThreadUtils.isUIThread()) {
                    ImageLoader.this.mGifListener.onSuccess(null);
                } else {
                    ImageLoader.mMainThreadHandler.post(new Runnable() {
                        public void run() {
                            AnonymousClass2.this.onFailure(fileRequest, e);
                        }
                    });
                }
            }
        }
    };
    private IGifLoadCallback mGifListener;
    private boolean mIsRecycled = true;
    private String mLastRequestUrl = "";
    private IImageLoadCallback mListener;
    private IImageCallback mLoadImageCallback = new IImageCallback() {
        public void onSuccess(ImageRequest imageRequest, final Bitmap bitmap) {
            if (ImageLoader.this.mListener != null && imageRequest != null && TextUtils.equals(ImageLoader.this.mLastRequestUrl, imageRequest.getUrl())) {
                if (ThreadUtils.isUIThread()) {
                    ImageLoader.this.mListener.onSuccess(bitmap);
                    ImageLoader.this.mIsRecycled = false;
                    return;
                }
                ImageLoader.mMainThreadHandler.post(new Runnable() {
                    public void run() {
                        TraceEx.beginSection("Imageload.onSuccess");
                        ImageLoader.this.mListener.onSuccess(bitmap);
                        ImageLoader.this.mIsRecycled = false;
                        TraceEx.endSection();
                    }
                });
            }
        }

        public void onFailure(final ImageRequest imageRequest, Exception e) {
            if (ImageLoader.this.mListener != null && imageRequest != null && TextUtils.equals(ImageLoader.this.mLastRequestUrl, imageRequest.getUrl())) {
                if (ThreadUtils.isUIThread()) {
                    ImageLoader.this.mListener.onFailed(imageRequest.getUrl());
                } else {
                    ImageLoader.mMainThreadHandler.post(new Runnable() {
                        public void run() {
                            TraceEx.beginSection("Imageload.onFailure");
                            ImageLoader.this.mListener.onFailed(imageRequest.getUrl());
                            TraceEx.endSection();
                        }
                    });
                }
            }
        }
    };

    public interface IImageLoadCallback {
        void onFailed(String str);

        void onSuccess(Bitmap bitmap);
    }

    public interface IGifLoadCallback {
        void onSuccess(GifDrawable gifDrawable);
    }

    public static class ImageCropModel {
        public ScaleType cropType;
        public int height;
        public int radius;
        public int width;
    }

    public void resetLoader() {
        this.mListener = null;
        this.mGifListener = null;
        mMainThreadHandler.removeCallbacksAndMessages(null);
    }

    public void setImageLoadCallback(IImageLoadCallback listener) {
        this.mListener = listener;
        if (listener instanceof IGifLoadCallback) {
            this.mGifListener = (IGifLoadCallback) listener;
        }
    }

    public void loadImage(String url, ImageCropModel cropModel) {
        if (!TextUtils.isEmpty(url)) {
            if (!StringUtils.equals(this.mLastRequestUrl, url) || this.mIsRecycled) {
                this.mLastRequestUrl = url;
                ImageProviderApi.getImageProvider().loadImage(createImageRequest(url, cropModel), this.mLoadImageCallback);
            }
        }
    }

    public void loadGif(String url, IGifLoadCallback gifLoadCallback) {
        if (!TextUtils.isEmpty(url)) {
            if (!StringUtils.equals(this.mLastRequestUrl, url) || this.mIsRecycled) {
                this.mLastRequestUrl = url;
                this.mGifListener = gifLoadCallback;
                DownloaderAPI.getDownloader().loadGif(new FileRequest(url), this.mGifCallback);
            }
        }
    }

    public void recycle() {
        this.mIsRecycled = true;
    }

    public boolean isRecycled() {
        return this.mIsRecycled;
    }

    private ImageRequest createImageRequest(String url, ImageCropModel cropModel) {
        ImageRequest imageRequest = new ImageRequest(url, Integer.valueOf(hashCode()));
        if (cropModel != null) {
            if (cropModel.cropType != null) {
                imageRequest.setScaleType(cropModel.cropType);
            }
            if (cropModel.width > 0) {
                imageRequest.setTargetWidth(cropModel.width);
            }
            if (cropModel.height > 0) {
                imageRequest.setTargetHeight(cropModel.height);
            }
            if (cropModel.radius != 0) {
                imageRequest.setImageType(ImageType.ROUND);
                imageRequest.setRadius((float) cropModel.radius);
            }
        }
        return imageRequest;
    }
}
