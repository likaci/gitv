package com.gala.video.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.widget.adapter.ViewAdapter;
import com.gala.video.widget.test.R;
import com.gala.video.widget.util.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class PortraitVideoAdapter extends ViewAdapter<GalleryData> {
    private static final int ITEM_ID_BASE = 88609620;
    private static final String TAG = "gridpageview/PortraitVideoAdapter";
    private static Bitmap sDefaultBg;
    private Context mContext;
    private List<GalleryData> mDatas = new ArrayList();

    public class BaseViewHolder {
        public int holderId;
        public AbsGalleryPagerItem itemLayout;

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("BaseViewHolder@").append(hashCode());
            builder.append("{");
            builder.append("holderId=").append(this.holderId);
            builder.append("itemLayout=").append(this.itemLayout);
            builder.append("}");
            return builder.toString();
        }
    }

    public PortraitVideoAdapter(Context context) {
        this.mContext = context;
        initDefaultBg(context);
    }

    private static synchronized void initDefaultBg(Context context) {
        synchronized (PortraitVideoAdapter.class) {
            if (sDefaultBg == null) {
                sDefaultBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_gallery_item_port_default);
            }
        }
    }

    private View initConvertView(int position, ViewGroup parent) {
        LogUtils.m1598d(TAG, "initConvertView: pos=" + position);
        BaseViewHolder holder = new BaseViewHolder();
        GalleryPagerItemPort convertView = new GalleryPagerItemPort(this.mContext);
        holder.itemLayout = convertView;
        holder.itemLayout.setId(position);
        convertView.setTag(holder);
        return convertView;
    }

    private void updateData(BaseViewHolder holder, int position) {
        GalleryData data = (GalleryData) this.mDatas.get(position);
        holder.itemLayout.setText(data.getName());
        LogUtils.m1598d(TAG, "updateData: id=" + data.getId() + ", name=" + data.getName());
        holder.itemLayout.setImageBitmap(sDefaultBg);
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public Object getItem(int position) {
        return this.mDatas.get(position);
    }

    public long getItemId(int position) {
        return (long) (ITEM_ID_BASE + position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = initConvertView(position, parent);
        }
        BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
        int oldId = holder.holderId;
        int newId = extractHolderId(position);
        LogUtils.m1598d(TAG, "[" + hashCode() + AlbumEnterFactory.SIGN_STR + "getView(" + position + "): old/new holder Id=" + oldId + "/" + newId);
        if (oldId != newId || newId == -1) {
            holder.holderId = newId;
            updateData(holder, position);
        }
        return convertView;
    }

    protected int extractHolderId(int position) {
        if (position < 0 || position >= this.mDatas.size()) {
            return -1;
        }
        return ((GalleryData) this.mDatas.get(position)).getId();
    }

    public void notifyDataSetChanged(List<GalleryData> datas) {
        LogUtils.m1598d(TAG, "[" + hashCode() + "] notifyDataSetChanged: datas=" + datas);
        this.mDatas.clear();
        if (datas != null && datas.size() > 0) {
            this.mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }
}
