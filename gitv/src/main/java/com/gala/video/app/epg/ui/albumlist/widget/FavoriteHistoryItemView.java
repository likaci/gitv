package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.AlbumView;
import com.gala.video.lib.share.utils.ResourceUtil;

public class FavoriteHistoryItemView extends AlbumView {
    public static final String FavPage = "favorite";
    public static final String HistPage = "history";
    public static final String SubPage = "Subscribe";
    private String mPageType;
    private String mRecycleTitleText;
    private String mRecycleTitleText2;

    public FavoriteHistoryItemView(Context context) {
        super(context);
    }

    public FavoriteHistoryItemView(Context context, AlbumViewType type) {
        super(context, type);
    }

    public void setPageType(String type) {
        this.mPageType = type;
        this.mRecycleTitleText = "";
        this.mRecycleTitleText2 = "";
    }

    protected void focusRecyclerCover(boolean hasFocus) {
        getRecycleCoverView();
        if (this.mRecycleCoverView != null) {
            this.mRecycleCoverView.setDrawable(ResourceUtil.getDrawable(hasFocus ? R.drawable.epg_ic_recycle_focus_cover : R.drawable.epg_ic_recycle_unfocus_cover));
            getRecycleTitleView();
            if (this.mRecycleTitleView != null) {
                getRecycleTitle2View();
                if (this.mRecycleTitle2View == null) {
                    return;
                }
                if (hasFocus && this.mRecycleCoverView.getVisible() == 0) {
                    this.mRecycleTitleView.setVisible(0);
                    this.mRecycleTitleView.setText(this.mRecycleTitleText);
                    this.mRecycleTitle2View.setVisible(0);
                    this.mRecycleTitle2View.setText(this.mRecycleTitleText2);
                    return;
                }
                this.mRecycleTitleView.setVisible(8);
                this.mRecycleTitle2View.setVisible(8);
            }
        }
    }

    public void setRecycleCoverVisible(int visible) {
        if (visible == 0) {
            if (StringUtils.isEmpty(this.mRecycleTitleText)) {
                if (this.mPageType.equals("history")) {
                    this.mRecycleTitleText = ResourceUtil.getStr(R.string.record_ok_delete);
                } else if (this.mPageType.equals("favorite")) {
                    this.mRecycleTitleText = ResourceUtil.getStr(R.string.favor_ok_delete);
                } else if (this.mPageType.equals(SubPage)) {
                    this.mRecycleTitleText = ResourceUtil.getStr(R.string.subscrible_ok_delete);
                } else {
                    this.mRecycleTitleText = ResourceUtil.getStr(R.string.record_ok_delete);
                }
            }
            if (StringUtils.isEmpty(this.mRecycleTitleText2)) {
                if (this.mPageType.equals(SubPage)) {
                    this.mRecycleTitleText2 = ResourceUtil.getStr(R.string.subscrible_delete_back_quit);
                } else {
                    this.mRecycleTitleText2 = ResourceUtil.getStr(R.string.delete_back_quit);
                }
            }
        }
        super.setRecycleCoverVisible(visible);
    }
}
