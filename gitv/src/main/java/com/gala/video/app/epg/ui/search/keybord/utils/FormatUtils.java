package com.gala.video.app.epg.ui.search.keybord.utils;

import com.gala.cloudui.constants.CuteConstants;

public class FormatUtils {
    public static boolean isTxtFormat(String path) {
        return path.substring(path.lastIndexOf("."), path.length()).endsWith(CuteConstants.TYPE_TXT);
    }
}
