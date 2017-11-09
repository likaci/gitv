package com.gala.video.app.epg.home.component;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;

public abstract class WidgetTree extends Widget {
    private static final int ARRAY_CAPACITY_INCREMENT = 12;
    private static final int ARRAY_INITIAL_CAPACITY = 20;
    private static final String TAG = "WidgetTree";
    private Widget[] mChildren = new Widget[20];
    private int mChildrenCount = 0;

    public void addWidget(Widget child) {
        addWidget(child, -1);
    }

    public void addWidget(Widget child, int index) {
        if (child.mParent != null) {
            throw new IllegalStateException("The specified child already has a parent. ");
        }
        if (index < 0) {
            index = this.mChildrenCount;
        }
        child.mParent = this;
        addInArray(child, index);
        child.indexInParent = index;
        onChildAdded(child);
    }

    private void addInArray(Widget child, int index) {
        Widget[] children = this.mChildren;
        int count = this.mChildrenCount;
        int size = children.length;
        if (index == count) {
            if (size == count) {
                this.mChildren = new Widget[(size + 12)];
                System.arraycopy(children, 0, this.mChildren, 0, size);
                children = this.mChildren;
            }
            int i = this.mChildrenCount;
            this.mChildrenCount = i + 1;
            children[i] = child;
        } else if (index < count) {
            if (size == count) {
                this.mChildren = new Widget[(size + 12)];
                System.arraycopy(children, 0, this.mChildren, 0, index);
                System.arraycopy(children, index, this.mChildren, index + 1, count - index);
                children = this.mChildren;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = child;
            this.mChildrenCount++;
        } else {
            LogUtils.e(TAG, "IndexOutOfBoundsException index=" + index + " count=" + count);
        }
    }

    public int getChildCount() {
        return this.mChildrenCount;
    }

    public Widget getChildAt(int index) {
        if (index < 0 || index >= this.mChildrenCount) {
            return null;
        }
        return this.mChildren[index];
    }

    public int indexOfChild(Widget child) {
        int count = this.mChildrenCount;
        Widget[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    public int indexOfChildForPingback(Widget child) {
        Widget[] children = this.mChildren;
        int index = -1;
        for (Widget widget : children) {
            if (!(widget == null || !(widget instanceof WidgetTree) || ((WidgetTree) widget).mChildrenCount == 0)) {
                index++;
            }
            if (widget == child) {
                return index;
            }
        }
        return -1;
    }

    private void removeFromArray(int index) {
        Widget[] children = this.mChildren;
        int count = this.mChildrenCount;
        int i;
        if (index == count - 1) {
            synchronized (removeList) {
                removeList.add(this.mChildren[index]);
            }
            i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        } else if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        } else {
            System.arraycopy(children, index + 1, children, index, (count - index) - 1);
            synchronized (removeList) {
                removeList.add(this.mChildren[index]);
            }
            i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        }
    }

    protected void onChildAdded(Widget child) {
    }

    protected void onChildRemoved(Widget child) {
    }

    public void sendEvent(int eventtype, Object arg) {
        int c = this.mChildrenCount;
        for (int i = 0; i < c; i++) {
            if (this.mChildren[i] != null) {
                this.mChildren[i].onEvent(eventtype, arg);
            }
        }
    }

    public void onEvent(int event, Object arg) {
        sendEvent(event, arg);
    }

    final void dispatchCreate() {
        super.dispatchCreate();
        for (int i = 0; i < this.mChildrenCount; i++) {
            this.mChildren[i].create();
        }
    }

    final void dispatchDestroy() {
        for (int i = 0; i < this.mChildrenCount; i++) {
            this.mChildren[i].destroy();
        }
        super.dispatchDestroy();
    }

    public Object buildUI(Context context) {
        return null;
    }

    public Object updateUI() {
        for (int i = 0; i < this.mChildrenCount; i++) {
            this.mChildren[i].updateUI();
        }
        return null;
    }
}
