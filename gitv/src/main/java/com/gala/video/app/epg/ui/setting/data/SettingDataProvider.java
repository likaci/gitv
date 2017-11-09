package com.gala.video.app.epg.ui.setting.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.AndroidRuntimeException;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.lib.framework.core.proguard.Keep;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Keep
public class SettingDataProvider {
    private static final String AUTHORTY = "com.gala.video.settings.SettingContentProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://com.gala.video.settings.SettingContentProvider");
    private static final String ITEMOPTION = "itemoption";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static SettingDataProvider mInstance = null;
    private final String TAG = "SettingDataProvider";
    private Context mContext;

    private SettingDataProvider(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (SettingDataProvider.class) {
                if (mInstance == null) {
                    mInstance = new SettingDataProvider(context);
                }
            }
        }
    }

    public static synchronized SettingDataProvider getInstance() {
        SettingDataProvider settingDataProvider;
        synchronized (SettingDataProvider.class) {
            if (mInstance == null) {
                throw new AndroidRuntimeException("fetcher must be initialized before getting!");
            }
            settingDataProvider = mInstance;
        }
        return settingDataProvider;
    }

    public CustomDataInfo getCustomDataInfo(SettingItem item) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("SettingDataProvider", "getCustomDataInfo item=" + item);
        }
        CustomDataInfo info = null;
        if (StringUtils.isEmpty(item.getItemKey())) {
            return null;
        }
        try {
            Cursor cur = this.mContext.getContentResolver().query(CONTENT_URI, null, "key", new String[]{item.getItemKey()}, null);
            if (cur != null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d("SettingDataProvider", "cur.getCount =" + cur.getCount());
                }
                if (cur.getCount() <= 0 || !cur.moveToFirst()) {
                    cur.close();
                } else {
                    do {
                        info = createInfoFromCursor(cur);
                        if (LogUtils.mIsDebug) {
                            LogUtils.d("SettingDataProvider", "getCustomDataInfo info key/value/option=" + info.getKey() + "/" + info.getValue() + "/" + info.getItemOption());
                        }
                    } while (cur.moveToNext());
                    cur.close();
                }
            }
        } catch (Exception exception) {
            LogUtils.d("SettingDataProvider", "getCustomDataInfo exception", exception);
        }
        return info;
    }

    public String getCustomKey(CustomDataInfo info) {
        String key = "";
        if (info != null) {
            return info.getValue();
        }
        return key;
    }

    public String getCustomValue(CustomDataInfo info) {
        String value = "";
        if (info != null) {
            return info.getValue();
        }
        return value;
    }

    public List<String> getOptions(CustomDataInfo info) {
        List<String> op = new ArrayList();
        if (!(info == null || StringUtils.isEmpty(info.getItemOption()))) {
            String[] ops = info.getItemOption().split(",");
            if (ops != null && ops.length > 0) {
                for (String s : ops) {
                    op.add(s);
                }
            }
        }
        return op;
    }

    private CustomDataInfo createInfoFromCursor(Cursor cursor) {
        CustomDataInfo c = new CustomDataInfo();
        c.setKey(cursor.getString(cursor.getColumnIndex("key")));
        c.setValue(cursor.getString(cursor.getColumnIndex("value")));
        c.setItemOption(cursor.getString(cursor.getColumnIndex(ITEMOPTION)));
        return c;
    }

    private ContentValues createRow(CustomDataInfo info) {
        ContentValues ret = new ContentValues();
        ret.put("key", info.getKey());
        ret.put("value", info.getValue());
        ret.put(ITEMOPTION, info.getItemOption());
        return ret;
    }

    public void updateCustomItem(SettingItem item) {
        if (LogUtils.mIsDebug) {
            LogUtils.d("SettingDataProvider", "updateCustomItem item=" + item);
        }
        ContentValues values = new ContentValues();
        if (item != null) {
            try {
                if (LogUtils.mIsDebug) {
                    LogUtils.d("SettingDataProvider", "updateCustom key/value = " + item.getItemKey() + "/" + item.getItemLastState());
                }
                values.put("key", item.getItemKey());
                values.put("value", item.getItemLastState());
                this.mContext.getContentResolver().update(CONTENT_URI, values, "key", new String[]{item.getItemKey()});
            } catch (Exception exception) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d("SettingDataProvider", "updateCustomItem exception", exception);
                }
            }
        }
    }
}
