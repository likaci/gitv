package com.gala.video.albumlist4.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.database.Observable;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import com.gala.video.albumlist4.utils.LOG;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.LinkedList;
import org.xbill.DNS.WKSRecord.Service;

public class RecyclerView extends ViewGroup {
    public static final int INVALID_TYPE = -1;
    public static final int NO_POSITION = -1;
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
    private float f690a;
    private int f691a;
    private Rect f692a;
    private SparseArray<SparseArray<ViewHolder>> f693a;
    private LayoutManager f694a;
    private Adapter<ViewHolder> f695a;
    ItemDecoration f696a;
    private OnFirstLayoutListener f697a;
    private OnFocusLostListener f698a;
    private OnItemAnimatorFinishListener f699a;
    private OnItemClickListener f700a;
    private OnItemFocusChangedListener f701a;
    private OnItemRecycledListener f702a;
    private OnScrollListener f703a;
    private b f704a;
    private a f705a;
    private c f706a;
    private f f707a;
    private g f708a;
    private final b f709a;
    private final h f710a;
    private Object f711a;
    private final Runnable f712a;
    boolean f713a;
    private float b;
    private int f714b;
    private SparseArray<View> f715b;
    private Runnable f716b;
    private boolean f717b;
    private float c;
    private int f718c;
    private boolean f719c;
    private int d;
    private boolean f720d;
    private int e;
    private boolean f721e;
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
            holder.f723a.c = position;
            onBindViewHolder(holder, position);
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getNumRows(int position) {
            return 0;
        }

        public int getLastPosition() {
            return getCount() - 1;
        }

        public final void notifyDataSetChanged() {
            this.a.a();
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

        public final void notifyDataSetRemoved(int position) {
            this.a.a(position);
        }

        public final void notifyItemRemoved(int position) {
            this.a.b(position, 1);
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
        public abstract int getItemOffsets(int i, RecyclerView recyclerView);
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
            return this.a.f723a.c;
        }

        public int getViewColumn() {
            return this.a.f723a.b;
        }

        public int getViewRow() {
            return this.a.f723a.a;
        }

        public ViewHolder getViewHolder() {
            return this.a;
        }
    }

    public interface OnFirstLayoutListener {
        void onFirstLayout();
    }

    public interface OnFocusLostListener {
        void onFocusLost(ViewGroup viewGroup, ViewHolder viewHolder);
    }

    public interface OnItemAnimatorFinishListener {
        void onItemAnimatorFinished();
    }

    public interface OnItemClickListener {
        void onItemClick(ViewGroup viewGroup, ViewHolder viewHolder);
    }

    public interface OnItemFocusChangedListener {
        void onItemFocusChanged(ViewGroup viewGroup, ViewHolder viewHolder, boolean z);
    }

    public interface OnItemRecycledListener {
        void onItemRecycled(ViewGroup viewGroup, ViewHolder viewHolder);
    }

    public static abstract class OnScrollListener {
        public void onScrollBefore(int position) {
        }

        public void onScrollStart() {
        }

        public void onScrollStop() {
        }

        public void onScroll(ViewParent parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        }
    }

    public static abstract class ViewHolder {
        public static final int FLAG_ADDED = 4;
        public static final int FLAG_ATTACHED = 16;
        public static final int FLAG_FIXED = 8;
        public static final int FLAG_REMOVED = 2;
        int a = -1;
        com.gala.video.albumlist4.widget.d.a f723a;
        int b = 0;
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        public int getLayoutPosition() {
            return this.f723a.c;
        }

        public int getLayoutColumn() {
            return this.f723a.b;
        }

        public int getLayoutRow() {
            return this.f723a.a;
        }

        public int getItemViewType() {
            return this.a;
        }

        void a() {
            a(8);
        }

        void a(int i, boolean z) {
            a();
            com.gala.video.albumlist4.widget.d.a aVar = this.f723a;
            aVar.c += i;
        }

        void a(int i, int i2, boolean z) {
            a(2);
            this.f723a.c = Integer.MAX_VALUE - this.f723a.c;
        }

        void a(int i) {
            this.b |= i;
        }

        void b(int i) {
            this.b &= i ^ -1;
        }

        boolean m177a() {
            return (this.b & 2) != 0;
        }

        boolean b() {
            return (this.b & 8) != 0;
        }

        boolean c() {
            return (this.b & 4) != 0;
        }

        boolean d() {
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

        public void b(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((b) this.mObservers.get(size)).b(i, i2);
            }
        }
    }

    private class b {
        final /* synthetic */ RecyclerView a;

        private b(RecyclerView recyclerView) {
            this.a = recyclerView;
        }

        public void a() {
            synchronized (this.a.f714b) {
                this.a.mViewFlinger.b();
                this.a.f713a = true;
                this.a.h();
                this.a.f714b.clear();
                this.a.requestLayout();
            }
        }

