package com.gala.speedrunner.netdoctor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.gala.speedrunner.netdoctor.a.a;
import com.gala.speedrunner.netdoctor.a.b;
import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.speedrunner.speedrunner.IRunCheckCallback;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.netdoc.FileType;
import com.netdoc.LiveTaskInfo;
import com.netdoc.TaskInfo;

public class TVNetDoctor implements ITVNetDoctor {
    private b a;
    private String f379a = "04022001010000000000";
    private boolean f380a = false;

    public void initNetDoctor(String uuid, String domain) {
        a.a().a(uuid, domain);
        if (!domain.equals(BuildDefaultDocument.APK_DOMAIN_NAME)) {
            this.f379a = "04022001010010000000";
        }
    }

    @SuppressLint({"NewApi"})
    public void checkPlay(Context context, Album album, boolean isVipUser, FileType fileType, String cookie, String uuid, int time, String rever) {
        int i = 1;
        if (album != null) {
            Log.d("SpeedRunner", "tvid=" + album.tvQid + ", vid=" + album.vid + ",qpid=" + album.qpId + ",live_channelId=" + album.live_channelId + ", islive = " + album.isLiveProgram());
            this.f380a = true;
            if (album.isLiveProgram()) {
                LiveTaskInfo liveTaskInfo = new LiveTaskInfo();
                liveTaskInfo.channelid = album.live_channelId;
                if (uuid == null) {
                    uuid = "";
                }
                liveTaskInfo.uid = uuid;
                liveTaskInfo.uuid = TVApiBase.getTVApiProperty().getPassportDeviceId();
                if (cookie == null) {
                    cookie = "";
                }
                liveTaskInfo.cookie = cookie;
                liveTaskInfo.vipRes = album.isPurchase > 0 ? 1 : 0;
                if (!isVipUser) {
                    i = 0;
                }
                liveTaskInfo.vipUser = i;
                liveTaskInfo.timepoint = time;
                liveTaskInfo.platformid = this.f379a;
                liveTaskInfo.bid = "5";
                a.a().a(liveTaskInfo, fileType);
                return;
            }
            int i2;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.platformid = this.f379a;
            taskInfo.tvid = album.tvQid;
            taskInfo.vid = album.vid;
            taskInfo.timepoint = time;
            taskInfo.aid = album.qpId;
            taskInfo.cid = String.valueOf(album.chnId);
            if (uuid == null) {
                uuid = "";
            }
            taskInfo.uid = uuid;
            if (album.isVipVideo()) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            taskInfo.vipRes = i2;
            if (!isVipUser) {
                i = 0;
            }
            taskInfo.vipUser = i;
            if (cookie == null) {
                cookie = "";
            }
            taskInfo.cookie = cookie;
            taskInfo.deviceid = TVApiBase.getTVApiProperty().getPassportDeviceId();
            taskInfo.bid = "5";
            if (taskInfo.vid == null || taskInfo.vid.isEmpty()) {
                taskInfo.vid = taskInfo.bid;
            }
            if (this.a != null) {
                this.a.f388a = taskInfo.vid;
            }
            a.a().a(taskInfo, fileType, rever);
        }
    }

