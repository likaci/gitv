package com.gala.video.app.epg.home.presenter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.component.PageManage;
import com.gala.video.app.epg.home.contract.PromotionContract.Presenter;
import com.gala.video.app.epg.home.controller.UIController;
import com.gala.video.app.epg.home.controller.UIEvent;
import com.gala.video.app.epg.home.controller.activity.ActivityLifeCycleDispatcher;
import com.gala.video.app.epg.home.controller.activity.IActivityLifeCycle;
import com.gala.video.app.epg.home.controller.exit.ExitDialogStatusDispatcher;
import com.gala.video.app.epg.home.controller.exit.IExitDialogStatusListener;
import com.gala.video.app.epg.home.data.TabData;
import com.gala.video.app.epg.home.widget.ViewPager.OnPageChangeListener;
import com.gala.video.app.epg.home.widget.ViewPager.SimpleOnPageChangeListener;
import com.gala.video.app.epg.home.widget.pager.ScrollViewPager;
import com.gala.video.app.epg.home.widget.tabhost.TabBarAdapter;
import com.gala.video.app.epg.home.widget.tabhost.TabBarHost;
import com.gala.video.app.epg.home.widget.tabhost.TabBarHost.OnTurnPageListener;
import com.gala.video.app.epg.home.widget.tabhost.TabBarSettingView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.utils.TraceEx;
import java.util.ArrayList;
import java.util.List;

