package com.gala.video.albumlist.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.database.Observable;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.utils.LOG;
import com.gala.video.albumlist.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist.widget.LayoutManager.Orientation;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.LinkedList;
import org.xbill.DNS.WKSRecord.Service;

public class BlocksView extends ViewGroup {
    public static final int INVALID_POSITION = -1;
    public static final int INVALID_TYPE = -1;
    public static final int SCROLL_STATE_IDLE = 1;
    public static final int SCROLL_STATE_SMOOTH = 2;
    public static final int SCROLL_STATE_SPRINGBACK = 3;
    public static final int SCROLL_TYPE_CONTINUOUS = 18;
    public static final int SCROLL_TYPE_QUICK = 19;
    public static final int SCROLL_TYPE_STEP = 17;
    private static final Interpolator a = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    };
    private static final Interpolator b = new DecelerateInterpolator(1.2f);
    private static final Interpolator c = new LinearInterpolator();
    private float f583a;
    private int f584a;
    private Rect f585a;
    private SparseArray<SparseArray<ViewHolder>> f586a;
    private OnAttachStateChangeListener f587a;
    private Adapter<ViewHolder> f588a;
    ItemDecoration f589a;
    private OnFirstLayoutListener f590a;
    private OnFocusLostListener f591a;
    private OnFocusPositionChangedListener f592a;
    private OnItemAnimatorFinishListener f593a;
    private OnItemClickListener f594a;
    private OnItemFocusChangedListener f595a;
    private OnItemStateChangeListener f596a;
    private OnMoveToTheBorderListener f597a;
    private OnScrollListener f598a;
    private b f599a;
    private a f600a;
    private c f601a;
    private f f602a;
    private g f603a;
    private LayoutManager f604a;
    private final b f605a;
    private final g f606a;
    private Object f607a;
    private Runnable f608a;
    boolean f609a;
    private float f610b;
    private int f611b;
    private Rect f612b;
    private SparseArray<View> f613b;
    private Runnable f614b;
    private boolean f615b;
    private float f616c;
    private int f617c;
    private boolean f618c;
    private int d;
    private boolean f619d;
    private boolean e;
    private boolean f;
    private boolean g;
    private boolean h;
    private boolean i;
    protected h mViewFlinger;

    public static abstract class Adapter<VH extends ViewHolder> {
        private a a = new a();

        public abstract int getCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            VH onCreateViewHolder = onCreateViewHolder(parent, viewType);
            onCreateViewHolder.a = viewType;
            return onCreateViewHolder;
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.f621a.b = position;
            onBindViewHolder(holder, position);
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getLastPosition() {
            return getCount() - 1;
        }

        public final void notifyDataSetChanged() {
            this.a.a();
        }

        public final void notifyDataSetChanged(int positionStart, int positionEnd, int count) {
            this.a.a(positionStart, positionEnd, count);
        }

        public final void notifyDataSetUpdate() {
            this.a.b();
        }

        public final void notifyDataSetAdd() {
            this.a.c();
        }

        public final void notifyDataSetAdd(int position) {
            this.a.a(position, 1);
        }

        public final void notifyDataSetAdd(int position, int count) {
            this.a.a(position, count);
        }

        public final void notifyDataSetRemoved(int position) {
            this.a.a(position);
        }

        public final void notifyItemRemoved(int position) {
            this.a.a(position, 1, true);
        }

        public final void notifyItemRemoved(int position, int count) {
            this.a.a(position, count, true);
        }

        public final void notifyItemRemoved(int position, int count, boolean isAnimationRequired) {
            this.a.a(position, count, isAnimationRequired);
        }

        public void registerAdapterDataObserver(b observer) {
            this.a.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(b observer) {
            this.a.unregisterObserver(observer);
        }

        public boolean isFocusable(int position) {
            return true;
        }
    }

    public static abstract class ItemDecoration {
        public abstract int getItemOffsets(int i, BlocksView blocksView);
    }

    public static class LayoutParams extends MarginLayoutParams {
        ViewHolder a;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super(source);
        }

        public int getViewPosition() {
            return this.a.f621a.b;
        }

        public int getViewColumn() {
            return this.a.f621a.a;
        }

        public ViewHolder getViewHolder() {
            return this.a;
        }
    }

    public interface OnFirstLayoutListener {
        void onFirstLayout(ViewGroup viewGroup);
    }

    public interface OnFocusLostListener {
        void onFocusLost(ViewGroup viewGroup, ViewHolder viewHolder);
    }

    public interface OnFocusPositionChangedListener {
        void onFocusPositionChanged(ViewGroup viewGroup, int i, boolean z);
    }

    public interface OnItemAnimatorFinishListener {
        void onItemAnimatorFinished(ViewGroup viewGroup);
    }

    public interface OnItemClickListener {
        void onItemClick(ViewGroup viewGroup, ViewHolder viewHolder);
    }

    public interface OnItemFocusChangedListener {
        void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean z);
    }

    public interface OnItemStateChangeListener {
        void onItemAttached(ViewGroup viewGroup, ViewHolder viewHolder);

        void onItemDetached(ViewGroup viewGroup, ViewHolder viewHolder);
    }

    public interface OnMoveToTheBorderListener {
        void onMoveToTheBorder(ViewGroup viewGroup, View view, int i);
    }

    public static abstract class OnScrollListener {
        public void onScrollBefore(ViewGroup parent, ViewHolder holder) {
        }

        public void onScrollStart(ViewGroup parent) {
        }

        public void onScrollStop(ViewGroup parent) {
        }

        public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        }
    }

    public static abstract class ViewHolder {
        public static final int FLAG_ADDED = 4;
        public static final int FLAG_ATTACHED = 16;
        public static final int FLAG_FIXED = 8;
        public static final int FLAG_REMOVED = 2;
        int a = -1;
        com.gala.video.albumlist.widget.d.a f621a;
        int b = 0;
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        public int getLayoutPosition() {
            return this.f621a.b;
        }

        public int getLayoutColumn() {
            return this.f621a.a;
        }

        public int getItemViewType() {
            return this.a;
        }

        void a() {
            b(8);
        }

        void a(int i) {
            a();
            com.gala.video.albumlist.widget.d.a aVar = this.f621a;
            aVar.b += i;
        }

        void b() {
            this.f621a.b = Integer.MAX_VALUE - this.f621a.b;
        }

        void c() {
            b(2);
            this.f621a.b = Integer.MAX_VALUE - this.f621a.b;
        }

        void b(int i) {
            this.b |= i;
        }

        void c(int i) {
            this.b &= i ^ -1;
        }

        void d() {
            this.b = 0;
        }

        boolean m124a() {
            return (this.b & 2) != 0;
        }

        boolean m125b() {
            return (this.b & 8) != 0;
        }

        boolean m126c() {
            return (this.b & 4) != 0;
        }

        boolean m127d() {
            return (this.b & 16) != 0;
        }
    }

    static class a extends Observable<b> {
        a() {
        }

        public void a() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).a();
            }
        }

        public void a(int i, int i2, int i3) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).a(i, i2, i3);
            }
        }

        public void b() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).b();
            }
        }

        public void c() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).c();
            }
        }

        public void a(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).a(i, i2);
            }
        }

        public void a(int i) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).a(i);
            }
        }

        public void a(int i, int i2, boolean z) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).a(i, i2, z);
            }
        }
    }

    private class b {
        final /* synthetic */ BlocksView a;

        private b(BlocksView blocksView) {
            this.a = blocksView;
        }

        public void a() {
            synchronized (this.a.f617c) {
                this.a.f584a;
                this.a.mViewFlinger.b();
                this.a.f609a = true;
                this.a.f618c = true;
                this.a.requestLayout();
            }
        }

        public void a(int i, int i2, int i3) {
            this.a.f603a = new g(4, i, i2, i3);
            this.a.h();
        }

        public void b() {
            synchronized (this.a.f617c) {
                this.a.f584a;
                this.a.f617c.onUpdateChildren();
            }
        }

        public void c() {
            this.a.f584a;
            this.a.f617c.fastLayoutChildren();
        }

        public void a(int i, int i2) {
            this.a.f603a = new g(1, i, i2);
            this.a.h();
        }

        public void a(int i) {
            this.a.f584a;
            this.a.f617c.onRemoved(i);
        }

        public void a(int i, int i2, boolean z) {
            if (!this.a.f617c) {
                this.a.f603a = new g(2, i, i2);
                this.a.a(z);
            }
        }
    }

    static abstract class c {
        private long a = 120;
        a f622a;
        private long b = 120;
        private long c = 250;

        interface a {
            void a();

            void a(ViewHolder viewHolder);
        }

        static class b implements AnimatorListener {
            b() {
            }

            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }
        }

        public abstract void m128a();

        public abstract void a(ViewHolder viewHolder, d dVar, d dVar2);

        public abstract boolean m129a();

        public abstract void m130b();

        public abstract void b(ViewHolder viewHolder, d dVar, d dVar2);

        public abstract void c(ViewHolder viewHolder, d dVar, d dVar2);

        c() {
        }

        public long a() {
            return this.a;
        }

        public long b() {
            return this.b;
        }

        public long c() {
            return this.c;
        }

        public void a(a aVar) {
            this.f622a = aVar;
        }
    }

    static class d {
        public int a;
        public int b;
        public int c;
        public int d;

        d() {
        }

        public String toString() {
            return this.a + " " + this.b + " " + this.c + " " + this.d;
        }
    }

    class e extends OverScroller {
        private int a;
        final /* synthetic */ BlocksView f623a;

        public e(BlocksView blocksView, Context context, Interpolator interpolator) {
            this.f623a = blocksView;
            super(context, interpolator);
        }

        public boolean equals(Object object) {
            if (!(object instanceof e)) {
                return false;
            }
            e eVar = (e) object;
            if (getFinalX() == eVar.getFinalX() && getFinalY() == eVar.getFinalY() && this.a == eVar.a) {
                return true;
            }
            return false;
        }

        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, duration);
            this.a = duration;
        }
    }

    public interface f {
        void a();
    }

    static class g {
        int a;
        int b;
        int c;
        int d;

        g(int i, int i2, int i3) {
            this.a = i;
            this.c = i2;
            this.d = (i2 + i3) - 1;
            this.b = i3;
        }

        g(int i, int i2, int i3, int i4) {
            this.a = i;
            this.c = i2;
            this.d = i3;
            this.b = i4;
        }
    }

    class h implements Runnable {
        private int a = 0;
        private Interpolator f624a = BlocksView.a();
        private e f625a;
        final /* synthetic */ BlocksView f626a;
        private boolean f627a = false;
        private int b = 0;
        private boolean f628b = false;
        private int c = 0;
        private int d = 0;
        private int e = 0;

        public h(BlocksView blocksView) {
            this.f626a = blocksView;
            this.f625a = new e(blocksView, blocksView.getContext(), BlocksView.a());
        }

        void a() {
            if (this.f627a) {
                this.f628b = true;
                return;
            }
            this.f626a.removeCallbacks(this);
            this.f626a.post(this);
        }

        private int a(int i, int i2) {
            int abs = Math.abs(i);
            int abs2 = Math.abs(i2);
            Object obj = abs > abs2 ? 1 : null;
            int width = obj != null ? this.f626a.getWidth() : this.f626a.getHeight();
            if (obj == null) {
                abs = abs2;
            }
            float f = (float) abs;
            if (this.f626a.f617c == 19) {
                abs = (int) (((double) f) / 1.115d);
            } else {
                abs = (int) (((f / ((float) width)) + 1.0f) * 300.0f);
            }
            return Math.min(abs, 2000);
        }

        public void run() {
            int i = 0;
            c();
            OverScroller overScroller = this.f625a;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i2 = currX - this.d;
                int i3 = currY - this.e;
                this.d = currX;
                this.e = currY;
                this.f626a.a();
                if (i2 != 0) {
                    i3 = i2;
                }
                if (i3 != 0) {
                    i2 = this.f626a.f617c.scrollBy(i3, this.f626a.f584a);
                } else {
                    i2 = 0;
                }
                this.f626a.b();
                this.f626a.invalidate();
                if (!(i3 == 0 || i2 == 0)) {
                    this.f626a.b(2);
                    this.f626a.f617c;
                }
                i2 = (i3 == 0 || i3 == i2) ? 0 : 1;
                if (!this.f625a.equals(overScroller)) {
                    i = 1;
                }
                if (i != 0 || (!overScroller.isFinished() && i2 == 0)) {
                    a();
                } else {
                    this.f626a.b(1);
                }
            } else {
                this.f626a.b(1);
            }
            d();
        }

        private void c() {
            this.f628b = false;
            this.f627a = true;
        }

        private void d() {
            this.f627a = false;
            if (this.f628b) {
                a();
            }
        }

        public void m131a(int i, int i2) {
            a(i, i2, a(i, i2), this.f626a.f617c);
        }

        public void a(int i, int i2, int i3, Interpolator interpolator) {
            this.a = i;
            this.b = i2;
            this.c = this.f626a.b(i3);
            if (this.f624a != interpolator) {
                this.f624a = interpolator;
                this.f625a = new e(this.f626a, this.f626a.getContext(), interpolator);
            }
            e();
        }

        private void e() {
            this.e = 0;
            this.d = 0;
            this.f625a.startScroll(0, 0, this.a, this.b, this.c);
            a();
        }

        public void b() {
            this.f626a.removeCallbacks(this);
            this.f625a.abortAnimation();
        }
    }

    public static class i {
        private int a = -1;
        private BlocksView f629a;
        private boolean f630a = false;
        private int b = -1;

        i(BlocksView blocksView, boolean z) {
            this.f630a = z;
            this.f629a = blocksView;
            this.a = blocksView.getFirstAttachedPosition();
            this.b = blocksView.getLastAttachedPosition();
        }
    }

    public BlocksView(Context context) {
        super(context);
        this.f586a = new SparseArray();
        this.f613b = new SparseArray();
        this.mViewFlinger = new h(this);
        this.f599a = new b();
        this.f618c = true;
        this.f607a = new Object();
        this.f584a = 1;
        this.f611b = 1;
        this.f609a = true;
        this.f583a = 1.0f;
        this.f610b = 1.0f;
        this.f616c = 1.0f;
        this.f619d = true;
        this.d = Service.CISCO_FNA;
        this.e = false;
        this.f606a = new g();
        this.f601a = new b();
        this.h = true;
        this.i = false;
        this.f612b = new Rect();
        this.f608a = new Runnable(this) {
            final /* synthetic */ BlocksView a;

            {
                this.a = r1;
            }

            public void run() {
                if (this.a.f617c == 19) {
                    this.a.a();
                }
            }
        };
        this.f605a = new b(this) {
            final /* synthetic */ BlocksView a;

            {
                this.a = r1;
            }

            public void a(ViewHolder viewHolder, d dVar, d dVar2) {
                this.a.b(viewHolder, dVar, dVar2);
            }

            public void b(ViewHolder viewHolder, d dVar, d dVar2) {
                this.a.a(viewHolder, dVar, dVar2);
            }

            public void c(ViewHolder viewHolder, d dVar, d dVar2) {
                this.a.c(viewHolder, dVar, dVar2);
            }
        };
        this.f600a = new a(this) {
            final /* synthetic */ BlocksView a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.q();
                this.a.g = false;
                if (this.a.f617c == 19) {
                    if (this.a.f584a) {
                        this.a.b();
                    } else {
                        this.a.a();
                    }
                }
                if (this.a.f617c != null) {
                    this.a.f617c.onItemAnimatorFinished(this.a);
                }
            }

            public void a(ViewHolder viewHolder) {
                if (viewHolder.c()) {
                    viewHolder.c(4);
                    this.a.removeView(viewHolder.itemView);
                    this.a.a(viewHolder);
                }
            }
        };
        this.f614b = new Runnable(this) {
            final /* synthetic */ BlocksView a;

            {
                this.a = r1;
            }

            public void run() {
                if (this.a.f617c != null) {
                    this.a.f617c.b();
                }
                this.a.f = false;
            }
        };
        j();
    }

    public BlocksView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlocksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f586a = new SparseArray();
        this.f613b = new SparseArray();
        this.mViewFlinger = new h(this);
        this.f599a = new b();
        this.f618c = true;
        this.f607a = new Object();
        this.f584a = 1;
        this.f611b = 1;
        this.f609a = true;
        this.f583a = 1.0f;
        this.f610b = 1.0f;
        this.f616c = 1.0f;
        this.f619d = true;
        this.d = Service.CISCO_FNA;
        this.e = false;
        this.f606a = new g();
        this.f601a = new b();
        this.h = true;
        this.i = false;
        this.f612b = new Rect();
        this.f608a = /* anonymous class already generated */;
        this.f605a = /* anonymous class already generated */;
        this.f600a = /* anonymous class already generated */;
        this.f614b = /* anonymous class already generated */;
        j();
    }

    public synchronized void setAdapter(Adapter adapter) {
        if (this.f588a != null) {
            this.f588a.unregisterAdapterDataObserver(this.f599a);
            m();
        }
        this.f604a.onAdapterChanged(this.f588a);
        this.f588a = adapter;
        this.f588a.registerAdapterDataObserver(this.f599a);
        this.f609a = true;
        this.f618c = true;
        requestLayout();
    }

    protected void removeUnattachedViews() {
        ViewHolder viewHolder;
        for (int i = 0; i < this.f586a.size(); i++) {
            SparseArray sparseArray = (SparseArray) this.f586a.valueAt(i);
            if (sparseArray != null && sparseArray.size() > 0) {
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    viewHolder = (ViewHolder) sparseArray.valueAt(size);
                    if (!viewHolder.d()) {
                        removeView(viewHolder.itemView);
                    }
                    viewHolder.d();
                }
            }
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            viewHolder = a(getChildAt(childCount));
            if (!(viewHolder == null || viewHolder.d())) {
                a(viewHolder.itemView, true);
                viewHolder.d();
            }
        }
    }

    private void i() {
        for (int i = 0; i < this.f586a.size(); i++) {
            ((SparseArray) this.f586a.valueAt(i)).clear();
        }
        this.f586a.clear();
    }

    public void setOrientation(Orientation orientation) {
        this.f604a.setOrientation(orientation);
    }

    public boolean isVisibleToUser() {
        return getGlobalVisibleRect(this.f612b);
    }

    public boolean isChildVisibleToUser(View child, boolean fully) {
        if (isVisibleToUser()) {
            return isChildVisible(child, fully);
        }
        return false;
    }

    public boolean isChildVisible(int position) {
        return isChildVisible(getViewByPosition(position), false);
    }

    public boolean isChildVisible(int position, boolean fully) {
        return isChildVisible(getViewByPosition(position), fully);
    }

    public boolean isChildVisible(View child, boolean fully) {
        if (!containsView(this, child)) {
            return false;
        }
        if (this.f585a == null) {
            this.f585a = new Rect();
        }
        this.f585a.set(getPaddingLeft() + getScrollX(), getPaddingTop() + getScrollY(), (getWidth() + getScrollX()) - getPaddingRight(), (getHeight() + getScrollY()) - getPaddingBottom());
        if (fully) {
            return this.f585a.contains(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
        return this.f585a.intersect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }

    private void j() {
        setFocusableInTouchMode(true);
        this.f604a = new e(this);
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(true);
        this.f601a.a(this.f600a);
    }

    public void setVerticalMargin(int margin) {
        this.f604a.setVerticalMargin(margin);
        requestLayout();
    }

    public int getVerticalMargin() {
        return this.f604a.getVerticalMargin();
    }

    public void setHorizontalMargin(int margin) {
        this.f604a.setHorizontalMargin(margin);
        requestLayout();
    }

    public int getHorizontalMargin() {
        return this.f604a.getHorizontalMargin();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.d = a(event.getKeyCode());
        this.e = event.getAction() == 1;
        if (this.g) {
            LOG.d("drop the key event when animator start.");
            return true;
        } else if (getChildCount() <= 0) {
            LOG.d("child count is 0.");
            return false;
        } else {
            View focusView = getFocusView();
            if (focusView == null || !focusView.dispatchKeyEvent(event)) {
                switch (event.getKeyCode()) {
                    case 4:
                        if (event.getAction() == 0) {
                            f();
                            break;
                        }
                        break;
                    case 19:
                    case 20:
                        if ((this.f604a.mFocusLoop || this.f604a.getOrientation() == Orientation.VERTICAL) && this.f604a.dispatchKeyEvent(event, this.d)) {
                            return true;
                        }
                    case 21:
                    case 22:
                        if ((this.f604a.mFocusLoop || this.f604a.getOrientation() == Orientation.HORIZONTAL) && this.f604a.dispatchKeyEvent(event, this.d)) {
                            return true;
                        }
                }
                return super.dispatchKeyEvent(event);
            }
            LOG.d("focused view handled the key event.");
            return true;
        }
    }

    private int a(int i) {
        switch (i) {
            case 19:
                return 33;
            case 21:
                return 17;
            case 22:
                return 66;
            default:
                return Service.CISCO_FNA;
        }
    }

    public boolean isQuickSmooth() {
        return this.f617c == 19;
    }

    View a(View view, int i) {
        return super.focusSearch(view, i);
    }

    public View focusSearch(View focused, int direction) {
        View focusSearch = this.f604a.focusSearch(focused, direction);
        if (!containsView(this, focusSearch)) {
            f();
        }
        LOG.d("direction = " + direction + " focused = " + focused + " view = " + focusSearch);
        return focusSearch;
    }

    public static boolean containsView(ViewGroup parent, View view) {
        if (parent == null || view == null) {
            return false;
        }
        ViewGroup parent2 = view.getParent();
        while (parent2 != null && (parent2 instanceof View) && parent2 != parent) {
            view = (View) view.getParent();
            parent2 = view.getParent();
        }
        if (parent2 == parent) {
            return true;
        }
        return false;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        this.f604a.onRequestChildFocus(child, focused);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.f604a.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.f604a == null || !this.f604a.onAddFocusables(views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.f604a.onRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return true;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l();
        if (this.f588a == null) {
            LOG.d("BlocksView", "Adapter should not be null!!!");
            return;
        }
        this.f604a.onLayoutChildren();
        this.f609a = false;
        if (this.f618c) {
            this.f618c = false;
            if (this.f590a != null) {
                this.f590a.onFirstLayout(this);
            }
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.i = true;
        this.f618c = true;
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        if (this.f587a != null && getChildCount() > 0) {
            this.f587a.onViewAttachedToWindow(this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.i = false;
        k();
        if (this.f587a != null && getChildCount() > 0) {
            this.f587a.onViewDetachedFromWindow(this);
        }
    }

    private void k() {
        this.mViewFlinger.b();
        removeCallbacks(this.f614b);
        this.f = false;
        l();
    }

    private void l() {
        this.f601a.a();
        this.g = false;
    }

    public void release() {
        k();
        setDescendantFocusability(393216);
        m();
        this.f604a.resetValues();
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    private void m() {
        n();
        removeAllViews();
        i();
        this.f613b.clear();
    }

    public void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        this.f587a = listener;
    }

    public boolean isAttached() {
        return this.i;
    }

    private void n() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            a(a(getChildAt(childCount)));
        }
    }

    public ViewHolder getViewHolderByPosition(int position) {
        return a(getViewByPosition(position));
    }

    public int getViewPosition(View view) {
        ViewHolder a = a(view);
        if (a == null) {
            return -1;
        }
        return a.getLayoutPosition();
    }

    public int getFocusPosition() {
        return this.f604a.getFocusPosition();
    }

    public void setContentHeight(int contentHeight) {
        this.f604a.setContentHeight(contentHeight);
    }

    public int getContentHeight() {
        return this.f604a.getContentHeight();
    }

    public void setContentWidth(int contentWidth) {
        this.f604a.setContentWidth(contentWidth);
    }

    public int getContentWidth() {
        return this.f604a.getContentWidth();
    }

    void m112a() {
        if (!this.f615b) {
            this.f615b = true;
        }
    }

    void m119b() {
        if (this.f615b) {
            this.f615b = false;
        }
    }

    public void requestLayout() {
        if (!this.f615b) {
            super.requestLayout();
        }
    }

    private Interpolator b() {
        switch (this.f617c) {
            case 17:
                return b;
            case 19:
                return c;
            default:
                return a;
        }
    }

    private int b(int i) {
        switch (this.f617c) {
            case 17:
                return (int) (((float) i) / this.f583a);
            case 18:
                return (int) (((float) i) / this.f610b);
            case 19:
                return (int) (((float) i) / this.f616c);
            default:
                return i;
        }
    }

    public void setScrollRoteScale(float smoothScrollFriction, float contScrollFriction, float quickScrollFriction) {
        if (smoothScrollFriction > 0.0f) {
            this.f583a = smoothScrollFriction;
        }
        if (contScrollFriction > 0.0f) {
            this.f610b = contScrollFriction;
        }
        if (quickScrollFriction > 0.0f) {
            this.f616c = quickScrollFriction;
        }
    }

    void c() {
        post(this.f608a);
    }

    void d() {
        b(17);
    }

    void m123e() {
        b(18);
    }

    public void setQuickFocusLeaveForbidden(boolean isForbidden) {
        this.f619d = isForbidden;
    }

    private View a() {
        View findFocus = this.f604a.findFocus();
        if (containsView(this, findFocus)) {
            return findFocus;
        }
        return this.f604a.getFocusView();
    }

    boolean m117a() {
        View a = a();
        View a2 = a(a);
        if (!containsView(this, a2)) {
            return a(a, a2);
        }
        if (this.f604a.isAtEdge(a2, this.d)) {
            if (hasFocus()) {
                this.f604a.onRequestChildFocus(a2, a2);
            }
            return a(a, a2);
        } else if (isFocusable(getViewPosition(a2))) {
            if (a != a2) {
                if (this.f617c != 19) {
                    b(19);
                    requestChildFocus(null, null);
                    setDescendantFocusability(393216);
                }
                this.f604a.onRequestChildFocus(a2, a2);
            }
            return true;
        } else {
            if (hasFocus() && this.f604a.getScrollingView() != a2) {
                this.f604a.onRequestChildFocus(a2, a2);
            }
            return a(a, a2);
        }
    }

    boolean m120b() {
        return a(null, null);
    }

    public void stopViewFlinger() {
        this.mViewFlinger.b();
        this.f604a.onScrollStop();
    }

    boolean a(View view, View view2) {
        if (this.f617c == 19) {
            if (view == null || view2 == null) {
                view = a();
                view2 = a(view);
            }
            if (this.f604a.isCanScroll(this.d == Service.CISCO_FNA)) {
                stopViewFlinger();
                b(17);
            }
            setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
            if (containsView(this, view2)) {
                if (isFocusable(getViewPosition(view2))) {
                    this.f604a.resumeChildFocus(view2);
                } else {
                    view.requestFocus();
                    this.f604a.onRequestChildFocus(view2, view2);
                }
            } else if (!view.isFocused()) {
                view.requestFocus();
            }
        }
        return this.f619d;
    }

    private void m109b(int i) {
        if (i != this.f617c) {
            this.f617c = i;
        }
    }

    public int getScrollState() {
        return this.f584a;
    }

    private void c(int i) {
        boolean z = true;
        this.f611b = this.f584a;
        this.f584a = i;
        if (this.f611b != 1 && this.f584a == 1) {
            if (this.f617c == 19) {
                LayoutManager layoutManager = this.f604a;
                if (this.d != Service.CISCO_FNA) {
                    z = false;
                }
                if (layoutManager.isCanScroll(z)) {
                    return;
                }
            }
            stopViewFlinger();
        }
        o();
    }

    private void o() {
        if (this.f598a == null) {
            return;
        }
        if (this.f611b != 1 && this.f584a == 1) {
            this.f604a.onScrollStop();
            this.f598a.onScrollStop(this);
        } else if (this.f611b == 1 && this.f584a == 2) {
            this.f598a.onScrollStart(this);
        }
    }

    private void p() {
        if (this.f598a != null && !this.g) {
            this.f598a.onScroll(this, getFirstAttachedPosition(), getLastAttachedPosition(), this.f588a.getCount());
        }
    }

    void f() {
        ViewHolder a = a(getFocusView());
        if (a != null) {
            this.f604a.onFocusLost(a);
            if (this.f591a != null) {
                this.f591a.onFocusLost(this, a);
            }
        }
    }

    void a(int i, boolean z) {
        if (this.f592a != null) {
            this.f592a.onFocusPositionChanged(this, i, z);
        }
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.f598a = l;
    }

    void m113a(int i) {
        if (this.f598a != null) {
            this.f598a.onScrollBefore(this, getViewHolderByPosition(i));
        }
    }

    void g() {
        c(1);
    }

    View a(View view) {
        return this.f604a.focusSearch(view, this.d);
    }

    private SparseArray<ViewHolder> m107a(int i) {
        SparseArray<ViewHolder> sparseArray = (SparseArray) this.f586a.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        sparseArray = new SparseArray();
        this.f586a.put(i, sparseArray);
        return sparseArray;
    }

    private boolean a(ViewHolder viewHolder, int i) {
        return !viewHolder.b() || (viewHolder.b() && viewHolder.getLayoutPosition() == i);
    }

    private boolean b(ViewHolder viewHolder, int i) {
        if (!viewHolder.itemView.isFocused()) {
            return true;
        }
        if (getFocusPosition() < getFirstAttachedPosition() || getFocusPosition() > getLastAttachedPosition()) {
            return false;
        }
        return true;
    }

    private ViewHolder a(int i, int i2) {
        if (i2 != getFocusPosition()) {
            return null;
        }
        View a = a();
        if (a == null) {
            return null;
        }
        ViewHolder a2 = a(a);
        if (a2.getItemViewType() != i || !a(a2, i2) || a2.a() || a2.d()) {
            return null;
        }
        ((SparseArray) this.f586a.get(i)).remove(a2.getLayoutPosition());
        return a2;
    }

    private ViewHolder b(int i, int i2) {
        SparseArray sparseArray = (SparseArray) this.f586a.get(i2);
        if (sparseArray != null && sparseArray.size() > 0) {
            int indexOfKey = sparseArray.indexOfKey(i);
            if (indexOfKey < 0) {
                indexOfKey = 0;
            }
            int i3 = indexOfKey;
            for (int i4 = 0; i4 < sparseArray.size(); i4++) {
                ViewHolder viewHolder = (ViewHolder) sparseArray.valueAt(i3);
                if (!viewHolder.a() && a(viewHolder, i) && b(viewHolder, i)) {
                    sparseArray.removeAt(i3);
                    return viewHolder;
                }
                i3++;
                if (i3 == sparseArray.size()) {
                    i3 = 0;
                }
            }
        }
        return null;
    }

    View a(com.gala.video.albumlist.widget.d.a aVar) {
        int itemViewType = this.f588a.getItemViewType(aVar.b);
        ViewHolder a = a(itemViewType, aVar.b);
        if (a == null) {
            a = b(aVar.b, itemViewType);
        }
        if (a == null) {
            LayoutParams generateDefaultLayoutParams;
            ViewHolder createViewHolder = this.f588a.createViewHolder(this, itemViewType);
            android.view.ViewGroup.LayoutParams layoutParams = createViewHolder.itemView.getLayoutParams();
            if (layoutParams == null) {
                generateDefaultLayoutParams = generateDefaultLayoutParams();
                createViewHolder.itemView.setLayoutParams(generateDefaultLayoutParams);
            } else {
                generateDefaultLayoutParams = (LayoutParams) layoutParams;
            }
            generateDefaultLayoutParams.a = createViewHolder;
            d(createViewHolder.itemView);
            a = createViewHolder;
        }
        a.f621a = aVar.a();
        if (a.b()) {
            a.c(8);
        } else {
            this.f588a.bindViewHolder(a, aVar.b);
        }
        addView(a.itemView);
        return a.itemView;
    }

    void m114a(View view) {
        if (view != null) {
            view.setNextFocusUpId(getNextFocusUpId());
            view.setNextFocusDownId(getNextFocusDownId());
            view.setNextFocusRightId(getNextFocusRightId());
            view.setNextFocusLeftId(getNextFocusLeftId());
        }
    }

    private void d(View view) {
        final OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
        view.setOnFocusChangeListener(new OnFocusChangeListener(this) {
            final /* synthetic */ BlocksView f620a;

            public void onFocusChange(View view, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(view, hasFocus);
                }
                if (this.f620a.f617c != null) {
                    ViewHolder a = this.f620a.a(view);
                    if (a != null && !a.a()) {
                        this.f620a.f617c.onItemFocusChanged(this.f620a, a, hasFocus);
                    }
                }
            }
        });
        view.setOnClickListener(new OnClickListener(this) {
            final /* synthetic */ BlocksView a;

            {
                this.a = r1;
            }

            public void onClick(View view) {
                if (this.a.f617c != null && !this.a.f609a) {
                    this.a.f617c.onItemClick(this.a, this.a.a(view));
                }
            }
        });
        view.setOnKeyListener(new OnKeyListener(this) {
            final /* synthetic */ BlocksView a;

            {
                this.a = r1;
            }

            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || event.getRepeatCount() > 0) {
                    return false;
                }
                if ((keyCode != 23 && keyCode != 66) || this.a.f617c == null || this.a.f609a) {
                    return false;
                }
                this.a.f617c.onItemClick(this.a, this.a.a(view));
                return true;
            }
        });
    }

    public void updateItem(ViewHolder vh, int position) {
        this.f588a.bindViewHolder(vh, position);
    }

    public int getCount() {
        return this.f588a.getCount();
    }

    public int getLastPosition() {
        return this.f588a.getLastPosition();
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mViewFlinger.a(dx, dy);
    }

    public void setViewRecycled(boolean isViewRecycled) {
        this.h = isViewRecycled;
    }

    boolean m121c() {
        return this.h;
    }

    void b(View view) {
        if (view != null) {
            a(a(view));
        }
    }

    void a(View view, boolean z) {
        ViewHolder a = a(view);
        if (z) {
            removeView(view);
        } else {
            if (this.f613b.indexOfValue(view) < 0) {
                LOG.d("view = " + view + " position = " + a.getLayoutPosition() + " flags = " + a.b);
            }
            this.f613b.removeAt(this.f613b.indexOfValue(view));
            a.c(16);
        }
        SparseArray a2 = a(a.a);
        if (a2.indexOfValue(a) < 0) {
            a2.put(a.f621a.b, a);
        }
        if (!a.b()) {
            a(a);
        }
    }

    public void removeView(View view) {
        if (containsView(this, view)) {
            super.removeView(view);
            int indexOfValue = this.f613b.indexOfValue(view);
            if (indexOfValue >= 0) {
                this.f613b.removeAt(indexOfValue);
            }
            ViewHolder a = a(view);
            if (a != null) {
                a.d();
            }
        }
    }

    public void addView(View child) {
        if (!containsView(this, child)) {
            super.addView(child);
        }
        ViewHolder a = a(child);
        if (a != null) {
            if (this.f613b.indexOfKey(a.getLayoutPosition()) >= 0) {
                LOG.e("Warn: key repeat!!! new = " + child + " old = " + this.f613b.valueAt(this.f613b.indexOfKey(a.getLayoutPosition())));
            } else {
                this.f613b.append(a.getLayoutPosition(), child);
            }
            a.b(16);
        }
    }

    ViewHolder m111a(View view) {
        while (view != null && (view instanceof View)) {
            android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && (layoutParams instanceof LayoutParams)) {
                return ((LayoutParams) layoutParams).a;
            }
            view = (View) view.getParent();
        }
        return null;
    }

    void m116a(ViewHolder viewHolder) {
        if (this.f596a != null) {
            this.f596a.onItemDetached(this, viewHolder);
        }
    }

    void c(View view) {
        b(a(view));
    }

    void b(ViewHolder viewHolder) {
        if (this.f596a != null) {
            this.f596a.onItemAttached(this, viewHolder);
        }
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    void h() {
        b(true);
    }

    void a(boolean z) {
        b(z);
    }

    private d a(ViewHolder viewHolder) {
        d dVar = new d();
        if (this.f604a.getOrientation() == Orientation.VERTICAL) {
            dVar.a = viewHolder.itemView.getLeft();
            dVar.b = viewHolder.itemView.getTop() - getScrollY();
            dVar.c = viewHolder.itemView.getRight();
            dVar.d = viewHolder.itemView.getBottom() - getScrollY();
        } else {
            dVar.a = viewHolder.itemView.getLeft() - getScrollX();
            dVar.b = viewHolder.itemView.getTop();
            dVar.c = viewHolder.itemView.getRight() - getScrollX();
            dVar.d = viewHolder.itemView.getBottom();
        }
        return dVar;
    }

    private void b(boolean z) {
        if (this.f603a != null && !this.f609a) {
            int i;
            boolean z2;
            l();
            if (z && e()) {
                i = 1;
            } else {
                z2 = false;
            }
            u();
            if (this.f609a) {
                r();
                this.f604a.onLayoutChildren();
                this.f609a = false;
                s();
            } else {
                q();
                z2 = false;
            }
            if (i != 0 && isVisibleToUser() && VERSION.SDK_INT >= 14) {
                c(1);
                this.f606a.a(this.f605a);
                this.g = this.f601a.a();
            }
        }
    }

    private void q() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = a(getChildAt(i));
            if (a != null) {
                a.c(8);
            }
        }
    }

    private boolean e() {
        g gVar = this.f603a;
        int i = gVar.c;
        int i2 = gVar.b + i;
        switch (gVar.a) {
            case 1:
                return isChildVisible(i);
            case 2:
                if (isChildVisible(i) || isChildVisible(i2)) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private void r() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = a(getChildAt(i));
            this.f606a.a(a, a(a));
        }
    }

    private void s() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = a(getChildAt(i));
            this.f606a.b(a, a(a));
        }
    }

    private void a(ViewHolder viewHolder, d dVar, d dVar2) {
        this.f601a.a(viewHolder, dVar, dVar2);
        t();
    }

    private void b(ViewHolder viewHolder, d dVar, d dVar2) {
        a(viewHolder, dVar);
        this.f601a.b(viewHolder, dVar, dVar2);
        t();
    }

    private void c(ViewHolder viewHolder, d dVar, d dVar2) {
        this.f601a.c(viewHolder, dVar, dVar2);
        t();
    }

    private void a(ViewHolder viewHolder, d dVar) {
        View view = viewHolder.itemView;
        addView(view);
        if (this.f604a.getOrientation() == Orientation.VERTICAL) {
            view.layout(dVar.a, dVar.b + getScrollY(), dVar.c, dVar.d + getScrollY());
        } else {
            view.layout(dVar.a + getScrollX(), dVar.b, dVar.c + getScrollX(), dVar.d);
        }
        viewHolder.b(4);
    }

    private void t() {
        if (!this.f) {
            post(this.f614b);
            this.f = true;
        }
    }

    private void u() {
        g gVar = this.f603a;
        switch (gVar.a) {
            case 1:
                b(gVar);
                return;
            case 2:
                c(gVar);
                return;
            case 4:
                a(gVar);
                return;
            default:
                return;
        }
    }

    private void a(g gVar) {
        a(gVar.c, gVar.d, gVar.b);
    }

    private void a(int i, int i2, int i3) {
        int i4 = ((i + i3) - i2) - 1;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ViewHolder a = a(getChildAt(childCount));
            if (a != null) {
                if (a.f621a.b < i) {
                    a.a();
                } else if (a.f621a.b > i2) {
                    a.a(i4);
                    this.f609a = true;
                } else if (a.f621a.b >= i) {
                    a.b();
                    this.f609a = true;
                }
            }
        }
    }

    private void b(g gVar) {
        a(gVar.c, gVar.b);
        this.f604a.onItemsAdded(gVar.c, gVar.b);
    }

    private void m108a(int i, int i2) {
        if (getChildCount() == 0) {
            this.f609a = true;
            return;
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ViewHolder a = a(getChildAt(childCount));
            if (a != null) {
                if (a.f621a.b >= i) {
                    a.a(i2);
                    this.f609a = true;
                }
                a.a();
            }
        }
    }

    private void c(g gVar) {
        b(gVar.c, gVar.d, gVar.b);
        this.f604a.onItemsRemoved(gVar.c, gVar.b);
    }

    private void b(int i, int i2, int i3) {
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            ViewHolder a = a(getChildAt(i4));
            if (a != null) {
                if (a.f621a.b > i2) {
                    a.a(-i3);
                    this.f609a = true;
                } else if (a.f621a.b >= i) {
                    a.c();
                    this.f609a = true;
                } else {
                    a.a();
                }
            }
        }
    }

    public boolean isFocusable(int position) {
        return this.f588a.isFocusable(position);
    }

    public void setItemDecoration(ItemDecoration decor) {
        this.f589a = decor;
    }

    public Adapter getAdapter() {
        return this.f588a;
    }

    public int getFirstAttachedPosition() {
        return this.f604a.getFirstAttachedPosition();
    }

    public int getLastAttachedPosition() {
        return this.f604a.getLastAttachedPosition();
    }

    public void setFocusPlace(FocusPlace focusPlace) {
        this.f604a.setFocusPlace(focusPlace);
    }

    public void setFocusPlace(int low, int high) {
        this.f604a.setFocusPlace(low, high);
    }

    public void setFocusPosition(int focusPosition) {
        setFocusPosition(focusPosition, false);
    }

    public void setFocusPosition(int focusPosition, boolean scroll) {
        this.f604a.setFocusPosition(focusPosition);
        if (scroll) {
            this.mViewFlinger.b();
            View focusView = this.f604a.getFocusView();
            if (focusView != null) {
                this.f604a.onRequestChildFocus(focusView, focusView);
            }
        }
    }

    public void setFocusMode(int mode) {
        this.f604a.setScrollMode(mode);
    }

    public void setOnFocusLostListener(OnFocusLostListener l) {
        this.f591a = l;
    }

    public void setOnItemFocusChangedListener(OnItemFocusChangedListener l) {
        this.f595a = l;
    }

    public void setOnFocusPositionChangedListener(OnFocusPositionChangedListener l) {
        this.f592a = l;
    }

    public void setOnItemStateChangeListener(OnItemStateChangeListener l) {
        this.f596a = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.f594a = l;
    }

    public void setOnItemAnimatorFinishListener(OnItemAnimatorFinishListener l) {
        this.f593a = l;
    }

    public void setGravity(int gravity) {
        this.f604a.setGravity(gravity);
    }

    public LinkedList<View> getViewList() {
        LinkedList<View> linkedList = new LinkedList();
        for (int i = 0; i < linkedList.size(); i++) {
            linkedList.add(getChildAt(i));
        }
        return linkedList;
    }

    public LayoutManager getLayoutManager() {
        return this.f604a;
    }

    public View getViewByPosition(int position) {
        if (position == -1) {
            return null;
        }
        View view = (View) this.f613b.get(position);
        if (view != null) {
            return view;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (a(childAt).getLayoutPosition() == position) {
                return childAt;
            }
        }
        return view;
    }

    public void setFocusLeaveForbidden(int direction) {
        this.f604a.setFocusLeaveForbidden(direction);
    }

    public void setSpringbackForbidden(int direction) {
        this.f604a.setSpringbackForbidden(direction);
    }

    public void setShakeForbidden(int direction) {
        this.f604a.setShakeForbidden(direction);
    }

    public void setSpringbackDelta(int delta) {
        this.f604a.setSpringbackDelta(delta);
    }

    public int getScrollType() {
        return this.f617c;
    }

    public void setExtraPadding(int extraPadding) {
        this.f604a.setExtraPadding(extraPadding);
    }

    public void setFocusLoop(boolean focusLoop) {
        this.f604a.setFocusLoop(focusLoop);
    }

    public View getFocusView() {
        return this.f604a.getFocusView();
    }

    int m110a() {
        return this.f604a.getMovement();
    }

    public int getColumn(int position) {
        ViewHolder viewHolderByPosition = getViewHolderByPosition(position);
        return viewHolderByPosition != null ? viewHolderByPosition.getLayoutColumn() : -1;
    }

    public int getRow(int position) {
        return 0;
    }

    boolean m122d() {
        return this.f604a.hasScrollOffset();
    }

    int m118b() {
        return this.f604a.getMinScroll();
    }

    public int getDirection() {
        return this.d;
    }

    public void setOnFirstLayoutListener(OnFirstLayoutListener listener) {
        this.f590a = listener;
    }

    void m115a(View view, int i) {
        if (this.f597a != null) {
            this.f597a.onMoveToTheBorder(this, view, i);
        }
    }

    public void setOnMoveToTheBorderListener(OnMoveToTheBorderListener listener) {
        this.f597a = listener;
    }

    protected void lineFeed() {
        if (this.f602a != null) {
            this.f602a.a();
        }
    }

    public void setOnLineFeedListener(f l) {
        this.f602a = l;
    }

    public void setScrollOnly(boolean scrollOnly) {
        this.f604a.setScrollOnly(scrollOnly);
    }

    public void setFocusMemorable(boolean memorable) {
        this.f604a.setFocusMemorable(memorable);
    }

    public i getVisibleViewsIterator(boolean fully) {
        return new i(this, fully);
    }

    public BlockLayout getBlockLayout(int position) {
        return this.f604a.getBlockLayout(position);
    }
}
