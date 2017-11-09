package com.gala.video.lib.share.uikit.data.flatbuffers;

import android.util.Log;
import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteBg;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.cloudui.utils.CuteUtils;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemData;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemMap;
import com.gala.video.lib.share.uikit.data.data.Model.itemstyle.ItemTemplet;
import com.gala.video.lib.share.uikit.data.data.cache.LayoutCache;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.Map.Entry;

public class ItemStyleFlatBufferParser {
    private static final String TAG = "ItemStyleFlatBufferParser";

    public void initItemStyle() {
        ItemMap item = new LayoutCache().getItem();
        if (item == null) {
            Log.e(TAG, "initItemStyle ItemMap=null,return");
            return;
        }
        for (Entry<String, ItemTemplet> next : item.templeMap.entrySet()) {
            String key = (String) next.getKey();
            ItemData[] itemList = ((ItemTemplet) next.getValue()).itemList;
            Cute[] cutes = new Cute[itemList.length];
            for (int i = 0; i < itemList.length; i++) {
                ItemData itemData = itemList[i];
                String type = itemData.type;
                Cute c = null;
                if (CuteConstants.TYPE_IMG.equals(type)) {
                    c = new CuteImage();
                } else if (CuteConstants.TYPE_TXT.equals(type)) {
                    c = new CuteText();
                } else if ("bg".equals(type)) {
                    c = new CuteBg();
                }
                setJsonDataByForeach(c, itemData);
                cutes[i] = c;
            }
            CuteUtils.putItemStyle(key, cutes);
        }
    }

