package com.gala.video.app.epg.home.data.provider;

import android.graphics.Bitmap;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.home.data.model.ExitOperateImageModel;
import com.gala.video.app.epg.screensaver.utils.BitmapUtil;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExitOperateImageProvider {
    private static final ExitOperateImageProvider INSTANCE = new ExitOperateImageProvider();
    private static final String TAG = "ExitOperateImageProvider";
    private static int mIndexTest = 0;
    private volatile int mCount = 0;
    private List<ExitOperateImageModel> mLocalExitOperateImageModelList = new ArrayList();
    private List<ChannelLabel> mRawLabelList = null;

    private ExitOperateImageProvider() {
    }

    public static ExitOperateImageProvider getInstance() {
        return INSTANCE;
    }

    public void readExitOperateData() {
        if (ListUtils.isEmpty(this.mLocalExitOperateImageModelList)) {
            this.mLocalExitOperateImageModelList = getLocalExitOperateModelList();
        }
    }

    public void download() {
        LogUtils.d(TAG, "download new exit operate data");
        download(this.mRawLabelList);
    }

    public void download(List<ChannelLabel> channelLabels) {
        LogUtils.d(TAG, "download, start...");
        List<FileRequest> requestList = new ArrayList();
        if (ListUtils.isEmpty((List) channelLabels)) {
            LogUtils.d(TAG, "download, exit operate label is empty");
            return;
        }
        for (ChannelLabel channelLabel : channelLabels) {
            requestList.add(new FileRequest(channelLabel.itemImageUrl, channelLabel));
        }
        final int labelSize = channelLabels.size();
        LogUtils.d(TAG, "download, all raw exit operate label size :" + labelSize);
        final List<ExitOperateImageModel> tempExitOperateImageModels = new ArrayList();
        resetCount();
        DownloaderAPI.getDownloader().loadFiles(requestList, new IFileCallback() {
            public void onSuccess(FileRequest fileRequest, String s) {
                ExitOperateImageModel ExitOperateImageModel = new ExitOperateImageModel();
                ExitOperateImageModel.setChannelLabel((ChannelLabel) fileRequest.getCookie());
                ExitOperateImageModel.setImagePath(s);
                tempExitOperateImageModels.add(ExitOperateImageModel);
                ExitOperateImageProvider.this.mCount = ExitOperateImageProvider.this.mCount + 1;
                if (ExitOperateImageProvider.this.mCount == labelSize) {
                    ExitOperateImageProvider.this.downloadFinished(tempExitOperateImageModels);
                }
            }

            public void onFailure(FileRequest fileRequest, Exception e) {
                ExitOperateImageProvider.this.mCount = ExitOperateImageProvider.this.mCount + 1;
                if (ExitOperateImageProvider.this.mCount == labelSize) {
                    ExitOperateImageProvider.this.downloadFinished(tempExitOperateImageModels);
                }
                LogUtils.d(ExitOperateImageProvider.TAG, "download, IFileCallback---OnFailure,url ," + (fileRequest != null ? fileRequest.getUrl() : "FileRequest is null"));
            }
        });
    }

    private void downloadFinished(List<ExitOperateImageModel> tempExitOperateImageModels) {
        LogUtils.d(TAG, "downloadFinished, exit operate data size : " + ListUtils.getCount((List) tempExitOperateImageModels));
        deleteOldImage(this.mLocalExitOperateImageModelList, tempExitOperateImageModels);
        writeExitOperateImageModelListToCache(tempExitOperateImageModels);
    }

    public void onEmptyExitOperateData() {
        LogUtils.d(TAG, "onEmptyExitOperateData");
        List<ExitOperateImageModel> emptyExitOperateImageModels = new ArrayList();
        this.mLocalExitOperateImageModelList.clear();
        downloadFinished(emptyExitOperateImageModels);
    }

    private void deleteOldImage(List<ExitOperateImageModel> list, List<ExitOperateImageModel> list2) {
    }

    private void resetCount() {
        this.mCount = 0;
    }

    public int getLocalDataSize() {
        readExitOperateData();
        return ListUtils.getCount(this.mLocalExitOperateImageModelList);
    }

    public Bitmap getBitmap(int rawPos) {
        int size = getLocalDataSize();
        if (size == 0) {
            return null;
        }
        return new BitmapUtil().getBitmapFromPath(((ExitOperateImageModel) this.mLocalExitOperateImageModelList.get(rawPos % size)).getImagePath());
    }

    public ExitOperateImageModel getExitOperateImageModel() {
        int size = getLocalDataSize();
        if (size == 0) {
            return null;
        }
        int index = new Random().nextInt(size);
        LogUtils.d(TAG, "getExitOperateImageModel  , index = " + index);
        return (ExitOperateImageModel) this.mLocalExitOperateImageModelList.get(index);
    }

    private synchronized void writeExitOperateImageModelListToCache(List<ExitOperateImageModel> ExitOperateImageModels) {
        LogUtils.d(TAG, "writeExitOperateImageModelListToCache...");
        try {
            SerializableUtils.write(ExitOperateImageModels, HomeDataConfig.HOME_EXIT_OPERATE_DIR);
            setLocalData(ExitOperateImageModels);
        } catch (IOException e) {
            LogUtils.w(TAG, "writeExitOperateImageModelListToCache, write exit operate data failed", e);
        }
    }

    private synchronized List<ExitOperateImageModel> getLocalExitOperateModelList() {
        List labelList;
        labelList = null;
        try {
            labelList = (List) SerializableUtils.read(HomeDataConfig.HOME_EXIT_OPERATE_DIR);
        } catch (Exception e) {
            LogUtils.e(TAG, "getLocalExitOperateModelList, read exit operate data failed", e);
        }
        LogUtils.d(TAG, "getLocalExitOperateModelList, size = " + (!ListUtils.isEmpty(labelList) ? labelList.size() : 0));
        return labelList;
    }

    public void setData(List<ChannelLabel> channelLabels) {
        this.mRawLabelList = channelLabels;
    }

    private void setLocalData(List<ExitOperateImageModel> ExitOperateImageModels) {
        this.mLocalExitOperateImageModelList = ExitOperateImageModels;
    }
}
