package com.tvos.appdetailpage.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.tvos.appdetailpage.utils.ResourcesUtils;

public class RatingBarView extends RelativeLayout {
    private static final int MAX_RATING_VALUE = 5;
    private Context mContext;
    private int mMaxStarNum;
    private float mProgressValue;
    private int mStarHeight;
    private int mStarWidth;

    public RatingBarView(Context context) {
        super(context);
        init(context);
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RatingBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
    }

    public void setParams(int starHeight, int starWidth, float progressValue, int maxStarNum) {
        this.mStarHeight = starHeight;
        this.mStarWidth = starWidth;
        this.mProgressValue = progressValue;
        this.mMaxStarNum = maxStarNum;
        initView();
    }

    private void initView() {
        int i;
        float f = 5.0f;
        removeAllViewsInLayout();
        int leftCount = 0;
        if (this.mProgressValue <= 5.0f) {
            f = this.mProgressValue;
        }
        this.mProgressValue = f;
        for (i = 0; i < ((int) this.mProgressValue); i++) {
            ImageView starImageView = new ImageView(this.mContext);
            starImageView.setBackgroundResource(getResId("drawable", "apps_star1"));
            LayoutParams starParams = new LayoutParams(this.mStarWidth, this.mStarHeight);
            starParams.leftMargin = this.mStarWidth * leftCount;
            leftCount++;
            addView(starImageView, starParams);
        }
        float decimal = this.mProgressValue - ((float) ((int) this.mProgressValue));
        if (decimal != 0.0f) {
            starImageView = new ImageView(this.mContext);
            starImageView.setBackgroundResource(getResId("drawable", "apps_star2"));
            starParams = new LayoutParams(this.mStarWidth, this.mStarHeight);
            if (((int) this.mProgressValue) == 0) {
                starParams.leftMargin = this.mStarWidth * leftCount;
            } else {
                leftCount++;
                starParams.leftMargin = (leftCount - 1) * this.mStarWidth;
            }
            addView(starImageView, starParams);
        }
        int greyRotate = (int) (((double) (this.mMaxStarNum - ((int) this.mProgressValue))) - Math.ceil((double) decimal));
        for (i = 0; i < greyRotate; i++) {
            starImageView = new ImageView(this.mContext);
            starImageView.setBackgroundResource(getResId("drawable", "apps_star3"));
            starParams = new LayoutParams(this.mStarWidth, this.mStarHeight);
            if (((int) this.mProgressValue) == 0 && decimal == 0.0f) {
                starParams.leftMargin = this.mStarWidth * leftCount;
                leftCount++;
            }
            if (((int) this.mProgressValue) == 0 && decimal != 0.0f) {
                leftCount++;
                starParams.leftMargin = this.mStarWidth * leftCount;
            }
            if (!(((int) this.mProgressValue) == 0 || decimal == 0.0f)) {
                leftCount++;
                starParams.leftMargin = (leftCount - 1) * this.mStarWidth;
            }
            if (((int) this.mProgressValue) != 0 && decimal == 0.0f) {
                leftCount++;
                starParams.leftMargin = (leftCount - 1) * this.mStarWidth;
            }
            addView(starImageView, starParams);
        }
    }

    private int getResId(String className, String name) {
        return ResourcesUtils.getResourceId(getContext(), className, name);
    }
}
