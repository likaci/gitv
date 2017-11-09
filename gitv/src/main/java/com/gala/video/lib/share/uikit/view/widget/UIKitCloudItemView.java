package com.gala.video.lib.share.uikit.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import com.gala.cloudui.CloudViewGala;
import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class UIKitCloudItemView extends CloudViewGala {
    public UIKitCloudItemView(Context context) {
        super(context);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == 66 || event.getKeyCode() == 23) && event.getAction() == 0) {
            return performClick() ? true : super.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public void updateUI(ItemInfoModel mItemModel) {
        if (mItemModel != null) {
            try {
                HashMap<String, HashMap<String, String>> cuteViewDatas = mItemModel.getCuteViewDatas();
                if (cuteViewDatas != null) {
                    for (Entry<String, HashMap<String, String>> entry : cuteViewDatas.entrySet()) {
                        String id = (String) entry.getKey();
                        HashMap<String, String> value = (HashMap) entry.getValue();
                        if (!(value == null || value.size() == 0)) {
                            Cute childAt = getChildAt(id);
                            if (childAt == null) {
                                continue;
                            } else if (childAt instanceof CuteText) {
                                String text = (String) value.get("text");
                                if (!TextUtils.isEmpty(text)) {
                                    ((CuteText) childAt).setText(text);
                                }
                            } else if (childAt instanceof CuteImage) {
                                Drawable img = CloudUtilsGala.getDrawable((String) value.get("value"));
                                Drawable focusImg = CloudUtilsGala.getDrawable((String) value.get(CuteConstants.FOCUS_VALUE));
                                if (img != null) {
                                    ((CuteImage) childAt).setDrawable(img);
                                }
                                if (focusImg != null) {
                                    ((CuteImage) childAt).setFocusDrawable(focusImg);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(getClass() + "@" + hashCode(), "updateUI error,mItemModel" + mItemModel);
                e.printStackTrace();
            }
        }
    }
}
