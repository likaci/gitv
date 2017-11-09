package com.gala.video.app.epg.ui.imsg.fetch;

import android.util.Log;
import com.gala.albumprovider.model.Tag;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.IMsgType;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.cybergarage.upnp.ssdp.SSDP;

public class MsgDataSource implements IMsgDataSource {
    private static final int DEFAULT_LABEL_COUNT = 3;

    public List<Tag> getLabels() {
        List<Tag> list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Tag label = new Tag();
            label.setType(IMsgType.getTypeType(i) + "");
            label.setName(IMsgType.getTypeName(i));
            list.add(label);
        }
        if (Project.getInstance().getBuild().isSupportSubscribe()) {
            label = new Tag();
            label.setType(String.valueOf(3));
            label.setName(IMsgType.getTypeName(3));
            list.add(label);
        }
        Log.e("getLabels", "getLabels --- list =" + list.size());
        return list;
    }

    public void getMsgList(int type, IMsgCallback callback) {
        List list = new ArrayList();
        if (type == 0) {
            try {
                list = GetInterfaceTools.getMsgCenter().getIMsgList();
            } catch (Exception e) {
                Log.e("getMsgList", "getMsgList --- e = " + e.getMessage());
            }
        } else {
            list = GetInterfaceTools.getMsgCenter().getIMsgListForType(type);
        }
        Log.e("getMsgList", "getMsgList --- list = " + list.size());
        if (ListUtils.isEmpty(list)) {
            callback.onFail();
            return;
        }
        handleTimeWord(list);
        callback.onSuccess(list);
    }

    private void handleTimeWord(List<IMsgContent> list) {
        String str = "";
        for (IMsgContent content : list) {
            long offset = System.currentTimeMillis() - content.localTime;
            if (offset < 0) {
                content.showTime = "";
            } else {
                long minOffset = (offset / 60) / 1000;
                if (minOffset < 1) {
                    str = ResourceUtil.getStr(C0508R.string.msg_showtime_1);
                } else if (minOffset < 60) {
                    str = ResourceUtil.getStr(C0508R.string.msg_showtime_2, Long.valueOf(minOffset));
                } else if (minOffset < 1440) {
                    str = ResourceUtil.getStr(C0508R.string.msg_showtime_3, Long.valueOf(minOffset / 60));
                } else if (minOffset < 43200) {
                    str = ResourceUtil.getStr(C0508R.string.msg_showtime_4, Long.valueOf(minOffset / 1440));
                } else {
                    Date date = new Date(content.localTime);
                    int year = date.getYear() + SSDP.PORT;
                    int month = date.getMonth() + 1;
                    int day = date.getDate();
                    str = ResourceUtil.getStr(C0508R.string.msg_showtime_5, Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
                }
                content.showTime = str;
            }
        }
    }
}