        public void b() {
            synchronized (this.a.f714b) {
                this.a.f714b.onUpdateChildren();
            }
        }

        public void c() {
            this.a.f714b.onLayoutChildren();
        }

        public void a(int i, int i2) {
            if (!this.a.f714b) {
                this.a.h = true;
                this.a.f708a = new g(1, i, i2, null);
                d();
            }
        }

        public void a(int i) {
            this.a.f714b.onRemoved(i);
        }

        public void b(int i, int i2) {
            if (!this.a.f714b) {
                this.a.h = true;
                this.a.f708a = new g(2, i, i2, null);
                d();
            }
        }

        void d() {
            this.a.post(this.a.f714b);
        }
    }

    static abstract class c {
        private long a = 120;
        a f724a;
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

        public abstract void m178a();

        public abstract void a(ViewHolder viewHolder, d dVar, d dVar2);

        public abstract void b(ViewHolder viewHolder, d dVar, d dVar2);

        public abstract void m179c();

        public abstract void c(ViewHolder viewHolder, d dVar, d dVar2);

        c() {
        }

        public long c() {
            return this.a;
        }

        public long b() {
            return this.b;
        }

        public long a() {
            return this.c;
        }

        public void a(a aVar) {
            this.f724a = aVar;
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
        final /* synthetic */ RecyclerView f725a;

        public e(RecyclerView recyclerView, Context context, Interpolator interpolator) {
            this.f725a = recyclerView;
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
        Object f726a;
        int b;
        int c;

        g(int i, int i2, int i3, Object obj) {
            this.a = i;
            this.c = i2;
            this.b = i3;
            this.f726a = obj;
        }
    }

    class h implements Runnable {
        private int a = 0;
        private Interpolator f727a = RecyclerView.a;
        private e f728a;
        final /* synthetic */ RecyclerView f729a;
        private boolean f730a = false;
        private int b = 0;
        private boolean f731b = false;
        private int c = 0;
        private int d = 0;
        private int e = 0;

        public h(RecyclerView recyclerView) {
            this.f729a = recyclerView;
            this.f728a = new e(recyclerView, recyclerView.getContext(), RecyclerView.a);
        }

        void a() {
            if (this.f730a) {
                this.f731b = true;
                return;
            }
            this.f729a.removeCallbacks(this);
            this.f729a.post(this);
        }

        private int a(int i, int i2) {
            int abs = Math.abs(i);
            int abs2 = Math.abs(i2);
            Object obj = abs > abs2 ? 1 : null;
            int width = obj != null ? this.f729a.getWidth() : this.f729a.getHeight();
            if (obj == null) {
                abs = abs2;
            }
            return Math.min((int) (((((float) abs) / ((float) width)) + 1.0f) * 300.0f), 2000);
        }

        public void run() {
            int i = 0;
            c();
            OverScroller overScroller = this.f728a;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i2 = currX - this.d;
                int i3 = currY - this.e;
                this.d = currX;
                this.e = currY;
                RecyclerView.a;
                if (i2 != 0) {
                    i3 = i2;
                }
                if (i3 != 0) {
                    i2 = this.f729a.f714b.scrollBy(i3, this.f729a.f714b);
                } else {
                    i2 = 0;
                }
                this.f729a.b();
                this.f729a.invalidate();
                if (!(i3 == 0 || i2 == 0)) {
                    this.f729a.b(2);
                    this.f729a.f714b;
                }
                i2 = (i3 == 0 || i3 == i2) ? 0 : 1;
                if (!this.f728a.equals(overScroller)) {
                    i = 1;
                }
                if (i != 0 || (!overScroller.isFinished() && i2 == 0)) {
                    a();
                } else {
                    this.f729a.b(1);
                }
            } else {
                this.f729a.b(1);
            }
            d();
        }

        private void c() {
            this.f731b = false;
            this.f730a = true;
        }

        private void d() {
            this.f730a = false;
            if (this.f731b) {
                a();
            }
        }

        public void m180a(int i, int i2) {
            a(i, i2, a(i, i2), this.f727a);
        }

        public void a(int i, int i2, int i3, Interpolator interpolator) {
            this.a = i;
            this.b = i2;
            this.c = this.f729a.b(i3);
            if (this.f727a != interpolator) {
                this.f727a = interpolator;
                this.f728a = new e(this.f729a, this.f729a.getContext(), interpolator);
            }
            e();
        }

        private void e() {
            this.e = 0;
            this.d = 0;
            this.f728a.startScroll(0, 0, this.a, this.b, this.c);
            a();
        }

        public void b() {
            this.f729a.removeCallbacks(this);
            this.f728a.abortAnimation();
        }
    }

    public static class i {
        private int a = -1;
        private RecyclerView f732a;
        private boolean f733a = false;
        private int b = -1;

