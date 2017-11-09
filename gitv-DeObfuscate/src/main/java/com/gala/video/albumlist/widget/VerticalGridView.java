package com.gala.video.albumlist.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Interpolator;
import android.graphics.Interpolator.Result;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;

public class VerticalGridView extends BlocksView {
    private int f1495a;
    private C0407a f1496a;
    private C0425f f1497a;
    private int f1498b;
    private boolean f1499b;
    private int f1500c;
    private int f1501d;
    private int f1502e;
    private int f1503f;
    private int f1504g;

    private static class C0407a implements Runnable {
        private static final float[] f1523b = new float[]{255.0f};
        private static final float[] f1524c = new float[]{0.0f};
        public int f1525a = 0;
        public long f1526a;
        public final Interpolator f1527a = new Interpolator(1, 2);
        private View f1528a;
        public float[] f1529a;
        public int f1530b;
        public int f1531c;

        public C0407a(View view) {
            this.f1528a = view;
            this.f1530b = IAlbumConfig.DELAY_SHOW_LOADING_VIEW;
            this.f1531c = 250;
        }

        public void run() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            if (currentAnimationTimeMillis >= this.f1526a) {
                int i = (int) currentAnimationTimeMillis;
                Interpolator interpolator = this.f1527a;
                interpolator.setKeyFrame(0, i, f1523b);
                interpolator.setKeyFrame(1, i + this.f1531c, f1524c);
                this.f1525a = 2;
                this.f1528a.invalidate();
            }
        }
    }

    public VerticalGridView(Context context) {
        super(context);
        this.f1498b = -1;
        this.f1500c = -1;
        this.f1501d = -1;
        this.f1499b = false;
        this.f1502e = -1;
        this.f1503f = -1;
        this.f1504g = 0;
        m1032a(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f1498b = -1;
        this.f1500c = -1;
        this.f1501d = -1;
        this.f1499b = false;
        this.f1502e = -1;
        this.f1503f = -1;
        this.f1504g = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, 0);
        m1032a(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    private void m1032a(Context context, TypedArray typedArray) {
        setOrientation(Orientation.VERTICAL);
        if (isVerticalScrollBarEnabled()) {
            setWillNotDraw(false);
            this.f1499b = true;
            super.setVerticalScrollBarEnabled(false);
        }
        this.f1497a = new C0425f(context);
        this.f1496a = new C0407a(this);
        if (typedArray != null) {
            this.f1497a.m1202a(typedArray.getDrawable(2));
        }
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        this.f1499b = verticalScrollBarEnabled;
    }

    public void setTotalSize(int totalSize) {
        this.f1498b = getRow(totalSize - 1) + 1;
    }

    public void setScrollRange(int scrollRange) {
        this.f1495a = scrollRange;
    }

    public void setScrollBarDrawable(int resId) {
        this.f1497a.m1202a(getContext().getResources().getDrawable(resId));
    }

    public void setScrollBarDrawable(Drawable drawable) {
        this.f1497a.m1202a(drawable);
    }

    protected boolean awakenScrollBars() {
        if (m1035e()) {
            long currentAnimationTimeMillis = ((long) this.f1496a.f1530b) + AnimationUtils.currentAnimationTimeMillis();
            this.f1496a.f1526a = currentAnimationTimeMillis;
            this.f1496a.f1525a = 1;
            if (getHandler() != null) {
                getHandler().removeCallbacks(this.f1496a);
                getHandler().postAtTime(this.f1496a, currentAnimationTimeMillis);
            }
            m1036i();
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (m1035e()) {
            C0407a c0407a = this.f1496a;
            int i = c0407a.f1525a;
            if (i != 0) {
                int i2;
                if (i == 2) {
                    if (c0407a.f1529a == null) {
                        c0407a.f1529a = new float[1];
                    }
                    float[] fArr = c0407a.f1529a;
                    if (c0407a.f1527a.timeToValues(fArr) == Result.FREEZE_END) {
                        c0407a.f1525a = 0;
                    } else {
                        this.f1497a.m1198a(Math.round(fArr[0]));
                    }
                    i2 = 1;
                } else {
                    this.f1497a.m1198a(255);
                    i2 = 0;
                }
                this.f1497a.m1199a(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
                this.f1497a.m1200a(getWidth(), getScrollY(), computeVerticalScrollRange(), computeVerticalScrollOffset(), computeVerticalScrollExtent());
                this.f1497a.m1201a(canvas);
                if (i2 != 0) {
                    m1036i();
                }
            }
        }
    }

    private void m1036i() {
        if (this.f1497a != null) {
            Rect a = this.f1497a.m1196a();
            invalidate(a.left, a.top, a.right, a.bottom);
        }
    }

    private boolean m1035e() {
        return this.f1499b && this.f1497a.m1196a() != null && computeVerticalScrollRange() > computeVerticalScrollExtent() + 1;
    }

    protected int computeVerticalScrollRange() {
        if (!hasFocus() && !isQuickSmooth()) {
            return 0;
        }
        if (this.f1495a != 0) {
            return this.f1495a;
        }
        return m1033c();
    }

    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    protected int computeVerticalScrollOffset() {
        if (m1034d()) {
            return m1034d();
        }
        return getScrollY() - m960b();
    }

    private int m1033c() {
        int i = this.f1498b;
        if (i == -1 && (this.f1501d == -1 || this.f1500c != getCount())) {
            this.f1500c = getCount();
            i = getRow(this.f1500c - 1) + 1;
        }
        View childAt = getChildAt(0);
        if (childAt == null) {
            return 0;
        }
        return ((((i - 1) * getVerticalMargin()) + (childAt.getHeight() * i)) + getPaddingTop()) + getPaddingBottom();
    }

    private int m1034d() {
        int focusPosition = getFocusPosition();
        int row = getRow(focusPosition) + 1;
        if (this.f1503f == getScrollY() && this.f1502e == row) {
            return this.f1504g;
        }
        this.f1502e = row;
        this.f1503f = getScrollY();
        View childAt = getChildAt(0);
        if (childAt != null) {
            row = (((row - 1) * getVerticalMargin()) + (childAt.getHeight() * (row - 1))) + getPaddingTop();
        } else {
            row = 0;
        }
        if (getViewByPosition(focusPosition) != null) {
            this.f1504g = row - (getViewByPosition(focusPosition).getTop() - getScrollY());
        } else {
            this.f1504g = 0;
        }
        return this.f1504g;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                case 21:
                case 22:
                    m1037j();
                    break;
            }
        }
        return dispatchKeyEvent;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        m1037j();
    }

    protected void lineFeed() {
        super.lineFeed();
        awakenScrollBars();
    }

    private void m1037j() {
        int a = m935a();
        if (a == 16 || a == 8) {
            awakenScrollBars();
        }
    }

    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        super.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
        this.f1496a.f1530b = scrollBarDefaultDelayBeforeFade;
    }
}
