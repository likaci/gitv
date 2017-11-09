package com.gala.video.app.epg.home.data.provider;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.HashMap;
import java.util.Map;

public class ChannelIconProvider {
    private static final String COMMA = ",";
    private static final String DOT = ".";
    private static final String SOLIDUS = "/";
    private static final String TAG = "ChannelIconProvider";
    private static final String UNDERLINE = "_";
    private static ChannelIconProvider mInstance;
    private Map<Integer, String> mChannelIcons = new HashMap();

    private ChannelIconProvider() {
    }

    public static ChannelIconProvider get() {
        if (mInstance == null) {
            mInstance = new ChannelIconProvider();
        }
        return mInstance;
    }

    public void clear() {
        synchronized (this.mChannelIcons) {
            this.mChannelIcons.clear();
        }
    }

    public String getIconUrl(int channelId) {
        synchronized (this.mChannelIcons) {
            if (this.mChannelIcons.size() == 0) {
                CharSequence iconsUrl = GetInterfaceTools.getIThemeProvider().getDayChannelIconUrls();
                if (!StringUtils.isEmpty(iconsUrl)) {
                    for (String icon : iconsUrl.split(COMMA)) {
                        int chnId = getChannleId(icon);
                        if (chnId > -1) {
                            this.mChannelIcons.put(Integer.valueOf(chnId), icon);
                        }
                    }
                }
            }
        }
        return (String) this.mChannelIcons.get(Integer.valueOf(channelId));
    }

    private int getChannleId(String iconUrl) {
        int channelId = -1;
        if (StringUtils.isEmpty((CharSequence) iconUrl)) {
            return channelId;
        }
        int index = iconUrl.lastIndexOf(DOT);
        if (index > -1) {
            iconUrl = iconUrl.substring(0, index);
        }
        LogUtils.d(TAG, "getChannelId iconUrl=" + iconUrl);
        String[] solidus = iconUrl.split(SOLIDUS);
        if (solidus != null && solidus.length > 0) {
            CharSequence iconName = solidus[solidus.length - 1];
            if (!StringUtils.isEmpty(iconName)) {
                String[] underlines = iconName.split(UNDERLINE);
                if (underlines != null && underlines.length > 0) {
                    try {
                        channelId = Integer.parseInt(underlines[underlines.length - 1]);
                    } catch (NumberFormatException e) {
                        LogUtils.e(TAG, "getChannelId error msg=" + e.getMessage());
                    }
                }
            }
        }
        LogUtils.d(TAG, "getChannelId channelId=" + channelId);
        return channelId;
    }
}
