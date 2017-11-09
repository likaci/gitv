package com.gala.video.lib.share.utils;

import android.content.Context;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.io.FileUtil;
import com.gala.video.lib.framework.core.utils.reflect.ClassMethodHolder;
import com.gala.video.lib.framework.core.utils.reflect.ObjectMethodHolder;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JsExcutor {
    private static final String JS_EXCUTOR_PATH = "lib/JsExcutor.jar";
    private static final String TAG = "JsExcutor";
    private static String sJsExcutorJarPath = null;

    private JsExcutor() {
    }

    public static String runScript(String js, String functionName, Object[] functionParams, Context context) {
        if (StringUtils.isEmpty((CharSequence) js)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "runScript,js is empty!");
            }
            return null;
        }
        StringBuilder params = new StringBuilder();
        for (Object o : functionParams) {
            if (o != null) {
                params.append(o.toString() + ", ");
            } else {
                params.append(o + ", ");
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "runScript(functionName:" + functionName + ",functionParams:" + params.toString() + ")");
        }
        DexClassLoader clzLoader = getDexClassLoader(context);
        if (clzLoader == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "runScript(functionName:" + functionName + "), clzLoader is null, return null");
            }
            return null;
        }
        String jsonResult = null;
        try {
            Class clzContext = clzLoader.loadClass("org.mozilla.javascript.Context");
            Object rhino = new ClassMethodHolder(clzContext, "enter", new Class[0]).getValue(new Object[0]);
            new ObjectMethodHolder(rhino, "setOptimizationLevel", Integer.TYPE).getValue(Integer.valueOf(-1));
            ObjectMethodHolder objectMethodHolder = new ObjectMethodHolder(rhino, "initStandardObjects", new Class[0]);
            Class<?> clzScriptable = clzLoader.loadClass("org.mozilla.javascript.Scriptable");
            Object scope = objectMethodHolder.getValue(new Object[0]);
            ClassMethodHolder clzContextJavaToJs = new ClassMethodHolder(clzContext, "javaToJS", Object.class, clzScriptable);
            Object jsContext = clzContextJavaToJs.getValue(AppRuntimeEnv.get().getApplicationContext(), scope);
            Object jsClassLoader = clzContextJavaToJs.getValue(AppRuntimeEnv.get().getApplicationContext(), scope);
            ClassMethodHolder clzScriptableObjectPutProperty = new ClassMethodHolder(clzLoader.loadClass("org.mozilla.javascript.ScriptableObject"), "putProperty", (Class[]) new Class[]{clzScriptable, String.class, Object.class});
            clzScriptableObjectPutProperty.getValue(scope, "javaContext", jsContext);
            clzScriptableObjectPutProperty.getValue(scope, "javaLoader", jsClassLoader);
            new ObjectMethodHolder(rhino, "evaluateString", clzScriptable, String.class, String.class, Integer.TYPE, Object.class).getValue(scope, js, TAG, Integer.valueOf(1), null);
            Object result = new ObjectMethodHolder(new ObjectMethodHolder(scope, "get", String.class, clzScriptable).getValue(functionName, scope), "call", clzContext, clzScriptable, clzScriptable, Object[].class).getValue(rhino, scope, scope, functionParams);
            Class<?> clzNativeJavaObject = clzLoader.loadClass("org.mozilla.javascript.NativeJavaObject");
            Class<?> clzNativeObject = clzLoader.loadClass("org.mozilla.javascript.NativeObject");
            if (result != null) {
                if (result instanceof String) {
                    jsonResult = (String) result;
                } else if (result.getClass().isAssignableFrom(clzNativeJavaObject)) {
                    jsonResult = (String) new ObjectMethodHolder(result, "getDefaultValue", Class.class).getValue(String.class);
                } else if (result.getClass().isAssignableFrom(clzNativeObject)) {
                    jsonResult = (String) new ObjectMethodHolder(result, "getDefaultValue", Class.class).getValue(String.class);
                } else {
                    jsonResult = result.toString();
                }
            }
        } catch (Exception e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1569d(TAG, "runScript(functionName:" + functionName + ",functionParams:" + functionParams + "), exception occurs!", e);
            }
            try {
                new ClassMethodHolder(clzLoader.loadClass("org.mozilla.javascript.Context"), "exit", new Class[0]).getValue(new Object[0]);
            } catch (Exception e2) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1569d(TAG, "runScript(functionName:" + functionName + ",functionParams:" + functionParams + "), exception occurs!", e);
                }
            }
        }
        if (!LogUtils.mIsDebug) {
            return jsonResult;
        }
        LogUtils.m1568d(TAG, "runScript(functionName:" + functionName + "), return " + jsonResult);
        return jsonResult;
    }

    private static synchronized DexClassLoader getDexClassLoader(Context context) {
        DexClassLoader dexLoader;
        synchronized (JsExcutor.class) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "getDexClassLoader(), sJsExcutorJarPath = " + sJsExcutorJarPath);
            }
            if (StringUtils.isEmpty(sJsExcutorJarPath)) {
                sJsExcutorJarPath = getJsExcutorJarPath(context, JS_EXCUTOR_PATH, SystemConfigPreference.getJsExcutorJarVersion(context));
            }
            dexLoader = null;
            if (!StringUtils.isEmpty(sJsExcutorJarPath)) {
                File jarFile = new File(sJsExcutorJarPath);
                if (jarFile.exists()) {
                    SystemConfigPreference.setJsExcutorJarVersion(context, AppClientUtils.getClientVersion());
                    dexLoader = new DexClassLoader(jarFile.getAbsolutePath(), context.getFilesDir().toString(), null, context.getClassLoader());
                }
            }
        }
        return dexLoader;
    }

    private static String getJsExcutorJarPath(Context context, String pathInAssets, String currentFileVersion) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getJsExcutorJarPath(pathInAssets:" + pathInAssets + ",currentFileVersion:" + currentFileVersion + ")");
        }
        if (StringUtils.isEmpty((CharSequence) pathInAssets)) {
            return null;
        }
        String targetFilePath = context.getFilesDir() + "/" + pathInAssets;
        boolean isCheckPass = false;
        String clientVersion = AppClientUtils.getClientVersion();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getJsExcutorJarPath, clientVersion : " + clientVersion);
        }
        if (StringUtils.isEmpty((CharSequence) currentFileVersion) || !(clientVersion == null || clientVersion.equalsIgnoreCase(currentFileVersion))) {
            File file = new File(targetFilePath);
            if (file.exists()) {
                file.delete();
            }
            isCheckPass = copyFileFromAssets(context, pathInAssets, targetFilePath);
        } else if (clientVersion != null && clientVersion.equals(currentFileVersion)) {
            isCheckPass = new File(targetFilePath).exists() ? true : copyFileFromAssets(context, pathInAssets, targetFilePath);
        }
        if (!isCheckPass) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "getJsExcutorJarPath, return null");
            }
            return null;
        } else if (!LogUtils.mIsDebug) {
            return targetFilePath;
        } else {
            LogUtils.m1568d(TAG, "getJsExcutorJarPath, return " + targetFilePath);
            return targetFilePath;
        }
    }

    private static boolean copyFileFromAssets(Context context, String fileName, String targetFilePath) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "copyFileFromAssets(fileInAssets: " + fileName + ", targetFilePath:" + targetFilePath + ")");
        }
        try {
            InputStream in = context.getAssets().open(fileName);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            FileUtil.checkDir(targetFilePath);
            FileUtil.writeFile(targetFilePath, buffer);
            return true;
        } catch (IOException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1569d(TAG, "copyFileFromAssets(fileInAssets: " + fileName + ", targetFilePath:" + targetFilePath + "), exception occurs!", e);
            }
            return false;
        }
    }
}