        i(RecyclerView recyclerView, boolean z) {
            this.f733a = z;
            this.f732a = recyclerView;
            this.a = recyclerView.getFirstAttachedPosition();
            this.b = recyclerView.getLastAttachedPosition();
        }
    }

    public RecyclerView(Context context) {
        super(context);
        this.f693a = new SparseArray();
        this.f715b = new SparseArray();
        this.mViewFlinger = new h(this);
        this.f704a = new b();
        this.f720d = true;
        this.f711a = new Object();
        this.f691a = 1;
        this.f714b = 1;
        this.f718c = 1;
        this.f713a = true;
        this.f690a = 1.0f;
        this.b = 1.0f;
        this.c = 1.0f;
        this.f721e = true;
        this.e = Service.CISCO_FNA;
        this.f = false;
        this.f710a = new h();
        this.f706a = new b();
        this.i = true;
        this.f712a = new Runnable(this) {
            final /* synthetic */ RecyclerView a;

            {
                this.a = r1;
            }

            public void run() {
                this.a.p();
            }
        };
        this.f709a = new b(this) {
            final /* synthetic */ RecyclerView a;

            {
                this.a = r1;
            }

            public void a(ViewHolder viewHolder, d dVar, d dVar2) {
                this.a.a(viewHolder);
                this.a.b(viewHolder, dVar, dVar2);
            }

            public void b(ViewHolder viewHolder, d dVar, d dVar2) {
                this.a.a(viewHolder, dVar, dVar2);
            }

            public void c(ViewHolder viewHolder, d dVar, d dVar2) {
                this.a.c(viewHolder, dVar, dVar2);
            }
        };
        this.f705a = new a(this) {
            final /* synthetic */ RecyclerView a;

            {
                this.a = r1;
            }

            public void a() {
                this.a.h = false;
                if (this.a.f714b != null) {
                    this.a.f714b.onItemAnimatorFinished();
                }
            }

            public void a(ViewHolder viewHolder) {
                viewHolder.b(2);
                if (viewHolder.c()) {
                    viewHolder.b(4);
                    this.a.removeView(viewHolder.itemView);
                    this.a.b(viewHolder);
                }
            }
        };
        this.f716b = new Runnable(this) {
            final /* synthetic */ RecyclerView a;

            {
                this.a = r1;
            }

            public void run() {
                if (this.a.f714b != null) {
                    this.a.f714b.a();
                }
                this.a.g = false;
            }
        };
        i();
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f693a = new SparseArray();
        this.f715b = new SparseArray();
        this.mViewFlinger = new h(this);
        this.f704a = new b();
        this.f720d = true;
        this.f711a = new Object();
        this.f691a = 1;
        this.f714b = 1;
        this.f718c = 1;
        this.f713a = true;
        this.f690a = 1.0f;
        this.b = 1.0f;
        this.c = 1.0f;
        this.f721e = true;
        this.e = Service.CISCO_FNA;
        this.f = false;
        this.f710a = new h();
        this.f706a = new b();
        this.i = true;
        this.f712a = /* anonymous class already generated */;
        this.f709a = /* anonymous class already generated */;
        this.f705a = /* anonymous class already generated */;
        this.f716b = /* anonymous class already generated */;
        i();
    }

    public synchronized void setAdapter(Adapter adapter) {
        if (this.f695a != null) {
            this.f695a.unregisterAdapterDataObserver(this.f704a);
            g();
        }
        this.f694a.onAdapterChanged(this.f695a);
        this.f695a = adapter;
        this.f695a.registerAdapterDataObserver(this.f704a);
        this.f713a = true;
        requestLayout();
    }

    private void g() {
        removeAllViews();
        h();
    }

    protected void removeUnattachedViews() {
        for (int i = 0; i < this.f693a.size(); i++) {
            SparseArray sparseArray = (SparseArray) this.f693a.valueAt(i);
            if (sparseArray != null && sparseArray.size() > 0) {
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    ViewHolder viewHolder = (ViewHolder) sparseArray.valueAt(size);
                    if (!(viewHolder == null || viewHolder.d())) {
                        removeView(viewHolder.itemView);
                    }
                }
            }
        }
    }

    private void h() {
        for (int i = 0; i < this.f693a.size(); i++) {
            ((SparseArray) this.f693a.valueAt(i)).clear();
        }
        this.f693a.clear();
    }

    public void setOrientation(Orientation orientation) {
        this.f694a.setOrientation(orientation);
    }

