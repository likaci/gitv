package com.gala.video.lib.share.uikit.card.settingapp;

import android.text.TextUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.card.Card.CardActionPolicy;
import com.gala.video.lib.share.uikit.card.GridCard;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;

public class SettingAppCard extends GridCard {
    private static final String LOG_TAG = "SettingAppCard";
    private SettingAppActionPolicy mActionPolicy;
    private ISettingApp mCard;

    public class SettingAppActionPolicy extends CardActionPolicy {
        public SettingAppActionPolicy(Card card) {
            super(card);
        }
    }

    public void setModel(CardInfoModel model) {
        super.setModel(model);
        if (this.mCard == null) {
            if (TextUtils.equals(Source.CONFIGURATION, model.mSource)) {
                this.mCard = new SettingCard(this);
            } else if (TextUtils.equals("setting", model.mSource)) {
                this.mCard = new MySettingCard(this);
            } else if (TextUtils.equals(Source.APPLICATION, model.mSource)) {
                this.mCard = new AppCard(this);
            }
        }
        if (this.mCard == null) {
            LogUtils.e(LOG_TAG, "model.mSource = ", model.mSource);
            return;
        }
        LogUtils.i(LOG_TAG, "SettingAppCard cardmodel = ", model);
        this.mCard.setModel(model);
    }

    public ActionPolicy getActionPolicy() {
        if (this.mActionPolicy == null) {
            this.mActionPolicy = new SettingAppActionPolicy(this);
        }
        return this.mActionPolicy;
    }

    public void destroy() {
        super.destroy();
        if (this.mCard != null) {
            this.mCard.onDestory();
        }
    }
}