public class MainPagePresenter implements IActivityLifeCycle, IExitDialogStatusListener, IStatusListener {
    private static final String TAG = "home/MainPagePresenter";
    private static final boolean sLoop = false;
    private ActionBarPresenter mActionBarPresenter;
    private final Context mContext;
    private int mDefaultTab = 0;
    private boolean mIsPageScrolled = false;
    private OnPageChangeListener mPageChangeListener = new SimpleOnPageChangeListener() {
        public void onPageScrolled(int position, float percent, int offset) {
        }

        public void onPageSelected(int position) {
            TraceEx.beginSection("onPageSelected");
            if (LogUtils.mIsDebug) {
                LogUtils.d(MainPagePresenter.TAG, "onPageSelected position = " + position + " PrePageIndex = " + MainPagePresenter.this.mPrePageIndex);
            }
            int currentPageIndex = position % MainPagePresenter.this.mTabBarHost.getChildCount();
            MainPagePresenter.this.setActionBarNextFocusDownId(MainPagePresenter.this.mTabBarHost.getChildViewAt(currentPageIndex).getId());
            if (MainPagePresenter.this.mPromotionPresenter != null) {
                MainPagePresenter.this.mPromotionPresenter.setNextFocusDownId(MainPagePresenter.this.mTabBarHost.getChildViewAt(currentPageIndex).getId());
            }
            if (MainPagePresenter.this.mPages != null && currentPageIndex < MainPagePresenter.this.mPages.size() && MainPagePresenter.this.mPrePageIndex < MainPagePresenter.this.mPages.size()) {
                ((PageManage) MainPagePresenter.this.mPages.get(MainPagePresenter.this.mPrePageIndex)).pageScrollStart();
                ImageProviderApi.getImageProvider().stopAllTasks();
            }
            TraceEx.endSection();
        }

        public void onPageScrollStateChanged(int state) {
            TraceEx.beginSection("onPageScrollStateChanged");
            if (LogUtils.mIsDebug) {
                LogUtils.d(MainPagePresenter.TAG, "onPageScrollStateChanged state = " + state + " getCurrentItem = " + MainPagePresenter.this.mPager.getCurrentItem());
            }
            if (state == 0) {
                int currentPageIndex = MainPagePresenter.this.mPager.getCurrentItem() % MainPagePresenter.this.mTabBarHost.getChildCount();
                if (MainPagePresenter.this.mPages != null && currentPageIndex < MainPagePresenter.this.mPages.size()) {
                    ((PageManage) MainPagePresenter.this.mPages.get(currentPageIndex)).onPageIn();
                }
                if (!(MainPagePresenter.this.mPages == null || MainPagePresenter.this.mPrePageIndex == -1 || MainPagePresenter.this.mPrePageIndex >= MainPagePresenter.this.mPages.size() || MainPagePresenter.this.mPrePageIndex == currentPageIndex)) {
                    ((PageManage) MainPagePresenter.this.mPages.get(MainPagePresenter.this.mPrePageIndex)).onPageOut();
                }
                MainPagePresenter.this.mPrePageIndex = currentPageIndex;
                MainPagePresenter.this.muiEvent.post(258, null);
            } else if (MainPagePresenter.this.mPages != null && MainPagePresenter.this.mPrePageIndex >= 0 && MainPagePresenter.this.mPrePageIndex < MainPagePresenter.this.mPages.size() && state == 2) {
                MainPagePresenter.this.mIsPageScrolled = true;
                if (MainPagePresenter.this.mPages.get(MainPagePresenter.this.mPrePageIndex) != null) {
                    ((PageManage) MainPagePresenter.this.mPages.get(MainPagePresenter.this.mPrePageIndex)).pageScrollEnd();
                }
                MainPagePresenter.this.muiEvent.post(257, null);
            }
            TraceEx.endSection();
        }
    };
    private List<ViewGroup> mPageList = new ArrayList();
    private final ScrollViewPager mPager;
    private List<PageManage> mPages;
    private int mPrePageIndex = -1;
    private Presenter mPromotionPresenter;
    private TabBarSettingView mSettingView;
    private Runnable mStopTimeCallback;
    private TabBarAdapter mTabBarAdapter;
    private final TabBarHost mTabBarHost;
    private int[] mTabBarIds;
    private ImageView mTabHostDecoratedView;
    private OnFocusChangeListener mTabHostFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View view, boolean hasfocus) {
            int id = MainPagePresenter.this.mActionBarPresenter.getPreFocusUpId();
            int promotionId = MainPagePresenter.this.mPromotionPresenter.getPreFocusId();
            if (id != -1) {
                view.setNextFocusUpId(MainPagePresenter.this.mActionBarPresenter.getPreFocusUpId());
            } else if (promotionId == -1 || !MainPagePresenter.this.mPromotionPresenter.isPromotionViewVisibility()) {
                view.setNextFocusUpId(MainPagePresenter.this.mActionBarPresenter.getLastViewId());
            } else {
                view.setNextFocusUpId(promotionId);
            }
        }
    };
    private List<TabData> mTabListInfo = new ArrayList();
    private int mTargetPage = -1;
    private RelativeLayout mTopLayout;
    private OnTurnPageListener mTurnPageListener = new OnTurnPageListener() {
        public void onTurnPage(int newPage) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(MainPagePresenter.TAG, "onTurnPage newPage = " + newPage);
            }
            MainPagePresenter.this.mPager.setCurrentItem(newPage);
        }
    };
    private Handler mhandle;
    private final UIEvent muiEvent;

    public MainPagePresenter(Context context, View rootView, ActionBarPresenter presenter, UIEvent uiEvent, int targetPage, Presenter promotionPresenter) {
        this.mContext = context;
        this.mPager = (ScrollViewPager) rootView.findViewById(R.id.epg_pager);
        this.mTabBarHost = (TabBarHost) rootView.findViewById(R.id.epg_tab_host);
        this.mTabHostDecoratedView = (ImageView) rootView.findViewById(R.id.epg_tab_bar_decorated);
        this.mTopLayout = (RelativeLayout) rootView.findViewById(R.id.epg_top_layout);
        presenter.setActionBarResource(this.mTopLayout);
        this.mActionBarPresenter = presenter;
        this.mPromotionPresenter = promotionPresenter;
        this.muiEvent = uiEvent;
        this.mTargetPage = targetPage;
        ActivityLifeCycleDispatcher.get().register(this);
        this.mSettingView = (TabBarSettingView) rootView.findViewById(R.id.epg_home_tab_setting_view);
    }

    public void buildTabHost(PageManage[] pages, int max) {
        boolean isFocusAssigned = false;
        if (pages != null) {
            this.mTabBarHost.removeAllViewsInLayout();
            this.mTabListInfo.clear();
            int size = 0;
            int index = 0;
            for (PageManage tab : pages) {
                if (tab.mTabdata != null) {
                    this.mTabListInfo.add(tab.mTabdata);
                    if (tab.mTabdata.isIsFocusTab() && !isFocusAssigned) {
                        this.mDefaultTab = index;
                        isFocusAssigned = true;
                    }
                    index++;
                }
                LogUtils.d(TAG, "tab host data = " + tab.mTabdata);
                size++;
                if (size >= max) {
                    break;
                }
            }
            LogUtils.d(TAG, "tab host size : " + this.mTabListInfo.size());
            this.mTabBarAdapter = new TabBarAdapter(this.mContext, this.mTabListInfo, this.mTabHostDecoratedView);
            this.mTabBarHost.setAdapter(this.mTabBarAdapter, this.mDefaultTab);
            initViewPager();
            setActionBarNextFocusDownId();
            if (this.mPromotionPresenter != null) {
                this.mPromotionPresenter.setNextFocusDownId(this.mTabBarHost.getChildViewAt(this.mTabBarHost.getCurrentChildIndex()).getId());
            }
            this.mPrePageIndex = this.mTabBarHost.getCurrentChildIndex();
            this.mTabBarIds = this.mTabBarHost.getTabBarIds();
            this.mTabBarHost.requestChildFocus(this.mDefaultTab);
            initFocusTracer(pages, max);
            View lastTabHostView = this.mTabBarHost.getChildViewAt(this.mTabBarHost.getChildCount() - 1);
            lastTabHostView.setNextFocusRightId(this.mSettingView.getTextView().getId());
            this.mSettingView.getTextView().setNextFocusLeftId(lastTabHostView.getId());
        }
    }

    public void requestDefaultFocus() {
        if (getDefaultTab() < this.mPageList.size()) {
            this.mTabBarHost.requestChildFocus(getDefaultTab());
            ((ViewGroup) this.mPageList.get(getDefaultTab())).requestFocus();
        }
    }

    public int getCurrentPageindex() {
        if (this.mPager != null) {
            return this.mPager.getCurrentItem();
        }
        return 0;
    }

    public boolean isPageScrolled() {
        return this.mIsPageScrolled;
    }

    public void initFocusTracer(PageManage[] pages, int len) {
        if (pages != null) {
            int index = 0;
            while (index < len && index < pages.length) {
                if (pages[index].mChild != null) {
                    LogUtils.d(TAG, "setFirstRowFocusUpId id = " + this.mTabBarIds[index]);
                    pages[index].mChild.setNextFocusUpId(TabBarHost.VIEW_IDS[index]);
                }
                index++;
            }
        }
    }

    public void backToTop(PageManage[] pages, int len) {
        if (this.mPrePageIndex < len) {
            LogUtils.d(TAG, "backToTop mPrePageIndex = " + this.mPrePageIndex);
            pages[this.mPrePageIndex].getUIkitEngine().getPage().backToTop();
        }
    }

    public void buildPages(UIController controller) {
        LogUtils.d(TAG, "buildPages");
        PageManage[] pageTabs = controller.mPageTabs;
        this.mPageList.clear();
        int index = 0;
        if (pageTabs != null) {
            for (PageManage pageTab : pageTabs) {
                if (index >= controller.mTotalTabCount) {
                    break;
                }
                this.mPageList.add(pageTab.mChild);
                index++;
            }
            this.mPages = new ArrayList(10);
            buildPageViews(this.mPageList, this.mContext);
            for (int pageIndex = 0; pageIndex < controller.mTotalTabCount; pageIndex++) {
                if (pageTabs[pageIndex].mChild != null) {
                    this.mPages.add(pageTabs[pageIndex]);
                }
            }
            int tabIndex = this.mTabBarHost.getCurrentChildIndex();
            LogUtils.d(TAG, "default focus tab index = " + this.mDefaultTab + ",current tab host index = " + tabIndex);
            this.mPager.setCurrentItem(tabIndex);
            ((PageManage) this.mPages.get(tabIndex)).onPageIn();
            this.mPrePageIndex = tabIndex;
        }
    }

    public int getDefaultTab() {
        if (this.mTargetPage < 0 || this.mTargetPage >= this.mTabListInfo.size()) {
            return this.mDefaultTab;
        }
        return this.mTargetPage;
    }

    private void initViewPager() {
        this.mPager.setLooper(false);
        this.mPager.addOnPageChangeListener(this.mPageChangeListener);
        this.mTabBarHost.setOnTurnPageListener(this.mTurnPageListener);
        this.mTabBarHost.setOnFocusChangeListener(this.mTabHostFocusChangeListener);
        this.mSettingView.setOnFocusChangeListener(this.mTabHostFocusChangeListener);
    }

    private void buildPageViews(List<ViewGroup> pageViews, Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "build page views size = " + pageViews.size());
        }
        this.mPager.addPageViews(pageViews, this.mTabBarHost.getChildCount(), context);
    }

    public PageManage getCurrentPage() {
        if (this.mPager == null || this.mPages == null) {
            return null;
        }
        int curIndex = this.mPager.getCurrentItem();
        if (curIndex < 0 || curIndex >= this.mPages.size()) {
            return null;
        }
        return (PageManage) this.mPages.get(curIndex);
    }

    public void onActivityStart() {
    }

    public void onActivityResume() {
        if (this.mPages != null) {
            for (PageManage page : this.mPages) {
                if (page != null) {
                    page.onActivityIn();
                }
            }
        }
        this.mActionBarPresenter.startVipAnimation(false);
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this);
        ExitDialogStatusDispatcher.get().register(this);
    }

    public void onActivityPause() {
        LogUtils.d(TAG, "onActivityPause!!!");
    }

    public void onActivityStop() {
        LogUtils.d(TAG, "onActivityStop!!!");
        this.mActionBarPresenter.stopVipAnimation();
        if (this.mPages != null) {
            for (PageManage page : this.mPages) {
                if (page != null) {
                    page.onActivityOut();
                }
            }
        }
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this);
        ExitDialogStatusDispatcher.get().unregister(this);
    }

    public void onActivityDestroy() {
        ActivityLifeCycleDispatcher.get().unregister(this);
        ExitDialogStatusDispatcher.get().unregister(this);
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this);
    }

    public void onExitDialogShow() {
        if (this.mPages != null) {
            int pageIndex = this.mPager.getCurrentItem();
            if (pageIndex < this.mPages.size() && this.mPages.get(pageIndex) != null) {
                ((PageManage) this.mPages.get(pageIndex)).onExitDialogShow();
            }
        }
    }

    public void onExitDialogDismiss() {
        if (this.mPages != null) {
            int pageIndex = this.mPager.getCurrentItem();
            if (pageIndex < this.mPages.size() && this.mPages.get(pageIndex) != null) {
                ((PageManage) this.mPages.get(pageIndex)).onExitDialogDismiss();
            }
        }
    }

    public void onStart() {
        if (this.mPages != null) {
            int pageIndex = this.mPager.getCurrentItem();
            if (pageIndex < this.mPages.size() && this.mPages.get(pageIndex) != null) {
                ((PageManage) this.mPages.get(pageIndex)).onScreenSaverShow();
            }
        }
    }

    public void onStop() {
        if (this.mPages != null) {
            int pageIndex = this.mPager.getCurrentItem();
            if (pageIndex < this.mPages.size() && this.mPages.get(pageIndex) != null) {
                ((PageManage) this.mPages.get(pageIndex)).onScreenSaverDismiss();
            }
        }
    }

    private void setActionBarNextFocusDownId(int id) {
        this.mActionBarPresenter.setNextFocusDownId(id);
    }

    public void setActionBarNextFocusDownId() {
        setActionBarNextFocusDownId(this.mTabBarHost.getChildViewAt(this.mTabBarHost.getCurrentChildIndex()).getId());
    }
}
