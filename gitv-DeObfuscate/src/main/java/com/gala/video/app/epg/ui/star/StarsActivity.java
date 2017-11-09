package com.gala.video.app.epg.ui.star;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.gala.albumprovider.model.Tag;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel;
import com.gala.video.app.epg.ui.star.domain.StarsDataSource;
import com.gala.video.app.epg.ui.star.model.StarsInfoModel;
import com.gala.video.app.epg.ui.star.presenter.StarsContract.Presenter;
import com.gala.video.app.epg.ui.star.presenter.StarsContract.View;
import com.gala.video.app.epg.ui.star.presenter.StarsPresenter;
import com.gala.video.app.epg.ui.star.widget.StarErrorView;
import com.gala.video.app.epg.ui.star.widget.StarVerticalGridView;
import com.gala.video.app.epg.ui.star.widget.StarsFullView;
import com.gala.video.app.epg.ui.star.widget.StarsFullView.OnFullDescClickedListener;
import com.gala.video.app.epg.ui.star.widget.StarsTopView;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import java.util.List;
import java.util.Map;

public class StarsActivity extends QMultiScreenActivity implements View {
    private StarsInfoModel mInfoModel;
    private android.view.View mRootView;
    private StarErrorView mStarErrorView;
    private StarVerticalGridView mStarVerticalGridView;
    private StarsFullView mStarsFullView;
    private Presenter mStarsPresenter;
    private StarsTopView mStarsTopView;

    class C10801 implements OnFullDescClickedListener {
        C10801() {
        }

        public void onFullDescClicked() {
            StarsActivity.this.hideFullView();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopImageProvider();
        setContentView(C0508R.layout.epg_activity_stars);
        this.mStarVerticalGridView = (StarVerticalGridView) findViewById(C0508R.id.epg_stars_gridview_id);
        this.mRootView = getRootView();
        Intent intent = getIntent();
        AlbumIntentModel intentModel = null;
        if (intent != null) {
            try {
                intentModel = (AlbumIntentModel) intent.getSerializableExtra("intent_model");
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
            this.mInfoModel = new StarsInfoModel(intentModel);
            initGridView();
            return;
        }
        throw new IllegalArgumentException("StarsActivity.onCreate(), need a INTENT_MODEL");
    }

    public void setPresenter(Presenter presenter) {
        this.mStarsPresenter = (Presenter) ActivityUtils.checkNotNull(presenter);
    }

    private android.view.View getRootView() {
        if (this.mRootView == null) {
            this.mRootView = getWindow().getDecorView().findViewById(16908290);
        }
        return this.mRootView;
    }

    protected android.view.View getBackgroundContainer() {
        return getRootView();
    }

    private void stopImageProvider() {
        ImageProviderApi.getImageProvider().stopAllTasks();
    }

    public void onResume() {
        super.onResume();
        this.mStarsPresenter.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mStarsPresenter.onPause();
    }

    private void initGridView() {
        this.mStarVerticalGridView.init(this, this.mInfoModel);
        StarsPresenter starsPresenter = new StarsPresenter(StarsDataSource.getInstance(), this, this.mInfoModel);
        this.mStarsPresenter.start();
        this.mStarVerticalGridView.setOnTextClickedListener();
    }

    public void showDatas(Map<String, List<IData>> map, List<Tag> list) {
        this.mStarVerticalGridView.showDatas(map, list);
    }

    public void showProgressBar() {
        getStarErrorView().showProgressBar();
    }

    public void showNoResultPanel(ErrorKind kind, ApiException e) {
        this.mStarVerticalGridView.setFocusable(false);
        GetInterfaceTools.getUICreator().maketNoResultView(AppRuntimeEnv.get().getApplicationContext(), getStarErrorView().getNoResultPanel(), kind, e);
        getStarErrorView().showNoResultPanel();
    }

    public void showHasResultPanel() {
        getStarErrorView().showHasResultPanel();
    }

    public void showTopView(boolean isShow) {
        getStarsTopView().showTopView(isShow);
    }

    public void showFullView(int realcount) {
        getFullView().show(realcount);
    }

    private void hideFullView() {
        getFullView().hide();
    }

    private StarsTopView getStarsTopView() {
        if (this.mStarsTopView == null) {
            this.mStarsTopView = new StarsTopView(this.mRootView, this.mInfoModel);
        }
        return this.mStarsTopView;
    }

    private StarErrorView getStarErrorView() {
        if (this.mStarErrorView == null) {
            this.mStarErrorView = new StarErrorView(this.mRootView);
        }
        return this.mStarErrorView;
    }

    private synchronized StarsFullView getFullView() {
        if (this.mStarsFullView == null) {
            this.mStarsFullView = new StarsFullView(this.mRootView);
            this.mStarsFullView.setOnFullDescClickedListener(new C10801());
        }
        this.mStarsFullView.setStar(this.mStarVerticalGridView.getStar());
        return this.mStarsFullView;
    }

    public void setDetails(Star star) {
        this.mStarVerticalGridView.setDetails(star);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        if (getFullView().handleKeyEvent(event)) {
            return true;
        }
        int keyCode = event.getKeyCode();
        if (keyCode == 4 || keyCode == 111) {
            stopImageProvider();
        }
        return super.handleKeyEvent(event);
    }
}
