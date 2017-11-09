package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class EquityView extends RelativeLayout {
    private static final String TAG = "EquityView";
    public static final int TYPE_COUPON_NOT_VIP = 8;
    public static final int TYPE_COUPON_VIP = 4;
    public static final int TYPE_NOT_VIP = 50;
    public static final int TYPE_OTHER = 100;
    public static final int TYPE_PAY_NOT_PREFERENTIAL_PRICE = 296;
    public static final int TYPE_PAY_NOT_VIP = 2;
    public static final int TYPE_PAY_VIP = 1;
    public static final int TYPE_VIP = 22;
    private ImageView mIvEquity;
    private TextView mTvCouponCount;
    private TextView mTvOriginPrice;
    private TextView mTvPrice;
    private int mType = 100;

    public EquityView(Context context) {
        super(context);
        initialize(context);
    }

    public EquityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public EquityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C1291R.layout.share_detail_equity_view, this);
        this.mIvEquity = (ImageView) findViewById(C1291R.id.share_detail_iv_album_equity);
        this.mTvCouponCount = (TextView) findViewById(C1291R.id.share_detail_tv_album_coupon_count);
        this.mTvPrice = (TextView) findViewById(C1291R.id.share_detail_tv_album_price);
        this.mTvOriginPrice = (TextView) findViewById(C1291R.id.share_detail_tv_album_origin_price);
    }

    private void showPayView(boolean isShowOrigin) {
        LogUtils.m1568d(TAG, "showPayView()");
        this.mTvPrice.setVisibility(0);
        if (isShowOrigin) {
            this.mTvOriginPrice.setVisibility(0);
        } else {
            this.mTvOriginPrice.setVisibility(8);
        }
    }

    private void hidePayView() {
        LogUtils.m1568d(TAG, "hidePayView()");
        this.mTvPrice.setVisibility(8);
        this.mTvOriginPrice.setVisibility(8);
    }

    private void showCouponView() {
        LogUtils.m1568d(TAG, "showCouponView()");
        this.mTvCouponCount.setVisibility(0);
    }

    private void hideCouponView() {
        LogUtils.m1568d(TAG, "hideCouponView()");
        this.mTvCouponCount.setVisibility(8);
    }

    public void setType(int type) {
        this.mType = type;
        Bitmap bitmap = null;
        switch (type) {
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(), C1291R.drawable.share_detail_pay_vip);
                showPayView(true);
                hideCouponView();
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), C1291R.drawable.share_detail_pay_not_vip);
                hideCouponView();
                hidePayView();
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), C1291R.drawable.share_detail_coupon_vip);
                showCouponView();
                hidePayView();
                break;
            case 8:
                bitmap = BitmapFactory.decodeResource(getResources(), C1291R.drawable.share_detail_coupon_not_vip);
                hideCouponView();
                hidePayView();
                break;
            case 22:
            case 50:
            case 100:
                hideCouponView();
                hidePayView();
                break;
            case 296:
                bitmap = BitmapFactory.decodeResource(getResources(), C1291R.drawable.share_detail_pay_not_preferential_price);
                showPayView(false);
                hideCouponView();
                break;
            default:
                hideCouponView();
                hidePayView();
                break;
        }
        if (bitmap != null) {
            setBackground(bitmap);
        }
    }

    public void setBackground(Bitmap bitmap) {
        if (bitmap != null) {
            this.mIvEquity.setImageBitmap(bitmap);
        }
    }

    public void setCouponCount(String count) {
        if (this.mType == 4 && !StringUtils.isEmpty((CharSequence) count)) {
            this.mTvCouponCount.setText(count);
        }
    }

    public void setPrice(String price, String originPrice) {
        if (this.mType == 1 && !StringUtils.isEmpty((CharSequence) price) && !StringUtils.isEmpty((CharSequence) originPrice)) {
            this.mTvPrice.setText(conversionUnit(price));
            this.mTvOriginPrice.setText(conversionUnit(originPrice));
        }
    }

    public void setPrice(String price) {
        if (this.mType == 296) {
            this.mTvPrice.setText(conversionUnit(price));
        }
    }

    private String conversionUnit(String points) {
        String yuan = String.valueOf(((float) parseInt(points)) / 100.0f);
        if (yuan.indexOf(".") > 0) {
            return yuan.replaceAll("0+?$", "").replaceAll("[.]$", "");
        }
        return yuan;
    }

    private int parseInt(String s) {
        if (!StringUtils.isEmpty((CharSequence) s)) {
            try {
                return Integer.valueOf(s).intValue();
            } catch (Exception e) {
                LogUtils.m1568d(TAG, "parseInt(): error" + s);
            }
        }
        return 0;
    }
}
