package com.gala.video.cloudui.view.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteView;
import java.io.Serializable;

public class QCloudViewInfoModel implements Serializable {
    public int bgPaddingBottom;
    public int bgPaddingLeft;
    public int bgPaddingRight;
    public int bgPaddingTop;
    public int contentHeight;
    public int contentWidth;
    public boolean focusable;
    public boolean focused;
    public Drawable itemBackground;
    public int itemHeight;
    public int itemWidth;
    public int ninePatchBorder;
    public int order;
    public CuteView[] viewMap;

    public QCloudViewInfoModel(CloudView cloudView, QCloudViewJsonModel viewJson) {
        if (viewJson == null) {
            Log.e("q/cloud/CloudViewInfoModel", "QCloudViewInfoModel is null , cause create CloudViewInfoModel error");
            return;
        }
        Context context = cloudView.getContext();
        this.order = viewJson.order;
        this.itemBackground = CloudUtils.getDrawableFromResidStr(context, viewJson.background);
        this.focusable = viewJson.focusable;
        this.bgPaddingLeft = CloudUtils.getDimenFromResidStr(context, viewJson.bgPaddingLeft);
        this.bgPaddingTop = CloudUtils.getDimenFromResidStr(context, viewJson.bgPaddingTop);
        this.bgPaddingRight = CloudUtils.getDimenFromResidStr(context, viewJson.bgPaddingRight);
        this.bgPaddingBottom = CloudUtils.getDimenFromResidStr(context, viewJson.bgPaddingBottom);
        this.viewMap = CloudUtils.getCloudViews(viewJson.texts, viewJson.images, this, cloudView);
    }
}
