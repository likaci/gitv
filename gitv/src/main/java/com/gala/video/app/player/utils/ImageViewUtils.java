package com.gala.video.app.player.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.lib.framework.core.utils.BitmapUtils;
import com.gala.video.lib.framework.core.utils.BitmapUtils.ScalingLogic;
import com.gala.video.lib.framework.core.utils.BlurUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import org.xbill.DNS.WKSRecord.Service;

public class ImageViewUtils {
    private static final String TAG = "ImageViewUtils";

    public static void updateImageView(final ImageView view, String url, final Handler uiHandler) {
        if (view == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(TAG, "updateImageView: ImageView is null");
            }
        } else if (TextUtils.isEmpty(url)) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(TAG, "updateImageView: no new logo url, return");
            }
        } else if (uiHandler != null) {
            ImageProviderApi.getImageProvider().loadImage(new ImageRequest(url), new IImageCallback() {
                public void onSuccess(ImageRequest request, final Bitmap bitmap) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ImageViewUtils.TAG, "updateImageView onSuccess");
                    }
                    uiHandler.post(new Runnable() {
                        public void run() {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(ImageViewUtils.TAG, "setImageBitmap" + bitmap);
                            }
                            view.setImageBitmap(bitmap);
                        }
                    });
                }

                public void onFailure(ImageRequest request, Exception exception) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ImageViewUtils.TAG, "updateImageView onFailure", exception);
                    }
                }
            });
        } else if (LogUtils.mIsDebug) {
            LogUtils.w(TAG, "updateImageView null == uiHandler");
        }
    }

    public static void updateAndBlurImageView(final ImageView view, String url, final Handler uiHandler) {
        if (view == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(TAG, "updateImageView: ImageView is null");
            }
        } else if (TextUtils.isEmpty(url)) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(TAG, "updateImageView: no new logo url, return");
            }
        } else if (uiHandler != null) {
            ImageProviderApi.getImageProvider().loadImage(new ImageRequest(url), new IImageCallback() {
                public void onSuccess(ImageRequest request, Bitmap bitmap) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ImageViewUtils.TAG, "updateImageView onSuccess");
                    }
                    Bitmap bitmap2 = BlurUtils.boxBlurFilter(BitmapUtils.createScaledBitmap(bitmap, 200, Service.LOCUS_MAP, ScalingLogic.FIT));
                    final Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap2, 5, 5, bitmap2.getWidth() - 10, bitmap2.getHeight() - 10);
                    uiHandler.post(new Runnable() {
                        public void run() {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(ImageViewUtils.TAG, "setImageBitmap" + mScaleBitmap);
                            }
                            view.setImageBitmap(mScaleBitmap);
                        }
                    });
                }

                public void onFailure(ImageRequest request, Exception exception) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(ImageViewUtils.TAG, "updateImageView onFailure", exception);
                    }
                }
            });
        } else if (LogUtils.mIsDebug) {
            LogUtils.w(TAG, "updateImageView null == uiHandler");
        }
    }
}
