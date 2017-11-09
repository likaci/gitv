package com.gala.video.lib.share.ifimpl.logrecord;

import android.content.Context;
import android.util.Log;
import com.gala.report.core.log.ILogCore;
import com.gala.report.core.multiprocess.IMultiProcess;
import com.gala.report.core.upload.IFeedbackResultListener;
import com.gala.report.core.upload.IUploadCore;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.msghandler.IMsgHandlerCore;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.preference.LogRecordPreference;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordInitListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

class LogRecordProvider extends Wrapper implements ILogRecordProvider {
    private static final String TAG = "LogRecordProvider";
    private LogRecordInit mLogRecordFeature;

    private class CommonDummyHandler implements InvocationHandler {
        private CommonDummyHandler() {
        }

        private Class getClass(Type type, int i) {
            if (type instanceof ParameterizedType) {
                return getGenericClass((ParameterizedType) type, i);
            }
            if (type instanceof TypeVariable) {
                return getClass(((TypeVariable) type).getBounds()[0], 0);
            }
            return (Class) type;
        }

        private Class getGenericClass(ParameterizedType parameterizedType, int i) {
            Type genericClass = parameterizedType.getActualTypeArguments()[i];
            if (genericClass instanceof ParameterizedType) {
                return (Class) ((ParameterizedType) genericClass).getRawType();
            }
            if (genericClass instanceof GenericArrayType) {
                return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
            }
            if (genericClass instanceof TypeVariable) {
                return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
            }
            return (Class) genericClass;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                String clsName = getClass(method.getGenericReturnType(), 0).getName();
                if (clsName.equals("boolean")) {
                    return Boolean.valueOf(false);
                }
                if (clsName.equals("int") || clsName.equals("long") || clsName.equals("byte") || clsName.equals("short") || clsName.equals("char")) {
                    return Integer.valueOf(0);
                }
                if (clsName.equals("float") || clsName.equals("double")) {
                    return Float.valueOf(0.0f);
                }
                return null;
            } catch (Exception e) {
                Log.d(LogRecordProvider.TAG, "CommonDummyHandler Exception = ", e);
                return null;
            }
        }
    }

    LogRecordProvider() {
    }

    public void initialize(ILogRecordInitListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initialize begin");
        }
        this.mLogRecordFeature = new LogRecordInit(listener);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initialize end");
        }
    }

    public ILogCore getLogCore() {
        ILogCore ret = null;
        if (this.mLogRecordFeature != null) {
            ret = this.mLogRecordFeature.getLogCore();
        }
        if (ret != null) {
            return ret;
        }
        return (ILogCore) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{ILogCore.class}, new CommonDummyHandler());
    }

    public IUploadCore getUploadCore() {
        IUploadCore ret = null;
        if (this.mLogRecordFeature != null) {
            ret = this.mLogRecordFeature.getUploadCore();
        }
        if (ret != null) {
            return ret;
        }
        return (IUploadCore) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IUploadCore.class}, new CommonDummyHandler());
    }

    public IMultiProcess getMultiProcess() {
        IMultiProcess ret = null;
        if (this.mLogRecordFeature != null) {
            ret = this.mLogRecordFeature.getMultiProcess();
        }
        if (ret != null) {
            return ret;
        }
        return (IMultiProcess) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IMultiProcess.class}, new CommonDummyHandler());
    }

    public IMsgHandlerCore getMsgHandlerCore() {
        IMsgHandlerCore ret = null;
        if (this.mLogRecordFeature != null) {
            ret = this.mLogRecordFeature.getMsgHandlerCore();
        }
        if (ret != null) {
            return ret;
        }
        return (IMsgHandlerCore) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{IMsgHandlerCore.class}, new CommonDummyHandler());
    }

    public UploadOption getUploadOptionInfoAndParse(UploadOptionMap map) {
        UploadOption uploadOption = getUploadOption();
        parseUploadOption(uploadOption, map);
        return uploadOption;
    }

    private UploadOption getUploadOption() {
        if (this.mLogRecordFeature != null) {
            return this.mLogRecordFeature.getLogRecordFeature().getUploadOption();
        }
        return null;
    }

    private void parseUploadOption(UploadOption uploadOption, UploadOptionMap map) {
        if (uploadOption != null) {
            uploadOption.parseUploadOptionMap(map.getUploadOptionMap());
        }
    }

    public UploadExtraInfo getUploadExtraInfoAndParse(UploadExtraMap map) {
        UploadExtraInfo uploadExtraInfo = getUploadExtraInfo();
        parseUploadExtraInfo(uploadExtraInfo, map);
        return uploadExtraInfo;
    }

    private UploadExtraInfo getUploadExtraInfo() {
        if (this.mLogRecordFeature != null) {
            return this.mLogRecordFeature.getLogRecordFeature().getUploadExtraInfo();
        }
        return null;
    }

    private void parseUploadExtraInfo(UploadExtraInfo uploadExtraInfo, UploadExtraMap map) {
        if (uploadExtraInfo != null) {
            uploadExtraInfo.parseUploadExtraInfoMap(map.getUploadExtraMap());
        }
    }

    public void notifySaveLogFile() {
        if (this.mLogRecordFeature != null) {
            this.mLogRecordFeature.getLogCore().snapShot();
        }
    }

    public void sendRecorder(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, IFeedbackResultListener listener) {
        if (isLogRecordFeatureReady()) {
            getUploadCore().sendRecorder(uploadExtraInfo, uploadOption, recorder, listener);
        } else {
            LogRecordUtils.showLogRecordNotAlreadyToast(context);
        }
    }

    public void setLogRecordEnable(boolean open) {
        LogRecordPreference.saveLogrecordOpen(AppRuntimeEnv.get().getApplicationContext(), open);
    }

    public boolean isLogRecordEnable() {
        return LogRecordPreference.getLogrecordOpen(AppRuntimeEnv.get().getApplicationContext());
    }

    public boolean isLogRecordFeatureReady() {
        if (this.mLogRecordFeature == null || this.mLogRecordFeature.getLogRecordFeature() == null) {
            return false;
        }
        return true;
    }
}
