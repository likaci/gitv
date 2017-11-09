package com.gala.video.app.epg.home.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class GuidePresenter {
    private static final int GUIDE_AUTO_LOOP_INTERVAL = 10000;
    private static final int MSG_NEXT_GUIDE = 10000;
    private static final String TAG = "/home/GuidePresenter";
    private GuideAdapter mAdapter;
    private final MyHandler mHandler = new MyHandler();
    private onGuideDismissListener mListener;
    private final ViewPager mPager;
    private final FrameLayout mRootView;

    private static class GuideAdapter extends PagerAdapter {
        private static final int[] GUIDE_MAP_IMAGE = new int[0];
        private List<ImageView> mViews = new ArrayList(GUIDE_MAP_IMAGE.length);

        public GuideAdapter(Context context) {
            for (int index : GUIDE_MAP_IMAGE) {
                this.mViews.add(new ImageView(context));
            }
        }

        public void update(int index) {
            if (index >= 0 && index < GUIDE_MAP_IMAGE.length) {
                ((ImageView) this.mViews.get(index)).setImageResource(GUIDE_MAP_IMAGE[index]);
            }
        }

        public int getCount() {
            return GUIDE_MAP_IMAGE.length;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = (ImageView) this.mViews.get(position);
            container.addView(imageView, 0);
            imageView.setImageResource(GUIDE_MAP_IMAGE[position]);
            return imageView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) this.mViews.get(position));
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    private class MyHandler extends Handler {
        private MyHandler() {
        }

        public void handleMessage(Message msg) {
            GuidePresenter.this.nextPage();
        }
    }

    public interface onGuideDismissListener {
        void onGuideDismiss(FrameLayout frameLayout, ViewPager viewPager);
    }

    public GuidePresenter(FrameLayout rootView) {
        this.mPager = new ViewPager(rootView.getContext());
        this.mRootView = rootView;
        rootView.addView(this.mPager, new MarginLayoutParams(-1, -1));
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.m1568d(TAG, "guide dispatchKeyEvent " + event);
        if (event.getAction() == 0 && (event.getKeyCode() == 22 || event.getKeyCode() == 23 || event.getKeyCode() == 20 || event.getKeyCode() == 4 || event.getKeyCode() == 66)) {
            if (this.mPager.getCurrentItem() == this.mAdapter.getCount() - 1) {
                this.mHandler.removeCallbacksAndMessages(null);
                if (this.mListener == null) {
                    return true;
                }
                this.mListener.onGuideDismiss(this.mRootView, this.mPager);
                return true;
            }
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1, false);
            return true;
        } else if (event.getAction() == 0 && (event.getKeyCode() == 19 || event.getKeyCode() == 21)) {
            if (this.mPager.getCurrentItem() == 0) {
                return true;
            }
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() - 1, false);
            return true;
        } else if (event.getKeyCode() == 82) {
            return true;
        } else {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler.sendEmptyMessageDelayed(10000, 10000);
            return false;
        }
    }

    public void setOnGuideCompleteListener(onGuideDismissListener listener) {
        this.mListener = listener;
    }

    private void nextPage() {
        if (this.mPager.getCurrentItem() != this.mAdapter.getCount() - 1) {
            this.mPager.setCurrentItem(this.mPager.getCurrentItem() + 1);
        }
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandler.sendEmptyMessageDelayed(10000, 10000);
    }

    public void show() {
        this.mAdapter = new GuideAdapter(this.mPager.getContext());
        this.mPager.setAdapter(this.mAdapter);
        this.mHandler.removeCallbacksAndMessages(null);
        this.mHandler.sendEmptyMessageDelayed(10000, 10000);
    }
}
