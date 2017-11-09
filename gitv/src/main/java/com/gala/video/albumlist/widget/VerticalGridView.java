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
    private int a;
    private a f652a;
    private f f653a;
    private int b;
    private boolean f654b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;

    private static class a implements Runnable {
        private static final float[] b = new float[]{255.0f};
        private static final float[] c = new float[]{0.0f};
        public int a = 0;
        public long f655a;
        public final Interpolator f656a = new Interpolator(1, 2);
        private View f657a;
        public float[] f658a;
        public int f659b;
        public int f660c;

        public a(View view) {
            this.f657a = view;
            this.f659b = IAlbumConfig.DELAY_SHOW_LOADING_VIEW;
            this.f660c = 250;
        }

        public void run() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            if (currentAnimationTimeMillis >= this.f655a) {
                int i = (int) currentAnimationTimeMillis;
                Interpolator interpolator = this.f656a;
                interpolator.setKeyFrame(0, i, b);
                interpolator.setKeyFrame(1, i + this.f660c, c);
                this.a = 2;
                this.f657a.invalidate();
            }
        }
    }

    public VerticalGridView(Context context) {
        super(context);
        this.b = -1;
        this.c = -1;
        this.d = -1;
        this.f654b = false;
        this.e = -1;
        this.f = -1;
        this.g = 0;
        a(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.b = -1;
        this.c = -1;
        this.d = -1;
        this.f654b = false;
        this.e = -1;
        this.f = -1;
        this.g = 0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.View, defStyleAttr, 0);
        a(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
    }

    private void a(Context context, TypedArray typedArray) {
        setOrientation(Orientation.VERTICAL);
        if (isVerticalScrollBarEnabled()) {
            setWillNotDraw(false);
            this.f654b = true;
            super.setVerticalScrollBarEnabled(false);
        }
        this.f653a = new f(context);
        this.f652a = new a(this);
        if (typedArray != null) {
            this.f653a.a(typedArray.getDrawable(2));
        }
    }

    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        this.f654b = verticalScrollBarEnabled;
    }

    public void setTotalSize(int totalSize) {
        this.b = getRow(totalSize - 1) + 1;
    }

    public void setScrollRange(int scrollRange) {
        this.a = scrollRange;
    }

    public void setScrollBarDrawable(int resId) {
        this.f653a.a(getContext().getResources().getDrawable(resId));
    }

    public void setScrollBarDrawable(Drawable drawable) {
        this.f653a.a(drawable);
    }

    protected boolean awakenScrollBars() {
        if (e()) {
            long currentAnimationTimeMillis = ((long) this.f652a.f659b) + AnimationUtils.currentAnimationTimeMillis();
            this.f652a.f655a = currentAnimationTimeMillis;
            this.f652a.a = 1;
            if (getHandler() != null) {
                getHandler().removeCallbacks(this.f652a);
                getHandler().postAtTime(this.f652a, currentAnimationTimeMillis);
            }
            i();
        }
        return false;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (e()) {
            a aVar = this.f652a;
            int i = aVar.a;
            if (i != 0) {
                int i2;
                if (i == 2) {
                    if (aVar.f658a == null) {
                        aVar.f658a = new float[1];
                    }
                    float[] fArr = aVar.f658a;
                    if (aVar.f656a.timeToValues(fArr) == Result.FREEZE_END) {
                        aVar.a = 0;
                    } else {
                        this.f653a.a(Math.round(fArr[0]));
                    }
                    i2 = 1;
                } else {
                    this.f653a.a(255);
                    i2 = 0;
                }
                this.f653a.a(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
                this.f653a.a(getWidth(), getScrollY(), computeVerticalScrollRange(), computeVerticalScrollOffset(), computeVerticalScrollExtent());
                this.f653a.a(canvas);
                if (i2 != 0) {
                    i();
                }
            }
        }
    }

    private void i() {
        if (this.f653a != null) {
            Rect a = this.f653a.a();
            invalidate(a.left, a.top, a.right, a.bottom);
        }
    }

    private boolean e() {
        return this.f654b && this.f653a.a() != null && computeVerticalScrollRange() > computeVerticalScrollExtent() + 1;
    }

    protected int computeVerticalScrollRange() {
        if (!hasFocus() && !isQuickSmooth()) {
            return 0;
        }
        if (this.a != 0) {
            return this.a;
        }
        return c();
    }

    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    protected int computeVerticalScrollOffset() {
        if (d()) {
            return d();
        }
        return getScrollY() - b();
    }

    private int c() {
        int i = this.b;
        if (i == -1 && (this.d == -1 || this.c != getCount())) {
            this.c = getCount();
            i = getRow(this.c - 1) + 1;
        }
        View childAt = getChildAt(0);
        if (childAt == null) {
            return 0;
        }
        return ((((i - 1) * getVerticalMargin()) + (childAt.getHeight() * i)) + getPaddingTop()) + getPaddingBottom();
    }

    private int d() {
        int focusPosition = getFocusPosition();
        int row = getRow(focusPosition) + 1;
        if (this.f == getScrollY() && this.e == row) {
            return this.g;
        }
        this.e = row;
        this.f = getScrollY();
        View childAt = getChildAt(0);
        if (childAt != null) {
            row = (((row - 1) * getVerticalMargin()) + (childAt.getHeight() * (row - 1))) + getPaddingTop();
        } else {
            row = 0;
        }
        if (getViewByPosition(focusPosition) != null) {
            this.g = row - (getViewByPosition(focusPosition).getTop() - getScrollY());
        } else {
            this.g = 0;
        }
        return this.g;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        if (event.getAction() == 0) {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                case 21:
                case 22:
                    j();
                    break;
            }
        }
        return dispatchKeyEvent;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        j();
    }

    protected void lineFeed() {
        super.lineFeed();
        awakenScrollBars();
    }

    private void j() {
        int a = a();
        if (a == 16 || a == 8) {
            awakenScrollBars();
        }
    }

    public void setScrollBarDefaultDelayBeforeFade(int scrollBarDefaultDelayBeforeFade) {
        super.setScrollBarDefaultDelayBeforeFade(scrollBarDefaultDelayBeforeFade);
        this.f652a.f659b = scrollBarDefaultDelayBeforeFade;
    }
}
