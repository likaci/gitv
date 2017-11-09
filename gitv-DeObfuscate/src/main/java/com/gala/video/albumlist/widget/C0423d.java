package com.gala.video.albumlist.widget;

import android.util.Log;
import android.view.View;
import com.gala.video.albumlist.layout.BlockLayout;
import java.util.ArrayList;
import java.util.List;

public class C0423d {
    private BlockLayout f1578a;
    protected C0422b f1579a;
    private List<BlockLayout> f1580a = new ArrayList();

    public static class C0421a implements Cloneable {
        public int f1576a;
        public int f1577b;

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            return m1102a();
        }

        public C0421a m1102a() {
            try {
                return (C0421a) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    public interface C0422b {
        int mo908a();

        int mo909a(int i);

        int mo910a(View view);

        int mo911a(C0421a c0421a, boolean z, Object[] objArr);

        void mo912a(Object obj, int i, int i2, int i3, int i4, boolean z);

        int mo913b(int i);

        int mo914b(View view);

        void mo915b(Object obj, int i, int i2, int i3, int i4, boolean z);

        int mo916c(int i);

        int mo917c(View view);

        int mo919d(int i);

        int mo920d(View view);

        int mo922e(int i);

        int mo923f(int i);

        int mo927g(int i);

        int getCount();

        int getFirstAttachedPosition();

        int getFocusPosition();

        int getLastAttachedPosition();

        int getPaddingEnd();

        int getPaddingMin();

        int getPaddingStart();

        View getViewByPosition(int i);

        int getWidth();

        int mo937h(int i);

        int mo939i(int i);

        int mo947j(int i);

        int mo948k(int i);

        void scrapView(View view, boolean z);
    }

    public C0423d(C0422b c0422b) {
        this.f1579a = c0422b;
    }

    public void m1127a(List<BlockLayout> list) {
        this.f1578a = null;
        this.f1580a.clear();
        this.f1580a.addAll(list);
        int i = 0;
        for (BlockLayout blockLayout : list) {
            blockLayout.setRang(i, (blockLayout.getItemCount() + i) - 1);
            i += blockLayout.getItemCount();
            blockLayout.setGrid(this);
            blockLayout.setProvider(this.f1579a);
        }
    }

    protected int m1123a() {
        if (this.f1579a.getLastAttachedPosition() >= 0) {
            return this.f1579a.getLastAttachedPosition() + 1;
        }
        if (this.f1579a.getFocusPosition() >= 0) {
            return this.f1579a.getFocusPosition();
        }
        return 0;
    }

    protected int m1130b() {
        if (this.f1579a.getFirstAttachedPosition() >= 0) {
            return this.f1579a.getFirstAttachedPosition() - 1;
        }
        return this.f1579a.getCount() - 1;
    }

    public int m1124a(int i, boolean z) {
        BlockLayout a = m1124a(i, z);
        if (a != null) {
            return a.getNumRows(i);
        }
        Log.d("Grid", "blockLayout is null, position = " + i);
        return 1;
    }

    public BlockLayout m1126a(int i, boolean z) {
        if (this.f1578a != null && !this.f1578a.isOutRang(i)) {
            return this.f1578a;
        }
        BlockLayout a = m1125a(i);
        if (a == null) {
            return a;
        }
        a.updateLayoutRegion(m1122a(a, z), z);
        this.f1578a = a;
        return a;
    }

    private BlockLayout m1122a(BlockLayout blockLayout, boolean z) {
        int i;
        int indexOf = this.f1580a.indexOf(blockLayout);
        do {
            int i2;
            if (z) {
                i2 = indexOf - 1;
                i = i2;
                indexOf = i2;
            } else {
                i2 = indexOf + 1;
                i = i2;
                indexOf = i2;
            }
            if (i < 0 || i > this.f1580a.size() - 1) {
                return null;
            }
        } while (((BlockLayout) this.f1580a.get(i)).getItemCount() <= 0);
        return (BlockLayout) this.f1580a.get(i);
    }

    public BlockLayout m1125a(int i) {
        if (this.f1580a == null) {
            return null;
        }
        int size = this.f1580a.size();
        if (size == 0) {
            return null;
        }
        BlockLayout blockLayout;
        int i2 = 0;
        int i3 = size - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) / 2;
            blockLayout = (BlockLayout) this.f1580a.get(i4);
            if (blockLayout.getFirstPosition() <= i) {
                if (blockLayout.getLastPosition() >= i) {
                    if (blockLayout.getFirstPosition() <= i && blockLayout.getLastPosition() >= i) {
                        break;
                    }
                    size = i3;
                    i3 = i2;
                } else {
                    int i5 = i3;
                    i3 = i4 + 1;
                    size = i5;
                }
            } else {
                size = i4 - 1;
                i3 = i2;
            }
            i2 = i3;
            i3 = size;
        }
        blockLayout = null;
        return blockLayout;
    }

    protected boolean m1128a(int i, boolean z) {
        int a = m1123a();
        BlockLayout a2 = m1124a(a, true);
        if (a2 == null) {
            return false;
        }
        return a2.appendAttachedItems(a, i, z);
    }

    protected boolean m1129a(boolean z) {
        int b = m1130b();
        BlockLayout a = m1124a(b, false);
        if (a == null) {
            return false;
        }
        return a.prependAttachedItems(b, z);
    }
}
