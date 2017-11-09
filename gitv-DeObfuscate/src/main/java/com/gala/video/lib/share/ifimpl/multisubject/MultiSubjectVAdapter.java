package com.gala.video.lib.share.ifimpl.multisubject;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.IActionListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.adapter.IMultiSubjectVAdapter;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card.IHorizontalCarkView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card.IVerticalCardView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card.IVerticalCardView.IFocusableCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.IPingbackListener;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.ShareDebug;
import java.util.List;

public class MultiSubjectVAdapter extends Adapter<PrivateVViewHolder> implements IMultiSubjectVAdapter {
    private static final int TYPE_LOADING = 102;
    private static final int TYPE_TOP = 101;
    private String TAG = "EPG/multisubject/MultiSubjectVAdapter";
    private IActionListener mActionListener;
    private SparseIntArray mConfigPosMap = new SparseIntArray();
    private Context mContext;
    private IFocusableCallback mFocusableCallback;
    private View mHeaderView;
    private int mLoadingHeight = ResourceUtil.getDimen(C1632R.dimen.dimen_48dp);
    private IPingbackListener mPingbackListener;
    private SparseIntArray mPositionMap = new SparseIntArray();
    private boolean mShowLoading = false;
    private List<CardModel> mVDataList;
    private IVerticalCardView mVGridView;

    public class PrivateVViewHolder extends ViewHolder {
        public PrivateVViewHolder(View view) {
            super(view);
        }
    }

    public MultiSubjectVAdapter(Context context, IVerticalCardView multiSubjectVGridView) {
        this.mContext = context;
        this.mVGridView = multiSubjectVGridView;
    }

    public void setVData(List<CardModel> cardArray) {
        this.mVDataList = cardArray;
    }

    public List<CardModel> getVData() {
        return this.mVDataList;
    }

    public void setPingbackListener(IPingbackListener pingbackSender) {
        this.mPingbackListener = pingbackSender;
    }

    public IPingbackListener getPingbackListener() {
        return this.mPingbackListener;
    }

    public void setActionListener(IActionListener actionListener) {
        this.mActionListener = actionListener;
    }

    public IActionListener getActionListener() {
        return this.mActionListener;
    }

    public String getCardResId(int position) {
        if (position < 0 || position >= ListUtils.getCount(this.mVDataList)) {
            return null;
        }
        CardModel cardModel = (CardModel) this.mVDataList.get(position);
        if (cardModel != null) {
            return cardModel.getId();
        }
        return null;
    }

    public void addHeaderView(View v) {
        this.mHeaderView = v;
    }

    public View getHeaderView() {
        return this.mHeaderView;
    }

    public boolean hasHeader() {
        return this.mHeaderView != null;
    }

    public void showLoading() {
        this.mShowLoading = true;
    }

    public void showLoading(int height) {
        this.mShowLoading = true;
        this.mLoadingHeight = height;
    }

    public void hideLoading() {
        int position = getLastPosition();
        if (this.mShowLoading) {
            notifyDataSetRemoved(position);
        }
        this.mShowLoading = false;
    }

    public int getCount() {
        int count = this.mHeaderView != null ? ListUtils.getCount(this.mVDataList) + 1 : ListUtils.getCount(this.mVDataList);
        if (this.mShowLoading) {
            return count + 1;
        }
        return count;
    }

    public int getItemViewType(int position) {
        if (position == getCount() - 1 && this.mShowLoading) {
            return 102;
        }
        if (position != 0 || this.mHeaderView == null) {
            return ((CardModel) this.mVDataList.get(getPositionExistHeader(position))).getWidgetType();
        }
        return 101;
    }

    public boolean isFocusable(int position) {
        if (position == getCount() - 1 && this.mShowLoading) {
            return false;
        }
        if (this.mFocusableCallback != null) {
            return this.mFocusableCallback.isFocusable(position);
        }
        return true;
    }

    public void reLoad() {
        this.mVGridView.reLoadTask();
    }

    public void setChildFocusPosition(SparseIntArray configPosMap) {
        this.mConfigPosMap = configPosMap;
    }

    public void setLastLoseFocusPosition(int rowPos, int i) {
        this.mPositionMap.put(rowPos, i);
    }

    public void onBindViewHolder(PrivateVViewHolder holder, int position) {
        LayoutParams params = holder.itemView.getLayoutParams();
        int viewType = holder.getItemViewType();
        if (viewType == 101) {
            params.width = holder.itemView.getLayoutParams().width;
            params.height = holder.itemView.getLayoutParams().height;
        } else if (viewType == 102) {
            holder.itemView.setFocusable(true);
            params.width = -1;
            params.height = this.mLoadingHeight;
        } else {
            int pos = getPositionExistHeader(position);
            CardModel cardModel = (CardModel) this.mVDataList.get(pos);
            holder.itemView.setFocusable(true);
            params.width = -1;
            params.height = ResourceUtil.getPx(GetInterfaceTools.getMultiSubjectViewFactory().getCardHeight(cardModel));
            int i = this.mConfigPosMap.get(pos, -1);
            IHorizontalCarkView iHorizontalCarkView = (IHorizontalCarkView) holder.itemView;
            if (i == -1) {
                i = this.mPositionMap.get(pos);
            }
            iHorizontalCarkView.initial(pos, i, cardModel);
            if (ShareDebug.DEBUG_LOG) {
                Log.e(this.TAG, "vAdapter, onBindViewHolder,params.height=" + params.height + ",pos=" + pos + ",cardModel.getWidgetType()=" + cardModel.getWidgetType());
            }
        }
    }

    private int getPositionExistHeader(int position) {
        return this.mHeaderView == null ? position : position - 1;
    }

    public PrivateVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 101) {
            view = this.mHeaderView;
        } else if (viewType == 102) {
            view = LayoutInflater.from(this.mContext).inflate(C1632R.layout.share_albumlist5_loading, parent, false);
        } else {
            view = GetInterfaceTools.getMultiSubjectViewFactory().createHGridView(viewType, this.mContext, this);
        }
        return new PrivateVViewHolder(view);
    }

    public void setFocusableCallback(IFocusableCallback focusableCallback) {
        this.mFocusableCallback = focusableCallback;
    }

    public void setChildFocusPosition(int row, int column) {
        this.mConfigPosMap.put(row, column);
    }

    public void clearChildFocusPosition() {
        this.mConfigPosMap.clear();
    }
}
