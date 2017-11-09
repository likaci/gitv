package com.gala.video.widget.episode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.tvapi.type.PayMarkType;
import com.gala.video.widget.ItemPopupWindow;
import com.gala.video.widget.ItemPopupWindow.VerticalPosition;
import com.gala.video.widget.util.AnimationUtils;
import com.gala.video.widget.util.DebugOptions;
import com.gala.video.widget.util.LogUtils;
import com.mcto.ads.internal.net.SendFlag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.xbill.DNS.WKSRecord.Service;

@SuppressLint({"ClickableViewAccessibility"})
public class EpisodeListView extends RelativeLayout implements EpisodeListViewManager, OnFocusChangeListener, OnClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gala$tvapi$type$PayMarkType = null;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gala$video$widget$episode$ParentLayoutMode = null;
    private static final AtomicInteger BASE_CHILD_LAYOUT_ID = new AtomicInteger(6636321);
    private static final AtomicInteger BASE_PARENT_LAYOUT_ID = new AtomicInteger(1118481);
    private static final String CHILD_VIEW_TAG_DISABLE = "disable";
    static final int COLOR_PARENT_TEXT_NORMAL_DEFAULT = -1;
    private static final int COLOR_TXT_FOCUSED_DEFAULT = -1;
    private static final int COLOR_TXT_NORMAL_DEFAULT = -1;
    private static final int COLOR_TXT_SELECTED_DEFAULT = -7681775;
    private static final boolean DEBUGMODE = DebugOptions.isInDebugMode();
    private static final AtomicInteger ITEM_BASE_ID_CHILD = new AtomicInteger(45756743);
    private static final AtomicInteger ITEM_BASE_ID_PARENT = new AtomicInteger(91513486);
    private final String TAG;
    private RelativeLayout childLayout;
    private List<CornerImageTextView> childList;
    private boolean mAutoFocusSelection;
    private Rect mBgDrawablePaddings;
    private int mChildHeight;
    private OnKeyListener mChildOnKeyListener;
    private int mChildSum;
    private int mChildTextSize;
    private int mChildWidth;
    private int mColorParentTextNormal;
    private int mColorTextDisable;
    private int mColorTextDisableFocus;
    private int mColorTextFocused;
    private int mColorTextNormal;
    private int mColorTextSelected;
    private Context mContext;
    private ArrayList<Integer> mCornerIconIndexList;
    private int mCornerIconResId;
    private int[] mCornerImgMargins;
    private Bitmap mCouponBitmap;
    private int mCurChildPage;
    private int mCurParentPage;
    private List<Integer> mDisableOrderList;
    private DissmissTipWindowRunnable mDissmissTipRunnable;
    private boolean mEnable;
    private FocusPosition mFocusPos;
    private int mFocusedChild;
    private int mFocusedEpisodeIndex;
    private boolean mFocusedParent;
    private GestureDetector mGestureDetector;
    private Handler mHandler;
    private boolean mIsHWAccelerated;
    private int mItemBgResId;
    private int mMarginleft;
    private int mNextFocusDownId;
    private int mNextFocusUpId;
    private OnEpisodeSlideListener mOnEpisodeSlideListener;
    private OnTouchListener mOnParentListener;
    private OnTouchListener mOnTouchListener;
    private int mParentHeight;
    private int mParentItemCountPerPage;
    private int mParentMarginTop;
    private int mParentSum;
    private int mParentSumPage;
    private int mParentTextSize;
    private int mParentWidth;
    private float mParentZoomRatio;
    private int mPrevChildPage;
    private View mRootView;
    private int mSelectedChild;
    private ShowTipWindowRunnable mShowTipLocRunnable;
    private ShowTipWindowRunnable mShowTipRunnable;
    private Bitmap mSinglePayBitmap;
    private Rect mTempRect;
    private int mTipOffsetXOfAnchorView;
    private int mTipOffsetYOfAnchorView;
    private VerticalPosition mTipShowLoc;
    private int mTipsBgResId;
    private HashMap<Integer, String> mTipsContentHashMap;
    private int mTipsMaxTextNum;
    private int mTipsTextColor;
    private int mTipsTextSize;
    private ItemPopupWindow mTipsWindow;
    private Bitmap mTopRightBitmap;
    private Bitmap mVIPBitmap;
    private int mVipIconResId;
    private int[] mVipImgMargins;
    private HashMap<Integer, PayMarkType> mVipOrderList;
    private boolean mZoomEnabled;
    private OnEpisodeClickListener onEpisodeClickListener;
    private OnEpisodeFocusChangeListener onEpisodeFocusChangeListener;
    private RelativeLayout parentLayout;
    private List<CornerImageTextView> parentList;
    private float x1;
    private float x2;
    private float x3;
    private float x4;

    public interface OnEpisodeClickListener {
        void onEpisodeClick(View view, int i);
    }

    public interface OnEpisodeFocusChangeListener {
        void onEpisodeFocus(int i);
    }

    class DissmissTipWindowRunnable implements Runnable {
        DissmissTipWindowRunnable() {
        }

        public void run() {
            if (EpisodeListView.this.mTipsWindow != null) {
                LogUtils.d(EpisodeListView.this.TAG, "real dismiss");
                EpisodeListView.this.mTipsWindow.dismiss();
            }
        }
    }

    private enum FocusPosition {
        LOST,
        CHILD,
        PARENT
    }

    class LearnGestureListener extends SimpleOnGestureListener {
        LearnGestureListener() {
        }

        public boolean onSingleTapUp(MotionEvent ev) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onSingleTapUp-->" + ev.toString());
            }
            return false;
        }

        public void onShowPress(MotionEvent ev) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onShowPress-->" + ev.toString());
            }
        }

        public void onLongPress(MotionEvent ev) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onLongPress-->" + ev.toString());
            }
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onScroll-->" + e1.toString());
            }
            if (e1.getX() - e2.getX() > 50.0f) {
                if (EpisodeListView.this.mOnEpisodeSlideListener != null) {
                    EpisodeListView.this.mOnEpisodeSlideListener.onEpisodeSlide(1);
                }
                EpisodeListView.this.childLeftKey();
            } else if (e2.getX() - e1.getX() > 50.0f) {
                if (EpisodeListView.this.mOnEpisodeSlideListener != null) {
                    EpisodeListView.this.mOnEpisodeSlideListener.onEpisodeSlide(1);
                }
                EpisodeListView.this.childRightKey();
            }
            return false;
        }

        public boolean onDown(MotionEvent ev) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onDownd-->" + ev.toString());
            }
            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onFling-->" + e1.toString());
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeListView.this.TAG, "onFling-->" + e2.toString());
            }
            if (e1.getX() - e2.getX() > 50.0f && Math.abs(velocityX) > 50.0f) {
                if (EpisodeListView.this.mOnEpisodeSlideListener != null) {
                    EpisodeListView.this.mOnEpisodeSlideListener.onEpisodeSlide(1);
                }
                EpisodeListView.this.childLeftKey();
            } else if (e2.getX() - e1.getX() > 50.0f && Math.abs(velocityX) > 50.0f) {
                if (EpisodeListView.this.mOnEpisodeSlideListener != null) {
                    EpisodeListView.this.mOnEpisodeSlideListener.onEpisodeSlide(1);
                }
                EpisodeListView.this.childRightKey();
            }
            return false;
        }
    }

    public interface OnEpisodeSlideListener {
        void onEpisodeSlide(int i);
    }

    class ShowTipWindowRunnable implements Runnable {
        private int mIndex;
        private View mView;

        public ShowTipWindowRunnable(View view, int index) {
            this.mIndex = index;
            this.mView = view;
        }

        public void run() {
            if (EpisodeListView.this.mTipsWindow != null && EpisodeListView.this.mTipsContentHashMap != null && !EpisodeListView.this.mTipsContentHashMap.isEmpty()) {
                if (this.mView instanceof EpisodeListView) {
                    EpisodeListView.this.mTipsWindow.show(this.mView, (String) EpisodeListView.this.mTipsContentHashMap.get(Integer.valueOf(this.mIndex)), EpisodeListView.this.mTipOffsetXOfAnchorView, EpisodeListView.this.mTipOffsetYOfAnchorView);
                } else {
                    EpisodeListView.this.mTipsWindow.show(this.mView, (String) EpisodeListView.this.mTipsContentHashMap.get(Integer.valueOf(this.mIndex)), -((EpisodeListView.this.getBgDrawablePaddings().top * 5) / 4));
                }
            }
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gala$tvapi$type$PayMarkType() {
        int[] iArr = $SWITCH_TABLE$com$gala$tvapi$type$PayMarkType;
        if (iArr == null) {
            iArr = new int[PayMarkType.values().length];
            try {
                iArr[PayMarkType.COUPONS_ON_DEMAND_MARK.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PayMarkType.DEFAULT.ordinal()] = 5;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PayMarkType.NONE_MARK.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PayMarkType.PAY_ON_DEMAND_MARK.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[PayMarkType.VIP_MARK.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$gala$tvapi$type$PayMarkType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gala$video$widget$episode$ParentLayoutMode() {
        int[] iArr = $SWITCH_TABLE$com$gala$video$widget$episode$ParentLayoutMode;
        if (iArr == null) {
            iArr = new int[ParentLayoutMode.values().length];
            try {
                iArr[ParentLayoutMode.CUSTOM.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ParentLayoutMode.DOUBLE_CHILD_WIDTH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ParentLayoutMode.SINGLE_CHILD_WIDTH.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$gala$video$widget$episode$ParentLayoutMode = iArr;
        }
        return iArr;
    }

    public EpisodeListView(Context context) {
        super(context);
        this.childList = new ArrayList();
        this.parentList = new ArrayList();
        this.mCornerIconResId = Integer.MIN_VALUE;
        this.mTipOffsetXOfAnchorView = -1;
        this.mTipOffsetYOfAnchorView = -1;
        this.mCurChildPage = 1;
        this.mCurParentPage = 1;
        this.mParentSumPage = 1;
        this.mFocusedChild = -1;
        this.mFocusedParent = false;
        this.mTempRect = new Rect();
        this.mItemBgResId = Integer.MIN_VALUE;
        this.mCornerImgMargins = new int[4];
        this.mVipImgMargins = new int[2];
        this.onEpisodeClickListener = null;
        this.onEpisodeFocusChangeListener = null;
        this.mOnEpisodeSlideListener = null;
        this.mParentItemCountPerPage = 5;
        this.mZoomEnabled = true;
        this.mTipsTextColor = -16777216;
        this.mTipsMaxTextNum = 9;
        this.mTipsBgResId = Integer.MIN_VALUE;
        this.mTipsWindow = null;
        this.mColorTextNormal = -1;
        this.mColorTextSelected = COLOR_TXT_SELECTED_DEFAULT;
        this.mColorTextFocused = -1;
        this.mColorParentTextNormal = -1;
        this.mTipShowLoc = VerticalPosition.DROPUP;
        this.mRootView = null;
        this.mFocusPos = FocusPosition.LOST;
        this.x1 = 0.0f;
        this.x2 = 0.0f;
        this.x3 = 0.0f;
        this.x4 = 0.0f;
        this.mChildOnKeyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode != 23 && keyCode != 66) || event.getAction() != 0 || event.getRepeatCount() != 0) {
                    return false;
                }
                EpisodeListView.this.onClick(v);
                return true;
            }
        };
        this.mOnTouchListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    EpisodeListView.this.x1 = event.getX();
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "childContainer action down");
                    }
                } else if (event.getAction() == 2) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "childContainer action move");
                    }
                } else if (event.getAction() == 1) {
                    EpisodeListView.this.x2 = event.getX();
                    if (EpisodeListView.this.x1 - EpisodeListView.this.x2 > 50.0f) {
                        EpisodeListView.this.childRightKey();
                    } else if (EpisodeListView.this.x2 - EpisodeListView.this.x1 > 50.0f) {
                        EpisodeListView.this.childLeftKey();
                    }
                    if (EpisodeListView.this.mOnEpisodeSlideListener != null) {
                        EpisodeListView.this.mOnEpisodeSlideListener.onEpisodeSlide(1);
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "childContainer action up x1,x2 (" + EpisodeListView.this.x1 + "," + EpisodeListView.this.x2);
                    }
                }
                return false;
            }
        };
        this.mOnParentListener = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    EpisodeListView.this.x3 = event.getX();
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "parentContainer action down");
                    }
                } else if (event.getAction() == 2) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "parentContainer action move");
                    }
                } else if (event.getAction() == 1) {
                    EpisodeListView.this.x4 = event.getX();
                    if (EpisodeListView.this.x3 - EpisodeListView.this.x4 <= 50.0f) {
                    }
                    if (EpisodeListView.this.mOnEpisodeSlideListener != null) {
                        EpisodeListView.this.mOnEpisodeSlideListener.onEpisodeSlide(-1);
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "parentContainer action up x3,x4 (" + EpisodeListView.this.x3 + "," + EpisodeListView.this.x4);
                    }
                }
                return false;
            }
        };
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mPrevChildPage = this.mCurChildPage;
        this.mFocusedEpisodeIndex = -1;
        this.mVipIconResId = Integer.MIN_VALUE;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mContext = context;
        this.TAG = "gridpageview/EpisodeListView@" + Integer.toHexString(hashCode());
        if (DEBUGMODE) {
            setBackgroundColor(DebugOptions.DEBUG_BG_COLOR);
        }
    }

    public EpisodeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.childList = new ArrayList();
        this.parentList = new ArrayList();
        this.mCornerIconResId = Integer.MIN_VALUE;
        this.mTipOffsetXOfAnchorView = -1;
        this.mTipOffsetYOfAnchorView = -1;
        this.mCurChildPage = 1;
        this.mCurParentPage = 1;
        this.mParentSumPage = 1;
        this.mFocusedChild = -1;
        this.mFocusedParent = false;
        this.mTempRect = new Rect();
        this.mItemBgResId = Integer.MIN_VALUE;
        this.mCornerImgMargins = new int[4];
        this.mVipImgMargins = new int[2];
        this.onEpisodeClickListener = null;
        this.onEpisodeFocusChangeListener = null;
        this.mOnEpisodeSlideListener = null;
        this.mParentItemCountPerPage = 5;
        this.mZoomEnabled = true;
        this.mTipsTextColor = -16777216;
        this.mTipsMaxTextNum = 9;
        this.mTipsBgResId = Integer.MIN_VALUE;
        this.mTipsWindow = null;
        this.mColorTextNormal = -1;
        this.mColorTextSelected = COLOR_TXT_SELECTED_DEFAULT;
        this.mColorTextFocused = -1;
        this.mColorParentTextNormal = -1;
        this.mTipShowLoc = VerticalPosition.DROPUP;
        this.mRootView = null;
        this.mFocusPos = FocusPosition.LOST;
        this.x1 = 0.0f;
        this.x2 = 0.0f;
        this.x3 = 0.0f;
        this.x4 = 0.0f;
        this.mChildOnKeyListener = /* anonymous class already generated */;
        this.mOnTouchListener = /* anonymous class already generated */;
        this.mOnParentListener = /* anonymous class already generated */;
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mPrevChildPage = this.mCurChildPage;
        this.mFocusedEpisodeIndex = -1;
        this.mVipIconResId = Integer.MIN_VALUE;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.TAG = "gridpageview/EpisodeListView@" + Integer.toHexString(hashCode());
        this.mContext = context;
        if (DEBUGMODE) {
            setBackgroundColor(DebugOptions.DEBUG_BG_COLOR);
        }
    }

    public EpisodeListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.childList = new ArrayList();
        this.parentList = new ArrayList();
        this.mCornerIconResId = Integer.MIN_VALUE;
        this.mTipOffsetXOfAnchorView = -1;
        this.mTipOffsetYOfAnchorView = -1;
        this.mCurChildPage = 1;
        this.mCurParentPage = 1;
        this.mParentSumPage = 1;
        this.mFocusedChild = -1;
        this.mFocusedParent = false;
        this.mTempRect = new Rect();
        this.mItemBgResId = Integer.MIN_VALUE;
        this.mCornerImgMargins = new int[4];
        this.mVipImgMargins = new int[2];
        this.onEpisodeClickListener = null;
        this.onEpisodeFocusChangeListener = null;
        this.mOnEpisodeSlideListener = null;
        this.mParentItemCountPerPage = 5;
        this.mZoomEnabled = true;
        this.mTipsTextColor = -16777216;
        this.mTipsMaxTextNum = 9;
        this.mTipsBgResId = Integer.MIN_VALUE;
        this.mTipsWindow = null;
        this.mColorTextNormal = -1;
        this.mColorTextSelected = COLOR_TXT_SELECTED_DEFAULT;
        this.mColorTextFocused = -1;
        this.mColorParentTextNormal = -1;
        this.mTipShowLoc = VerticalPosition.DROPUP;
        this.mRootView = null;
        this.mFocusPos = FocusPosition.LOST;
        this.x1 = 0.0f;
        this.x2 = 0.0f;
        this.x3 = 0.0f;
        this.x4 = 0.0f;
        this.mChildOnKeyListener = /* anonymous class already generated */;
        this.mOnTouchListener = /* anonymous class already generated */;
        this.mOnParentListener = /* anonymous class already generated */;
        this.mNextFocusUpId = -1;
        this.mNextFocusDownId = -1;
        this.mPrevChildPage = this.mCurChildPage;
        this.mFocusedEpisodeIndex = -1;
        this.mVipIconResId = Integer.MIN_VALUE;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mContext = context;
        this.TAG = "gridpageview/EpisodeListView@" + Integer.toHexString(hashCode());
        if (DEBUGMODE) {
            setBackgroundColor(DebugOptions.DEBUG_BG_COLOR);
        }
    }

    public void setItemBackgroundResource(int resId) {
        this.mBgDrawablePaddings = null;
        this.mItemBgResId = resId;
    }

    public void setDimens(int[] dimens, int itemSpacingPx) {
        if (this.mItemBgResId == Integer.MIN_VALUE) {
            throw new IllegalStateException("Item background resource must be set before calling this method!");
        } else if (dimens == null || dimens.length != 3) {
            throw new IllegalArgumentException("Please provide exactly 3 parameters for setDimen()!");
        } else {
            this.mChildTextSize = getDimensionPixelSize(dimens[0]);
            this.mParentTextSize = this.mChildTextSize;
            this.mChildWidth = dimens[1];
            this.mChildHeight = dimens[2];
            int ninePatchPaddingLeft = getBgDrawablePaddings().left;
            int topWPadded = this.mChildWidth + (ninePatchPaddingLeft * 2);
            int zoomInSpace = getZoomInSpace(topWPadded, this.mChildHeight + (getBgDrawablePaddings().top * 2));
            this.mParentWidth = (((topWPadded * 2) + zoomInSpace) - (ninePatchPaddingLeft * 2)) + itemSpacingPx;
            this.mParentHeight = this.mChildHeight;
            this.mParentMarginTop = zoomInSpace + itemSpacingPx;
            this.mParentZoomRatio = ((AnimationUtils.getDefaultZoomRatio() - 1.0f) / (((float) getParentWidthPadded()) / ((float) getChildWidthPadded()))) + 1.0f;
            this.mMarginleft = zoomInSpace + itemSpacingPx;
        }
    }

    public void setDimens(DimensParamBuilder builder) {
        if (this.mItemBgResId == Integer.MIN_VALUE) {
            throw new IllegalStateException("Item background resource must be set before calling this method!");
        } else if (builder == null || !builder.isValid()) {
            throw new IllegalArgumentException("Please provide at least 4 valid parameters for setDimen()!");
        } else {
            this.mChildTextSize = getDimensionPixelSize(builder.mChildTextSizeResId);
            this.mChildWidth = builder.mChildWidth;
            this.mChildHeight = builder.mChildHeight;
            int itemSpacing = builder.mItemSpacing;
            this.mParentHeight = builder.mParentHeight > 0 ? builder.mParentHeight : this.mChildHeight;
            this.mParentTextSize = builder.mParentTextSizeResId != 0 ? getDimensionPixelSize(builder.mParentTextSizeResId) : this.mChildTextSize;
            int zoomInSpace = getZoomInSpace(this.mChildWidth + (getBgDrawablePaddings().left * 2), this.mChildHeight + (getBgDrawablePaddings().top * 2));
            ParentLayoutMode layoutMode = builder.mParentLayoutMode;
            switch ($SWITCH_TABLE$com$gala$video$widget$episode$ParentLayoutMode()[layoutMode.ordinal()]) {
                case 1:
                    this.mParentWidth = this.mChildWidth;
                    this.mParentItemCountPerPage = 10;
                    break;
                case 2:
                    this.mParentWidth = getDefaultParentWidth(this.mChildWidth, this.mChildHeight, itemSpacing);
                    this.mParentItemCountPerPage = 5;
                    break;
                default:
                    throw new IllegalArgumentException("current layout mode <" + layoutMode + "> is not supported");
            }
            this.mParentMarginTop = zoomInSpace + itemSpacing;
            this.mParentZoomRatio = ((AnimationUtils.getDefaultZoomRatio() - 1.0f) / (((float) getParentWidthPadded()) / ((float) getChildWidthPadded()))) + 1.0f;
            this.mMarginleft = zoomInSpace + itemSpacing;
        }
    }

    private int getDefaultParentWidth(int childWidth, int childHeight, int itemSpacing) {
        int ninePatchPaddingLeft = getBgDrawablePaddings().left;
        int topWPadded = childWidth + (ninePatchPaddingLeft * 2);
        return (((topWPadded * 2) + getZoomInSpace(topWPadded, childHeight + (getBgDrawablePaddings().top * 2))) - (ninePatchPaddingLeft * 2)) + itemSpacing;
    }

    public void setCornerImgMargins(int[] margins) {
        if (margins == null || margins.length != 4) {
            throw new IllegalArgumentException("Please provide exactly 4 parameters for setCornerImgMargins()!");
        }
        this.mCornerImgMargins = Arrays.copyOf(margins, margins.length);
    }

    public void setVipImgMargins(int left, int top, int right, int bottom) {
        this.mVipImgMargins[0] = left;
        this.mVipImgMargins[1] = top;
    }

    @Deprecated
    public void setItemTextStyle(int textColorNormal, int textColorSelected) {
        this.mColorTextNormal = textColorNormal;
        this.mColorTextSelected = textColorSelected;
        this.mColorParentTextNormal = this.mColorTextNormal;
    }

    @Deprecated
    public void setItemTextStyle(int textColorNormal, int textColorSelected, int textColorFocused) {
        this.mColorTextNormal = textColorNormal;
        this.mColorTextSelected = textColorSelected;
        this.mColorTextFocused = textColorFocused;
        this.mColorParentTextNormal = this.mColorTextNormal;
    }

    public void setItemTextStyle(ItemStyleParamBuilder builder) {
        this.mColorTextNormal = builder.getTextNormalColor();
        this.mColorTextSelected = builder.getTextSelectedColor();
        this.mColorTextFocused = builder.getTextFocusedColor();
        this.mColorParentTextNormal = builder.getParentTextNormalColor();
    }

    public void setItemDisableTextStyle(int textColorDisable, int textColorDisableFocus) {
        this.mColorTextDisable = textColorDisable;
        this.mColorTextDisableFocus = textColorDisableFocus;
    }

    public void setDataSource(int childSum, int selectedChild) {
        this.mChildSum = childSum;
        this.mFocusedChild = -1;
        if (selectedChild < 0) {
            this.mSelectedChild = 0;
        } else if (selectedChild > childSum - 1) {
            this.mSelectedChild = childSum - 1;
        } else {
            this.mSelectedChild = selectedChild;
        }
        init(this.mContext);
        setSelectedChild(this.mSelectedChild);
    }

    public void setDataSource(int childSum) {
        this.mChildSum = childSum;
        init(this.mContext);
    }

    public void updateDataSource(int childSum) {
        int i;
        LogUtils.d(this.TAG, "updateDataSource childSum" + childSum);
        LogUtils.d(this.TAG, "updateDataSource mTipsWindow" + this.mTipsWindow);
        this.mChildSum = childSum;
        if (this.mChildSum % 10 == 0) {
            i = this.mChildSum / 10;
        } else {
            i = (this.mChildSum / 10) + 1;
        }
        this.mParentSum = i;
        if (this.mParentSum % this.mParentItemCountPerPage == 0) {
            i = this.mParentSum / this.mParentItemCountPerPage;
        } else {
            i = (this.mParentSum / this.mParentItemCountPerPage) + 1;
        }
        this.mParentSumPage = i;
        refreshParentData(this.mCurParentPage);
        refreshChildData(this.mCurChildPage);
        LogUtils.d(this.TAG, "updateDataSource mTipsWindow" + this.mTipsWindow);
    }

    public void setAutoFocusSelection(boolean autoFocusSelection) {
        this.mAutoFocusSelection = autoFocusSelection;
    }

    private void init(Context context) {
        if (this.mItemBgResId == 0) {
            throw new IllegalStateException("please invoke setChildBackgroundResource(), setDimens first!");
        }
        int i;
        removeAllViews();
        this.parentList.clear();
        this.childList.clear();
        setFocusable(true);
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        if (this.mChildSum % 10 == 0) {
            i = this.mChildSum / 10;
        } else {
            i = (this.mChildSum / 10) + 1;
        }
        this.mParentSum = i;
        if (this.mParentSum % this.mParentItemCountPerPage == 0) {
            i = this.mParentSum / this.mParentItemCountPerPage;
        } else {
            i = (this.mParentSum / this.mParentItemCountPerPage) + 1;
        }
        this.mParentSumPage = i;
        int childs = Math.min(10, this.mChildSum);
        int parents = Math.min(this.mParentItemCountPerPage, this.mParentSum);
        LayoutParams containerParams = new LayoutParams(-2, -2);
        setFocusable(true);
        int childLayoutId = BASE_CHILD_LAYOUT_ID.getAndIncrement();
        this.childLayout = new RelativeLayout(context);
        this.childLayout.setId(childLayoutId);
        LayoutParams childLayoutParams = new LayoutParams(-2, -2);
        initChildLayout(context, childs, this.childLayout);
        this.parentLayout = new RelativeLayout(context);
        this.parentLayout.setId(BASE_PARENT_LAYOUT_ID.getAndIncrement());
        LayoutParams parentLayoutParams = new LayoutParams(-2, -2);
        parentLayoutParams.setMargins(0, this.mParentMarginTop, 0, 0);
        parentLayoutParams.addRule(3, childLayoutId);
        initParentLayout(context, parents, this.parentLayout);
        this.childLayout.setNextFocusDownId(this.parentLayout.getId());
        this.childLayout.setNextFocusLeftId(this.childLayout.getId());
        this.childLayout.setNextFocusRightId(this.childLayout.getId());
        this.parentLayout.setNextFocusUpId(this.childLayout.getId());
        this.parentLayout.setNextFocusLeftId(this.parentLayout.getId());
        this.parentLayout.setNextFocusRightId(this.parentLayout.getId());
        this.parentLayout.setOnTouchListener(this.mOnParentListener);
        if (this.mNextFocusDownId != -1) {
            setNextFocusDownId(this.mNextFocusDownId);
        }
        if (this.mNextFocusUpId != -1) {
            setNextFocusUpId(this.mNextFocusUpId);
        }
        if (this.mZoomEnabled) {
            int itemBgpaddingLeft = getBgDrawablePaddings().left;
            int itemBgpaddingTop = getBgDrawablePaddings().top;
            childLayoutParams.leftMargin -= itemBgpaddingLeft;
            parentLayoutParams.leftMargin -= itemBgpaddingLeft;
            childLayoutParams.topMargin -= itemBgpaddingTop;
        }
        addView(this.childLayout, childLayoutParams);
        addView(this.parentLayout, parentLayoutParams);
        if (this.mZoomEnabled) {
            for (ViewParent parent = this; parent instanceof ViewGroup; parent = parent.getParent()) {
                ((ViewGroup) parent).setClipChildren(false);
                ((ViewGroup) parent).setClipToPadding(false);
            }
        }
        this.mGestureDetector = new GestureDetector(this.mContext, new LearnGestureListener());
    }

    private void initChildLayout(Context context, int childs, RelativeLayout childLayout) {
        for (int i = 0; i < 10; i++) {
            int childId = ITEM_BASE_ID_CHILD.getAndIncrement();
            CornerImageTextView childView = new CornerImageTextView(context);
            childView.setId(childId);
            childView.setFocusable(true);
            childView.setOnFocusChangeListener(this);
            childView.setOnKeyListener(this.mChildOnKeyListener);
            childView.setOnTouchListener(this.mOnTouchListener);
            childView.setBackgroundDrawable(this.mContext.getResources().getDrawable(this.mItemBgResId));
            childView.setRequestFocusDelegator(new RequestFocusDelegator() {
                public boolean requestFocus(CornerImageTextView view, int direction, Rect previouslyFocusedRect) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "childView.requestFocus, direction=" + direction + ", view=" + view);
                    }
                    View globalLastFocusView = EpisodeListView.this.findRootView().findFocus();
                    if ((Service.CISCO_FNA != direction || EpisodeListView.this.isInViewGroup(EpisodeListView.this, globalLastFocusView)) && !(33 == direction && EpisodeListView.this.isInViewGroup(EpisodeListView.this, globalLastFocusView))) {
                        if (17 == direction || 66 == direction || 2 == direction || 1 == direction) {
                            return view.superRequestFocus(direction, previouslyFocusedRect);
                        }
                        return EpisodeListView.this.requestParentFocus((EpisodeListView.this.mCurChildPage - 1) % EpisodeListView.this.mParentItemCountPerPage, direction);
                    } else if (view == EpisodeListView.this.getDesiredFocusChild()) {
                        return view.superRequestFocus(direction, previouslyFocusedRect);
                    } else {
                        return EpisodeListView.this.getDesiredFocusChild().requestFocus(direction);
                    }
                }
            });
            if (this.mIsHWAccelerated) {
                childView.setLayerType(2, null);
            }
            childView.setTextColor(this.mColorTextNormal);
            childView.setTextSize(0, (float) this.mChildTextSize);
            childView.setGravity(17);
            LayoutParams childParams = new LayoutParams(getChildWidthPadded(), getChildHeightPadded());
            childParams.addRule(10);
            if (i > 0) {
                childParams.setMargins(this.mMarginleft, 0, 0, 0);
                childParams.addRule(1, childId - 1);
            }
            childView.setCornerImagePadding(getBgDrawablePaddings());
            childView.setCornerImageMargin(this.mVipImgMargins[0], 0, 0, 0);
            childLayout.addView(childView, childParams);
            if (i >= childs) {
                childView.setVisibility(8);
            } else {
                childView.setVisibility(0);
            }
            this.childList.add(childView);
        }
        if (this.mZoomEnabled) {
            childLayout.setClipChildren(false);
        }
    }

    private void initParentLayout(Context context, int parents, RelativeLayout parentLayout) {
        for (int i = 0; i < this.mParentItemCountPerPage; i++) {
            int parentId = ITEM_BASE_ID_PARENT.getAndIncrement();
            CornerImageTextView parent = new CornerImageTextView(context);
            if (this.mIsHWAccelerated) {
                parent.setLayerType(2, null);
            }
            parent.setTextColor(this.mColorParentTextNormal);
            parent.setId(parentId);
            parent.setFocusable(true);
            parent.setTextSize(0, (float) this.mParentTextSize);
            parent.setGravity(17);
            parent.setBackgroundDrawable(this.mContext.getResources().getDrawable(this.mItemBgResId));
            parent.setOnFocusChangeListener(this);
            parent.setOnClickListener(this);
            parent.setOnTouchListener(this.mOnParentListener);
            parent.setRequestFocusDelegator(new RequestFocusDelegator() {
                public boolean requestFocus(CornerImageTextView view, int direction, Rect previouslyFocusedRect) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(EpisodeListView.this.TAG, "parentView.requestFocus, direction=" + direction + ", view=" + view);
                    }
                    View globalLastFocusView = EpisodeListView.this.findRootView().findFocus();
                    if (!(Service.CISCO_FNA == direction && EpisodeListView.this.isInViewGroup(EpisodeListView.this, globalLastFocusView)) && (33 != direction || EpisodeListView.this.isInViewGroup(EpisodeListView.this, globalLastFocusView))) {
                        if (17 == direction || 66 == direction || 2 == direction || 1 == direction) {
                            return view.superRequestFocus(direction, previouslyFocusedRect);
                        }
                        return EpisodeListView.this.resetNextFocus();
                    } else if (view == EpisodeListView.this.getDesiredFocusParent()) {
                        return view.superRequestFocus(direction, previouslyFocusedRect);
                    } else {
                        return EpisodeListView.this.getDesiredFocusParent().requestFocus(direction);
                    }
                }
            });
            LayoutParams parentParams = new LayoutParams(getParentWidthPadded(), getParentHeightPadded());
            parentParams.addRule(10);
            if (i > 0) {
                parentParams.setMargins(this.mMarginleft, 0, 0, 0);
                parentParams.addRule(1, parentId - 1);
            }
            if (i >= parents) {
                parent.setVisibility(8);
            } else {
                parent.setVisibility(0);
            }
            parentLayout.addView(parent, parentParams);
            this.parentList.add(parent);
        }
        if (this.mZoomEnabled) {
            parentLayout.setClipChildren(false);
        }
    }

    private boolean isInViewGroup(ViewGroup group, View v) {
        int i = 0;
        int size = this.childList.size();
        while (i < size) {
            if (v != this.childList.get(i)) {
                i++;
            } else if (!LogUtils.mIsDebug) {
                return true;
            } else {
                LogUtils.d(this.TAG, "isInViewGroup = true, inner child layout.");
                return true;
            }
        }
        i = 0;
        size = this.parentList.size();
        while (i < size) {
            if (v != this.parentList.get(i)) {
                i++;
            } else if (!LogUtils.mIsDebug) {
                return true;
            } else {
                LogUtils.d(this.TAG, "isInViewGroup = true, inner parent layout.");
                return true;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "isInViewGroup = false");
        }
        return false;
    }

    private View findRootView() {
        if (this.mRootView == null) {
            for (ViewParent parent = this; parent instanceof ViewGroup; parent = parent.getParent()) {
                this.mRootView = (View) parent;
            }
        }
        return this.mRootView;
    }

    private int getDimensionPixelSize(int resId) {
        return this.mContext.getResources().getDimensionPixelSize(resId);
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        this.mNextFocusUpId = nextFocusUpId;
        LogUtils.i(this.TAG, "setNextFocusUpId: applying id:" + nextFocusUpId);
        if (nextFocusUpId == getId()) {
            for (View child : this.childList) {
                child.setNextFocusUpId(child.getId());
            }
            return;
        }
        for (View child2 : this.childList) {
            child2.setNextFocusUpId(nextFocusUpId);
        }
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        this.mNextFocusDownId = nextFocusDownId;
        LogUtils.i(this.TAG, "setNextFocusDownId: applying id:" + nextFocusDownId);
        if (nextFocusDownId == getId()) {
            for (View parent : this.parentList) {
                parent.setNextFocusDownId(parent.getId());
            }
            return;
        }
        for (View parent2 : this.parentList) {
            parent2.setNextFocusDownId(nextFocusDownId);
        }
    }

    private View getDesiredFocusParent() {
        return (View) this.parentList.get((this.mCurChildPage - 1) % this.mParentItemCountPerPage);
    }

    private View getDesiredFocusChild() {
        LogUtils.d(this.TAG, "getDesiredFocusChild");
        if (this.mFocusedChild >= 0) {
            return (View) this.childList.get(this.mFocusedChild);
        }
        int selectItem = this.mSelectedChild % 10;
        if (this.mCurChildPage == (this.mSelectedChild / 10) + 1) {
            return (View) this.childList.get(selectItem);
        }
        return (View) this.childList.get(0);
    }

    public boolean resetNextFocus() {
        LogUtils.d(this.TAG, "resetNextFocus");
        if (this.mFocusedChild >= 0) {
            LogUtils.d(this.TAG, "resetNextFocus: focus goes to mFocusedChild(" + this.mFocusedChild + ")");
            return requestChildFocus(this.mFocusedChild, 2);
        }
        int selectItem = this.mSelectedChild % 10;
        if (this.mCurChildPage == (this.mSelectedChild / 10) + 1) {
            return requestChildFocus(selectItem, 2);
        }
        return requestChildFocus(0, 2);
    }

    public void resetSelectedChild(int child) {
        LogUtils.d(this.TAG, ">>resetSelectedChild: child = " + child + " mCurChildPage = " + this.mCurChildPage);
        if (child < 0) {
            child = 0;
        }
        this.mSelectedChild = child;
        refreshChildData(this.mCurChildPage);
    }

    public void requestChildFocus(long selectedChild, int focusDirection) {
        requestChildFocus(selectedChild % 10, focusDirection);
    }

    private boolean requestChildFocus(int childPos, int focusDirection) {
        CornerImageTextView childView = (CornerImageTextView) this.childList.get(childPos);
        boolean hasFocus = childView.hasFocus();
        LogUtils.d(this.TAG, "requestChildFocus: " + childPos + ", {" + childView.getId() + "}, hasFocus=" + hasFocus);
        if (childView.requestFocus(focusDirection)) {
            LogUtils.d(this.TAG, "requestFocus() success");
            return !hasFocus;
        } else {
            LogUtils.d(this.TAG, "requestFocus() failed");
            return hasFocus;
        }
    }

    private boolean requestParentFocus(int parentPos, int direction) {
        CornerImageTextView parentView = (CornerImageTextView) this.parentList.get(parentPos);
        LogUtils.d(this.TAG, "requestParentFocus: " + parentPos + ", {" + parentView.getId() + "}, hasFocus=" + parentView.hasFocus());
        return parentView.requestFocus(direction);
    }

    private int getChildFocusPos() {
        int childSize = getChildSize(this.mCurChildPage);
        for (int i = 0; i < childSize; i++) {
            if (((CornerImageTextView) this.childList.get(i)).hasFocus()) {
                return ((this.mCurChildPage - 1) * 10) + i;
            }
        }
        return 0;
    }

    private boolean isChildFocus() {
        int childSize = getChildSize(this.mCurChildPage);
        for (int i = 0; i < childSize; i++) {
            if (((CornerImageTextView) this.childList.get(i)).hasFocus()) {
                LogUtils.e(this.TAG, "isChildFocus: focused child=" + ((CornerImageTextView) this.childList.get(i)).getId());
                return true;
            }
        }
        return false;
    }

    private boolean isParentFocus() {
        int parentSize = getParentSize(this.mCurParentPage);
        for (int i = 0; i < parentSize; i++) {
            if (((CornerImageTextView) this.parentList.get(i)).hasFocus()) {
                LogUtils.e(this.TAG, "isParentFocus: focused parent=" + ((CornerImageTextView) this.parentList.get(i)).getId());
                return true;
            }
        }
        return false;
    }

    public int getSelectedChild() {
        return this.mSelectedChild;
    }

    public void setSelectedChild(int child) {
        LogUtils.e(this.TAG, ">>setSelectedChild" + child + " mCurChildPage = " + this.mCurChildPage + " mCurParentPage =" + this.mCurParentPage + " mChildSum = " + this.mChildSum);
        if (child + 1 <= this.mChildSum) {
            if (child < 0) {
                child = 0;
            }
            this.mSelectedChild = child;
            this.mPrevChildPage = this.mCurChildPage;
            this.mCurChildPage = (this.mSelectedChild / 10) + 1;
            refreshChildData(this.mCurChildPage);
            this.mCurParentPage = this.mCurChildPage % this.mParentItemCountPerPage == 0 ? this.mCurChildPage / this.mParentItemCountPerPage : (this.mCurChildPage / this.mParentItemCountPerPage) + 1;
            refreshParentData(this.mCurParentPage);
        }
    }

    private int getChildSize(int page) {
        if (page < this.mParentSum) {
            return 10;
        }
        if (this.mChildSum == 0 || this.mChildSum % 10 != 0) {
            return this.mChildSum % 10;
        }
        return 10;
    }

    private int getParentSize(int page) {
        if (page < this.mParentSumPage) {
            return this.mParentItemCountPerPage;
        }
        if (this.mParentSum == 0 || this.mParentSum % this.mParentItemCountPerPage != 0) {
            return this.mParentSum % this.mParentItemCountPerPage;
        }
        return this.mParentItemCountPerPage;
    }

    private void refreshChildData(int page) {
        LogUtils.d(this.TAG, "refreshChildData" + page);
        if (page <= this.mParentSum + 1) {
            int i;
            if (this.mDisableOrderList != null) {
                for (View view : this.childList) {
                    view.setTag("");
                }
                if (!this.mDisableOrderList.isEmpty()) {
                    int orderSize = this.mDisableOrderList.size();
                    for (i = 0; i < orderSize; i++) {
                        int order = ((Integer) this.mDisableOrderList.get(i)).intValue();
                        int index = (order - 1) - ((page - 1) * 10);
                        if (index >= 0 && index < this.childList.size()) {
                            LogUtils.d(this.TAG, "refreshChildData disable:" + order);
                            ((View) this.childList.get(index)).setTag(CHILD_VIEW_TAG_DISABLE);
                        }
                    }
                }
            }
            int selectPage = (this.mSelectedChild / 10) + 1;
            int selectItem = this.mSelectedChild % 10;
            int size = getChildSize(page);
            for (i = 0; i < size; i++) {
                if (page != this.mParentSum || this.mChildSum % 10 == 0 || this.mChildSum % 10 == 10 || this.mChildSum <= 10) {
                    ((CornerImageTextView) this.childList.get(i)).setVisibility(0);
                } else {
                    int showNum = this.mChildSum % 10;
                    for (int j = 0; j < 10; j++) {
                        if (j < showNum) {
                            ((CornerImageTextView) this.childList.get(j)).setVisibility(0);
                        } else {
                            ((CornerImageTextView) this.childList.get(j)).setVisibility(4);
                        }
                    }
                }
                getTextViewFromList(i).setText(((((page - 1) * 10) + i) + 1));
                getTextViewFromList(i).setTopLeftCornerImage(null);
                getTextViewFromList(i).setTopRightCornerImage(null);
            }
            if (page == selectPage) {
                getTextViewFromList(selectItem).setTextColor(getChildTextColor(selectItem, true));
                ((CornerImageTextView) this.childList.get(selectItem)).setSelected(true);
                for (i = 0; i < this.childList.size(); i++) {
                    if (i != selectItem) {
                        ((CornerImageTextView) this.childList.get(i)).setSelected(false);
                        getTextViewFromList(i).setTextColor(getChildTextColor(i, false));
                    }
                }
            } else {
                for (i = 0; i < this.childList.size(); i++) {
                    ((CornerImageTextView) this.childList.get(i)).setSelected(false);
                    getTextViewFromList(i).setTextColor(getChildTextColor(i, false));
                }
            }
            LogUtils.d(this.TAG, "refreshChildData: old/new page=" + this.mPrevChildPage + "/" + this.mCurChildPage);
            if (this.mCurChildPage != this.mPrevChildPage) {
                this.mFocusedChild = 0;
            }
            this.mPrevChildPage = this.mCurChildPage;
            showCornerIcon();
            showVipCornerIcon();
        }
    }

    private int getChildTextColor(int index, boolean isSelected) {
        int color = this.mColorTextNormal;
        View view = (View) this.childList.get(index);
        if (isSelected) {
            return view.hasFocus() ? this.mColorTextFocused : this.mColorTextSelected;
        }
        color = view.getTag() == CHILD_VIEW_TAG_DISABLE ? view.hasFocus() ? this.mColorTextDisableFocus : this.mColorTextDisable : view.hasFocus() ? this.mColorTextFocused : this.mColorTextNormal;
        return color;
    }

    private CornerImageTextView getTextViewFromList(int index) {
        if (index >= 0 && this.childList != null && index < this.childList.size() && this.childList.get(index) != null) {
            return (CornerImageTextView) this.childList.get(index);
        }
        LogUtils.e(this.TAG, "getTextViewFromList error");
        return null;
    }

    private void refreshParentData(int page) {
        LogUtils.d(this.TAG, "refreshParentData" + page);
        if (page <= this.mParentSumPage + 1) {
            int size = getParentSize(page);
            int i = 0;
            while (i < size) {
                if (page != this.mParentSumPage || this.mParentSum % this.mParentItemCountPerPage == this.mParentItemCountPerPage || this.mParentSum % this.mParentItemCountPerPage == 0 || this.mParentSum <= this.mParentItemCountPerPage) {
                    ((CornerImageTextView) this.parentList.get(i)).setVisibility(0);
                } else {
                    int offset = this.mParentItemCountPerPage - (this.mParentSum % this.mParentItemCountPerPage);
                    for (int j = 0; j < offset; j++) {
                        ((CornerImageTextView) this.parentList.get((this.mParentItemCountPerPage - j) - 1)).setVisibility(8);
                    }
                }
                if (this.mCurParentPage != this.mParentSumPage || i != size - 1) {
                    ((CornerImageTextView) this.parentList.get(i)).setText(new StringBuilder(String.valueOf(((i * 10) + 1) + ((this.mParentItemCountPerPage * 10) * (page - 1)))).append("-").append(((i * 10) + 10) + ((this.mParentItemCountPerPage * 10) * (page - 1))).toString());
                } else if (this.mChildSum % 10 == 1) {
                    ((CornerImageTextView) this.parentList.get(i)).setText(String.valueOf(this.mChildSum));
                } else {
                    ((CornerImageTextView) this.parentList.get(i)).setText(new StringBuilder(String.valueOf(((i * 10) + 1) + ((this.mParentItemCountPerPage * 10) * (page - 1)))).append("-").append(this.mChildSum).toString());
                }
                i++;
            }
            setSelectedParentBg((this.mCurChildPage - 1) % this.mParentItemCountPerPage);
        }
    }

    private void setSelectedParentBg(int selectedId) {
        for (int i = 0; i < getParentSize(this.mCurParentPage); i++) {
            TextView parentTextView = (TextView) this.parentList.get(i);
            int i2;
            if (i == selectedId) {
                if (parentTextView.hasFocus()) {
                    i2 = this.mColorTextFocused;
                } else {
                    i2 = this.mColorTextSelected;
                }
                parentTextView.setTextColor(i2);
                parentTextView.setSelected(true);
            } else {
                if (parentTextView.hasFocus()) {
                    i2 = this.mColorTextFocused;
                } else {
                    i2 = this.mColorParentTextNormal;
                }
                parentTextView.setTextColor(i2);
                parentTextView.setSelected(false);
            }
        }
    }

    private String debugKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        String actionStr = event.getAction() == 0 ? "DOWN" : "UP";
        String codeStr = null;
        switch (keyCode) {
            case 19:
                codeStr = "DPAD_UP";
                break;
            case 20:
                codeStr = "DPAD_DOWN";
                break;
            case 21:
                codeStr = "DPAD_LEFT";
                break;
            case 22:
                codeStr = "DPAD_RIGHT";
                break;
            case 23:
                codeStr = "DPAD_CENTER";
                break;
        }
        View focusedView = findFocus();
        StringBuilder builder = new StringBuilder();
        builder.append("KeyEvent{");
        builder.append(codeStr);
        builder.append(", ");
        builder.append(actionStr);
        builder.append("}");
        if (focusedView != null) {
            builder.append(", focus={" + focusedView.getClass().getName() + ", " + focusedView.getId() + "}");
        }
        return builder.toString();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.d(this.TAG, debugKeyEvent(event));
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        int childSize = getChildSize(this.mCurChildPage);
        int parentSize = getParentSize(this.mCurParentPage);
        switch (event.getKeyCode()) {
            case 19:
                if (isParentFocus()) {
                    parentUpKey();
                    return true;
                }
                break;
            case 20:
                if (isChildFocus()) {
                    childDownKey();
                    return true;
                }
                break;
            case 21:
                if (((CornerImageTextView) this.childList.get(0)).hasFocus()) {
                    childLeftKey();
                    return true;
                } else if (((CornerImageTextView) this.parentList.get(0)).hasFocus()) {
                    parentLeftKey();
                    return true;
                }
                break;
            case 22:
                if (((CornerImageTextView) this.childList.get(childSize - 1)).hasFocus()) {
                    childRightKey();
                    return true;
                } else if (((CornerImageTextView) this.parentList.get(parentSize - 1)).hasFocus()) {
                    parentRightKey();
                    return true;
                }
                break;
        }
        if (this.mEnable && event.getRepeatCount() > 0) {
            ViewGroup parent = (ViewGroup) getParent();
            if (event.getAction() == 0) {
                int direction = 0;
                switch (event.getKeyCode()) {
                    case 21:
                        if (event.hasNoModifiers()) {
                            direction = 17;
                            break;
                        }
                        break;
                    case 22:
                        if (event.hasNoModifiers()) {
                            direction = 66;
                            break;
                        }
                        break;
                }
                if (direction != 0) {
                    View focused = parent.findFocus();
                    View v;
                    if (focused != null) {
                        v = focused.focusSearch(direction);
                        if (!(v == null || v == focused)) {
                            focused.getFocusedRect(this.mTempRect);
                            if (parent instanceof ViewGroup) {
                                parent.offsetDescendantRectToMyCoords(focused, this.mTempRect);
                                parent.offsetRectIntoDescendantCoords(v, this.mTempRect);
                            }
                            if (v.requestFocus(direction, this.mTempRect)) {
                                return true;
                            }
                        }
                    }
                    v = focusSearch(null, direction);
                    if (v != null && v.requestFocus(direction)) {
                        return true;
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        return false;
    }

    private void childDownKey() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> childDownKey");
        }
        requestParentFocus((this.mCurChildPage - 1) % this.mParentItemCountPerPage, Service.CISCO_FNA);
    }

    private void childLeftKey() {
        LogUtils.e(this.TAG, ">> childLeftKey");
        if (this.mCurChildPage > 1) {
            this.mCurChildPage--;
            refreshChildData(this.mCurChildPage);
            requestChildFocus(getChildSize(this.mCurChildPage) - 1, 17);
            setSelectedParentBg((this.mCurChildPage - 1) % this.mParentItemCountPerPage);
            if (this.mCurChildPage % this.mParentItemCountPerPage == 0) {
                this.mCurParentPage--;
                refreshParentData(this.mCurParentPage);
            }
        } else if (this.mCurChildPage == 1) {
            this.mCurChildPage = this.mParentSum;
            refreshChildData(this.mCurChildPage);
            int childSize = getChildSize(this.mCurChildPage);
            View focusChild = (View) this.childList.get(childSize - 1);
            if (focusChild.hasFocus()) {
                onFocusChange(focusChild, false);
                onFocusChange(focusChild, true);
            } else {
                requestChildFocus(childSize - 1, 17);
            }
            setSelectedParentBg((this.mCurChildPage - 1) % this.mParentItemCountPerPage);
            this.mCurParentPage = this.mCurChildPage % this.mParentItemCountPerPage == 0 ? this.mCurChildPage / this.mParentItemCountPerPage : (this.mCurChildPage / this.mParentItemCountPerPage) + 1;
            refreshParentData(this.mCurParentPage);
        }
    }

    private void childRightKey() {
        LogUtils.e(this.TAG, ">> childRightKey");
        if (this.mCurChildPage < this.mParentSum) {
            this.mCurChildPage++;
            requestChildFocus(0, 66);
            refreshChildData(this.mCurChildPage);
            setSelectedParentBg((this.mCurChildPage - 1) % this.mParentItemCountPerPage);
            if (this.mCurChildPage % this.mParentItemCountPerPage == 1) {
                this.mCurParentPage++;
                refreshParentData(this.mCurParentPage);
            }
        } else if (this.mCurChildPage == this.mParentSum) {
            this.mCurChildPage = 1;
            refreshChildData(this.mCurChildPage);
            View focusChild = (View) this.childList.get(0);
            if (focusChild.hasFocus()) {
                onFocusChange(focusChild, false);
                onFocusChange(focusChild, true);
            } else {
                requestChildFocus(0, 66);
            }
            setSelectedParentBg((this.mCurChildPage - 1) % this.mParentItemCountPerPage);
            this.mCurParentPage = this.mCurChildPage % this.mParentItemCountPerPage == 0 ? this.mCurChildPage / this.mParentItemCountPerPage : (this.mCurChildPage / this.mParentItemCountPerPage) + 1;
            refreshParentData(this.mCurParentPage);
        }
    }

    private void parentUpKey() {
        LogUtils.e(this.TAG, "parentUpKey: focusedChild=" + this.mFocusedChild);
        requestChildFocus(this.mFocusedChild, 33);
    }

    public void parentLeftKey() {
        LogUtils.d(this.TAG, ">> parentLeftKey");
        if (this.mCurParentPage > 1) {
            this.mCurParentPage--;
            this.mCurChildPage--;
            refreshParentData(this.mCurParentPage);
            refreshChildData(this.mCurChildPage);
            requestParentFocus(getParentSize(this.mCurParentPage) - 1, 17);
        } else if (this.mCurParentPage == 1) {
            this.mCurParentPage = this.mParentSumPage;
            this.mCurChildPage = this.mParentSum;
            refreshParentData(this.mCurParentPage);
            refreshChildData(this.mCurChildPage);
            requestParentFocus(getParentSize(this.mCurParentPage) - 1, 17);
        }
    }

    public void parentRightKey() {
        LogUtils.d(this.TAG, ">> parentRightKey");
        checkFocusPos();
        if (this.mCurParentPage < this.mParentSumPage) {
            this.mCurParentPage++;
            this.mCurChildPage++;
            requestParentFocus(0, 66);
            refreshParentData(this.mCurParentPage);
            refreshChildData(this.mCurChildPage);
        } else if (this.mCurParentPage == this.mParentSumPage) {
            this.mCurParentPage = 1;
            this.mCurChildPage = 1;
            requestParentFocus(0, 66);
            refreshParentData(this.mCurParentPage);
            refreshChildData(this.mCurChildPage);
        }
        LogUtils.d(this.TAG, "<< parentRightKey");
    }

    private void logParentFocuses() {
        for (int i = 0; i < this.parentList.size(); i++) {
            View parent = (View) this.parentList.get(i);
            LogUtils.i(this.TAG, "  parent[" + i + "] id/nextId=" + parent.getId() + "/" + parent.getNextFocusDownId());
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        LogUtils.i(this.TAG, ">> onFocusChange: {" + v.getId() + "}, " + hasFocus);
        boolean gainingFocus;
        if (hasFocus && this.mFocusPos == FocusPosition.LOST) {
            gainingFocus = true;
        } else {
            gainingFocus = false;
        }
        checkFocusPos();
        if (hasFocus) {
            ((RelativeLayout) v.getParent()).bringToFront();
            v.bringToFront();
            int i;
            if (isParentFocus()) {
                LogUtils.d(this.TAG, "onFocusChange: {" + v.getId() + "} parent focus");
                if (DEBUGMODE) {
                    logParentFocuses();
                }
                int parentSize = getParentSize(this.mCurParentPage);
                for (i = 0; i < parentSize; i++) {
                    if (((CornerImageTextView) this.parentList.get(i)).getId() == v.getId()) {
                        this.mCurChildPage = (i + 1) + (this.mParentItemCountPerPage * (this.mCurParentPage - 1));
                        this.mFocusedParent = true;
                        if (this.onEpisodeFocusChangeListener != null) {
                            this.onEpisodeFocusChangeListener.onEpisodeFocus(-1);
                        }
                    }
                }
                if (this.mZoomEnabled) {
                    AnimationUtils.zoomIn(v, this.mParentZoomRatio);
                }
                dismissTipsPopWindow();
            } else if (isChildFocus()) {
                LogUtils.d(this.TAG, "onFocusChange: {" + v.getId() + "} child focus");
                int childSize = getChildSize(this.mCurChildPage);
                i = 0;
                while (i < childSize) {
                    if (((CornerImageTextView) this.childList.get(i)).getId() == v.getId()) {
                        this.mFocusedChild = i;
                        LogUtils.d(this.TAG, "onFocusChange: {" + v.getId() + "} focusedChild=" + this.mFocusedChild);
                        this.mFocusedParent = false;
                        this.mFocusedEpisodeIndex = getChildFocusPos();
                        if (this.onEpisodeFocusChangeListener != null) {
                            this.onEpisodeFocusChangeListener.onEpisodeFocus(this.mFocusedEpisodeIndex);
                        }
                        if (this.mZoomEnabled) {
                            AnimationUtils.zoomIn(v);
                        }
                        showTipsPopWindow(this.mFocusedEpisodeIndex);
                    } else {
                        i++;
                    }
                }
                if (this.mZoomEnabled) {
                    AnimationUtils.zoomIn(v);
                }
                showTipsPopWindow(this.mFocusedEpisodeIndex);
            }
        } else if (this.mFocusPos == FocusPosition.LOST) {
            dismissTipsPopWindow();
        }
        refreshChildData(this.mCurChildPage);
        refreshParentData(this.mCurParentPage);
        v.invalidate();
        ((RelativeLayout) v.getParent()).invalidate();
        invalidate();
        if (!hasFocus && this.mZoomEnabled) {
            if (this.childList.contains(v)) {
                AnimationUtils.zoomOut(v);
            } else if (this.parentList.contains(v)) {
                AnimationUtils.zoomOut(v, this.mParentZoomRatio);
            }
        }
        LogUtils.i(this.TAG, "<< onFocusChange: {" + v.getId() + "}, " + hasFocus);
    }

    private void checkFocusPos() {
        if (this.mAutoFocusSelection) {
            boolean isParent = isParentFocus();
            boolean isChild = isChildFocus();
            if (isParent) {
                this.mFocusPos = FocusPosition.PARENT;
            } else if (isChild) {
                this.mFocusPos = FocusPosition.CHILD;
            } else {
                this.mFocusPos = FocusPosition.LOST;
            }
            LogUtils.i(this.TAG, "checkFocusPos: " + this.mFocusPos);
        }
    }

    public void onClick(View v) {
        int i;
        LogUtils.d(this.TAG, "onClick");
        int childSize = getChildSize(this.mCurChildPage);
        int clickedIndex = -1;
        for (i = 0; i < childSize; i++) {
            if (this.childList.get(i) == v) {
                clickedIndex = ((this.mCurChildPage - 1) * 10) + i;
            }
        }
        LogUtils.d(this.TAG, "onClick: clicked child index=" + clickedIndex);
        if (clickedIndex < 0) {
            int parentSize = getParentSize(this.mCurParentPage);
            for (i = 0; i < parentSize; i++) {
                if (this.parentList.get(i) == v) {
                    setSelectedParentBg(i);
                    int newChildPage = (i + 1) + (this.mParentItemCountPerPage * (this.mCurParentPage - 1));
                    if (this.mCurChildPage != newChildPage) {
                        this.mCurChildPage = newChildPage;
                        refreshChildData(this.mCurChildPage);
                    }
                    if (this.onEpisodeFocusChangeListener != null) {
                        this.onEpisodeFocusChangeListener.onEpisodeFocus(-1);
                        return;
                    }
                    return;
                }
            }
        } else if (this.onEpisodeClickListener != null) {
            this.onEpisodeClickListener.onEpisodeClick(v, clickedIndex);
        }
    }

    public void setMarginleft(int marginleft) {
        this.mMarginleft = marginleft;
    }

    public void setOnEpisodeClickListener(OnEpisodeClickListener onEpisodeClickListener) {
        this.onEpisodeClickListener = onEpisodeClickListener;
    }

    public void setOnEpisodeFocusChangeListener(OnEpisodeFocusChangeListener onEpisodeFocusChangeListener) {
        this.onEpisodeFocusChangeListener = onEpisodeFocusChangeListener;
    }

    public void setOnEpisodeSlideListener(OnEpisodeSlideListener onEpisodeSlideListener) {
        this.mOnEpisodeSlideListener = onEpisodeSlideListener;
    }

    private int getZoomInSpace(int w, int h) {
        int deltaW = Math.round((((float) w) / 2.0f) * (AnimationUtils.getDefaultZoomRatio() - 1.0f));
        int negativeW = getBgDrawablePaddings().left * -2;
        LogUtils.d(this.TAG, "getZoomInSpace: deltaW=" + deltaW);
        int result = negativeW + deltaW;
        LogUtils.d(this.TAG, "getZoomInSpace: result=" + result);
        return result;
    }

    private Rect getBgDrawablePaddings() {
        if (this.mBgDrawablePaddings != null) {
            return this.mBgDrawablePaddings;
        }
        Drawable d = this.mContext.getResources().getDrawable(this.mItemBgResId);
        this.mBgDrawablePaddings = new Rect();
        if (d != null) {
            d.getPadding(this.mBgDrawablePaddings);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> getBgDrawablePaddings, mBgDrawablePaddings(" + this.mBgDrawablePaddings.left + ", " + this.mBgDrawablePaddings.top + ", " + this.mBgDrawablePaddings.right + ", " + this.mBgDrawablePaddings.bottom);
        }
        return this.mBgDrawablePaddings;
    }

    private int getChildWidthPadded() {
        return this.mChildWidth + (getBgDrawablePaddings().left * 2);
    }

    private int getChildHeightPadded() {
        return this.mChildHeight + (getBgDrawablePaddings().top * 2);
    }

    private int getParentWidthPadded() {
        return this.mParentWidth + (getBgDrawablePaddings().left * 2);
    }

    private int getParentHeightPadded() {
        return this.mParentHeight + (getBgDrawablePaddings().top * 2);
    }

    public void setZoomEnabled(boolean enable) {
        this.mZoomEnabled = enable;
    }

    public Rect getContentPadding() {
        return getBgDrawablePaddings();
    }

    private void showCornerIcon() {
        if (this.mCornerIconIndexList == null || this.mCornerIconIndexList.size() == 0) {
            LogUtils.e(this.TAG, "showCornerIcon, mCornerIconIndexList is empty or null");
        } else if (this.mCornerIconResId == Integer.MIN_VALUE) {
            throw new IllegalStateException("Item corner image resource must be set before calling this method!");
        } else {
            LogUtils.d(this.TAG, "showCornerIcon mCornerIconPositionList.size" + this.mCornerIconIndexList.size());
            int size = this.mCornerIconIndexList.size();
            for (int i = 0; i < size; i++) {
                int order = ((Integer) this.mCornerIconIndexList.get(i)).intValue();
                LogUtils.e(this.TAG, "order=" + order);
                CornerImageTextView child = getTextViewFromList(order % 10);
                if (order / 10 == this.mCurChildPage - 1) {
                    child.setTopRightCornerImage(this.mTopRightBitmap);
                }
            }
        }
    }

    private void showVipCornerIcon() {
        if (this.mVipOrderList == null || this.mVipOrderList.size() == 0) {
            LogUtils.e(this.TAG, "showVipCornerIcon, mVipOrderList is empty or null");
        } else if (this.mVipIconResId == Integer.MIN_VALUE) {
            throw new IllegalStateException("Item corner image resource must be set before calling this method!");
        } else {
            LogUtils.d(this.TAG, "showVipCornerIcon mVipOrderList.size" + this.mVipOrderList.size());
            for (Integer key : this.mVipOrderList.keySet()) {
                PayMarkType vipType = (PayMarkType) this.mVipOrderList.get(key);
                int order = key.intValue();
                CornerImageTextView child = getTextViewFromList(order % 10);
                if (order / 10 == this.mCurChildPage - 1) {
                    switch ($SWITCH_TABLE$com$gala$tvapi$type$PayMarkType()[vipType.ordinal()]) {
                        case 2:
                            child.setTopLeftCornerImage(this.mVIPBitmap);
                            break;
                        case 3:
                            child.setTopLeftCornerImage(this.mSinglePayBitmap);
                            break;
                        case 4:
                            child.setTopLeftCornerImage(this.mCouponBitmap);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void setCornerIconResId(int iconId) {
        this.mCornerIconResId = iconId;
        this.mTopRightBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), iconId);
    }

    public void setCornerIconPositionList(ArrayList<Integer> cornerIconPositionList) {
        this.mCornerIconIndexList = cornerIconPositionList;
    }

    public void setVipIconResId(int iconId, int couponId, int singlePayId) {
        this.mVipIconResId = iconId;
        this.mVIPBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), iconId);
        this.mCouponBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), couponId);
        this.mSinglePayBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), singlePayId);
    }

    public void setVipCornerList(HashMap<Integer, PayMarkType> vipOrderList) {
        this.mVipOrderList = vipOrderList;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.e(this.TAG, "onDetachedFromWindow()");
        if (this.mTipsWindow != null) {
            this.mTipsWindow.dismiss();
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mDissmissTipRunnable);
            this.mHandler.removeCallbacks(this.mShowTipLocRunnable);
            this.mHandler.removeCallbacks(this.mShowTipRunnable);
            LogUtils.e(this.TAG, "onDetachedFromWindow removeCallbacksAndMessages");
        }
    }

    protected void onAttachedToWindow() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> onAttachedToWindow()");
        }
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        LogUtils.e(this.TAG, "onVisibilityChanged visibility=" + visibility);
        if (visibility != 0) {
            onFocusLeave();
        }
    }

    public void setTipsMaxTextNum(int num) {
        this.mTipsMaxTextNum = num;
    }

    public void setTipsTextSizePixel(int textSize) {
        this.mTipsTextSize = textSize;
    }

    public void setTipsTextSizeResId(int textSizeResId) {
        this.mTipsTextSize = getDimensionPixelSize(textSizeResId);
    }

    public void setTipsTextColor(int color) {
        this.mTipsTextColor = color;
    }

    public void setTipsBgResId(int bgResId) {
        this.mTipsBgResId = bgResId;
    }

    public void setTipsShowLocation(VerticalPosition verticalPos) {
        this.mTipShowLoc = verticalPos;
    }

    public void setShowTipsAtSpecifiedPos(int offsetX, int offsetY) {
        this.mTipOffsetXOfAnchorView = offsetX;
        this.mTipOffsetYOfAnchorView = offsetY;
    }

    public void setTipsContent(HashMap<Integer, String> list) {
        if (this.mTipsBgResId == Integer.MIN_VALUE) {
            throw new IllegalStateException("Item tips background image resource must be set before calling this method!");
        }
        if (!(list == null || list.isEmpty())) {
            this.mTipsContentHashMap = list;
        }
        if (this.mTipsWindow == null) {
            this.mTipsWindow = new ItemPopupWindow(this.mContext, (float) this.mTipsTextSize, this.mTipsBgResId, this.mTipsTextColor, this.mTipsMaxTextNum);
        }
    }

    public void setDisableOrderList(List<Integer> orders) {
        this.mDisableOrderList = orders;
    }

    private void showTipsPopWindow(int index) {
        LogUtils.d(this.TAG, "showTipsPopWindow=" + index);
        if (this.mTipsContentHashMap == null || this.mTipsContentHashMap.isEmpty()) {
            LogUtils.e(this.TAG, "mTipsContentHashMap is empty");
        } else if (isShown()) {
            if (this.mTipOffsetXOfAnchorView == -1 || this.mTipOffsetYOfAnchorView == -1) {
                View view = (View) this.childList.get(index % 10);
                if (view == null) {
                    LogUtils.e(this.TAG, "mEpisodesView.getChild==null index" + index);
                    return;
                }
                this.mShowTipLocRunnable = new ShowTipWindowRunnable(view, index);
                this.mHandler.post(this.mShowTipLocRunnable);
                return;
            }
            this.mShowTipRunnable = new ShowTipWindowRunnable(this, index);
            this.mHandler.post(this.mShowTipRunnable);
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showTipsPopWindow not shown");
        }
    }

    private void dismissTipsPopWindow() {
        LogUtils.e(this.TAG, ">>dismissTipsPopWindow");
        this.mDissmissTipRunnable = new DissmissTipWindowRunnable();
        this.mHandler.post(this.mDissmissTipRunnable);
    }

    private void onFocusLeave() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onFocusLeave");
        }
        dismissTipsPopWindow();
    }

    public void setHWAccelerated(boolean accelerated) {
        this.mIsHWAccelerated = accelerated;
    }

    public void resetDefaultFocus(int selection) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "resetDefaultFocus");
        }
        if (selection >= 0) {
            this.mSelectedChild = selection;
        }
        this.mFocusedChild = -1;
        this.mCurChildPage = (this.mSelectedChild / 10) + 1;
        this.mPrevChildPage = (this.mSelectedChild / 10) + 1;
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "resetDefaultFocus mSelectedChild=" + this.mSelectedChild + ", mCurChildPage=" + this.mCurChildPage + ", mPrevChildPage=" + this.mPrevChildPage);
        }
    }

    public int getMaxChildCountPerParentPage() {
        return this.mParentItemCountPerPage * 10;
    }

    public void setEnableRequestFocusByParent(boolean enable) {
        this.mEnable = enable;
    }
}
