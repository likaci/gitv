package com.gala.video.lib.share.uikit.data;

import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import java.io.Serializable;
import java.util.HashMap;

public class ItemInfoModel implements Serializable {
    private BaseActionModel mActionModel;
    private HashMap<String, HashMap<String, String>> mCuteViewDatas;
    private short mHeight;
    private String mId;
    private short mItemType;
    private float mScale = 1.1f;
    private String mSkinEndsWith;
    private short mSpaceH;
    private short mSpaceV;
    private String mStyle;
    private short mWidth;

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public short getItemType() {
        return this.mItemType;
    }

    public void setItemType(short itemType) {
        this.mItemType = itemType;
    }

    public String getStyle() {
        return this.mStyle;
    }

    public void setStyle(String style) {
        this.mStyle = style;
    }

    public String getSkinEndsWith() {
        return this.mSkinEndsWith;
    }

    public void setSkinEndsWith(String skinEndsWith) {
        this.mSkinEndsWith = skinEndsWith;
    }

    public short getWidth() {
        return this.mWidth;
    }

    public void setWidth(short width) {
        this.mWidth = width;
    }

    public short getHeight() {
        return this.mHeight;
    }

    public void setHeight(short height) {
        this.mHeight = height;
    }

    public short getSpaceV() {
        return this.mSpaceV;
    }

    public void setSpaceV(short spaceV) {
        this.mSpaceV = spaceV;
    }

    public short getSpaceH() {
        return this.mSpaceH;
    }

    public void setSpaceH(short spaceH) {
        this.mSpaceH = spaceH;
    }

    public float getScale() {
        return this.mScale;
    }

    public void setScale(float scale) {
        this.mScale = scale;
    }

    public HashMap<String, HashMap<String, String>> getCuteViewDatas() {
        return this.mCuteViewDatas;
    }

    public void setCuteViewDatas(HashMap<String, HashMap<String, String>> cuteViewDatas) {
        if (this.mCuteViewDatas != null) {
            throw new IllegalArgumentException("iteminfomodel has a cuteViewDatas，so refuse new map! u can getCuteViewDatas():" + cuteViewDatas + ",model:" + this);
        }
        this.mCuteViewDatas = cuteViewDatas;
    }

    public String getCuteViewData(String id, String key) {
        if (this.mCuteViewDatas != null) {
            HashMap<String, String> stringStringHashMap = (HashMap) this.mCuteViewDatas.get(id);
            if (stringStringHashMap != null) {
                return (String) stringStringHashMap.get(key);
            }
        }
        return null;
    }

    public BaseActionModel getActionModel() {
        return this.mActionModel;
    }

    public void setActionModel(BaseActionModel actionModel) {
        this.mActionModel = actionModel;
    }

    public String toString() {
        StringBuilder sb = null;
        try {
            if (this.mCuteViewDatas != null) {
                StringBuilder sb2 = null;
                for (String id : this.mCuteViewDatas.keySet()) {
                    try {
                        if (sb2 == null) {
                            sb = new StringBuilder();
                        } else {
                            sb = sb2;
                        }
                        HashMap<String, String> stringStringHashMap = (HashMap) this.mCuteViewDatas.get(id);
                        sb.append("[id=").append(id);
                        for (String key : stringStringHashMap.keySet()) {
                            sb.append(";key=").append(key).append(",value=").append((String) stringStringHashMap.get(key));
                        }
                        sb2 = sb;
                    } catch (Exception e) {
                        sb = sb2;
                    }
                }
                sb = sb2;
            }
        } catch (Exception e2) {
        }
        return "ItemInfoModel{mId='" + this.mId + '\'' + ", mItemDataType=" + this.mItemType + ", mStyle='" + this.mStyle + '\'' + ", mWidth=" + this.mWidth + ", mHeight=" + this.mHeight + ",CuteViewData:" + sb + '}';
    }
}