    private void setJsonDataByForeach(Cute dataModel, ItemData item) {
        if (dataModel instanceof CuteImage) {
            CuteImage d = (CuteImage) dataModel;
            d.setId(item.id);
            d.setType(item.type);
            d.setZOrder(item.z_order);
            d.setWidth(ResourceUtil.getPxShort(item.style.w));
            d.setHeight(ResourceUtil.getPxShort(item.style.h));
            d.setMarginLeft(ResourceUtil.getPxShort(item.style.mg_l));
            d.setMarginTop(ResourceUtil.getPxShort(item.style.mg_t));
            d.setMarginRight(ResourceUtil.getPxShort(item.style.mg_r));
            d.setMarginBottom(ResourceUtil.getPxShort(item.style.mg_b));
            d.setPaddingLeft(ResourceUtil.getPxShort(item.style.pd_l));
            d.setPaddingTop(ResourceUtil.getPxShort(item.style.pd_t));
            d.setPaddingRight(ResourceUtil.getPxShort(item.style.pd_r));
            d.setPaddingBottom(ResourceUtil.getPxShort(item.style.pd_b));
            d.setVisible(item.style.visible);
            d.setClipPadding(item.style.clip_padding);
            d.setClipType(item.style.clip_type);
            d.setGravity(item.style.gravity);
            d.setScaleType(item.style.scale_type);
            d.setDrawable(CloudUtilsGala.getDrawable(item.style.value));
            d.setFocusDrawable(CloudUtilsGala.getDrawable(item.style.focus_value));
            d.setDefaultDrawable(CloudUtilsGala.getDrawable(item.style.default_value));
        } else if (dataModel instanceof CuteText) {
            CuteText d2 = (CuteText) dataModel;
            d2.setId(item.id);
            d2.setType(item.type);
            d2.setZOrder(item.z_order);
            d2.setWidth(ResourceUtil.getPxShort(item.style.w));
            d2.setHeight(ResourceUtil.getPxShort(item.style.h));
            d2.setMarginLeft(ResourceUtil.getPxShort(item.style.mg_l));
            d2.setMarginTop(ResourceUtil.getPxShort(item.style.mg_t));
            d2.setMarginRight(ResourceUtil.getPxShort(item.style.mg_r));
            d2.setMarginBottom(ResourceUtil.getPxShort(item.style.mg_b));
            d2.setPaddingLeft(ResourceUtil.getPxShort(item.style.pd_l));
            d2.setPaddingTop(ResourceUtil.getPxShort(item.style.pd_t));
            d2.setPaddingRight(ResourceUtil.getPxShort(item.style.pd_r));
            d2.setPaddingBottom(ResourceUtil.getPxShort(item.style.pd_b));
            d2.setVisible(item.style.visible);
            d2.setGravity(item.style.gravity);
            d2.setBgWidth(ResourceUtil.getPxShort(item.style.bg_w));
            d2.setBgHeight(ResourceUtil.getPxShort(item.style.bg_h));
            d2.setBgPaddingLeft(ResourceUtil.getPxShort(item.style.bg_pd_l));
            d2.setBgPaddingTop(ResourceUtil.getPxShort(item.style.bg_pd_t));
            d2.setBgPaddingRight(ResourceUtil.getPxShort(item.style.bg_pd_r));
            d2.setBgPaddingBottom(ResourceUtil.getPxShort(item.style.bg_pd_b));
            d2.setBgMarginLeft(ResourceUtil.getPxShort(item.style.bg_mg_l));
            d2.setBgMarginTop(ResourceUtil.getPxShort(item.style.bg_mg_t));
            d2.setBgMarginRight(ResourceUtil.getPxShort(item.style.bg_mg_r));
            d2.setBgMarginBottom(ResourceUtil.getPxShort(item.style.bg_mg_b));
            d2.setFontSize(ResourceUtil.getPxShort(item.style.font_size));
            d2.setLineSpace(ResourceUtil.getPxShort(item.style.line_space));
            d2.setMarqueeTextSpace(ResourceUtil.getPxShort(item.style.marq_text_space));
            d2.setShadowLayerDx(ResourceUtil.getPxShort(item.style.shadow_dx));
            d2.setShadowLayerDy(ResourceUtil.getPxShort(item.style.shadow_dy));
            d2.setBgDrawable(CloudUtilsGala.getDrawable(item.style.bg_value));
            d2.setBgFocusDrawable(CloudUtilsGala.getDrawable(item.style.bg_focus_value));
            d2.setFontColor(CloudUtilsGala.getColor(item.style.font_color));
            d2.setFocusFontColor(CloudUtilsGala.getColor(item.style.focus_font_color));
            d2.setShadowLayerColor(CloudUtilsGala.getColor(item.style.shadow_color));
            d2.setBgVisible(item.style.bg_visible);
            d2.setBgScaleType(item.style.bg_scale_type);
            d2.setBgClipPadding(item.style.bg_clip_padding);
            d2.setBgGravity(item.style.bg_gravity);
            d2.setText(item.style.text);
            d2.setDefaultText(item.style.default_text);
            d2.setTitleType(item.style.title_type);
            d2.setFont(item.style.font);
            d2.setLines(item.style.lines);
            d2.setEllipsize(item.style.ellipsize);
            d2.setSkewX(item.style.skew_x);
            d2.setShadowLayerRadius(item.style.shadow_radius);
            d2.setMarqueeDelay(item.style.marq_delay);
            d2.setMarqueeSpeed(item.style.marq_speed);
        } else if (dataModel instanceof CuteBg) {
            CuteBg d3 = (CuteBg) dataModel;
            d3.setId(item.id);
            d3.setType(item.type);
            d3.setZOrder(item.z_order);
            d3.setPaddingLeft(ResourceUtil.getPxShort(item.style.pd_l));
            d3.setPaddingTop(ResourceUtil.getPxShort(item.style.pd_t));
            d3.setPaddingRight(ResourceUtil.getPxShort(item.style.pd_r));
            d3.setPaddingBottom(ResourceUtil.getPxShort(item.style.pd_b));
            d3.setBackgroundDrawable(CloudUtilsGala.getDrawable(item.style.value));
        }
    }
}
