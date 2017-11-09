package com.gala.video.app.epg.ui.star.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.gala.albumprovider.model.Tag;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ItemDecoration;
import com.gala.video.albumlist4.widget.RecyclerView.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.star.adapter.StarsAdapter;
import com.gala.video.app.epg.ui.star.adapter.StarsAdapter.MyViewHolder;
import com.gala.video.app.epg.ui.star.model.StarsInfoModel;
import com.gala.video.app.epg.ui.star.presenter.StarsContract.View;
import com.gala.video.app.epg.ui.star.utils.StarsPingbackUtil;
import com.gala.video.app.epg.ui.star.widget.StarsInfoView.OnTextClickedListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;
import java.util.Map;

public class StarVerticalGridView extends VerticalGridView {
    private static final int DUFALUT_HEIGHT = getDimen(C0508R.dimen.dimen_110dp);
    private static final int FIRST_LOW = getDimen(C0508R.dimen.dimen_528dp);
    private static final int GAP_HEIGHT = TIP_HEIGHT;
    private static final int POS_LOW_1 = getDimen(C0508R.dimen.dimen_308dp);
    private static final int TIP_HEIGHT = getDimen(C0508R.dimen.dimen_28dp);
    private StarsInfoModel mInfoModel;
    private ItemDecoration mItemDecoration;
    private OnItemRecycledListener mOnItemRecycledListener;
    private OnScrollListener mOnScrollListener;
    private OnTextClickedListener mOnTextClickedListener;
    private StarsAdapter mStarsAdapter;
    private View mView;

    class C10981 extends ItemDecoration {
        C10981() {
        }

        public int getItemOffsets(int itemPosition, RecyclerView parent) {
            return StarVerticalGridView.this.mStarsAdapter.getItemOffsets(itemPosition);
        }
    }

    class C10992 implements OnItemRecycledListener {
        C10992() {
        }

        public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
            StarVerticalGridView.this.recycle(holder);
        }
    }

    class C11003 extends OnScrollListener {
        C11003() {
        }

        public void onScrollStop() {
            StarVerticalGridView.this.reLoadTask();
        }

        public void onScrollStart() {
            ImageProviderApi.getImageProvider().stopAllTasks();
        }

        public void onScroll(ViewParent parent, int firstVisibleItem, int lastVisibleItem, int totalItemCount) {
            boolean z = false;
            android.view.View view = StarVerticalGridView.this.getViewByPosition(0);
            if (view != null && StarVerticalGridView.this.mView != null) {
                int offset = view.getBottom() - StarVerticalGridView.this.getScrollY();
                View access$100 = StarVerticalGridView.this.mView;
                if (offset < StarsTopView.TOP_HEIGHT) {
                    z = true;
                }
                access$100.showTopView(z);
            }
        }

        public void onScrollBefore(int position) {
            int low;
            int height = StarVerticalGridView.DUFALUT_HEIGHT;
            android.view.View view = StarVerticalGridView.this.getViewByPosition(position);
            if (view != null) {
                height = view.getHeight();
            }
            if (position == 1) {
                low = StarVerticalGridView.POS_LOW_1 + (height / 2);
            } else {
                low = (((height / 2) + StarsTopView.TOP_HEIGHT) + StarVerticalGridView.TIP_HEIGHT) + (StarVerticalGridView.GAP_HEIGHT * 3);
            }
            StarVerticalGridView.this.setFocusPlace(low, (StarVerticalGridView.this.getHeight() - (height / 2)) - (StarVerticalGridView.TIP_HEIGHT * 2));
        }
    }

    class C11014 implements OnTextClickedListener {
        C11014() {
        }

        public void onClick() {
            if (StarVerticalGridView.this.mView != null) {
                StarVerticalGridView.this.mView.showFullView(StarVerticalGridView.this.getDetailDescRealCount());
                StarsPingbackUtil.sendPageDetailClick(StarVerticalGridView.this.mInfoModel);
            }
        }
    }

    public StarVerticalGridView(Context context) {
        this(context, null);
    }

    public StarVerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarVerticalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mItemDecoration = new C10981();
        this.mOnItemRecycledListener = new C10992();
        this.mOnScrollListener = new C11003();
        this.mOnTextClickedListener = new C11014();
        init(context);
    }

    private void init(Context context) {
        setNumRows(1);
        setFocusMode(1);
        setScrollRoteScale(0.8f, 1.0f, 2.5f);
        setPadding(0, getDimen(C0508R.dimen.dimen_37dp), 0, getDimen(C0508R.dimen.dimen_02dp));
        setShakeForbidden(115);
        setViewRecycled(false);
        setQuickFocusLeaveForbidden(false);
        setWillNotDraw(false);
        setVisibility(0);
        setFocusable(false);
        setFocusPlace(FIRST_LOW, FIRST_LOW);
        setOnItemRecycledListener(this.mOnItemRecycledListener);
        setOnScrollListener(this.mOnScrollListener);
        this.mStarsAdapter = new StarsAdapter(AppRuntimeEnv.get().getApplicationContext());
        setItemDecoration(this.mItemDecoration);
    }

    public void init(View view, StarsInfoModel infoModel) {
        this.mView = view;
        this.mInfoModel = infoModel;
        this.mStarsAdapter.setAlbumInfoModel(infoModel);
        setAdapter(this.mStarsAdapter);
    }

    public void showDatas(Map<String, List<IData>> map, List<Tag> list) {
        this.mStarsAdapter.updateList(map, list);
        setFocusable(true);
        setFocusPosition(1, true);
    }

    public void recycle(ViewHolder holder) {
        MyViewHolder h = (MyViewHolder) holder;
        if (h != null && (h.itemView instanceof StarHorizontalGridView)) {
            ((StarHorizontalGridView) h.itemView).recycle();
        }
    }

    public void reLoadTask() {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        int i = first;
        while (i <= last) {
            android.view.View view = getViewByPosition(i);
            if (view != null && (view instanceof StarHorizontalGridView) && isViewVisible(i)) {
                ((StarHorizontalGridView) ((MyViewHolder) ((LayoutParams) view.getLayoutParams()).getViewHolder()).itemView).reLoadBitmap();
            }
            i++;
        }
    }

    private boolean isViewVisible(int pos) {
        if (pos < 0 || pos > getLastPosition()) {
            return false;
        }
        android.view.View v = getViewByPosition(pos);
        if (v == null) {
            return false;
        }
        boolean topIn;
        int topY = v.getTop() - getScrollY();
        int bottomY = v.getBottom() - getScrollY();
        int height = getBottom() - getTop();
        if (topY < 0 || topY >= height) {
            topIn = false;
        } else {
            topIn = true;
        }
        boolean bottomIn;
        if (bottomY <= 0 || bottomY > height) {
            bottomIn = false;
        } else {
            bottomIn = true;
        }
        if (topIn || bottomIn) {
            return true;
        }
        return false;
    }

    public void setDetails(Star star) {
        this.mStarsAdapter.updateStar(star);
    }

    public void setOnTextClickedListener() {
        this.mStarsAdapter.setOnTextClickedListener(this.mOnTextClickedListener);
    }

    public Star getStar() {
        return this.mStarsAdapter.getStar();
    }

    private static int getDimen(int id) {
        return ResourceUtil.getDimen(id);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mOnTextClickedListener = null;
        this.mOnScrollListener = null;
        this.mOnItemRecycledListener = null;
        this.mItemDecoration = null;
    }

    public int getDetailDescRealCount() {
        if (this.mStarsAdapter != null) {
            return this.mStarsAdapter.getDetailDescRealCount();
        }
        return 0;
    }
}
