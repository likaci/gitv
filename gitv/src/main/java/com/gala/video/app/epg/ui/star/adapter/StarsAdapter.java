package com.gala.video.app.epg.ui.star.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.ChannelHorizontalAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.star.model.StarsInfoModel;
import com.gala.video.app.epg.ui.star.widget.StarHorizontalGridView;
import com.gala.video.app.epg.ui.star.widget.StarHorizontalGridView.OnStarItemFocusChangedListener;
import com.gala.video.app.epg.ui.star.widget.StarsInfoView;
import com.gala.video.app.epg.ui.star.widget.StarsInfoView.OnTextClickedListener;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarsAdapter extends Adapter<MyViewHolder> {
    private static final int GAP_PADDING = getDimen(R.dimen.dimen_06dp);
    private static final int TOPANDTIPPADDING = getDimen(R.dimen.dimen_30dp);
    public static final int TYPE_HORIZONTAL = 3;
    private static final int TYPE_TOP = 0;
    private static final int TYPE_VERTICAL = 1;
    private Map<String, List<IData>> mAlbumMap = new HashMap();
    private Context mContext;
    private Star mDetailStar;
    private StarsInfoModel mInfoModel;
    private boolean mIsStarsRefresh = false;
    private OnTextClickedListener mOnTextClickedListener;
    private SparseIntArray mPositionMap = new SparseIntArray();
    private StarsInfoView mStarsInfoView;
    private List<Tag> mTagList = new ArrayList();

    public static class MyViewHolder extends ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    public StarsAdapter(Context context) {
        this.mContext = context;
    }

    public boolean isFocusable(int position) {
        if (position == 0 && (this.mDetailStar == null || StringUtils.isEmpty(this.mDetailStar.desc))) {
            return false;
        }
        return true;
    }

    public void updateList(Map<String, List<IData>> map, List<Tag> list) {
        if (!ListUtils.isEmpty((List) list) && !ListUtils.isEmpty((Map) map)) {
            this.mTagList.clear();
            this.mTagList.addAll(list);
            this.mAlbumMap.clear();
            this.mAlbumMap.putAll(map);
            if (ListUtils.getCount(this.mTagList) > 0) {
                notifyDataSetChanged();
            }
        }
    }

    public void updateStar(Star star) {
        this.mDetailStar = star;
        if (this.mStarsInfoView != null) {
            this.mStarsInfoView.setData(this.mDetailStar);
        }
    }

    public int getCount() {
        if (ListUtils.isEmpty(this.mTagList)) {
            return 1;
        }
        return this.mTagList.size() + 1;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        LayoutParams params = holder.itemView.getLayoutParams();
        params.width = -1;
        int type = holder.getItemViewType();
        if (type == 3 || type == 1) {
            onBindStarHorizontalView(type, holder, params, position);
            return;
        }
        params.height = -2;
        onBindViewHolderTop(holder);
    }

    private int getDataIndex(int position) {
        return position - 1;
    }

    private void onBindStarHorizontalView(int type, MyViewHolder holder, LayoutParams params, int position) {
        Tag tag = (Tag) this.mTagList.get(getDataIndex(position));
        if (this.mAlbumMap != null && tag != null) {
            holder.itemView.setFocusable(true);
            int itemWidth = ChannelHorizontalAdapter.WIDTH;
            int itemHeight = ChannelHorizontalAdapter.HEIGHT;
            if (type == 3) {
                params.height = getDimen(R.dimen.dimen_250dp);
            } else if (type == 1) {
                params.height = getDimen(R.dimen.dimen_330dp);
                itemWidth = GridAdapter.WIDTH;
                itemHeight = GridAdapter.HEIGHT;
            }
            onBindViewHolderGridView(holder, itemWidth, itemHeight, position, getLastLoseFocusPosition(position), (List) this.mAlbumMap.get(tag.getID()), tag);
        }
    }

    private void onBindViewHolderTop(MyViewHolder holder) {
        boolean z = true;
        holder.itemView.setFocusable(true);
        if (this.mIsStarsRefresh && this.mStarsInfoView != null) {
            this.mStarsInfoView.setRootViewOnFocusChangeListener();
        }
        if (this.mStarsInfoView == null) {
            this.mStarsInfoView = new StarsInfoView(holder.itemView);
            this.mStarsInfoView.setKeyWord(this.mInfoModel.getSearchModel().getKeyWord());
            this.mStarsInfoView.setOnTextClickedListener(this.mOnTextClickedListener);
            this.mStarsInfoView.setRootViewOnFocusChangeListener();
        }
        this.mStarsInfoView.setData(this.mDetailStar);
        if (this.mStarsInfoView.getStar() == null) {
            z = false;
        }
        this.mIsStarsRefresh = z;
    }

    private void onBindViewHolderGridView(MyViewHolder holder, int width, int height, int rowIndex, int horizontalPosition, List<IData> data, Tag tag) {
        StarHorizontalGridView starsHorinzontalItem = holder.itemView;
        starsHorinzontalItem.init(rowIndex, horizontalPosition, width, height, data, tag);
        starsHorinzontalItem.setOnStarHorizontalFocusChangedListener(new OnStarItemFocusChangedListener() {
            public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
                if (StarsAdapter.this.mStarsInfoView != null) {
                    StarsAdapter.this.mStarsInfoView.setScaleDuration(200);
                }
            }
        });
    }

    public void setLastLoseFocusPosition(int rowIndex, int horizontalPosition) {
        this.mPositionMap.put(rowIndex, horizontalPosition);
    }

    public int getLastLoseFocusPosition(int rowIndex) {
        return this.mPositionMap.get(rowIndex);
    }

    private static int getDimen(int id) {
        return ResourceUtil.getDimen(id);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(this.mContext).inflate(R.layout.epg_search_staritem_top, parent, false);
                break;
            case 1:
                view = new StarHorizontalGridView(this.mContext, this, new GridAdapter(this.mContext, AlbumViewType.VERTICAL));
                break;
            case 3:
                view = new StarHorizontalGridView(this.mContext, this, new ChannelHorizontalAdapter(this.mContext, AlbumViewType.HORIZONTAL));
                break;
        }
        return new MyViewHolder(view);
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        Tag tag = (Tag) this.mTagList.get(getDataIndex(position));
        return (tag == null || !tag.getLayout().equals(QLayoutKind.LANDSCAPE)) ? 1 : 3;
    }

    public int getItemOffsets(int position) {
        switch (getItemViewType(position)) {
            case 0:
                return TOPANDTIPPADDING;
            case 1:
            case 3:
                return GAP_PADDING;
            default:
                return 0;
        }
    }

    public Star getStar() {
        return this.mDetailStar;
    }

    public void setOnTextClickedListener(OnTextClickedListener listener) {
        this.mOnTextClickedListener = listener;
    }

    public void setAlbumInfoModel(StarsInfoModel infoModel) {
        this.mInfoModel = infoModel;
    }

    public StarsInfoModel getInfoModel() {
        return this.mInfoModel;
    }

    public int getDetailDescRealCount() {
        if (this.mStarsInfoView != null) {
            return this.mStarsInfoView.getDetailDescRealCount();
        }
        return 0;
    }
}
