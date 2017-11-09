package com.gala.video.app.player.ui.carousel;

import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class CarouselPlayerDataProvider {
    private static HashMap<String, List<TVChannelCarousel>> sMap = new HashMap();
    private static HashMap<String, List<CarouselChannelDetail>> sProgramMap = new HashMap();
    private static CarouselPlayerDataProvider sProvider;
    private final String TAG = "Player/Ui/CarouselPlayerDataProvider";
    private final Object mLock1 = new Object();
    private final Object mLock2 = new Object();

    public static void init() {
        if (sProvider == null) {
            sProvider = new CarouselPlayerDataProvider();
        }
    }

    public static CarouselPlayerDataProvider getInstance() {
        return sProvider;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCarouselChannel(java.util.List<com.gala.tvapi.tv2.model.TVChannelCarousel> r9, com.gala.tvapi.tv2.model.TVChannelCarouselTag r10) {
        /*
        r8 = this;
        r5 = r8.mLock1;
        monitor-enter(r5);
        if (r10 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
    L_0x0006:
        return;
    L_0x0007:
        r4 = com.gala.video.lib.framework.core.utils.ListUtils.isEmpty(r9);	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x0012;
    L_0x000d:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        goto L_0x0006;
    L_0x000f:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        throw r4;
    L_0x0012:
        r4 = sMap;	 Catch:{ all -> 0x000f }
        r6 = r10.name;	 Catch:{ all -> 0x000f }
        r4 = r4.containsKey(r6);	 Catch:{ all -> 0x000f }
        if (r4 != 0) goto L_0x0053;
    L_0x001c:
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x000f }
        r2.<init>();	 Catch:{ all -> 0x000f }
        r2.addAll(r9);	 Catch:{ all -> 0x000f }
        r4 = sMap;	 Catch:{ all -> 0x000f }
        r6 = r10.name;	 Catch:{ all -> 0x000f }
        r4.put(r6, r2);	 Catch:{ all -> 0x000f }
        r4 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x0051;
    L_0x002f:
        r4 = "Player/Ui/CarouselPlayerDataProvider";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x000f }
        r6.<init>();	 Catch:{ all -> 0x000f }
        r7 = "save the label of ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r7 = r10.name;	 Catch:{ all -> 0x000f }
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r7 = sProvider;	 Catch:{ all -> 0x000f }
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r6 = r6.toString();	 Catch:{ all -> 0x000f }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r4, r6);	 Catch:{ all -> 0x000f }
    L_0x0051:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        goto L_0x0006;
    L_0x0053:
        r4 = sMap;	 Catch:{ all -> 0x000f }
        r4 = r4.entrySet();	 Catch:{ all -> 0x000f }
        r1 = r4.iterator();	 Catch:{ all -> 0x000f }
    L_0x005d:
        r4 = r1.hasNext();	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x0051;
    L_0x0063:
        r0 = r1.next();	 Catch:{ all -> 0x000f }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x000f }
        r3 = r0.getKey();	 Catch:{ all -> 0x000f }
        r3 = (java.lang.String) r3;	 Catch:{ all -> 0x000f }
        if (r3 != 0) goto L_0x0073;
    L_0x0071:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        goto L_0x0006;
    L_0x0073:
        r4 = r10.name;	 Catch:{ all -> 0x000f }
        r4 = r4.equals(r3);	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x005d;
    L_0x007b:
        r2 = r0.getValue();	 Catch:{ all -> 0x000f }
        r2 = (java.util.List) r2;	 Catch:{ all -> 0x000f }
        if (r2 == 0) goto L_0x005d;
    L_0x0083:
        r2.clear();	 Catch:{ all -> 0x000f }
        r2.addAll(r9);	 Catch:{ all -> 0x000f }
        r4 = sMap;	 Catch:{ all -> 0x000f }
        r6 = r10.name;	 Catch:{ all -> 0x000f }
        r4.put(r6, r2);	 Catch:{ all -> 0x000f }
        r4 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x005d;
    L_0x0094:
        r4 = "Player/Ui/CarouselPlayerDataProvider";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x000f }
        r6.<init>();	 Catch:{ all -> 0x000f }
        r7 = "update the label of ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r7 = r10.name;	 Catch:{ all -> 0x000f }
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r6 = r6.toString();	 Catch:{ all -> 0x000f }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r4, r6);	 Catch:{ all -> 0x000f }
        goto L_0x005d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.player.ui.carousel.CarouselPlayerDataProvider.updateCarouselChannel(java.util.List, com.gala.tvapi.tv2.model.TVChannelCarouselTag):void");
    }

    public List<TVChannelCarousel> getCarouselChannelList(TVChannelCarouselTag tvtag) {
        List<TVChannelCarousel> list = null;
        synchronized (this.mLock1) {
            if (tvtag == null) {
            } else if (sMap.containsKey(tvtag.name)) {
                for (Entry<String, List<TVChannelCarousel>> entry : sMap.entrySet()) {
                    CharSequence tagname = (String) entry.getKey();
                    if (!StringUtils.isEmpty(tagname)) {
                        if (tvtag.name.equals(tagname)) {
                            list = (List) entry.getValue();
                            break;
                        }
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d("Player/Ui/CarouselPlayerDataProvider", "tag is null");
                    }
                }
            } else {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d("Player/Ui/CarouselPlayerDataProvider", "map does not contain the label of " + tvtag.name + sProvider);
                }
            }
        }
        return list;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCarouselChannelProgram(java.util.List<com.gala.sdk.player.data.CarouselChannelDetail> r9, com.gala.tvapi.tv2.model.TVChannelCarouselTag r10) {
        /*
        r8 = this;
        r5 = r8.mLock2;
        monitor-enter(r5);
        if (r10 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
    L_0x0006:
        return;
    L_0x0007:
        r4 = com.gala.video.lib.framework.core.utils.ListUtils.isEmpty(r9);	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x0012;
    L_0x000d:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        goto L_0x0006;
    L_0x000f:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        throw r4;
    L_0x0012:
        r4 = sProgramMap;	 Catch:{ all -> 0x000f }
        r6 = r10.name;	 Catch:{ all -> 0x000f }
        r4 = r4.containsKey(r6);	 Catch:{ all -> 0x000f }
        if (r4 != 0) goto L_0x0053;
    L_0x001c:
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x000f }
        r2.<init>();	 Catch:{ all -> 0x000f }
        r2.addAll(r9);	 Catch:{ all -> 0x000f }
        r4 = sProgramMap;	 Catch:{ all -> 0x000f }
        r6 = r10.name;	 Catch:{ all -> 0x000f }
        r4.put(r6, r2);	 Catch:{ all -> 0x000f }
        r4 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x0051;
    L_0x002f:
        r4 = "Player/Ui/CarouselPlayerDataProvider";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x000f }
        r6.<init>();	 Catch:{ all -> 0x000f }
        r7 = "save the label of ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r7 = r10.name;	 Catch:{ all -> 0x000f }
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r7 = sProvider;	 Catch:{ all -> 0x000f }
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r6 = r6.toString();	 Catch:{ all -> 0x000f }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r4, r6);	 Catch:{ all -> 0x000f }
    L_0x0051:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        goto L_0x0006;
    L_0x0053:
        r4 = sProgramMap;	 Catch:{ all -> 0x000f }
        r4 = r4.entrySet();	 Catch:{ all -> 0x000f }
        r1 = r4.iterator();	 Catch:{ all -> 0x000f }
    L_0x005d:
        r4 = r1.hasNext();	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x0051;
    L_0x0063:
        r0 = r1.next();	 Catch:{ all -> 0x000f }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x000f }
        r3 = r0.getKey();	 Catch:{ all -> 0x000f }
        r3 = (java.lang.String) r3;	 Catch:{ all -> 0x000f }
        if (r3 != 0) goto L_0x0073;
    L_0x0071:
        monitor-exit(r5);	 Catch:{ all -> 0x000f }
        goto L_0x0006;
    L_0x0073:
        r4 = r10.name;	 Catch:{ all -> 0x000f }
        r4 = r4.equals(r3);	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x005d;
    L_0x007b:
        r2 = r0.getValue();	 Catch:{ all -> 0x000f }
        r2 = (java.util.List) r2;	 Catch:{ all -> 0x000f }
        if (r2 == 0) goto L_0x005d;
    L_0x0083:
        r2.clear();	 Catch:{ all -> 0x000f }
        r2.addAll(r9);	 Catch:{ all -> 0x000f }
        r4 = sProgramMap;	 Catch:{ all -> 0x000f }
        r6 = r10.name;	 Catch:{ all -> 0x000f }
        r4.put(r6, r2);	 Catch:{ all -> 0x000f }
        r4 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;	 Catch:{ all -> 0x000f }
        if (r4 == 0) goto L_0x005d;
    L_0x0094:
        r4 = "Player/Ui/CarouselPlayerDataProvider";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x000f }
        r6.<init>();	 Catch:{ all -> 0x000f }
        r7 = "update the label of ";
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r7 = r10.name;	 Catch:{ all -> 0x000f }
        r6 = r6.append(r7);	 Catch:{ all -> 0x000f }
        r6 = r6.toString();	 Catch:{ all -> 0x000f }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r4, r6);	 Catch:{ all -> 0x000f }
        goto L_0x005d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.player.ui.carousel.CarouselPlayerDataProvider.updateCarouselChannelProgram(java.util.List, com.gala.tvapi.tv2.model.TVChannelCarouselTag):void");
    }

    public List<CarouselChannelDetail> getCarouselChannelProgramList(TVChannelCarouselTag tvtag) {
        List<CarouselChannelDetail> list = null;
        synchronized (this.mLock2) {
            if (tvtag == null) {
            } else if (sProgramMap.containsKey(tvtag.name)) {
                for (Entry<String, List<CarouselChannelDetail>> entry : sProgramMap.entrySet()) {
                    CharSequence tagname = (String) entry.getKey();
                    if (!StringUtils.isEmpty(tagname)) {
                        if (tvtag.name.equals(tagname)) {
                            list = (List) entry.getValue();
                            break;
                        }
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d("Player/Ui/CarouselPlayerDataProvider", "tag is null");
                    }
                }
            } else {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d("Player/Ui/CarouselPlayerDataProvider", "map does not contain the label of " + tvtag.name + sProvider);
                }
            }
        }
        return list;
    }
}
