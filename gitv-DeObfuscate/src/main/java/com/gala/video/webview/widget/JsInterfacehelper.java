package com.gala.video.webview.widget;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class JsInterfacehelper {
    private static final String KEY_ARG_ARRAY = "args";
    private static final String KEY_ARG_PREFIX = "arg";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String MSG_PROMPT_HEADER = "GALAApp:";
    private static final String TAG = "JsInterfacehelper";
    private Map<String, Object> mJsInterfaceMap;
    private String mJsSave = null;

    public JsInterfacehelper(String interfaceName, Object interfaceObj) {
        if (this.mJsInterfaceMap == null) {
            this.mJsInterfaceMap = new HashMap(1);
        }
        this.mJsInterfaceMap.put(interfaceName, interfaceObj);
    }

    public void setInterfaceJS(String js) {
        this.mJsSave = js;
    }

    String getInterfaceJS() {
        return injectJavascriptInterfaces();
    }

    private String injectJavascriptInterfaces() {
        if (TextUtils.isEmpty(this.mJsSave)) {
            this.mJsSave = genJavascript();
            return this.mJsSave;
        }
        Log.e(TAG, "return injectJavascriptInterfaces");
        return this.mJsSave;
    }

    private String genJavascript() {
        if (this.mJsInterfaceMap == null || this.mJsInterfaceMap.size() == 0) {
            this.mJsSave = null;
            return this.mJsSave;
        }
        StringBuilder script = new StringBuilder("javascript:(function JsAddJavascriptInterface_(){");
        for (Entry<String, Object> entry : this.mJsInterfaceMap.entrySet()) {
            try {
                createJsMethod((String) entry.getKey(), entry.getValue(), script);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        script.append("})()");
        return script.toString();
    }

    @SuppressLint({"NewApi"})
    private void createJsMethod(String interfaceName, Object obj, StringBuilder script) {
        if (!TextUtils.isEmpty(interfaceName) && obj != null && script != null) {
            Class<? extends Object> objClass = obj.getClass();
            script.append("if(typeof(window.").append(interfaceName).append(")!='undefined'){console.log('window.");
            script.append(interfaceName);
            script.append("is exist!'); }else { window.").append(interfaceName).append("={");
            for (Method method : objClass.getDeclaredMethods()) {
                int i;
                String methodName = method.getName();
                script.append(methodName).append(":function(");
                int argCount = method.getParameterTypes().length;
                if (argCount > 0) {
                    int maxCount = argCount - 1;
                    for (i = 0; i < maxCount; i++) {
                        script.append(KEY_ARG_PREFIX).append(i).append(",");
                    }
                    script.append(KEY_ARG_PREFIX).append(argCount - 1);
                }
                if (method.getReturnType() != Void.TYPE) {
                    script.append(") {return prompt('").append(MSG_PROMPT_HEADER).append("'+");
                } else {
                    script.append(") {prompt('").append(MSG_PROMPT_HEADER).append("'+");
                }
                script.append("JSON.stringify({");
                script.append(KEY_INTERFACE_NAME).append(":'").append(interfaceName).append("',");
                script.append(KEY_FUNCTION_NAME).append(":'").append(methodName).append("',");
                script.append(KEY_ARG_ARRAY).append(":[");
                if (argCount > 0) {
                    int max = argCount - 1;
                    for (i = 0; i < max; i++) {
                        script.append(KEY_ARG_PREFIX).append(i).append(",");
                    }
                    script.append(KEY_ARG_PREFIX).append(max);
                }
                script.append("]})");
                script.append(");");
                script.append("}, ");
            }
            script.append("};");
            script.append("}");
        }
    }

    public String call(WebView view, JSONObject jsPromptObject) {
        String str = null;
        if (jsPromptObject != null) {
            try {
                String interfaceName = jsPromptObject.getString(KEY_INTERFACE_NAME);
                String methodName = jsPromptObject.getString(KEY_FUNCTION_NAME);
                JSONArray argsArray = jsPromptObject.getJSONArray(KEY_ARG_ARRAY);
                Object[] args = null;
                if (argsArray != null) {
                    int len = argsArray.length();
                    if (len > 0) {
                        args = new Object[len];
                        for (int i = 0; i < len; i++) {
                            args[i] = argsArray.get(i);
                        }
                    }
                }
                str = invokeJSInterfaceMethod(interfaceName, methodName, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    private String invokeJSInterfaceMethod(String interfaceName, String methodName, Object[] args) {
        String str = null;
        Object obj = this.mJsInterfaceMap.get(interfaceName);
        if (obj != null) {
            Class<?>[] parameterTypes = null;
            int count = 0;
            if (args != null) {
                count = args.length;
            }
            if (count > 0) {
                parameterTypes = new Class[count];
                for (int i = 0; i < count; i++) {
                    parameterTypes[i] = getClassFromJsonObject(args[i]);
                }
            }
            try {
                Object returnObj = obj.getClass().getMethod(methodName, parameterTypes).invoke(obj, args);
                boolean isVoid = returnObj == null || returnObj.getClass() == Void.TYPE;
                str = isVoid ? "" : returnObj.toString();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return str;
    }

    private Class<?> getClassFromJsonObject(Object obj) {
        Class<?> cls = obj.getClass();
        if (cls == Integer.class) {
            return Integer.TYPE;
        }
        if (cls == Boolean.class) {
            return Boolean.TYPE;
        }
        return String.class;
    }

    static String getInterfacedName(JSONObject jsonObject) {
        return jsonObject.optString(KEY_INTERFACE_NAME);
    }

    static JSONObject getMessageJSONObject(String message) {
        try {
            return new JSONObject(message.substring(MSG_PROMPT_HEADER.length()));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    static boolean isSafeWebViewCallMsg(String message) {
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        return message.startsWith(MSG_PROMPT_HEADER);
    }
}
