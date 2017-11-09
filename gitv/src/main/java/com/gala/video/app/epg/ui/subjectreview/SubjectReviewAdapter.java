package com.gala.video.app.epg.ui.subjectreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SubjectReviewAdapter extends BaseGridAdapter<ChannelLabel> {
    private static final int DEFAULT_COUNT = 9;
    private static final int HEIGHT = ResourceUtil.getDimen(R.dimen.dimen_232dp);
    private static final int WIDTH = ResourceUtil.getDimen(R.dimen.dimen_391dp);

    public SubjectReviewAdapter(Context context) {
        super(context);
    }

    protected int getDataCount() {
        if (ListUtils.isEmpty(this.mDataList)) {
            return getDefalutCount();
        }
        return ListUtils.getCount(this.mDataList);
    }

    public int getDefalutCount() {
        return 9;
    }

    public void releaseData(View convertView) {
        if (convertView != null) {
            ((SubjectView) convertView).releaseData();
            convertView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        }
    }

    protected void showDefaultBitmap(View convertView) {
        if (convertView != null) {
            convertView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
            ((SubjectView) convertView).setDefaultImage();
        }
    }

    protected View onCreateItemViewHolder(int viewType) {
        SubjectView view = new SubjectView(this.mContext);
        view.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        return view;
    }

    protected void onBindItemViewHolder(ViewHolder holder, int position, LayoutParams params) {
        setViewContent(position, holder.itemView);
    }

    private void setViewContent(int position, SubjectView itemView) {
        if (!ListUtils.isEmpty(this.mDataList) && position < getCount()) {
            ChannelLabel channelLabel = (ChannelLabel) this.mDataList.get(position);
            if (channelLabel != null) {
                itemView.setTag(TAG_KEY_INFO_DATA, channelLabel);
                String imageUrlByPos = getImageUrl(channelLabel);
                itemView.releaseData();
                itemView.setTitle(GetInterfaceTools.getCornerProvider().getChannelLabelTitle(channelLabel));
                loadBitmap(itemView, imageUrlByPos);
            }
        }
    }

    private String getImageUrl(ChannelLabel channelLabel) {
        if (channelLabel == null) {
            return null;
        }
        return TextUtils.isEmpty(channelLabel.itemImageUrl) ? PicSizeUtils.getUrlWithSize(PhotoSize._480_270, channelLabel.imageUrl) : channelLabel.itemImageUrl;
    }

    public void initLayoutParamsParams(ViewHolder holder, LayoutParams params) {
        if (params != null) {
            params.width = WIDTH;
            params.height = HEIGHT;
        }
    }

    protected void requestBitmapSucc(String url, Bitmap netBitmap, Object cookie) {
        if (cookie != null) {
            SubjectView view = (SubjectView) cookie;
            view.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(false));
            view.setImage(netBitmap);
        }
    }

    protected void requestBitmapFailed(String url, Object cookie, Exception exception) {
        if (cookie != null) {
            SubjectView view = (SubjectView) cookie;
            if (!loadOtherUrl(url, view)) {
                showDefaultBitmap(view);
            }
        }
    }

    private boolean loadOtherUrl(String originalUrl, SubjectView view) {
        if (TextUtils.isEmpty(originalUrl) || view == null) {
            return false;
        }
        ChannelLabel channelLabel = (ChannelLabel) view.getTag(TAG_KEY_INFO_DATA);
        if (channelLabel == null || channelLabel.getType() == ResourceType.RESOURCE_GROUP || originalUrl.contains(PhotoSize._480_270.toString())) {
            return false;
        }
        loadBitmap(view, PicSizeUtils.getUrlWithSize(PhotoSize._480_270, channelLabel.imageUrl));
        return true;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected boolean isShowingDefault(View convertView) {
        if (convertView == null || convertView.getTag(TAG_KEY_SHOW_DEFAULT) == null) {
            return true;
        }
        return ((Boolean) convertView.getTag(TAG_KEY_SHOW_DEFAULT)).booleanValue();
    }
}
