package com.gala.video.albumlist4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;

public class ListView extends VerticalGridView {
    private int f1689a;
    private Drawable f1690a;
    private ItemDivider f1691a;
    private int f1692b;

    public static abstract class ItemDivider {
        public abstract Drawable getItemDivider(int i, RecyclerView recyclerView);
    }

    public ListView(Context context) {
        super(context);
        m1310g();
    }

    public ListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m1310g();
    }

    private void m1310g() {
        setNumRows(1);
        setWillNotDraw(false);
    }

    public void setOrientation(Orientation orientation) {
        super.setOrientation(Orientation.VERTICAL);
    }

    public void setNumRows(int numRows) {
        super.setNumRows(1);
    }

    public void setDividerHeight(int dividerHeight) {
        setVerticalMargin(dividerHeight);
    }

    public void setDividerWidth(int dividerWidth) {
        this.f1689a = dividerWidth;
    }

    public void setBackgroundWidth(int backgroundWidth) {
        this.f1692b = backgroundWidth;
    }

    public void setDivider(int resId) {
        this.f1690a = getContext().getResources().getDrawable(resId);
        setWillNotDraw(false);
        invalidate();
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new RecyclerView.LayoutParams(getContext(), attrs);
    }

    protected RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-1, -2);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.f1692b != 0 ? this.f1692b : getWidth();
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition < getLastAttachedPosition(); firstAttachedPosition++) {
            Drawable itemDivider;
            Drawable drawable = this.f1690a;
            if (this.f1691a != null) {
                itemDivider = this.f1691a.getItemDivider(firstAttachedPosition, this);
            } else {
                itemDivider = drawable;
            }
            if (itemDivider != null) {
                int i;
                View viewByPosition = getViewByPosition(firstAttachedPosition);
                int verticalMargin = getVerticalMargin();
                int intrinsicHeight = itemDivider.getIntrinsicHeight();
                if (this.a != null) {
                    verticalMargin = this.a.getItemOffsets(firstAttachedPosition, this);
                }
                verticalMargin = verticalMargin > intrinsicHeight ? (verticalMargin - intrinsicHeight) / 2 : 0;
                if (this.f1689a > 0) {
                    i = (width - this.f1689a) / 2;
                } else {
                    i = 0;
                }
                verticalMargin += viewByPosition.getBottom();
                itemDivider.setBounds(i, verticalMargin, width - i, intrinsicHeight + verticalMargin);
                itemDivider.draw(canvas);
            }
        }
    }

    protected int computeVerticalScrollRange() {
        return m1309a(getCount());
    }

    protected int computeVerticalScrollOffset() {
        return m1309a(getFocusPosition());
    }

    private int m1309a(int i) {
        View childAt = getChildAt(0);
        if (childAt != null) {
            return (childAt.getHeight() + getVerticalMargin()) * (i / getNumRows());
        }
        return 0;
    }

    public View focusSearch(View focused, int direction) {
        if (isVerticalScrollBarEnabled()) {
            awakenScrollBars();
        }
        return super.focusSearch(focused, direction);
    }

    public void setItemDivider(ItemDivider divider) {
        this.f1691a = divider;
    }
}
