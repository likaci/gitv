package com.gala.video.app.epg.home.presenter.storage;

import android.content.Context;
import android.widget.ImageView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.storage.ExternalStorageWatcher;
import com.gala.video.lib.framework.core.storage.ExternalStorageWatcher.ExternalStorageWatcherCallback;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;

public class StorageStatePresenter {
    private static String TAG = "StorageStatePresenter";
    private static final int TAG_EXIST_SDCARD = 1;
    private static final int TAG_EXIST_USB = 2;
    private static final int TAG_EXIST_USB_SDCARD = 3;
    private static final int TAG_NONE = 0;
    private Context mContext;
    private StorageStateUIListener mUIListener = null;

    class C06811 implements ExternalStorageWatcherCallback {
        C06811() {
        }

        public void onUsbDisconnected() {
            StorageStatePresenter.this.mUIListener.onUsbDisconnected();
        }

        public void onUsbConnected() {
            StorageStatePresenter.this.mUIListener.onUsbConnected();
        }

        public void onSdcardDisconnected() {
            onUsbDisconnected();
        }

        public void onSdcardConnected() {
            onUsbConnected();
        }
    }

    class StorageStateJavaUI implements StorageStateUIListener {
        private ImageView mUsbStorageImage;

        public StorageStateJavaUI(ImageView image) {
            this.mUsbStorageImage = image;
        }

        public void showStorageIndicators() {
            switch (StorageStatePresenter.getExternalStorageTag()) {
                case 2:
                case 3:
                    this.mUsbStorageImage.setVisibility(0);
                    return;
                default:
                    return;
            }
        }

        public void onUsbDisconnected() {
            this.mUsbStorageImage.setVisibility(8);
        }

        public void onUsbConnected() {
            this.mUsbStorageImage.setImageResource(C0508R.drawable.epg_main_page_image_usb);
            this.mUsbStorageImage.setVisibility(0);
        }
    }

    public StorageStatePresenter(Context context, ImageView image) {
        this.mContext = context;
        this.mUIListener = new StorageStateJavaUI(image);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            this.mUIListener.showStorageIndicators();
            initExternalStorageWatcherCallback();
        }
    }

    private void initExternalStorageWatcherCallback() {
        ExternalStorageWatcher.setup(this.mContext, new C06811());
    }

    public static int getExternalStorageTag() {
        boolean hasUsb = Project.getInstance().getConfig().isUsbDeviceAvailable();
        boolean hasSdCard = DeviceUtils.isSdCardAvailable();
        if (hasSdCard && hasUsb) {
            LogUtils.m1568d(TAG, " getExternalStorageTag() ---  TAG_EXIST_USB_SDCARD");
            return 3;
        } else if (hasSdCard) {
            LogUtils.m1568d(TAG, " getExternalStorageTag() ---  TAG_EXIST_SDCARD");
            return 1;
        } else if (hasUsb) {
            LogUtils.m1568d(TAG, " getExternalStorageTag() ---  TAG_EXIST_USB");
            return 2;
        } else {
            LogUtils.m1568d(TAG, " getExternalStorageTag() ---  TAG_NONE");
            return 0;
        }
    }
}
