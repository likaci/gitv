package com.gala.video.lib.share.ifimpl.netdiagnose.collection;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.AlbumInfoCheck;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.AlbumListCheck;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.CheckEntity;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.DeviceAutoCheck;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.PlayListChannelCheck;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.PlayListQipuCheck;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.check.PlayerAuthCheck;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CollectionTask {
    private static final String TAG = "CollectionTask";
    private CheckEntity mCheckEntity;
    private String mPingUrls;

    public CollectionTask() {
        this.mCheckEntity = new CheckEntity();
    }

    public CollectionTask(String pingUrls) {
        this();
        this.mPingUrls = pingUrls;
    }

    public void collection(ICheckInterfaceCallBack collectionCallBack) {
        pingNetWork();
        runInterfaceCheck(collectionCallBack);
    }

    public void pingNetWork() {
        this.mCheckEntity.add("---------------Ping Test---------------");
        LogUtils.i(TAG, "pingNetWork mPingUrls: " + this.mPingUrls);
        if (StringUtils.isTrimEmpty(this.mPingUrls)) {
            pingDefault();
        } else if (NetDiagnoseCheckTools.NO_CHECK_FLAG.equals(this.mPingUrls.trim())) {
            this.mCheckEntity.add("--------no ping check-------\r\n");
        } else {
            String[] urls = NetDiagnoseCheckTools.getParseUrls(this.mPingUrls);
            if (StringUtils.isEmpty(urls)) {
                pingDefault();
            } else {
                LogUtils.i(TAG, "onRun: use online ping domain");
                for (String url : urls) {
                    if (!StringUtils.isEmpty(url.trim())) {
                        LogUtils.i(TAG, "pingNetWork url: " + url);
                        pingStr(url.trim());
                    }
                }
            }
        }
        this.mCheckEntity.add("---------------\r\n");
    }

    private void pingDefault() {
        pingStr(PingConfig.DATA2_ITV);
        pingStr(PingConfig.CACHE_VIDEO);
        pingStr(PingConfig.CACHE_M);
        pingStr(PingConfig.PDATA_VIDEO);
        pingStr(PingConfig.ITV_VIDEO);
    }

    private void pingStr(String str) {
        try {
            String result;
            Process p = Runtime.getRuntime().exec("ping -c 2 -w 10 " + str);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            InputStream errorInput = p.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            BufferedReader errorIn = new BufferedReader(new InputStreamReader(errorInput));
            StringBuffer buffer = new StringBuffer();
            StringBuffer errorBuffer = new StringBuffer();
            String str2 = "";
            while (true) {
                str2 = in.readLine();
                if (str2 != null) {
                    buffer.append(str2).append("\n");
                } else {
                    while (true) {
                    }
                    if (status != 0) {
                        result = "success";
                    } else {
                        result = "failed";
                    }
                    this.mCheckEntity.add("@@@@ping " + str);
                    this.mCheckEntity.add("normalbuffer = \n" + buffer.toString());
                    this.mCheckEntity.add("errorBuffer = " + errorBuffer.toString());
                    this.mCheckEntity.add("status = " + status);
                    this.mCheckEntity.add("result = " + result + "\n");
                }
            }
            str2 = errorIn.readLine();
            if (str2 != null) {
                errorBuffer.append(str2).append("\n");
            } else {
                if (status != 0) {
                    result = "failed";
                } else {
                    result = "success";
                }
                this.mCheckEntity.add("@@@@ping " + str);
                this.mCheckEntity.add("normalbuffer = \n" + buffer.toString());
                this.mCheckEntity.add("errorBuffer = " + errorBuffer.toString());
                this.mCheckEntity.add("status = " + status);
                this.mCheckEntity.add("result = " + result + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    public void runInterfaceCheck(ICheckInterfaceCallBack checkInterfaceCallBack) {
        this.mCheckEntity.add("---------------Server Service Test---------------");
        try {
            if (new DeviceAutoCheck(this.mCheckEntity).runCheck()) {
                new AlbumListCheck(this.mCheckEntity).runCheck();
                new AlbumInfoCheck(this.mCheckEntity).runCheck();
                new PlayerAuthCheck(this.mCheckEntity).runCheck();
                if (new PlayListChannelCheck(this.mCheckEntity).runCheck() && new PlayListQipuCheck(this.mCheckEntity).runCheck()) {
                    this.mCheckEntity.add("---------------");
                    checkInterfaceCallBack.checkInterfaceSuccess(this.mCheckEntity.getStringBuffer().toString());
                    return;
                }
            }
            this.mCheckEntity.add("---------------");
            checkInterfaceCallBack.checkInterfaceFail(this.mCheckEntity.getStringBuffer().toString());
        } catch (Exception e) {
            e.printStackTrace();
            this.mCheckEntity.add("runInterfaceCheck happen exception ,ex = " + e.toString());
            this.mCheckEntity.add("---------------");
            checkInterfaceCallBack.checkInterfaceFail(this.mCheckEntity.getStringBuffer().toString());
        }
    }
}
