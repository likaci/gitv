package com.gala.video.app.epg.screensaver;

import android.graphics.Bitmap;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants;
import com.gala.video.app.epg.screensaver.imagedownload.ImageDownload;
import com.gala.video.app.epg.screensaver.utils.BitmapUtil;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScreenSaverImageProvider {
    private static final ScreenSaverImageProvider INSTANCE = new ScreenSaverImageProvider();
    private static final String TAG = "ScreenSaverImageProvider";
    private volatile int mCount = 0;
    private List<ScreenSaverModel> mLocalScreenSaverModelList = new CopyOnWriteArrayList();
    private String mParentPath = "";
    private List<ChannelLabel> mRawLabelList = null;

    private ScreenSaverImageProvider() {
        File file = AppRuntimeEnv.get().getApplicationContext().getFilesDir();
        this.mParentPath = file != null ? file.getPath() + "/" : "/";
        LogUtils.d(TAG, "local parent screensaver image path : " + this.mParentPath);
    }

    public static ScreenSaverImageProvider getInstance() {
        return INSTANCE;
    }

    private String initDownloadFile() {
        String path = this.mParentPath + ScreenSaverConstants.IMAGE_LOAD_PATH;
        File imgDir = new File(path);
        if (imgDir.exists()) {
            return path;
        }
        if (imgDir.mkdir()) {
            LogUtils.w(TAG, "initDownloadFile, create image directory success, path = " + path);
            return path;
        }
        LogUtils.w(TAG, "initDownloadFile, create image directory failed!!!, path = " + path);
        return "";
    }

    public void prepare() {
        if (ListUtils.isEmpty(this.mLocalScreenSaverModelList)) {
            this.mLocalScreenSaverModelList = readLocalScreenSaverModelList();
            if (!ListUtils.isEmpty(this.mLocalScreenSaverModelList)) {
                for (ScreenSaverModel model : this.mLocalScreenSaverModelList) {
                    if (!new File(model.getImagePath()).exists()) {
                        this.mLocalScreenSaverModelList.remove(model);
                        LogUtils.d(TAG, "prepare, file not exists , path: " + model.getImagePath() + " ,remove this screensaver data");
                    }
                }
            }
        }
    }

    public void download() {
        LogUtils.d(TAG, "download, download new screen saver data");
        download(this.mRawLabelList);
    }

    public void download(List<ChannelLabel> channelLabelList) {
        if (ListUtils.isEmpty((List) channelLabelList)) {
            LogUtils.d(TAG, "download, screen saver label is empty");
            return;
        }
        LogUtils.d(TAG, "download, all raw screen saver label size :" + ListUtils.getCount((List) channelLabelList));
        final List<ScreenSaverModel> tempScreenSaverModels = new CopyOnWriteArrayList();
        final ImageDownload imageDownloader = new ImageDownload();
        final CharSequence downloadPath = initDownloadFile();
        if (StringUtils.isEmpty(downloadPath)) {
            LogUtils.w(TAG, "download, current download path empty, do not download images");
            return;
        }
        resetCount();
        final List<ChannelLabel> list = channelLabelList;
        new Thread8K(new Runnable() {
            public void run() {
                for (ChannelLabel label : list) {
                    CharSequence imageUrl = label.itemImageUrl;
                    String imageName = StringUtils.base64(imageUrl);
                    String imagePath = downloadPath + imageName;
                    File file = new File(imagePath);
                    if (StringUtils.isEmpty(imageUrl)) {
                        LogUtils.d(ScreenSaverImageProvider.TAG, "current image url is empty, do not need to download");
                    } else if (file.exists()) {
                        LogUtils.d(ScreenSaverImageProvider.TAG, "current image has been downloaded, imageUrl :" + imageUrl);
                        model = new ScreenSaverModel();
                        model.setChannelLabel(label);
                        model.setImagePath(imagePath);
                        model.setImageName(imageName);
                        tempScreenSaverModels.add(model);
                    } else if (imageDownloader.downloadImage(imageUrl, imagePath)) {
                        model = new ScreenSaverModel();
                        model.setChannelLabel(label);
                        model.setImagePath(imagePath);
                        model.setImageName(imageName);
                        tempScreenSaverModels.add(model);
                    } else {
                        LogUtils.d(ScreenSaverImageProvider.TAG, "download, failed, url : " + imageUrl);
                    }
                }
                ScreenSaverImageProvider.this.downloadFinished(tempScreenSaverModels);
            }
        }, TAG).start();
    }

    private void downloadFinished(List<ScreenSaverModel> tempScreenSaverModels) {
        LogUtils.d(TAG, "downloadFinished, screen saver data size : " + ListUtils.getCount((List) tempScreenSaverModels));
        deleteOldImage(readLocalScreenSaverModelList(), tempScreenSaverModels);
        writeScreenSaverModelListToCache(tempScreenSaverModels);
    }

    public void onEmptyScreenSaverData() {
        LogUtils.d(TAG, "onEmptyScreenSaverData");
        List<ScreenSaverModel> emptyScreenSaverModels = new ArrayList();
        this.mLocalScreenSaverModelList.clear();
        downloadFinished(emptyScreenSaverModels);
    }

    private void deleteOldImage(List<ScreenSaverModel> oldScreenSaverModels, List<ScreenSaverModel> newScreenSaverModels) {
        if (ListUtils.isEmpty((List) oldScreenSaverModels)) {
            LogUtils.d(TAG, "deleteOldImage, old screensaver images are empty, do not need to delete images");
            return;
        }
        if (newScreenSaverModels == null) {
            newScreenSaverModels = new ArrayList();
        }
        List<String> oldImagePathList = new ArrayList();
        List<String> newImagePathList = new ArrayList();
        for (ScreenSaverModel oldModel : oldScreenSaverModels) {
            oldImagePathList.add(oldModel.getImagePath());
        }
        for (ScreenSaverModel newModel : newScreenSaverModels) {
            newImagePathList.add(newModel.getImagePath());
        }
        try {
            for (String oldImagePath : oldImagePathList) {
                if (!newImagePathList.contains(oldImagePath)) {
                    File file = new File(oldImagePath);
                    if (file.exists()) {
                        file.delete();
                        if (!file.exists()) {
                            LogUtils.d(TAG, "deleteOldImage, delete old image success, path :" + oldImagePath);
                        }
                    } else {
                        LogUtils.w(TAG, "deleteOldImage, try to delete a image, but the image file is not exist, path :" + oldImagePath);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "deleteOldImage, exception :", e);
        }
    }

    private void resetCount() {
        this.mCount = 0;
    }

    public int getLocalDataSize() {
        prepare();
        return ListUtils.getCount(this.mLocalScreenSaverModelList);
    }

    public Bitmap getBitmap(int rawPos) {
        int size = getLocalDataSize();
        if (size == 0) {
            return null;
        }
        return new BitmapUtil().getBitmapFromPath(((ScreenSaverModel) this.mLocalScreenSaverModelList.get(rawPos % size)).getImagePath());
    }

    public ScreenSaverModel getScreenSaverModel(int rawPos) {
        int size = getLocalDataSize();
        if (size == 0) {
            return null;
        }
        return (ScreenSaverModel) this.mLocalScreenSaverModelList.get(rawPos % size);
    }

    private synchronized void writeScreenSaverModelListToCache(List<ScreenSaverModel> screenSaverModels) {
        try {
            SerializableUtils.write(screenSaverModels, HomeDataConfig.HOME_SCREEN_SAVER_DIR);
            LogUtils.d(TAG, "write ScreenSaver data to disk success ");
            setLocalData(screenSaverModels);
            LogUtils.d(TAG, "write ScreenSaver data to memory success");
        } catch (IOException e) {
            LogUtils.w(TAG, "writeScreenSaverModelListToCache, write screen saver data failed", e);
        }
    }

    private synchronized List<ScreenSaverModel> readLocalScreenSaverModelList() {
        List labelList;
        labelList = null;
        try {
            labelList = (List) SerializableUtils.read(HomeDataConfig.HOME_SCREEN_SAVER_DIR);
        } catch (Exception e) {
            LogUtils.e(TAG, "readLocalScreenSaverModelList, read screen saver data failed", e);
        }
        LogUtils.d(TAG, "readLocalScreenSaverModelList, size = " + (!ListUtils.isEmpty(labelList) ? labelList.size() : 0));
        return labelList;
    }

    public void setData(List<ChannelLabel> channelLabels) {
        this.mRawLabelList = channelLabels;
    }

    private void setLocalData(List<ScreenSaverModel> screenSaverModels) {
        this.mLocalScreenSaverModelList = screenSaverModels;
    }
}
