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
import com.gala.video.albumlist4.widget.C0468d.C0466a;
import com.gala.video.albumlist4.widget.C0473h.C0437b;
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
    private static final Interpolator f1618a = new C04321();
    private float f1619a;
    private int f1620a;
    private Rect f1621a;
    private SparseArray<SparseArray<ViewHolder>> f1622a;
    private LayoutManager f1623a;
    private Adapter<ViewHolder> f1624a;
    ItemDecoration f1625a;
    private OnFirstLayoutListener f1626a;
    private OnFocusLostListener f1627a;
    private OnItemAnimatorFinishListener f1628a;
    private OnItemClickListener f1629a;
    private OnItemFocusChangedListener f1630a;
    private OnItemRecycledListener f1631a;
    private OnScrollListener f1632a;
    private C0443b f1633a;
    private C0439a f1634a;
    private C0445c f1635a;
    private C0448f f1636a;
    private C0449g f1637a;
    private final C0437b f1638a;
    private final C0473h f1639a;
    private Object f1640a;
    private final Runnable f1641a;
    boolean f1642a;
    private float f1643b;
    private int f1644b;
    private SparseArray<View> f1645b;
    private Runnable f1646b;
    private boolean f1647b;
    private float f1648c;
    private int f1649c;
    private boolean f1650c;
    private int f1651d;
    private boolean f1652d;
    private int f1653e;
    private boolean f1654e;
    private boolean f1655f;
    private boolean f1656g;
    private boolean f1657h;
    private boolean f1658i;
    protected C0450h mViewFlinger;

    static class C04321 implements Interpolator {
        C04321() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    }

    class C04343 implements OnClickListener {
        final /* synthetic */ RecyclerView f1695a;

        C04343(RecyclerView recyclerView) {
            this.f1695a = recyclerView;
        }

        public void onClick(View view) {
            if (this.f1695a.f1644b != null && !this.f1695a.f1642a) {
                this.f1695a.f1644b.onItemClick(this.f1695a, this.f1695a.m1272a(view));
            }
        }
    }

    class C04354 implements OnKeyListener {
        final /* synthetic */ RecyclerView f1696a;

        C04354(RecyclerView recyclerView) {
            this.f1696a = recyclerView;
        }

        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (event.getAction() != 0 || event.getRepeatCount() > 0) {
                return false;
            }
            if ((keyCode != 23 && keyCode != 66) || this.f1696a.f1644b == null || this.f1696a.f1642a) {
                return false;
            }
            this.f1696a.f1644b.onItemClick(this.f1696a, this.f1696a.m1272a(view));
            return true;
        }
    }

    class C04365 implements Runnable {
        final /* synthetic */ RecyclerView f1697a;

        C04365(RecyclerView recyclerView) {
            this.f1697a = recyclerView;
        }

        public void run() {
            this.f1697a.m1266p();
        }
    }

    class C04386 implements C0437b {
        final /* synthetic */ RecyclerView f1698a;

        C04386(RecyclerView recyclerView) {
            this.f1698a = recyclerView;
        }

        public void mo981a(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
            this.f1698a.m1225a(viewHolder);
            this.f1698a.m1247b(viewHolder, c0446d, c0446d2);
        }

        public void mo982b(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
            this.f1698a.m1231a(viewHolder, c0446d, c0446d2);
        }

        public void mo983c(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
            this.f1698a.m1254c(viewHolder, c0446d, c0446d2);
        }
    }

    class C04407 implements C0439a {
        final /* synthetic */ RecyclerView f1699a;

        C04407(RecyclerView recyclerView) {
            this.f1699a = recyclerView;
        }

        public void mo984a() {
            this.f1699a.f1657h = false;
            if (this.f1699a.f1644b != null) {
                this.f1699a.f1644b.onItemAnimatorFinished();
            }
        }

        public void mo985a(ViewHolder viewHolder) {
            viewHolder.m1326b(2);
            if (viewHolder.m1328c()) {
                viewHolder.m1326b(4);
                this.f1699a.removeView(viewHolder.itemView);
                this.f1699a.m1285b(viewHolder);
            }
        }
    }

    class C04418 implements Runnable {
        final /* synthetic */ RecyclerView f1700a;

        C04418(RecyclerView recyclerView) {
            this.f1700a = recyclerView;
        }

        public void run() {
            if (this.f1700a.f1644b != null) {
                this.f1700a.f1644b.mo989a();
            }
            this.f1700a.f1656g = false;
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private C0442a f1701a = new C0442a();

        public abstract int getCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            VH onCreateViewHolder = onCreateViewHolder(parent, viewType);
            onCreateViewHolder.f1703a = viewType;
            return onCreateViewHolder;
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.f1704a.f1803c = position;
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
            this.f1701a.m1330a();
        }

        public final void notifyDataSetUpdate() {
            this.f1701a.m1333b();
        }

        public final void notifyDataSetAdd() {
            this.f1701a.m1335c();
        }

        public final void notifyDataSetAdd(int position) {
            this.f1701a.m1332a(position, 1);
        }

        public final void notifyDataSetRemoved(int position) {
            this.f1701a.m1331a(position);
        }

        public final void notifyItemRemoved(int position) {
            this.f1701a.m1334b(position, 1);
        }

        public void registerAdapterDataObserver(C0443b observer) {
            this.f1701a.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(C0443b observer) {
            this.f1701a.unregisterObserver(observer);
        }

        public boolean isFocusable(int position) {
            return true;
        }
    }

    public static abstract class ItemDecoration {
        public abstract int getItemOffsets(int i, RecyclerView recyclerView);
    }

    public static class LayoutParams extends MarginLayoutParams {
        ViewHolder f1702a;

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
            return this.f1702a.f1704a.f1803c;
        }

        public int getViewColumn() {
            return this.f1702a.f1704a.f1802b;
        }

        public int getViewRow() {
            return this.f1702a.f1704a.f1801a;
        }

        public ViewHolder getViewHolder() {
            return this.f1702a;
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
        int f1703a = -1;
        C0466a f1704a;
        int f1705b = 0;
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        public int getLayoutPosition() {
            return this.f1704a.f1803c;
        }

        public int getLayoutColumn() {
            return this.f1704a.f1802b;
        }

        public int getLayoutRow() {
            return this.f1704a.f1801a;
        }

        public int getItemViewType() {
            return this.f1703a;
        }

        void m1321a() {
            m1322a(8);
        }

        void m1324a(int i, boolean z) {
            m1321a();
            C0466a c0466a = this.f1704a;
            c0466a.f1803c += i;
        }

        void m1323a(int i, int i2, boolean z) {
            m1322a(2);
            this.f1704a.f1803c = Integer.MAX_VALUE - this.f1704a.f1803c;
        }

        void m1322a(int i) {
            this.f1705b |= i;
        }

        void m1326b(int i) {
            this.f1705b &= i ^ -1;
        }

        boolean m1325a() {
            return (this.f1705b & 2) != 0;
        }

        boolean m1327b() {
            return (this.f1705b & 8) != 0;
        }

        boolean m1328c() {
            return (this.f1705b & 4) != 0;
        }

        boolean m1329d() {
            return (this.f1705b & 16) != 0;
        }
    }

    static class C0442a extends Observable<C0443b> {
        C0442a() {
        }

        public void m1330a() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0443b) this.mObservers.get(size)).m1336a();
            }
        }

        public void m1333b() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0443b) this.mObservers.get(size)).m1339b();
            }
        }

        public void m1335c() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0443b) this.mObservers.get(size)).m1341c();
            }
        }

        public void m1332a(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0443b) this.mObservers.get(size)).m1338a(i, i2);
            }
        }

        public void m1331a(int i) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0443b) this.mObservers.get(size)).m1337a(i);
            }
        }

        public void m1334b(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0443b) this.mObservers.get(size)).m1340b(i, i2);
            }
        }
    }

    private class C0443b {
        final /* synthetic */ RecyclerView f1706a;

        private C0443b(RecyclerView recyclerView) {
            this.f1706a = recyclerView;
        }

        public void m1336a() {
            synchronized (this.f1706a.f1644b) {
                this.f1706a.mViewFlinger.m1363b();
                this.f1706a.f1642a = true;
                this.f1706a.m1258h();
                this.f1706a.f1644b.clear();
                this.f1706a.requestLayout();
            }
        }

        public void m1339b() {
            synchronized (this.f1706a.f1644b) {
                this.f1706a.f1644b.onUpdateChildren();
            }
        }

        public void m1341c() {
            this.f1706a.f1644b.onLayoutChildren();
        }

        public void m1338a(int i, int i2) {
            if (!this.f1706a.f1644b) {
                this.f1706a.f1657h = true;
                this.f1706a.f1637a = new C0449g(1, i, i2, null);
                m1342d();
            }
        }

        public void m1337a(int i) {
            this.f1706a.f1644b.onRemoved(i);
        }

        public void m1340b(int i, int i2) {
            if (!this.f1706a.f1644b) {
                this.f1706a.f1657h = true;
                this.f1706a.f1637a = new C0449g(2, i, i2, null);
                m1342d();
            }
        }

        void m1342d() {
            this.f1706a.post(this.f1706a.f1644b);
        }
    }

    static abstract class C0445c {
        private long f1707a = 120;
        C0439a f1708a;
        private long f1709b = 120;
        private long f1710c = 250;

        interface C0439a {
            void mo984a();

            void mo985a(ViewHolder viewHolder);
        }

        static class C0444b implements AnimatorListener {
            C0444b() {
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

        public abstract void m1344a();

        public abstract void mo990a(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2);

        public abstract void mo993b(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2);

        public abstract void m1350c();

        public abstract void mo995c(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2);

        C0445c() {
        }

        public long mo994c() {
            return this.f1707a;
        }

        public long mo992b() {
            return this.f1709b;
        }

        public long mo989a() {
            return this.f1710c;
        }

        public void mo991a(C0439a c0439a) {
            this.f1708a = c0439a;
        }
    }

    static class C0446d {
        public int f1711a;
        public int f1712b;
        public int f1713c;
        public int f1714d;

        C0446d() {
        }

        public String toString() {
            return this.f1711a + " " + this.f1712b + " " + this.f1713c + " " + this.f1714d;
        }
    }

    class C0447e extends OverScroller {
        private int f1715a;
        final /* synthetic */ RecyclerView f1716a;

        public C0447e(RecyclerView recyclerView, Context context, Interpolator interpolator) {
            this.f1716a = recyclerView;
            super(context, interpolator);
        }

        public boolean equals(Object object) {
            if (!(object instanceof C0447e)) {
                return false;
            }
            C0447e c0447e = (C0447e) object;
            if (getFinalX() == c0447e.getFinalX() && getFinalY() == c0447e.getFinalY() && this.f1715a == c0447e.f1715a) {
                return true;
            }
            return false;
        }

        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, duration);
            this.f1715a = duration;
        }
    }

    public interface C0448f {
        void m1352a();
    }

    static class C0449g {
        int f1717a;
        Object f1718a;
        int f1719b;
        int f1720c;

        C0449g(int i, int i2, int i3, Object obj) {
            this.f1717a = i;
            this.f1720c = i2;
            this.f1719b = i3;
            this.f1718a = obj;
        }
    }

    class C0450h implements Runnable {
        private int f1721a = 0;
        private Interpolator f1722a = RecyclerView.f1618a;
        private C0447e f1723a;
        final /* synthetic */ RecyclerView f1724a;
        private boolean f1725a = false;
        private int f1726b = 0;
        private boolean f1727b = false;
        private int f1728c = 0;
        private int f1729d = 0;
        private int f1730e = 0;

        public C0450h(RecyclerView recyclerView) {
            this.f1724a = recyclerView;
            this.f1723a = new C0447e(recyclerView, recyclerView.getContext(), RecyclerView.f1618a);
        }

        void m1360a() {
            if (this.f1725a) {
                this.f1727b = true;
                return;
            }
            this.f1724a.removeCallbacks(this);
            this.f1724a.post(this);
        }

        private int m1353a(int i, int i2) {
            int abs = Math.abs(i);
            int abs2 = Math.abs(i2);
            Object obj = abs > abs2 ? 1 : null;
            int width = obj != null ? this.f1724a.getWidth() : this.f1724a.getHeight();
            if (obj == null) {
                abs = abs2;
            }
            return Math.min((int) (((((float) abs) / ((float) width)) + 1.0f) * 300.0f), 2000);
        }

        public void run() {
            int i = 0;
            m1357c();
            OverScroller overScroller = this.f1723a;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i2 = currX - this.f1729d;
                int i3 = currY - this.f1730e;
                this.f1729d = currX;
                this.f1730e = currY;
                RecyclerView.f1618a;
                if (i2 != 0) {
                    i3 = i2;
                }
                if (i3 != 0) {
                    i2 = this.f1724a.f1644b.scrollBy(i3, this.f1724a.f1644b);
                } else {
                    i2 = 0;
                }
                this.f1724a.m1241b();
                this.f1724a.invalidate();
                if (!(i3 == 0 || i2 == 0)) {
                    this.f1724a.m1240b(2);
                    this.f1724a.f1644b;
                }
                i2 = (i3 == 0 || i3 == i2) ? 0 : 1;
                if (!this.f1723a.equals(overScroller)) {
                    i = 1;
                }
                if (i != 0 || (!overScroller.isFinished() && i2 == 0)) {
                    m1360a();
                } else {
                    this.f1724a.m1240b(1);
                }
            } else {
                this.f1724a.m1240b(1);
            }
            m1358d();
        }

        private void m1357c() {
            this.f1727b = false;
            this.f1725a = true;
        }

        private void m1358d() {
            this.f1725a = false;
            if (this.f1727b) {
                m1360a();
            }
        }

        public void m1361a(int i, int i2) {
            m1362a(i, i2, m1353a(i, i2), this.f1722a);
        }

        public void m1362a(int i, int i2, int i3, Interpolator interpolator) {
            this.f1721a = i;
            this.f1726b = i2;
            this.f1728c = this.f1724a.m1240b(i3);
            if (this.f1722a != interpolator) {
                this.f1722a = interpolator;
                this.f1723a = new C0447e(this.f1724a, this.f1724a.getContext(), interpolator);
            }
            m1359e();
        }

        private void m1359e() {
            this.f1730e = 0;
            this.f1729d = 0;
            this.f1723a.startScroll(0, 0, this.f1721a, this.f1726b, this.f1728c);
            m1360a();
        }

        public void m1363b() {
            this.f1724a.removeCallbacks(this);
            this.f1723a.abortAnimation();
        }
    }

    public static class C0451i {
        private int f1731a = -1;
        private RecyclerView f1732a;
        private boolean f1733a = false;
        private int f1734b = -1;

        C0451i(RecyclerView recyclerView, boolean z) {
            this.f1733a = z;
            this.f1732a = recyclerView;
            this.f1731a = recyclerView.getFirstAttachedPosition();
            this.f1734b = recyclerView.getLastAttachedPosition();
        }
    }

    public RecyclerView(Context context) {
        super(context);
        this.f1622a = new SparseArray();
        this.f1645b = new SparseArray();
        this.mViewFlinger = new C0450h(this);
        this.f1633a = new C0443b();
        this.f1652d = true;
        this.f1640a = new Object();
        this.f1620a = 1;
        this.f1644b = 1;
        this.f1649c = 1;
        this.f1642a = true;
        this.f1619a = 1.0f;
        this.f1643b = 1.0f;
        this.f1648c = 1.0f;
        this.f1654e = true;
        this.f1653e = Service.CISCO_FNA;
        this.f1655f = false;
        this.f1639a = new C0473h();
        this.f1635a = new C0460b();
        this.f1658i = true;
        this.f1641a = new C04365(this);
        this.f1638a = new C04386(this);
        this.f1634a = new C04407(this);
        this.f1646b = new C04418(this);
        m1259i();
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f1622a = new SparseArray();
        this.f1645b = new SparseArray();
        this.mViewFlinger = new C0450h(this);
        this.f1633a = new C0443b();
        this.f1652d = true;
        this.f1640a = new Object();
        this.f1620a = 1;
        this.f1644b = 1;
        this.f1649c = 1;
        this.f1642a = true;
        this.f1619a = 1.0f;
        this.f1643b = 1.0f;
        this.f1648c = 1.0f;
        this.f1654e = true;
        this.f1653e = Service.CISCO_FNA;
        this.f1655f = false;
        this.f1639a = new C0473h();
        this.f1635a = new C0460b();
        this.f1658i = true;
        this.f1641a = new C04365(this);
        this.f1638a = new C04386(this);
        this.f1634a = new C04407(this);
        this.f1646b = new C04418(this);
        m1259i();
    }

    public synchronized void setAdapter(Adapter adapter) {
        if (this.f1624a != null) {
            this.f1624a.unregisterAdapterDataObserver(this.f1633a);
            m1257g();
        }
        this.f1623a.onAdapterChanged(this.f1624a);
        this.f1624a = adapter;
        this.f1624a.registerAdapterDataObserver(this.f1633a);
        this.f1642a = true;
        requestLayout();
    }

    private void m1257g() {
        removeAllViews();
        m1258h();
    }

    protected void removeUnattachedViews() {
        for (int i = 0; i < this.f1622a.size(); i++) {
            SparseArray sparseArray = (SparseArray) this.f1622a.valueAt(i);
            if (sparseArray != null && sparseArray.size() > 0) {
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    ViewHolder viewHolder = (ViewHolder) sparseArray.valueAt(size);
                    if (!(viewHolder == null || viewHolder.m1329d())) {
                        removeView(viewHolder.itemView);
                    }
                }
            }
        }
    }

    private void m1258h() {
        for (int i = 0; i < this.f1622a.size(); i++) {
            ((SparseArray) this.f1622a.valueAt(i)).clear();
        }
        this.f1622a.clear();
    }

    public void setOrientation(Orientation orientation) {
        this.f1623a.setOrientation(orientation);
    }

    public boolean isChildVisible(View child, boolean fully) {
        if (!m1236a((ViewGroup) this, child)) {
            return false;
        }
        if (this.f1621a == null) {
            this.f1621a = new Rect();
        }
        this.f1621a.set(getPaddingLeft() + getScrollX(), getPaddingTop() + getScrollY(), (getWidth() + getScrollX()) - getPaddingRight(), (getHeight() + getScrollY()) - getPaddingBottom());
        if (fully) {
            return this.f1621a.contains(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
        return this.f1621a.intersect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }

    private void m1259i() {
        setFocusableInTouchMode(true);
        this.f1623a = new C0469e(this);
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(true);
        this.f1635a.mo991a(this.f1634a);
    }

    public void setVerticalMargin(int margin) {
        this.f1623a.setVerticalMargin(margin);
        requestLayout();
    }

    public int getVerticalMargin() {
        return this.f1623a.getVerticalMargin();
    }

    public void setHorizontalMargin(int margin) {
        this.f1623a.setHorizontalMargin(margin);
        requestLayout();
    }

    public int getHorizontalMargin() {
        return this.f1623a.getHorizontalMargin();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.f1653e = m1213a(event.getKeyCode());
        if (this.f1657h) {
            LOG.m1208d("drop the key event when animator start.");
            return true;
        } else if (getChildCount() <= 0) {
            LOG.m1208d("child count is 0.");
            return false;
        } else {
            switch (event.getKeyCode()) {
                case 19:
                case 20:
                    if ((this.f1623a.mFocusLoop || this.f1623a.getOrientation() == Orientation.VERTICAL) && this.f1623a.dispatchKeyEvent(event, this.f1653e)) {
                        return true;
                    }
                case 21:
                case 22:
                    if ((this.f1623a.mFocusLoop || this.f1623a.getOrientation() == Orientation.HORIZONTAL) && this.f1623a.dispatchKeyEvent(event, this.f1653e)) {
                        return true;
                    }
            }
            return super.dispatchKeyEvent(event);
        }
    }

    private int m1213a(int i) {
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
        return this.f1651d == 19;
    }

    View m1273a(View view, int i) {
        return super.focusSearch(view, i);
    }

    public View focusSearch(View focused, int direction) {
        View focusSearch = this.f1623a.focusSearch(focused, direction);
        if (indexOfChild(focusSearch) >= 0 && !isFocusable(getViewPosition(focusSearch))) {
            if (this.f1623a.getScrollingView() != focusSearch) {
                this.f1623a.onRequestChildFocus(focusSearch, focusSearch);
            }
            focusSearch = focused;
        } else if (!m1236a((ViewGroup) this, focusSearch)) {
            m1265o();
        }
        LOG.m1208d("direction = " + direction + " focused = " + focused + " view = " + focusSearch);
        return focusSearch;
    }

    private static boolean m1236a(ViewGroup viewGroup, View view) {
        if (viewGroup == null || view == null) {
            return false;
        }
        if (viewGroup.indexOfChild(view) >= 0) {
            return true;
        }
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if ((childAt instanceof ViewGroup) && m1236a((ViewGroup) childAt, view)) {
                return true;
            }
        }
        return false;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        this.f1623a.onRequestChildFocus(child, focused);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.f1623a.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.f1623a == null || !this.f1623a.onAddFocusables(views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        this.f1655f = false;
        if (this.f1623a.gridOnRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return true;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    public boolean hasFocus() {
        return this.f1655f ? false : super.hasFocus();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        m1260j();
    }

    private void m1260j() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() != 8) {
                this.f1623a.measureChild(childAt);
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.f1624a == null) {
            LOG.m1209d("RecyclerView", "Adapter should not be null!!!");
            return;
        }
        this.f1623a.onLayoutChildren();
        this.f1642a = false;
        m1261k();
        if (this.f1652d && getChildCount() > 0) {
            this.f1652d = false;
            if (this.f1626a != null) {
                this.f1626a.onFirstLayout();
            }
        }
    }

    private void m1261k() {
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
        this.f1652d = true;
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mViewFlinger.m1363b();
        removeCallbacks(this.f1646b);
        this.f1656g = false;
        if (this.f1635a != null) {
            this.f1635a.mo994c();
        }
        this.f1623a.doOnDetachedFromWindow();
        m1262l();
    }

    private void m1262l() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            m1285b(m1272a(getChildAt(childCount)));
        }
    }

    ViewHolder m1275a(int i) {
        return m1243b(getViewByPosition(i));
    }

    private ViewHolder m1243b(View view) {
        if (!m1236a((ViewGroup) this, view)) {
            return null;
        }
        while (view != null && view.getParent() != this) {
            view = (View) view.getParent();
        }
        return ((LayoutParams) view.getLayoutParams()).f1702a;
    }

    public int getViewPosition(View view) {
        ViewHolder b = m1243b(view);
        if (b == null) {
            return -1;
        }
        return b.getLayoutPosition();
    }

    public int getFocusPosition() {
        return this.f1623a.getFocusPosition();
    }

    public void setContentHeight(int contentHeight) {
        this.f1623a.setContentHeight(contentHeight);
    }

    public int getContentHeight() {
        return this.f1623a.getContentHeight();
    }

    public void setContentWidth(int contentWidth) {
        this.f1623a.setContentWidth(contentWidth);
    }

    public int getContentWidth() {
        return this.f1623a.getContentWidth();
    }

    void m1277a() {
        if (!this.f1647b) {
            this.f1647b = true;
        }
    }

    void m1284b() {
        if (this.f1647b) {
            this.f1647b = false;
            if (this.f1650c) {
                requestLayout();
                this.f1650c = false;
            }
        }
    }

    public void requestLayout() {
        if (this.f1647b) {
            this.f1650c = true;
        } else {
            super.requestLayout();
        }
    }

    private Interpolator m1241b() {
        switch (this.f1651d) {
            case 17:
                return new DecelerateInterpolator(1.2f);
            case 19:
                return new LinearInterpolator();
            default:
                return f1618a;
        }
    }

    private int m1240b(int i) {
        switch (this.f1651d) {
            case 17:
                return (int) (((float) i) / this.f1619a);
            case 18:
                return (int) (((float) i) / this.f1643b);
            case 19:
                return (int) (((float) i) / this.f1648c);
            default:
                return i;
        }
    }

    public void setScrollRoteScale(float smoothScrollFriction, float contScrollFriction, float quickScrollFriction) {
        if (smoothScrollFriction > 0.0f) {
            this.f1619a = smoothScrollFriction;
        }
        if (contScrollFriction > 0.0f) {
            this.f1643b = contScrollFriction;
        }
        if (quickScrollFriction > 0.0f) {
            this.f1648c = quickScrollFriction;
        }
    }

    void m1287c() {
        if (this.f1651d != 19) {
            focusSearch(findFocus(), this.f1653e);
        } else if (!f1618a) {
            m1241b();
        }
    }

    void m1289d() {
        m1240b(17);
    }

    void m1291e() {
        m1240b(18);
    }

    public void setQuickFocusLeaveForbidden(boolean isForbidden) {
        this.f1654e = isForbidden;
    }

    boolean m1282a() {
        View findFocus = findFocus();
        if (findFocus == null) {
            findFocus = this.f1623a.getFocusView();
        }
        View a = m1272a(findFocus);
        if ((findFocus == a && this.f1623a.isAtEdge(a)) || !m1236a((ViewGroup) this, a)) {
            return false;
        }
        if (!isFocusable(getViewPosition(a))) {
            if (hasFocus() && this.f1623a.getScrollingView() != a) {
                this.f1623a.onRequestChildFocus(a, a);
            }
            return false;
        } else if (findFocus == a) {
            return this.f1623a.isNeedRequestFocus();
        } else {
            if (this.f1651d != 19) {
                m1240b(19);
                requestChildFocus(null, null);
                setDescendantFocusability(393216);
            }
            this.f1623a.onRequestChildFocus(a, a);
            return true;
        }
    }

    boolean m1286b() {
        if (this.f1651d == 19) {
            setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
            View focusView = this.f1623a.getFocusView();
            View a = m1272a(focusView);
            this.mViewFlinger.m1363b();
            m1240b(17);
            if (indexOfChild(a) >= 0) {
                if (isFocusable(getViewPosition(a))) {
                    this.f1623a.resumeChildFocus(a);
                } else {
                    focusView.requestFocus();
                    this.f1623a.onRequestChildFocus(a, a);
                }
            } else if (!focusView.isFocused()) {
                focusView.requestFocus();
            }
        }
        return this.f1654e;
    }

    private void m1244b(int i) {
        if (i != this.f1651d) {
            this.f1651d = i;
            this.mViewFlinger.f1722a = m1241b();
            this.mViewFlinger.f1723a = new C0447e(this, getContext(), this.mViewFlinger.f1722a);
        }
    }

    private void m1252c(int i) {
        this.f1649c = this.f1644b;
        this.f1644b = i;
        if (this.f1644b == 1) {
            this.mViewFlinger.m1363b();
        }
        m1264n();
    }

    private void m1263m() {
        if (this.f1632a != null && !this.f1657h) {
            this.f1632a.onScroll(this, getFirstAttachedPosition(), getLastAttachedPosition(), this.f1624a.getCount());
        }
    }

    private void m1264n() {
        if (this.f1632a == null) {
            return;
        }
        if (this.f1649c != 1 && this.f1644b == 1) {
            this.f1632a.onScrollStop();
        } else if (this.f1649c == 1 && this.f1644b == 2) {
            this.f1632a.onScrollStart();
        }
    }

    private void m1265o() {
        this.f1655f = true;
        ViewHolder a = m1272a(getFocusView());
        if (a != null) {
            this.f1623a.onFocusLost(a);
            if (this.f1627a != null) {
                this.f1627a.onFocusLost(this, a);
            }
        }
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.f1632a = l;
    }

    void m1278a(int i) {
        if (this.f1632a != null) {
            this.f1632a.onScrollBefore(i);
        }
    }

    void m1292f() {
        m1252c(1);
    }

    View m1272a(View view) {
        return this.f1623a.focusSearch(view, this.f1653e);
    }

    private SparseArray<ViewHolder> m1216a(int i) {
        SparseArray<ViewHolder> sparseArray = (SparseArray) this.f1622a.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        sparseArray = new SparseArray();
        this.f1622a.put(i, sparseArray);
        return sparseArray;
    }

    private boolean m1237a(ViewHolder viewHolder, int i) {
        boolean z = !viewHolder.m1327b() || (viewHolder.m1327b() && viewHolder.getLayoutPosition() == i);
        if (z) {
            viewHolder.m1326b(8);
        }
        return z;
    }

    private ViewHolder m1223a(int i, int i2) {
        View focusedChild = getFocusedChild();
        if (focusedChild != null) {
            ViewHolder a = m1272a(focusedChild);
            if (a.getItemViewType() == i && m1237a(a, i2) && !a.m1321a() && !a.m1329d()) {
                ((SparseArray) this.f1622a.get(i)).remove(a.getLayoutPosition());
                return a;
            }
        }
        return null;
    }

    private ViewHolder m1242b(int i, int i2) {
        SparseArray sparseArray = (SparseArray) this.f1622a.get(i2);
        if (sparseArray != null && sparseArray.size() > 0) {
            int indexOfKey = sparseArray.indexOfKey(i);
            int i3 = indexOfKey < 0 ? 0 : indexOfKey;
            ViewHolder viewHolder = (ViewHolder) sparseArray.valueAt(i3);
            if (!viewHolder.m1321a() && m1237a(viewHolder, i)) {
                sparseArray.removeAt(i3);
                return viewHolder;
            }
        }
        return null;
    }

    View m1274a(C0466a c0466a) {
        int itemViewType = this.f1624a.getItemViewType(c0466a.f1803c);
        ViewHolder a = m1223a(itemViewType, c0466a.f1803c);
        if (a == null) {
            a = m1242b(c0466a.f1803c, itemViewType);
        }
        if (a == null) {
            LayoutParams generateDefaultLayoutParams;
            ViewHolder createViewHolder = this.f1624a.createViewHolder(this, itemViewType);
            android.view.ViewGroup.LayoutParams layoutParams = createViewHolder.itemView.getLayoutParams();
            if (layoutParams == null) {
                generateDefaultLayoutParams = generateDefaultLayoutParams();
                createViewHolder.itemView.setLayoutParams(generateDefaultLayoutParams);
            } else {
                generateDefaultLayoutParams = (LayoutParams) layoutParams;
            }
            generateDefaultLayoutParams.f1702a = createViewHolder;
            m1253c(createViewHolder.itemView);
            a = createViewHolder;
        }
        a.f1704a = c0466a.m1420a();
        m1243b(a.itemView);
        this.f1624a.bindViewHolder(a, c0466a.f1803c);
        this.f1645b.append(c0466a.f1803c, a.itemView);
        a.m1322a(16);
        if (indexOfChild(a.itemView) < 0) {
            addView(a.itemView);
        }
        return a.itemView;
    }

    private void m1246b(View view) {
        if (view != null) {
            view.setNextFocusUpId(getNextFocusUpId());
            view.setNextFocusDownId(getNextFocusDownId());
            view.setNextFocusRightId(getNextFocusRightId());
            view.setNextFocusLeftId(getNextFocusLeftId());
        }
    }

    private void m1253c(View view) {
        final OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
        view.setOnFocusChangeListener(new OnFocusChangeListener(this) {
            final /* synthetic */ RecyclerView f1694a;

            public void onFocusChange(View view, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(view, hasFocus);
                }
                if (this.f1694a.f1644b != null) {
                    this.f1694a.f1644b.onItemFocusChanged(this.f1694a, this.f1694a.m1272a(view), hasFocus);
                }
            }
        });
        view.setOnClickListener(new C04343(this));
        view.setOnKeyListener(new C04354(this));
    }

    public void updateItem(ViewHolder vh, int position) {
        this.f1624a.bindViewHolder(vh, position);
    }

    public int getCount() {
        return this.f1624a.getCount();
    }

    public int getLastPosition() {
        return this.f1624a.getLastPosition();
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mViewFlinger.m1353a(dx, dy);
    }

    void m1281a(ViewHolder viewHolder) {
        m1213a(viewHolder.f1703a).delete(viewHolder.f1704a.f1803c);
    }

    public void setViewRecycled(boolean isViewRecycled) {
        this.f1658i = isViewRecycled;
    }

    boolean m1288c() {
        return this.f1658i;
    }

    void m1279a(View view) {
        if (view != null) {
            m1285b(m1272a(view));
        }
    }

    void m1280a(View view, boolean z) {
        if (z) {
            removeView(view);
        }
        ViewHolder a = m1272a(view);
        int i = a.f1704a.f1803c;
        this.f1645b.remove(i);
        a.m1326b(16);
        SparseArray a2 = m1213a(a.f1703a);
        if (a2.indexOfValue(a) < 0) {
            a2.put(i, a);
        }
        m1285b(a);
    }

    ViewHolder m1276a(View view) {
        if (view != null) {
            return ((LayoutParams) view.getLayoutParams()).f1702a;
        }
        return null;
    }

    void m1285b(ViewHolder viewHolder) {
        if (this.f1631a != null && !this.f1657h) {
            this.f1631a.onItemRecycled(this, viewHolder);
        }
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    private C0446d m1225a(ViewHolder viewHolder) {
        C0446d c0446d = new C0446d();
        if (this.f1623a.getOrientation() == Orientation.VERTICAL) {
            c0446d.f1711a = viewHolder.itemView.getLeft();
            c0446d.f1712b = viewHolder.itemView.getTop() - getScrollY();
            c0446d.f1713c = viewHolder.itemView.getRight();
            c0446d.f1714d = viewHolder.itemView.getBottom() - getScrollY();
        } else {
            c0446d.f1711a = viewHolder.itemView.getLeft() - getScrollX();
            c0446d.f1712b = viewHolder.itemView.getTop();
            c0446d.f1713c = viewHolder.itemView.getRight() - getScrollX();
            c0446d.f1714d = viewHolder.itemView.getBottom();
        }
        return c0446d;
    }

    private void m1266p() {
        if (this.f1637a != null) {
            m1270t();
            m1267q();
            this.f1623a.onLayoutChildren();
            this.f1642a = false;
            m1268r();
            if (VERSION.SDK_INT >= 14) {
                this.f1639a.m1513a(this.f1638a);
            }
        }
    }

    private void m1267q() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = m1272a(getChildAt(i));
            this.f1639a.m1512a(a, m1225a(a));
        }
    }

    private void m1268r() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = m1272a(getChildAt(i));
            this.f1639a.m1514b(a, m1225a(a));
        }
    }

    private void m1231a(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
        this.f1635a.mo990a(viewHolder, c0446d, c0446d2);
        m1269s();
    }

    private void m1247b(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
        m1230a(viewHolder, c0446d);
        this.f1635a.mo993b(viewHolder, c0446d, c0446d2);
        m1269s();
    }

    private void m1254c(ViewHolder viewHolder, C0446d c0446d, C0446d c0446d2) {
        this.f1635a.mo995c(viewHolder, c0446d, c0446d2);
        m1269s();
    }

    private void m1230a(ViewHolder viewHolder, C0446d c0446d) {
        View view = viewHolder.itemView;
        if ((view.getParent() == this ? 1 : null) == null) {
            addView(view);
        }
        if (this.f1623a.getOrientation() == Orientation.VERTICAL) {
            view.layout(c0446d.f1711a, c0446d.f1712b + getScrollY(), c0446d.f1713c, c0446d.f1714d + getScrollY());
        } else {
            view.layout(c0446d.f1711a + getScrollX(), c0446d.f1712b, c0446d.f1713c + getScrollX(), c0446d.f1714d);
        }
        viewHolder.m1322a(4);
    }

    private void m1269s() {
        if (!this.f1656g) {
            post(this.f1646b);
            this.f1656g = true;
        }
    }

    private void m1270t() {
        C0449g c0449g = this.f1637a;
        switch (c0449g.f1717a) {
            case 1:
                m1232a(c0449g);
                return;
            case 2:
                m1248b(c0449g);
                return;
            default:
                return;
        }
    }

    private void m1232a(C0449g c0449g) {
        m1229a(c0449g.f1720c, c0449g.f1719b, true);
        this.f1623a.onItemsAdded(c0449g.f1720c, c0449g.f1719b);
    }

    private void m1229a(int i, int i2, boolean z) {
        int childCount = getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            ViewHolder a = m1272a(getChildAt(i3));
            if (a != null) {
                if (a.f1704a.f1803c >= i) {
                    a.m1324a(i2, z);
                    this.f1642a = true;
                } else {
                    a.m1321a();
                }
            }
        }
        requestLayout();
    }

    private void m1248b(C0449g c0449g) {
        m1245b(c0449g.f1720c, c0449g.f1719b, true);
        this.f1623a.onItemsRemoved(c0449g.f1720c, c0449g.f1719b);
    }

    private void m1245b(int i, int i2, boolean z) {
        int i3 = i + i2;
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            ViewHolder a = m1272a(getChildAt(i4));
            if (a != null) {
                if (a.f1704a.f1803c >= i3) {
                    a.m1324a(-i2, z);
                    this.f1642a = true;
                } else if (a.f1704a.f1803c >= i) {
                    a.m1323a(i - 1, -i2, z);
                    this.f1642a = true;
                } else {
                    a.m1321a();
                }
            }
        }
        requestLayout();
    }

    public int getNumRows(int position) {
        int numRows = this.f1624a.getNumRows(position);
        if (numRows == 0) {
            numRows = this.f1620a;
            if (numRows == 0) {
                throw new AndroidRuntimeException("Row number can't be zero!!!");
            }
        }
        return numRows;
    }

    public boolean isFocusable(int position) {
        return this.f1624a.isFocusable(position);
    }

    public void setItemDecoration(ItemDecoration decor) {
        this.f1625a = decor;
    }

    public Adapter getAdapter() {
        return this.f1624a;
    }

    public void setNumRows(int numRows) {
        if (numRows == 0) {
            throw new AndroidRuntimeException("Row number can't be zero!!!");
        }
        this.f1620a = numRows;
        this.f1623a.setNumRows(numRows);
    }

    public int getNumRows() {
        return this.f1623a.getNumRows();
    }

    public int getFirstAttachedPosition() {
        return this.f1623a.getFirstAttachedPosition();
    }

    public int getLastAttachedPosition() {
        return this.f1623a.getLastAttachedPosition();
    }

    public void setFocusPlace(FocusPlace focusPlace) {
        this.f1623a.setFocusPlace(focusPlace);
    }

    public void setFocusPlace(int low, int high) {
        this.f1623a.setFocusPlace(low, high);
    }

    public void setFocusPosition(int focusPosition) {
        setFocusPosition(focusPosition, false);
    }

    public void setFocusPosition(int focusPosition, boolean scroll) {
        this.f1623a.setFocusPosition(focusPosition);
        if (scroll) {
            this.mViewFlinger.m1363b();
            View focusView = this.f1623a.getFocusView();
            if (focusView != null) {
                this.f1623a.onRequestChildFocus(focusView, focusView);
            }
        }
    }

    public void setFocusMode(int mode) {
        this.f1623a.setScrollMode(mode);
    }

    public void setOnFocusLostListener(OnFocusLostListener l) {
        this.f1627a = l;
    }

    public void setOnItemFocusChangedListener(OnItemFocusChangedListener l) {
        this.f1630a = l;
    }

    public void setOnItemRecycledListener(OnItemRecycledListener l) {
        this.f1631a = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.f1629a = l;
    }

    public void setOnItemAnimatorFinishListener(OnItemAnimatorFinishListener l) {
        this.f1628a = l;
    }

    public void setGravity(int gravity) {
        this.f1623a.setGravity(gravity);
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
        View view = (View) this.f1645b.get(position);
        if (view != null) {
            return view;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            ViewHolder a = m1272a(childAt);
            if (a.getLayoutPosition() == position && a.m1329d()) {
                return childAt;
            }
        }
        return view;
    }

    public void setFocusLeaveForbidden(int direction) {
        this.f1623a.setFocusLeaveForbidden(direction);
    }

    public void setSpringbackForbidden(int direction) {
        this.f1623a.setSpringbackForbidden(direction);
    }

    public void setShakeForbidden(int direction) {
        this.f1623a.setShakeForbidden(direction);
    }

    public void setSpringbackDelta(int delta) {
        this.f1623a.setSpringbackDelta(delta);
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
        return this.f1651d;
    }

    public void setExtraPadding(int extraPadding) {
        this.f1623a.setExtraPadding(extraPadding);
    }

    public void setFocusLoop(boolean focusLoop) {
        this.f1623a.setFocusLoop(focusLoop);
    }

    public View getFocusView() {
        return this.f1623a.getFocusView();
    }

    int m1271a() {
        return this.f1623a.getMovement();
    }

    public int getColumn(int position) {
        int layoutColumn;
        ViewHolder a = m1213a(position);
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
        ViewHolder a = m1213a(position);
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

    boolean m1290d() {
        return this.f1623a.hasScrollOffset();
    }

    int m1283b() {
        return this.f1623a.getMinScroll();
    }

    public void setOnFirstLayoutListener(OnFirstLayoutListener listener) {
        this.f1626a = listener;
    }

    protected void lineFeed() {
        if (this.f1636a != null) {
            this.f1636a.m1352a();
        }
    }

    public void setOnLineFeedListener(C0448f l) {
        this.f1636a = l;
    }

    public void setFocusMemorable(boolean memorable) {
        this.f1623a.setFocusMemorable(memorable);
    }

    public C0451i getVisibleViewsIterator(boolean fully) {
        return new C0451i(this, fully);
    }
}
