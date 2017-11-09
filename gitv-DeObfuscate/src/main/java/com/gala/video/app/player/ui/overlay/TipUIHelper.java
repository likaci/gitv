package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.widget.RelativeLayout;
import com.gala.sdk.player.BaseAdData;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.TipType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.type.PayMarkType;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.utils.PlayerExitHelper;
import com.gala.video.app.player.utils.PlayerUIHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDataUtils;
import com.gala.video.lib.share.system.preference.AppPreference;
import java.util.Random;
import org.cybergarage.soap.SOAP;

public class TipUIHelper {
    private static final String TAG = "Player/Ui/TipUIHelper";
    private BaseAdData mAdData;
    private Context mContext;
    private IVideo mVideo;

    public TipUIHelper(Context context) {
        this.mContext = context;
    }

    public void setVideo(IVideo video) {
        this.mVideo = video;
    }

    public TipWrapper decorateTip(TipWrapper origintip) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> decorateTip origintip=" + origintip);
        }
        TipWrapper tip = null;
        switch (origintip.getTipType().getConcreteTipType()) {
            case 301:
                tip = decoratePreview(origintip);
                break;
            case 302:
            case TipType.CONCRETE_TYPE_SDR_VIP_STREAM_PREVIEW /*323*/:
                tip = decorateVipBitstream(origintip);
                break;
            case 303:
                tip = decorate3D(origintip);
                break;
            case TipType.CONCRETE_TYPE_REPLAY_PLAYNEXT /*304*/:
                tip = decorateReplay(origintip);
                break;
            case TipType.CONCRETE_TYPE_HISTORY /*305*/:
                tip = decorateHistory(origintip);
                break;
            case TipType.CONCRETE_TYPE_JUMP_HEAD /*306*/:
                tip = decorateJumHead(origintip);
                break;
            case 307:
                tip = decorateCheckTailOrder(origintip);
                break;
            case 308:
                tip = decorateSelection(origintip);
                break;
            case TipType.CONCRETE_TYPE_MIDDLE_AD_READY /*309*/:
                tip = decorateMiddleAdReady(origintip);
                break;
            case TipType.CONCRETE_TYPE_AD_END /*310*/:
                tip = decorateAdEnd(origintip);
                break;
            case TipType.CONCRETE_TYPE_AD_START /*311*/:
                tip = decorateAdStart(origintip);
                break;
            case 312:
                tip = decorateBitStreamChange(origintip);
                break;
            case TipType.CONCRETE_TYPE_CAROUSEL_AD_READY /*313*/:
                tip = decorateCarouselAdReady(origintip);
                break;
            case TipType.CONCRETE_TYPE_MIDDLE_AD_START /*314*/:
                tip = decorateMiddleAdStart(origintip);
                break;
            case TipType.CONCRETE_TYPE_VIP_BITSTREAM_CHANGE /*315*/:
                tip = decorateVIPBitStreamChange(origintip);
                break;
            case TipType.CONCRETE_TYPE_CHECK_TAIL_NAME /*316*/:
                tip = decorateCheckTailName(origintip);
                break;
            case TipType.CONCRETE_TYPE_HDR_OPEN /*317*/:
                tip = decorateHDROpen(origintip);
                break;
            case TipType.CONCRETE_TYPE_HDR_TO_SDR /*318*/:
                tip = decorateBitStreamHDRToSDRChange(origintip);
                break;
            case TipType.CONCRETE_TYPE_WILL_BECOME_HDR /*319*/:
                tip = decorateBitStreamBecomeHDR(origintip);
                break;
            case 320:
                tip = decorateSkipAd(origintip);
                break;
            case TipType.CONCRETE_TYPE_CHANGE_HDR_FAIL_TO_SDR /*321*/:
                tip = decorateHDRBitStreamFailToSDR(origintip);
                break;
            case TipType.CONCRETE_TYPE_CHANGE_4K_FAIL_TO_SDR /*322*/:
                tip = decorate4KFailToSDR(origintip);
                break;
            case TipType.CONCRETE_TYPE_QUESTIONNAIRE_START /*324*/:
                tip = decorateQuestionnaireStart(origintip);
                break;
            case TipType.CONCRETE_TYPE_LOGIN /*325*/:
                tip = decorateLogin(origintip);
                break;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< decorateTip origintip=" + tip);
        }
        return tip;
    }

    private TipWrapper decorateSkipAd(TipWrapper origintip) {
        return origintip.setContent(PlayerUIHelper.getVIPHighLightString(this.mContext.getResources().getString(C1291R.string.already_jump) + new String[]{"60", "75", "90", "105", "120"}[new Random().nextInt(5)] + this.mContext.getResources().getString(C1291R.string.second_ad)));
    }

    private TipWrapper decorateAdEnd(TipWrapper tip) {
        return tip.setContent("");
    }

    private TipWrapper decorateAdStart(TipWrapper tip) {
        String tipMsg = this.mContext.getResources().getString(C1291R.string.ad_tip);
        CharSequence adTip = PlayerUIHelper.getJumpAdTip();
        if (StringUtils.isEmpty(adTip)) {
            return null;
        }
        return tip.setContent(adTip);
    }

    private TipWrapper decorateBitStreamChange(TipWrapper tip) {
        String adtip = null;
        RelativeLayout adview = null;
        String spAd = null;
        int id = 0;
        if (this.mAdData != null) {
            adtip = this.mAdData.getAdTxt();
            adview = this.mAdData.getAdView();
            id = this.mAdData.getID();
        }
        if (!(adtip == null || adview == null)) {
            spAd = adtip + SOAP.DELIM;
        }
        BitStream toDefinition = tip.getTipExtra().getBitStream();
        SpannableString sp = PlayerUIHelper.getHighLightString(this.mContext.getResources().getString(C1291R.string.has_change_stream) + PlayerDataUtils.getBitStreamString(this.mContext, toDefinition), PlayerDataUtils.getBitStreamString(this.mContext, toDefinition));
        SpannableStringBuilder spb = new SpannableStringBuilder();
        if (spAd == null) {
            spb.append(sp);
        } else {
            spb.append(spAd).append(sp);
        }
        return tip.setContent(spb).setAdView(adview).setAdID(id);
    }

    private TipWrapper decorateBitStreamHDRToSDRChange(TipWrapper tip) {
        BitStream toDefinition = tip.getTipExtra().getBitStream();
        return tip.setContent(PlayerUIHelper.getHighLightString(String.format(this.mContext.getResources().getString(C1291R.string.hdr_to_sdr_stream1), new Object[]{PlayerDataUtils.getBitStreamString(this.mContext, toDefinition)}), PlayerDataUtils.getBitStreamString(this.mContext, toDefinition)));
    }

    private TipWrapper decorateHDRBitStreamFailToSDR(TipWrapper tip) {
        return tip.setContent(this.mContext.getResources().getString(C1291R.string.hdr_change_error));
    }

    private TipWrapper decorate4KFailToSDR(TipWrapper tip) {
        BitStream toDefinition = tip.getTipExtra().getBitStream();
        return tip.setContent(PlayerUIHelper.getHighLightString(String.format(this.mContext.getResources().getString(C1291R.string.definition4k_to_sdr_stream), new Object[]{PlayerDataUtils.getBitStreamString(this.mContext, toDefinition)}), PlayerDataUtils.getBitStreamString(this.mContext, toDefinition)));
    }

    private TipWrapper decorateBitStreamBecomeHDR(TipWrapper tip) {
        BitStream toDefinition = tip.getTipExtra().getBitStream();
        return tip.setContent(PlayerUIHelper.getVIPHighLightString(String.format(this.mContext.getResources().getString(C1291R.string.will_become_hdr_stream), new Object[]{PlayerDataUtils.getBitStreamString(this.mContext, toDefinition)})));
    }

    private TipWrapper decorateVIPBitStreamChange(TipWrapper tip) {
        return tip.setContent(PlayerUIHelper.getVIPHighLightString(this.mContext.getResources().getString(C1291R.string.has_change_vip_stream) + PlayerDataUtils.getBitStreamString(this.mContext, tip.getTipExtra().getBitStream())));
    }

    private TipWrapper decorateCarouselAdReady(TipWrapper tip) {
        String name = tip.getTipExtra().getProgramNmae();
        return tip.setContent(PlayerUIHelper.getHighLightString(this.mContext.getResources().getString(C1291R.string.carousel_playing_next) + name, name));
    }

    private TipWrapper decorateMiddleAdStart(TipWrapper tip) {
        String tipMsg = this.mContext.getResources().getString(C1291R.string.ad_tip);
        CharSequence adTip = PlayerUIHelper.getJumpAdTip();
        if (StringUtils.isEmpty(adTip)) {
            return null;
        }
        return tip.setContent(adTip);
    }

    private TipWrapper decorate3D(TipWrapper tip) {
        if (!PlayerAppConfig.isNeedShowFullScreenHint()) {
            return tip.setContent(this.mContext.getString(C1291R.string.toast_3d_tv));
        }
        if (new AppPreference(this.mContext, PlayerExitHelper.SHARED_PREF_NAME).getBoolean("player_3d_hint_shown", false)) {
            return tip.setContent(this.mContext.getString(C1291R.string.toast_3d));
        }
        return null;
    }

    private TipWrapper decorateMiddleAdReady(TipWrapper tip) {
        return tip.setContent(this.mContext.getString(C1291R.string.middle_ad_tip));
    }

    private TipWrapper decorateSelection(TipWrapper tip) {
        return tip.setContent(PlayerUIHelper.getHighLightString(this.mContext.getString(C1291R.string.selection_panel_tip).toString(), this.mContext.getResources().getString(C1291R.string.selection_highlight_tip)));
    }

    private TipWrapper decorateCheckTailOrder(TipWrapper tip) {
        if (tip.getTipExtra().getNextVideo() == null) {
            return null;
        }
        return tip.setContent(PlayerUIHelper.getHighLightString(this.mContext.getString(C1291R.string.continue_play_next, new Object[]{episodeString}).toString(), this.mContext.getString(C1291R.string.play_order, new Object[]{Integer.valueOf(video.getPlayOrder())})));
    }

    private TipWrapper decorateCheckTailName(TipWrapper tip) {
        IVideo video = tip.getTipExtra().getNextVideo();
        if (video == null) {
            return null;
        }
        String name;
        String albumName = video.getAlbumName();
        CharSequence tvName = video.getTvName();
        if (StringUtils.isEmpty(tvName)) {
            name = albumName;
        } else {
            name = tvName;
        }
        return tip.setContent(PlayerUIHelper.getHighLightString(this.mContext.getString(C1291R.string.continue_play_next, new Object[]{name}).toString(), name));
    }

    private TipWrapper decorateReplay(TipWrapper tip) {
        return tip.setContent(this.mContext.getString(C1291R.string.video_replay));
    }

    private TipWrapper decorateJumHead(TipWrapper tip) {
        int resId;
        if (NetworkUtils.isNetworkAvaliable()) {
            resId = C1291R.string.has_jump_header;
        } else {
            resId = C1291R.string.has_jump_header_no_menu;
        }
        return tip.setContent(this.mContext.getString(resId));
    }

    private TipWrapper decorateHistory(TipWrapper tip) {
        CharSequence history;
        int videoPlayTime = this.mVideo.getVideoPlayTime();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "videoPlayTime:" + videoPlayTime);
        }
        boolean isTvSeries = this.mVideo.isTvSeries();
        int playOrder = this.mVideo.getPlayOrder();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isTV:" + isTvSeries + " plalyOrder:" + playOrder);
        }
        if (!isTvSeries || playOrder <= 0) {
            if (videoPlayTime < 60000) {
                history = this.mContext.getString(C1291R.string.video_history_continue_play_in_1_minute);
            } else {
                history = PlayerUIHelper.getHighLightString(this.mContext.getString(C1291R.string.episode_history_continue_play2, new Object[]{timeContent}).toString(), this.mContext.getString(C1291R.string.play_time, new Object[]{Integer.valueOf(videoPlayTime / 60000), Integer.valueOf(videoPlayTime / 60000), Integer.valueOf(videoPlayTime % 60000)}));
            }
        } else if (videoPlayTime < 60000) {
            history = this.mContext.getString(C1291R.string.video_history_continue_play_in_1_minute);
        } else {
            String orderContent = this.mContext.getString(C1291R.string.play_order, new Object[]{Integer.valueOf(playOrder)});
            history = PlayerUIHelper.getHighLightString(this.mContext.getString(C1291R.string.episode_history_continue_play2, new Object[]{content}).toString(), orderContent + this.mContext.getString(C1291R.string.play_time, new Object[]{Integer.valueOf(videoPlayTime / 60000)}));
        }
        return tip.setContent(history);
    }

    private TipWrapper decorateVipBitstream(TipWrapper tip) {
        int width = 0;
        int height = 0;
        BitStream toDefinition = tip.getTipExtra().getBitStream();
        SpannableString sp = PlayerUIHelper.getHighLightString(String.format(this.mContext.getString(C1291R.string.tryplay_vip_bitstream_tip), new Object[]{PlayerDataUtils.getBitStreamString(this.mContext, toDefinition)}), PlayerDataUtils.getBitStreamString(this.mContext, toDefinition));
        Drawable drawable = null;
        PlayerUIHelper.getNewPreviewPurchaseTip();
        Object[] object = PlayerUIHelper.getPreviewPurchasePicture(this.mContext, this.mVideo.is3d());
        if (object == null || object[0] == null) {
            object = PlayerUIHelper.getLiveDefaultBitmap(this.mContext);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendVipBitStreamTip bitmap width/height=" + object[1] + "/" + object[2]);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendVipBitStreamTip bitmap" + object.length + object[0]);
            }
            if (((Integer) object[1]).intValue() > 0 && ((Integer) object[2]).intValue() > 0) {
                width = ((Integer) object[1]).intValue();
                height = ((Integer) object[2]).intValue();
            }
            if (object[0] != null) {
                drawable = new BitmapDrawable(this.mContext.getResources(), (Bitmap) object[0]);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendVipBitStreamTip bitmap is null");
            }
        } else {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendVipBitStreamTip bitmap width/height=" + object[1] + "/" + object[2]);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendVipBitStreamTip bitmap" + object.length + object[0]);
            }
            if (((Integer) object[1]).intValue() > 0 && ((Integer) object[2]).intValue() > 0) {
                width = ((Integer) object[1]).intValue();
                height = ((Integer) object[2]).intValue();
            }
            if (object[0] != null) {
                drawable = new BitmapDrawable(this.mContext.getResources(), (Bitmap) object[0]);
            }
        }
        return tip.setContent(sp).setDrawable(drawable).setHeight(height).setWidth(width);
    }

    private TipWrapper decoratePreview(TipWrapper itip) {
        if (this.mVideo == null) {
            return null;
        }
        String msg = null;
        Drawable drawable = null;
        int width = 0;
        int height = 0;
        CharSequence tryTip;
        if (this.mVideo.getProvider().getSourceType() == SourceType.PUSH) {
            msg = this.mContext.getString(C1291R.string.tryplay_tip_vip_push);
            tryTip = PlayerUIHelper.getPushVipPreviewTip();
            if (!StringUtils.isEmpty(tryTip)) {
                msg = tryTip;
            }
        } else if (this.mVideo.isPreview()) {
            tryTip = PlayerUIHelper.getNewPreviewPurchaseTip();
            object = PlayerUIHelper.getPreviewPurchasePicture(this.mContext, this.mVideo.is3d());
            if (this.mVideo.getAlbum().getPayMarkType() == PayMarkType.COUPONS_ON_DEMAND_MARK) {
                msg = this.mContext.getResources().getString(C1291R.string.tryplay_tip_need_coupon);
            } else if (this.mVideo.getAlbum().getPayMarkType() == PayMarkType.PAY_ON_DEMAND_MARK) {
                msg = this.mContext.getResources().getString(C1291R.string.tryplay_tip_need_single_buy);
            } else if (this.mVideo.getAlbum().getPayMarkType() == PayMarkType.VIP_MARK) {
                if (StringUtils.isEmpty(tryTip) || object == null || object[0] == null) {
                    msg = this.mContext.getResources().getString(C1291R.string.tryplay_tip_need_buy);
                    object = PlayerUIHelper.getDefaultBitmap(this.mContext, this.mVideo.is3d());
                } else {
                    msg = tryTip;
                }
            } else if (this.mVideo.getAlbum().getPayMarkType() == null) {
                if (StringUtils.isEmpty(tryTip) || object == null || object[0] == null) {
                    msg = this.mContext.getResources().getString(C1291R.string.tryplay_tip_need_buy);
                    object = PlayerUIHelper.getDefaultBitmap(this.mContext, this.mVideo.is3d());
                } else {
                    msg = tryTip;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendPreviewTip bitmap width/height=" + object[1] + "/" + object[2]);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendPreviewTip bitmap" + object.length + object[0]);
            }
            if (((Integer) object[1]).intValue() > 0 && ((Integer) object[2]).intValue() > 0) {
                width = ((Integer) object[1]).intValue();
                height = ((Integer) object[2]).intValue();
            }
            if (object[0] != null) {
                drawable = new BitmapDrawable(this.mContext.getResources(), (Bitmap) object[0]);
            }
        } else if (this.mVideo.getProvider().getSourceType() == SourceType.LIVE && this.mVideo.getProvider().getLiveVideo().isLiveVipShowTrailer()) {
            tryTip = PlayerUIHelper.getLivePreviewPurchaseTip();
            object = PlayerUIHelper.getLLivePurchasePicture(this.mContext);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendPreviewTip bitmap" + object.length + object[0]);
            }
            if (StringUtils.isEmpty(tryTip) || object == null || object[0] == null) {
                msg = this.mContext.getString(C1291R.string.live_tryplay_tip_need_buy);
                object = PlayerUIHelper.getLiveDefaultBitmap(this.mContext);
            } else {
                msg = tryTip;
            }
            if (((Integer) object[1]).intValue() > 0 && ((Integer) object[2]).intValue() > 0) {
                width = ((Integer) object[1]).intValue();
                height = ((Integer) object[2]).intValue();
            }
            if (object[0] != null) {
                drawable = new BitmapDrawable(this.mContext.getResources(), (Bitmap) object[0]);
            }
        } else {
            msg = this.mContext.getString(C1291R.string.tryplay_tip_only_see);
            tryTip = PlayerUIHelper.getPreviewCannotBuyTip();
            if (!StringUtils.isEmpty(tryTip)) {
                msg = tryTip;
            }
        }
        return itip.setContent(msg).setDrawable(drawable).setHeight(height).setWidth(width);
    }

    public void setAdData(BaseAdData data) {
        this.mAdData = data;
    }

    private TipWrapper decorateHDROpen(TipWrapper itip) {
        String msg;
        Drawable drawable = null;
        int width = 0;
        int height = 0;
        if (this.mVideo.getProvider().getSourceType() == SourceType.PUSH) {
            msg = this.mContext.getString(C1291R.string.tryplay_tip_vip_push);
            CharSequence tryTip = PlayerUIHelper.getPushVipPreviewTip();
            if (!StringUtils.isEmpty(tryTip)) {
                msg = tryTip;
            }
        } else if (this.mVideo.getProvider().getSourceType() == SourceType.LIVE && this.mVideo.getProvider().getLiveVideo().isLiveVipShowTrailer()) {
            return null;
        } else {
            msg = this.mContext.getString(C1291R.string.hdr_tip_need_buy);
            Object[] object = PlayerUIHelper.getHDRDefaultBitmap(this.mContext, this.mVideo.is3d());
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendHDRTip bitmap width/height=" + object[1] + "/" + object[2]);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendHDRTip bitmap" + object.length + object[0]);
            }
            if (((Integer) object[1]).intValue() > 0 && ((Integer) object[2]).intValue() > 0) {
                width = ((Integer) object[1]).intValue();
                height = ((Integer) object[2]).intValue();
            }
            if (object[0] != null) {
                drawable = new BitmapDrawable(this.mContext.getResources(), (Bitmap) object[0]);
            }
        }
        return itip.setContent(msg).setDrawable(drawable).setHeight(height).setWidth(width);
    }

    private TipWrapper decorateQuestionnaireStart(TipWrapper itip) {
        return itip.setContent(this.mContext.getResources().getString(C1291R.string.questionnaire_start));
    }

    private TipWrapper decorateLogin(TipWrapper itip) {
        Drawable drawable = null;
        int width = 0;
        int height = 0;
        String str = this.mContext.getResources().getString(C1291R.string.player_login_tip);
        Object[] object = PlayerUIHelper.getLoginBitmap(this.mContext, this.mVideo.is3d());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "sendPreviewTip bitmap" + object.length + object[0]);
        }
        if (((Integer) object[1]).intValue() > 0 && ((Integer) object[2]).intValue() > 0) {
            width = ((Integer) object[1]).intValue();
            height = ((Integer) object[2]).intValue();
        }
        if (object[0] != null) {
            drawable = new BitmapDrawable(this.mContext.getResources(), (Bitmap) object[0]);
        }
        return itip.setContent(str).setDrawable(drawable).setHeight(height).setWidth(width);
    }
}