    public boolean isChildVisible(View child, boolean fully) {
        if (!a((ViewGroup) this, child)) {
            return false;
        }
        if (this.f692a == null) {
            this.f692a = new Rect();
        }
        this.f692a.set(getPaddingLeft() + getScrollX(), getPaddingTop() + getScrollY(), (getWidth() + getScrollX()) - getPaddingRight(), (getHeight() + getScrollY()) - getPaddingBottom());
        if (fully) {
            return this.f692a.contains(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
        return this.f692a.intersect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }

    private void i() {
        setFocusableInTouchMode(true);
        this.f694a = new e(this);
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(true);
        this.f706a.a(this.f705a);
    }

    public void setVerticalMargin(int margin) {
        this.f694a.setVerticalMargin(margin);
        requestLayout();
    }

    public int getVerticalMargin() {
        return this.f694a.getVerticalMargin();
    }

    public void setHorizontalMargin(int margin) {
        this.f694a.setHorizontalMargin(margin);
        requestLayout();
    }

    public int getHorizontalMargin() {
        return this.f694a.getHorizontalMargin();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.e = a(event.getKeyCode());
        if (this.h) {
            LOG.d("drop the key event when animator start.");
            return true;
        } else if (getChildCount() <= 0) {
            LOG.d("child count is 0.");
            return false;
        } else {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                    if ((this.f694a.mFocusLoop || this.f694a.getOrientation() == Orientation.VERTICAL) && this.f694a.dispatchKeyEvent(event, this.e)) {
                        return true;
                    }
                case 21:
                case 22:
                    if ((this.f694a.mFocusLoop || this.f694a.getOrientation() == Orientation.HORIZONTAL) && this.f694a.dispatchKeyEvent(event, this.e)) {
                        return true;
                    }
            }
            return super.dispatchKeyEvent(event);
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
        return this.d == 19;
    }

    View a(View view, int i) {
        return super.focusSearch(view, i);
    }

    public View focusSearch(View focused, int direction) {
        View focusSearch = this.f694a.focusSearch(focused, direction);
        if (indexOfChild(focusSearch) >= 0 && !isFocusable(getViewPosition(focusSearch))) {
            if (this.f694a.getScrollingView() != focusSearch) {
                this.f694a.onRequestChildFocus(focusSearch, focusSearch);
            }
            focusSearch = focused;
        } else if (!a((ViewGroup) this, focusSearch)) {
            o();
        }
        LOG.d("direction = " + direction + " focused = " + focused + " view = " + focusSearch);
        return focusSearch;
    }

    private static boolean a(ViewGroup viewGroup, View view) {
        if (viewGroup == null || view == null) {
            return false;
        }
        if (viewGroup.indexOfChild(view) >= 0) {
            return true;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if ((childAt instanceof ViewGroup) && a((ViewGroup) childAt, view)) {
                return true;
            }
        }
        return false;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        this.f694a.onRequestChildFocus(child, focused);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.f694a.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.f694a == null || !this.f694a.onAddFocusables(views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        this.f = false;
        if (this.f694a.gridOnRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return true;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    public boolean hasFocus() {
        return this.f ? false : super.hasFocus();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        j();
    }

    private void j() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                this.f694a.measureChild(childAt);
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.f695a == null) {
            LOG.d("RecyclerView", "Adapter should not be null!!!");
            return;
        }
        this.f694a.onLayoutChildren();
        this.f713a = false;
        k();
        if (this.f720d && getChildCount() > 0) {
            this.f720d = false;
            if (this.f697a != null) {
                this.f697a.onFirstLayout();
            }
        }
    }

    private void k() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                childAt.layout(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
            }
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f720d = true;
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mViewFlinger.b();
        removeCallbacks(this.f716b);
        this.g = false;
        if (this.f706a != null) {
            this.f706a.c();
        }
        this.f694a.doOnDetachedFromWindow();
        l();
    }

    private void l() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            b(a(getChildAt(childCount)));
        }
    }

    ViewHolder m165a(int i) {
        return b(getViewByPosition(i));
    }

    private ViewHolder b(View view) {
        if (!a((ViewGroup) this, view)) {
            return null;
        }
        while (view != null && view.getParent() != this) {
            view = (View) view.getParent();
        }
        return ((LayoutParams) view.getLayoutParams()).a;
    }

    public int getViewPosition(View view) {
        ViewHolder b = b(view);
        if (b == null) {
            return -1;
        }
        return b.getLayoutPosition();
    }

    public int getFocusPosition() {
        return this.f694a.getFocusPosition();
    }

    public void setContentHeight(int contentHeight) {
        this.f694a.setContentHeight(contentHeight);
    }

    public int getContentHeight() {
        return this.f694a.getContentHeight();
    }

    public void setContentWidth(int contentWidth) {
        this.f694a.setContentWidth(contentWidth);
    }

    public int getContentWidth() {
        return this.f694a.getContentWidth();
    }

    void m167a() {
        if (!this.f717b) {
            this.f717b = true;
        }
    }

    void m173b() {
        if (this.f717b) {
            this.f717b = false;
            if (this.f719c) {
                requestLayout();
                this.f719c = false;
            }
        }
    }

    public void requestLayout() {
        if (this.f717b) {
            this.f719c = true;
        } else {
            super.requestLayout();
        }
    }

