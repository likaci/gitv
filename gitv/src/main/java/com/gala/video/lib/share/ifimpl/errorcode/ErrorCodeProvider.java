package com.gala.video.lib.share.ifimpl.errorcode;

import com.alibaba.fastjson.JSON;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel.ErrorCodeJSON;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.IErrorCodeProvider.Callback;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.IErrorCodeProvider.Wrapper;
import com.gala.video.lib.share.project.Project;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class ErrorCodeProvider extends Wrapper {
    private static final String CACHE_FILENAME = "home/error_code_v2.dem";
    private static final String ERROR_CODE_URL = "/ext/tv/app/error_msg.json";
    private static final String TAG = "EPG/startup/ErrorCodeProvider";
    private HashMap<String, ArrayList<ErrorCodeModel>> mModels = new HashMap();
    private final Object mRWLock = new Object();
    private String mUrl;

    public ErrorCodeProvider() {
        String domainName = Project.getInstance().getBuild().getDomainName();
        if (StringUtils.isEmpty((CharSequence) domainName)) {
            domainName = BuildDefaultDocument.APK_DOMAIN_NAME;
        }
        this.mUrl = "http://static." + domainName + ERROR_CODE_URL;
    }

    public void updateErrorCode(final Callback callback) {
        ApiFactory.getCommonApi().callSync(this.mUrl, new ICommonApiCallback() {
            public void onSuccess(String json) {
                if (!StringUtils.isEmpty((CharSequence) json)) {
                    try {
                        ErrorCodeJSON data = (ErrorCodeJSON) JSON.parseObject(json, ErrorCodeJSON.class);
                        if (data != null) {
                            List models = data.getData();
                            if (!ListUtils.isEmpty(models)) {
                                ErrorCodeProvider.this.saveDataToLocal(models);
                                callback.onSuccess(json);
                            } else if (LogUtils.mIsDebug) {
                                LogUtils.e(ErrorCodeProvider.TAG, "updateErrorCode models is empty");
                            }
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.e(ErrorCodeProvider.TAG, "updateErrorCode data is empty");
                        }
                    } catch (Exception e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.e(ErrorCodeProvider.TAG, "updateErrorCode parse json error:" + e);
                        }
                    }
                }
            }

            public void onException(Exception e, String arg1) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(ErrorCodeProvider.TAG, "updateErrorCode api exception:" + e);
                }
                callback.onException(e, arg1, ErrorCodeProvider.this.mUrl);
            }
        }, false, "ErrorCodeDownload");
    }

    private void saveDataToLocal(List<ErrorCodeModel> models) {
        synchronized (this.mRWLock) {
            this.mModels.clear();
            for (ErrorCodeModel model : models) {
                ArrayList<ErrorCodeModel> sameKeyErrorCodeModel;
                if (this.mModels.containsKey(model.getCode())) {
                    sameKeyErrorCodeModel = (ArrayList) this.mModels.get(model.getCode());
                } else {
                    sameKeyErrorCodeModel = new ArrayList();
                }
                sameKeyErrorCodeModel.add(model);
                this.mModels.put(model.getCode(), sameKeyErrorCodeModel);
            }
            try {
                SerializableUtils.write(this.mModels, "home/error_code_v2.dem");
            } catch (Exception e) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(TAG, "saveDataToLocal()---e=" + e.getMessage());
                }
            }
        }
    }

    private HashMap<String, ArrayList<ErrorCodeModel>> getDataMap() {
        if (ListUtils.isEmpty(this.mModels)) {
            synchronized (this.mRWLock) {
                if (ListUtils.isEmpty(this.mModels)) {
                    try {
                        this.mModels = (HashMap) SerializableUtils.read("home/error_code_v2.dem");
                    } catch (Exception e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.e(TAG, "getDataMap()---e=" + e.getMessage());
                        }
                    }
                }
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "getDataMap()---out------");
        }
        return this.mModels;
    }

    public ErrorCodeModel getErrorCodeModel(String id) {
        return getErrorCodeModel(id, null);
    }

    public ErrorCodeModel getErrorCodeModel(String id, String type) {
        Map models = getDataMap();
        if (ListUtils.isEmpty(models)) {
            return null;
        }
        try {
            if (StringUtils.isEmpty((CharSequence) type)) {
                if (models.get(id) != null) {
                    return (ErrorCodeModel) ((ArrayList) models.get(id)).get(0);
                }
            } else if (models.get(id) != null) {
                Iterator it = ((ArrayList) models.get(id)).iterator();
                while (it.hasNext()) {
                    ErrorCodeModel each = (ErrorCodeModel) it.next();
                    if (type.equals(each.getType())) {
                        return each;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.mModels.clear();
            deleteUnuseFile("home/error_code_v2.dem");
        }
        return null;
    }

    private void deleteUnuseFile(String fileName) {
        synchronized (this.mRWLock) {
            File file = new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
