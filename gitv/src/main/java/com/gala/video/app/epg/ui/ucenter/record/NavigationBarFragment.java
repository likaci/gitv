package com.gala.video.app.epg.ui.ucenter.record;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.ListView;
import com.gala.video.albumlist4.widget.ListView.ItemDivider;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ItemDecoration;
import com.gala.video.albumlist4.widget.RecyclerView.OnFocusLostListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.widget.adapter.FootAlbumAdapter;
import com.gala.video.app.epg.ui.albumlist.widget.adapter.LabelAlbumAdapter;
import com.gala.video.app.epg.ui.ucenter.record.contract.NavigationBarContract.Presenter;
import com.gala.video.app.epg.ui.ucenter.record.contract.NavigationBarContract.View;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class NavigationBarFragment extends Fragment implements View, OnItemFocusChangedListener {
    private static final int ANIMATION_DURATION = 100;
    private static final float ANIMATION_SCALE = 1.05f;
    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int REFRESH_DELAY = 350;
    private LabelAlbumAdapter mAdapter;
    private FootLeftRefreshPage mDefaultPage;
    private MyHandler mHandler;
    private LabelChangedListener mLabelChangedListener;
    private ListView mListView;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnFocusLostListener mOnFocusLostListener = new OnFocusLostListener() {
        public void onFocusLost(ViewGroup parent, ViewHolder holder) {
            NavigationBarFragment.this.mHandler.removeMessages(0);
            if (NavigationBarFragment.this.mAdapter.getSelect() != -1) {
                NavigationBarFragment.this.mListView.setFocusPosition(NavigationBarFragment.this.mAdapter.getSelect());
            }
        }
    };
    private FootLeftRefreshPage mPage = FootLeftRefreshPage.NONE;
    private Presenter mPresenter;
    protected boolean mRefreshImmediately = true;
    private List<Tag> mTagList = new ArrayList();

    public class GridItemDecoration extends ItemDecoration {
        public int getItemOffsets(int position, RecyclerView parent) {
            return NavigationBarFragment.this.isLevel(position) ? ResourceUtil.getDimen(R.dimen.dimen_1dp) : ResourceUtil.getDimen(R.dimen.dimen_8dp);
        }
    }

    public class GridItemDivider extends ItemDivider {
        public Drawable getItemDivider(int position, RecyclerView parent) {
            return NavigationBarFragment.this.isLevel(position) ? null : ResourceUtil.getDrawable(R.drawable.epg_album_label_line);
        }
    }

    public interface LabelChangedListener {
        void onLabelChanged(FootLeftRefreshPage footLeftRefreshPage);
    }

    private static class MyHandler extends Handler {
        private WeakReference<NavigationBarFragment> mFragment;

        public MyHandler(NavigationBarFragment fragment) {
            this.mFragment = new WeakReference(fragment);
        }

        public void handleMessage(Message msg) {
            FootLeftRefreshPage page = IFootEnum.valueOf(msg.obj.getName());
            ((NavigationBarFragment) this.mFragment.get()).mAdapter.setSelectDefault();
            if (!(((NavigationBarFragment) this.mFragment.get()).mLabelChangedListener == null || ((NavigationBarFragment) this.mFragment.get()).mPage == page)) {
                ((NavigationBarFragment) this.mFragment.get()).mLabelChangedListener.onLabelChanged(page);
            }
            ((NavigationBarFragment) this.mFragment.get()).mPage = page;
        }
    }

    @SuppressLint({"InflateParams"})
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View rootView = inflater.inflate(R.layout.epg_fragment_navigation_bar, null);
        this.mListView = (ListView) rootView.findViewById(R.id.epg_list_view);
        initListView();
        this.mHandler = new MyHandler(this);
        return rootView;
    }

    public void onResume() {
        super.onResume();
        this.mPresenter.start();
    }

    public void setBarLists(List<Tag> list) {
        this.mTagList.clear();
        this.mTagList.addAll(list);
    }

    private void initListView() {
        this.mListView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mListView.setFocusMode(1);
        this.mListView.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mListView.setFocusLeaveForbidden(Service.CISCO_FNA);
        this.mListView.setItemDivider(new GridItemDivider());
        this.mListView.setItemDecoration(new GridItemDecoration());
        this.mListView.setDividerWidth((int) getResources().getDimension(R.dimen.dimen_169dp));
        this.mListView.setBackgroundWidth((int) getResources().getDimension(R.dimen.dimen_218dp));
        this.mListView.setPadding(0, ResourceUtil.getPx(84), 0, 0);
        this.mListView.setShakeForbidden(83);
        this.mListView.setOnItemFocusChangedListener(this);
        this.mListView.setOnFocusLostListener(this.mOnFocusLostListener);
    }

    private boolean isLevel(int position) {
        List list = this.mAdapter.getList();
        if (ListUtils.isEmpty(list)) {
            return false;
        }
        Tag tag = (Tag) list.get(position);
        Tag tagNext = (Tag) list.get(position + 1);
        if (tag == null || tagNext == null) {
            return false;
        }
        if (tag.getLevel() == 1 || tagNext.getLevel() == 1) {
            return true;
        }
        return false;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mListView.setOnItemClickListener(l);
    }

    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void updateNavigationBarItem(AlbumInfoModel infoModel) {
        if (this.mAdapter == null) {
            this.mAdapter = new FootAlbumAdapter(getActivity(), this.mTagList, infoModel);
            this.mAdapter.setSelect(getIndex(this.mDefaultPage.valueName()));
            this.mListView.setAdapter(this.mAdapter);
            this.mListView.requestFocus();
        }
    }

    public void setLabelChangedListener(LabelChangedListener l) {
        this.mLabelChangedListener = l;
    }

    public void requestFocus() {
        this.mListView.requestFocus();
    }

    public void setFocusLeaveForbidden(int direction) {
        this.mListView.setFocusLeaveForbidden(direction);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.mOnFocusChangeListener = l;
    }

    public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
        this.mHandler.removeMessages(0);
        int position = holder.getLayoutPosition();
        if (hasFocus) {
            Message msg = this.mHandler.obtainMessage(0);
            msg.obj = this.mTagList.get(position);
            this.mHandler.sendMessageDelayed(msg, this.mRefreshImmediately ? 0 : 350);
            this.mRefreshImmediately = false;
            if (this.mAdapter.getSelect() == position) {
                this.mAdapter.setSelectDefault();
            }
        } else {
            this.mAdapter.setSelect(this.mPage != FootLeftRefreshPage.NONE ? getIndex(this.mPage.valueName()) : getIndex(this.mDefaultPage.valueName()));
            this.mAdapter.notifyDataSetUpdate();
        }
        AnimationUtil.zoomLeftAnimation(holder.itemView, hasFocus, ANIMATION_SCALE, 100);
        if (this.mOnFocusChangeListener != null) {
            this.mOnFocusChangeListener.onFocusChange(parent, hasFocus);
        }
    }

    public FootLeftRefreshPage getPage() {
        return this.mPage == FootLeftRefreshPage.NONE ? this.mDefaultPage : this.mPage;
    }

    public void setFocusPosition(int position) {
        this.mAdapter.setSelectDefault();
        this.mListView.setFocusPosition(position);
    }

    public void setDefaultPage(FootLeftRefreshPage page) {
        this.mDefaultPage = page;
        this.mListView.setFocusPosition(getIndex(this.mDefaultPage.valueName()));
    }

    private int getIndex(String name) {
        if (this.mTagList != null) {
            for (int i = 0; i < this.mTagList.size(); i++) {
                if (((Tag) this.mTagList.get(i)).getName().equals(name)) {
                    return i;
                }
            }
        }
        return 1;
    }
}