    private Interpolator b() {
        switch (this.d) {
            case 17:
                return new DecelerateInterpolator(1.2f);
            case 19:
                return new LinearInterpolator();
            default:
                return a;
        }
    }

    private int b(int i) {
        switch (this.d) {
            case 17:
                return (int) (((float) i) / this.f690a);
            case 18:
                return (int) (((float) i) / this.b);
            case 19:
                return (int) (((float) i) / this.c);
            default:
                return i;
        }
    }

    public void setScrollRoteScale(float smoothScrollFriction, float contScrollFriction, float quickScrollFriction) {
        if (smoothScrollFriction > 0.0f) {
            this.f690a = smoothScrollFriction;
        }
        if (contScrollFriction > 0.0f) {
            this.b = contScrollFriction;
        }
        if (quickScrollFriction > 0.0f) {
            this.c = quickScrollFriction;
        }
    }

    void c() {
        if (this.d != 19) {
            focusSearch(findFocus(), this.e);
        } else if (!a) {
            b();
        }
    }

    void d() {
        b(17);
    }

    void e() {
        b(18);
    }

    public void setQuickFocusLeaveForbidden(boolean isForbidden) {
        this.f721e = isForbidden;
    }

    boolean m171a() {
        View findFocus = findFocus();
        if (findFocus == null) {
            findFocus = this.f694a.getFocusView();
        }
        View a = a(findFocus);
        if ((findFocus == a && this.f694a.isAtEdge(a)) || !a((ViewGroup) this, a)) {
            return false;
        }
        if (!isFocusable(getViewPosition(a))) {
            if (hasFocus() && this.f694a.getScrollingView() != a) {
                this.f694a.onRequestChildFocus(a, a);
            }
            return false;
        } else if (findFocus == a) {
            return this.f694a.isNeedRequestFocus();
        } else {
            if (this.d != 19) {
                b(19);
                requestChildFocus(null, null);
                setDescendantFocusability(393216);
            }
            this.f694a.onRequestChildFocus(a, a);
            return true;
        }
    }

    boolean m174b() {
        if (this.d == 19) {
            setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
            View focusView = this.f694a.getFocusView();
            View a = a(focusView);
            this.mViewFlinger.b();
            b(17);
            if (indexOfChild(a) >= 0) {
                if (isFocusable(getViewPosition(a))) {
                    this.f694a.resumeChildFocus(a);
                } else {
                    focusView.requestFocus();
                    this.f694a.onRequestChildFocus(a, a);
                }
            } else if (!focusView.isFocused()) {
                focusView.requestFocus();
            }
        }
        return this.f721e;
    }

    private void m163b(int i) {
        if (i != this.d) {
            this.d = i;
            this.mViewFlinger.f727a = b();
            this.mViewFlinger.f728a = new e(this, getContext(), this.mViewFlinger.f727a);
        }
    }

    private void c(int i) {
        this.f718c = this.f714b;
        this.f714b = i;
        if (this.f714b == 1) {
            this.mViewFlinger.b();
        }
        n();
    }

    private void m() {
        if (this.f703a != null && !this.h) {
            this.f703a.onScroll(this, getFirstAttachedPosition(), getLastAttachedPosition(), this.f695a.getCount());
        }
    }

    private void n() {
        if (this.f703a == null) {
            return;
        }
        if (this.f718c != 1 && this.f714b == 1) {
            this.f703a.onScrollStop();
        } else if (this.f718c == 1 && this.f714b == 2) {
            this.f703a.onScrollStart();
        }
    }

    private void o() {
        this.f = true;
        ViewHolder a = a(getFocusView());
        if (a != null) {
            this.f694a.onFocusLost(a);
            if (this.f698a != null) {
                this.f698a.onFocusLost(this, a);
            }
        }
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.f703a = l;
    }

    void m168a(int i) {
        if (this.f703a != null) {
            this.f703a.onScrollBefore(i);
        }
    }

    void f() {
        c(1);
    }

    View a(View view) {
        return this.f694a.focusSearch(view, this.e);
    }

    private SparseArray<ViewHolder> m162a(int i) {
        SparseArray<ViewHolder> sparseArray = (SparseArray) this.f693a.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        sparseArray = new SparseArray();
        this.f693a.put(i, sparseArray);
        return sparseArray;
    }

    private boolean a(ViewHolder viewHolder, int i) {
        boolean z = !viewHolder.b() || (viewHolder.b() && viewHolder.getLayoutPosition() == i);
        if (z) {
            viewHolder.b(8);
        }
        return z;
    }

    private ViewHolder a(int i, int i2) {
        View focusedChild = getFocusedChild();
        if (focusedChild != null) {
            ViewHolder a = a(focusedChild);
            if (a.getItemViewType() == i && a(a, i2) && !a.a() && !a.d()) {
                ((SparseArray) this.f693a.get(i)).remove(a.getLayoutPosition());
                return a;
            }
        }
        return null;
    }

