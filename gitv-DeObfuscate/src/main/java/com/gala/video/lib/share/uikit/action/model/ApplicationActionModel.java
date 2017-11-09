package com.gala.video.lib.share.uikit.action.model;

import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.AppActionData;
import com.gala.video.lib.share.uikit.loader.data.AppStore;

public class ApplicationActionModel extends BaseActionModel<AppStore> {
    private AppActionData mData;

    public ApplicationActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(AppStore dataSource) {
        if (this.mData == null) {
            this.mData = new AppActionData();
        }
        if (dataSource.app_type == 2) {
            this.mData.setAppId(StringUtils.parse(dataSource.app_id, 0));
        }
        if (dataSource.app_type == 4) {
            this.mData.setAppDownloadUrl(dataSource.app_download_url);
        }
        this.mData.setApplicationType(dataSource.app_type);
        this.mData.setAppPackageName(dataSource.app_package_name);
        this.mData.setAppName(dataSource.app_name);
        return this;
    }

    public void setData(AppActionData data) {
        this.mData = data;
    }

    public AppActionData getData() {
        return this.mData;
    }
}
