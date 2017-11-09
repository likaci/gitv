package com.gala.video.lib.share.ifimpl.multisubject;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.albumlist4.widget.HorizontalGridView;
import com.gala.video.albumlist4.widget.RecyclerView.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.OnFocusLostListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.font.FontManager;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectHAdapter.PrivateHViewHolder;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.IActionListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.adapter.IMultiSubjectVAdapter;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card.IHorizontalCarkView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.IPingbackListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.MultiSubjectPingBackModel;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.ShareDebug;
import java.util.ArrayList;
import java.util.List;

public class MultiSubjectHGridView extends HorizontalGridView implements IHorizontalCarkView {
    protected static final String LOG_TAG = "MultiSubjectHGridView";
    private int dftitem;
    protected CardModel mCardModel;
    protected Context mContext;
    private long mEndTime;
    protected MultiSubjectHAdapter mHAdapter;
    private boolean mIsTimeKeeping;
    private OnFocusLostListener mOnFocusLostListener;
    private OnItemClickListener mOnItemClickListener;
    private OnItemFocusChangedListener mOnItemFocusListener;
    private OnItemRecycledListener mOnItemRecycledListener;
    private OnScrollListener mOnScrollListener;
    private int mPosInV;
    private long mStartTime;
    private List<String> mTimeLineStr;
    private IMultiSubjectVAdapter mVAdapter;
    private int sawitem;

