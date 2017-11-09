package com.gala.video.app.epg.home.component;

import android.content.Context;
import com.gala.video.app.epg.home.component.item.TitleInItem;
import com.gala.video.app.epg.home.component.item.TitleOutItem;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Widget {
    public static final int INVISIBLE = 0;
    public static final int PARTIAL = 1;
    private static final String TAG = "Widget";
    public static final int VISIBLE = 2;
    protected static final List<Widget> removeList = new CopyOnWriteArrayList();
    protected int indexInParent = -1;
    private ViewAttachInfo mAttatchInfo;
    private WidgetChangeStatus mChangeStatus = WidgetChangeStatus.InitChange;
    private boolean mCreated;
    private DataSource mDataSource;
    private boolean mIsStandard = true;
    protected WidgetTree mParent;
    private PingbackDataSource mPingback = new PingbackDataSource();
    private int visibilityInParent;

    public abstract Object buildUI(Context context);

    public abstract Object updateUI();

    public final void create() {
        dispatchCreate();
    }

    void dispatchCreate() {
        if (!this.mCreated) {
            onCreate();
            this.mCreated = true;
        }
    }

    public final void destroy() {
        dispatchDestroy();
    }

    void dispatchDestroy() {
        if (this.mCreated) {
            this.mCreated = false;
            this.mPingback.mData = null;
            this.mPingback.mAttachInfo = null;
            onDestroy();
        }
    }

    protected void onCreate() {
    }

    public PingbackDataSource getPingbackDataSource() {
        return this.mPingback;
    }

    public void onDestroy() {
    }

    public final int indexInParent() {
        return this.indexInParent;
    }

    public ViewAttachInfo getViewAttachInfo() {
        return this.mAttatchInfo;
    }

    public void setViewAttachInfo(ViewAttachInfo viewAttachInfo) {
        this.mAttatchInfo = viewAttachInfo;
        this.mPingback.mAttachInfo = viewAttachInfo;
    }

    public void setDataSource(DataSource dataSource) {
        this.mDataSource = dataSource;
        this.mPingback.mData = this.mDataSource;
    }

    public DataSource getDataSource() {
        return this.mDataSource;
    }

    public void setChanged(WidgetChangeStatus changeStatus) {
        this.mChangeStatus = changeStatus;
    }

    public WidgetChangeStatus getChangeStatus() {
        return this.mChangeStatus;
    }

    public void onEvent(int event, Object arg) {
    }

    public boolean isVisibleToUser() {
        return this.mParent != null && this.mParent.isVisibleToUser() && this.visibilityInParent > 0;
    }

    @Deprecated
    public boolean isVisibleInParent() {
        return this.visibilityInParent > 0;
    }

    public void setStandardType(boolean standard) {
        this.mIsStandard = standard;
    }

    public boolean getStandardType() {
        return this.mIsStandard;
    }

    protected int getVisibilityInParent() {
        return this.visibilityInParent;
    }

    protected void setVisibilityInParent(int visibility) {
        this.visibilityInParent = visibility;
    }

    public static Widget createWidget(int widgettype) {
        switch (widgettype) {
            case 2:
            case 3:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 14:
            case 16:
            case 17:
            case 18:
            case 31:
            case WidgetType.ITEM_TITLE_OUT /*267*/:
                return new TitleOutItem(widgettype);
            case WidgetType.ITEM_TITLE_IN /*266*/:
                return new TitleInItem(widgettype);
            default:
                LogUtils.m1571e(TAG, "widgettype error is type:" + widgettype);
                return null;
        }
    }

    public WidgetTree getParent() {
        return this.mParent;
    }
}
