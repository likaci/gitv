package com.gala.video.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.gala.video.widget.IListViewPagerManager;
import com.gala.video.widget.IPageViewListener;
import com.gala.video.widget.adapter.ViewAdapter;
import com.gala.video.widget.util.ListViewPagerUtils;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class DrawingOrderListView<T> extends ListView implements IListViewPagerManager<T> {
    private static final int SCROLL_DURATION = 500;
    private int lastPosition;
    private Context mContext;
    private int mItemAnimation = 0;
    private int mItemHeight = Service.CISCO_FNA;
    private int mLastListItem = -1;
    private int mScroll = 1;
    private int mZoomInBg = 0;
    private int mZoomOutBg = 0;
    private OnItemClickListener onItemClickListener = new C18881();
    OnItemSelectedListener onItemSelectedListener = new C18892();
    IPageViewListener pageViewListener = null;

    class C18881 implements OnItemClickListener {
        C18881() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (DrawingOrderListView.this.pageViewListener != null) {
                DrawingOrderListView.this.pageViewListener.onItemClick(parent, view, position, id);
            }
        }
    }

    class C18892 implements OnItemSelectedListener {
        C18892() {
        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            DrawingOrderListView.this.log("onItemSelected:" + position);
            switch (DrawingOrderListView.this.mItemAnimation) {
                case 1:
                    if (DrawingOrderListView.this.mLastListItem > -1) {
                        for (int i = 0; i < DrawingOrderListView.this.getChildCount(); i++) {
                            DrawingOrderListView.this.getChild(i).setBackgroundResource(DrawingOrderListView.this.mZoomOutBg);
                        }
                    }
                    DrawingOrderListView.this.getChild(view).setBackgroundResource(DrawingOrderListView.this.mZoomInBg);
                    break;
                case 2:
                    DrawingOrderListView.this.zoomOut();
                    DrawingOrderListView.this.zoomIn(view);
                    break;
            }
            DrawingOrderListView.this.mLastListItem = position;
            if (DrawingOrderListView.this.pageViewListener != null) {
                DrawingOrderListView.this.pageViewListener.onItemSelected(parent, view, position, id);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public void setPageViewListener(IPageViewListener pageViewListener) {
        this.pageViewListener = pageViewListener;
    }

    public DrawingOrderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrawingOrderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawingOrderListView(Context context) {
        super(context);
        init(context);
    }

    public void setScroll(int scroll) {
        this.mScroll = scroll;
    }

    public void setZoomOutBg(int zoomOutBg) {
        this.mZoomOutBg = zoomOutBg;
    }

    public void setZoomInBg(int zoomInBg) {
        this.mZoomInBg = zoomInBg;
    }

    public void setItemAnimation(int itemAnimation) {
        this.mItemAnimation = itemAnimation;
    }

    private void init(Context context) {
        this.mContext = context;
        setSelection(0);
        setVerticalScrollBarEnabled(false);
        setChildrenDrawingOrderEnabled(true);
        setOnItemSelectedListener(this.onItemSelectedListener);
        setOnItemClickListener(this.onItemClickListener);
        setFriction(0.0f);
    }

    public void setDataSource(List<T> datas, Class<?> adapter) {
        try {
            ViewAdapter<T> gridAdapter = (ViewAdapter) adapter.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this.mContext});
            setAdapter(gridAdapter);
            gridAdapter.notifyDataSetChanged(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View getChild(int pos) {
        return ((FrameLayout) getChildAt(pos)).getChildAt(0);
    }

    private View getChild(View view) {
        return ((FrameLayout) view).getChildAt(0);
    }

    private void zoomOut() {
        if (this.mLastListItem > -1) {
            for (int i = 0; i < getChildCount(); i++) {
                getChild(i).setBackgroundResource(this.mZoomOutBg);
            }
            ListViewPagerUtils.scale(getChildAt(this.mLastListItem % getChildCount()), 1.1f, 1.0f, 1.1f, 1.0f, 150);
        }
    }

    private void zoomIn(View view) {
        getChild(view).setBackgroundResource(this.mZoomInBg);
        ListViewPagerUtils.scale(getChild(view), 1.0f, 1.1f, 1.0f, 1.1f, 300);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mScroll == 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (getCount() <= 4) {
            return super.onKeyDown(keyCode, event);
        }
        int curItemPos = getSelectedItemPosition();
        View view = getSelectedView();
        if (view == null) {
            return super.onKeyDown(keyCode, event);
        }
        this.mItemHeight = view.getHeight();
        switch (keyCode) {
            case 19:
                if (curItemPos > 1) {
                    if (curItemPos < getCount() - 1 && curItemPos > 2) {
                        log("onKeyDown smooth up------------:" + curItemPos);
                        smoothScrollToPositionFromTop(curItemPos - 2, this.mItemHeight + 10, 500);
                        break;
                    }
                }
                scrollTo(0, 0);
                break;
            case 20:
                if (curItemPos > 1 && curItemPos < getCount() - 2) {
                    log("onKeyDown smooth down------------:" + curItemPos);
                    smoothScrollBy(this.mItemHeight, 500);
                    break;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void log(String msg) {
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = getSelectedItemPosition() - getFirstVisiblePosition();
        if (i == 0) {
            this.lastPosition = 0;
        }
        if (selectedIndex < 0) {
            return i;
        }
        int ret;
        if (i == childCount - 1) {
            ret = selectedIndex;
        } else if (i >= selectedIndex) {
            this.lastPosition++;
            ret = childCount - this.lastPosition;
        } else {
            ret = i;
        }
        return ret;
    }
}
