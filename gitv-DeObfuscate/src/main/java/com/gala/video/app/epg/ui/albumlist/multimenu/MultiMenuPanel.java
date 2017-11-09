package com.gala.video.app.epg.ui.albumlist.multimenu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.ThreeLevelTag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.multimenu.ScrollerLayout.ScrollerDrawListener;
import com.gala.video.app.epg.ui.albumlist.multimenu.ScrollerLayout.ScrollerEventListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.TagKeyUtil;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class MultiMenuPanel extends RelativeLayout {
    public static final int MULTI_IS_SORT_LINE = TagKeyUtil.generateTagKey();
    public static final int MULTI_MENU_FOCUSABLE = TagKeyUtil.generateTagKey();
    public static final int MULTI_MENU_LINE_NUM = TagKeyUtil.generateTagKey();
    public static final int MULTI_MENU_LINE_POSITION = TagKeyUtil.generateTagKey();
    public static final String STR_ALL = IAlbumConfig.STR_ALL_02;
    private int mChannelId;
    private Tag mCheckedTag;
    private Context mContext;
    private int mDefaultSelectPos;
    private boolean mIsInitCompleted = false;
    private boolean mIsSortLineBtn;
    private LinearLayout mLeftTitleLayout;
    private int mMenuDataLineCount;
    private MultiMenuPanelListenter mPanelListenter;
    private RectF mRectLeft;
    private RectF mRectRight;
    private LinearLayout mRightMenuLayout;
    private List<ScrollerLayout> mScollerMenuList = new ArrayList();
    private ScrollerDrawListener mScrollerDrawListener = new C08622();
    private ScrollerEventListener mScrollerEventListener = new C08611();
    private LinearGradient mShaderLeft;
    private LinearGradient mShaderRight;
    private Paint mShadowPaint;
    private List<TwoLevelTag> mTagsList;

    public interface MultiMenuPanelListenter {
        void onAlbumDataChanged();

        String onCallAlbumTagId();

        void onMenuPanelDismiss();
    }

    class C08611 implements ScrollerEventListener {
        C08611() {
        }

        public void onItemSelectListener(View curView, int curPos, boolean hasFocus) {
            AnimationUtil.zoomAnimation(curView, hasFocus, 1.06f, 200, false);
        }

        public void onItemClickListener(ScrollerLayout scrollerLayout, int curPos) {
            if (scrollerLayout != null) {
                View curView = scrollerLayout.getChildAt(curPos);
                int selectPos = scrollerLayout.getSelectPos();
                String menuTagId = MultiMenuPanel.this.getCheckedTag().getID();
                String albumTagId = "";
                if (MultiMenuPanel.this.mPanelListenter != null) {
                    albumTagId = MultiMenuPanel.this.mPanelListenter.onCallAlbumTagId();
                }
                if (curPos != selectPos || !menuTagId.equals(albumTagId)) {
                    View preSelectView = scrollerLayout.getChildAt(selectPos);
                    if (preSelectView != null) {
                        preSelectView.setSelected(false);
                    }
                    curView.setSelected(true);
                    MultiMenuPanel.this.setSortLineBtn(curView);
                    if (MultiMenuPanel.this.mPanelListenter != null) {
                        MultiMenuPanel.this.mPanelListenter.onAlbumDataChanged();
                    }
                } else if (MultiMenuPanel.this.mPanelListenter != null) {
                    MultiMenuPanel.this.mPanelListenter.onMenuPanelDismiss();
                }
                scrollerLayout.setSelectPos(curPos);
            }
        }

        public void onSlidingToRight(ScrollerLayout scrollerLayout) {
            if (scrollerLayout != null) {
                scrollerLayout.setLastItemVisible(false);
                scrollerLayout.setFirstItemVisible(false);
            }
        }

        public void onSlidingToLeft(ScrollerLayout scrollerLayout) {
            if (scrollerLayout != null) {
                scrollerLayout.setLastItemVisible(false);
                scrollerLayout.setFirstItemVisible(false);
            }
        }

        public void onLastItemVisible(ScrollerLayout scrollerLayout) {
            if (scrollerLayout != null) {
                scrollerLayout.setLastItemVisible(true);
            }
        }

        public void onFirstItemVisible(ScrollerLayout scrollerLayout) {
            if (scrollerLayout != null) {
                scrollerLayout.setFirstItemVisible(true);
            }
        }
    }

    class C08622 implements ScrollerDrawListener {
        C08622() {
        }

        public void drawShaderLayer(ScrollerLayout layout, Canvas canvas) {
            if (!layout.isLastItemVisible() || !layout.isFirstItemVisible()) {
                float f;
                MultiMenuPanel.this.mShadowPaint = new Paint();
                MultiMenuPanel.this.mShadowPaint.setAntiAlias(true);
                if (!layout.isFirstItemVisible()) {
                    f = 0.0f;
                    MultiMenuPanel.this.mShaderRight = new LinearGradient((float) (layout.getScrollX() + (layout.getWidth() - 190)), 0.0f, (float) (layout.getScrollX() + layout.getWidth()), f, new int[]{0, Color.parseColor("#E6000000")}, null, TileMode.CLAMP);
                    MultiMenuPanel.this.mRectRight = new RectF((float) (layout.getScrollX() + (layout.getWidth() - 190)), 0.0f, (float) (layout.getScrollX() + layout.getWidth()), (float) layout.getHeight());
                    MultiMenuPanel.this.mShadowPaint.setShader(MultiMenuPanel.this.mShaderRight);
                    canvas.drawRect(MultiMenuPanel.this.mRectRight, MultiMenuPanel.this.mShadowPaint);
                }
                if (!layout.isLastItemVisible()) {
                    f = 0.0f;
                    MultiMenuPanel.this.mShaderLeft = new LinearGradient((float) layout.getScrollX(), 0.0f, (float) (layout.getScrollX() + 190), f, new int[]{Color.parseColor("#E6000000"), 0}, null, TileMode.CLAMP);
                    MultiMenuPanel.this.mRectLeft = new RectF((float) layout.getScrollX(), 0.0f, (float) (layout.getScrollX() + 190), (float) layout.getHeight());
                    MultiMenuPanel.this.mShadowPaint.setShader(MultiMenuPanel.this.mShaderLeft);
                    canvas.drawRect(MultiMenuPanel.this.mRectLeft, MultiMenuPanel.this.mShadowPaint);
                }
            }
        }

        public void drawItemDividingLine(ScrollerLayout layout, Canvas canvas) {
        }
    }

    public MultiMenuPanel(Context context) {
        super(context);
        init(context);
    }

    public MultiMenuPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiMenuPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        initMainLayout();
    }

    public void fillData(List<TwoLevelTag> subTags, int channelId) {
        this.mIsInitCompleted = false;
        this.mChannelId = channelId;
        this.mTagsList = subTags;
        this.mMenuDataLineCount = this.mTagsList.size();
        this.mLeftTitleLayout = new LinearLayout(this.mContext);
        this.mLeftTitleLayout.setOrientation(1);
        this.mLeftTitleLayout.setFocusable(false);
        this.mRightMenuLayout = new LinearLayout(this.mContext);
        this.mRightMenuLayout.setOrientation(1);
        this.mRightMenuLayout.setClipChildren(false);
        this.mRightMenuLayout.setClipToPadding(false);
        this.mRightMenuLayout.setFocusable(true);
        for (int i = 0; i < this.mMenuDataLineCount; i++) {
            boolean isSortLine = false;
            TextView titleTv = getMultiMenuTwoLevelLeftTitleTv(((TwoLevelTag) this.mTagsList.get(i)).sn);
            if (!(titleTv == null || titleTv.getText() == null)) {
                isSortLine = ((String) titleTv.getText()).contains("排序");
            }
            LayoutParams params1 = new LayoutParams(-1, ResourceUtil.getDimen(C0508R.dimen.dimen_42dp));
            if (i < this.mMenuDataLineCount - 1) {
                params1.topMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_7dp);
                params1.bottomMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_7dp);
            } else {
                params1.topMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_7dp);
                params1.bottomMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_12dp);
            }
            this.mLeftTitleLayout.addView(titleTv, params1);
            ScrollerLayout scrollerLayout = getPerScrollerLayout(i, isSortLine);
            this.mRightMenuLayout.addView(scrollerLayout, new LayoutParams(ResourceUtil.getDimen(C0508R.dimen.dimen_900dp), -2));
            this.mScollerMenuList.add(scrollerLayout);
        }
        addRightLayout();
        addLeftLayout();
        this.mIsInitCompleted = true;
    }

    private ScrollerLayout getPerScrollerLayout(int linePos, boolean isSortLine) {
        LayoutParams params;
        ScrollerLayout scrollerLayout = new ScrollerLayout(this.mContext);
        scrollerLayout.init(this.mContext, 1);
        scrollerLayout.setClipChildren(false);
        scrollerLayout.setClipToPadding(false);
        scrollerLayout.setTag(Integer.valueOf(linePos));
        scrollerLayout.setSelectPos(this.mDefaultSelectPos);
        scrollerLayout.setScrollerEventListener(this.mScrollerEventListener);
        scrollerLayout.setScrollerDrawListener(this.mScrollerDrawListener);
        scrollerLayout.setFocusable(true);
        scrollerLayout.setOrientation(0);
        int size = Math.min(50, ((TwoLevelTag) this.mTagsList.get(linePos)).tags.size());
        for (int childPos = 0; childPos < size; childPos++) {
            ThreeLevelTag threeLevelTag = (ThreeLevelTag) ((TwoLevelTag) this.mTagsList.get(linePos)).tags.get(childPos);
            TextView textView = new TextView(this.mContext);
            textView.setText(threeLevelTag.f1015n);
            textView.setGravity(17);
            textView.setFocusable(true);
            textView.setSingleLine(true);
            if (childPos == this.mDefaultSelectPos) {
                textView.setSelected(true);
            }
            setMultiMenuTextViewStyle(childPos, textView);
            float textWidth = 0.0f;
            try {
                textWidth = textView.getPaint().measureText(threeLevelTag.f1015n);
            } catch (Exception e) {
            }
            textView.setTag(((TwoLevelTag) this.mTagsList.get(linePos)).tags.get(childPos));
            textView.setTag(MULTI_IS_SORT_LINE, Boolean.valueOf(isSortLine));
            textView.setTag(MULTI_MENU_LINE_NUM, Integer.valueOf(linePos));
            textView.setTag(MULTI_MENU_LINE_POSITION, Integer.valueOf(childPos));
            params = new LayoutParams(ResourceUtil.getDimen(C0508R.dimen.dimen_82dp), ResourceUtil.getDimen(C0508R.dimen.dimen_43dp));
            if (textWidth > ((float) ResourceUtil.getDimen(C0508R.dimen.dimen_102dp))) {
                params.width = ResourceUtil.getDimen(C0508R.dimen.dimen_151dp);
            } else if (textWidth > ((float) ResourceUtil.getDimen(C0508R.dimen.dimen_60dp))) {
                params.width = ResourceUtil.getDimen(C0508R.dimen.dimen_122dp);
            }
            params.leftMargin = (int) getResources().getDimension(C0508R.dimen.dimen_10dp);
            if (linePos < this.mMenuDataLineCount - 1) {
                params.topMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_7dp);
                params.bottomMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_7dp);
            } else {
                params.topMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_7dp);
                params.bottomMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_12dp);
            }
            scrollerLayout.addView(textView, params);
        }
        params = new LayoutParams(ResourceUtil.getDimen(C0508R.dimen.dimen_30dp), ResourceUtil.getDimen(C0508R.dimen.dimen_43dp));
        View spaceView = new View(this.mContext);
        spaceView.setFocusable(false);
        scrollerLayout.addView(spaceView, params);
        return scrollerLayout;
    }

    private void addRightLayout() {
        RelativeLayout.LayoutParams rightParams = new RelativeLayout.LayoutParams(ResourceUtil.getDimen(C0508R.dimen.dimen_1050dp), -2);
        rightParams.leftMargin = (int) getResources().getDimension(C0508R.dimen.dimen_150dp);
        rightParams.topMargin = (int) getResources().getDimension(C0508R.dimen.dimen_20dp);
        addView(this.mRightMenuLayout, rightParams);
        ((ScrollerLayout) this.mScollerMenuList.get(0)).requestFocus();
    }

    private void addLeftLayout() {
        RelativeLayout.LayoutParams leftParams = new RelativeLayout.LayoutParams(ResourceUtil.getDimen(C0508R.dimen.dimen_143dp), -2);
        leftParams.topMargin = ResourceUtil.getDimen(C0508R.dimen.dimen_20dp);
        addView(this.mLeftTitleLayout, leftParams);
    }

    private void setMultiMenuTextViewStyle(int i, TextView textView) {
        textView.setTextColor(ResourceUtil.getColorStateList(C0508R.color.epg_menu_panel_textcolor_selected));
        textView.setBackgroundResource(C0508R.drawable.epg_menu_panel_button);
        textView.setTextSize(0, (float) ResourceUtil.getDimen(C0508R.dimen.dimen_20dp));
    }

    private void initMainLayout() {
        setBackgroundColor(Color.parseColor("#E6000000"));
        setClipChildren(false);
    }

    public View focusSearch(View focused, int direction) {
        FocusFinder ff = FocusFinder.getInstance();
        if (focused.getParent() instanceof ScrollerLayout) {
            int index = ((Integer) ((View) focused.getParent()).getTag()).intValue();
            if (direction == 33) {
                if (index > 0) {
                    index--;
                }
                return (View) this.mScollerMenuList.get(index);
            } else if (direction == Service.CISCO_FNA) {
                if (index < this.mScollerMenuList.size() - 1) {
                    index++;
                }
                return (View) this.mScollerMenuList.get(index);
            }
        }
        return ff.findNextFocus(this, focused, direction);
    }

    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        views.clear();
        for (int i = 0; i < getChildCount(); i++) {
            views.add(getChildAt(i));
        }
        super.addFocusables(views, direction, focusableMode);
    }

    public void requestDefaultFocus() {
        ((ScrollerLayout) this.mScollerMenuList.get(0)).requestFocus();
    }

    public Tag getCheckedTag() {
        StringBuffer stringName = new StringBuffer();
        StringBuffer stringId = new StringBuffer();
        int i = 0;
        for (ScrollerLayout scrollerLayout : this.mScollerMenuList) {
            View selectView = scrollerLayout.getSelectedView();
            if (selectView instanceof TextView) {
                stringName = appendTagStringName(selectView, stringName);
                stringId = appendTagStringId(selectView, stringId, i);
                i++;
            }
        }
        String name = "";
        String id = stringId.toString();
        if (stringName.length() > 1) {
            name = stringName.substring(0, stringName.length() - 1).toString();
        } else {
            name = stringName.toString();
        }
        this.mCheckedTag = new Tag(id, name, "-100", SourceTool.setLayoutKind(String.valueOf(this.mChannelId)));
        return this.mCheckedTag;
    }

    private StringBuffer appendTagStringId(View view, StringBuffer stringId, int i) {
        ThreeLevelTag threeLevelTag = (ThreeLevelTag) view.getTag();
        if (!StringUtils.isEmpty(threeLevelTag.f1016v)) {
            stringId.append(threeLevelTag.f1016v);
            if (i < this.mTagsList.size() - 1) {
                stringId.append(",");
            }
        }
        return stringId;
    }

    private StringBuffer appendTagStringName(View view, StringBuffer stringName) {
        ThreeLevelTag threeLevelTag = (ThreeLevelTag) view.getTag();
        if (!STR_ALL.equals(threeLevelTag.f1015n)) {
            stringName.append(threeLevelTag.f1015n);
            stringName.append("/");
        }
        return stringName;
    }

    private String getThreeLevelTagId(View view) {
        return ((ThreeLevelTag) view.getTag()).f1016v;
    }

    public boolean isSortLineBtn() {
        return this.mIsSortLineBtn;
    }

    public void setSortLineBtn(View view) {
        this.mIsSortLineBtn = ((Boolean) view.getTag(MULTI_IS_SORT_LINE)).booleanValue();
    }

    public void setDefaultSelectPos(int defaultSelectPos) {
        this.mDefaultSelectPos = defaultSelectPos;
    }

    public void selectTargetTag(String targetId, boolean withLoading) {
        selectTargetTag(new String[]{targetId}, withLoading);
    }

    public void selectTargetTag(String[] targetIds, boolean withLoading) {
        if (StringUtils.isEmpty(targetIds)) {
            if (withLoading && this.mPanelListenter != null) {
                this.mPanelListenter.onAlbumDataChanged();
            }
        } else if (this.mIsInitCompleted) {
            for (int i = this.mMenuDataLineCount - 1; i >= 0; i--) {
                ScrollerLayout lineLayout = (ScrollerLayout) this.mScollerMenuList.get(i);
                View newText = null;
                View oldText = null;
                for (int j = 0; j < lineLayout.getChildCount(); j++) {
                    boolean flag = false;
                    View view = lineLayout.getChildAt(j);
                    if (view instanceof TextView) {
                        View pView = view;
                        String tagId = getThreeLevelTagId(pView);
                        for (String targetId : targetIds) {
                            if (targetId.equals(tagId) && !";must".equals(tagId)) {
                                newText = pView;
                                oldText = lineLayout.getSelectedView();
                                lineLayout.setSelectPos(j);
                                lineLayout.setCurViewPos(j);
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            break;
                        }
                    }
                }
                updateCheckedButtonStatus(newText, oldText, lineLayout);
            }
            if (withLoading && this.mPanelListenter != null) {
                this.mPanelListenter.onAlbumDataChanged();
            }
        }
    }

    private boolean updateCheckedButtonStatus(View newButton, View oldButton, ScrollerLayout layout) {
        if (oldButton == null || newButton == null || layout == null) {
            return false;
        }
        for (int j = 0; j < layout.getChildCount(); j++) {
            View view = layout.getChildAt(j);
            if (view != null) {
                view.setSelected(false);
            }
        }
        oldButton.setSelected(false);
        newButton.setSelected(true);
        updateCheckedTag();
        return true;
    }

    private void updateCheckedTag() {
        if (this.mIsInitCompleted) {
            StringBuffer stringName = new StringBuffer();
            StringBuffer stringId = new StringBuffer();
            int size = ListUtils.getCount(this.mScollerMenuList);
            for (int i = 0; i < size; i++) {
                View view = ((ScrollerLayout) this.mScollerMenuList.get(i)).getSelectedView();
                if (view instanceof TextView) {
                    stringName = appendTagStringName(view, stringName);
                    stringId = appendTagStringId(view, stringId, i);
                }
            }
            String name = "";
            String id = stringId.toString();
            if (stringName.length() > 1) {
                name = stringName.substring(0, stringName.length() - 1).toString();
            } else {
                name = stringName.toString();
            }
            this.mCheckedTag = new Tag(id, name, "-100", SourceTool.setLayoutKind(String.valueOf(this.mChannelId)));
        }
    }

    public boolean isInitCompleted() {
        return this.mIsInitCompleted;
    }

    public void setMeltiMenuPanelListener(MultiMenuPanelListenter listenter) {
        this.mPanelListenter = listenter;
    }

    private TextView getMultiMenuTwoLevelLeftTitleTv(String text) {
        TextView titleTxt = new TextView(AppRuntimeEnv.get().getApplicationContext());
        titleTxt.setTextColor(ResourceUtil.getColor(C0508R.color.card_grey_color));
        titleTxt.setSingleLine();
        titleTxt.setTextSize(0, (float) ResourceUtil.getDimen(C0508R.dimen.dimen_20dp));
        titleTxt.setGravity(21);
        titleTxt.setText(text + "       |");
        titleTxt.setFocusable(false);
        return titleTxt;
    }
}
