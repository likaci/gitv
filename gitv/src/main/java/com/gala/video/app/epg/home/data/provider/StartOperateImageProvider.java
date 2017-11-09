package com.gala.video.app.epg.home.data.provider;

import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.startup.StartOperateImageModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartOperateImageProvider {
    private static final String INDEX_TEST = "index_test";
    private static final StartOperateImageProvider INSTANCE = new StartOperateImageProvider();
    private static final String TAG = "StartOperateImageProvider";
    private static int mIndexTest = 0;
    private volatile int mCount = 0;
    private List<StartOperateImageModel> mLocalStartOperateImageModelList = new ArrayList();
    private List<ChannelLabel> mRawLabelList = null;

    private StartOperateImageProvider() {
    }

    public static StartOperateImageProvider getInstance() {
        return INSTANCE;
    }

    public void readStartOperateData() {
        if (ListUtils.isEmpty(this.mLocalStartOperateImageModelList)) {
            this.mLocalStartOperateImageModelList = getLocalStartOperateModelList();
        }
    }

    public void download() {
        LogUtils.d(TAG, "download new start operate data");
        download(this.mRawLabelList);
    }

    public void download(List<ChannelLabel> channelLabels) {
        LogUtils.d(TAG, "download, start...");
        List<FileRequest> requestList = new ArrayList();
        if (ListUtils.isEmpty((List) channelLabels)) {
            LogUtils.d(TAG, "download, start operate label is empty");
            return;
        }
        for (ChannelLabel channelLabel : channelLabels) {
            requestList.add(new FileRequest(channelLabel.itemImageUrl, channelLabel));
        }
        final int labelSize = channelLabels.size();
        LogUtils.d(TAG, "download, all raw start operate label size :" + labelSize);
        final List<StartOperateImageModel> tempStartOperateImageModels = new ArrayList();
        resetCount();
        DownloaderAPI.getDownloader().loadFiles(requestList, new IFileCallback() {
            public void onSuccess(FileRequest fileRequest, String s) {
                StartOperateImageModel startOperateImageModel = new StartOperateImageModel();
                startOperateImageModel.setChannelLabel((ChannelLabel) fileRequest.getCookie());
                startOperateImageModel.setImagePath(s);
                tempStartOperateImageModels.add(startOperateImageModel);
                StartOperateImageProvider.this.mCount = StartOperateImageProvider.this.mCount + 1;
                if (StartOperateImageProvider.this.mCount == labelSize) {
                    StartOperateImageProvider.this.downloadFinished(tempStartOperateImageModels);
                }
            }

            public void onFailure(FileRequest fileRequest, Exception e) {
                StartOperateImageProvider.this.mCount = StartOperateImageProvider.this.mCount + 1;
                if (StartOperateImageProvider.this.mCount == labelSize) {
                    StartOperateImageProvider.this.downloadFinished(tempStartOperateImageModels);
                }
                LogUtils.d(StartOperateImageProvider.TAG, "download, IFileCallback---OnFailure,url ," + (fileRequest != null ? fileRequest.getUrl() : "FileRequest is null"));
            }
        });
    }

    private void downloadFinished(List<StartOperateImageModel> tempStartOperateImageModels) {
        LogUtils.d(TAG, "downloadFinished, start operate data size : " + ListUtils.getCount((List) tempStartOperateImageModels));
        deleteOldImage(this.mLocalStartOperateImageModelList, tempStartOperateImageModels);
        writeStartOperateImageModelListToCache(tempStartOperateImageModels);
    }

    public void onEmptyStartOperateData() {
        LogUtils.d(TAG, "onEmptyStartOperateData");
        List<StartOperateImageModel> emptyStartOperateImageModels = new ArrayList();
        this.mLocalStartOperateImageModelList.clear();
        downloadFinished(emptyStartOperateImageModels);
    }

    private void deleteOldImage(List<StartOperateImageModel> list, List<StartOperateImageModel> list2) {
    }

    private void resetCount() {
        this.mCount = 0;
    }

    public int getLocalDataSize() {
        readStartOperateData();
        return ListUtils.getCount(this.mLocalStartOperateImageModelList);
    }

    public StartOperateImageModel getStartOperateImageModel() {
        int size = getLocalDataSize();
        if (size == 0) {
            return null;
        }
        int index = new Random().nextInt(size);
        LogUtils.d(TAG, "getStartOperateImageModel, index = " + index);
        return (StartOperateImageModel) this.mLocalStartOperateImageModelList.get(index);
    }

    private synchronized int getLocalTestIndex() {
        int index;
        index = 0;
        try {
            index = ((Integer) SerializableUtils.read(INDEX_TEST)).intValue();
        } catch (Exception e) {
            LogUtils.w(TAG, "getLocalTestIndex, read test index failed", e);
        }
        return index;
    }

    private synchronized void writeTestIndexToLocal() {
        try {
            SerializableUtils.write(Integer.valueOf(mIndexTest), INDEX_TEST);
        } catch (IOException e) {
            LogUtils.w(TAG, "writeTestIndexToLocal, write test index failed", e);
        }
    }

    private synchronized void writeStartOperateImageModelListToCache(List<StartOperateImageModel> StartOperateImageModels) {
        LogUtils.d(TAG, "writeStartOperateImageModelListToCache...");
        try {
            SerializableUtils.write(StartOperateImageModels, HomeDataConfig.HOME_START_OPERATE_DIR);
            setLocalData(StartOperateImageModels);
        } catch (IOException e) {
            LogUtils.w(TAG, "writeStartOperateImageModelListToCache, write start operate data failed", e);
        }
    }

    private synchronized List<StartOperateImageModel> getLocalStartOperateModelList() {
        List labelList;
        labelList = null;
        try {
            labelList = (List) SerializableUtils.read(HomeDataConfig.HOME_START_OPERATE_DIR);
        } catch (Exception e) {
            LogUtils.e(TAG, "getLocalStartOperateModelList, read start operate data failed", e);
        }
        LogUtils.d(TAG, "getLocalStartOperateModelList, size = " + (!ListUtils.isEmpty(labelList) ? labelList.size() : 0));
        return labelList;
    }

    public void setData(List<ChannelLabel> channelLabels) {
        this.mRawLabelList = channelLabels;
    }

    private void setLocalData(List<StartOperateImageModel> StartOperateImageModels) {
        this.mLocalStartOperateImageModelList = StartOperateImageModels;
    }
}
