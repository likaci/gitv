package com.gala.video.app.epg.home.data.hdata.task;

import android.content.Context;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IDownloader;
import com.gala.download.base.IFileCallback;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultLet2kb;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.ui.search.keybord.preferece.KeyboardPreference;
import com.gala.video.app.epg.ui.search.keybord.utils.FormatUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.Precondition;
import java.io.File;

public class IntelligentSearchDownLoadTask extends BaseRequestTask {
    private static final String TAG = "IntelligentSearchDownLoadTask";
    private String keyboardLocalPath = (this.mContext.getFilesDir() + "/keyboardDictionary.txt");
    private Context mContext = AppRuntimeEnv.get().getApplicationContext();
    private IDownloader mDownloader = DownloaderAPI.getDownloader();
    private IFileCallback mIFileCallback = new C06462();
    private String mKeyboardUrl;

    class C06451 implements IApiCallback<ApiResultLet2kb> {
        C06451() {
        }

        public void onSuccess(ApiResultLet2kb apiResultLet2kb) {
            IntelligentSearchDownLoadTask.this.callBackSuccess(apiResultLet2kb);
        }

        public void onException(ApiException e) {
            String str;
            LogUtils.m1572e(IntelligentSearchDownLoadTask.TAG, "startDwonload() -> onException", e);
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
            String str2 = "pfec";
            if (e == null) {
                str = "";
            } else {
                str = e.getCode();
            }
            addItem = addItem.addItem(str2, str);
            str2 = Keys.ERRURL;
            if (e == null) {
                str = "";
            } else {
                str = e.getUrl();
            }
            addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "let2kb");
            str2 = Keys.ERRDETAIL;
            if (e == null) {
                str = "";
            } else {
                str = e.getMessage();
            }
            addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.f2035T, "0").setOthersNull().post();
        }
    }

    class C06462 implements IFileCallback {
        C06462() {
        }

        public void onSuccess(FileRequest imageRequest, String path) {
            IntelligentSearchDownLoadTask.this.onSuccessFile(path);
        }

        public void onFailure(FileRequest imageRequest, Exception e) {
            LogUtils.m1572e(IntelligentSearchDownLoadTask.TAG, "mIFileCallback() -> onFailure", e);
        }
    }

    public void invoke() {
        LogUtils.m1568d(TAG, "invoke IntelligentSearchDownLoadTask");
        TVApi.let2kb.callSync(new C06451(), "0");
    }

    public void onOneTaskFinished() {
        LogUtils.m1568d(TAG, "intelligentSearchDownLoadTask finished");
    }

    private void callBackSuccess(ApiResultLet2kb apiResultLet2kb) {
        if (apiResultLet2kb == null || Precondition.isNull(apiResultLet2kb.url)) {
            LogUtils.m1571e(TAG, "callBackSuccess() -> data is null!");
            return;
        }
        this.mKeyboardUrl = apiResultLet2kb.url;
        FileRequest imageRequest = new FileRequest(this.mKeyboardUrl);
        imageRequest.setSavePath(this.mContext.getFilesDir() + "/");
        this.mDownloader.loadFile(imageRequest, this.mIFileCallback);
        LogUtils.m1568d(TAG, "callBackSuccess() -> mKeyboardUrl" + this.mKeyboardUrl);
    }

    private void onSuccessFile(String path) {
        LogUtils.m1570d(TAG, "onSuccessFile() -> local path: ", path);
        if (StringUtils.isEmpty((CharSequence) path)) {
            LogUtils.m1571e(TAG, "onSuccessFile() -> path is null");
        } else if (!FormatUtils.isTxtFormat(path)) {
            LogUtils.m1571e(TAG, "onSuccessFile() ->  path is no txt format，use default txt");
        } else if (renameFile(path, this.keyboardLocalPath)) {
            LogUtils.m1571e(TAG, "remane file success!");
            KeyboardPreference.saveDownloadAddress(this.mContext, this.keyboardLocalPath);
        } else {
            LogUtils.m1571e(TAG, "rename fails");
        }
    }

    private static boolean renameFile(String oldPath, String newPath) {
        if (oldPath.equals(newPath)) {
            LogUtils.m1571e(TAG, "new file name is equals old name");
            return false;
        }
        File oldfile = new File(oldPath);
        if (oldfile.exists()) {
            File newfile = new File(newPath);
            if (newfile.exists()) {
                LogUtils.m1571e(TAG, "newfile :" + newPath + "exists！");
            }
            if (newfile.delete()) {
                LogUtils.m1571e(TAG, "newfile :" + newPath + "deleted！");
            }
            if (!oldfile.renameTo(newfile)) {
                return false;
            }
            LogUtils.m1571e(TAG, "newfile :" + newPath + "rename！");
            return true;
        }
        LogUtils.m1571e(TAG, "original file not exists");
        return false;
    }
}
