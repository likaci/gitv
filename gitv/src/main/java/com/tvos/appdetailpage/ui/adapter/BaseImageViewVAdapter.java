package com.tvos.appdetailpage.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tvos.appdetailpage.utils.ResourcesUtils;
import com.tvos.appdetailpage.utils.StringUtils;
import com.tvos.widget.VAdapter;

public abstract class BaseImageViewVAdapter extends VAdapter {
    protected String TAG = "BaseImageViewVAdapter";
    protected Context mContext;

    protected abstract void requestBitmapFailed();

    protected abstract void requestBitmapSucc();

    protected void loadBitmap(ImageView imageView, String imageUrl, int position, int placeholderResId) {
        if (StringUtils.isEmpty(imageUrl)) {
            Log.e("ViewAdapter", ">>>>>>>>>>>>>>ViewAdapter  loadBitmap -------imageUrl  = null or \"\" ! ");
            return;
        }
        Picasso.with(this.mContext).cancelRequest(imageView);
        Picasso.with(this.mContext).load(imageUrl).skipMemoryCache().fit().placeholder(placeholderResId).into(imageView, new Callback() {
            public void onError() {
                BaseImageViewVAdapter.this.requestBitmapFailed();
            }

            public void onSuccess() {
                BaseImageViewVAdapter.this.requestBitmapSucc();
            }
        });
    }

    protected void release() {
        this.mContext = null;
    }

    protected int getResId(String className, String name) {
        return ResourcesUtils.getResourceId(this.mContext, className, name);
    }

    protected int getDimen(int id) {
        return (int) this.mContext.getResources().getDimension(id);
    }
}
