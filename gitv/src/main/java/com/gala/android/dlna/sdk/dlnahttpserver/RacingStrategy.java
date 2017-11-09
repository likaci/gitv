package com.gala.android.dlna.sdk.dlnahttpserver;

import java.util.HashMap;
import java.util.Map;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.util.Debug;

public class RacingStrategy {
    public static Map<String, MessageData> dataHashMap = new HashMap();

    public static synchronized boolean isMessageOk(HTTPRequest httpReq) {
        boolean z = false;
        synchronized (RacingStrategy.class) {
            String[] dataSplit = httpReq.getTempContent().split("#");
            if (dataSplit.length == 3) {
                String uuidKey = dataSplit[0].trim();
                if (dataHashMap.containsKey(uuidKey)) {
                    MessageData msgData = (MessageData) dataHashMap.get(uuidKey);
                    long time = Long.parseLong(dataSplit[1]);
                    if (msgData.getTime() == time) {
                        Debug.message("repeat, uuid:" + dataSplit[0] + " time:" + dataSplit[1] + " data:" + dataSplit[2]);
                    } else if (time < msgData.getTime()) {
                        Debug.message("later, uuid:" + dataSplit[0] + " time:" + dataSplit[1] + " data:" + dataSplit[2]);
                    } else {
                        msgData.setTime(time);
                        z = true;
                    }
                } else {
                    Debug.message("uuid 没有命中");
                    dataHashMap.put(uuidKey, new MessageData(uuidKey, Long.parseLong(dataSplit[1]), dataSplit[2].getBytes()[0]));
                    z = true;
                }
            }
        }
        return z;
    }
}