    private MultiSubjectHGridView(Context context) {
        super(context);
        this.mTimeLineStr = new ArrayList();
        this.sawitem = 0;
        this.dftitem = 0;
        this.mIsTimeKeeping = false;
        this.mOnFocusLostListener = new OnFocusLostListener() {
            public void onFocusLost(ViewGroup parent, ViewHolder holder) {
                if (MultiSubjectHGridView.this.mVAdapter != null) {
                    MultiSubjectHGridView.this.mVAdapter.setLastLoseFocusPosition(MultiSubjectHGridView.this.mPosInV, holder.getLayoutPosition());
                }
                CreateInterfaceTools.createMultiSubjectUtils().resetSubscribeFocusIndex();
            }
        };
        this.mOnItemFocusListener = new OnItemFocusChangedListener() {
            public void onItemFocusChanged(final ViewGroup parent, final ViewHolder holder, final boolean hasFocus) {
                IActionListener listener = MultiSubjectHGridView.this.mVAdapter.getActionListener();
                if (listener != null) {
                    listener.onHorizontalItemFocusChanged(holder, hasFocus, MultiSubjectHGridView.this.mCardModel);
                }
                MultiSubjectHGridView.this.postDelayed(new Runnable() {
                    public void run() {
                        holder.itemView.bringToFront();
                        parent.bringToFront();
                    }
                }, 50);
                final float scaleFactor = MultiSubjectHGridView.this.mCardModel.getScaleFactor();
                MultiSubjectHGridView.this.post(new Runnable() {
                    public void run() {
                        AnimationUtil.zoomAnimation(holder.itemView, hasFocus, scaleFactor, 200, true);
                    }
                });
                if (MultiSubjectHGridView.this.mVAdapter != null && hasFocus) {
                    MultiSubjectHGridView.this.mVAdapter.setLastLoseFocusPosition(MultiSubjectHGridView.this.mPosInV, holder.getLayoutPosition());
                }
            }
        };
        this.mOnItemRecycledListener = new OnItemRecycledListener() {
            public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
                MultiSubjectHGridView.this.recycle(holder);
            }
        };
        this.mOnItemClickListener = new OnItemClickListener() {
            public void onItemClick(ViewGroup parent, ViewHolder viewHolder) {
                IPingbackListener pingbackListener = MultiSubjectHGridView.this.mVAdapter.getPingbackListener();
                if (pingbackListener != null) {
                    MultiSubjectPingBackModel pingBackModel = new MultiSubjectPingBackModel();
                    pingBackModel.line = MultiSubjectHGridView.this.getLine();
                    pingbackListener.sendItemClickPingback(viewHolder, MultiSubjectHGridView.this.mCardModel, MultiSubjectHGridView.this.mPosInV, pingBackModel);
                }
                IActionListener listener = MultiSubjectHGridView.this.mVAdapter.getActionListener();
                if (listener != null) {
                    listener.onHorizontalItemClick(MultiSubjectHGridView.this.mContext, MultiSubjectHGridView.this, viewHolder, MultiSubjectHGridView.this.mCardModel);
                }
            }
        };
        this.mOnScrollListener = new OnScrollListener() {
            public void onScrollStart() {
                IActionListener listener = MultiSubjectHGridView.this.mVAdapter.getActionListener();
                if (listener != null) {
                    listener.onHorizontalScrollStart(MultiSubjectHGridView.this.mCardModel);
                }
                MultiSubjectHGridView.this.fetchSawItem(false);
                if (ShareDebug.DEBUG_LOG) {
                    Log.e(MultiSubjectHGridView.LOG_TAG, "onScrollStart");
                }
                MultiSubjectHGridView.this.mHAdapter.setInvalidateBitmap(false);
                ImageProviderApi.getImageProvider().stopAllTasks();
            }

            public void onScrollStop() {
                IActionListener listener = MultiSubjectHGridView.this.mVAdapter.getActionListener();
                if (listener != null) {
                    listener.onHorizontalScrollStop(MultiSubjectHGridView.this.mCardModel, MultiSubjectHGridView.this);
                }
                MultiSubjectHGridView.this.fetchSawItem(false);
                if (ShareDebug.DEBUG_LOG) {
                    Log.e(MultiSubjectHGridView.LOG_TAG, "onScrollStop");
                }
                MultiSubjectHGridView.this.mHAdapter.setInvalidateBitmap(true);
                if (MultiSubjectHGridView.this.mVAdapter != null) {
                    MultiSubjectHGridView.this.mVAdapter.reLoad();
                }
            }

            public void onScroll(ViewParent parent, int firstVisibleItem, int lastVisibleItem, int totalItemCount) {
                IActionListener listener = MultiSubjectHGridView.this.mVAdapter.getActionListener();
                if (listener != null) {
                    listener.onHorizontalScroll(MultiSubjectHGridView.this.mCardModel, firstVisibleItem, lastVisibleItem, totalItemCount);
                }
                if (ShareDebug.DEBUG_LOG) {
                    Log.e(MultiSubjectHGridView.LOG_TAG, "onScroll");
                }
            }

            public void onScrollBefore(int nextPos) {
                IActionListener listener = MultiSubjectHGridView.this.mVAdapter.getActionListener();
                if (listener != null) {
                    listener.onHorizontalScrollBefore(MultiSubjectHGridView.this.mCardModel);
                }
                if (ShareDebug.DEBUG_LOG) {
                    Log.e(MultiSubjectHGridView.LOG_TAG, "onScrollBefore");
                }
                MultiSubjectHGridView.this.getDftItem();
            }
        };
    }

    public MultiSubjectHGridView(Context context, IMultiSubjectVAdapter adapter) {
        this(context);
        this.mContext = context;
        this.mVAdapter = adapter;
        setClipToPadding(false);
        setClipChildren(false);
        setFocusMode(1);
        setScrollRoteScale(0.8f, 1.0f, 2.5f);
        setExtraCount(0);
        setFocusLeaveForbidden(83);
        setLabelColor(ResourceUtil.getColor(R.color.albumview_normal_color));
        setLabelPadding(ResourceUtil.getPx(46), 0, ResourceUtil.getPx(13), ResourceUtil.getPx(16));
        setLabelSize(ResourceUtil.getDimensionPixelSize(R.dimen.dimen_27dp));
        setLabelColor(ResourceUtil.getColor(R.color.detail_title_text_color_new));
        setTypeface(FontManager.getInstance().getTypeface(context));
        setTimeColor(ResourceUtil.getColorStateList(R.color.share_normal_item_text_color));
        setTimePadding(ResourceUtil.getPx(10));
        setTimeLineExtraPadding(ResourceUtil.getPx(21));
        setTimeSize(ResourceUtil.getDimensionPixelSize(R.dimen.dimen_20dp));
        this.mHAdapter = new MultiSubjectHAdapter(this.mContext);
        setAdapter(this.mHAdapter);
        setOnItemFocusChangedListener(this.mOnItemFocusListener);
        setOnScrollListener(this.mOnScrollListener);
        setOnItemRecycledListener(this.mOnItemRecycledListener);
        setOnFocusLostListener(this.mOnFocusLostListener);
        setOnItemClickListener(this.mOnItemClickListener);
        showPositionInfo(false);
    }

    public void initial(int posInV, int lastPosInH, CardModel cardModel) {
        this.mPosInV = posInV;
        setFocusPosition(lastPosInH);
        setCardModel(cardModel);
        preFirstNotifyDataSetChanged();
        this.mHAdapter.notifyDataSetChanged();
        post(new Runnable() {
            public void run() {
                MultiSubjectHGridView.this.updateUI();
            }
        });
        resetDftItem();
    }

    public CardModel getData() {
        return this.mCardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.mCardModel = cardModel;
        this.mHAdapter.setHData(this.mCardModel);
        int widgettype = this.mCardModel.getWidgetType();
        int left = ResourceUtil.getPx(GetInterfaceTools.getMultiSubjectViewFactory().getItemViewPaddingLeft());
        setPadding(left, ResourceUtil.getPx(GetInterfaceTools.getMultiSubjectViewFactory().getItemViewPaddingTop(widgettype)), left, 0);
        setHorizontalMargin(ResourceUtil.getPx(GetInterfaceTools.getMultiSubjectViewFactory().getItemViewHorizontalSpace(widgettype)));
        setTitle();
        if (widgettype == 10 || widgettype == 13 || widgettype == 15 || widgettype == 26) {
            this.mTimeLineStr.clear();
            int size = this.mCardModel.getItemModelList().size();
            for (int i = 0; i < size; i++) {
                this.mTimeLineStr.add(((ItemModel) this.mCardModel.getItemModelList().get(i)).getOnlineTime());
            }
            setTimeList(this.mTimeLineStr);
            return;
        }
        setTimeList(null);
    }

    public void setTitle() {
        setLabel(this.mCardModel.getTitle());
    }

    public void setTitle(String newtitle) {
        this.mCardModel.setTitle(newtitle);
        setTitle();
    }

    public void setCharSqTitle() {
        setLabel(this.mCardModel.getCharSqTitle().toString());
    }

    public void setTitle(CharSequence charSequence) {
        this.mCardModel.setCharSqTitle(charSequence);
        setCharSqTitle();
    }

    protected void preFirstNotifyDataSetChanged() {
    }

    protected void updateUI() {
    }

    public void notifyDataSetChanged() {
        this.mHAdapter.notifyDataSetChanged();
        post(new Runnable() {
            public void run() {
                MultiSubjectHGridView.this.mVAdapter.reLoad();
            }
        });
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        CreateInterfaceTools.createMultiSubjectUtils().resetSubscribeFocusIndex();
        IActionListener listener = this.mVAdapter.getActionListener();
        if (listener != null) {
            listener.onHorizontalDetachedFromWindow(this.mCardModel);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IActionListener listener = this.mVAdapter.getActionListener();
        if (listener != null) {
            listener.onHorizontalAttachedToWindow(this.mCardModel);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        IActionListener listener = this.mVAdapter.getActionListener();
        if (listener == null || !listener.onHorizontalDispatchKeyEvent(event, this.mCardModel)) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    public void recycle() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        for (int i = first; i <= last; i++) {
            View view = getViewByPosition(i);
            if (!(this.mHAdapter == null || view == null)) {
                recycle(((LayoutParams) view.getLayoutParams()).getViewHolder());
            }
        }
    }

    public void reLoadTask() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        int i = first;
        while (i <= last) {
            View view = getViewByPosition(i);
            if (view != null && isViewVisible(i, false)) {
                ((PrivateHViewHolder) ((LayoutParams) view.getLayoutParams()).getViewHolder()).item.loadImage();
            }
            i++;
        }
    }

    public void recycle(ViewHolder holder) {
        if (ShareDebug.DEBUG_LOG) {
            Log.e(LOG_TAG, "recycle,holder=" + holder);
        }
        ((PrivateHViewHolder) holder).item.recycleAndShowDefaultImage();
    }

    private boolean isViewVisible(int i, boolean isJudgeHoleItemVisible) {
        if (i < 0 || i > getLastPosition()) {
            return false;
        }
        View view = getViewByPosition(i);
        int left = view.getLeft() - getScrollX();
        int right = view.getRight() - getScrollX();
        int screenWidth = ResourceUtil.getScreenWidth();
        if (isJudgeHoleItemVisible) {
            if (left < 0 || left >= screenWidth || right <= 0 || right > screenWidth) {
                return false;
            }
            return true;
        } else if (left >= 0 && left < screenWidth) {
            return true;
        } else {
            if (right <= 0 || right > screenWidth) {
                return false;
            }
            return true;
        }
    }

    public void resetSawItem() {
        this.sawitem = 0;
    }

    public void startTimeKeep() {
        this.mStartTime = System.currentTimeMillis();
        this.mIsTimeKeeping = true;
    }

    public void endTimeKeep() {
        this.mEndTime = System.currentTimeMillis();
        this.mIsTimeKeeping = false;
    }

    public boolean isTimeKeeping() {
        return this.mIsTimeKeeping;
    }

    public long getShowedTime() {
        if (this.mIsTimeKeeping) {
            LogUtils.e(LOG_TAG, "getShowedTime --- mIsTimeKeeping is wrong, mIsTimeKeeping should be false");
        }
        return this.mEndTime - this.mStartTime;
    }

    public int getAllItem() {
        return this.mHAdapter.getCount();
    }

    public int getDftItem() {
        if (this.dftitem == 0) {
            int first = getFirstAttachedPosition();
            int last = getLastAttachedPosition();
            for (int i = first; i <= last; i++) {
                if (isViewVisible(i, true)) {
                    this.dftitem++;
                }
            }
        }
        return this.dftitem;
    }

    public int getLine() {
        return this.mPosInV + 1;
    }

    public void resetDftItem() {
        this.dftitem = 0;
    }

    public int fetchSawItem(boolean isVerticalScrolling) {
        if (isVerticalScrolling) {
            return this.sawitem;
        }
        int first = getFirstAttachedPosition();
        for (int i = getLastAttachedPosition(); i >= first; i--) {
            if (isViewVisible(i, true)) {
                if (i + 1 > this.sawitem) {
                    this.sawitem = i + 1;
                }
                return this.sawitem;
            }
        }
        return this.sawitem;
    }

    public void onActivityResume() {
    }

    public void onActivityStart() {
    }

    public void onActivityStop() {
    }

    public void onActivityPause() {
    }

    public void onActivityDestroy() {
    }

    public void onDestory() {
    }
}
