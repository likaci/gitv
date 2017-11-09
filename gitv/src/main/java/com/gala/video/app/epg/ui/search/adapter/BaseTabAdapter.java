package com.gala.video.app.epg.ui.search.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.TagKeyUtil;
import java.util.List;

public abstract class BaseTabAdapter<T> extends BaseAdapter {
    private static final int TAG_KEY_SHOW_IMAGE = TagKeyUtil.generateTagKey();
    protected final Handler mHandler = new Handler(Looper.getMainLooper());
    protected final IImageProvider mImageProvider = ImageProviderApi.getImageProvider();

    protected abstract void requestBitmapFailed(ImageRequest imageRequest, Exception exception);

    protected abstract void requestBitmapSucc(Bitmap bitmap, Object obj);

    protected void loadBitmap(View convertView, String imageUrl, int position) {
        if (StringUtils.isEmpty((CharSequence) imageUrl)) {
            Log.e("ViewAdapter", ">>>>>>>>>>>>>>ViewAdapter  loadBitmap -------imageUrl  = null or \"\" ! ");
            return;
        }
        convertView.setTag(TAG_KEY_SHOW_IMAGE, imageUrl);
        this.mImageProvider.loadImage(new ImageRequest(imageUrl, convertView), new IImageCallback() {
            public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
                Object cookie = imageRequest.getCookie();
                if (cookie != null) {
                    BaseTabAdapter.this.requestBitmapSucc(bitmap, cookie);
                }
            }

            public void onFailure(ImageRequest imageRequest, Exception exception) {
                BaseTabAdapter.this.requestBitmapFailed(imageRequest, exception);
            }
        });
    }

    protected void runOnUiThread(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public void notifyDataSetChanged(List<T> list) {
        super.notifyDataSetChanged();
    }

    public long getItemId(int position) {
        return 0;
    }

    public Object getItem(int position) {
        return null;
    }
}
