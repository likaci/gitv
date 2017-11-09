package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Bitmap;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.contract.VipItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;

public class VipItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    private static final String LOG_TAG = "VipItemView";
    private ItemInfoModel mItemInfoModel;
    private ImageLoader mLoader = new ImageLoader();

    private static class ImageLoadCallback implements IImageLoadCallback {
        WeakReference<VipItemView> mOuter;

        public ImageLoadCallback(VipItemView outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(Bitmap bitmap) {
            VipItemView outer = (VipItemView) this.mOuter.get();
            if (outer != null) {
                CuteImage coreImageView = outer.getCoreImageView();
                if (coreImageView != null) {
                    coreImageView.setBitmap(bitmap);
                }
            }
        }

        public void onFailed(String url) {
            VipItemView outer = (VipItemView) this.mOuter.get();
            if (outer != null) {
                outer.recycleAndShowDefaultImage();
            }
        }
    }

    public VipItemView(Context context) {
        super(context);
        setTag(CardFocusHelper.FOCUS_RES, "share_bg_focus_home_vip");
    }

    public void onBind(Presenter object) {
        setStyleByName(object.getModel().getStyle());
        this.mItemInfoModel = object.getModel();
        updateUI(this.mItemInfoModel);
        CuteImage img = getCuteImage("ID_IMAGE");
        CuteText txt = getCuteText("ID_TITLE");
        int spaceW = ResourceUtil.getPx(6);
        if (this.mItemInfoModel.getActionModel().getItemType() == ItemDataType.VIP_BUY) {
            img.setWidth(ResourceUtil.getDimen(C1632R.dimen.dimen_56dp));
            img.setHeight(ResourceUtil.getDimen(C1632R.dimen.dimen_56dp));
            spaceW = ResourceUtil.getPx(5);
        }
        int itemW = this.mItemInfoModel.getWidth();
        int imgW = img.getWidth();
        int txtW = (int) txt.getPaint().measureText(txt.getText());
        int marginLeftImg = (itemW - ((imgW + txtW) + spaceW)) / 2;
        img.setMarginLeft(marginLeftImg);
        txt.setWidth(txtW);
        txt.setMarginLeft((marginLeftImg + imgW) + spaceW);
        setContentDescription(this.mItemInfoModel.getCuteViewData("ID_TITLE", "text"));
    }

    public void onUnbind(Presenter object) {
        recycleAndShowDefaultImage();
        recycle();
    }

    public void onShow(Presenter object) {
        loadImage();
        BaseActionModel actionMode = this.mItemInfoModel.getActionModel();
        if (actionMode != null && actionMode.getItemType() == ItemDataType.MSGCENTER) {
            setBubbleCount();
        }
    }

    public void onHide(Presenter object) {
        recycleAndShowDefaultImage();
    }

    private void recycleAndShowDefaultImage() {
        if (this.mLoader != null && !this.mLoader.isRecycled()) {
            this.mLoader.recycle();
        }
    }

    private void loadImage() {
        String iconUrl = this.mItemInfoModel.getCuteViewData("ID_IMAGE", "value");
        this.mLoader.setImageLoadCallback(new ImageLoadCallback(this));
        this.mLoader.loadImage(iconUrl, null);
    }

    public CuteImage getCoreImageView() {
        return getCuteImage("ID_IMAGE");
    }

    private CuteImage getBubbleBG() {
        return getCuteImage("ID_BUBBLE_BG");
    }

    private CuteText getBubbleTextView() {
        return getCuteText("ID_BUBBLE_DESC");
    }

    private void setBubbleCount() {
        int count = GetInterfaceTools.getMsgCenter().getUnreadIMsgListCount();
        CuteText bubbleTextView = getBubbleTextView();
        CuteImage bubbleBG = getBubbleBG();
        if (bubbleTextView != null && bubbleBG != null) {
            if (count <= 0) {
                bubbleTextView.setText("");
                bubbleBG.setVisible(0);
            } else if (count <= 9) {
                bubbleTextView.setText(String.valueOf(count));
                bubbleTextView.setMarginLeft(ResourceUtil.getPx(51));
                bubbleBG.setVisible(1);
                bubbleBG.setDrawable(ResourceUtil.getDrawable(C1632R.drawable.share_msg_bubble));
                bubbleBG.setWidth(ResourceUtil.getPx(37));
            } else {
                bubbleTextView.setText("9+");
                bubbleTextView.setMarginLeft(ResourceUtil.getPx(54));
                bubbleBG.setVisible(1);
                bubbleBG.setDrawable(ResourceUtil.getDrawable(C1632R.drawable.share_msg_long_bubble));
                bubbleBG.setWidth(ResourceUtil.getPx(43));
            }
        }
    }
}
