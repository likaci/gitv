package com.gala.video.app.epg.project.config;

import android.content.Context;
import android.util.Log;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.config.IConfigInterface;

public class ConfigCreator {
    private void checkConfigArguments(Context context) {
        if (context == null) {
            throw new NullPointerException("The config properties should not be null.");
        }
    }

    public IConfigInterface config(Context context) {
        IConfigInterface configProvider = null;
        checkConfigArguments(context);
        String defaultCustomer = "gala";
        try {
            return (IConfigInterface) Class.forName(getConfigClassName(BuildCache.getInstance().getString(BuildConstance.APK_CUSTOMER, defaultCustomer))).newInstance();
        } catch (Exception e) {
            try {
                return (IConfigInterface) Class.forName(getConfigClassName(defaultCustomer)).newInstance();
            } catch (Exception e1) {
                e1.printStackTrace();
                return configProvider;
            }
        }
    }

    private String getConfigClassName(String customer) {
        StringBuilder builder = new StringBuilder(AppEnvConstant.DEF_PKG_NAME);
        builder.append(".app.epg.project.config.").append(customer).append(".DeviceConfig");
        Log.d("ConfigCreator", "getConfigClassName = " + builder.toString());
        return builder.toString();
    }
}
