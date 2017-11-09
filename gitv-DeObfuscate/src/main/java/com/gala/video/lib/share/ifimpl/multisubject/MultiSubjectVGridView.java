package com.gala.video.lib.share.ifimpl.multisubject;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.RecyclerView.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.TextCanvas;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectVAdapter.PrivateVViewHolder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.IActionListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card.IVerticalCardView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card.IVerticalCardView.IFocusableCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.IPingbackListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.MultiSubjectPingBackModel;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.ShareDebug;
import java.util.ArrayList;
import java.util.List;

public class MultiSubjectVGridView extends VerticalGridView implements IVerticalCardView {
    private String LOG_TAG;
    private SparseIntArray mCardVisilibitys = new SparseIntArray();
    private Context mContext;
    private boolean mFirstResume = true;
    private OnItemRecycledListener mOnItemRecycledListener = new C16906();
    private OnScrollListener mOnScrollListener = new C16917();
    private int mTopBarHeight = 0;
    private int mTopShade = 0;
    private MultiSubjectVAdapter mVAdapter;

    class C16851 implements OnGlobalLayoutListener {
        C16851() {
        }

        public void onGlobalLayout() {
            if (MultiSubjectVGridView.this.getChildCount() > 0 && MultiSubjectVGridView.this.mFirstResume) {
                MultiSubjectVGridView.this.mFirstResume = false;
                if (MultiSubjectVGridView.this.mVAdapter.getActionListener() != null) {
                    MultiSubjectVGridView.this.mVAdapter.getActionListener().onVerticalCreate();
                }
            }
        }
    }

    class C16895 implements Runnable {
        C16895() {
        }

        public void run() {
            MultiSubjectVGridView.this.initPingbackState();
            MultiSubjectVGridView.this.initCardVisibleState();
            MultiSubjectVGridView.this.reLoadTask();
            MultiSubjectVGridView.this.mVAdapter.clearChildFocusPosition();
        }
    }

    class C16906 implements OnItemRecycledListener {
        C16906() {
        }

        public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
            recycle(holder);
        }

