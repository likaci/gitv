package com.gala.video.app.epg.ui.star.widget;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ImageType;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.alignmentview.AlignmentTextView;
import com.gala.video.lib.share.common.widget.alignmentview.AlignmentTextView.OnLineCountListener;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class StarsInfoView {
    private static final String TAG = "EPG/StarsInfoView";
    private AlignmentTextView mDesc;
    private int mDescRealCount = 0;
    private TextView mDetailTextBirthday;
    private TextView mDetailTextHeight;
    private TextView mDetailTextPlace;
    private TextView mDetailTextWrok;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ImageView mImageView;
    private String mKeyWord;
    public String mLastUrl = "";
    private OnTextClickedListener mOnTextClickedListener;
    private View mRootView;
    private int mScaleDuration = 0;
    private Star mStar;
    private OnFocusChangeListener mTextOnFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            TextView textView = (TextView) v;
            if (textView != null) {
                textView.setTextColor(hasFocus ? ResourceUtil.getColor(R.color.play_button_text_focus_color) : ResourceUtil.getColor(R.color.play_button_text_normal_color));
                AnimationUtil.zoomAnimation(textView, hasFocus, 1.01f, StarsInfoView.this.mScaleDuration, true);
                if (!hasFocus && StarsInfoView.this.mScaleDuration == 0) {
                    StarsInfoView.this.mScaleDuration = 200;
                }
            }
        }
    };
    private TextView mTitle;

    public interface OnTextClickedListener {
        void onClick();
    }

    public void setOnTextClickedListener(OnTextClickedListener listener) {
        this.mOnTextClickedListener = listener;
    }

    public StarsInfoView(View root) {
        if (root == null) {
            throw new RuntimeException("root must not null!");
        }
        this.mRootView = root;
        getDesc();
    }

    private ImageView getImageView() {
        if (this.mImageView == null) {
            this.mImageView = (ImageView) this.mRootView.findViewById(R.id.epg_star_detail_photo_id);
            this.mImageView.setScaleType(ScaleType.FIT_XY);
        }
        return this.mImageView;
    }

    private TextView getTitleView() {
        if (this.mTitle == null) {
            this.mTitle = (TextView) this.mRootView.findViewById(R.id.epg_txt_album_title_id);
        }
        return this.mTitle;
    }

    private void setDefaultImage() {
        getImageView().setImageDrawable(ImageCacheUtil.DEFAULT_CIRCLE_DRAWABLE);
    }

    private void setTitle(String str) {
        if (!StringUtils.isEmpty((CharSequence) str)) {
            getTitleView().setText(str);
        }
    }

    private TextView getDetailTextWrok() {
        if (this.mDetailTextWrok == null) {
            this.mDetailTextWrok = (TextView) this.mRootView.findViewById(R.id.epg_txt_detail_occupation_id);
        }
        return this.mDetailTextWrok;
    }

    private TextView getDetailTextPlace() {
        if (this.mDetailTextPlace == null) {
            this.mDetailTextPlace = (TextView) this.mRootView.findViewById(R.id.epg_txt_detail_birthPlace_id);
        }
        return this.mDetailTextPlace;
    }

    private TextView getDetailTextBirthday() {
        if (this.mDetailTextBirthday == null) {
            this.mDetailTextBirthday = (TextView) this.mRootView.findViewById(R.id.epg_txt_detail_birthday_id);
        }
        return this.mDetailTextBirthday;
    }

    private TextView getDetailTextHeight() {
        if (this.mDetailTextHeight == null) {
            this.mDetailTextHeight = (TextView) this.mRootView.findViewById(R.id.epg_txt_detail_height_id);
        }
        return this.mDetailTextHeight;
    }

    public AlignmentTextView getDesc() {
        if (this.mDesc == null) {
            this.mDesc = (AlignmentTextView) this.mRootView.findViewById(R.id.epg_recommend_id);
            resetDescLayout();
            this.mDesc.setNextFocusUpId(this.mDesc.getId());
            this.mDesc.setNextFocusLeftId(this.mDesc.getId());
            this.mDesc.setNextFocusRightId(this.mDesc.getId());
            this.mDesc.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    StarsInfoView.this.notifyClicked();
                }
            });
        }
        this.mDesc.setOnFocusChangeListener(this.mTextOnFocusChangeListener);
        this.mDesc.setOnLineCountListener(new OnLineCountListener() {
            public void getRealCount(int lineCount) {
                StarsInfoView.this.mDescRealCount = lineCount;
            }
        });
        return this.mDesc;
    }

    public int getDetailDescRealCount() {
        return this.mDescRealCount;
    }

    private void resetDescLayout() {
        int left = getDimen(R.dimen.dimen_12dp);
        int top = getDimen(R.dimen.dimen_6dp);
        getDesc().setPadding(left, top, left, top);
    }

    public void setRootViewOnFocusChangeListener() {
        this.mHandler.post(new Runnable() {
            public void run() {
                StarsInfoView.this.getDesc().setOnFocusChangeListener(StarsInfoView.this.mTextOnFocusChangeListener);
            }
        });
    }

    private synchronized void notifyClicked() {
        if (this.mOnTextClickedListener != null) {
            this.mOnTextClickedListener.onClick();
        }
    }

    private void setDefInfo() {
        setDefaultImage();
        setDetailDefText();
    }

    private void setDetailDefText() {
        getDetailTextWrok().setText(String.format(ResourceUtil.getStr(R.string.stars_occupation), new Object[]{"-"}));
        getDetailTextPlace().setText(String.format(ResourceUtil.getStr(R.string.stars_birthPlace), new Object[]{"-"}));
        getDetailTextBirthday().setText(String.format(ResourceUtil.getStr(R.string.stars_birthday), new Object[]{"-"}));
        getDetailTextHeight().setText(String.format(ResourceUtil.getStr(R.string.stars_height), new Object[]{"-"}));
    }

    private void setDetailTextWork() {
        if (this.mStar == null || StringUtils.isEmpty(this.mStar.occupation)) {
            LogUtils.e(TAG, "setDetailTextWork occupation is null!");
            return;
        }
        setDetailTextWork(String.format(ResourceUtil.getStr(R.string.stars_occupation), new Object[]{this.mStar.occupation}));
    }

    private void setDetailTextPlace() {
        if (this.mStar == null || StringUtils.isEmpty(this.mStar.birthPlace)) {
            LogUtils.e(TAG, "setDetailTextPlace birthPlace is null!");
            return;
        }
        setDetailTextPlace(String.format(ResourceUtil.getStr(R.string.stars_birthPlace), new Object[]{this.mStar.birthPlace}));
    }

    private void setDetailTextBirthday() {
        if (this.mStar == null || StringUtils.isEmpty(this.mStar.birthday)) {
            LogUtils.e(TAG, "setDetailTextBirthday birthday is null!");
            return;
        }
        setDetailTextBirthday(String.format(ResourceUtil.getStr(R.string.stars_birthday), new Object[]{this.mStar.birthday}));
    }

    private void setDetailTextHeight() {
        if (this.mStar == null || StringUtils.isEmpty(this.mStar.height)) {
            LogUtils.e(TAG, "setDetailTextHeight height is null!");
            return;
        }
        setDetailTextHeight(String.format(ResourceUtil.getStr(R.string.stars_height), new Object[]{this.mStar.height + "cm"}));
    }

    private void setDetailTextBirthday(String str) {
        getDetailTextBirthday().setText(str);
    }

    private void setDetailTextHeight(String str) {
        getDetailTextHeight().setText(str);
    }

    private void setDetailTextWork(String str) {
        getDetailTextWrok().setText(str);
    }

    private void setDetailTextPlace(String str) {
        getDetailTextPlace().setText(str);
    }

    private void setDesc(String str) {
        if (!StringUtils.isEmpty((CharSequence) str)) {
            getDesc().setText(str);
            getDesc().setVisibility(0);
        }
    }

    private void setDesc() {
        setDesc(this.mStar.desc);
    }

    public void setStar(Star star) {
        this.mStar = star;
    }

    public Star getStar() {
        return this.mStar;
    }

    private void setImageBitmap() {
        loadBitmap(getImageView(), this.mStar.cover, this.mHandler);
    }

    public void setKeyWord(String keyWord) {
        this.mKeyWord = keyWord;
    }

    public void setData(Star star) {
        this.mStar = star;
        setTitle(this.mKeyWord);
        if (this.mStar == null) {
            setDefInfo();
            return;
        }
        setDetailTextWork();
        setDetailTextPlace();
        setDetailTextBirthday();
        setDetailTextHeight();
        setDesc();
        setImageBitmap();
    }

    public void setScaleDuration(int scaleDuration) {
        this.mScaleDuration = scaleDuration;
    }

    private static int getDimen(int id) {
        return ResourceUtil.getDimen(id);
    }

    private static String getUrlWithSize(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        int index = url.lastIndexOf(".");
        return index >= 0 ? index : url;
    }

    public void loadBitmap(ImageView iv, String coverUrl, final Handler handler) {
        if (StringUtils.isEmpty((CharSequence) coverUrl) || StringUtils.equals(coverUrl, this.mLastUrl)) {
            LogUtils.e(TAG, "loadDetailPhoto() -> coverUrl is null or coverUrl eques mLastUrl!");
            return;
        }
        this.mLastUrl = coverUrl;
        ImageRequest imageRequest = new ImageRequest(getUrlWithSize(coverUrl), iv);
        LogUtils.e(TAG, "loadDetailPhoto() -> coverUrl :" + getUrlWithSize(coverUrl));
        imageRequest.setImageType(ImageType.ROUND);
        imageRequest.setRadius((float) getDimen(R.dimen.dimen_150dp));
        imageRequest.setTargetHeight(getDimen(R.dimen.dimen_300dp));
        imageRequest.setTargetWidth(getDimen(R.dimen.dimen_300dp));
        ImageProviderApi.getImageProvider().loadImage(imageRequest, new IImageCallback() {
            public void onSuccess(ImageRequest imageRequest, final Bitmap bitmap) {
                if (bitmap == null) {
                    LogUtils.e(StarsInfoView.TAG, "loadDetailPhoto() -> onSuccess bitmap is null!");
                } else if (imageRequest.getCookie() == null) {
                    LogUtils.e(StarsInfoView.TAG, "loadDetailPhoto() -> onSuccess cookie is null!");
                } else {
                    final ImageView image = (ImageView) imageRequest.getCookie();
                    if (image != null) {
                        handler.post(new Runnable() {
                            public void run() {
                                image.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            }

            public void onFailure(ImageRequest request, Exception e) {
                LogUtils.e(StarsInfoView.TAG, "loadDetailPhoto() -> onFailure e:", e);
            }
        });
    }
}