    private ViewHolder b(int i, int i2) {
        SparseArray sparseArray = (SparseArray) this.f693a.get(i2);
        if (sparseArray != null && sparseArray.size() > 0) {
            int indexOfKey = sparseArray.indexOfKey(i);
            int i3 = indexOfKey < 0 ? 0 : indexOfKey;
            ViewHolder viewHolder = (ViewHolder) sparseArray.valueAt(i3);
            if (!viewHolder.a() && a(viewHolder, i)) {
                sparseArray.removeAt(i3);
                return viewHolder;
            }
        }
        return null;
    }

    View a(com.gala.video.albumlist4.widget.d.a aVar) {
        int itemViewType = this.f695a.getItemViewType(aVar.c);
        ViewHolder a = a(itemViewType, aVar.c);
        if (a == null) {
            a = b(aVar.c, itemViewType);
        }
        if (a == null) {
            LayoutParams generateDefaultLayoutParams;
            ViewHolder createViewHolder = this.f695a.createViewHolder(this, itemViewType);
            android.view.ViewGroup.LayoutParams layoutParams = createViewHolder.itemView.getLayoutParams();
            if (layoutParams == null) {
                generateDefaultLayoutParams = generateDefaultLayoutParams();
                createViewHolder.itemView.setLayoutParams(generateDefaultLayoutParams);
            } else {
                generateDefaultLayoutParams = (LayoutParams) layoutParams;
            }
            generateDefaultLayoutParams.a = createViewHolder;
            c(createViewHolder.itemView);
            a = createViewHolder;
        }
        a.f723a = aVar.a();
        b(a.itemView);
        this.f695a.bindViewHolder(a, aVar.c);
        this.f715b.append(aVar.c, a.itemView);
        a.a(16);
        if (indexOfChild(a.itemView) < 0) {
            addView(a.itemView);
        }
        return a.itemView;
    }

    private void m164b(View view) {
        if (view != null) {
            view.setNextFocusUpId(getNextFocusUpId());
            view.setNextFocusDownId(getNextFocusDownId());
            view.setNextFocusRightId(getNextFocusRightId());
            view.setNextFocusLeftId(getNextFocusLeftId());
        }
    }

