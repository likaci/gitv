package com.gala.video.app.epg.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.search.ad.Keys;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.mcto.ads.internal.net.TrackingConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class GALAKeyboard extends RelativeLayout {
    public static final int FIRST_ID = 101;
    public static final int KEYLAYOUT_ACCOUNT_LETTER = 3;
    public static final int KEYLAYOUT_ACCOUNT_LOWER = 1;
    public static final int KEYLAYOUT_ACCOUNT_UPPER = 2;
    public static final int KEYLAYOUT_SEARCH = 0;
    public static final int NUMBER_ID = 110;
    private static final String STR_ABC = "abc 字母";
    private static final String STR_ACTOR = "搜主演";
    private static final String STR_ALBUM = "搜片名";
    private static final String STR_BLANK = "空格";
    private static final String STR_CLEAR = "清空";
    private static final String STR_CONFIRM = "确定";
    private static final String STR_DEL = "删除";
    private static final String STR_LETTER = "</> 符号";
    private static final String STR_LOWER = "小写";
    private static final String STR_UPPER = "大写";
    private static final String[] accountActionLine = new String[]{STR_DEL, STR_DEL, STR_DEL, STR_CLEAR, STR_CLEAR, STR_CLEAR, STR_BLANK, STR_BLANK, STR_BLANK, STR_CONFIRM, STR_CONFIRM, STR_CONFIRM};
    private static List<String> action_strs = new ArrayList();
    private static final String[][] letterLayout = new String[][]{letterLine1, letterLine2, letterLine3, letterSwitchLine, accountActionLine};
    private static final String[] letterLine1 = new String[]{"`", "~", "!", "$", "%", "^", "&", "(", ")", "1", "2", "3"};
    private static final String[] letterLine2 = new String[]{SearchCriteria.EQ, "+", "[", AlbumEnterFactory.SIGN_STR, "{", "}", "\\", "|", "/", "4", "5", "6"};
    private static final String[] letterLine3 = new String[]{";", SOAP.DELIM, "'", "\"", SearchCriteria.LT, SearchCriteria.GT, ",", "?", "0", "7", "8", "9"};
    private static final String[] letterSwitchLine = new String[]{"#", NetDiagnoseCheckTools.NO_CHECK_FLAG, ".", "@", "-", "_", ".com", ".com", ".com", STR_ABC, STR_ABC, STR_ABC};
    private static final String[] lowerCaseLine1 = new String[]{"a", TrackingConstants.TRACKING_KEY_TIMESTAMP, "c", "d", "e", "f", Keys.G, CuteConstants.H, "i", "1", "2", "3"};
    private static final String[] lowerCaseLine2 = new String[]{"j", "k", "l", "m", "n", "o", "p", "q", "r", "4", "5", "6"};
    private static final String[] lowerCaseLine3 = new String[]{"s", PingBackParams.Keys.T, "u", "v", "w", WebConstants.PARAM_KEY_X, WebConstants.PARAM_KEY_Y, "z", "0", "7", "8", "9"};
    private static final String[] lowerCaseSwitchLine = new String[]{STR_UPPER, STR_UPPER, STR_UPPER, "@", "-", "_", ".com", ".com", ".com", STR_LETTER, STR_LETTER, STR_LETTER};
    private static final String[][] lowerLayout = new String[][]{lowerCaseLine1, lowerCaseLine2, lowerCaseLine3, lowerCaseSwitchLine, accountActionLine};
    private static final String[] searchActionLine = new String[]{STR_DEL, STR_DEL, STR_DEL, STR_CLEAR, STR_CLEAR, STR_CLEAR, STR_ALBUM, STR_ALBUM, STR_ALBUM, STR_ACTOR, STR_ACTOR, STR_ACTOR};
    private static final String[][] searchLayout = new String[][]{upperCaseLine1, upperCaseLine2, upperCaseLine3, searchActionLine};
    private static HashMap<String, Integer> str_drawable = new HashMap();
    private static final String[] upperCaseLine1 = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "1", "2", "3"};
    private static final String[] upperCaseLine2 = new String[]{"J", "K", "L", "M", "N", "O", "P", "Q", "R", "4", "5", "6"};
    private static final String[] upperCaseLine3 = new String[]{"S", "T", "U", "V", "W", "X", "Y", "Z", "0", "7", "8", "9"};
    private static final String[] upperCaseSwitchLine = new String[]{STR_LOWER, STR_LOWER, STR_LOWER, "@", "-", "_", ".com", ".com", ".com", STR_LETTER, STR_LETTER, STR_LETTER};
    private static final String[][] upperLayout = new String[][]{upperCaseLine1, upperCaseLine2, upperCaseLine3, upperCaseSwitchLine, accountActionLine};
    private int bottomPadding = ((int) getResources().getDimension(R.dimen.dimen_010dp));
    private int clearId = 504;
    private int commitId = WidgetType.ITEM_TEXTVIEW;
    private int focusId = 0;
    @SuppressLint({"UseSparseArrays"})
    private Map<Integer, Integer> focusTreatMap;
    private int iconW = ((int) getResources().getDimension(R.dimen.dimen_34dp));
    private IKeyboardListener keyboardListener;
    private int mConfirmResId = 0;
    private String mConfirmText = null;
    private OnFocusChangeListener mKeyFocusChangeLisener;
    private OnClickListener mOnClickListener;
    private StringBuffer mTextBuffer = new StringBuffer();
    private int mTopFocusForwardID = -1;
    private int margin = ((int) getResources().getDimension(R.dimen.dimen_015dp));
    private int singleItemHeight = ((int) getResources().getDimension(R.dimen.dimen_90dp));
    private int singleItemWidth = ((int) getResources().getDimension(R.dimen.dimen_120dp));
    private float textSize = getResources().getDimension(R.dimen.dimen_34sp);

    public GALAKeyboard(Context context) {
        super(context);
        action_strs.add(STR_ACTOR);
        action_strs.add(STR_ALBUM);
        action_strs.add(STR_DEL);
        action_strs.add(STR_CLEAR);
        action_strs.add(STR_UPPER);
        action_strs.add(STR_LOWER);
        action_strs.add(STR_LETTER);
        action_strs.add(STR_ABC);
        action_strs.add(STR_BLANK);
        action_strs.add(STR_CONFIRM);
        str_drawable.put(STR_DEL, Integer.valueOf(R.drawable.epg_ico_keyboard_del));
        str_drawable.put(STR_CLEAR, Integer.valueOf(R.drawable.epg_ico_keyboard_clear));
        str_drawable.put(STR_ALBUM, Integer.valueOf(R.drawable.epg_ico_keyboard_album));
        str_drawable.put(STR_ACTOR, Integer.valueOf(R.drawable.epg_ico_keyboard_actor));
        str_drawable.put(STR_BLANK, Integer.valueOf(R.drawable.epg_ico_keyboard_login_space));
        str_drawable.put(STR_LOWER, Integer.valueOf(R.drawable.epg_ico_keyboard_login_lower));
        str_drawable.put(STR_UPPER, Integer.valueOf(R.drawable.epg_ico_keyboard_login_upper));
        str_drawable.put(STR_CONFIRM, Integer.valueOf(R.drawable.epg_ico_keyboard_login_confirm));
        this.mOnClickListener = new OnClickListener() {
            public void onClick(View v) {
                AnimationUtil.clickScaleAnimation(v);
                String s = (String) v.getTag();
                if (GALAKeyboard.action_strs.contains(s)) {
                    GALAKeyboard.this.focusId = v.getId();
                    int textLength = GALAKeyboard.this.mTextBuffer.length();
                    if (GALAKeyboard.STR_DEL.equals(s)) {
                        if (textLength > 0) {
                            GALAKeyboard.this.mTextBuffer.deleteCharAt(textLength - 1);
                        }
                        GALAKeyboard.this.notifyTextChange();
                        return;
                    } else if (GALAKeyboard.STR_CLEAR.equals(s)) {
                        GALAKeyboard.this.mTextBuffer.delete(0, textLength);
                        GALAKeyboard.this.notifyTextChange();
                        return;
                    } else if (GALAKeyboard.STR_BLANK.equals(s)) {
                        GALAKeyboard.this.mTextBuffer.append(" ");
                        GALAKeyboard.this.notifyTextChange();
                        return;
                    } else if (GALAKeyboard.STR_UPPER.equals(s)) {
                        GALAKeyboard.this.initKeyLayout(GALAKeyboard.upperLayout);
                        return;
                    } else if (GALAKeyboard.STR_LETTER.equals(s)) {
                        GALAKeyboard.this.initKeyLayout(GALAKeyboard.letterLayout);
                        return;
                    } else if (GALAKeyboard.STR_ACTOR.equals(s)) {
                        GALAKeyboard.this.notifySearchActor();
                        return;
                    } else if (GALAKeyboard.STR_LOWER.equals(s) || GALAKeyboard.STR_ABC.equals(s)) {
                        GALAKeyboard.this.initKeyLayout(GALAKeyboard.lowerLayout);
                        return;
                    } else if (GALAKeyboard.STR_CONFIRM.equals(s) || GALAKeyboard.STR_ALBUM.equals(s)) {
                        GALAKeyboard.this.notifyCommit();
                        return;
                    } else {
                        return;
                    }
                }
                GALAKeyboard.this.mTextBuffer.append(s);
                GALAKeyboard.this.notifyTextChange();
            }
        };
        this.focusTreatMap = new HashMap();
        this.focusTreatMap.put(Integer.valueOf(33), Integer.valueOf(44));
        this.focusTreatMap.put(Integer.valueOf(37), Integer.valueOf(46));
        this.mKeyFocusChangeLisener = new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (Project.getInstance().getControl().isOpenAnimation()) {
                    AnimationUtil.zoomAnimation(v, hasFocus, 1.05f, hasFocus ? 200 : 0, true);
                }
                if (hasFocus) {
                    ((View) v.getParent()).bringToFront();
                    v.bringToFront();
                }
                for (Integer upId : GALAKeyboard.this.focusTreatMap.keySet()) {
                    int downId = ((Integer) GALAKeyboard.this.focusTreatMap.get(upId)).intValue();
                    if (v.getId() == upId.intValue() && GALAKeyboard.this.findViewById(downId) != null) {
                        GALAKeyboard.this.findViewById(downId).bringToFront();
                    }
                }
            }
        };
        setPadding(0, 0, 0, this.bottomPadding);
    }

    public GALAKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        action_strs.add(STR_ACTOR);
        action_strs.add(STR_ALBUM);
        action_strs.add(STR_DEL);
        action_strs.add(STR_CLEAR);
        action_strs.add(STR_UPPER);
        action_strs.add(STR_LOWER);
        action_strs.add(STR_LETTER);
        action_strs.add(STR_ABC);
        action_strs.add(STR_BLANK);
        action_strs.add(STR_CONFIRM);
        str_drawable.put(STR_DEL, Integer.valueOf(R.drawable.epg_ico_keyboard_del));
        str_drawable.put(STR_CLEAR, Integer.valueOf(R.drawable.epg_ico_keyboard_clear));
        str_drawable.put(STR_ALBUM, Integer.valueOf(R.drawable.epg_ico_keyboard_album));
        str_drawable.put(STR_ACTOR, Integer.valueOf(R.drawable.epg_ico_keyboard_actor));
        str_drawable.put(STR_BLANK, Integer.valueOf(R.drawable.epg_ico_keyboard_login_space));
        str_drawable.put(STR_LOWER, Integer.valueOf(R.drawable.epg_ico_keyboard_login_lower));
        str_drawable.put(STR_UPPER, Integer.valueOf(R.drawable.epg_ico_keyboard_login_upper));
        str_drawable.put(STR_CONFIRM, Integer.valueOf(R.drawable.epg_ico_keyboard_login_confirm));
        this.mOnClickListener = /* anonymous class already generated */;
        this.focusTreatMap = new HashMap();
        this.focusTreatMap.put(Integer.valueOf(33), Integer.valueOf(44));
        this.focusTreatMap.put(Integer.valueOf(37), Integer.valueOf(46));
        this.mKeyFocusChangeLisener = /* anonymous class already generated */;
        setPadding(0, 0, 0, this.bottomPadding);
    }

    public GALAKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        action_strs.add(STR_ACTOR);
        action_strs.add(STR_ALBUM);
        action_strs.add(STR_DEL);
        action_strs.add(STR_CLEAR);
        action_strs.add(STR_UPPER);
        action_strs.add(STR_LOWER);
        action_strs.add(STR_LETTER);
        action_strs.add(STR_ABC);
        action_strs.add(STR_BLANK);
        action_strs.add(STR_CONFIRM);
        str_drawable.put(STR_DEL, Integer.valueOf(R.drawable.epg_ico_keyboard_del));
        str_drawable.put(STR_CLEAR, Integer.valueOf(R.drawable.epg_ico_keyboard_clear));
        str_drawable.put(STR_ALBUM, Integer.valueOf(R.drawable.epg_ico_keyboard_album));
        str_drawable.put(STR_ACTOR, Integer.valueOf(R.drawable.epg_ico_keyboard_actor));
        str_drawable.put(STR_BLANK, Integer.valueOf(R.drawable.epg_ico_keyboard_login_space));
        str_drawable.put(STR_LOWER, Integer.valueOf(R.drawable.epg_ico_keyboard_login_lower));
        str_drawable.put(STR_UPPER, Integer.valueOf(R.drawable.epg_ico_keyboard_login_upper));
        str_drawable.put(STR_CONFIRM, Integer.valueOf(R.drawable.epg_ico_keyboard_login_confirm));
        this.mOnClickListener = /* anonymous class already generated */;
        this.focusTreatMap = new HashMap();
        this.focusTreatMap.put(Integer.valueOf(33), Integer.valueOf(44));
        this.focusTreatMap.put(Integer.valueOf(37), Integer.valueOf(46));
        this.mKeyFocusChangeLisener = /* anonymous class already generated */;
        setPadding(0, 0, 0, this.bottomPadding);
    }

    public void initKeyLayout(int layoutType) {
        this.singleItemWidth = ((getContext().getResources().getDisplayMetrics().widthPixels - 6) / 12) - (this.margin * 2);
        switch (layoutType) {
            case 0:
                initKeyLayout(searchLayout);
                return;
            case 1:
                initKeyLayout(lowerLayout);
                return;
            case 2:
                initKeyLayout(upperLayout);
                return;
            case 3:
                initKeyLayout(letterLayout);
                return;
            default:
                initKeyLayout(lowerLayout);
                return;
        }
    }

    public void initKeyLayout(int layoutType, int forwordId) {
        this.mTopFocusForwardID = forwordId;
        this.singleItemWidth = ((getContext().getResources().getDisplayMetrics().widthPixels - 6) / 12) - (this.margin * 2);
        switch (layoutType) {
            case 0:
                initKeyLayout(searchLayout);
                return;
            case 1:
                initKeyLayout(lowerLayout);
                return;
            case 2:
                initKeyLayout(upperLayout);
                return;
            case 3:
                initKeyLayout(letterLayout);
                return;
            default:
                initKeyLayout(lowerLayout);
                return;
        }
    }

    private void initKeyLayout(String[][] ss) {
        detachAllViewsFromParent();
        RelativeLayout topView = null;
        int i = 0;
        while (i < ss.length) {
            topView = addLine(topView, ss[i], i + 1, i == ss.length + -1);
            i++;
        }
        requestFocus();
        restoreFocus(101);
    }

    private RelativeLayout addLine(View lineTop, String[] chars, int row, boolean isBottomLine) {
        int leftId = 0;
        RelativeLayout lineLayout = new RelativeLayout(getContext());
        lineLayout.setId(row);
        lineLayout.setGravity(17);
        LayoutParams p = new LayoutParams(-1, -2);
        p.addRule(14);
        p.bottomMargin = this.margin;
        if (lineTop != null) {
            p.topMargin = this.margin;
            p.addRule(3, lineTop.getId());
        }
        addView(lineLayout, p);
        View leftView = null;
        View rightView = null;
        int itemWidthCount = 1;
        for (int i = 0; i < chars.length; i++) {
            int itemWidth = this.singleItemWidth;
            if (i == chars.length - 1) {
                itemWidth = (this.singleItemWidth * itemWidthCount) + ((this.margin * 2) * (itemWidthCount - 1));
            } else if (chars[i].equals(chars[i + 1])) {
                itemWidthCount++;
            } else {
                itemWidth = (this.singleItemWidth * itemWidthCount) + ((this.margin * 2) * (itemWidthCount - 1));
            }
            View buttonItem = initItemView(chars[i], generateId(row, i + 1));
            LayoutParams lp = new LayoutParams(itemWidth, this.singleItemHeight);
            if (i > 0) {
                lp.addRule(1, leftId);
            }
            lp.leftMargin = this.margin;
            lp.rightMargin = this.margin;
            leftId = buttonItem.getId();
            if (str_drawable.containsKey(chars[i])) {
                Paint paint = buttonItem.getPaint();
                int textWidth = (int) paint.measureText(chars[i]);
                if (STR_CONFIRM.equals(chars[i]) && this.mConfirmText != null) {
                    textWidth = (int) paint.measureText(this.mConfirmText);
                }
                int padding = (((itemWidth / 2) + this.margin) - (this.iconW / 2)) - (textWidth / 2);
                if (!STR_CONFIRM.equals(chars[i]) || this.mConfirmResId <= 0) {
                    setLeftDrawable(buttonItem, ((Integer) str_drawable.get(chars[i])).intValue(), this.iconW, this.iconW, padding);
                } else {
                    setLeftDrawable(buttonItem, this.mConfirmResId, this.iconW, this.iconW, padding);
                }
            }
            if (row == 1 && this.mTopFocusForwardID != -1) {
                buttonItem.setNextFocusUpId(this.mTopFocusForwardID);
            }
            lineLayout.addView(buttonItem, lp);
            itemWidthCount = 1;
            buttonItem.setPivotX((float) (itemWidth / 2));
            buttonItem.setPivotY((float) (this.singleItemHeight / 2));
            if (leftView == null) {
                leftView = buttonItem;
                buttonItem.setPivotX(0.0f);
            } else if (i == chars.length - 1) {
                rightView = buttonItem;
                buttonItem.setPivotX((float) itemWidth);
            }
            if (isBottomLine) {
                buttonItem.setPivotY((float) this.singleItemHeight);
            }
        }
        leftView.setNextFocusLeftId(rightView.getId());
        rightView.setNextFocusRightId(leftView.getId());
        return lineLayout;
    }

    public void setKeyListener(IKeyboardListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public void setConfirmTextAndDrawable(int confirmTextId, int resId) {
        this.mConfirmText = getContext().getString(confirmTextId);
        this.mConfirmResId = resId;
        initKeyLayout(lowerLayout);
    }

    public void restoreFocus(int focusId) {
        if (focusId != 0 && findViewById(focusId) != null) {
            findViewById(focusId).requestFocus();
        }
    }

    public int getFocusId() {
        return this.focusId;
    }

    public int getClearId() {
        return this.clearId;
    }

    public int getCommitId() {
        return this.commitId;
    }

    private Button initItemView(String c, int id) {
        Button btn = new Button(getContext());
        String text = c;
        if (STR_CONFIRM.equals(c)) {
            this.commitId = id;
            if (this.mConfirmText != null) {
                text = this.mConfirmText;
            }
        }
        if (STR_CLEAR.equals(c)) {
            this.clearId = id;
        }
        btn.setId(id);
        btn.setTag(c);
        btn.setText(text);
        btn.setTextSize(0, this.textSize);
        btn.setTextColor(-921103);
        if (Project.getInstance().getBuild().isLitchi()) {
            btn.setBackgroundResource(R.drawable.epg_keyboard_key_bg2);
        } else {
            btn.setBackgroundResource(R.drawable.epg_keyboard_key_bg2);
        }
        btn.setOnClickListener(this.mOnClickListener);
        btn.setOnFocusChangeListener(this.mKeyFocusChangeLisener);
        return btn;
    }

    private int generateId(int row, int column) {
        return (row * 100) + column;
    }

    private void setLeftDrawable(TextView tv, int id, int w, int h, int padding) {
        setLeftDrawable(tv, id, w, h);
        tv.setGravity(17);
        tv.setPadding(padding, 0, padding, 0);
    }

    private void setLeftDrawable(TextView tv, int id, int w, int h) {
        Drawable d = getResources().getDrawable(id);
        d.setBounds(0, 0, w, h);
        tv.setCompoundDrawables(d, null, null, null);
    }

    public void updateTextBuffer(String s) {
        this.mTextBuffer.delete(0, this.mTextBuffer.length());
        this.mTextBuffer.append(s);
    }

    private void notifyTextChange() {
        if (this.keyboardListener != null) {
            this.keyboardListener.onTextChange(this.mTextBuffer.toString());
        }
    }

    private void notifyCommit() {
        if (this.keyboardListener != null) {
            this.keyboardListener.onCommit(this.mTextBuffer.toString());
        }
    }

    private void notifySearchActor() {
        if (this.keyboardListener != null) {
            this.keyboardListener.onSearchActor(this.mTextBuffer.toString());
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            int keyCode = event.getKeyCode();
            if (keyCode >= 7 && keyCode <= 16) {
                this.mTextBuffer.append((keyCode - 7) + "");
                notifyTextChange();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
