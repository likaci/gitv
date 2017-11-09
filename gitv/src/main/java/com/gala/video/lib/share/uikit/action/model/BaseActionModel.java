package com.gala.video.lib.share.uikit.action.model;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import java.io.Serializable;

public class BaseActionModel<D> implements Serializable {
    protected static boolean sDebugable;
    protected final String TAG = getClass().getSimpleName();
    protected boolean mCanAction = true;
    protected transient ItemDataType mItemDataType;
    private String mItemTypeValue;

    static {
        boolean z = true;
        if (SysPropUtils.getInt("log.action.debug", 0) != 1) {
            z = false;
        }
        sDebugable = z;
    }

    public BaseActionModel(ItemDataType itemDataType) {
        this.mItemDataType = itemDataType;
        this.mItemTypeValue = itemDataType.getValue();
    }

    public ItemDataType getItemType() {
        if (this.mItemDataType != null) {
            if (sDebugable) {
                LogUtils.d(this.TAG, "BaseActionModel dataType = " + this.mItemTypeValue);
            }
            return this.mItemDataType;
        }
        this.mItemDataType = ItemDataType.getItemTypeByValue(this.mItemTypeValue);
        return this.mItemDataType;
    }

    public BaseActionModel buildActionModel(D d) {
        return this;
    }

    public void onItemClick(Context context) {
        if (getItemType() != null) {
            GetInterfaceTools.getIActionJump().onItemClick(context, this);
        }
    }

    public void setCanAction(boolean canAction) {
        this.mCanAction = canAction;
    }

    public boolean isCanAction() {
        return this.mCanAction;
    }

    public String toString() {
        return "BaseActionModel{mItemTypeValue=" + this.mItemTypeValue + ", mItemDataType=" + getItemType() + '}';
    }
}
