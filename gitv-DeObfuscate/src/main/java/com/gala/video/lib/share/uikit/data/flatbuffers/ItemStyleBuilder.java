package com.gala.video.lib.share.uikit.data.flatbuffers;

import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemData;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemMap;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemTemplet;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.Style;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle.FlatItemData;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle.FlatItemTemplet;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle.FlatItemTempletList;
import com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle.FlatStyle;
import java.nio.ByteBuffer;

public class ItemStyleBuilder {
    public ItemMap buildTempletList(ByteBuffer bf) {
        FlatItemTempletList ftl = FlatItemTempletList.getRootAsFlatItemTempletList(bf);
        ItemMap list = new ItemMap();
        for (int i = 0; i < ftl.itemStyleListLength(); i++) {
            ItemTemplet item = buildTemplet(ftl.itemStyleList(i));
            list.templeMap.put(item.styleType, item);
        }
        return list;
    }

    private ItemTemplet buildTemplet(FlatItemTemplet ft) {
        if (ft == null) {
            return null;
        }
        ItemTemplet it = new ItemTemplet();
        it.styleType = ft.styleType();
        it.itemList = new ItemData[ft.styleListLength()];
        for (int i = 0; i < ft.styleListLength(); i++) {
            it.itemList[i] = buildItemData(ft.styleList(i));
        }
        return it;
    }

    private ItemData buildItemData(FlatItemData fi) {
        if (fi == null) {
            return null;
        }
        ItemData item = new ItemData();
        item.id = fi.id();
        item.style = buildStyle(fi.style());
        item.type = fi.type();
        item.z_order = fi.zOrder();
        return item;
    }

    private Style buildStyle(FlatStyle fs) {
        if (fs == null) {
            return null;
        }
        Style style = new Style();
        style.f2043w = fs.m1584w();
        style.f2042h = fs.m1583h();
        style.mg_b = fs.mgB();
        style.mg_l = fs.mgL();
        style.mg_r = fs.mgR();
        style.mg_t = fs.mgT();
        style.pd_b = fs.pdB();
        style.pd_l = fs.pdL();
        style.pd_r = fs.pdR();
        style.pd_t = fs.pdT();
        style.visible = fs.visible();
        style.gravity = fs.gravity();
        style.bg_h = fs.bgH();
        style.bg_w = fs.bgW();
        style.bg_pd_b = fs.bgPdB();
        style.bg_pd_l = fs.bgPdL();
        style.bg_pd_r = fs.bgPdR();
        style.bg_pd_t = fs.bgPdT();
        style.bg_mg_b = fs.bgMgB();
        style.bg_mg_l = fs.bgMgL();
        style.bg_mg_r = fs.bgMgR();
        style.bg_mg_t = fs.bgMgT();
        style.font_size = fs.fontSize();
        style.line_space = fs.lineSpace();
        style.marq_text_space = fs.marqTextSpace();
        style.shadow_dx = fs.shadowDx();
        style.shadow_dy = fs.shadowDy();
        style.bg_value = fs.bgValue();
        style.bg_focus_value = fs.bgFocusValue();
        style.bg_visible = fs.bgVisible();
        style.bg_scale_type = fs.bgScaleType();
        style.bg_clip_padding = fs.bgClipPadding();
        style.bg_gravity = fs.bgGravity();
        style.text = fs.text();
        style.default_text = fs.defaultText();
        style.title_type = fs.titleType();
        style.font = fs.font();
        style.font_color = fs.fontColor();
        style.focus_font_color = fs.focusFontColor();
        style.lines = fs.lines();
        style.ellipsize = fs.ellipsize();
        style.skew_x = fs.skewX();
        style.shadow_color = fs.shadowColor();
        style.shadow_radius = fs.shadowRadius();
        style.marq_delay = fs.marqDelay();
        style.marq_speed = fs.marqSpeed();
        style.clip_padding = fs.clipPadding();
        style.clip_type = fs.clipType();
        style.scale_type = fs.scaleType();
        style.value = fs.value();
        style.focus_value = fs.focusValue();
        style.default_value = fs.defaultValue();
        return style;
    }
}
