package com.tvos.appdetailpage.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tvos.appdetailpage.info.RecommendApp;
import com.tvos.appdetailpage.utils.TagKeyUtil;
import java.util.ArrayList;

public class AppDetailRecommdAdapter extends BaseImageViewVAdapter {
    public static int FIRST_ITEM = TagKeyUtil.generateTagKey();
    public static int LAST_ITEM = TagKeyUtil.generateTagKey();
    public static final int TAG_POSITION = TagKeyUtil.generateTagKey();
    protected String TAG = "AppDetailRecommdAdapter";
    protected Handler mHandler = new Handler();
    private LayoutInflater mLayoutInflater;
    private OnClickListener mOnItemClickedListener;
    private ArrayList<RecommendApp> mRecommApps;

    class ViewHolder {
        TextView appsDownloadTextView;
        TextView appsNameTextView;
        ImageView iconImageView;

        ViewHolder() {
        }
    }

    public AppDetailRecommdAdapter(Context mContext, Drawable defaultDrawable, ArrayList<RecommendApp> mRecommApps, OnClickListener mOnItemClickedListener) {
        this.mContext = mContext;
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mRecommApps = mRecommApps;
        this.TAG = "AppDetailRecommdAdapter";
        this.mOnItemClickedListener = mOnItemClickedListener;
    }

    public void release() {
        super.release();
        this.mContext = null;
        if (this.mRecommApps != null) {
            this.mRecommApps.clear();
            this.mRecommApps = null;
        }
        this.mLayoutInflater = null;
        this.mHandler = null;
        this.mOnItemClickedListener = null;
    }

    public View getView(int position, ViewGroup parent) {
        ViewHolder viewHolder;
        View convertView = parent.getChildAt(position);
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(getResId("layout", "apps_appdetail_recommend"), null);
            viewHolder = new ViewHolder();
            setView(convertView, position, viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.appsDownloadTextView.setText(((RecommendApp) this.mRecommApps.get(position)).total_download);
        viewHolder.appsNameTextView.setText(((RecommendApp) this.mRecommApps.get(position)).name);
        fillData(convertView, position);
        if (position == 0) {
            convertView.setId(FIRST_ITEM);
            convertView.setNextFocusLeftId(FIRST_ITEM);
        } else if (position == this.mRecommApps.size() - 1) {
            convertView.setId(LAST_ITEM);
            convertView.setNextFocusRightId(LAST_ITEM);
        }
        return convertView;
    }

    private void setView(View convertView, int position, ViewHolder viewHolder) {
        viewHolder.appsDownloadTextView = (TextView) convertView.findViewById(getResId("id", "apps_download_recommend"));
        viewHolder.appsNameTextView = (TextView) convertView.findViewById(getResId("id", "apps_name_recommend"));
        viewHolder.iconImageView = (ImageView) convertView.findViewById(getResId("id", "recomm_apps_icon"));
        convertView.setFocusable(true);
        convertView.setTag(viewHolder);
        convertView.setTag(TAG_POSITION, Integer.valueOf(position));
        convertView.setOnClickListener(this.mOnItemClickedListener);
        convertView.setVisibility(0);
    }

    private void fillData(View convertView, int position) {
        loadBitmap((ImageView) convertView.findViewById(getResId("id", "recomm_apps_icon")), ((RecommendApp) this.mRecommApps.get(position)).logo_url, position, getResId("drawable", "apps_image_default_bg"));
    }

    public int getCount() {
        return this.mRecommApps.size();
    }

    protected void requestBitmapSucc() {
    }

    protected void requestBitmapFailed() {
    }

    public void onScrollItemIn(int arg0, View arg1) {
    }

    public void onScrollItemOut(int arg0, View arg1) {
    }
}