    private void c(View view) {
        final OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
        view.setOnFocusChangeListener(new OnFocusChangeListener(this) {
            final /* synthetic */ RecyclerView f722a;

            public void onFocusChange(View view, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(view, hasFocus);
                }
                if (this.f722a.f714b != null) {
                    this.f722a.f714b.onItemFocusChanged(this.f722a, this.f722a.a(view), hasFocus);
                }
            }
        });
        view.setOnClickListener(new OnClickListener(this) {
            final /* synthetic */ RecyclerView a;

            {
                this.a = r1;
            }

            public void onClick(View view) {
                if (this.a.f714b != null && !this.a.f713a) {
                    this.a.f714b.onItemClick(this.a, this.a.a(view));
                }
            }
        });
        view.setOnKeyListener(new OnKeyListener(this) {
            final /* synthetic */ RecyclerView a;

            {
                this.a = r1;
            }

            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() != 0 || event.getRepeatCount() > 0) {
                    return false;
                }
                if ((keyCode != 23 && keyCode != 66) || this.a.f714b == null || this.a.f713a) {
                    return false;
                }
                this.a.f714b.onItemClick(this.a, this.a.a(view));
                return true;
            }
        });
    }

    public void updateItem(ViewHolder vh, int position) {
        this.f695a.bindViewHolder(vh, position);
    }

    public int getCount() {
        return this.f695a.getCount();
    }

    public int getLastPosition() {
        return this.f695a.getLastPosition();
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mViewFlinger.a(dx, dy);
    }

    void m170a(ViewHolder viewHolder) {
        a(viewHolder.a).delete(viewHolder.f723a.c);
    }

    public void setViewRecycled(boolean isViewRecycled) {
        this.i = isViewRecycled;
    }

    boolean m175c() {
        return this.i;
    }

    void m169a(View view) {
        if (view != null) {
            b(a(view));
        }
    }

    void a(View view, boolean z) {
        if (z) {
            removeView(view);
        }
        ViewHolder a = a(view);
        int i = a.f723a.c;
        this.f715b.remove(i);
        a.b(16);
        SparseArray a2 = a(a.a);
        if (a2.indexOfValue(a) < 0) {
            a2.put(i, a);
        }
        b(a);
    }

    ViewHolder m166a(View view) {
        if (view != null) {
            return ((LayoutParams) view.getLayoutParams()).a;
        }
        return null;
    }

    void b(ViewHolder viewHolder) {
        if (this.f702a != null && !this.h) {
            this.f702a.onItemRecycled(this, viewHolder);
        }
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    private d a(ViewHolder viewHolder) {
        d dVar = new d();
        if (this.f694a.getOrientation() == Orientation.VERTICAL) {
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

    private void p() {
        if (this.f708a != null) {
            t();
            q();
            this.f694a.onLayoutChildren();
            this.f713a = false;
            r();
            if (VERSION.SDK_INT >= 14) {
                this.f710a.a(this.f709a);
            }
        }
    }

    private void q() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = a(getChildAt(i));
            this.f710a.a(a, a(a));
        }
    }

    private void r() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = a(getChildAt(i));
            this.f710a.b(a, a(a));
        }
    }

    private void a(ViewHolder viewHolder, d dVar, d dVar2) {
        this.f706a.a(viewHolder, dVar, dVar2);
        s();
    }

    private void b(ViewHolder viewHolder, d dVar, d dVar2) {
        a(viewHolder, dVar);
        this.f706a.b(viewHolder, dVar, dVar2);
        s();
    }

    private void c(ViewHolder viewHolder, d dVar, d dVar2) {
        this.f706a.c(viewHolder, dVar, dVar2);
        s();
    }

    private void a(ViewHolder viewHolder, d dVar) {
        View view = viewHolder.itemView;
        if ((view.getParent() == this ? 1 : null) == null) {
            addView(view);
        }
        if (this.f694a.getOrientation() == Orientation.VERTICAL) {
            view.layout(dVar.a, dVar.b + getScrollY(), dVar.c, dVar.d + getScrollY());
        } else {
            view.layout(dVar.a + getScrollX(), dVar.b, dVar.c + getScrollX(), dVar.d);
        }
        viewHolder.a(4);
    }

    private void s() {
        if (!this.g) {
            post(this.f716b);
            this.g = true;
        }
    }

    private void t() {
        g gVar = this.f708a;
        switch (gVar.a) {
            case 1:
                a(gVar);
                return;
            case 2:
                b(gVar);
                return;
            default:
                return;
        }
    }

    private void a(g gVar) {
        a(gVar.c, gVar.b, true);
        this.f694a.onItemsAdded(gVar.c, gVar.b);
    }

    private void a(int i, int i2, boolean z) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            ViewHolder a = a(getChildAt(i3));
            if (a != null) {
                if (a.f723a.c >= i) {
                    a.a(i2, z);
                    this.f713a = true;
                } else {
                    a.a();
                }
            }
        }
        requestLayout();
    }

    private void b(g gVar) {
        b(gVar.c, gVar.b, true);
        this.f694a.onItemsRemoved(gVar.c, gVar.b);
    }

    private void b(int i, int i2, boolean z) {
        int i3 = i + i2;
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            ViewHolder a = a(getChildAt(i4));
            if (a != null) {
                if (a.f723a.c >= i3) {
                    a.a(-i2, z);
                    this.f713a = true;
                } else if (a.f723a.c >= i) {
                    a.a(i - 1, -i2, z);
                    this.f713a = true;
                } else {
                    a.a();
                }
            }
        }
        requestLayout();
    }

    public int getNumRows(int position) {
        int numRows = this.f695a.getNumRows(position);
        if (numRows == 0) {
            numRows = this.f691a;
            if (numRows == 0) {
                throw new AndroidRuntimeException("Row number can't be zero!!!");
            }
        }
        return numRows;
    }

    public boolean isFocusable(int position) {
        return this.f695a.isFocusable(position);
    }

    public void setItemDecoration(ItemDecoration decor) {
        this.f696a = decor;
    }

    public Adapter getAdapter() {
        return this.f695a;
    }

    public void setNumRows(int numRows) {
        if (numRows == 0) {
            throw new AndroidRuntimeException("Row number can't be zero!!!");
        }
        this.f691a = numRows;
        this.f694a.setNumRows(numRows);
    }

    public int getNumRows() {
        return this.f694a.getNumRows();
    }

    public int getFirstAttachedPosition() {
        return this.f694a.getFirstAttachedPosition();
    }

    public int getLastAttachedPosition() {
        return this.f694a.getLastAttachedPosition();
    }

    public void setFocusPlace(FocusPlace focusPlace) {
        this.f694a.setFocusPlace(focusPlace);
    }

    public void setFocusPlace(int low, int high) {
        this.f694a.setFocusPlace(low, high);
    }

    public void setFocusPosition(int focusPosition) {
        setFocusPosition(focusPosition, false);
    }

    public void setFocusPosition(int focusPosition, boolean scroll) {
        this.f694a.setFocusPosition(focusPosition);
        if (scroll) {
            this.mViewFlinger.b();
            View focusView = this.f694a.getFocusView();
            if (focusView != null) {
                this.f694a.onRequestChildFocus(focusView, focusView);
            }
        }
    }

    public void setFocusMode(int mode) {
        this.f694a.setScrollMode(mode);
    }

    public void setOnFocusLostListener(OnFocusLostListener l) {
        this.f698a = l;
    }

    public void setOnItemFocusChangedListener(OnItemFocusChangedListener l) {
        this.f701a = l;
    }

    public void setOnItemRecycledListener(OnItemRecycledListener l) {
        this.f702a = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.f700a = l;
    }

    public void setOnItemAnimatorFinishListener(OnItemAnimatorFinishListener l) {
        this.f699a = l;
    }

    public void setGravity(int gravity) {
        this.f694a.setGravity(gravity);
    }

    public LinkedList<View> getViewList() {
        LinkedList<View> linkedList = new LinkedList();
        for (int i = 0; i < linkedList.size(); i++) {
            linkedList.add(getChildAt(i));
        }
        return linkedList;
    }

    public View getViewByPosition(int position) {
        if (position == -1) {
            return null;
        }
        View view = (View) this.f715b.get(position);
        if (view != null) {
            return view;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            ViewHolder a = a(childAt);
            if (a.getLayoutPosition() == position && a.d()) {
                return childAt;
            }
        }
        return view;
    }

    public void setFocusLeaveForbidden(int direction) {
        this.f694a.setFocusLeaveForbidden(direction);
    }

    public void setSpringbackForbidden(int direction) {
        this.f694a.setSpringbackForbidden(direction);
    }

    public void setShakeForbidden(int direction) {
        this.f694a.setShakeForbidden(direction);
    }

    public void setSpringbackDelta(int delta) {
        this.f694a.setSpringbackDelta(delta);
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        super.setNextFocusUpId(nextFocusUpId);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setNextFocusUpId(nextFocusUpId);
        }
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        super.setNextFocusDownId(nextFocusDownId);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setNextFocusDownId(nextFocusDownId);
        }
    }

    public void setNextFocusLeftId(int nextFocusLeftId) {
        super.setNextFocusLeftId(nextFocusLeftId);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setNextFocusLeftId(nextFocusLeftId);
        }
    }

    public void setNextFocusRightId(int nextFocusRightId) {
        super.setNextFocusRightId(nextFocusRightId);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setNextFocusRightId(nextFocusRightId);
        }
    }

    public int getScrollType() {
        return this.d;
    }

    public void setExtraPadding(int extraPadding) {
        this.f694a.setExtraPadding(extraPadding);
    }

    public void setFocusLoop(boolean focusLoop) {
        this.f694a.setFocusLoop(focusLoop);
    }

    public View getFocusView() {
        return this.f694a.getFocusView();
    }

    int a() {
        return this.f694a.getMovement();
    }

    public int getColumn(int position) {
        int layoutColumn;
        ViewHolder a = a(position);
        if (a != null) {
            layoutColumn = a.getLayoutColumn();
        } else {
            layoutColumn = -1;
        }
        if (layoutColumn != -1) {
            return layoutColumn;
        }
        int i = 0;
        int i2 = 0;
        int i3 = layoutColumn;
        while (i <= position) {
            layoutColumn = getNumRows(i);
            if (i2 != layoutColumn) {
                i2 = 0;
            } else {
                layoutColumn = i3 + 1;
                if (layoutColumn == i2) {
                    layoutColumn = i2;
                    i2 = 0;
                } else {
                    int i4 = i2;
                    i2 = layoutColumn;
                    layoutColumn = i4;
                }
            }
            i++;
            i3 = i2;
            i2 = layoutColumn;
        }
        return i3;
    }

    public int getRow(int position) {
        ViewHolder a = a(position);
        int layoutRow = a != null ? a.getLayoutRow() : -1;
        if (layoutRow < 0) {
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (i <= position) {
                int numRows = getNumRows(i);
                if (i2 != numRows) {
                    layoutRow++;
                    i2 = 0;
                } else {
                    numRows = i3 + 1;
                    if (numRows == i2) {
                        layoutRow++;
                        numRows = i2;
                        i2 = 0;
                    } else {
                        int i4 = i2;
                        i2 = numRows;
                        numRows = i4;
                    }
                }
                i++;
                i3 = i2;
                i2 = numRows;
            }
        }
        return layoutRow;
    }

    boolean m176d() {
        return this.f694a.hasScrollOffset();
    }

    int m172b() {
        return this.f694a.getMinScroll();
    }

    public void setOnFirstLayoutListener(OnFirstLayoutListener listener) {
        this.f697a = listener;
    }

    protected void lineFeed() {
        if (this.f707a != null) {
            this.f707a.a();
        }
    }

    public void setOnLineFeedListener(f l) {
        this.f707a = l;
    }

    public void setFocusMemorable(boolean memorable) {
        this.f694a.setFocusMemorable(memorable);
    }

    public i getVisibleViewsIterator(boolean fully) {
        return new i(this, fully);
    }
}
