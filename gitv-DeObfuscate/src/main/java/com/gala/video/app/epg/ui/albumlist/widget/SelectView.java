package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SelectView extends RelativeLayout {
    public static final int FOCUS = 0;
    private static final int FOCUS_COLOR = -1;
    private static final int GREEN_COLOR = ResourceUtil.getColor(C0508R.color.gala_green);
    private static final String LOG_TAG = "EPG/album4/SelectView";
    public static final int NONE = 0;
    public static final int NORMAL = 2;
    public static final int ONLY_CARROUSEL = 3;
    public static final int ONLY_SEARCH = 1;
    public static final int ONLY_SELECT = 2;
    private static final int POSITION_0 = 0;
    private static final int POSITION_1 = 1;
    private static final int POSITION_2 = 2;
    public static final int REMAIN = 1;
    public static final int SEARCH_CARROUSEL = 6;
    public static final int SEARCH_CARROUSEL_VIP = 9;
    public static final int SEARCH_CENTER = 7;
    public static final int SEARCH_SELECT = 4;
    public static final int SEARCH_SELECT_VIP = 8;
    public static final int SEARCH_VIP = 5;
    public static final int TAG_CARROUSEL = 12;
    public static final int TAG_CENTER = 14;
    public static final int TAG_SEARCH = 10;
    public static final int TAG_SELECT = 11;
    public static final int TAG_VIP = 13;
    private final int NORMAL_COLOR = ResourceUtil.getColor(C0508R.color.albumview_normal_color);
    private Context mContext;
    private ImageView mFirstImageView;
    private RelativeLayout mFirstItemLayout;
    private TextView mFirstTextView;
    private View mFocusView;
    private LayoutInflater mInflater;
    private boolean mIsLogined = false;
    private boolean mIsVip = false;
    private int mLayoutType = -1;
    private ImageView mLineImage;
    private OnClickListener mOnClickListener = new C08802();
    private OnFocusChangeListener mOnFocusChangeListener = new C08791();
    private OnItemClickListener mOnItemClickListener;
    private OnItemSelectListener mOnItemSelectListener;
    private ImageView mSecondImageView;
    private RelativeLayout mSecondItemLayout;
    private TextView mSecondTextView;
    private ImageView mThirdImageView;
    private RelativeLayout mThirdItemLayout;
    private TextView mThirdTextView;
    private int mWidth = 200;

    public interface OnItemSelectListener {
        boolean onItemSelected(View view, int i, boolean z);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    class C08791 implements OnFocusChangeListener {
        C08791() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SelectView.this.mFocusView = v;
            }
            if (SelectView.this.mOnItemSelectListener != null) {
                if (!SelectView.this.mOnItemSelectListener.onItemSelected(v, ((Integer) v.getTag()).intValue(), hasFocus)) {
                }
            }
        }
    }

    class C08802 implements OnClickListener {
        C08802() {
        }

        public void onClick(View v) {
            if (SelectView.this.mOnItemClickListener != null) {
                SelectView.this.mOnItemClickListener.onItemClick(v, ((Integer) v.getTag()).intValue());
            }
        }
    }

    public SelectView(Context context) {
        super(context);
        init(context);
    }

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public void setViewParams(int type) {
        LogUtils.m1576i(LOG_TAG, "setViewParams ---type = ", Integer.valueOf(type));
        this.mWidth = getDimen(C0508R.dimen.dimen_218dp);
        this.mLayoutType = type;
        initLayout();
        setFocusable(true);
    }

    private void initLayout() {
        LayoutParams layoutParams = null;
        switch (this.mLayoutType) {
            case 0:
                layoutParams = new LayoutParams(getDimen(C0508R.dimen.dimen_218dp), getDimen(C0508R.dimen.dimen_44dp));
                break;
            case 1:
            case 2:
            case 3:
                layoutParams = new LayoutParams(getDimen(C0508R.dimen.dimen_218dp), getDimen(C0508R.dimen.dimen_75dp));
                layoutParams.topMargin = getDimen(C0508R.dimen.dimen_33dp);
                this.mFirstItemLayout = initItemLayout(0);
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                layoutParams = new LayoutParams(-2, getDimen(C0508R.dimen.dimen_160dp));
                this.mFirstItemLayout = initItemLayout(0);
                this.mSecondItemLayout = initItemLayout(1);
                setFocus(null, this.mFirstItemLayout);
                setFocus(this.mFirstItemLayout, this.mSecondItemLayout);
                setRelativePlace(this.mFirstItemLayout, this.mSecondItemLayout);
                break;
            case 8:
            case 9:
                layoutParams = new LayoutParams(-2, getDimen(C0508R.dimen.dimen_230dp));
                this.mFirstItemLayout = initItemLayout(0);
                this.mSecondItemLayout = initItemLayout(1);
                this.mThirdItemLayout = initItemLayout(2);
                setFocus(null, this.mFirstItemLayout);
                setFocus(this.mFirstItemLayout, this.mSecondItemLayout);
                setFocus(this.mSecondItemLayout, this.mThirdItemLayout);
                setRelativePlace(this.mFirstItemLayout, this.mSecondItemLayout);
                setRelativePlace(this.mSecondItemLayout, this.mThirdItemLayout);
                break;
            default:
                LogUtils.m1573e(LOG_TAG, "init --- switch --- illgel argument, type = ", Integer.valueOf(this.mLayoutType));
                break;
        }
        setLayoutParams(layoutParams);
        if (this.mFirstItemLayout != null) {
            addView(this.mFirstItemLayout);
            this.mFirstImageView = findImageView(this.mFirstItemLayout);
            this.mFirstTextView = findTextView(this.mFirstItemLayout);
        }
        if (this.mSecondItemLayout != null) {
            addView(this.mSecondItemLayout);
            this.mSecondImageView = findImageView(this.mSecondItemLayout);
            this.mSecondTextView = findTextView(this.mSecondItemLayout);
        }
        if (this.mThirdItemLayout != null) {
            addView(this.mThirdItemLayout);
            this.mThirdImageView = findImageView(this.mThirdItemLayout);
            this.mThirdTextView = findTextView(this.mThirdItemLayout);
        }
        initDivideLine();
        switch (this.mLayoutType) {
            case 0:
                return;
            case 1:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                return;
            case 2:
                initSelectItem(this.mFirstImageView, this.mFirstTextView);
                return;
            case 3:
                initCarrouselItem(this.mFirstImageView, this.mFirstTextView);
                return;
            case 4:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                initSelectItem(this.mSecondImageView, this.mSecondTextView);
                return;
            case 5:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                initVipItem(this.mSecondImageView, this.mSecondTextView);
                return;
            case 6:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                initCarrouselItem(this.mSecondImageView, this.mSecondTextView);
                return;
            case 7:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                initCenterItem(this.mSecondImageView, this.mSecondTextView);
                return;
            case 8:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                initSelectItem(this.mSecondImageView, this.mSecondTextView);
                initVipItem(this.mThirdImageView, this.mThirdTextView);
                return;
            case 9:
                initSearchItem(this.mFirstImageView, this.mFirstTextView);
                initCarrouselItem(this.mSecondImageView, this.mSecondTextView);
                initVipItem(this.mThirdImageView, this.mThirdTextView);
                return;
            default:
                LogUtils.m1573e(LOG_TAG, "init --- illgel arguments, type = ", Integer.valueOf(this.mLayoutType));
                return;
        }
    }

    private RelativeLayout initItemLayout(int position) {
        RelativeLayout relativeLayout = (RelativeLayout) this.mInflater.inflate(C0508R.layout.epg_select_view_item_layout, null);
        LayoutParams layoutParams = new LayoutParams(this.mWidth, -2);
        if (position == 0) {
            layoutParams.height = getDimen(C0508R.dimen.dimen_53dp);
            layoutParams.topMargin = getDimen(C0508R.dimen.dimen_10dp);
            layoutParams.bottomMargin = getDimen(C0508R.dimen.dimen_10dp);
        } else {
            layoutParams.topMargin = getDimen(C0508R.dimen.dimen_0dp);
        }
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setId(ViewUtils.generateViewId());
        relativeLayout.setNextFocusLeftId(relativeLayout.getId());
        return relativeLayout;
    }

    private void setFocus(RelativeLayout upLayout, RelativeLayout downLayout) {
        if (upLayout != null && downLayout != null) {
            upLayout.setNextFocusDownId(downLayout.getId());
            downLayout.setNextFocusUpId(upLayout.getId());
        } else if (upLayout != null || downLayout == null) {
            LogUtils.m1573e(LOG_TAG, "setFocus error, illgel arguments, upLayout = ", upLayout, " downLayout = ", downLayout);
        } else {
            downLayout.setNextFocusUpId(downLayout.getId());
        }
    }

    private void setRelativePlace(RelativeLayout upLayout, RelativeLayout downLayout) {
        if (upLayout == null || downLayout == null) {
            LogUtils.m1573e(LOG_TAG, "setRelativePlace error --- illgel arguments, upLayout = ", upLayout, " downLayout = ", downLayout);
            return;
        }
        LayoutParams layoutParams = (LayoutParams) downLayout.getLayoutParams();
        layoutParams.addRule(3, upLayout.getId());
        downLayout.setLayoutParams(layoutParams);
    }

    private void initDivideLine() {
        this.mLineImage = (ImageView) this.mInflater.inflate(C0508R.layout.epg_select_view_divide_line_layout, null);
        LayoutParams imageParams = new LayoutParams(getDimen(C0508R.dimen.dimen_169dp), getDimen(C0508R.dimen.dimen_2dp));
        this.mLineImage.setFocusable(false);
        imageParams.addRule(14);
        imageParams.addRule(12);
        addView(this.mLineImage, imageParams);
    }

    private ImageView findImageView(RelativeLayout relativeLayout) {
        return relativeLayout != null ? (ImageView) relativeLayout.findViewById(C0508R.id.epg_select_view_item_imageview) : null;
    }

    private TextView findTextView(RelativeLayout relativeLayout) {
        return relativeLayout != null ? (TextView) relativeLayout.findViewById(C0508R.id.epg_select_view_item_textview) : null;
    }

    private void initSearchItem(ImageView imageView, TextView textView) {
        if (imageView != null && textView != null) {
            imageView.setImageResource(C0508R.drawable.epg_ic_top_search_unfocus_icon);
            textView.setText("搜 索");
            textView.setContentDescription(getStr(C0508R.string.search_title));
            getSearchItem().setTag(Integer.valueOf(10));
            getSearchItem().setOnClickListener(this.mOnClickListener);
            getSearchItem().setOnFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    private void initSelectItem(ImageView imageView, TextView textView) {
        if (imageView != null && textView != null) {
            imageView.setImageResource(C0508R.drawable.epg_ic_top_select_focus_icon);
            textView.setText("筛 选");
            textView.setContentDescription(getStr(C0508R.string.label_select));
            getSelectItem().setTag(Integer.valueOf(11));
            getSelectItem().setOnClickListener(this.mOnClickListener);
            getSelectItem().setOnFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    private void initVipItem(ImageView imageView, TextView textView) {
        if (imageView != null && textView != null) {
            String str;
            imageView.setImageResource(C0508R.drawable.epg_ic_top_vip_unfocus_icon);
            this.mIsVip = UserUtil.isOTTVip();
            if (this.mIsVip) {
                str = getStr(C0508R.string.label_long_vip);
                textView.setContentDescription(ActionBarDataFactory.TOP_BAR_TIME_NAME_RENEW_VIP);
            } else {
                str = getStr(C0508R.string.label_open_vip);
                textView.setContentDescription(ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP);
            }
            textView.setText(str);
            getVipItem().setTag(Integer.valueOf(13));
            getVipItem().setOnClickListener(this.mOnClickListener);
            getVipItem().setOnFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    private void initCarrouselItem(ImageView imageView, TextView textView) {
        if (imageView != null && textView != null) {
            imageView.setImageResource(C0508R.drawable.epg_ic_top_carrousel_focus_icon);
            textView.setText("轮 播");
            textView.setContentDescription(getStr(C0508R.string.label_carrousel));
            getCarrouselItem().setTag(Integer.valueOf(12));
            getCarrouselItem().setOnClickListener(this.mOnClickListener);
            getCarrouselItem().setOnFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    private void initCenterItem(ImageView imageView, TextView textView) {
        if (imageView != null && textView != null) {
            String str;
            imageView.setImageResource(C0508R.drawable.epg_foot_person_center_default);
            this.mIsLogined = UserUtil.isLogin();
            if (this.mIsLogined) {
                str = getStr(C0508R.string.foot_person_center);
                QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_30dp);
            } else {
                str = getStr(C0508R.string.foot_login_register);
                QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_28dp);
            }
            textView.setText(str);
            textView.setContentDescription(str);
            getCenterItem().setTag(Integer.valueOf(14));
            getCenterItem().setOnClickListener(this.mOnClickListener);
            getCenterItem().setOnFocusChangeListener(this.mOnFocusChangeListener);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() != 66 && event.getKeyCode() != 23) || event.getAction() != 0 || this.mFocusView == null) {
            return super.dispatchKeyEvent(event);
        }
        this.mFocusView.performClick();
        return true;
    }

    public void setSelectViewColorStatus(int status) {
        if (getSelectItem() != null) {
            ImageView imageView = getSelectIcon();
            TextView textView = getSelectTextView();
            if (!isNull(imageView, textView)) {
                switch (status) {
                    case 0:
                        setImageResource(imageView, C0508R.drawable.epg_ic_top_select_focus_icon);
                        setTextColor(textView, -1);
                        return;
                    case 1:
                        setImageResource(imageView, C0508R.drawable.epg_ic_top_selected_icon);
                        setTextColor(textView, GREEN_COLOR);
                        return;
                    case 2:
                        setImageResource(imageView, C0508R.drawable.epg_ic_top_select_normal_icon);
                        setTextColor(textView, this.NORMAL_COLOR);
                        return;
                    default:
                        LogUtils.m1573e(LOG_TAG, "setSelectViewColorStatus --- illgel arguments,status = ", Integer.valueOf(status));
                        return;
                }
            }
        }
    }

    public void setCarrouselViewColorStatus(int status) {
        if (getCarrouselItem() != null) {
            ImageView imageView = getCarrouselIcon();
            TextView textView = getCarrouselTextView();
            if (!isNull(imageView, textView)) {
                switch (status) {
                    case 0:
                        setImageResource(imageView, C0508R.drawable.epg_ic_top_carrousel_focus_icon);
                        setTextColor(textView, -1);
                        return;
                    case 2:
                        setImageResource(imageView, C0508R.drawable.epg_ic_top_carrousel_normal_icon);
                        setTextColor(textView, this.NORMAL_COLOR);
                        return;
                    default:
                        LogUtils.m1573e(LOG_TAG, "setCarrouselViewColorStatus --- illgel arguments,status = ", Integer.valueOf(status));
                        return;
                }
            }
        }
    }

    private boolean isNull(ImageView imageView, TextView textView) {
        if (imageView == null || textView == null) {
            return true;
        }
        return false;
    }

    private void setImageResource(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }

    private void setTextColor(TextView textView, int color) {
        textView.setTextColor(color);
    }

    public void setOnItemSelectListener(OnItemSelectListener selectListener) {
        this.mOnItemSelectListener = selectListener;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    private int getDimen(int id) {
        return ResourceUtil.getDimensionPixelSize(id);
    }

    private String getStr(int resId) {
        return ResourceUtil.getStr(resId);
    }

    public void setLineImageVisible(int visible) {
        if (this.mLineImage != null) {
            this.mLineImage.setVisibility(visible);
        }
    }

    public View getSearchItem() {
        if (1 == this.mLayoutType || this.mLayoutType >= 4) {
            return this.mFirstItemLayout;
        }
        return null;
    }

    public View getSelectItem() {
        if (2 == this.mLayoutType) {
            return this.mFirstItemLayout;
        }
        if (4 == this.mLayoutType || 8 == this.mLayoutType) {
            return this.mSecondItemLayout;
        }
        return null;
    }

    public View getCarrouselItem() {
        if (3 == this.mLayoutType) {
            return this.mFirstItemLayout;
        }
        if (6 == this.mLayoutType || 9 == this.mLayoutType) {
            return this.mSecondItemLayout;
        }
        return null;
    }

    public View getVipItem() {
        if (5 == this.mLayoutType) {
            return this.mSecondItemLayout;
        }
        if (8 == this.mLayoutType || 9 == this.mLayoutType) {
            return this.mThirdItemLayout;
        }
        return null;
    }

    public View getCenterItem() {
        if (7 == this.mLayoutType) {
            return this.mSecondItemLayout;
        }
        return null;
    }

    public View getFirstItem() {
        if (this.mLayoutType == 0) {
            return null;
        }
        return this.mFirstItemLayout;
    }

    public View getLastItem() {
        switch (this.mLayoutType) {
            case 0:
                return null;
            case 1:
            case 2:
            case 3:
                return this.mFirstItemLayout;
            case 4:
            case 5:
            case 6:
            case 7:
                return this.mSecondItemLayout;
            case 8:
            case 9:
                return this.mThirdItemLayout;
            default:
                LogUtils.m1573e(LOG_TAG, "getLastItem --- illgel type, mType = ", Integer.valueOf(this.mLayoutType));
                return null;
        }
    }

    public void setVipItemStatus(boolean isVip) {
        if (getVipItem() != null && isVip != this.mIsVip) {
            String str;
            this.mIsVip = isVip;
            TextView textView = getVipTextView();
            if (this.mIsVip) {
                str = getStr(C0508R.string.label_long_vip);
                textView.setContentDescription(ActionBarDataFactory.TOP_BAR_TIME_NAME_RENEW_VIP);
            } else {
                str = getStr(C0508R.string.label_open_vip);
                textView.setContentDescription(ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP);
            }
            textView.setText(str);
        }
    }

    public void setCenterItemStatus(boolean isLogined) {
        if (getCenterItem() != null && isLogined != this.mIsLogined) {
            String str;
            this.mIsLogined = isLogined;
            TextView textView = getCenterTextView();
            if (this.mIsLogined) {
                str = getStr(C0508R.string.foot_person_center);
                QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_30dp);
            } else {
                str = getStr(C0508R.string.foot_login_register);
                QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_28dp);
            }
            textView.setText(str);
            textView.setContentDescription(str);
        }
    }

    public String getTagName(int tagType) {
        switch (tagType) {
            case 10:
                return getNameStr(getSearchTextView());
            case 11:
                return getNameStr(getSelectTextView());
            case 12:
                return getNameStr(getCarrouselTextView());
            case 13:
                return getNameStr(getVipTextView());
            case 14:
                return getNameStr(getCenterTextView());
            default:
                LogUtils.m1573e(LOG_TAG, "getTagName --- illgel argument, tagType = ", Integer.valueOf(tagType));
                return "";
        }
    }

    private String getNameStr(TextView view) {
        if (view == null || view.getContentDescription() == null) {
            return null;
        }
        return view.getContentDescription().toString();
    }

    public ImageView getSearchIcon() {
        if (1 == this.mLayoutType || this.mLayoutType >= 4) {
            return this.mFirstImageView;
        }
        return null;
    }

    public TextView getSearchTextView() {
        if (1 == this.mLayoutType || this.mLayoutType >= 4) {
            return this.mFirstTextView;
        }
        return null;
    }

    public ImageView getSelectIcon() {
        return findImageView((RelativeLayout) getSelectItem());
    }

    public TextView getSelectTextView() {
        return findTextView((RelativeLayout) getSelectItem());
    }

    public ImageView getVipIcon() {
        return findImageView((RelativeLayout) getVipItem());
    }

    public TextView getVipTextView() {
        return findTextView((RelativeLayout) getVipItem());
    }

    public ImageView getCarrouselIcon() {
        return findImageView((RelativeLayout) getCarrouselItem());
    }

    public TextView getCarrouselTextView() {
        return findTextView((RelativeLayout) getCarrouselItem());
    }

    public ImageView getCenterIcon() {
        return findImageView((RelativeLayout) getCenterItem());
    }

    public TextView getCenterTextView() {
        return findTextView((RelativeLayout) getCenterItem());
    }
}