    @SuppressLint({"NewApi"})
    public void checkPlay(Context context, Album album, boolean isVipUser, FileType fileType, String cookie, String uuid, int time, String bid, String sgti, String rever) {
        int i = 0;
        int i2 = 1;
        if (album != null) {
            Log.d("SpeedRunner", "tvid=" + album.tvQid + ", vid=" + album.vid + ",qpid=" + album.qpId + ",live_channelId=" + album.live_channelId + ", islive = " + album.isLiveProgram());
            this.f380a = true;
            if (album.isLiveProgram()) {
                LiveTaskInfo liveTaskInfo = new LiveTaskInfo();
                liveTaskInfo.channelid = album.live_channelId;
                liveTaskInfo.bid = bid;
                if (uuid == null) {
                    uuid = "";
                }
                liveTaskInfo.uid = uuid;
                liveTaskInfo.uuid = TVApiBase.getTVApiProperty().getPassportDeviceId();
                if (cookie == null) {
                    cookie = "";
                }
                liveTaskInfo.cookie = cookie;
                liveTaskInfo.vipRes = album.isPurchase > 0 ? 1 : 0;
                if (isVipUser) {
                    i = 1;
                }
                liveTaskInfo.vipUser = i;
                liveTaskInfo.timepoint = time;
                liveTaskInfo.platformid = this.f379a;
                if (this.a != null) {
                    this.a.b = liveTaskInfo.channelid;
                    this.a.f390a = true;
                }
                a.a().a(liveTaskInfo, fileType);
                return;
            }
            int i3;
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.platformid = this.f379a;
            taskInfo.tvid = album.tvQid;
            taskInfo.vid = album.vid;
            taskInfo.bid = bid;
            taskInfo.timepoint = time;
            taskInfo.aid = album.qpId;
            taskInfo.cid = String.valueOf(album.chnId);
            if (uuid == null) {
                uuid = "";
            }
            taskInfo.uid = uuid;
            if (album.isVipVideo()) {
                i3 = 1;
            } else {
                i3 = 0;
            }
            taskInfo.vipRes = i3;
            if (!isVipUser) {
                i2 = 0;
            }
            taskInfo.vipUser = i2;
            if (cookie == null) {
                cookie = "";
            }
            taskInfo.cookie = cookie;
            taskInfo.deviceid = TVApiBase.getTVApiProperty().getPassportDeviceId();
            taskInfo.sgti = sgti;
            if (taskInfo.vid == null || taskInfo.vid.isEmpty()) {
                taskInfo.vid = taskInfo.bid;
            }
            if (this.a != null) {
                this.a.f388a = taskInfo.vid;
            }
            a.a().a(taskInfo, fileType, rever);
        }
    }

    public void checkPlay(Context context, final FileType fileType, final int time, IOneAlbumProvider albumProvider, final String rever) {
        this.f380a = true;
        if (albumProvider != null) {
            albumProvider.pickOneAlbum(new com.gala.speedrunner.speedrunner.a(this) {
                private /* synthetic */ TVNetDoctor f381a;

                public final void onFailure(Exception exception) {
                    if (this.f381a.a != null && this.f381a.a.a != null) {
                        this.f381a.a.a.onFailed(null);
                    }
                }

                @SuppressLint({"NewApi"})
                public final void a(Album album) {
                    Log.d("SpeedRunner", album.tvQid + "------" + album.vid);
                    TaskInfo taskInfo = new TaskInfo();
                    taskInfo.tvid = album.tvQid;
                    taskInfo.vid = album.vid;
                    taskInfo.timepoint = time;
                    taskInfo.aid = album.qpId;
                    taskInfo.cid = String.valueOf(album.chnId);
                    taskInfo.uid = "";
                    taskInfo.vipRes = album.isVipVideo() ? 1 : 0;
                    taskInfo.vipUser = 0;
                    taskInfo.cookie = "";
                    taskInfo.platformid = this.f381a.a;
                    taskInfo.deviceid = TVApiBase.getTVApiProperty().getPassportDeviceId();
                    taskInfo.bid = "5";
                    if (taskInfo.vid == null || taskInfo.vid.isEmpty()) {
                        taskInfo.vid = taskInfo.bid;
                    }
                    if (this.f381a.a != null) {
                        this.f381a.a.f388a = taskInfo.vid;
                    }
                    a.a().a(taskInfo, fileType, rever);
                }
            });
        }
    }

    public void sendLogInfo(String info) {
        a.a().a(info);
    }

    public void stopPlay() {
        a.a().b(this.a);
        this.f380a = false;
    }

    public boolean isStart() {
        return this.f380a;
    }

    public void setSpeedListener(IRunCheckCallback callback) {
        b bVar = new b();
        bVar.f389a = a.a().a();
        bVar.a = callback;
        a.a().a(bVar);
        Log.d("SpeedRunner", "setListemer-id=" + bVar.f389a);
        this.a = bVar;
    }
}
