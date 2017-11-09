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
import com.gala.video.albumlist.widget.C0423d.C0421a;
import com.gala.video.albumlist.widget.C0427g.C0391b;
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
    private static final Interpolator f1429a = new C03861();
    private static final Interpolator f1430b = new DecelerateInterpolator(1.2f);
    private static final Interpolator f1431c = new LinearInterpolator();
    private float f1432a;
    private int f1433a;
    private Rect f1434a;
    private SparseArray<SparseArray<ViewHolder>> f1435a;
    private OnAttachStateChangeListener f1436a;
    private Adapter<ViewHolder> f1437a;
    ItemDecoration f1438a;
    private OnFirstLayoutListener f1439a;
    private OnFocusLostListener f1440a;
    private OnFocusPositionChangedListener f1441a;
    private OnItemAnimatorFinishListener f1442a;
    private OnItemClickListener f1443a;
    private OnItemFocusChangedListener f1444a;
    private OnItemStateChangeListener f1445a;
    private OnMoveToTheBorderListener f1446a;
    private OnScrollListener f1447a;
    private C0397b f1448a;
    private C0393a f1449a;
    private C0399c f1450a;
    private C0402f f1451a;
    private C0403g f1452a;
    private LayoutManager f1453a;
    private final C0391b f1454a;
    private final C0427g f1455a;
    private Object f1456a;
    private Runnable f1457a;
    boolean f1458a;
    private float f1459b;
    private int f1460b;
    private Rect f1461b;
    private SparseArray<View> f1462b;
    private Runnable f1463b;
    private boolean f1464b;
    private float f1465c;
    private int f1466c;
    private boolean f1467c;
    private int f1468d;
    private boolean f1469d;
    private boolean f1470e;
    private boolean f1471f;
    private boolean f1472g;
    private boolean f1473h;
    private boolean f1474i;
    protected C0404h mViewFlinger;

    static class C03861 implements Interpolator {
        C03861() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return ((((t * t) * t) * t) * t) + 1.0f;
        }
    }

    class C03872 implements Runnable {
        final /* synthetic */ BlocksView f1387a;

        C03872(BlocksView blocksView) {
            this.f1387a = blocksView;
        }

        public void run() {
            if (this.f1387a.f1466c == 19) {
                this.f1387a.m935a();
            }
        }
    }

    class C03894 implements OnClickListener {
        final /* synthetic */ BlocksView f1390a;

        C03894(BlocksView blocksView) {
            this.f1390a = blocksView;
        }

        public void onClick(View view) {
            if (this.f1390a.f1466c != null && !this.f1390a.f1458a) {
                this.f1390a.f1466c.onItemClick(this.f1390a, this.f1390a.m994a(view));
            }
        }
    }

    class C03905 implements OnKeyListener {
        final /* synthetic */ BlocksView f1391a;

        C03905(BlocksView blocksView) {
            this.f1391a = blocksView;
        }

        public boolean onKey(View view, int keyCode, KeyEvent event) {
            if (event.getAction() != 0 || event.getRepeatCount() > 0) {
                return false;
            }
            if ((keyCode != 23 && keyCode != 66) || this.f1391a.f1466c == null || this.f1391a.f1458a) {
                return false;
            }
            this.f1391a.f1466c.onItemClick(this.f1391a, this.f1391a.m994a(view));
            return true;
        }
    }

    class C03926 implements C0391b {
        final /* synthetic */ BlocksView f1392a;

        C03926(BlocksView blocksView) {
            this.f1392a = blocksView;
        }

        public void mo882a(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
            this.f1392a.m964b(viewHolder, c0400d, c0400d2);
        }

        public void mo883b(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
            this.f1392a.m950a(viewHolder, c0400d, c0400d2);
        }

        public void mo884c(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
            this.f1392a.m973c(viewHolder, c0400d, c0400d2);
        }
    }

    class C03947 implements C0393a {
        final /* synthetic */ BlocksView f1393a;

        C03947(BlocksView blocksView) {
            this.f1393a = blocksView;
        }

        public void mo885a() {
            this.f1393a.m988q();
            this.f1393a.f1472g = false;
            if (this.f1393a.f1466c == 19) {
                if (this.f1393a.f1433a) {
                    this.f1393a.m960b();
                } else {
                    this.f1393a.m935a();
                }
            }
            if (this.f1393a.f1466c != null) {
                this.f1393a.f1466c.onItemAnimatorFinished(this.f1393a);
            }
        }

        public void mo886a(ViewHolder viewHolder) {
            if (viewHolder.m893c()) {
                viewHolder.m894c(4);
                this.f1393a.removeView(viewHolder.itemView);
                this.f1393a.m943a(viewHolder);
            }
        }
    }

    class C03958 implements Runnable {
        final /* synthetic */ BlocksView f1394a;

        C03958(BlocksView blocksView) {
            this.f1394a = blocksView;
        }

        public void run() {
            if (this.f1394a.f1466c != null) {
                this.f1394a.f1466c.mo904b();
            }
            this.f1394a.f1471f = false;
        }
    }

    public static abstract class Adapter<VH extends ViewHolder> {
        private C0396a f1395a = new C0396a();

        public abstract int getCount();

        public abstract void onBindViewHolder(VH vh, int i);

        public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

        public final VH createViewHolder(ViewGroup parent, int viewType) {
            VH onCreateViewHolder = onCreateViewHolder(parent, viewType);
            onCreateViewHolder.f1397a = viewType;
            return onCreateViewHolder;
        }

        public final void bindViewHolder(VH holder, int position) {
            holder.f1398a.f1577b = position;
            onBindViewHolder(holder, position);
        }

        public int getItemViewType(int position) {
            return 0;
        }

        public int getLastPosition() {
            return getCount() - 1;
        }

        public final void notifyDataSetChanged() {
            this.f1395a.m898a();
        }

        public final void notifyDataSetChanged(int positionStart, int positionEnd, int count) {
            this.f1395a.m901a(positionStart, positionEnd, count);
        }

        public final void notifyDataSetUpdate() {
            this.f1395a.m903b();
        }

        public final void notifyDataSetAdd() {
            this.f1395a.m904c();
        }

        public final void notifyDataSetAdd(int position) {
            this.f1395a.m900a(position, 1);
        }

        public final void notifyDataSetAdd(int position, int count) {
            this.f1395a.m900a(position, count);
        }

        public final void notifyDataSetRemoved(int position) {
            this.f1395a.m899a(position);
        }

        public final void notifyItemRemoved(int position) {
            this.f1395a.m902a(position, 1, true);
        }

        public final void notifyItemRemoved(int position, int count) {
            this.f1395a.m902a(position, count, true);
        }

        public final void notifyItemRemoved(int position, int count, boolean isAnimationRequired) {
            this.f1395a.m902a(position, count, isAnimationRequired);
        }

        public void registerAdapterDataObserver(C0397b observer) {
            this.f1395a.registerObserver(observer);
        }

        public void unregisterAdapterDataObserver(C0397b observer) {
            this.f1395a.unregisterObserver(observer);
        }

        public boolean isFocusable(int position) {
            return true;
        }
    }

    public static abstract class ItemDecoration {
        public abstract int getItemOffsets(int i, BlocksView blocksView);
    }

    public static class LayoutParams extends MarginLayoutParams {
        ViewHolder f1396a;

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
            return this.f1396a.f1398a.f1577b;
        }

        public int getViewColumn() {
            return this.f1396a.f1398a.f1576a;
        }

        public ViewHolder getViewHolder() {
            return this.f1396a;
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
        int f1397a = -1;
        C0421a f1398a;
        int f1399b = 0;
        public final View itemView;

        public ViewHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }

        public int getLayoutPosition() {
            return this.f1398a.f1577b;
        }

        public int getLayoutColumn() {
            return this.f1398a.f1576a;
        }

        public int getItemViewType() {
            return this.f1397a;
        }

        void m887a() {
            m891b(8);
        }

        void m888a(int i) {
            m887a();
            C0421a c0421a = this.f1398a;
            c0421a.f1577b += i;
        }

        void m890b() {
            this.f1398a.f1577b = Integer.MAX_VALUE - this.f1398a.f1577b;
        }

        void m893c() {
            m891b(2);
            this.f1398a.f1577b = Integer.MAX_VALUE - this.f1398a.f1577b;
        }

        void m891b(int i) {
            this.f1399b |= i;
        }

        void m894c(int i) {
            this.f1399b &= i ^ -1;
        }

        void m896d() {
            this.f1399b = 0;
        }

        boolean m889a() {
            return (this.f1399b & 2) != 0;
        }

        boolean m892b() {
            return (this.f1399b & 8) != 0;
        }

        boolean m895c() {
            return (this.f1399b & 4) != 0;
        }

        boolean m897d() {
            return (this.f1399b & 16) != 0;
        }
    }

    static class C0396a extends Observable<C0397b> {
        C0396a() {
        }

        public void m898a() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m905a();
            }
        }

        public void m901a(int i, int i2, int i3) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m908a(i, i2, i3);
            }
        }

        public void m903b() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m910b();
            }
        }

        public void m904c() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m911c();
            }
        }

        public void m900a(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m907a(i, i2);
            }
        }

        public void m899a(int i) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m906a(i);
            }
        }

        public void m902a(int i, int i2, boolean z) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((C0397b) this.mObservers.get(size)).m909a(i, i2, z);
            }
        }
    }

    private class C0397b {
        final /* synthetic */ BlocksView f1400a;

        private C0397b(BlocksView blocksView) {
            this.f1400a = blocksView;
        }

        public void m905a() {
            synchronized (this.f1400a.f1466c) {
                this.f1400a.f1433a;
                this.f1400a.mViewFlinger.m930b();
                this.f1400a.f1458a = true;
                this.f1400a.f1467c = true;
                this.f1400a.requestLayout();
            }
        }

        public void m908a(int i, int i2, int i3) {
            this.f1400a.f1452a = new C0403g(4, i, i2, i3);
            this.f1400a.m1021h();
        }

        public void m910b() {
            synchronized (this.f1400a.f1466c) {
                this.f1400a.f1433a;
                this.f1400a.f1466c.onUpdateChildren();
            }
        }

        public void m911c() {
            this.f1400a.f1433a;
            this.f1400a.f1466c.fastLayoutChildren();
        }

        public void m907a(int i, int i2) {
            this.f1400a.f1452a = new C0403g(1, i, i2);
            this.f1400a.m1021h();
        }

        public void m906a(int i) {
            this.f1400a.f1433a;
            this.f1400a.f1466c.onRemoved(i);
        }

        public void m909a(int i, int i2, boolean z) {
            if (!this.f1400a.f1466c) {
                this.f1400a.f1452a = new C0403g(2, i, i2);
                this.f1400a.m1005a(z);
            }
        }
    }

    static abstract class C0399c {
        private long f1401a = 120;
        C0393a f1402a;
        private long f1403b = 120;
        private long f1404c = 250;

        interface C0393a {
            void mo885a();

            void mo886a(ViewHolder viewHolder);
        }

        static class C0398b implements AnimatorListener {
            C0398b() {
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

        public abstract void m913a();

        public abstract void mo902a(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2);

        public abstract boolean m916a();

        public abstract void m918b();

        public abstract void mo905b(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2);

        public abstract void mo907c(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2);

        C0399c() {
        }

        public long mo901a() {
            return this.f1401a;
        }

        public long mo904b() {
            return this.f1403b;
        }

        public long mo906c() {
            return this.f1404c;
        }

        public void mo903a(C0393a c0393a) {
            this.f1402a = c0393a;
        }
    }

    static class C0400d {
        public int f1405a;
        public int f1406b;
        public int f1407c;
        public int f1408d;

        C0400d() {
        }

        public String toString() {
            return this.f1405a + " " + this.f1406b + " " + this.f1407c + " " + this.f1408d;
        }
    }

    class C0401e extends OverScroller {
        private int f1409a;
        final /* synthetic */ BlocksView f1410a;

        public C0401e(BlocksView blocksView, Context context, Interpolator interpolator) {
            this.f1410a = blocksView;
            super(context, interpolator);
        }

        public boolean equals(Object object) {
            if (!(object instanceof C0401e)) {
                return false;
            }
            C0401e c0401e = (C0401e) object;
            if (getFinalX() == c0401e.getFinalX() && getFinalY() == c0401e.getFinalY() && this.f1409a == c0401e.f1409a) {
                return true;
            }
            return false;
        }

        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, duration);
            this.f1409a = duration;
        }
    }

    public interface C0402f {
        void m922a();
    }

    static class C0403g {
        int f1411a;
        int f1412b;
        int f1413c;
        int f1414d;

        C0403g(int i, int i2, int i3) {
            this.f1411a = i;
            this.f1413c = i2;
            this.f1414d = (i2 + i3) - 1;
            this.f1412b = i3;
        }

        C0403g(int i, int i2, int i3, int i4) {
            this.f1411a = i;
            this.f1413c = i2;
            this.f1414d = i3;
            this.f1412b = i4;
        }
    }

    class C0404h implements Runnable {
        private int f1415a = 0;
        private Interpolator f1416a = BlocksView.m935a();
        private C0401e f1417a;
        final /* synthetic */ BlocksView f1418a;
        private boolean f1419a = false;
        private int f1420b = 0;
        private boolean f1421b = false;
        private int f1422c = 0;
        private int f1423d = 0;
        private int f1424e = 0;

        public C0404h(BlocksView blocksView) {
            this.f1418a = blocksView;
            this.f1417a = new C0401e(blocksView, blocksView.getContext(), BlocksView.m935a());
        }

        void m927a() {
            if (this.f1419a) {
                this.f1421b = true;
                return;
            }
            this.f1418a.removeCallbacks(this);
            this.f1418a.post(this);
        }

        private int m923a(int i, int i2) {
            int abs = Math.abs(i);
            int abs2 = Math.abs(i2);
            Object obj = abs > abs2 ? 1 : null;
            int width = obj != null ? this.f1418a.getWidth() : this.f1418a.getHeight();
            if (obj == null) {
                abs = abs2;
            }
            float f = (float) abs;
            if (this.f1418a.f1466c == 19) {
                abs = (int) (((double) f) / 1.115d);
            } else {
                abs = (int) (((f / ((float) width)) + 1.0f) * 300.0f);
            }
            return Math.min(abs, 2000);
        }

        public void run() {
            int i = 0;
            m924c();
            OverScroller overScroller = this.f1417a;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i2 = currX - this.f1423d;
                int i3 = currY - this.f1424e;
                this.f1423d = currX;
                this.f1424e = currY;
                this.f1418a.m935a();
                if (i2 != 0) {
                    i3 = i2;
                }
                if (i3 != 0) {
                    i2 = this.f1418a.f1466c.scrollBy(i3, this.f1418a.f1433a);
                } else {
                    i2 = 0;
                }
                this.f1418a.m960b();
                this.f1418a.invalidate();
                if (!(i3 == 0 || i2 == 0)) {
                    this.f1418a.m958b(2);
                    this.f1418a.f1466c;
                }
                i2 = (i3 == 0 || i3 == i2) ? 0 : 1;
                if (!this.f1417a.equals(overScroller)) {
                    i = 1;
                }
                if (i != 0 || (!overScroller.isFinished() && i2 == 0)) {
                    m927a();
                } else {
                    this.f1418a.m958b(1);
                }
            } else {
                this.f1418a.m958b(1);
            }
            m925d();
        }

        private void m924c() {
            this.f1421b = false;
            this.f1419a = true;
        }

        private void m925d() {
            this.f1419a = false;
            if (this.f1421b) {
                m927a();
            }
        }

        public void m928a(int i, int i2) {
            m929a(i, i2, m923a(i, i2), this.f1418a.f1466c);
        }

        public void m929a(int i, int i2, int i3, Interpolator interpolator) {
            this.f1415a = i;
            this.f1420b = i2;
            this.f1422c = this.f1418a.m958b(i3);
            if (this.f1416a != interpolator) {
                this.f1416a = interpolator;
                this.f1417a = new C0401e(this.f1418a, this.f1418a.getContext(), interpolator);
            }
            m926e();
        }

        private void m926e() {
            this.f1424e = 0;
            this.f1423d = 0;
            this.f1417a.startScroll(0, 0, this.f1415a, this.f1420b, this.f1422c);
            m927a();
        }

        public void m930b() {
            this.f1418a.removeCallbacks(this);
            this.f1417a.abortAnimation();
        }
    }

    public static class C0405i {
        private int f1425a = -1;
        private BlocksView f1426a;
        private boolean f1427a = false;
        private int f1428b = -1;

        C0405i(BlocksView blocksView, boolean z) {
            this.f1427a = z;
            this.f1426a = blocksView;
            this.f1425a = blocksView.getFirstAttachedPosition();
            this.f1428b = blocksView.getLastAttachedPosition();
        }
    }

    public BlocksView(Context context) {
        super(context);
        this.f1435a = new SparseArray();
        this.f1462b = new SparseArray();
        this.mViewFlinger = new C0404h(this);
        this.f1448a = new C0397b();
        this.f1467c = true;
        this.f1456a = new Object();
        this.f1433a = 1;
        this.f1460b = 1;
        this.f1458a = true;
        this.f1432a = 1.0f;
        this.f1459b = 1.0f;
        this.f1465c = 1.0f;
        this.f1469d = true;
        this.f1468d = Service.CISCO_FNA;
        this.f1470e = false;
        this.f1455a = new C0427g();
        this.f1450a = new C0415b();
        this.f1473h = true;
        this.f1474i = false;
        this.f1461b = new Rect();
        this.f1457a = new C03872(this);
        this.f1454a = new C03926(this);
        this.f1449a = new C03947(this);
        this.f1463b = new C03958(this);
        m981j();
    }

    public BlocksView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlocksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.f1435a = new SparseArray();
        this.f1462b = new SparseArray();
        this.mViewFlinger = new C0404h(this);
        this.f1448a = new C0397b();
        this.f1467c = true;
        this.f1456a = new Object();
        this.f1433a = 1;
        this.f1460b = 1;
        this.f1458a = true;
        this.f1432a = 1.0f;
        this.f1459b = 1.0f;
        this.f1465c = 1.0f;
        this.f1469d = true;
        this.f1468d = Service.CISCO_FNA;
        this.f1470e = false;
        this.f1455a = new C0427g();
        this.f1450a = new C0415b();
        this.f1473h = true;
        this.f1474i = false;
        this.f1461b = new Rect();
        this.f1457a = new C03872(this);
        this.f1454a = new C03926(this);
        this.f1449a = new C03947(this);
        this.f1463b = new C03958(this);
        m981j();
    }

    public synchronized void setAdapter(Adapter adapter) {
        if (this.f1437a != null) {
            this.f1437a.unregisterAdapterDataObserver(this.f1448a);
            m984m();
        }
        this.f1453a.onAdapterChanged(this.f1437a);
        this.f1437a = adapter;
        this.f1437a.registerAdapterDataObserver(this.f1448a);
        this.f1458a = true;
        this.f1467c = true;
        requestLayout();
    }

    protected void removeUnattachedViews() {
        for (int i = 0; i < this.f1435a.size(); i++) {
            SparseArray sparseArray = (SparseArray) this.f1435a.valueAt(i);
            if (sparseArray != null && sparseArray.size() > 0) {
                for (int size = sparseArray.size() - 1; size >= 0; size--) {
                    ViewHolder viewHolder;
                    viewHolder = (ViewHolder) sparseArray.valueAt(size);
                    if (!viewHolder.m896d()) {
                        removeView(viewHolder.itemView);
                    }
                    viewHolder.m896d();
                }
            }
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            viewHolder = m994a(getChildAt(childCount));
            if (!(viewHolder == null || viewHolder.m896d())) {
                m1003a(viewHolder.itemView, true);
                viewHolder.m896d();
            }
        }
    }

    private void m980i() {
        for (int i = 0; i < this.f1435a.size(); i++) {
            ((SparseArray) this.f1435a.valueAt(i)).clear();
        }
        this.f1435a.clear();
    }

    public void setOrientation(Orientation orientation) {
        this.f1453a.setOrientation(orientation);
    }

    public boolean isVisibleToUser() {
        return getGlobalVisibleRect(this.f1461b);
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
        if (this.f1434a == null) {
            this.f1434a = new Rect();
        }
        this.f1434a.set(getPaddingLeft() + getScrollX(), getPaddingTop() + getScrollY(), (getWidth() + getScrollX()) - getPaddingRight(), (getHeight() + getScrollY()) - getPaddingBottom());
        if (fully) {
            return this.f1434a.contains(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
        return this.f1434a.intersect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
    }

    private void m981j() {
        setFocusableInTouchMode(true);
        this.f1453a = new C0424e(this);
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(true);
        this.f1450a.mo903a(this.f1449a);
    }

    public void setVerticalMargin(int margin) {
        this.f1453a.setVerticalMargin(margin);
        requestLayout();
    }

    public int getVerticalMargin() {
        return this.f1453a.getVerticalMargin();
    }

    public void setHorizontalMargin(int margin) {
        this.f1453a.setHorizontalMargin(margin);
        requestLayout();
    }

    public int getHorizontalMargin() {
        return this.f1453a.getHorizontalMargin();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.f1468d = m931a(event.getKeyCode());
        this.f1470e = event.getAction() == 1;
        if (this.f1472g) {
            LOG.m869d("drop the key event when animator start.");
            return true;
        } else if (getChildCount() <= 0) {
            LOG.m869d("child count is 0.");
            return false;
        } else {
            View focusView = getFocusView();
            if (focusView == null || !focusView.dispatchKeyEvent(event)) {
                switch (event.getKeyCode()) {
                    case 4:
                        if (event.getAction() == 0) {
                            m1019f();
                            break;
                        }
                        break;
                    case 19:
                    case 20:
                        if ((this.f1453a.mFocusLoop || this.f1453a.getOrientation() == Orientation.VERTICAL) && this.f1453a.dispatchKeyEvent(event, this.f1468d)) {
                            return true;
                        }
                    case 21:
                    case 22:
                        if ((this.f1453a.mFocusLoop || this.f1453a.getOrientation() == Orientation.HORIZONTAL) && this.f1453a.dispatchKeyEvent(event, this.f1468d)) {
                            return true;
                        }
                }
                return super.dispatchKeyEvent(event);
            }
            LOG.m869d("focused view handled the key event.");
            return true;
        }
    }

    private int m931a(int i) {
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
        return this.f1466c == 19;
    }

    View m995a(View view, int i) {
        return super.focusSearch(view, i);
    }

    public View focusSearch(View focused, int direction) {
        View focusSearch = this.f1453a.focusSearch(focused, direction);
        if (!containsView(this, focusSearch)) {
            m1019f();
        }
        LOG.m869d("direction = " + direction + " focused = " + focused + " view = " + focusSearch);
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
        this.f1453a.onRequestChildFocus(child, focused);
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.f1453a.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        if (this.f1453a == null || !this.f1453a.onAddFocusables(views, direction, focusableMode)) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.f1453a.onRequestFocusInDescendants(direction, previouslyFocusedRect)) {
            return true;
        }
        return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        m983l();
        if (this.f1437a == null) {
            LOG.m870d("BlocksView", "Adapter should not be null!!!");
            return;
        }
        this.f1453a.onLayoutChildren();
        this.f1458a = false;
        if (this.f1467c) {
            this.f1467c = false;
            if (this.f1439a != null) {
                this.f1439a.onFirstLayout(this);
            }
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f1474i = true;
        this.f1467c = true;
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        if (this.f1436a != null && getChildCount() > 0) {
            this.f1436a.onViewAttachedToWindow(this);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f1474i = false;
        m982k();
        if (this.f1436a != null && getChildCount() > 0) {
            this.f1436a.onViewDetachedFromWindow(this);
        }
    }

    private void m982k() {
        this.mViewFlinger.m930b();
        removeCallbacks(this.f1463b);
        this.f1471f = false;
        m983l();
    }

    private void m983l() {
        this.f1450a.mo901a();
        this.f1472g = false;
    }

    public void release() {
        m982k();
        setDescendantFocusability(393216);
        m984m();
        this.f1453a.resetValues();
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    private void m984m() {
        m985n();
        removeAllViews();
        m980i();
        this.f1462b.clear();
    }

    public void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
        this.f1436a = listener;
    }

    public boolean isAttached() {
        return this.f1474i;
    }

    private void m985n() {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            m943a(m994a(getChildAt(childCount)));
        }
    }

    public ViewHolder getViewHolderByPosition(int position) {
        return m994a(getViewByPosition(position));
    }

    public int getViewPosition(View view) {
        ViewHolder a = m994a(view);
        if (a == null) {
            return -1;
        }
        return a.getLayoutPosition();
    }

    public int getFocusPosition() {
        return this.f1453a.getFocusPosition();
    }

    public void setContentHeight(int contentHeight) {
        this.f1453a.setContentHeight(contentHeight);
    }

    public int getContentHeight() {
        return this.f1453a.getContentHeight();
    }

    public void setContentWidth(int contentWidth) {
        this.f1453a.setContentWidth(contentWidth);
    }

    public int getContentWidth() {
        return this.f1453a.getContentWidth();
    }

    void m998a() {
        if (!this.f1464b) {
            this.f1464b = true;
        }
    }

    void m1009b() {
        if (this.f1464b) {
            this.f1464b = false;
        }
    }

    public void requestLayout() {
        if (!this.f1464b) {
            super.requestLayout();
        }
    }

    private Interpolator m960b() {
        switch (this.f1466c) {
            case 17:
                return f1430b;
            case 19:
                return f1431c;
            default:
                return f1429a;
        }
    }

    private int m958b(int i) {
        switch (this.f1466c) {
            case 17:
                return (int) (((float) i) / this.f1432a);
            case 18:
                return (int) (((float) i) / this.f1459b);
            case 19:
                return (int) (((float) i) / this.f1465c);
            default:
                return i;
        }
    }

    public void setScrollRoteScale(float smoothScrollFriction, float contScrollFriction, float quickScrollFriction) {
        if (smoothScrollFriction > 0.0f) {
            this.f1432a = smoothScrollFriction;
        }
        if (contScrollFriction > 0.0f) {
            this.f1459b = contScrollFriction;
        }
        if (quickScrollFriction > 0.0f) {
            this.f1465c = quickScrollFriction;
        }
    }

    void m1013c() {
        post(this.f1457a);
    }

    void m1016d() {
        m958b(17);
    }

    void m1018e() {
        m958b(18);
    }

    public void setQuickFocusLeaveForbidden(boolean isForbidden) {
        this.f1469d = isForbidden;
    }

    private View m935a() {
        View findFocus = this.f1453a.findFocus();
        if (containsView(this, findFocus)) {
            return findFocus;
        }
        return this.f1453a.getFocusView();
    }

    boolean m1006a() {
        View a = m935a();
        View a2 = m994a(a);
        if (!containsView(this, a2)) {
            return m1007a(a, a2);
        }
        if (this.f1453a.isAtEdge(a2, this.f1468d)) {
            if (hasFocus()) {
                this.f1453a.onRequestChildFocus(a2, a2);
            }
            return m1007a(a, a2);
        } else if (isFocusable(getViewPosition(a2))) {
            if (a != a2) {
                if (this.f1466c != 19) {
                    m958b(19);
                    requestChildFocus(null, null);
                    setDescendantFocusability(393216);
                }
                this.f1453a.onRequestChildFocus(a2, a2);
            }
            return true;
        } else {
            if (hasFocus() && this.f1453a.getScrollingView() != a2) {
                this.f1453a.onRequestChildFocus(a2, a2);
            }
            return m1007a(a, a2);
        }
    }

    boolean m1012b() {
        return m1007a(null, null);
    }

    public void stopViewFlinger() {
        this.mViewFlinger.m930b();
        this.f1453a.onScrollStop();
    }

    boolean m1007a(View view, View view2) {
        if (this.f1466c == 19) {
            if (view == null || view2 == null) {
                view = m935a();
                view2 = m994a(view);
            }
            if (this.f1453a.isCanScroll(this.f1468d == Service.CISCO_FNA)) {
                stopViewFlinger();
                m958b(17);
            }
            setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
            if (containsView(this, view2)) {
                if (isFocusable(getViewPosition(view2))) {
                    this.f1453a.resumeChildFocus(view2);
                } else {
                    view.requestFocus();
                    this.f1453a.onRequestChildFocus(view2, view2);
                }
            } else if (!view.isFocused()) {
                view.requestFocus();
            }
        }
        return this.f1469d;
    }

    private void m962b(int i) {
        if (i != this.f1466c) {
            this.f1466c = i;
        }
    }

    public int getScrollState() {
        return this.f1433a;
    }

    private void m972c(int i) {
        boolean z = true;
        this.f1460b = this.f1433a;
        this.f1433a = i;
        if (this.f1460b != 1 && this.f1433a == 1) {
            if (this.f1466c == 19) {
                LayoutManager layoutManager = this.f1453a;
                if (this.f1468d != Service.CISCO_FNA) {
                    z = false;
                }
                if (layoutManager.isCanScroll(z)) {
                    return;
                }
            }
            stopViewFlinger();
        }
        m986o();
    }

    private void m986o() {
        if (this.f1447a == null) {
            return;
        }
        if (this.f1460b != 1 && this.f1433a == 1) {
            this.f1453a.onScrollStop();
            this.f1447a.onScrollStop(this);
        } else if (this.f1460b == 1 && this.f1433a == 2) {
            this.f1447a.onScrollStart(this);
        }
    }

    private void m987p() {
        if (this.f1447a != null && !this.f1472g) {
            this.f1447a.onScroll(this, getFirstAttachedPosition(), getLastAttachedPosition(), this.f1437a.getCount());
        }
    }

    void m1019f() {
        ViewHolder a = m994a(getFocusView());
        if (a != null) {
            this.f1453a.onFocusLost(a);
            if (this.f1440a != null) {
                this.f1440a.onFocusLost(this, a);
            }
        }
    }

    void m1000a(int i, boolean z) {
        if (this.f1441a != null) {
            this.f1441a.onFocusPositionChanged(this, i, z);
        }
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.f1447a = l;
    }

    void m999a(int i) {
        if (this.f1447a != null) {
            this.f1447a.onScrollBefore(this, getViewHolderByPosition(i));
        }
    }

    void m1020g() {
        m972c(1);
    }

    View m994a(View view) {
        return this.f1453a.focusSearch(view, this.f1468d);
    }

    private SparseArray<ViewHolder> m934a(int i) {
        SparseArray<ViewHolder> sparseArray = (SparseArray) this.f1435a.get(i);
        if (sparseArray != null) {
            return sparseArray;
        }
        sparseArray = new SparseArray();
        this.f1435a.put(i, sparseArray);
        return sparseArray;
    }

    private boolean m955a(ViewHolder viewHolder, int i) {
        return !viewHolder.m890b() || (viewHolder.m890b() && viewHolder.getLayoutPosition() == i);
    }

    private boolean m969b(ViewHolder viewHolder, int i) {
        if (!viewHolder.itemView.isFocused()) {
            return true;
        }
        if (getFocusPosition() < getFirstAttachedPosition() || getFocusPosition() > getLastAttachedPosition()) {
            return false;
        }
        return true;
    }

    private ViewHolder m941a(int i, int i2) {
        if (i2 != getFocusPosition()) {
            return null;
        }
        View a = m935a();
        if (a == null) {
            return null;
        }
        ViewHolder a2 = m994a(a);
        if (a2.getItemViewType() != i || !m955a(a2, i2) || a2.m887a() || a2.m896d()) {
            return null;
        }
        ((SparseArray) this.f1435a.get(i)).remove(a2.getLayoutPosition());
        return a2;
    }

    private ViewHolder m961b(int i, int i2) {
        SparseArray sparseArray = (SparseArray) this.f1435a.get(i2);
        if (sparseArray != null && sparseArray.size() > 0) {
            int indexOfKey = sparseArray.indexOfKey(i);
            if (indexOfKey < 0) {
                indexOfKey = 0;
            }
            int i3 = indexOfKey;
            for (int i4 = 0; i4 < sparseArray.size(); i4++) {
                ViewHolder viewHolder = (ViewHolder) sparseArray.valueAt(i3);
                if (!viewHolder.m887a() && m955a(viewHolder, i) && m969b(viewHolder, i)) {
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

    View m996a(C0421a c0421a) {
        int itemViewType = this.f1437a.getItemViewType(c0421a.f1577b);
        ViewHolder a = m941a(itemViewType, c0421a.f1577b);
        if (a == null) {
            a = m961b(c0421a.f1577b, itemViewType);
        }
        if (a == null) {
            LayoutParams generateDefaultLayoutParams;
            ViewHolder createViewHolder = this.f1437a.createViewHolder(this, itemViewType);
            android.view.ViewGroup.LayoutParams layoutParams = createViewHolder.itemView.getLayoutParams();
            if (layoutParams == null) {
                generateDefaultLayoutParams = generateDefaultLayoutParams();
                createViewHolder.itemView.setLayoutParams(generateDefaultLayoutParams);
            } else {
                generateDefaultLayoutParams = (LayoutParams) layoutParams;
            }
            generateDefaultLayoutParams.f1396a = createViewHolder;
            m978d(createViewHolder.itemView);
            a = createViewHolder;
        }
        a.f1398a = c0421a.m1102a();
        if (a.m890b()) {
            a.m894c(8);
        } else {
            this.f1437a.bindViewHolder(a, c0421a.f1577b);
        }
        addView(a.itemView);
        return a.itemView;
    }

    void m1001a(View view) {
        if (view != null) {
            view.setNextFocusUpId(getNextFocusUpId());
            view.setNextFocusDownId(getNextFocusDownId());
            view.setNextFocusRightId(getNextFocusRightId());
            view.setNextFocusLeftId(getNextFocusLeftId());
        }
    }

    private void m978d(View view) {
        final OnFocusChangeListener onFocusChangeListener = view.getOnFocusChangeListener();
        view.setOnFocusChangeListener(new OnFocusChangeListener(this) {
            final /* synthetic */ BlocksView f1389a;

            public void onFocusChange(View view, boolean hasFocus) {
                if (onFocusChangeListener != null) {
                    onFocusChangeListener.onFocusChange(view, hasFocus);
                }
                if (this.f1389a.f1466c != null) {
                    ViewHolder a = this.f1389a.m994a(view);
                    if (a != null && !a.m887a()) {
                        this.f1389a.f1466c.onItemFocusChanged(this.f1389a, a, hasFocus);
                    }
                }
            }
        });
        view.setOnClickListener(new C03894(this));
        view.setOnKeyListener(new C03905(this));
    }

    public void updateItem(ViewHolder vh, int position) {
        this.f1437a.bindViewHolder(vh, position);
    }

    public int getCount() {
        return this.f1437a.getCount();
    }

    public int getLastPosition() {
        return this.f1437a.getLastPosition();
    }

    public void smoothScrollBy(int dx, int dy) {
        this.mViewFlinger.m923a(dx, dy);
    }

    public void setViewRecycled(boolean isViewRecycled) {
        this.f1473h = isViewRecycled;
    }

    boolean m1015c() {
        return this.f1473h;
    }

    void m1010b(View view) {
        if (view != null) {
            m943a(m994a(view));
        }
    }

    void m1003a(View view, boolean z) {
        ViewHolder a = m994a(view);
        if (z) {
            removeView(view);
        } else {
            if (this.f1462b.indexOfValue(view) < 0) {
                LOG.m869d("view = " + view + " position = " + a.getLayoutPosition() + " flags = " + a.f1399b);
            }
            this.f1462b.removeAt(this.f1462b.indexOfValue(view));
            a.m894c(16);
        }
        SparseArray a2 = m931a(a.f1397a);
        if (a2.indexOfValue(a) < 0) {
            a2.put(a.f1398a.f1577b, a);
        }
        if (!a.m890b()) {
            m943a(a);
        }
    }

    public void removeView(View view) {
        if (containsView(this, view)) {
            super.removeView(view);
            int indexOfValue = this.f1462b.indexOfValue(view);
            if (indexOfValue >= 0) {
                this.f1462b.removeAt(indexOfValue);
            }
            ViewHolder a = m994a(view);
            if (a != null) {
                a.m896d();
            }
        }
    }

    public void addView(View child) {
        if (!containsView(this, child)) {
            super.addView(child);
        }
        ViewHolder a = m994a(child);
        if (a != null) {
            if (this.f1462b.indexOfKey(a.getLayoutPosition()) >= 0) {
                LOG.m871e("Warn: key repeat!!! new = " + child + " old = " + this.f1462b.valueAt(this.f1462b.indexOfKey(a.getLayoutPosition())));
            } else {
                this.f1462b.append(a.getLayoutPosition(), child);
            }
            a.m891b(16);
        }
    }

    ViewHolder m997a(View view) {
        while (view != null && (view instanceof View)) {
            android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null && (layoutParams instanceof LayoutParams)) {
                return ((LayoutParams) layoutParams).f1396a;
            }
            view = (View) view.getParent();
        }
        return null;
    }

    void m1004a(ViewHolder viewHolder) {
        if (this.f1445a != null) {
            this.f1445a.onItemDetached(this, viewHolder);
        }
    }

    void m1014c(View view) {
        m1011b(m994a(view));
    }

    void m1011b(ViewHolder viewHolder) {
        if (this.f1445a != null) {
            this.f1445a.onItemAttached(this, viewHolder);
        }
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    void m1021h() {
        m968b(true);
    }

    void m1005a(boolean z) {
        m968b(z);
    }

    private C0400d m943a(ViewHolder viewHolder) {
        C0400d c0400d = new C0400d();
        if (this.f1453a.getOrientation() == Orientation.VERTICAL) {
            c0400d.f1405a = viewHolder.itemView.getLeft();
            c0400d.f1406b = viewHolder.itemView.getTop() - getScrollY();
            c0400d.f1407c = viewHolder.itemView.getRight();
            c0400d.f1408d = viewHolder.itemView.getBottom() - getScrollY();
        } else {
            c0400d.f1405a = viewHolder.itemView.getLeft() - getScrollX();
            c0400d.f1406b = viewHolder.itemView.getTop();
            c0400d.f1407c = viewHolder.itemView.getRight() - getScrollX();
            c0400d.f1408d = viewHolder.itemView.getBottom();
        }
        return c0400d;
    }

    private void m968b(boolean z) {
        if (this.f1452a != null && !this.f1458a) {
            int i;
            boolean z2;
            m983l();
            if (z && m979e()) {
                i = 1;
            } else {
                z2 = false;
            }
            m992u();
            if (this.f1458a) {
                m989r();
                this.f1453a.onLayoutChildren();
                this.f1458a = false;
                m990s();
            } else {
                m988q();
                z2 = false;
            }
            if (i != 0 && isVisibleToUser() && VERSION.SDK_INT >= 14) {
                m972c(1);
                this.f1455a.m1204a(this.f1454a);
                this.f1472g = this.f1450a.mo901a();
            }
        }
    }

    private void m988q() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = m994a(getChildAt(i));
            if (a != null) {
                a.m894c(8);
            }
        }
    }

    private boolean m979e() {
        C0403g c0403g = this.f1452a;
        int i = c0403g.f1413c;
        int i2 = c0403g.f1412b + i;
        switch (c0403g.f1411a) {
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

    private void m989r() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = m994a(getChildAt(i));
            this.f1455a.m1203a(a, m943a(a));
        }
    }

    private void m990s() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewHolder a = m994a(getChildAt(i));
            this.f1455a.m1205b(a, m943a(a));
        }
    }

    private void m950a(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
        this.f1450a.mo902a(viewHolder, c0400d, c0400d2);
        m991t();
    }

    private void m964b(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
        m949a(viewHolder, c0400d);
        this.f1450a.mo905b(viewHolder, c0400d, c0400d2);
        m991t();
    }

    private void m973c(ViewHolder viewHolder, C0400d c0400d, C0400d c0400d2) {
        this.f1450a.mo907c(viewHolder, c0400d, c0400d2);
        m991t();
    }

    private void m949a(ViewHolder viewHolder, C0400d c0400d) {
        View view = viewHolder.itemView;
        addView(view);
        if (this.f1453a.getOrientation() == Orientation.VERTICAL) {
            view.layout(c0400d.f1405a, c0400d.f1406b + getScrollY(), c0400d.f1407c, c0400d.f1408d + getScrollY());
        } else {
            view.layout(c0400d.f1405a + getScrollX(), c0400d.f1406b, c0400d.f1407c + getScrollX(), c0400d.f1408d);
        }
        viewHolder.m891b(4);
    }

    private void m991t() {
        if (!this.f1471f) {
            post(this.f1463b);
            this.f1471f = true;
        }
    }

    private void m992u() {
        C0403g c0403g = this.f1452a;
        switch (c0403g.f1411a) {
            case 1:
                m965b(c0403g);
                return;
            case 2:
                m974c(c0403g);
                return;
            case 4:
                m951a(c0403g);
                return;
            default:
                return;
        }
    }

    private void m951a(C0403g c0403g) {
        m948a(c0403g.f1413c, c0403g.f1414d, c0403g.f1412b);
    }

    private void m948a(int i, int i2, int i3) {
        int i4 = ((i + i3) - i2) - 1;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ViewHolder a = m994a(getChildAt(childCount));
            if (a != null) {
                if (a.f1398a.f1577b < i) {
                    a.m887a();
                } else if (a.f1398a.f1577b > i2) {
                    a.m888a(i4);
                    this.f1458a = true;
                } else if (a.f1398a.f1577b >= i) {
                    a.m890b();
                    this.f1458a = true;
                }
            }
        }
    }

    private void m965b(C0403g c0403g) {
        m941a(c0403g.f1413c, c0403g.f1412b);
        this.f1453a.onItemsAdded(c0403g.f1413c, c0403g.f1412b);
    }

    private void m947a(int i, int i2) {
        if (getChildCount() == 0) {
            this.f1458a = true;
            return;
        }
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            ViewHolder a = m994a(getChildAt(childCount));
            if (a != null) {
                if (a.f1398a.f1577b >= i) {
                    a.m888a(i2);
                    this.f1458a = true;
                }
                a.m887a();
            }
        }
    }

    private void m974c(C0403g c0403g) {
        m963b(c0403g.f1413c, c0403g.f1414d, c0403g.f1412b);
        this.f1453a.onItemsRemoved(c0403g.f1413c, c0403g.f1412b);
    }

    private void m963b(int i, int i2, int i3) {
        int childCount = getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            ViewHolder a = m994a(getChildAt(i4));
            if (a != null) {
                if (a.f1398a.f1577b > i2) {
                    a.m888a(-i3);
                    this.f1458a = true;
                } else if (a.f1398a.f1577b >= i) {
                    a.m893c();
                    this.f1458a = true;
                } else {
                    a.m887a();
                }
            }
        }
    }

    public boolean isFocusable(int position) {
        return this.f1437a.isFocusable(position);
    }

    public void setItemDecoration(ItemDecoration decor) {
        this.f1438a = decor;
    }

    public Adapter getAdapter() {
        return this.f1437a;
    }

    public int getFirstAttachedPosition() {
        return this.f1453a.getFirstAttachedPosition();
    }

    public int getLastAttachedPosition() {
        return this.f1453a.getLastAttachedPosition();
    }

    public void setFocusPlace(FocusPlace focusPlace) {
        this.f1453a.setFocusPlace(focusPlace);
    }

    public void setFocusPlace(int low, int high) {
        this.f1453a.setFocusPlace(low, high);
    }

    public void setFocusPosition(int focusPosition) {
        setFocusPosition(focusPosition, false);
    }

    public void setFocusPosition(int focusPosition, boolean scroll) {
        this.f1453a.setFocusPosition(focusPosition);
        if (scroll) {
            this.mViewFlinger.m930b();
            View focusView = this.f1453a.getFocusView();
            if (focusView != null) {
                this.f1453a.onRequestChildFocus(focusView, focusView);
            }
        }
    }

    public void setFocusMode(int mode) {
        this.f1453a.setScrollMode(mode);
    }

    public void setOnFocusLostListener(OnFocusLostListener l) {
        this.f1440a = l;
    }

    public void setOnItemFocusChangedListener(OnItemFocusChangedListener l) {
        this.f1444a = l;
    }

    public void setOnFocusPositionChangedListener(OnFocusPositionChangedListener l) {
        this.f1441a = l;
    }

    public void setOnItemStateChangeListener(OnItemStateChangeListener l) {
        this.f1445a = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.f1443a = l;
    }

    public void setOnItemAnimatorFinishListener(OnItemAnimatorFinishListener l) {
        this.f1442a = l;
    }

    public void setGravity(int gravity) {
        this.f1453a.setGravity(gravity);
    }

    public LinkedList<View> getViewList() {
        LinkedList<View> linkedList = new LinkedList();
        for (int i = 0; i < linkedList.size(); i++) {
            linkedList.add(getChildAt(i));
        }
        return linkedList;
    }

    public LayoutManager getLayoutManager() {
        return this.f1453a;
    }

    public View getViewByPosition(int position) {
        if (position == -1) {
            return null;
        }
        View view = (View) this.f1462b.get(position);
        if (view != null) {
            return view;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (m994a(childAt).getLayoutPosition() == position) {
                return childAt;
            }
        }
        return view;
    }

    public void setFocusLeaveForbidden(int direction) {
        this.f1453a.setFocusLeaveForbidden(direction);
    }

    public void setSpringbackForbidden(int direction) {
        this.f1453a.setSpringbackForbidden(direction);
    }

    public void setShakeForbidden(int direction) {
        this.f1453a.setShakeForbidden(direction);
    }

    public void setSpringbackDelta(int delta) {
        this.f1453a.setSpringbackDelta(delta);
    }

    public int getScrollType() {
        return this.f1466c;
    }

    public void setExtraPadding(int extraPadding) {
        this.f1453a.setExtraPadding(extraPadding);
    }

    public void setFocusLoop(boolean focusLoop) {
        this.f1453a.setFocusLoop(focusLoop);
    }

    public View getFocusView() {
        return this.f1453a.getFocusView();
    }

    int m993a() {
        return this.f1453a.getMovement();
    }

    public int getColumn(int position) {
        ViewHolder viewHolderByPosition = getViewHolderByPosition(position);
        return viewHolderByPosition != null ? viewHolderByPosition.getLayoutColumn() : -1;
    }

    public int getRow(int position) {
        return 0;
    }

    boolean m1017d() {
        return this.f1453a.hasScrollOffset();
    }

    int m1008b() {
        return this.f1453a.getMinScroll();
    }

    public int getDirection() {
        return this.f1468d;
    }

    public void setOnFirstLayoutListener(OnFirstLayoutListener listener) {
        this.f1439a = listener;
    }

    void m1002a(View view, int i) {
        if (this.f1446a != null) {
            this.f1446a.onMoveToTheBorder(this, view, i);
        }
    }

    public void setOnMoveToTheBorderListener(OnMoveToTheBorderListener listener) {
        this.f1446a = listener;
    }

    protected void lineFeed() {
        if (this.f1451a != null) {
            this.f1451a.m922a();
        }
    }

    public void setOnLineFeedListener(C0402f l) {
        this.f1451a = l;
    }

    public void setScrollOnly(boolean scrollOnly) {
        this.f1453a.setScrollOnly(scrollOnly);
    }

    public void setFocusMemorable(boolean memorable) {
        this.f1453a.setFocusMemorable(memorable);
    }

    public C0405i getVisibleViewsIterator(boolean fully) {
        return new C0405i(this, fully);
    }

    public BlockLayout getBlockLayout(int position) {
        return this.f1453a.getBlockLayout(position);
    }
}
