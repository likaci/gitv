package com.gala.video.app.epg.home.data.hdata;

import android.util.SparseIntArray;
import com.gala.video.app.epg.home.data.core.HomeDataTaskFactory;
import com.gala.video.app.epg.home.utils.AppStoreUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;

public class DataRequestRouter {
    private static final int APPLICATION_GROUP = 8195;
    private static final int DEVICE_CHECK = 8192;
    private static final int DYNAMIC = 8194;
    private static final int GROUP_RESOURCE = 4098;
    private static final int HOME_GROUP = 4099;
    private static final int NETWORK = 8193;
    private static final int REFRESH_TIME = 4097;
    private static final int TABINFO = 4096;
    private static final String TAG = "DataRequestRouter";
    public static DataRequestRouter sInstance = new DataRequestRouter();
    private SparseIntArray mNextTaskRouter = new SparseIntArray();

    public void startBootUpDataRequest(boolean isHome) {
        this.mNextTaskRouter.put(NETWORK, APPLICATION_GROUP);
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpDeviceCheckTaskAction(8192));
        if (isHome) {
            DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpHomeNetWorkCheckTaskAction());
        } else {
            DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpNetWorkCheckTaskAction(NETWORK));
        }
    }

    public void startHomeDataRequest() {
        this.mNextTaskRouter.put(4096, 4097);
        this.mNextTaskRouter.put(4097, 4099);
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpTabInfoTaskAction(4096));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void next(int r8) {
        /*
        r7 = this;
        r6 = 4097; // 0x1001 float:5.741E-42 double:2.024E-320;
        r5 = -1;
        monitor-enter(r7);
        r2 = r7.mNextTaskRouter;	 Catch:{ all -> 0x0043 }
        r3 = -1;
        r1 = r2.get(r8, r3);	 Catch:{ all -> 0x0043 }
        r2 = "DataRequestRouter";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0043 }
        r3.<init>();	 Catch:{ all -> 0x0043 }
        r4 = "pre task id : ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0043 }
        r3 = r3.append(r8);	 Catch:{ all -> 0x0043 }
        r4 = ",next task id : ";
        r3 = r3.append(r4);	 Catch:{ all -> 0x0043 }
        r3 = r3.append(r1);	 Catch:{ all -> 0x0043 }
        r3 = r3.toString();	 Catch:{ all -> 0x0043 }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r2, r3);	 Catch:{ all -> 0x0043 }
        if (r1 != r5) goto L_0x0034;
    L_0x0032:
        monitor-exit(r7);
        return;
    L_0x0034:
        switch(r1) {
            case 4097: goto L_0x004a;
            case 4098: goto L_0x0037;
            case 4099: goto L_0x0058;
            case 8195: goto L_0x0046;
            default: goto L_0x0037;
        };
    L_0x0037:
        r2 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        if (r8 == r2) goto L_0x0032;
    L_0x003b:
        if (r8 == r6) goto L_0x0032;
    L_0x003d:
        r2 = r7.mNextTaskRouter;	 Catch:{ all -> 0x0043 }
        r2.delete(r8);	 Catch:{ all -> 0x0043 }
        goto L_0x0032;
    L_0x0043:
        r2 = move-exception;
        monitor-exit(r7);
        throw r2;
    L_0x0046:
        r7.executeApplicationGroupTasks();	 Catch:{ all -> 0x0043 }
        goto L_0x0037;
    L_0x004a:
        r2 = 4097; // 0x1001 float:5.741E-42 double:2.024E-320;
        r0 = com.gala.video.app.epg.home.data.core.HomeDataTaskFactory.makeUpRefreshTimeTaskAction(r2);	 Catch:{ all -> 0x0043 }
        r2 = com.gala.video.app.epg.home.data.hdata.DataRequestTaskStrategy.getInstance();	 Catch:{ all -> 0x0043 }
        r2.addTask(r0);	 Catch:{ all -> 0x0043 }
        goto L_0x0037;
    L_0x0058:
        r7.executeHomeGroupTasks();	 Catch:{ all -> 0x0043 }
        goto L_0x0037;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.home.data.hdata.DataRequestRouter.next(int):void");
    }

    private void executeHomeGroupTasks() {
        LogUtils.m1568d(TAG, "execute home data request");
        boolean isOpenCarousel = Project.getInstance().getControl().isOpenCarousel();
        LogUtils.m1568d(TAG, "is open carousel = " + isOpenCarousel);
        if (isOpenCarousel) {
            DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpCarouselChannelTaskAction());
        }
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpDailyNewsRequestTaskAction());
        switch (AppStoreUtils.checkApps()) {
            case 21:
                DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpAppStoreTaskAction());
                break;
            case 22:
                DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpAppOperateTaskAction());
                break;
        }
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpHomeMenuTaskAction());
    }

    private void executeApplicationGroupTasks() {
        LogUtils.m1568d(TAG, "execute application data request");
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpDynamicTaskAction());
        if (Project.getInstance().getBuild().isNewAppUpgrade()) {
            DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpNewAppUpgradeTaskAction());
        } else {
            DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpAppUpgradeTaskAction());
        }
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpChannelRequestTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpServerTimeRequestTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpErrorCodeDownLoadTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpIntelligentSearchTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpScreenSaverOperateImageRequestTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpExitOperateImageRequestTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpStartOperateImageRequestTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpPluginApkUpgradeTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpBackgroundTaskAction());
        DataRequestTaskStrategy.getInstance().addTask(HomeDataTaskFactory.makeUpPromotionAppRequestTaskAction());
    }
}
