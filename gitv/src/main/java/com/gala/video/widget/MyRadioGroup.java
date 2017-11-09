package com.gala.video.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.gala.video.widget.ItemPopupWindow.HintWindowStyle;
import com.gala.video.widget.ItemPopupWindow.ItemHint;
import com.gala.video.widget.ItemPopupWindow.VerticalPosition;
import com.gala.video.widget.episode.CornerImageTextView;
import com.gala.video.widget.util.AnimationUtils;
import com.gala.video.widget.util.DebugOptions;
import com.gala.video.widget.util.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyRadioGroup extends FrameLayout {
    private static final boolean DEBUGMODE = DebugOptions.isInDebugMode();
    private static final AtomicInteger ITEM_BASE_ID = new AtomicInteger(1193046);
    private static final int ITEM_TXT_CHILD_INDEX = 0;
    private static final int ITEM_TXT_CORNER_INDEX = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    private static final String TAG = "MyRadioGroup";
    private boolean mAutoAlignLeft = true;
    private boolean mAutoAlignTop = true;
    private boolean mAutoFocusOnSelection;
    private Rect mBgDrawablePaddings;
    private OnCheckedChangeListener mCheckListener;
    private OnFocusChangeListener mChildFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (DebugOptions.LOG) {
                LogUtils.d(MyRadioGroup.TAG, "onFocusChange: " + v.getId() + ", " + hasFocus);
            }
            FocusState oldFocusState = MyRadioGroup.this.mFocusState;
            MyRadioGroup.this.checkFocusState();
            if (MyRadioGroup.this.mAutoFocusOnSelection) {
                boolean gainingFocus = hasFocus && oldFocusState == FocusState.LOST;
                MyRadioGroup.this.checkFocusState();
                if (gainingFocus && MyRadioGroup.this.isValidSelection() && MyRadioGroup.this.mChildList.indexOf(v) != MyRadioGroup.this.mSelection) {
                    MyRadioGroup.this.mSkipFocusView = v;
                    ((CornerImageTextView) MyRadioGroup.this.mChildList.get(MyRadioGroup.this.mSelection)).requestFocus();
                }
            }
            if (MyRadioGroup.this.mSkipFocusView != v) {
                if (hasFocus) {
                    if (MyRadioGroup.this.mZoomEnabled) {
                        AnimationUtils.zoomIn(v);
                    }
                    MyRadioGroup.this.showItemHint(MyRadioGroup.this.mChildList.indexOf(v));
                } else if (MyRadioGroup.this.mSkipFocusView != v) {
                    MyRadioGroup.this.mSkipFocusView = null;
                    if (MyRadioGroup.this.mZoomEnabled) {
                        AnimationUtils.zoomOut(v);
                    }
                    MyRadioGroup.this.hideItemHint();
                }
            }
            MyRadioGroup.this.refreshChildData();
        }
    };
    private ArrayList<CornerImageTextView> mChildList = new ArrayList();
    private int mColorTxtDefault = -1;
    private int mColorTxtDisabled = -10790053;
    private int mColorTxtFocused = -1;
    private int mColorTxtFocusedSelected = this.mColorTxtFocused;
    private int mColorTxtSelected = -7681775;
    private int mContentSpacing;
    private Context mContext;
    private Bitmap mCornerIconBitmap;
    private List<Integer> mCornerIconList = new ArrayList();
    private int mCornerIconResId = 0;
    private LayoutParams mCornerImageParams = null;
    private final OnItemClickListener mDefItemClickListener = new OnItemClickListener() {
        public void onItemClick(View v, int position) {
            if (MyRadioGroup.this.mCheckListener != null) {
                MyRadioGroup.this.mCheckListener.onItemChecked(position);
                if (MyRadioGroup.this.mSelection != position) {
                    MyRadioGroup.this.mCheckListener.onCheckedChanged(position);
                }
            }
            MyRadioGroup.this.mSelection = position;
            MyRadioGroup.this.refreshChildData();
        }
    };
    private List<Integer> mDisabledItems = new ArrayList();
    private int mDividerDrawableResId;
    private FocusState mFocusState = FocusState.LOST;
    private HintWindowStyle mHintStyle;
    private boolean mIncludeFontPadding = true;
    private boolean mIsEnabled = true;
    private int mItemBgResId = Integer.MIN_VALUE;
    private OnItemClickListener mItemClickListener = this.mDefItemClickListener;
    private int mItemHeightPadded;
    private ItemPopupWindow mItemHintWindow;
    private HashMap<Integer, ItemHint> mItemHints = new HashMap();
    private int mItemWidthPadded;
    private float mLineSpacingExtraPx = 0.0f;
    private float mLineSpacingMultiplier = 1.0f;
    private int mNextFocusDownId = -1;
    private int mNextFocusLeftId = -1;
    private int mNextFocusRightId = -1;
    private int mNextFocusUpId = -1;
    private int mRawItemHeight;
    private int mRawItemWidth;
    private RelativeLayout mRlChildren;
    private RelativeLayout mRlDivider;
    private int mSelection = -1;
    private int mShowDividerFlags;
    private View mSkipFocusView;
    private int mTextSizePx;
    private boolean mZoomEnabled = true;

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int i);

        void onItemChecked(int i);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    private enum FocusState {
        LOST,
        GAIN
    }

    public MyRadioGroup(Context context) {
        super(context);
        init(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mNextFocusLeftId = getNextFocusLeftId();
        this.mNextFocusUpId = getNextFocusUpId();
        this.mNextFocusRightId = getNextFocusRightId();
        this.mNextFocusDownId = getNextFocusDownId();
        if (DEBUGMODE) {
            setBackgroundColor(DebugOptions.DEBUG_BG_COLOR);
        }
        setFocusable(false);
        this.mRlDivider = new RelativeLayout(this.mContext);
        addView(this.mRlDivider, new LayoutParams(-1, -2));
        this.mRlChildren = new RelativeLayout(this.mContext);
        addView(this.mRlChildren, new LayoutParams(-1, -2));
    }

    public View getFirstFocusableChild() {
        if (isValidSelection()) {
            return (View) this.mChildList.get(this.mSelection);
        }
        View view;
        if (!this.mChildList.isEmpty()) {
            view = (View) this.mChildList.get(0);
        }
        return view;
    }

    public void setDimens(int[] dimens) {
        if (this.mItemBgResId == Integer.MIN_VALUE) {
            throw new IllegalStateException("Please invoke setItemBackground() before setDimens()!");
        } else if (dimens == null || dimens.length != 2) {
            throw new IllegalArgumentException("Please provide exactly 2 parameters for setDimens()!");
        } else {
            this.mRawItemWidth = dimens[0];
            this.mRawItemHeight = dimens[1];
            this.mContentSpacing = getContentSpacingForZoom(this.mRawItemWidth);
            this.mItemWidthPadded = dimens[0] + (getBgDrawablePaddings().left * 2);
            this.mItemHeightPadded = dimens[1] + (getBgDrawablePaddings().top * 2);
            LogUtils.d(TAG, "setDimens: [0]=" + dimens[0] + ", [1]=" + dimens[1] + ", paddings=" + getBgDrawablePaddings() + ", calculated contentSpacing=" + this.mContentSpacing);
        }
    }

    public int getItemPaddedWidth() {
        return this.mItemWidthPadded;
    }

    public int getItemPaddedHeight() {
        return this.mItemHeightPadded;
    }

    public void setDataSource(List<String> itemList, int initialSelection) {
        if (this.mItemBgResId == Integer.MIN_VALUE || this.mItemWidthPadded <= 0 || this.mItemHeightPadded <= 0) {
            throw new IllegalStateException("Please invoke setItemBackground() and setDimens() before setDataSource!");
        } else if (itemList == null || itemList.isEmpty()) {
            throw new IllegalArgumentException("MyRadioGroup.setDataSource() does not accept null/empty item list");
        } else {
            this.mItemHints.clear();
            this.mSelection = initialSelection;
            LogUtils.d(TAG, "setDataSource: initial selection=" + this.mSelection);
            if (this.mItemBgResId == Integer.MIN_VALUE) {
                throw new IllegalStateException("You must call setItemBackground before setDataSource!");
            }
            addChildViews(itemList, this.mSelection, false);
            if (this.mZoomEnabled) {
                for (ViewParent parent = this.mRlChildren; parent instanceof ViewGroup; parent = parent.getParent()) {
                    ((ViewGroup) parent).setClipChildren(false);
                    ((ViewGroup) parent).setClipToPadding(false);
                }
                this.mRlDivider.setClipChildren(false);
                this.mRlDivider.setClipToPadding(false);
            }
            refreshChildData();
        }
    }

    public void updateDataSource(List<String> list, int selection) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateDataSource, list=" + list);
        }
        if (list != null && list.size() != 0) {
            this.mSelection = selection;
            addChildViews(list, selection, true);
            refreshChildData();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateDataSource, invalid list.");
        }
    }

    private void checkFocusState() {
        this.mFocusState = FocusState.LOST;
        Iterator it = this.mChildList.iterator();
        while (it.hasNext()) {
            if (((View) it.next()).hasFocus()) {
                this.mFocusState = FocusState.GAIN;
                break;
            }
        }
        if (DebugOptions.LOG) {
            LogUtils.i(TAG, "checkFocusPos: " + this.mFocusState);
        }
    }

    private void showItemHint(int index) {
        if (index < 0 || index > this.mChildList.size()) {
            if (DebugOptions.LOG) {
                LogUtils.w(TAG, "showItemHint(" + index + "): index out of range!");
            }
        } else if (this.mItemHintWindow != null) {
            final View childView = (View) this.mChildList.get(index);
            ItemHint hint = (ItemHint) this.mItemHints.get(Integer.valueOf(index));
            if (hint != null) {
                final String content = hint.getHintContent();
                childView.post(new Runnable() {
                    public void run() {
                        MyRadioGroup.this.mItemHintWindow.show(childView, content, VerticalPosition.DROPDOWN);
                    }
                });
            } else if (DebugOptions.LOG) {
                LogUtils.w(TAG, "no hint for current item (#" + index + ")");
            }
        } else if (DebugOptions.LOG) {
            LogUtils.w(TAG, "hint window not created yet!");
        }
    }

    private void hideItemHint() {
        if (this.mItemHintWindow != null) {
            this.mItemHintWindow.dismiss();
        }
    }

    private boolean isValidSelection() {
        return this.mSelection >= 0 && this.mSelection < this.mChildList.size();
    }

    public void setItemBackground(int resId) {
        this.mBgDrawablePaddings = null;
        this.mItemBgResId = resId;
    }

    public void setCornerImageParams(LayoutParams params) {
        this.mCornerImageParams = params;
    }

    private void addChildViews(List<String> itemList, int initialSelection, boolean keepFocus) {
        boolean needRecoverFocus = false;
        checkFocusState();
        if (keepFocus && this.mFocusState == FocusState.GAIN) {
            setFocusable(true);
            requestFocus();
            needRecoverFocus = true;
        }
        this.mRlChildren.removeAllViews();
        this.mRlDivider.removeAllViews();
        this.mChildList.clear();
        int listSize = itemList.size();
        View previousChild = null;
        int dividerLeftMargin = 0;
        if ((this.mShowDividerFlags & 1) != 0) {
            addDividerViewAt(0);
        }
        int i = 0;
        while (i < listSize) {
            int id;
            String item = (String) itemList.get(i);
            View txt = new CornerImageTextView(this.mContext);
            txt.setId(ITEM_BASE_ID.getAndIncrement());
            txt.setBackgroundResource(this.mItemBgResId);
            txt.setOnFocusChangeListener(this.mChildFocusListener);
            txt.setFocusable(true);
            txt.setText(item);
            txt.setTextColor(this.mColorTxtDefault);
            txt.setTextSize(0, (float) this.mTextSizePx);
            txt.setIncludeFontPadding(this.mIncludeFontPadding);
            txt.setLineSpacing(this.mLineSpacingExtraPx, this.mLineSpacingMultiplier);
            txt.setGravity(17);
            txt.setCornerImagePadding(getBgDrawablePaddings());
            if (this.mNextFocusDownId != -1) {
                txt.setNextFocusDownId(this.mNextFocusDownId == getId() ? txt.getId() : this.mNextFocusDownId);
            }
            if (this.mNextFocusUpId != -1) {
                txt.setNextFocusUpId(this.mNextFocusUpId == getId() ? txt.getId() : this.mNextFocusUpId);
            }
            if (this.mNextFocusLeftId != -1 && i == 0) {
                txt.setNextFocusLeftId(this.mNextFocusLeftId == getId() ? txt.getId() : this.mNextFocusLeftId);
            }
            if (this.mNextFocusRightId != -1 && i == listSize - 1) {
                if (this.mNextFocusRightId == getId()) {
                    id = txt.getId();
                } else {
                    id = this.mNextFocusRightId;
                }
                txt.setNextFocusRightId(id);
            }
            int contentSpacingForZoom = this.mContentSpacing;
            int textWidth = checkTextWidth(item, this.mTextSizePx);
            int realWidth = this.mItemWidthPadded;
            int realHeight = this.mItemHeightPadded;
            if (this.mRawItemWidth < textWidth) {
                realWidth += textWidth - this.mRawItemWidth;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "addChildViews, realWidth=" + realWidth + ", realHeight=" + realHeight);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(realWidth, realHeight);
            if (i > 0) {
                int topMargin = (this.mZoomEnabled && this.mAutoAlignTop) ? -getBgDrawablePaddings().top : 0;
                params.setMargins(getZoomInSpace(this.mRawItemWidth), topMargin, 0, 0);
                params.addRule(1, previousChild.getId());
            } else {
                int deltaLeftMargin = (this.mZoomEnabled && this.mAutoAlignLeft) ? getBgDrawablePaddings().left : 0;
                deltaLeftMargin -= Math.round(((float) contentSpacingForZoom) / 2.0f);
                params.leftMargin = getZoomInSpace(this.mRawItemWidth);
                int i2 = params.topMargin;
                id = (this.mZoomEnabled && this.mAutoAlignTop) ? getBgDrawablePaddings().top : 0;
                params.topMargin = i2 - id;
                params.addRule(9, -1);
            }
            if (DebugOptions.LOG) {
                LogUtils.d(TAG, "addChild: child[" + i + "]: id=" + txt.getId());
            }
            this.mRlChildren.addView(txt, params);
            this.mChildList.add(txt);
            previousChild = txt;
            dividerLeftMargin += this.mRawItemWidth + contentSpacingForZoom;
            if (i == listSize - 1) {
                if ((this.mShowDividerFlags & 4) != 0) {
                    addDividerViewAt(dividerLeftMargin);
                }
            } else if ((this.mShowDividerFlags & 2) != 0) {
                addDividerViewAt(dividerLeftMargin);
            }
            i++;
        }
        if (keepFocus && needRecoverFocus) {
            View selectedView = (View) this.mChildList.get(this.mSelection);
            if (!selectedView.isFocused()) {
                selectedView.requestFocus();
            }
            setFocusable(false);
        }
        requestLayout();
    }

    private int checkTextWidth(String str, int charSize) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> checkTextWidth, str=" + str + ", charSize=" + charSize);
        }
        float itemCount = 0.0f;
        for (char isDigit : str.toCharArray()) {
            if (Character.isDigit(isDigit)) {
                itemCount = (float) (((double) itemCount) + 0.5d);
            } else {
                itemCount += 1.0f;
            }
        }
        itemCount = (float) (((double) itemCount) + 0.5d);
        int width = (int) (((float) charSize) * itemCount);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< checkTextWidth, itemCount=" + itemCount + ", width=" + width);
        }
        return width;
    }

    private void addDividerViewAt(int marginLeft) {
        if (this.mDividerDrawableResId == 0) {
            throw new IllegalStateException("A valid divider drawable should be set before adding dividers to layout!");
        }
        View dividerView = new View(this.mContext);
        Drawable dividerDrawable = this.mContext.getResources().getDrawable(this.mDividerDrawableResId);
        RelativeLayout.LayoutParams dividerParams = new RelativeLayout.LayoutParams(dividerDrawable.getIntrinsicWidth(), this.mRawItemHeight);
        dividerView.setBackgroundDrawable(dividerDrawable);
        dividerParams.leftMargin = marginLeft;
        dividerParams.topMargin = getBgDrawablePaddings().top;
        dividerParams.bottomMargin = getBgDrawablePaddings().bottom;
        dividerParams.addRule(9, -1);
        this.mRlDivider.addView(dividerView, dividerParams);
    }

    private void refreshChildData() {
        int size = this.mChildList.size();
        for (int i = 0; i < size; i++) {
            Bitmap bitmap;
            CornerImageTextView txt = (CornerImageTextView) this.mChildList.get(i);
            txt.setEnabled(this.mIsEnabled);
            txt.setFocusable(this.mIsEnabled);
            if (this.mIsEnabled) {
                boolean isItemSelected;
                int colorToUse;
                boolean isItemDisabled = this.mDisabledItems.contains(Integer.valueOf(i));
                if (this.mSelection == i) {
                    isItemSelected = true;
                } else {
                    isItemSelected = false;
                }
                if (txt.hasFocus()) {
                    if (!isItemSelected || isItemDisabled) {
                        colorToUse = this.mColorTxtFocused;
                    } else {
                        colorToUse = this.mColorTxtFocusedSelected;
                    }
                } else if (!isItemSelected || isItemDisabled) {
                    txt.setSelected(false);
                    if (isItemDisabled) {
                        colorToUse = this.mColorTxtDisabled;
                    } else {
                        colorToUse = this.mColorTxtDefault;
                    }
                } else {
                    colorToUse = this.mColorTxtSelected;
                    txt.setSelected(true);
                }
                txt.setTextColor(colorToUse);
            } else {
                txt.setTextColor(this.mColorTxtDisabled);
            }
            if (this.mCornerIconList.contains(Integer.valueOf(i))) {
                bitmap = this.mCornerIconBitmap;
            } else {
                bitmap = null;
            }
            txt.setTopLeftCornerImage(bitmap);
        }
    }

    public void setOnCheckedChangedListener(OnCheckedChangeListener listener) {
        this.mCheckListener = listener;
    }

    public void setEnabled(boolean enabled) {
        this.mIsEnabled = enabled;
        refreshChildData();
        super.setEnabled(enabled);
    }

    public void setItemHints(HashMap<Integer, ItemHint> hints) {
        if (this.mHintStyle == null) {
            throw new IllegalStateException("Please invoke setItemHintStyle() first!");
        }
        this.mItemHintWindow = new ItemPopupWindow(this.mContext, (float) this.mContext.getResources().getDimensionPixelSize(this.mHintStyle.getTextSizeResId()), this.mHintStyle.getTextBgResId());
        this.mItemHints = hints;
        refreshChildData();
    }

    public void setItemHintStyle(HintWindowStyle style) {
        this.mHintStyle = style;
    }

    public void setDisabledItemIndices(List<Integer> disabledItems) {
        this.mDisabledItems = disabledItems;
        refreshChildData();
    }

    public void setSelection(int selectedIndex) {
        this.mSelection = selectedIndex;
        refreshChildData();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((23 == event.getKeyCode() || 66 == event.getKeyCode()) && event.getAction() == 0 && event.getRepeatCount() == 0 && isShown() && hasFocus()) {
            View focus = findFocus();
            if (focus != null) {
                int clickedIndex = this.mChildList.indexOf(focus);
                if (this.mDisabledItems.contains(Integer.valueOf(clickedIndex))) {
                    if (DebugOptions.LOG) {
                        LogUtils.w(TAG, "onClick: item is disabled");
                    }
                    return false;
                } else if (clickedIndex >= 0 && clickedIndex < this.mChildList.size()) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "dispatchKeyEvent, perform click event=" + event);
                    }
                    this.mItemClickListener.onItemClick(focus, clickedIndex);
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public Rect getBgDrawablePaddings() {
        if (this.mBgDrawablePaddings != null) {
            if (DebugOptions.LOG) {
                LogUtils.d(TAG, "getBgDrawablePaddings: " + this.mBgDrawablePaddings);
            }
            return this.mBgDrawablePaddings;
        }
        Drawable d = this.mContext.getResources().getDrawable(this.mItemBgResId);
        this.mBgDrawablePaddings = new Rect();
        if (d != null) {
            d.getPadding(this.mBgDrawablePaddings);
        }
        if (DebugOptions.LOG) {
            LogUtils.d(TAG, "getBgDrawablePaddings: " + this.mBgDrawablePaddings);
        }
        return this.mBgDrawablePaddings;
    }

    private int getContentSpacingForZoom(int width) {
        return Math.round((((float) width) / 2.0f) * (AnimationUtils.getDefaultZoomRatio() - 1.0f));
    }

    public void setContentSpacing(int spacing) {
        if (spacing == Integer.MIN_VALUE) {
            spacing = getContentSpacingForZoom(this.mRawItemWidth);
        }
        this.mContentSpacing = spacing;
    }

    private int getZoomInSpace(int w) {
        int deltaW = this.mContentSpacing;
        int negativeW = getBgDrawablePaddings().left * -2;
        if (DebugOptions.LOG) {
            LogUtils.d(TAG, "getZoomInSpace: deltaW=" + deltaW);
        }
        int result = negativeW + deltaW;
        if (DebugOptions.LOG) {
            LogUtils.d(TAG, "getZoomInSpace: result=" + result);
        }
        return result;
    }

    public void setTextSize(int px) {
        if (px <= 0) {
            throw new IllegalArgumentException("Please specify a valid text size (" + px + " specified)");
        }
        this.mTextSizePx = px;
    }

    public void setTextSize(int px, int smallPx, List<Integer> list) {
        if (px <= 0) {
            throw new IllegalArgumentException("Please specify a valid text size (" + px + " specified)");
        } else if (smallPx <= 0) {
            throw new IllegalArgumentException("Please specify a valid small text size (" + smallPx + " specified)");
        } else {
            this.mTextSizePx = px;
        }
    }

    public void setTextColors(int defaultColor, int selectedColor, int focusedColor, int disabledColor) {
        this.mColorTxtDefault = defaultColor;
        this.mColorTxtSelected = selectedColor;
        this.mColorTxtFocused = focusedColor;
        this.mColorTxtDisabled = disabledColor;
        this.mColorTxtFocusedSelected = this.mColorTxtFocused;
    }

    public void setTextColors(int defaultColor, int selectedColor, int focusedColor, int disabledColor, int focusedSelectedColor) {
        setTextColors(defaultColor, focusedSelectedColor, focusedColor, disabledColor);
        this.mColorTxtFocusedSelected = focusedSelectedColor;
    }

    public int getCheckedIndex() {
        return this.mSelection;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return this.mItemClickListener;
    }

    public void setNextFocusDownId(int nextFocusDownId) {
        this.mNextFocusDownId = nextFocusDownId;
        Iterator it = this.mChildList.iterator();
        while (it.hasNext()) {
            View child = (View) it.next();
            child.setNextFocusDownId(nextFocusDownId == getId() ? child.getId() : nextFocusDownId);
        }
    }

    public void setNextFocusUpId(int nextFocusUpId) {
        this.mNextFocusUpId = nextFocusUpId;
        Iterator it = this.mChildList.iterator();
        while (it.hasNext()) {
            View child = (View) it.next();
            child.setNextFocusUpId(nextFocusUpId == getId() ? child.getId() : nextFocusUpId);
        }
    }

    public void setNextFocusLeftId(int nextFocusLeftId) {
        this.mNextFocusLeftId = nextFocusLeftId;
        if (!this.mChildList.isEmpty()) {
            View firstChild = (View) this.mChildList.get(0);
            if (nextFocusLeftId == getId()) {
                nextFocusLeftId = firstChild.getId();
            }
            firstChild.setNextFocusLeftId(nextFocusLeftId);
        }
    }

    public void setNextFocusRightId(int nextFocusRightId) {
        this.mNextFocusRightId = nextFocusRightId;
        if (!this.mChildList.isEmpty()) {
            View lastChild = (View) this.mChildList.get(this.mChildList.size() - 1);
            if (nextFocusRightId == getId()) {
                nextFocusRightId = lastChild.getId();
            }
            lastChild.setNextFocusRightId(nextFocusRightId);
        }
    }

    public void setAutoFocusOnSelection(boolean autoFocus) {
        this.mAutoFocusOnSelection = autoFocus;
    }

    public void requestFocusOnChild(int childIndex) {
        if (this.mChildList.size() > 0 && childIndex >= 0 && childIndex < this.mChildList.size()) {
            boolean autoFocus = this.mAutoFocusOnSelection;
            this.mAutoFocusOnSelection = false;
            if (!((CornerImageTextView) this.mChildList.get(childIndex)).isFocused()) {
                ((CornerImageTextView) this.mChildList.get(childIndex)).requestFocus();
            }
            this.mAutoFocusOnSelection = autoFocus;
        }
    }

    public void setZoomEnabled(boolean enable) {
        this.mZoomEnabled = enable;
    }

    public void setChildAutoAlignLeft(boolean autoAlignLeft) {
        this.mAutoAlignLeft = autoAlignLeft;
    }

    public void setChildAutoAlignTop(boolean autoAlignTop) {
        this.mAutoAlignTop = autoAlignTop;
    }

    public Rect getContentPadding() {
        return getBgDrawablePaddings();
    }

    public void setCornerIconResId(int resId) {
        this.mCornerIconResId = resId;
        this.mCornerIconBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), this.mCornerIconResId);
    }

    public void setCornerIconList(List<Integer> list) {
        if (list == null) {
            list = this.mCornerIconList;
        }
        this.mCornerIconList = list;
        if (!this.mCornerIconList.isEmpty()) {
            if (this.mCornerIconResId == 0) {
                throw new IllegalStateException("setCornerIconList: setCornerIconResId() should be invoked before this method");
            }
            refreshChildData();
        }
    }

    public void setDividerPadding(int dividerPadding) {
        this.mRlDivider.setPadding(0, dividerPadding, 0, dividerPadding);
    }

    public void setDividerDrawable(int resId) {
        this.mDividerDrawableResId = resId;
    }

    public void setShowDivider(int showDividerFlags) {
        this.mShowDividerFlags = showDividerFlags;
    }

    public void setLineSpacing(float extraPx, float multiplier) {
        this.mLineSpacingExtraPx = extraPx;
        this.mLineSpacingMultiplier = multiplier;
    }

    public void setIncludeFontPadding(boolean include) {
        this.mIncludeFontPadding = include;
    }
}
