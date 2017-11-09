package com.gala.video.lib.share.uikit.data.data.loader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardMap;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemMap;
import com.gala.video.lib.share.uikit.data.flatbuffers.CardListBuilder;
import com.gala.video.lib.share.uikit.data.flatbuffers.ItemStyleBuilder;
import com.google.flatbuffers.FlatBufferBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class LayoutLoader {
    private static final String NAME_CARD = "cardLayout7_10.bin";
    private static final String NAME_ITEM = "itemStyle7_10.bin";
    private static final String PATH_CARD = "home/home_cache/cardLayout7_10.bin";
    private static final String PATH_ITEM = "home/home_cache/itemStyle7_10.bin";
    private static final long REFRESH_TIME = 86400000;
    private static final String TAG = "flatbuffers/Loader";
    private static final String URL_CARD = "http://static.ptqy.gitv.tv/tv/app/uikit/cardlayout_v7.10.txt";
    private static final String URL_ITEM = "http://static.ptqy.gitv.tv/tv/app/uikit/itemstyle_v7.10.txt";
    public Context mContext = AppRuntimeEnv.get().getApplicationContext();
    private IDownloader mDownloader = DownloaderAPI.getDownloader();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public CardMap loadCard() {
        boolean readSuccess = false;
        CardMap mCardMap = null;
        try {
            File cardFile = new File(this.mContext.getFilesDir() + "/" + PATH_CARD);
            if (cardFile.exists()) {
                mCardMap = buildCardLayout(getBuffer(cardFile));
                readSuccess = true;
                Log.d(TAG, "read cardLayout file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!readSuccess) {
            try {
                Log.d(TAG, "read def cardLayout");
                mCardMap = loadDefCardLayout();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        callCardLayout();
        return mCardMap;
    }

    public ItemMap loadItem() {
        ItemMap mItemMap = null;
        boolean readSuccess = false;
        try {
            File cardFile = new File(this.mContext.getFilesDir() + "/" + PATH_ITEM);
            if (cardFile.exists()) {
                mItemMap = buildItemStyle(getBuffer(cardFile));
                readSuccess = true;
                Log.d(TAG, "read itemStyle file");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!readSuccess) {
            try {
                Log.d(TAG, "read def itemStyle");
                mItemMap = loadDefItemStyle();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        callItemStyle();
        return mItemMap;
    }

    private CardMap buildCardLayout(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.length);
        byteBuffer.put(buffer);
        new FlatBufferBuilder().init(byteBuffer);
        return new CardListBuilder().buildCardList(byteBuffer);
    }

    private CardMap loadDefCardLayout() {
        return buildCardLayout(getBuffer(R.raw.cardlayout_v1));
    }

    private ItemMap loadDefItemStyle() {
        return buildItemStyle(getBuffer(R.raw.itemstyle_v1));
    }

    private ItemMap buildItemStyle(byte[] buffer) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(buffer.length);
        byteBuffer.put(buffer);
        new FlatBufferBuilder().init(byteBuffer);
        return new ItemStyleBuilder().buildTempletList(byteBuffer);
    }

    private void callCardLayout() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                FileRequest imageRequest = new FileRequest(LayoutLoader.URL_CARD);
                imageRequest.setSavePath(LayoutLoader.this.mContext.getFilesDir() + "/" + "home/home_cache/");
                LayoutLoader.this.mDownloader.loadFile(imageRequest, new IFileCallback() {
                    public void onSuccess(FileRequest fileRequest, String path) {
                        LayoutLoader.this.onSuccessFile(path, LayoutLoader.this.mContext.getFilesDir() + "/" + LayoutLoader.PATH_CARD);
                    }

                    public void onFailure(FileRequest fileRequest, Exception e) {
                    }
                });
                LayoutLoader.this.mHandler.postDelayed(this, 86400000);
            }
        });
    }

    private void callItemStyle() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                FileRequest imageRequest = new FileRequest(LayoutLoader.URL_ITEM);
                imageRequest.setSavePath(LayoutLoader.this.mContext.getFilesDir() + "/" + "home/home_cache/");
                LayoutLoader.this.mDownloader.loadFile(imageRequest, new IFileCallback() {
                    public void onSuccess(FileRequest fileRequest, String path) {
                        LayoutLoader.this.onSuccessFile(path, LayoutLoader.this.mContext.getFilesDir() + "/" + LayoutLoader.PATH_ITEM);
                    }

                    public void onFailure(FileRequest fileRequest, Exception e) {
                    }
                });
                LayoutLoader.this.mHandler.postDelayed(this, 86400000);
            }
        });
    }

    private void onSuccessFile(String path, String name) {
        try {
            LogUtils.d(TAG, "onSuccessFile() -> local path: ", path);
            if (StringUtils.isEmpty((CharSequence) path)) {
                LogUtils.e(TAG, "onSuccessFile() -> path is null");
            } else if (!isBinFormat(path)) {
                LogUtils.e(TAG, "onSuccessFile() ->  path is no bin format，use default bin");
            } else if (renameFile(path, name)) {
                LogUtils.e(TAG, "remane file success!");
            } else {
                LogUtils.e(TAG, "rename fails");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isBinFormat(String path) {
        int beginIndex = path.lastIndexOf(".");
        if (beginIndex < 0 || beginIndex > path.length()) {
            return false;
        }
        return path.substring(beginIndex, path.length()).endsWith(CuteConstants.TYPE_TXT);
    }

    private boolean renameFile(String oldPath, String newPath) {
        if (oldPath.equals(newPath)) {
            LogUtils.e(TAG, "new file name is equals old name");
            return false;
        }
        File oldfile = new File(oldPath);
        if (oldfile.exists()) {
            File newfile = new File(newPath);
            if (newfile.exists()) {
                LogUtils.e(TAG, "newfile :" + newPath + " exists！");
                if (newfile.delete()) {
                    LogUtils.e(TAG, "newfile :" + newPath + " deleted！");
                }
            }
            if (!oldfile.renameTo(newfile)) {
                return false;
            }
            LogUtils.e(TAG, "newfile :" + newPath + " rename！");
            return true;
        }
        LogUtils.e(TAG, "original file not exists");
        return false;
    }

    private byte[] getBuffer(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return buffer;
        }
    }

    private byte[] getBuffer(int id) {
        byte[] buffer = null;
        try {
            InputStream in = this.mContext.getResources().openRawResource(id);
            buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return buffer;
        }
    }
}
