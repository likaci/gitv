package com.gala.tvapi.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TVApiRecordLog {
    private static LinkedList<TVApiLogModel> f922a = new LinkedList();

    public static synchronized List<TVApiLogModel> getTVApiRecordLogList() {
        List<TVApiLogModel> arrayList;
        synchronized (TVApiRecordLog.class) {
            arrayList = new ArrayList();
            Iterator it = f922a.iterator();
            while (it.hasNext()) {
                arrayList.add((TVApiLogModel) it.next());
            }
            Collections.reverse(arrayList);
        }
        return arrayList;
    }

    public static synchronized void addTVApiLogRecordLog(TVApiLogModel log) {
        synchronized (TVApiRecordLog.class) {
            if (f922a.size() == 40) {
                f922a.remove(0);
            }
            f922a.add(log);
        }
    }

    public static synchronized void addTVApiLogRecordLog(String url, String response) {
        synchronized (TVApiRecordLog.class) {
            TVApiLogModel tVApiLogModel = new TVApiLogModel();
            tVApiLogModel.setUrl(url);
            tVApiLogModel.setResponse(response);
            if (f922a.size() == 40) {
                f922a.remove(0);
            }
            f922a.add(tVApiLogModel);
        }
    }
}
