package com.gala.video.lib.share.uikit.card;

import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card.CardActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.view.TextCanvas;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.HashMap;

public class VipCard extends GridCard {
    private ActionPolicy mActionPolicy;

    class VipCardP extends CardActionPolicy {
        public VipCardP(Card card) {
            super(card);
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            super.onItemFocusChanged(parent, holder, hasFocus);
        }
    }

    public void setModel(CardInfoModel model) {
        super.setModel(model);
        HashMap<String, HashMap<String, String>> datas = model.getItemInfoModels()[0][0].getCuteViewDatas();
        if (!(datas == null || datas.get("ID_TITLE") == null)) {
            ((HashMap) datas.get("ID_TITLE")).put("text", GetInterfaceTools.getIGalaAccountManager().isVip() ? ActionBarDataFactory.TOP_BAR_TIME_NAME_RENEW_VIP : ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP);
        }
        CharSequence tips = model.getTitleTips();
        if (!StringUtils.isEmpty(tips)) {
            TextCanvas tc = new TextCanvas(AppRuntimeEnv.get().getApplicationContext());
            tc.setTextSize(ResourceUtil.getDimen(C1632R.dimen.dimen_16sp));
            tc.setHeight(ResourceUtil.getDimen(C1632R.dimen.dimen_26dp));
            tc.setBackground(C1632R.drawable.share_top_action_bar_tip_bg);
            tc.setMargin(ResourceUtil.getDimen(C1632R.dimen.dimen_8dp), 0, 0, 0);
            tc.setPadding(ResourceUtil.getDimen(C1632R.dimen.dimen_8dp), 0, ResourceUtil.getDimen(C1632R.dimen.dimen_9dp), 0);
            tc.setTextColor(ResourceUtil.getColor(C1632R.color.action_bar_tip_text));
            tc.setText(tips);
            getHeaderItem().setTips(tc);
        }
    }

    public ActionPolicy getActionPolicy() {
        if (this.mActionPolicy == null) {
            this.mActionPolicy = new VipCardP(this);
        }
        return this.mActionPolicy;
    }
}
