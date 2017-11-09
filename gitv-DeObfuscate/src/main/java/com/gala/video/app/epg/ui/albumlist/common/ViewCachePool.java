package com.gala.video.app.epg.ui.albumlist.common;

import android.view.View;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.tvos.apps.utils.LogUtils;
import java.util.Stack;

public class ViewCachePool {
    private static final Object LOCK = new Object();
    private static final int OFFSET = 10;
    private static final String TAG = "EPG/album4/ViewCachePool";
    private static volatile ViewCachePool mPool;
    private static volatile Stack<View> mViewStack;
    private int MAX_SIZE = 60;
    private ViewType mViewType = ViewType.PORTRAIT;

    public interface PreCreateViewListener {
        View getView();

        void onComplete();
    }

    public enum ViewType {
        PORTRAIT,
        HORIZONTAL,
        EXPAND
    }

    public static ViewCachePool getInstance() {
        if (mPool == null) {
            synchronized (LOCK) {
                if (mPool == null) {
                    mPool = new ViewCachePool();
                }
            }
        }
        if (mViewStack == null) {
            synchronized (LOCK) {
                if (mViewStack == null) {
                    mViewStack = new Stack();
                }
            }
        }
        return mPool;
    }

    public void setTotalSize(int size, ViewType type) {
        this.MAX_SIZE = size;
        LogUtils.m1738e(TAG, "setTotalSize ----  size = " + size + ", type = " + type + ",mViewType=" + this.mViewType);
        if (type != this.mViewType) {
            this.mViewType = type;
            mViewStack.clear();
        }
    }

    public int getPoolCount() {
        if (mViewStack == null) {
            return 0;
        }
        return mViewStack.size();
    }

    public void setViewType(ViewType type) {
        this.mViewType = type;
    }

    public View getCachedView() {
        LogUtils.m1738e(TAG, "getCachedView --- size = " + mViewStack.size());
        if (mViewStack.isEmpty()) {
            return null;
        }
        return (View) mViewStack.pop();
    }

    public void putView(View view) {
        if (mViewStack != null && view != null) {
            mViewStack.push(view);
        }
    }

    public void preCreateViews(PreCreateViewListener preCreateViewListener) {
        final int size = mViewStack.size();
        if (size < this.MAX_SIZE) {
            final int loadMaxIndex = preCreateViewListener == null ? size + 10 : this.MAX_SIZE;
            LogUtils.m1738e(TAG, "preCreateViews -------- 创建个数： " + (loadMaxIndex - size));
            final long time = System.currentTimeMillis();
            final PreCreateViewListener preCreateViewListener2 = preCreateViewListener;
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    int i = size;
                    while (i < loadMaxIndex) {
                        if (ViewCachePool.mViewStack == null || preCreateViewListener2 == null) {
                            LogUtils.m1738e(ViewCachePool.TAG, "preCreateViews -------- Stack or createListener is null");
                            return;
                        }
                        ViewCachePool.this.putView(preCreateViewListener2.getView());
                        if (ViewCachePool.mViewStack.size() < loadMaxIndex) {
                            i++;
                        } else if (preCreateViewListener2 != null) {
                            LogUtils.m1738e(ViewCachePool.TAG, "preCreateViews --------onComplete time =  " + (System.currentTimeMillis() - time));
                            preCreateViewListener2.onComplete();
                            return;
                        } else {
                            return;
                        }
                    }
                }
            });
        } else if (preCreateViewListener != null) {
            preCreateViewListener.onComplete();
        }
    }
}
