package com.gala.video.lib.share.ifimpl.dynamic;

import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

class DynaimicImageDownLoadTask {
    private static final String TAG = "DynaimicImageDownLoadTask";
    private static IDownloader mDownloader = DownloaderAPI.getDownloader();
    private IDynamicDownLoadCallback mCallback;
    private int mDownLoaded = 0;
    private List<FileRequest> mRequests;
    List<String> mResult = new ArrayList();
    private int mTotalCount = 0;

    public interface IDynamicDownLoadCallback {
        void onDownLoadTaskFinished(int i, boolean z, List<String> list);
    }

    public DynaimicImageDownLoadTask(int category, List<FileRequest> requests) {
        this.mRequests = requests;
        if (this.mRequests != null) {
            this.mTotalCount = this.mRequests.size();
        }
    }

    public void setCallBack(IDynamicDownLoadCallback callback) {
        this.mCallback = callback;
    }

    public void download() {
        if (this.mRequests != null) {
            for (FileRequest request : this.mRequests) {
                mDownloader.loadFile(request, new IFileCallback() {
                    public void onSuccess(FileRequest request, String path) {
                        LogUtils.d(DynaimicImageDownLoadTask.TAG, "load image count = " + DynaimicImageDownLoadTask.this.mTotalCount);
                        LogUtils.d(DynaimicImageDownLoadTask.TAG, "load image path = " + path);
                        LogUtils.d(DynaimicImageDownLoadTask.TAG, "load image cookie = " + ((Integer) request.getCookie()));
                        DynaimicImageDownLoadTask.this.mResult.add(path);
                        DynaimicImageDownLoadTask.this.mDownLoaded = DynaimicImageDownLoadTask.this.mDownLoaded + 1;
                        if (DynaimicImageDownLoadTask.this.mDownLoaded == DynaimicImageDownLoadTask.this.mTotalCount) {
                            DynaimicImageDownLoadTask.this.mCallback.onDownLoadTaskFinished(((Integer) request.getCookie()).intValue(), true, DynaimicImageDownLoadTask.this.mResult);
                        }
                    }

                    public void onFailure(FileRequest request, Exception arg1) {
                        DynaimicImageDownLoadTask.this.mCallback.onDownLoadTaskFinished(((Integer) request.getCookie()).intValue(), false, DynaimicImageDownLoadTask.this.mResult);
                    }
                });
            }
        }
    }
}
