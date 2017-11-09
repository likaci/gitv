package com.gala.video.app.epg.ui.imsg.mvpd;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.QBaseFragment;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.ui.imsg.mvpd.MsgDetailContract.Presenter;
import com.gala.video.app.epg.ui.imsg.mvpd.MsgDetailContract.View;
import com.gala.video.app.epg.ui.imsg.utils.MsgClickUtil;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.proguard.Keep;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.ImageCacheUtil;

@Keep
public class MsgDetailFragment extends QBaseFragment implements View {
    private static String TAG = "imsg/MsgDetailFragment";
    private Button mButton;
    private TextView mDescriptionAlignmentTextView;
    private TextView mDescriptionTextView;
    private ImageView mImageView;
    IMsgContent mImsgContent;
    private Presenter mMsgDetailPresenter;
    private TextView mTitleView;

    class C09001 implements OnClickListener {
        C09001() {
        }

        public void onClick(android.view.View v) {
            LogUtils.m1568d(MsgDetailFragment.TAG, "onClick==R.id.msgdetail_button");
            MsgClickUtil.jumpTo(MsgDetailFragment.this.getActivity(), MsgDetailFragment.this.mImsgContent, true);
            MsgDetailFragment.this.sendClickPingBack(true);
        }
    }

    public void setPresenter(Presenter presenter) {
        this.mMsgDetailPresenter = (Presenter) ActivityUtils.checkNotNull(presenter);
    }

    public boolean isActive() {
        return isAdded();
    }

    public static MsgDetailFragment newInstance() {
        return new MsgDetailFragment();
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(C0508R.layout.epg_fragment_msg_center_detail, container, false);
        initViews(root);
        this.mImsgContent = (IMsgContent) getActivity().getIntent().getExtras().get("content");
        CharSequence title = this.mImsgContent.msg_title;
        String deString = this.mImsgContent.description;
        String pic_url = this.mImsgContent.pic_url;
        CharSequence button_name = this.mImsgContent.button_name;
        LogUtils.m1568d(TAG, "title = " + title + " ; deString  = " + deString + " ; pic_url = " + pic_url + " ; button_name = " + button_name + " ; imsgContent = " + this.mImsgContent);
        if (!(this.mTitleView == null || StringUtils.isEmpty(title))) {
            this.mTitleView.setText(title);
        }
        handleDescription(deString);
        if (!(this.mButton == null || StringUtils.isEmpty(button_name))) {
            this.mButton.setText(button_name);
        }
        this.mImageView.setImageDrawable(ImageCacheUtil.DEFAULT_DRAWABLE);
        this.mMsgDetailPresenter.onLoadImage(pic_url);
        sendShowPingBack();
        return root;
    }

    private void initViews(android.view.View root) {
        this.mTitleView = (TextView) root.findViewById(C0508R.id.epg_msgdetail_title);
        this.mDescriptionAlignmentTextView = (TextView) root.findViewById(C0508R.id.epg_msgdetail_desc_AlignmentTextView);
        this.mDescriptionTextView = (TextView) root.findViewById(C0508R.id.epg_msgdetail_desc_TextView);
        this.mButton = (Button) root.findViewById(C0508R.id.epg_msgdetail_button);
        this.mImageView = (ImageView) root.findViewById(C0508R.id.epg_msgdetail_image);
        this.mButton.setOnClickListener(new C09001());
    }

    private void handleDescription(String deString) {
        if (this.mDescriptionAlignmentTextView != null && this.mDescriptionTextView != null) {
            if (StringUtils.isEmpty((CharSequence) deString)) {
                this.mDescriptionTextView.setVisibility(8);
                this.mDescriptionAlignmentTextView.setVisibility(8);
                return;
            }
            float textSize = this.mDescriptionTextView.getTextSize();
            Paint paint = new Paint();
            paint.setTextSize(textSize);
            if (paint.measureText(deString) < paint.measureText("中") * 38.0f) {
                this.mDescriptionTextView.setText(deString);
                this.mDescriptionTextView.setVisibility(0);
                this.mDescriptionAlignmentTextView.setVisibility(8);
                return;
            }
            this.mDescriptionAlignmentTextView.setText(deString);
            this.mDescriptionAlignmentTextView.setVisibility(0);
            this.mDescriptionTextView.setVisibility(8);
        }
    }

    public void sendClickPingBack(boolean isClick) {
        PingBackParams params = new PingBackParams();
        params.add("r", this.mImsgContent.msg_id + "").add("block", ISearchConstant.TVSRCHSOURCE_MSG).add("rt", "i").add("rseat", isClick ? ScreenSaverPingBack.SEAT_KEY_OK : "back").add("rpage", ISearchConstant.TVSRCHSOURCE_MSG).add(Keys.JUMP_TYPE, handlejumpType(this.mImsgContent.page_jumping)).add(Keys.ISREAD, this.mImsgContent.isRead ? "1" : "0");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public void sendShowPingBack() {
        PingBackParams params = new PingBackParams();
        params.add("qtcurl", ISearchConstant.TVSRCHSOURCE_MSG).add("qpid", this.mImsgContent.msg_id + "").add("block", ISearchConstant.TVSRCHSOURCE_MSG).add(Keys.f2035T, "21").add(Keys.JUMP_TYPE, handlejumpType(this.mImsgContent.page_jumping)).add(Keys.ISREAD, this.mImsgContent.isRead ? "1" : "0");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    String handlejumpType(int type) {
        switch (type) {
            case 1:
                return "H5页";
            case 2:
                return "专题页";
            case 3:
                return "详情页";
            case 4:
                return "播放页";
            case 5:
                return "其他";
            default:
                return "";
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void showImage(Bitmap bitmap) {
        if (this.mImageView != null && bitmap != null) {
            this.mImageView.setImageBitmap(bitmap);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mMsgDetailPresenter.onDestroy();
    }
}
