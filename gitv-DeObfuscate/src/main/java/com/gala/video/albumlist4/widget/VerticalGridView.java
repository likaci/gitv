package com.gala.video.albumlist4.widget;

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
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;

public class VerticalGridView extends RecyclerView {
    private int f1679a;
    private C0453a f1680a;
    private C0471g f1681a;
    private int f1682b;
    private boolean f1683b;
    private int f1684c;
    private int f1685d;
    private int f1686e;
    private int f1687f;
    private int f1688g;

    private static class C0453a implements Runnable {
        private static final float[] f1749b = new float[]{255.0f};
        private static final float[] f1750c = new float[]{0.0f};
        public int f1751a = 0;
        public long f1752a;
        public final Interpolator f1753a = new Interpolator(1, 2);
        private View f1754a;
        public float[] f1755a;
        public int f1756b;
        public int f1757c;

        public C0453a(View view) {
            this.f1754a = view;
            this.f1756b = IAlbumConfig.DELAY_SHOW_LOADING_VIEW;
            this.f1757c = 250;
        }

        public void run() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            if (currentAnimationTimeMillis >= this.f1752a) {
                int i = (int) currentAnimationTimeMillis;
                Interpolator interpolator = this.f1753a;
                interpolator.setKeyFrame(0, i, f1749b);
                interpolator.setKeyFrame(1, i + this.f1757c, f1750c);
                this.f1751a = 2;
                this.f1754a.invalidate();
            }
        }
    }

    public VerticalGridView(Context context) {
        super(context);
        this.f1682b = -1;
        this.f1684c = -1;
        this.f1685d = -1;
        this.f1683b = false;
        this.f1686e = -1;
        this.f1687f = -1;
        this.f1688g = 0;
        m1303a(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f1682b = -1;
        this.f1684c = -1;
        this.f1685d = -1;
        this.f1683b = false;
        this.f1686e = -1;
        this.f1687f = -1;
        this.f1688g = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, 0);
        m1303a(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    private void m1303a(Context context, TypedArray typedArray) {
        setOrientation(Orientation.VERTICAL);
        if (isVerticalScrollBarEnabled()) {
            setWillNotDraw(false);
            this.f1683b = true;
            super.setVerticalScrollBarEnabled(false);
        }
        this.f1681a = new C0471g(context);
        this.f1680a = new C0453a(this);
        if (typedArray != null) {
            this.f1681a.m1511a(typedArray.getDrawable(2));
        }
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        this.f1683b = verticalScrollBarEnabled;
    }

    public void setTotalSize(int totalSize) {
        this.f1682b = getRow(totalSize - 1) + 1;
    }

    public void setScrollRange(int scrollRange) {
        this.f1679a = scrollRange;
    }

    public void setScrollBarDrawable(int resId) {
        this.f1681a.m1511a(getContext().getResources().getDrawable(resId));
    }

    public void setScrollBarDrawable(Drawable drawable) {
        this.f1681a.m1511a(drawable);
    }

    protected boolean awakenScrollBars() {
        if (m1306e()) {
            long currentAnimationTimeMillis = ((long) this.f1680a.f1756b) + AnimationUtils.currentAnimationTimeMillis();
            this.f1680a.f1752a = currentAnimationTimeMillis;
            this.f1680a.f1751a = 1;
            if (getHandler() != null) {
                getHandler().removeCallbacks(this.f1680a);
                getHandler().postAtTime(this.f1680a, currentAnimationTimeMillis);
            }
            m1307g();
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (m1306e()) {
            C0453a c0453a = this.f1680a;
            int i = c0453a.f1751a;
            if (i != 0) {
                int i2;
                if (i == 2) {
                    if (c0453a.f1755a == null) {
                        c0453a.f1755a = new float[1];
                    }
                    float[] fArr = c0453a.f1755a;
                    if (c0453a.f1753a.timeToValues(fArr) == Result.FREEZE_END) {
                        c0453a.f1751a = 0;
                    } else {
                        this.f1681a.m1507a(Math.round(fArr[0]));
                    }
                    i2 = 1;
                } else {
                    this.f1681a.m1507a(255);
                    i2 = 0;
                }
                this.f1681a.m1508a(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
                this.f1681a.m1509a(getWidth(), getScrollY(), computeVerticalScrollRange(), computeVerticalScrollOffset(), computeVerticalScrollExtent());
                this.f1681a.m1510a(canvas);
                if (i2 != 0) {
                    m1307g();
                }
            }
        }
    }

    private void m1307g() {
        if (this.f1681a != null) {
            Rect a = this.f1681a.m1505a();
            invalidate(a.left, a.top, a.right, a.bottom);
        }
    }

    private boolean m1306e() {
        return this.f1683b && this.f1681a.m1505a() != null && computeVerticalScrollRange() > computeVerticalScrollExtent() + 1;
    }

    protected int computeVerticalScrollRange() {
        if (!hasFocus() && !isQuickSmooth()) {
            return 0;
        }
        if (this.f1679a != 0) {
            return this.f1679a;
        }
        return m1304c();
    }

    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    protected int computeVerticalScrollOffset() {
        if (m1305d()) {
            return m1305d();
        }
        return getScrollY() - m1241b();
    }

    private int m1304c() {
        int i = this.f1682b;
        if (i == -1 && (this.f1685d == -1 || this.f1684c != getCount())) {
            this.f1684c = getCount();
            i = getRow(this.f1684c - 1) + 1;
        }
        View childAt = getChildAt(0);
        if (childAt == null) {
            return 0;
        }
        return ((((i - 1) * getVerticalMargin()) + (childAt.getHeight() * i)) + getPaddingTop()) + getPaddingBottom();
    }

    private int m1305d() {
        int focusPosition = getFocusPosition();
        int row = getRow(focusPosition) + 1;
        if (this.f1687f == getScrollY() && this.f1686e == row) {
            return this.f1688g;
        }
        this.f1686e = row;
        this.f1687f = getScrollY();
        View childAt = getChildAt(0);
        if (childAt != null) {
            row = (((row - 1) * getVerticalMargin()) + (childAt.getHeight() * (row - 1))) + getPaddingTop();
        } else {
            row = 0;
        }
        if (getViewByPosition(focusPosition) != null) {
            this.f1688g = row - (getViewByPosition(focusPosition).getTop() - getScrollY());
        } else {
            this.f1688g = 0;
        }
        return this.f1688g;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                case 21:
                case 22:
                    m1308h();
                    break;
            }
        }
        return dispatchKeyEvent;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        m1308h();
    }

    protected void lineFeed() {
        super.lineFeed();
        awakenScrollBars();
    }

    private void m1308h() {
        int a = RecyclerView.f1618a;
        if (a == 16 || a == 8) {
            awakenScrollBars();
        }
    }

    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        super.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
        this.f1680a.f1756b = scrollBarDefaultDelayBeforeFade;
    }
}