        private void recycle(ViewHolder holder) {
            if (ShareDebug.DEBUG_LOG) {
                LogUtils.m1571e(MultiSubjectVGridView.this.LOG_TAG, "recycle,holder=" + holder);
            }
            PrivateVViewHolder h = (PrivateVViewHolder) holder;
            if (h.itemView instanceof MultiSubjectHGridView) {
                ((MultiSubjectHGridView) h.itemView).recycle();
            }
        }
    }

    class C16917 extends OnScrollListener {
        C16917() {
        }

        public void onScrollStop() {
            if (MultiSubjectVGridView.this.mVAdapter.getActionListener() != null) {
                MultiSubjectVGridView.this.mVAdapter.getActionListener().onVerticalScrollStop();
            }
            MultiSubjectVGridView.this.reLoadTask();
            MultiSubjectVGridView.this.fetchSawItems(false);
            MultiSubjectVGridView.this.onCardScrollForPingback();
            MultiSubjectVGridView.this.onCardShowAfterScroll();
        }

        public void onScrollStart() {
            if (MultiSubjectVGridView.this.mVAdapter.getActionListener() != null) {
                MultiSubjectVGridView.this.mVAdapter.getActionListener().onVerticalScrollStart();
            }
            ImageProviderApi.getImageProvider().stopAllTasks();
        }

        public void onScroll(ViewParent parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
            if (MultiSubjectVGridView.this.mVAdapter.getActionListener() != null) {
                MultiSubjectVGridView.this.mVAdapter.getActionListener().onVerticalScroll(parent, firstAttachedItem, lastAttachedItem, totalItemCount);
            }
            if (ShareDebug.DEBUG_LOG) {
                LogUtils.m1573e(MultiSubjectVGridView.this.LOG_TAG, "onScroll --- firstAttachedItem = ", Integer.valueOf(firstAttachedItem), " lastAttachedItem = ", Integer.valueOf(lastAttachedItem));
            }
        }

        public void onScrollBefore(int position) {
            if (MultiSubjectVGridView.this.mVAdapter.getActionListener() != null) {
                MultiSubjectVGridView.this.mVAdapter.getActionListener().onVerticalScrollBefore(position);
            }
            if (ShareDebug.DEBUG_LOG) {
                LogUtils.m1573e(MultiSubjectVGridView.this.LOG_TAG, "onScrollBefore --- position = ", Integer.valueOf(position));
            }
            boolean need = false;
            if (MultiSubjectVGridView.this.mVAdapter.getActionListener() != null) {
                need = MultiSubjectVGridView.this.mVAdapter.getActionListener().onVerticalScrollCloselyTop(position);
            }
            View view = MultiSubjectVGridView.this.getViewByPosition(position);
            if (!need || view == null) {
                MultiSubjectVGridView.this.setFocusPlace(FocusPlace.FOCUS_CENTER);
                return;
            }
            int height = (view.getHeight() / 2) + MultiSubjectVGridView.this.mTopBarHeight;
            MultiSubjectVGridView.this.setFocusPlace(height, height);
        }
    }

    public MultiSubjectVGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MultiSubjectVGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiSubjectVGridView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.LOG_TAG = "MultiSubjectVGridView@" + hashCode();
        this.mContext = context;
        setViewRecycled(false);
        setExtraPadding(1073741823);
        setNumRows(1);
        setClipChildren(false);
        setClipToPadding(false);
        setFocusPlace(FocusPlace.FOCUS_EDGE);
        setFocusMode(1);
        setScrollRoteScale(0.8f, 1.0f, 2.5f);
        setQuickFocusLeaveForbidden(false);
        setOnItemRecycledListener(this.mOnItemRecycledListener);
        setOnScrollListener(this.mOnScrollListener);
        setWillNotDraw(false);
        this.mVAdapter = new MultiSubjectVAdapter(context, this);
        setAdapter(this.mVAdapter);
        getViewTreeObserver().addOnGlobalLayoutListener(new C16851());
    }

    public void addHeaderView(View v, int width, int height) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",addHeaderView,v=" + v);
        if (v == null) {
            throw new IllegalArgumentException("VCardGridView.addHeaderView need a view");
        }
        v.setLayoutParams(new LayoutParams(width, height));
        this.mVAdapter.addHeaderView(v);
        this.mVAdapter.notifyDataSetChanged();
        postHandler();
    }

    public void setTopShade(int topShade) {
        this.mTopShade = topShade;
    }

    public void setCardTitle(final String newTitle, final int rowNumber) {
        post(new Runnable() {
            public void run() {
                MultiSubjectHGridView hGridView;
                Log.e(MultiSubjectVGridView.this.LOG_TAG, MultiSubjectVGridView.this.LOG_TAG + ",setCardTitle,newTitle=" + newTitle + ",rowNumber=" + rowNumber);
                if (MultiSubjectVGridView.this.hasHeader()) {
                    hGridView = MultiSubjectVGridView.this.getHViewByPosition(rowNumber + 1);
                } else {
                    hGridView = MultiSubjectVGridView.this.getHViewByPosition(rowNumber);
                }
                if (hGridView != null) {
                    hGridView.setTitle(newTitle);
                    hGridView.invalidate();
                }
            }
        });
    }

    public void setCardTitle(final CharSequence newTitle, final int rowNumber) {
        post(new Runnable() {
            public void run() {
                MultiSubjectHGridView hGridView;
                Log.e(MultiSubjectVGridView.this.LOG_TAG, MultiSubjectVGridView.this.LOG_TAG + ",setCardTitle,newTitle=" + newTitle + ",rowNumber=" + rowNumber);
                if (MultiSubjectVGridView.this.hasHeader()) {
                    hGridView = MultiSubjectVGridView.this.getHViewByPosition(rowNumber + 1);
                } else {
                    hGridView = MultiSubjectVGridView.this.getHViewByPosition(rowNumber);
                }
                if (hGridView != null) {
                    hGridView.setTitle(newTitle);
                    hGridView.invalidate();
                }
            }
        });
    }

    public void setCardTitle(final String newTitle, final String tips, final int rowNumber) {
        post(new Runnable() {
            public void run() {
                MultiSubjectHGridView hGridView;
                Log.e(MultiSubjectVGridView.this.LOG_TAG, MultiSubjectVGridView.this.LOG_TAG + ",setCardTitle,newTitle=" + newTitle + ",rowNumber=" + rowNumber);
                if (MultiSubjectVGridView.this.hasHeader()) {
                    hGridView = MultiSubjectVGridView.this.getHViewByPosition(rowNumber + 1);
                } else {
                    hGridView = MultiSubjectVGridView.this.getHViewByPosition(rowNumber);
                }
                if (hGridView != null) {
                    hGridView.setTitle(newTitle);
                    hGridView.setTips(MultiSubjectVGridView.this.getTip(tips));
                    hGridView.invalidate();
                }
            }
        });
    }

    private TextCanvas getTip(String tips) {
        TextCanvas tc = new TextCanvas(this.mContext);
        tc.setText(tips);
        tc.setTextSize(ResourceUtil.getDimensionPixelSize(C1632R.dimen.dimen_20dp));
        tc.setHeight(ResourceUtil.getDimen(C1632R.dimen.dimen_23dp));
        tc.setTextColor(ResourceUtil.getColor(C1632R.color.detail_text_color_default));
        tc.setBackground(C1632R.drawable.transparent_drawable);
        tc.setPadding(ResourceUtil.getDimen(C1632R.dimen.dimen_7dp), ResourceUtil.getDimen(C1632R.dimen.dimen_3dp), ResourceUtil.getDimen(C1632R.dimen.dimen_7dp), 0);
        return tc;
    }

    public void showLoading() {
        this.mVAdapter.showLoading();
        this.mVAdapter.notifyDataSetAdd();
    }

    public void showLoading(int height) {
        this.mVAdapter.showLoading(height);
        this.mVAdapter.notifyDataSetAdd();
    }

    public void hideLoading() {
        this.mVAdapter.hideLoading();
    }

    public void setData(List<CardModel> cardArray) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",setData,data.size=" + ListUtils.getCount((List) cardArray));
        onDestory();
        notify(cardArray);
        this.mVAdapter.notifyDataSetChanged();
        postHandler();
    }

    public List<CardModel> getData() {
        return this.mVAdapter.getVData();
    }

    public void setChildFocusPosition(int row, int column) {
        this.mVAdapter.setChildFocusPosition(row, column);
    }

    public void notifyDataSetChanged(List<CardModel> cardArray) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetChanged,data.size=" + ListUtils.getCount((List) cardArray));
        notify(cardArray);
        this.mVAdapter.notifyDataSetChanged();
        postHandler();
    }

    public void notifyDataSetChanged() {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetChanged");
        this.mVAdapter.notifyDataSetChanged();
        postHandler();
    }

    public void notifyFoucsOnLast(int row) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyFoucsOnLast , row = " + row);
        if (hasHeader()) {
            row--;
        }
        if (row >= 0) {
            this.mVAdapter.setLastLoseFocusPosition(row, 1000);
            this.mVAdapter.notifyDataSetChanged();
        }
    }

    public void notifyDataSetUpdate(List<CardModel> cardArray) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetUpdate,data.size=" + ListUtils.getCount((List) cardArray));
        notify(cardArray);
        this.mVAdapter.notifyDataSetUpdate();
        postHandler();
    }

    public void notifyDataSetUpdate(List<CardModel> cardArray, int rowPos) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetUpdate,data.size=" + ListUtils.getCount((List) cardArray) + ",rowPos=" + rowPos);
        if (rowPos < cardArray.size()) {
            MultiSubjectHGridView hGridView;
            notify(cardArray);
            if (hasHeader()) {
                hGridView = getHViewByPosition(rowPos + 1);
            } else {
                hGridView = getHViewByPosition(rowPos);
            }
            if (hGridView != null) {
                CardModel cardModel = (CardModel) cardArray.get(rowPos);
                if (cardModel != null) {
                    if (cardModel.getSize() == hGridView.mHAdapter.getCount()) {
                        hGridView.setCardModel(cardModel);
                        hGridView.mHAdapter.notifyDataSetUpdate();
                    } else {
                        hGridView.setCardModel(cardModel);
                        hGridView.mHAdapter.notifyDataSetChanged();
                    }
                }
            }
            postHandler();
        }
    }

    public void notifyDataSetAdd(List<CardModel> cardArray) {
        int newCount = ListUtils.getCount((List) cardArray);
        int oldCount = ListUtils.getCount(getData());
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetAdd,data.oldCount = " + oldCount + ", newCount = " + newCount);
        notify(cardArray);
        if (oldCount >= newCount) {
            this.mVAdapter.notifyDataSetChanged();
        } else {
            this.mVAdapter.notifyDataSetAdd();
        }
        postHandler();
    }

    public void notifyItemRemoved(int position) {
        int oldCount = ListUtils.getCount(getData());
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyItemRemoved,data.oldCount = " + oldCount + ", remove position = " + position);
        if (oldCount <= position || getData() == null) {
            Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyItemRemoved,return ");
            return;
        }
        getData().remove(position);
        notify(getData());
        if (this.mVAdapter.getHeaderView() == null) {
            this.mVAdapter.notifyItemRemoved(position);
        } else {
            this.mVAdapter.notifyItemRemoved(position + 1);
        }
        postHandler();
    }

    public void notifyDataSetAdd(CardModel cardModel, int position) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetAdd,data.oldCount = " + ListUtils.getCount(getData()) + ", add position = " + position);
        if (getData() == null || cardModel == null) {
            Log.e(this.LOG_TAG, this.LOG_TAG + ",notifyDataSetAdd,return ");
            return;
        }
        getData().add(position, cardModel);
        notify(getData());
        if (this.mVAdapter.getHeaderView() == null) {
            this.mVAdapter.notifyDataSetAdd(position);
        } else {
            this.mVAdapter.notifyDataSetAdd(position + 1);
        }
        postHandler();
    }

    public void notifyDataSetRemoved(List<CardModel> cardArray, int position) {
        LogUtils.m1574i(this.LOG_TAG, "notifyDataSetRemoved");
        notify(cardArray);
        this.mVAdapter.notifyDataSetRemoved(position);
        postHandler();
    }

    private void notify(List<CardModel> cardArray) {
        List<CardModel> newArray = new ArrayList();
        newArray.addAll(cardArray);
        computeVPaddingB(newArray);
        this.mVAdapter.setVData(newArray);
    }

    private void postHandler() {
        LogUtils.m1574i(this.LOG_TAG, "postHandler()");
        post(new C16895());
    }

    public void setActionListener(IActionListener actionListener) {
        this.mVAdapter.setActionListener(actionListener);
    }

    private void computeVPaddingB(List<CardModel> cardArray) {
        int pb;
        if (cardArray == null || !ListUtils.isLegal((List) cardArray, cardArray.size() - 1)) {
            pb = 0;
        } else {
            pb = GetInterfaceTools.getMultiSubjectViewFactory().getVPaddingBottom(((CardModel) cardArray.get(cardArray.size() - 1)).getWidgetType());
        }
        setPadding(0, 0, 0, ResourceUtil.getPx(pb));
    }

    public void setPingbackListener(IPingbackListener pingbackListener) {
        this.mVAdapter.setPingbackListener(pingbackListener);
    }

    public boolean isViewVisible(int pos, boolean isJudgeHoleItemVisible, int gridViewTopShade, int cardBottomOffset) {
        if (pos < 0 || pos > getLastPosition()) {
            return false;
        }
        View v = getViewByPosition(pos);
        if (v == null) {
            return false;
        }
        int topY = v.getTop() - getScrollY();
        int bottomY = (v.getBottom() - getScrollY()) + cardBottomOffset;
        if (ShareDebug.DEBUG_LOG) {
            LogUtils.m1571e(this.LOG_TAG, "isViewVisible --- topY = " + topY + "v.getTop() = " + v.getTop() + " getScrollY() = " + getScrollY());
            LogUtils.m1571e(this.LOG_TAG, "isViewVisible --- bottomY = " + topY + "v.getBottom() = " + v.getBottom() + " getScrollY() = " + getScrollY());
        }
        int height = getBottom() - getTop();
        if (isJudgeHoleItemVisible) {
            if (topY < gridViewTopShade || topY >= height || bottomY <= gridViewTopShade || bottomY > height) {
                return false;
            }
            return true;
        } else if ((topY < gridViewTopShade || topY >= height) && (bottomY <= gridViewTopShade || bottomY > height)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isCardVisible(int pos, boolean isJudgeHoleItemVisible) {
        int firstCardIndex;
        if (this.mVAdapter.hasHeader()) {
            firstCardIndex = 1;
        } else {
            firstCardIndex = 0;
        }
        if (pos < firstCardIndex || pos > getLastPosition()) {
            return false;
        }
        return isViewVisible(pos, isJudgeHoleItemVisible, this.mTopShade, -ResourceUtil.getPx(GetInterfaceTools.getMultiSubjectViewFactory().getCardVisiblePaddingBottom()));
    }

    public void reLoadTask() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        int i = first;
        while (i <= last) {
            View view = getViewByPosition(i);
            if (view != null && (view instanceof MultiSubjectHGridView) && isCardVisible(i, false)) {
                ((MultiSubjectHGridView) view).reLoadTask();
            }
            i++;
        }
    }

    public boolean hasHeader() {
        return this.mVAdapter.hasHeader();
    }

    public void setVerticalScrollCloselyTopBarHeight(int topBarHeight) {
        this.mTopBarHeight = topBarHeight;
    }

    private void fetchSawItems(boolean isVerticalScrolling) {
        int first;
        int firstAttachedPosition = getFirstAttachedPosition();
        int firstCardIndex = getFirstCardIndex();
        if (firstAttachedPosition > firstCardIndex) {
            first = firstAttachedPosition;
        } else {
            first = firstCardIndex;
        }
        int last = getLastAttachedPosition();
        for (int i = first; i <= last; i++) {
            if (isCardVisible(i, true)) {
                MultiSubjectHGridView view = getHViewByPosition(i);
                if (view != null) {
                    view.fetchSawItem(isVerticalScrolling);
                }
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mVAdapter.getActionListener() == null || !this.mVAdapter.getActionListener().onVerticalDispatchKeyEvent(event)) {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mVAdapter.getActionListener() != null) {
            this.mVAdapter.getActionListener().onVerticalAttachedToWindow();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mVAdapter.getActionListener() != null) {
            this.mVAdapter.getActionListener().onVerticalDetachedFromWindow();
        }
    }

    public void onActivityResume() {
        if (this.mVAdapter.getActionListener() != null) {
            this.mVAdapter.getActionListener().onVerticalResume();
        }
        if (!this.mFirstResume) {
            reLoadTask();
            initPingbackState();
            initCardVisibleState();
        }
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                view.onActivityResume();
            }
        }
    }

    public void onActivityStart() {
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                view.onActivityStart();
            }
        }
    }

    public void onActivityStop() {
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                view.onActivityStop();
            }
        }
    }

    public void onActivityPause() {
        onPauseForPingback();
        onCardVisibleToInvisibleWhenPaused();
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                view.onActivityPause();
            }
        }
    }

    public void onActivityDestroy() {
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                view.onActivityDestroy();
            }
        }
    }

    private void onDestory() {
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                view.onDestory();
            }
        }
    }

    public void setFocusableCallback(IFocusableCallback focusableCallback) {
        this.mVAdapter.setFocusableCallback(focusableCallback);
    }

    public void initPingbackState() {
        fetchSawItems(false);
        onCardScrollForPingback();
    }

    private void onCardScrollForPingback() {
        int last = getLastPosition();
        for (int i = 0; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                boolean isVisible = isCardVisible(i, true);
                boolean isTimeKeeping = view.isTimeKeeping();
                if (ShareDebug.DEBUG_LOG) {
                    LogUtils.m1573e(this.LOG_TAG, "onCardScrollForPingback --- isVisible = ", Boolean.valueOf(isVisible), " isTimeKeeping = ", Boolean.valueOf(isTimeKeeping), " i = ", Integer.valueOf(i));
                }
                if (isVisible && !isTimeKeeping) {
                    view.startTimeKeep();
                }
                if (!isVisible && isTimeKeeping) {
                    view.endTimeKeep();
                    sendCardShowPingback(view, i);
                    view.resetSawItem();
                }
            }
        }
    }

    public void onPauseForPingback() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        for (int i = first; i <= last; i++) {
            MultiSubjectHGridView view = getHViewByPosition(i);
            if (view != null) {
                boolean isVisible = isCardVisible(i, true);
                boolean isTimeKeeping = view.isTimeKeeping();
                if (ShareDebug.DEBUG_LOG) {
                    LogUtils.m1576i(this.LOG_TAG, "onPauseForPingback --- i = ", Integer.valueOf(i), "isVisible = ", Boolean.valueOf(isVisible), " isTimeKeeping = ", Boolean.valueOf(isTimeKeeping));
                    if ((isVisible && !isTimeKeeping) || (!isVisible && isTimeKeeping)) {
                        Object[] objArr = new Object[3];
                        objArr[0] = this.LOG_TAG;
                        objArr[1] = "onPauseForPingback() --- the state is wrong, isVisible && !view.isTimeKeeping() = ";
                        boolean z = isVisible && !isTimeKeeping;
                        objArr[2] = Boolean.valueOf(z);
                        LogUtils.m1573e(objArr);
                        objArr = new Object[3];
                        objArr[0] = this.LOG_TAG;
                        objArr[1] = "onPauseForPingback() --- the state is wrong, !isVisible && isTimeKeeping = ";
                        if (isVisible || !isTimeKeeping) {
                            z = false;
                        } else {
                            z = true;
                        }
                        objArr[2] = Boolean.valueOf(z);
                        LogUtils.m1573e(objArr);
                    }
                }
                if (isVisible && isTimeKeeping) {
                    view.endTimeKeep();
                    sendCardShowPingback(view, i);
                    view.resetSawItem();
                }
            }
        }
    }

    private void sendCardShowPingback(MultiSubjectHGridView view, int posInV) {
        if (this.mVAdapter.getPingbackListener() != null && view != null && view.getShowedTime() > 500) {
            MultiSubjectPingBackModel pingbackModel = new MultiSubjectPingBackModel();
            pingbackModel.allitem = view.getAllItem();
            pingbackModel.dftitem = view.getDftItem();
            pingbackModel.line = view.getLine();
            pingbackModel.sawitem = view.fetchSawItem(false);
            this.mVAdapter.getPingbackListener().sendCardShowPingback(posInV, view.getData(), view.getFocusPosition(), pingbackModel);
        }
    }

    private int getFirstCardIndex() {
        return this.mVAdapter.hasHeader() ? 1 : 0;
    }

    private void initCardVisibleState() {
        this.mCardVisilibitys.clear();
        int first = getFirstCardIndex();
        int last = getLastPosition();
        for (int i = first; i <= last; i++) {
            boolean isCardVisible = isCardVisible(i, false);
            this.mCardVisilibitys.put(i, isCardVisible ? 0 : 4);
            if (this.mVAdapter.getActionListener() != null && isCardVisible) {
                this.mVAdapter.getActionListener().onCardShow(i);
                LogUtils.m1576i(this.LOG_TAG, "initCardVisibleState card show index = ", Integer.valueOf(i));
            }
        }
    }

    private void onCardShowAfterScroll() {
        int first = getFirstCardIndex();
        int last = getLastPosition();
        for (int i = first; i <= last; i++) {
            int visibility;
            boolean isCardVisible = isCardVisible(i, false);
            if (isCardVisible) {
                visibility = 0;
            } else {
                visibility = 4;
            }
            if (this.mCardVisilibitys.get(i, 4) != visibility) {
                this.mCardVisilibitys.put(i, visibility);
                if (this.mVAdapter.getActionListener() != null && isCardVisible) {
                    this.mVAdapter.getActionListener().onCardShow(i);
                    LogUtils.m1576i(this.LOG_TAG, "onCardShowAfterScroll card show index = ", Integer.valueOf(i));
                }
            }
        }
    }

    private void onCardVisibleToInvisibleWhenPaused() {
        int first = getFirstCardIndex();
        int last = getLastPosition();
        for (int i = first; i <= last; i++) {
            if (this.mCardVisilibitys.get(i, 4) == 0) {
                this.mCardVisilibitys.put(i, 4);
            }
        }
    }

    private MultiSubjectHGridView getHViewByPosition(int index) {
        View viewByPosition = getViewByPosition(index);
        if (viewByPosition == null || !(viewByPosition instanceof MultiSubjectHGridView)) {
            return null;
        }
        return (MultiSubjectHGridView) viewByPosition;
    }
}
