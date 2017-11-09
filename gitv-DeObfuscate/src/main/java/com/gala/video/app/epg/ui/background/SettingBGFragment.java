package com.gala.video.app.epg.ui.background;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.widget.HorizontalGridView;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.QBaseFragment;
import com.gala.video.app.epg.ui.background.SettingBGContract.Presenter;
import com.gala.video.app.epg.ui.background.SettingBGContract.View;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper.BackgroundType;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class SettingBGFragment extends QBaseFragment implements View, OnItemClickListener {
    private static final String TAG = "EPG/SettingBGFragment";
    private BgSettingGridAdapter mAdapter;
    private Presenter mBgSettingPresenter;
    private HorizontalGridView mGridView;
    private OnItemFocusChangedListener mOnItemFocusChangedListener = new C08851();

    class C08851 implements OnItemFocusChangedListener {
        C08851() {
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            holder.itemView.bringToFront();
            AnimationUtil.zoomAnimation(holder.itemView, hasFocus, 1.1f, 200, true);
        }
    }

    public static SettingBGFragment newInstance() {
        return new SettingBGFragment();
    }

    public void setPresenter(Presenter presenter) {
        this.mBgSettingPresenter = (Presenter) ActivityUtils.checkNotNull(presenter);
    }

    public void onResume() {
        super.onResume();
        this.mBgSettingPresenter.start();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(C0508R.layout.epg_fragment_bg_setting, container, false);
        this.mGridView = (HorizontalGridView) root.findViewById(C0508R.id.epg_grid_skin_mode_id);
        this.mAdapter = new BgSettingGridAdapter(AppRuntimeEnv.get().getApplicationContext());
        set(this.mGridView, this.mAdapter, (int) getResources().getDimension(C0508R.dimen.dimen_310dp), (int) getResources().getDimension(C0508R.dimen.dimen_230dp));
        this.mGridView.setOnItemClickListener(this);
        return root;
    }

    private void set(HorizontalGridView grid, BgSettingGridAdapter adapter, int width, int height) {
        int ninePatchBorder = CloudUtils.calcNinePatchBorder(ResourceUtil.getDrawable(C0508R.drawable.share_item_rect_selector));
        LayoutParams params = grid.getLayoutParams();
        params.width = -1;
        params.height = ResourceUtil.getDimen(C0508R.dimen.dimen_134dp) + (ninePatchBorder * 2);
        grid.setHorizontalMargin(getDimen(C0508R.dimen.dimen_27dp) - (ninePatchBorder * 2));
        grid.setFocusMode(1);
        grid.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        grid.setAdapter(adapter);
        grid.setContentWidth(ResourceUtil.getDimen(C0508R.dimen.dimen_236dp) + (ninePatchBorder * 2));
        grid.setContentHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_134dp) + (ninePatchBorder * 2));
        grid.setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean isActive() {
        return isAdded();
    }

    private static int getDimen(int id) {
        return ResourceUtil.getDimen(id);
    }

    public void setBgList(List<String> nightList) {
        this.mAdapter.setList(nightList);
    }

    public void updateBgList(List<String> nightList) {
        this.mAdapter.updateList(nightList);
    }

    public void onItemClick(ViewGroup parent, ViewHolder vh) {
        LogUtils.m1568d(TAG, "onItemClick ---- parent.getId()= " + parent.getId());
        String smallPath = "";
        int position = vh.getLayoutPosition();
        int columns = position + 1;
        if (parent.getId() == C0508R.id.epg_grid_skin_mode_id) {
            this.mAdapter.setSelectedPosition(position);
            smallPath = (String) this.mAdapter.getList().get(position);
        }
        String rseat = 1 + "_" + columns;
        LogUtils.m1568d(TAG, ">>>>> pageClick pingback rseat：" + rseat);
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "20").add("rseat", rseat).add("rpage", "wallpaper").add("block", "wallpaper").add("rt", "i");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        this.mAdapter.notifyDataSetUpdate();
        LogUtils.m1568d(TAG, "onItemClick ---- smallPath= " + smallPath);
        GetInterfaceTools.getIBackgroundManager().setBackgroundDrawable(getSettingBgDrawableName(smallPath));
        ((SettingBGActivity) getActivity()).setBackground(((SettingBGActivity) getActivity()).getBackgroundContainer());
    }

    private String getSettingBgDrawableName(String rootpath) {
        String dayThumbPath = GetInterfaceTools.getIThemeZipHelper().getBackground(BackgroundType.DAY_THUMB);
        String nightThumbPath = GetInterfaceTools.getIThemeZipHelper().getBackground(BackgroundType.NIGHT_THUMB);
        LogUtils.m1568d(TAG, "getSettingBgDrawableName---ThemeZipHelper---dayThumbPath = " + dayThumbPath + " ;nightThumbPath =  " + nightThumbPath);
        String drawableName = rootpath.replace(dayThumbPath, "").replace(nightThumbPath, "");
        LogUtils.m1568d(TAG, "getSettingBgDrawableName ---- rootpath= " + rootpath + " ;drawableName = " + drawableName);
        return drawableName;
    }

    public void setSelectedPostion() {
        String selectedDrawableName = SystemConfigPreference.getNightModeBackground(getActivity());
        List<String> nightPathList = this.mAdapter.getList();
        int selectPostion = -1;
        if (nightPathList != null && !nightPathList.isEmpty()) {
            for (int i = 0; i < nightPathList.size(); i++) {
                if (((String) nightPathList.get(i)).contains(selectedDrawableName)) {
                    selectPostion = i;
                    break;
                }
            }
        }
        LogUtils.m1568d(TAG, "selectPostion = " + selectPostion);
        this.mAdapter.setSelectedPosition(selectPostion);
    }
}
