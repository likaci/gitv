package com.gala.video.app.player.albumdetail.ui.overlay.contents;

import android.content.Context;
import android.view.View;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.ui.overlay.contents.IContent;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectHGridView;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectVGridView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.DummyActionListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class ProgramCardContent implements IContent<CardModel, AlbumInfo> {
    public static final int CHANNEL_ID_TUO_KOU_XIU = 31;
    public final String TAG;
    private AlbumInfo mAlbumInfo;
    private ArrayList<CardModel> mCardModelList = new ArrayList();
    private MultiSubjectVGridView mContentView;
    private Context mContext;
    private boolean mEnableTvWindow;
    private boolean mIsPlayingIconErased = false;
    private boolean mIsShown;
    private IItemListener<AlbumInfo> mItemListener;

    class MyMultiSubjectAction extends DummyActionListener {
        private MultiSubjectVGridView mGridView;

        public MyMultiSubjectAction(MultiSubjectVGridView gridView) {
            this.mGridView = gridView;
        }

        public boolean onVerticalScrollCloselyTop(int pos) {
            return true;
        }

        public void onVerticalScrollStart() {
            this.mGridView.setExtraPadding(100);
        }

        public void onHorizontalItemClick(Context mContext, RecyclerView hGridView, ViewHolder viewHolder, CardModel cardModel) {
            if (ProgramCardContent.this.mItemListener != null) {
                ProgramCardContent.this.mItemListener.onItemClicked(null, viewHolder.getLayoutPosition());
            }
        }
    }

    public ProgramCardContent(Context context) {
        boolean z = false;
        if (Project.getInstance().getBuild().isSupportAlbumDetailWindowPlay() && Project.getInstance().getBuild().isSupportSmallWindowPlay() && PlayerDebugUtils.isAlbumDetailPageShowPlay()) {
            z = true;
        }
        this.mEnableTvWindow = z;
        this.TAG = "Detail/UI/ProgramCardContent@" + Integer.toHexString(hashCode());
        this.mContext = context;
    }

    public String getTitle() {
        String title = "";
        if (ListUtils.isEmpty(this.mCardModelList)) {
            return title;
        }
        return ((CardModel) this.mCardModelList.get(0)).getTitle();
    }

    public View getView() {
        if (this.mContentView == null) {
            initView();
        }
        return this.mContentView;
    }

    private void initView() {
        this.mContentView = new MultiSubjectVGridView(this.mContext);
        this.mContentView.setActionListener(new MyMultiSubjectAction(this.mContentView));
        this.mContentView.setFocusable(false);
        this.mContentView.showLoading((int) this.mContext.getResources().getDimension(C1291R.dimen.dimen_250dp));
    }

    public View getFocusableView() {
        return this.mContentView;
    }

    public CardModel getContentData() {
        return (CardModel) this.mCardModelList.get(0);
    }

    public void setData(CardModel data) {
        LogRecordUtils.logd(this.TAG, "setData");
        boolean hasData = false;
        if (!ListUtils.isEmpty(this.mCardModelList)) {
            hasData = true;
        }
        if (data == null || !ListUtils.isEmpty(data.getItemModelList())) {
            this.mCardModelList.clear();
            int position = 0;
            if (data != null) {
                position = updatePlayingSelection(data, this.mAlbumInfo);
                this.mCardModelList.add(data);
            }
            if (hasData) {
                this.mContentView.notifyDataSetUpdate(this.mCardModelList, 0);
            } else {
                updateFocusPosition(position);
                this.mContentView.notifyDataSetChanged(this.mCardModelList);
                this.mContentView.setFocusable(true);
            }
            this.mContentView.hideLoading();
            updateInfo(this.mAlbumInfo);
            return;
        }
        LogRecordUtils.logd(this.TAG, "setData, data is null");
    }

    public void update() {
        boolean hasData = false;
        if (!ListUtils.isEmpty(this.mCardModelList)) {
            hasData = true;
        }
        if (hasData) {
            this.mContentView.notifyDataSetUpdate(this.mCardModelList, 0);
        }
        this.mContentView.hideLoading();
        updateInfo(this.mAlbumInfo);
    }

    public void setSelection(AlbumInfo item) {
        LogRecordUtils.logd(this.TAG, "setSelection, item=" + item);
        if (item != null) {
            this.mAlbumInfo = item;
        }
        if (!ListUtils.isEmpty(this.mCardModelList)) {
            if (item != null) {
                this.mIsPlayingIconErased = false;
                updateFocusPosition(updatePlayingSelection((CardModel) this.mCardModelList.get(0), item));
                this.mContentView.notifyDataSetUpdate(this.mCardModelList);
                return;
            }
            this.mIsPlayingIconErased = true;
            updatePlayingSelection((CardModel) this.mCardModelList.get(0), this.mAlbumInfo);
            this.mContentView.notifyDataSetUpdate(this.mCardModelList);
        }
    }

    private void updateFocusPosition(int position) {
        LogRecordUtils.logd(this.TAG, "updateFocusPosition, position=" + position);
        this.mContentView.setChildFocusPosition(0, position);
    }

    public void show() {
        if (!this.mIsShown) {
            this.mIsShown = true;
            this.mContentView.setVisibility(0);
        }
    }

    public void hide() {
        if (this.mIsShown) {
            this.mIsShown = false;
            this.mContentView.setVisibility(4);
        }
    }

    public void setItemListener(IItemListener<AlbumInfo> listener) {
        this.mItemListener = listener;
    }

    private int updatePlayingSelection(CardModel cardModel, AlbumInfo albumInfo) {
        int position = findPlayingPosition(cardModel, albumInfo);
        LogRecordUtils.logd(this.TAG, "updatePlayingSelection, position=" + position + ", mEnableTvWindow=" + this.mEnableTvWindow + ", mIsPlayingIconErased=" + this.mIsPlayingIconErased);
        List items = cardModel.getItemModelList();
        if (!ListUtils.isEmpty(items)) {
            int size = items.size();
            for (int i = 0; i < size; i++) {
                boolean z;
                ItemModel itemModel = (ItemModel) items.get(i);
                if (i == position && this.mEnableTvWindow && !this.mIsPlayingIconErased) {
                    z = true;
                } else {
                    z = false;
                }
                itemModel.mIsPlaying = z;
            }
        }
        if (position < 0) {
            return 0;
        }
        return position;
    }

    public int getDftItemCount() {
        View view = this.mContentView.getViewByPosition(0);
        if (view instanceof MultiSubjectHGridView) {
            return ((MultiSubjectHGridView) view).getDftItem();
        }
        return 0;
    }

    public int getSawItemCount() {
        View view = this.mContentView.getViewByPosition(0);
        if (view instanceof MultiSubjectHGridView) {
            return ((MultiSubjectHGridView) view).fetchSawItem(false);
        }
        return 0;
    }

    private int findPlayingPosition(CardModel cardModel, AlbumInfo albumInfo) {
        int position = -1;
        if (albumInfo != null && cardModel != null) {
            List items = cardModel.getItemModelList();
            if (!ListUtils.isEmpty(items)) {
                int size = items.size();
                for (int i = 0; i < size; i++) {
                    if (((ItemModel) items.get(i)).getTvId().equals(albumInfo.getTvId())) {
                        position = i;
                        break;
                    }
                }
            }
        }
        LogRecordUtils.logd(this.TAG, "<< findPlayingPosition, position=" + position);
        return position;
    }

    public void updateInfo(AlbumInfo albumInfo) {
        LogRecordUtils.logd(this.TAG, "updateInfo()" + albumInfo);
        this.mAlbumInfo = albumInfo;
        if (this.mContentView != null && isNeedProgramUpdateInfo()) {
            CharSequence updateInfo = AlbumTextHelper.getUpdateInfo(albumInfo, this.mContext);
            if (!StringUtils.isEmpty(updateInfo)) {
                this.mContentView.setCardTitle(DetailConstants.CONTENT_TITLE_PROGRAMS, updateInfo, 0);
            }
            if (this.mItemListener != null) {
                this.mItemListener.onItemFilled();
            }
        }
    }

    private boolean isNeedProgramUpdateInfo() {
        if (this.mAlbumInfo != null && this.mAlbumInfo.isSourceType()) {
            int categoryId = this.mAlbumInfo.getChannelId();
            if (categoryId == 6 || categoryId == 31) {
                return true;
            }
        }
        return false;
    }

    public void reloadBitmap() {
        if (this.mContentView != null) {
            this.mContentView.reLoadTask();
        } else {
            LogRecordUtils.logd(this.TAG, "mContentView is null");
        }
    }
}
