package com.gala.video.app.epg.home.data;

import com.gala.tvapi.tv2.model.ResId;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.devcecheck.IDeviceCheckProxy.Wrapper;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import java.util.List;

public class DeviceCheckProxy extends Wrapper {
    public List<ResId> getHomeResId() {
        return DeviceCheckModel.getInstance().getHomeResId();
    }

    public boolean isDevCheckPass() {
        return DeviceCheckModel.getInstance().isDevCheckPass();
    }
}
