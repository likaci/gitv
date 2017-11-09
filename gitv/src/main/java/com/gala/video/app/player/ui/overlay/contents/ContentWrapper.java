package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.ui.overlay.contents.EpisodeAlbumListContent;
import com.gala.video.app.player.ui.overlay.contents.GalleryListContent.OnHorizontalScrollListener;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class ContentWrapper<T, E> extends FrameLayout implements IContent<T, E> {
    private final String TAG;
    private IContent<?, ?> mContent;
    private Context mContext;
    private String mInfo;
    private View mTitleRoot;
    private TextView mTitleText;

    public ContentWrapper(Context context, IContent<?, ?> content) {
        this(context, null, content);
    }

    public ContentWrapper(Context context, AttributeSet attrs, IContent<?, ?> content) {
        this(context, attrs, 0, content);
    }

    public ContentWrapper(Context context, AttributeSet attrs, int defStyle, IContent<?, ?> content) {
        super(context, attrs, defStyle);
        this.mContent = null;
        this.mTitleRoot = null;
        this.mTitleText = null;
        this.TAG = "ContentWrapper@" + Integer.toHexString(hashCode());
        this.mContext = context;
        this.mContent = content;
    }

    public String getTitle() {
        return this.mContent != null ? this.mContent.getTitle() : "";
    }

    public View getView() {
        if (this.mTitleRoot == null) {
            initViews();
        }
        return this;
    }

    public View getFocusableView() {
        return this.mContent.getFocusableView();
    }

    public T getContentData() {
        if (this.mContent instanceof EpisodeListContent) {
            return ((EpisodeListContent) this.mContent).getContentData();
        }
        if (this.mContent instanceof GalleryListContent) {
            return ((GalleryListContent) this.mContent).getContentData();
        }
        if (this.mContent instanceof GalleryListContent) {
            return ((EpisodeAlbumListContent) this.mContent).getContentData();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getContentData, unhandled content type, mContent=" + this.mContent);
        }
        return new ArrayList(0);
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> initViews");
        }
        setClipChildren(false);
        setClipToPadding(false);
        addTitleView();
        addContentView();
        setupViews();
    }

    private void addTitleView() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> addTitleView");
        }
        this.mTitleRoot = LayoutInflater.from(this.mContext).inflate(R.layout.player_layout_detail_title_content, this, true);
        this.mTitleText = (TextView) this.mTitleRoot.findViewById(R.id.detail_title_content_title);
    }

    private void addContentView() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> addContentView");
        }
        if (this.mContent != null) {
            View contentView = this.mContent.getView();
            if (contentView.getParent() == null) {
                addView(contentView);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "addContentView, mContent is null !!!");
        }
    }

    private void setupViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setupViews");
        }
        String title = getTitle();
        if (!StringUtils.isEmpty(this.mInfo)) {
            title = title + "" + this.mInfo;
        }
        this.mTitleText.setText(title);
        setupContentTitleParams();
    }

    private void setupContentTitleParams() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setupContentTitleParams");
        }
        LayoutParams titleTextLP = (LayoutParams) this.mTitleText.getLayoutParams();
        if (titleTextLP == null) {
            titleTextLP = new LayoutParams(-2, -2);
        }
        titleTextLP.leftMargin = ResourceUtil.getDimen(R.dimen.dimen_55dp);
        titleTextLP.topMargin = ResourceUtil.getDimen(R.dimen.dimen_12dp);
        this.mTitleText.setLayoutParams(titleTextLP);
        this.mTitleText.setTextSize(0, (float) ResourceUtil.getDimensionPixelSize(R.dimen.dimen_25dp));
        this.mTitleText.setTextColor(ResourceUtil.getColor(R.color.detail_title_text_color_new));
    }

    public void setData(T data) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setData");
        }
        if (this.mContent != null) {
            if (this.mContent instanceof EpisodeListContent) {
                ((EpisodeListContent) this.mContent).setData((List) data);
            } else if (this.mContent instanceof GalleryListContent) {
                ((GalleryListContent) this.mContent).setData((List) data);
            } else if (this.mContent instanceof EpisodeAlbumListContent) {
                ((EpisodeAlbumListContent) this.mContent).setData((List) data);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "setData, unhandled content type, content=" + this.mContent);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setData, mContent is null !!!");
        }
    }

    public void setSelection(E item) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setSelection, item");
        }
        if (this.mContent != null) {
            if (this.mContent instanceof EpisodeListContent) {
                ((EpisodeListContent) this.mContent).setSelection((IVideo) item);
            } else if (this.mContent instanceof GalleryListContent) {
                ((GalleryListContent) this.mContent).setSelection((IVideo) item);
            } else if (this.mContent instanceof EpisodeAlbumListContent) {
                ((EpisodeAlbumListContent) this.mContent).setSelection((AlbumInfo) item);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "setSelection, unhandled content type, content=" + this.mContent);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setSelection, mContent is null !!!");
        }
    }

    public void clearAlbumListDefaultSelectedTextColor() {
        if (this.mContent instanceof EpisodeAlbumListContent) {
            ((EpisodeAlbumListContent) this.mContent).setSelectedTextColor(false);
        }
    }

    public void show() {
        if (this.mContent != null) {
            this.mContent.show();
        }
    }

    public void hide() {
        if (this.mContent != null) {
            this.mContent.hide();
        }
        this.mInfo = null;
    }

    public void setItemListener(IItemListener<E> listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setItemListener, listener=" + listener);
        }
        if (this.mContent != null) {
            if (this.mContent instanceof EpisodeListContent) {
                ((EpisodeListContent) this.mContent).setItemListener(listener);
            } else if (this.mContent instanceof GalleryListContent) {
                ((GalleryListContent) this.mContent).setItemListener(listener);
            } else if (this.mContent instanceof EpisodeAlbumListContent) {
                ((EpisodeAlbumListContent) this.mContent).setItemListener(listener);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "setItemListener, unhandled content type, content=" + this.mContent);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setItemListener, mContent is null !!!");
        }
    }

    public void setOnHorizontalScrollListener(OnHorizontalScrollListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setOnHorizontalScrollListener, listener=" + listener);
        }
        if (this.mContent != null && (this.mContent instanceof GalleryListContent)) {
            ((GalleryListContent) this.mContent).setOnHorizontalScrollListener(listener);
        }
    }

    public List<Integer> getCurShownItems() {
        if (this.mContent instanceof GalleryListContent) {
            return ((GalleryListContent) this.mContent).getCurShownItems();
        }
        return null;
    }

    public void setUpdateInfo(String info) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setUpdateInfo:" + info);
        }
        this.mInfo = info;
        if (!StringUtils.isEmpty((CharSequence) info) && this.mTitleText != null) {
            this.mTitleText.setText(AlbumTextHelper.updateInfoText(new SpannableStringBuilder().append(getTitle()), this.mInfo));
        }
    }
}
