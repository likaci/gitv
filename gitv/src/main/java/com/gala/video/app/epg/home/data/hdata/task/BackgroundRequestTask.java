package com.gala.video.app.epg.home.data.hdata.task;

import android.os.Environment;
import android.util.Log;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Kv;
import com.gala.tvapi.tv2.model.Theme;
import com.gala.tvapi.tv2.result.ApiResultTheme;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.ChannelIconProvider;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.io.FileUtil;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeProvider.ThemeModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.utils.Precondition;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BackgroundRequestTask extends BaseRequestTask {
    private static long SAFETY_SIZE = 10485760;
    private static final String TAG = "BackgroundRequestTask";
    private final long APK_SIZE = 5242880;

    public void invoke() {
        final IThemeProvider provider = GetInterfaceTools.getIThemeProvider();
        TVApi.theme.callSync(new IApiCallback<ApiResultTheme>() {
            public void onSuccess(ApiResultTheme apiResultTheme) {
                if (apiResultTheme != null && !Precondition.isEmpty(apiResultTheme.data)) {
                    for (Theme theme : apiResultTheme.data) {
                        if (theme.field.equals(Values.day) && theme.kv != null) {
                            Kv kv = theme.kv;
                            if (!provider.getDayThemeSourceName().equals(kv.theme_source) || Precondition.isEmpty(provider.getDayThemeSourcePath())) {
                                provider.setStatus(0);
                                String path = BackgroundRequestTask.this.downloadThemeApk(kv.theme_source);
                                GetInterfaceTools.getIThemeZipHelper().unZipFile(path);
                                ThemeModel model = new ThemeModel();
                                model.mChannelIconUrls = kv.channel_icon_urls;
                                model.mThemeSourceName = kv.theme_source;
                                model.mThemeSourcePath = path;
                                provider.saveDayThemeJson(model);
                                provider.setStatus(1);
                                ChannelIconProvider.get().clear();
                                HomeDataObservable.getInstance().post(HomeDataType.THEME, WidgetChangeStatus.DataChange, null);
                                return;
                            }
                            if (!kv.channel_icon_urls.equals(provider.getDayChannelIconUrls())) {
                                ThemeModel themeModel = provider.getThemeModel("home/home_cache/day_theme_channel_icons.dem");
                                themeModel.mChannelIconUrls = kv.channel_icon_urls;
                                provider.saveDayThemeJson(themeModel);
                            }
                            provider.setStatus(1);
                            return;
                        }
                    }
                }
            }

            public void onException(ApiException e) {
                String str;
                Log.d(BackgroundRequestTask.TAG, "theme list api error");
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
                addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "theme");
                str2 = Keys.ERRDETAIL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getMessage();
                }
                addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            }
        }, new String[0]);
    }

    private String downloadThemeApk(String name) {
        return download(name);
    }

    private String download(String name) {
        IOException e;
        IHomePingback addItem;
        String str;
        String str2;
        Throwable th;
        Error error;
        Log.d(TAG, "download-url-" + name);
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "11").add("ct", "160602_load").add(Keys.LDTYPE, "daynight").add("st", "0");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.d(TAG, "sd dir=" + sdDir);
        String memoryDir = AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath();
        Log.d(TAG, "memory dir=" + memoryDir);
        String filePath = memoryDir + File.separator + HomeDataConfig.DAY_THEME_APK_NAME;
        File file = new File(sdDir);
        if (FileUtil.sdcardCanWrite() && DeviceUtils.getSDCardSpareQuantity() - SAFETY_SIZE > 5242880 && file.isDirectory() && file.canWrite()) {
            Log.d(TAG, "sd card can write");
            filePath = sdDir + File.separator + HomeDataConfig.DAY_THEME_APK_NAME;
        }
        File cacheFile = new File(filePath);
        if (cacheFile.exists()) {
            Log.d(TAG, "delete old theme.zip success " + cacheFile.delete());
        }
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(name).openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode == 200) {
                Log.d(TAG, "content length = " + httpConn.getContentLength());
                inputStream = httpConn.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    PingBackParams params1 = new PingBackParams();
                    params1.add(Keys.T, "11").add("ct", "160602_load").add(Keys.LDTYPE, "daynight").add("st", "1");
                    PingBack.getInstance().postPingBackToLongYuan(params1.build());
                    Log.d(TAG, "download finished");
                    outputStream = fileOutputStream;
                } catch (IOException e2) {
                    e = e2;
                    outputStream = fileOutputStream;
                    try {
                        Log.e(TAG, "download theme exception " + e);
                        filePath = "";
                        addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", "").addItem(Keys.ERRURL, name).addItem(Keys.APINAME, "downloadtheme");
                        str = Keys.ERRDETAIL;
                        if (e != null) {
                            str2 = "";
                        } else {
                            str2 = e.getMessage();
                        }
                        addItem.addItem(str, str2).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e3) {
                                filePath = "";
                                e3.printStackTrace();
                                return filePath;
                            }
                        }
                        if (inputStream != null) {
                            return filePath;
                        }
                        inputStream.close();
                        return filePath;
                    } catch (Throwable th2) {
                        th = th2;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e32) {
                                filePath = "";
                                e32.printStackTrace();
                                throw th;
                            }
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                } catch (Error e4) {
                    error = e4;
                    outputStream = fileOutputStream;
                    Log.e(TAG, "download theme error " + error);
                    filePath = "";
                    addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", "").addItem(Keys.ERRURL, name).addItem(Keys.APINAME, "downloadtheme");
                    str = Keys.ERRDETAIL;
                    if (error != null) {
                        str2 = "";
                    } else {
                        str2 = error.getMessage();
                    }
                    addItem.addItem(str, str2).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e322) {
                            filePath = "";
                            e322.printStackTrace();
                            return filePath;
                        }
                    }
                    if (inputStream != null) {
                        return filePath;
                    }
                    inputStream.close();
                    return filePath;
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = fileOutputStream;
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
            }
            Log.e(TAG, "http connect error" + responseCode);
            filePath = "";
            HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", String.valueOf(responseCode)).addItem(Keys.ERRURL, name).addItem(Keys.APINAME, "downloadtheme").addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            httpConn.disconnect();
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e3222) {
                    filePath = "";
                    e3222.printStackTrace();
                    return filePath;
                }
            }
            if (inputStream == null) {
                return filePath;
            }
            inputStream.close();
            return filePath;
        } catch (IOException e5) {
            e3222 = e5;
            Log.e(TAG, "download theme exception " + e3222);
            filePath = "";
            addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", "").addItem(Keys.ERRURL, name).addItem(Keys.APINAME, "downloadtheme");
            str = Keys.ERRDETAIL;
            if (e3222 != null) {
                str2 = e3222.getMessage();
            } else {
                str2 = "";
            }
            addItem.addItem(str, str2).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                return filePath;
            }
            inputStream.close();
            return filePath;
        } catch (Error e6) {
            error = e6;
            Log.e(TAG, "download theme error " + error);
            filePath = "";
            addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008").addItem("pfec", "").addItem(Keys.ERRURL, name).addItem(Keys.APINAME, "downloadtheme");
            str = Keys.ERRDETAIL;
            if (error != null) {
                str2 = error.getMessage();
            } else {
                str2 = "";
            }
            addItem.addItem(str, str2).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                return filePath;
            }
            inputStream.close();
            return filePath;
        }
    }

    public void onOneTaskFinished() {
    }
}
