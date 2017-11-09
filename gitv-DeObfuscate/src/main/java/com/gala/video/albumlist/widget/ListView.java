package com.gala.video.albumlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;

public class ListView extends VerticalGridView {
    private int f1505a;
    private Drawable f1506a;
    private ItemDivider f1507a;
    private int f1508b;

    public static abstract class ItemDivider {
        public abstract Drawable getItemDivider(int i, BlocksView blocksView);
    }

    public ListView(Context context) {
        super(context);
        m1039i();
    }

    public ListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m1039i();
    }

    private void m1039i() {
        setWillNotDraw(false);
    }

    public void setOrientation(Orientation orientation) {
        super.setOrientation(Orientation.VERTICAL);
    }

    public void setDividerHeight(int dividerHeight) {
        setVerticalMargin(dividerHeight);
    }

    public void setDividerWidth(int dividerWidth) {
        this.f1505a = dividerWidth;
    }

    public void setBackgroundWidth(int backgroundWidth) {
        this.f1508b = backgroundWidth;
    }

    public void setDivider(int resId) {
        this.f1506a = getContext().getResources().getDrawable(resId);
        setWillNotDraw(false);
        invalidate();
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new BlocksView.LayoutParams(getContext(), attrs);
    }

    protected BlocksView.LayoutParams generateDefaultLayoutParams() {
        return new BlocksView.LayoutParams(-1, -2);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.f1508b != 0 ? this.f1508b : getWidth();
        for (int firstAttachedPosition = getFirstAttachedPosition(); firstAttachedPosition < getLastAttachedPosition(); firstAttachedPosition++) {
            Drawable itemDivider;
            Drawable drawable = this.f1506a;
            if (this.f1507a != null) {
                itemDivider = this.f1507a.getItemDivider(firstAttachedPosition, this);
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
                if (this.f1505a > 0) {
                    i = (width - this.f1505a) / 2;
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
        return m1038a(getCount());
    }

    protected int computeVerticalScrollOffset() {
        return m1038a(getFocusPosition());
    }

    private int m1038a(int i) {
        return 0;
    }

    public View focusSearch(View focused, int direction) {
        if (isVerticalScrollBarEnabled()) {
            awakenScrollBars();
        }
        return super.focusSearch(focused, direction);
    }

    public void setItemDivider(ItemDivider divider) {
        this.f1507a = divider;
    }
}
