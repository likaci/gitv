package com.gala.tv.voice.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.gala.tv.voice.VoiceClient;
import com.gala.tv.voice.VoiceClient.ConnectionListener;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.VoiceEventFactory.PlayVoiceEvent;
import com.gala.tv.voice.VoiceEventGroup;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class VoiceTestServer extends Thread {
    Context a;
    private VoiceClient f429a;
    private MyHandler f430a = new MyHandler(this);
    private Thread f431a;
    private ServerSocket f432a;
    private List<VoiceEventModel> f433a = new ArrayList();

    class MyHandler extends Handler {
        private Toast a;
        private /* synthetic */ VoiceTestServer f435a;
        private String f436a = "";

        public void setToastMsg(String str) {
            this.f436a = str;
        }

        public MyHandler(VoiceTestServer voiceTestServer) {
            this.f435a = voiceTestServer;
        }

        public MyHandler(VoiceTestServer voiceTestServer, Looper looper) {
            this.f435a = voiceTestServer;
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            CharSequence charSequence = this.f436a;
            if (this.a == null) {
                this.a = Toast.makeText(this.f435a.a, charSequence, 1);
            } else {
                this.a.setText(charSequence);
            }
            this.a.show();
        }
    }

    class VoiceEventModel {
        int a;
        long f437a;
        String f438a;

        public VoiceEventModel(VoiceTestServer voiceTestServer, int i, String str, int i2) {
            this.a = i;
            this.f438a = str;
            this.f437a = (long) i2;
        }
    }

    static /* synthetic */ void a(VoiceTestServer voiceTestServer) throws InterruptedException {
        List supportedEvents = voiceTestServer.f429a.getSupportedEvents();
        if (supportedEvents == null || supportedEvents.isEmpty()) {
            Log.w("VoiceTestServer", "supported is Null or Empty");
        } else {
            int i = 0;
            for (int i2 = 0; i2 < supportedEvents.size(); i2++) {
                VoiceEventGroup voiceEventGroup = (VoiceEventGroup) supportedEvents.get(i2);
                if (voiceEventGroup == null || voiceEventGroup.getEvents().isEmpty()) {
                    Log.w("VoiceTestServer", "VoiceEventGroup is Null or Empty.---" + voiceEventGroup);
                } else {
                    int i3 = 0;
                    while (i3 < voiceEventGroup.getEvents().size()) {
                        int i4 = i + 1;
                        VoiceEvent voiceEvent = (VoiceEvent) voiceEventGroup.getEvents().get(i3);
                        voiceTestServer.a("语音列表[" + i2 + "][" + i3 + "]= 语音类型： " + a(voiceEvent.getType()) + "  ;语音关键字：" + voiceEvent.getKeyword());
                        Thread.sleep(2000);
                        Log.d("VoiceTestServer", "getSupportedVoiceEventsForAll[" + i2 + "][" + i3 + "]=  语音类型： " + a(voiceEvent.getType()) + "  ;语音关键字：" + voiceEvent.getKeyword() + "  ;voiceEvent = " + voiceEvent);
                        i3++;
                        i = i4;
                    }
                }
            }
            voiceTestServer.a("size====" + i);
            Thread.sleep(2000);
            Log.d("VoiceTestServer", "getSupportedVoiceEventsForAll() return size=" + i);
        }
        voiceTestServer.a("---测试结束---");
    }

    static /* synthetic */ void a(VoiceTestServer voiceTestServer, String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            voiceTestServer.a("发送的语音格式不正确！");
            return;
        }
        VoiceEvent createPlayEvent;
        int parseInt = Integer.parseInt(strArr[0]);
        if (parseInt == 16) {
            createPlayEvent = VoiceEventFactory.createPlayEvent(strArr[1]);
            if (strArr.length > 2) {
                ((PlayVoiceEvent) createPlayEvent).setChannelName(strArr[2]);
                if (strArr.length > 3) {
                    ((PlayVoiceEvent) createPlayEvent).setEpisodeIndex(Integer.parseInt(strArr[3]));
                }
            }
        } else {
            createPlayEvent = VoiceEventFactory.createVoiceEvent(parseInt, strArr[1]);
        }
        voiceTestServer.a("语音类型： " + a(createPlayEvent.getType()) + "  ;语音关键字：" + createPlayEvent.getKeyword());
        Log.d("VoiceTestServer", "sendVoiceEvent() event = " + createPlayEvent);
        Log.d("VoiceTestServer", "sendVoiceEvent() result =  " + voiceTestServer.f429a.dispatchVoiceEvent(createPlayEvent));
    }

    static /* synthetic */ void b(VoiceTestServer voiceTestServer) throws InterruptedException {
        List supportedEventGroupsByActivity = VoiceManager.instance().getSupportedEventGroupsByActivity();
        if (supportedEventGroupsByActivity == null || supportedEventGroupsByActivity.isEmpty()) {
            Log.w("VoiceTestServer", "supported is Null or Empty");
        } else {
            int i = 0;
            for (int i2 = 0; i2 < supportedEventGroupsByActivity.size(); i2++) {
                VoiceEventGroup voiceEventGroup = (VoiceEventGroup) supportedEventGroupsByActivity.get(i2);
                if (voiceEventGroup == null || voiceEventGroup.getEvents().isEmpty()) {
                    Log.w("VoiceTestServer", "VoiceEventGroup is Null or Empty.---" + voiceEventGroup);
                } else {
                    int i3 = 0;
                    while (i3 < voiceEventGroup.getEvents().size()) {
                        int i4 = i + 1;
                        VoiceEvent voiceEvent = (VoiceEvent) voiceEventGroup.getEvents().get(i3);
                        voiceTestServer.a("当前页面支持的语音列表[" + i2 + "][" + i3 + "]= 语音类型： " + a(voiceEvent.getType()) + "  ;语音关键字：" + voiceEvent.getKeyword());
                        Thread.sleep(2000);
                        Log.d("VoiceTestServer", "getSupportedVoiceEventsByActivity[" + i2 + "][" + i3 + "]=  语音类型： " + a(voiceEvent.getType()) + "  ;语音关键字：" + voiceEvent.getKeyword() + "  ;voiceEvent = " + voiceEvent);
                        i3++;
                        i = i4;
                    }
                }
            }
            voiceTestServer.a("size====" + i);
            Thread.sleep(2000);
            Log.d("VoiceTestServer", "getSupportedVoiceEventsByActivity() return size=" + i);
        }
        voiceTestServer.a("---测试结束---");
    }

    public VoiceTestServer(Context context, String str) {
        this.a = context;
        VoiceClient.initialize(context, str);
        this.f429a = VoiceClient.instance();
        this.f429a.setListener(new ConnectionListener() {
            public void onDisconnected(int i) {
                Log.d("VoiceTestServer", "onDisconnected, code=" + i);
            }

            public void onConnected() {
                Log.d("VoiceTestServer", "onConnected");
            }
        });
        this.f429a.connect();
        try {
            this.f432a = new ServerSocket(4700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r7 = this;
        r2 = 0;
        r1 = r2;
    L_0x0002:
        r0 = r7.isInterrupted();	 Catch:{ all -> 0x0086 }
        if (r0 != 0) goto L_0x0066;
    L_0x0008:
        r0 = r7.f432a;	 Catch:{ all -> 0x0086 }
        if (r0 == 0) goto L_0x0066;
    L_0x000c:
        r0 = r7.f432a;	 Catch:{ all -> 0x0086 }
        r3 = r0.accept();	 Catch:{ all -> 0x0086 }
        r1 = new java.io.BufferedReader;	 Catch:{ all -> 0x008b }
        r0 = new java.io.InputStreamReader;	 Catch:{ all -> 0x008b }
        r4 = r3.getInputStream();	 Catch:{ all -> 0x008b }
        r0.<init>(r4);	 Catch:{ all -> 0x008b }
        r1.<init>(r0);	 Catch:{ all -> 0x008b }
        r0 = r1.readLine();	 Catch:{ all -> 0x006f }
        r2 = "VoiceTestServer";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006f }
        r5 = "line = ";
        r4.<init>(r5);	 Catch:{ all -> 0x006f }
        r4 = r4.append(r0);	 Catch:{ all -> 0x006f }
        r4 = r4.toString();	 Catch:{ all -> 0x006f }
        android.util.Log.d(r2, r4);	 Catch:{ all -> 0x006f }
        r2 = r7.f431a;	 Catch:{ all -> 0x006f }
        if (r2 == 0) goto L_0x0057;
    L_0x003e:
        r2 = r7.f431a;	 Catch:{ all -> 0x006f }
        r2 = r2.isAlive();	 Catch:{ all -> 0x006f }
        if (r2 == 0) goto L_0x0057;
    L_0x0046:
        r2 = "VoiceTestServer";
        r4 = "mThread != null && mThread.isAlive()--mThread.interrupt()";
        android.util.Log.d(r2, r4);	 Catch:{ all -> 0x006f }
        r2 = r7.f431a;	 Catch:{ all -> 0x006f }
        r2.interrupt();	 Catch:{ all -> 0x006f }
        r2 = 0;
        r7.f431a = r2;	 Catch:{ all -> 0x006f }
    L_0x0057:
        r2 = new com.gala.tv.voice.service.VoiceTestServer$2;	 Catch:{ all -> 0x006f }
        r2.<init>(r7, r0);	 Catch:{ all -> 0x006f }
        r7.f431a = r2;	 Catch:{ all -> 0x006f }
        r0 = r7.f431a;	 Catch:{ all -> 0x006f }
        r0.start();	 Catch:{ all -> 0x006f }
        r2 = r1;
        r1 = r3;
        goto L_0x0002;
    L_0x0066:
        a(r2);	 Catch:{ IOException -> 0x007a }
        if (r1 == 0) goto L_0x006e;
    L_0x006b:
        r1.close();	 Catch:{ IOException -> 0x007a }
    L_0x006e:
        return;
    L_0x006f:
        r0 = move-exception;
        r2 = r3;
    L_0x0071:
        a(r1);	 Catch:{ IOException -> 0x007a }
        if (r2 == 0) goto L_0x0079;
    L_0x0076:
        r2.close();	 Catch:{ IOException -> 0x007a }
    L_0x0079:
        throw r0;	 Catch:{ IOException -> 0x007a }
    L_0x007a:
        r0 = move-exception;
        r1 = "VoiceTestServer";
        r0 = r0.toString();
        android.util.Log.e(r1, r0);
        goto L_0x006e;
    L_0x0086:
        r0 = move-exception;
        r6 = r2;
        r2 = r1;
        r1 = r6;
        goto L_0x0071;
    L_0x008b:
        r0 = move-exception;
        r1 = r2;
        r2 = r3;
        goto L_0x0071;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.tv.voice.service.VoiceTestServer.run():void");
    }

    final void a() throws InterruptedException {
        this.f433a.clear();
        this.f433a.add(new VoiceEventModel(this, 4, "电视剧", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "最新", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "最热", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "向右", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "向右", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "向左", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "确定", 5000));
        this.f433a.add(new VoiceEventModel(this, 10, "1", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "开启跳过片头片尾", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "第2集", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "关闭跳过片头片尾", 5000));
        this.f433a.add(new VoiceEventModel(this, 10, "3", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "开启跳过片头片尾", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "下一集", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "上一集", 10000));
        this.f433a.add(new VoiceEventModel(this, 15, "-1", 10000));
        this.f433a.add(new VoiceEventModel(this, 15, "1", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "流畅", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "高清", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "收藏", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "取消收藏", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "最后一集", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "退出", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "打开", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "打开", 5000));
        this.f433a.add(new VoiceEventModel(this, 10, "1", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "开启跳过片头片尾", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "第2集", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "关闭跳过片头片尾", 5000));
        this.f433a.add(new VoiceEventModel(this, 10, "3", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "开启跳过片头片尾", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "下一集", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "上一集", 10000));
        this.f433a.add(new VoiceEventModel(this, 15, "-1", 10000));
        this.f433a.add(new VoiceEventModel(this, 15, "1", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "流畅", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "高清", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "收藏", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "取消收藏", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "收藏", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "最后一集", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "退出", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "打开", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "打开", 5000));
        this.f433a.add(new VoiceEventModel(this, 10, "1", 5000));
        this.f433a.add(new VoiceEventModel(this, 1, String.valueOf(600000), 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "暂停", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "播放", 10000));
        this.f433a.add(new VoiceEventModel(this, 4, "快进", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "快进", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "快退", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "快退", 5000));
        this.f433a.add(new VoiceEventModel(this, 2, String.valueOf(UIKitConfig.ITEM_TYPE_HEADER), 5000));
        this.f433a.add(new VoiceEventModel(this, 2, String.valueOf(-300000), 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "返回", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "取消收藏", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "返回", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "返回", 5000));
        this.f433a.add(new VoiceEventModel(this, 3, "锦绣未央", 5000));
        this.f433a.add(new VoiceEventModel(this, 4, "返回", 5000));
        List list = this.f433a;
        if (this.f433a == null || this.f433a.size() == 0) {
            a("自动测试列表为空！");
        } else {
            for (VoiceEventModel voiceEventModel : this.f433a) {
                a("语音类型： " + a(voiceEventModel.a) + "  ;语音关键字：" + voiceEventModel.f438a);
                Log.d("VoiceTestServer", "autoTest---语音类型： " + a(voiceEventModel.a) + "  ;语音关键字：" + voiceEventModel.f438a);
                Log.d("VoiceTestServer", "autoTest---handledResult = " + this.f429a.dispatchVoiceEvent(VoiceEventFactory.createVoiceEvent(voiceEventModel.a, voiceEventModel.f438a)));
                Thread.sleep(voiceEventModel.f437a);
            }
        }
        a("---测试结束---");
    }

    private static String a(int i) {
        String str = "UNKNOW";
        switch (i) {
            case 1:
                return "TYPE_SEEK_TO";
            case 2:
                return "TYPE_SEEK_OFFSET";
            case 3:
                return "TYPE_SEARCH";
            case 4:
                return "TYPE_KEYWORDS";
            case 10:
                return "TYPE_EPISODE_INDEX";
            case 15:
                return "TYPE_EPISODE_DIRECTION";
            default:
                return str;
        }
    }

    private void a(String str) {
        this.f430a.setToastMsg(str);
        this.f430a.sendEmptyMessage(0);
    }

    private static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
