package com.gala.video.app.player.albumdetail.ui.overlay.panels;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumInfo.VideoKind;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class BasicInfoPanel {
    private static final String TAG = "AlbumDetail/UI/BasicInfoPanel";
    private AlbumInfo mAlbumInfo;
    private OnBasicDescVisibilityListener mBasicDescVisibilityListener;
    private IAlbumDetailUiConfig mConfig;
    private Context mContext;
    private int mDescRealCount = 0;
    private RelativeLayout mRlDetailRoot;
    private View mRootView;
    private TextView mTxtAlbumName;
    private TextView mTxtDetail;
    private TextView mTxtLine1;
    private TextView mTxtLine2;
    private TextView mTxtLine3;
    private TextView mTxtLine4;

    public interface OnBasicDescVisibilityListener {
        void OnDescVisibility(int i);
    }

    public BasicInfoPanel(Context context, View rootView, IAlbumDetailUiConfig config) {
        this.mRootView = rootView;
        this.mContext = context;
        this.mConfig = config;
        initViews();
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
    }

    public void onActivityFinishing() {
        this.mAlbumInfo = null;
    }

    public void setOnBasicDescVisibilityListener(OnBasicDescVisibilityListener basicDescVisibilityListener) {
        if (basicDescVisibilityListener != null) {
            this.mBasicDescVisibilityListener = basicDescVisibilityListener;
        }
    }

    private void onDescVisibility(int visibility) {
        if (this.mBasicDescVisibilityListener != null) {
            this.mBasicDescVisibilityListener.OnDescVisibility(visibility);
        }
    }

    private void initViews() {
        this.mTxtAlbumName = (TextView) this.mRootView.findViewById(R.id.share_detail_txt_album_title);
        this.mTxtAlbumName.setTextSize(0, (float) getDimensionPixelSize(R.dimen.dimen_30dp));
        this.mTxtLine1 = (TextView) this.mRootView.findViewById(R.id.share_detail_txt_line1);
        this.mTxtLine1.setTextSize(0, (float) getDimensionPixelSize(R.dimen.dimen_17dp));
        this.mTxtLine2 = (TextView) this.mRootView.findViewById(R.id.share_detail_txt_line2);
        this.mTxtLine2.setTextSize(0, (float) getDimensionPixelSize(R.dimen.dimen_20dp));
        this.mTxtLine3 = (TextView) this.mRootView.findViewById(R.id.share_detail_txt_line3);
        this.mTxtLine3.setTextSize(0, (float) getDimensionPixelSize(R.dimen.dimen_20dp));
        this.mTxtLine4 = (TextView) this.mRootView.findViewById(R.id.share_detail_txt_line4);
        this.mTxtLine4.setTextSize(0, (float) getDimensionPixelSize(R.dimen.dimen_20dp));
        this.mTxtDetail = (TextView) this.mRootView.findViewById(R.id.share_detail_txt_album_info);
        this.mRlDetailRoot = (RelativeLayout) this.mRootView.findViewById(R.id.share_detail_rl_album_info);
        this.mTxtDetail.setTextSize(0, (float) getDimensionPixelSize(R.dimen.dimen_20dp));
    }

    private int getDimensionPixelSize(int size) {
        return ResourceUtil.getDimensionPixelSize(size);
    }

    public int getDetailDescRealCount() {
        return this.mDescRealCount;
    }

    public void showOrUpdateBasicInfo() {
        if (this.mAlbumInfo != null && this.mAlbumInfo.getAlbum() != null) {
            LogRecordUtils.logd(TAG, "showOrUpdateBasicInfo: " + this.mAlbumInfo);
            AlbumInfo albumInfo = this.mAlbumInfo;
            this.mTxtAlbumName.setText(this.mAlbumInfo.getAlbumSubName());
            LogRecordUtils.logd(TAG, ">> showOrUpdateBasicInfo: descLength " + PlayerAppConfig.getAlbumDesc(this.mAlbumInfo).length());
            VideoKind kind = this.mAlbumInfo.getKind();
            LogRecordUtils.logd(TAG, "showOrUpdateBasicInfo(): kind -> " + kind);
            if (kind == VideoKind.ALBUM_EPISODE || kind == VideoKind.VIDEO_EPISODE) {
                setDetailInfoLine(this.mTxtLine1, AlbumTextHelper.getEpisodeMessage(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine2, AlbumTextHelper.getDirector(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine3, AlbumTextHelper.getPerformer(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine4, "");
                checkAutoTextLength();
            } else if (kind == VideoKind.ALBUM_SOURCE || kind == VideoKind.VIDEO_SOURCE) {
                setDetailInfoLine(this.mTxtLine1, AlbumTextHelper.getVarietyMessage(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine2, AlbumTextHelper.getHost(albumInfo.getAlbum(), this.mContext));
                setDetailInfoLine(this.mTxtLine3, AlbumTextHelper.getGuest(albumInfo.getAlbum(), this.mContext));
                setDetailInfoLine(this.mTxtLine4, "");
                checkAutoTextLength();
            } else {
                setDetailInfoLine(this.mTxtLine1, AlbumTextHelper.getFilmMessage(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine2, AlbumTextHelper.getDirector(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine3, AlbumTextHelper.getPerformer(albumInfo, this.mContext));
                setDetailInfoLine(this.mTxtLine4, "");
                checkAutoTextLength();
            }
            setTxtDetail(albumInfo);
        }
    }

    private void checkAutoTextLength() {
        int count = 0;
        if (!TextUtils.isEmpty(this.mTxtLine1.getText()) && this.mTxtLine1.getVisibility() == 0) {
            count = 0 + 1;
        }
        if (!TextUtils.isEmpty(this.mTxtLine2.getText()) && this.mTxtLine2.getVisibility() == 0) {
            count++;
        }
        if (!TextUtils.isEmpty(this.mTxtLine3.getText()) && this.mTxtLine3.getVisibility() == 0) {
            count++;
        }
        if (!TextUtils.isEmpty(this.mTxtLine4.getText()) && this.mTxtLine4.getVisibility() == 0) {
            count++;
        }
        LogRecordUtils.logd(TAG, "checkAutoTextLength: count " + count);
    }

    private void setTxtDetail(AlbumInfo albumInfo) {
        CharSequence desc = PlayerAppConfig.getAlbumDesc(albumInfo);
        LogRecordUtils.logd(TAG, "setTxtDetail(): desc -> " + PlayerAppConfig.getAlbumDesc(albumInfo));
        if (StringUtils.isEmpty(desc)) {
            this.mRlDetailRoot.setVisibility(8);
            this.mTxtDetail.setVisibility(8);
            onDescVisibility(8);
            return;
        }
        this.mRlDetailRoot.setVisibility(0);
        this.mTxtDetail.setVisibility(0);
        StringBuffer sb = new StringBuffer(ResourceUtil.getStr(R.string.detail_album_info_desc));
        sb.append(desc);
        this.mTxtDetail.setText(sb.toString());
        onDescVisibility(0);
    }

    private void setDetailInfoLine(TextView txt_line1, CharSequence line1) {
        if (StringUtils.isEmpty(line1)) {
            txt_line1.setVisibility(8);
            return;
        }
        txt_line1.setVisibility(0);
        txt_line1.setText(line1);
    }
}
