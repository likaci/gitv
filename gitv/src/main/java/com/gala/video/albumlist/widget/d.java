package com.gala.video.albumlist.widget;

import android.util.Log;
import android.view.View;
import com.gala.video.albumlist.layout.BlockLayout;
import java.util.ArrayList;
import java.util.List;

public class d {
    private BlockLayout a;
    protected b f579a;
    private List<BlockLayout> f580a = new ArrayList();

    public static class a implements Cloneable {
        public int a;
        public int b;

        public /* synthetic */ Object clone() throws CloneNotSupportedException {
            return a();
        }

        public a a() {
            try {
                return (a) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    public interface b {
        int a();

        int a(int i);

        int a(View view);

        int a(a aVar, boolean z, Object[] objArr);

        void a(Object obj, int i, int i2, int i3, int i4, boolean z);

        int b(int i);

        int b(View view);

        void b(Object obj, int i, int i2, int i3, int i4, boolean z);

        int c(int i);

        int c(View view);

        int d(int i);

        int d(View view);

        int e(int i);

        int f(int i);

        int g(int i);

        int getCount();

        int getFirstAttachedPosition();

        int getFocusPosition();

        int getLastAttachedPosition();

        int getPaddingEnd();

        int getPaddingMin();

        int getPaddingStart();

        View getViewByPosition(int i);

        int getWidth();

        int h(int i);

        int i(int i);

        int j(int i);

        int k(int i);

        void scrapView(View view, boolean z);
    }

    public d(b bVar) {
        this.f579a = bVar;
    }

    public void a(List<BlockLayout> list) {
        this.a = null;
        this.f580a.clear();
        this.f580a.addAll(list);
        int i = 0;
        for (BlockLayout blockLayout : list) {
            blockLayout.setRang(i, (blockLayout.getItemCount() + i) - 1);
            i += blockLayout.getItemCount();
            blockLayout.setGrid(this);
            blockLayout.setProvider(this.f579a);
        }
    }

    protected int a() {
        if (this.f579a.getLastAttachedPosition() >= 0) {
            return this.f579a.getLastAttachedPosition() + 1;
        }
        if (this.f579a.getFocusPosition() >= 0) {
            return this.f579a.getFocusPosition();
        }
        return 0;
    }

    protected int b() {
        if (this.f579a.getFirstAttachedPosition() >= 0) {
            return this.f579a.getFirstAttachedPosition() - 1;
        }
        return this.f579a.getCount() - 1;
    }

    public int a(int i, boolean z) {
        BlockLayout a = a(i, z);
        if (a != null) {
            return a.getNumRows(i);
        }
        Log.d("Grid", "blockLayout is null, position = " + i);
        return 1;
    }

    public BlockLayout m99a(int i, boolean z) {
        if (this.a != null && !this.a.isOutRang(i)) {
            return this.a;
        }
        BlockLayout a = a(i);
        if (a == null) {
            return a;
        }
        a.updateLayoutRegion(a(a, z), z);
        this.a = a;
        return a;
    }

    private BlockLayout a(BlockLayout blockLayout, boolean z) {
        int i;
        int indexOf = this.f580a.indexOf(blockLayout);
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
            if (i < 0 || i > this.f580a.size() - 1) {
                return null;
            }
        } while (((BlockLayout) this.f580a.get(i)).getItemCount() <= 0);
        return (BlockLayout) this.f580a.get(i);
    }

    public BlockLayout a(int i) {
        if (this.f580a == null) {
            return null;
        }
        int size = this.f580a.size();
        if (size == 0) {
            return null;
        }
        BlockLayout blockLayout;
        int i2 = 0;
        int i3 = size - 1;
        while (i2 <= i3) {
            int i4 = (i2 + i3) / 2;
            blockLayout = (BlockLayout) this.f580a.get(i4);
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

    protected boolean m100a(int i, boolean z) {
        int a = a();
        BlockLayout a2 = a(a, true);
        if (a2 == null) {
            return false;
        }
        return a2.appendAttachedItems(a, i, z);
    }

    protected boolean a(boolean z) {
        int b = b();
        BlockLayout a = a(b, false);
        if (a == null) {
            return false;
        }
        return a.prependAttachedItems(b, z);
    }
}
