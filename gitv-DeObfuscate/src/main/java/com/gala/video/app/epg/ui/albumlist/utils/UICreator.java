package com.gala.video.app.epg.ui.albumlist.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.BitmapUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.IUICreator.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.utils.ResourceUtil;

public class UICreator extends Wrapper {
    private static final String TAG = "EPG/album4/UICreator";

    public Bitmap maketNoResultView(Context context, GlobalQRFeedbackPanel noResultPanel, ErrorKind kind, ApiException e) {
        LogUtils.m1568d(TAG, "EPG/album4/UICreator---kind=" + kind + "---e=" + e);
        if (context == null || noResultPanel == null) {
            Log.e(TAG, "EPG/album4/UICreator---context=" + context + "---noResultPanel=" + noResultPanel);
            return null;
        }
        noResultPanel.setVisibility(0);
        if (e != null || kind == ErrorKind.SHOW_QR) {
            return createQR(context, noResultPanel, kind, e);
        }
        return createText(context, noResultPanel, kind);
    }

    private static Bitmap createText(Context context, GlobalQRFeedbackPanel noResultPanel, ErrorKind kind) {
        int txtResId;
        switch (kind) {
            case NO_FAVOURITE_RESULT:
                return createNoResultImage(context, noResultPanel, kind);
            case NET_ERROR:
                txtResId = C0508R.string.hisense_album_net_timeout;
                break;
            case NO_RESULT_AND_NO_MENU:
                txtResId = C0508R.string.search_result_nothing;
                break;
            case NO_PLAYHISTORY:
                txtResId = C0508R.string.temporary_no_play_history;
                break;
            case NO_PLAYHISTORY1:
                txtResId = C0508R.string.temporary_no_play_history_long;
                break;
            case ACCOUNT_ERROR:
                txtResId = C0508R.string.account_verify_fail;
                break;
            case NO_RESULT:
                txtResId = C0508R.string.album_no_content;
                break;
            default:
                txtResId = C0508R.string.album_no_content;
                break;
        }
        return createNoResultText(context, noResultPanel, context.getResources().getString(txtResId), kind);
    }

    private static Bitmap createQR(Context context, GlobalQRFeedbackPanel noResultPanel, ErrorKind kind, ApiException e) {
        try {
            FeedBackModel model = CreateInterfaceTools.createFeedbackFactory().createFeedBack(e);
            String errorContent = model.getErrorMsg();
            if (model.isShowQR()) {
                noResultPanel.setQRText(errorContent);
                noResultPanel.setQRExcetion(e);
                if (LogUtils.mIsDebug) {
                    Log.d(TAG, "EPG/album4/UICreator---model.isShowQR()=" + model.isShowQR() + "---content=" + errorContent);
                }
                return null;
            }
            if (LogUtils.mIsDebug) {
                Log.d(TAG, "EPG/album4/UICreator---model.isShowQR()=" + model.isShowQR());
            }
            return createNoResultText(context, noResultPanel, errorContent, kind);
        } catch (Exception exception) {
            Log.e(TAG, "EPG/album4/UICreator---try catch exception=" + exception.getMessage());
            return createNoResultText(context, noResultPanel, context.getResources().getString(C0508R.string.album_no_content), kind);
        }
    }

    private static Bitmap createNoResultImage(Context context, GlobalQRFeedbackPanel noResultPanel, ErrorKind kind) {
        switch (kind) {
            case NO_FAVOURITE_RESULT:
                int noResultImageId = C0508R.drawable.epg_no_favorite_record_alter_image;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(0);
                LayoutParams param = new LayoutParams(-2, -2);
                ImageView img = new ImageView(context);
                Bitmap bitmap = BitmapUtils.getBitmap4BigPicture(context, noResultImageId);
                img.setImageBitmap(bitmap);
                img.setScaleType(ScaleType.FIT_XY);
                param.width = ResourceUtil.getDimen(C0508R.dimen.dimen_907dp);
                param.height = ResourceUtil.getDimen(C0508R.dimen.dimen_384dp);
                param.topMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_100dp);
                layout.addView(img, param);
                noResultPanel.addMessageView(layout);
                return bitmap;
            default:
                return null;
        }
    }

    private static Bitmap createNoResultText(Context context, GlobalQRFeedbackPanel panel, String warnText, ErrorKind kind) {
        LinearLayout layout = new LinearLayout(context);
        LayoutParams params1 = new LayoutParams(-2, -2);
        LayoutParams params2 = new LayoutParams(-2, -2);
        params2.leftMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_3dp);
        params1.gravity = 16;
        ImageView img = new ImageView(context);
        layout.addView(img, params1);
        TextView tv = new TextView(context);
        tv.setText(warnText);
        tv.setGravity(1);
        layout.addView(tv, params2);
        tv.setTextColor(ResourceUtil.getColor(C0508R.color.albumview_yellow_color));
        tv.setTextSize(0, (float) ResourceUtil.getDimen(C0508R.dimen.dimen_32dp));
        img.setImageResource(C0508R.drawable.epg_no_album_text_warn);
        layout.setGravity(17);
        layout.setOrientation(0);
        panel.addMessageView(layout);
        return null;
    }

    public boolean isViewVisible(View view) {
        return view != null && view.getVisibility() == 0;
    }
}
