package com.gala.video.cloudui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.SparseArray;
import com.gala.video.cloudui.view.model.QCloudViewInfoModel;

public abstract class CuteView {
    public CloudView cloudView;
    public Context context;
    public String id;
    public SparseArray<Object> keyedTags;
    public int order;
    public Object tag;

    protected abstract void draw(Canvas canvas);

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
        a();
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setTag(int key, Object tag) {
        if ((key >>> 24) < 2) {
            throw new IllegalArgumentException("The key must be an unique resId of R.class, the same way as View.class");
        }
        if (this.keyedTags == null) {
            this.keyedTags = new SparseArray(2);
        }
        this.keyedTags.put(key, tag);
    }

    public Object getTag() {
        return this.tag;
    }

    public Object getTag(int key) {
        if (this.keyedTags != null) {
            return this.keyedTags.get(key);
        }
        return null;
    }

    public CloudView getCloudView() {
        return this.cloudView;
    }

    public void setCloudView(CloudView mCloudView) {
        this.cloudView = mCloudView;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context mContext) {
        this.context = mContext;
    }

    public Handler getHandler() {
        if (this.cloudView != null) {
            return this.cloudView.getHandler();
        }
        return null;
    }

    public QCloudViewInfoModel getInfoModel() {
        if (this.cloudView != null) {
            return this.cloudView.infoModel;
        }
        return null;
    }

    public void setInfoModel(QCloudViewInfoModel mInfoModel) {
        if (this.cloudView != null) {
            this.cloudView.infoModel = mInfoModel;
        }
    }

    public void invalidate() {
        if (this.cloudView != null) {
            this.cloudView.invalidate();
        }
    }

    private void a() {
        QCloudViewInfoModel infoModel = getInfoModel();
        if (infoModel != null) {
            CloudUtils.a(infoModel.viewMap);
            invalidate();
        }
    }

    protected void drawImageBylimit(Drawable drawable, Canvas canvas, int left, int top, int right, int bottom, boolean clipCanvas) {
        if (canvas != null && getInfoModel() != null && drawable != null) {
            int left2 = getInfoModel().ninePatchBorder;
            int top2 = getInfoModel().ninePatchBorder;
            int right2 = left2 + getInfoModel().contentWidth;
            int bottom2 = left2 + getInfoModel().contentHeight;
            if (clipCanvas) {
                if (left < left2) {
                    left = left2;
                }
                if (right > right2) {
                    right = right2;
                }
                if (top < top2) {
                    top = top2;
                }
                if (bottom > bottom2) {
                    bottom = bottom2;
                }
            }
            drawable.setBounds(left, top, right, bottom);
            canvas.save();
            canvas.clipRect(left, top, right, bottom);
            drawable.draw(canvas);
            canvas.restore();
        }
    }

    protected boolean isViewVisible() {
        return this.cloudView != null && this.cloudView.getVisibility() == 0;
    }
}
