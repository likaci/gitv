package com.gala.video.lib.share.ifimpl.openplay.service;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.impl.Command;
import com.qiyi.tv.client.impl.Params.TargetType;

public class ServerCommand<T> extends Command {
    private IAccessWatcher mAccessWatcher;
    private boolean mNeedNetwork;

    public ServerCommand(Context context, int target, int operation, int dataType) {
        super(context, target, operation, dataType);
    }

    public final Bundle process(Bundle params) {
        PingBackInitParams initParams = PingBack.getInstance().getPingbackInitParams();
        initParams.sChannel = Project.getInstance().getBuild().getCustomerName();
        initParams.sEnterMode = TargetType.TARGET_HOME_TAB == getTarget() ? "0" : "1";
        PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), initParams);
        return onProcess(params);
    }

    protected Bundle onProcess(Bundle inParams) {
        return null;
    }

    protected void setNeedNetwork(boolean network) {
        this.mNeedNetwork = network;
    }

    public boolean isNeedNetWork() {
        return this.mNeedNetwork;
    }

    public void setWatcher(IAccessWatcher token) {
        this.mAccessWatcher = token;
    }

    public void replaceWatcherProcess(int pid) {
        if (this.mAccessWatcher != null) {
            this.mAccessWatcher.replace(pid);
        }
    }

    protected boolean isAllowedAccess() {
        if (this.mAccessWatcher != null) {
            return this.mAccessWatcher.isAllowedAccess();
        }
        return true;
    }

    protected void increaseAccessCount() {
        if (this.mAccessWatcher != null) {
            this.mAccessWatcher.increaseAccessCount();
        }
    }
}
