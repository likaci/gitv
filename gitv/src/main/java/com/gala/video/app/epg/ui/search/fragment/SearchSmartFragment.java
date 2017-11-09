package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.search.keybord.manager.KeyboardManager;
import com.gala.video.app.epg.ui.search.utils.ExpandUtils;
import com.gala.video.app.epg.ui.search.widget.ExpandCustomView;
import com.gala.video.app.epg.ui.search.widget.ExpandCustomView.RelationKeyItemListener;
import com.gala.video.app.epg.ui.search.widget.ExpandCustomView.RelationKeyItemListener.KeyDerect;
import com.gala.video.app.epg.ui.search.widget.ExpandRalationItem;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.HashMap;
import java.util.Map;
import org.cybergarage.soap.SOAP;

public class SearchSmartFragment extends SearchBaseFragment {
    private static final int KEYBOARD_COls_ROWS = 6;
    public static final String KEYS_KEYBORD_TYPE = "keys-rel-type";
    private final String TAG = getClass().getName();
    private View mClickItem;
    private ExpandCustomView mCustomGridView;
    protected View mFocusView;
    private OnFocusChangeListener mGridFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (SearchSmartFragment.this.mContext != null) {
                int id = v.getId();
                int colorFocus = SearchSmartFragment.this.mContext.getResources().getColor(R.color.gala_write);
                int colorUnFocus = SearchSmartFragment.this.mContext.getResources().getColor(R.color.keyboard_letter);
                int colorNum = SearchSmartFragment.this.mContext.getResources().getColor(R.color.keyboard_num);
                boolean isContainId = id == 70 || id == 76 || id == 82 || id == 88 || id == 94 || id == 100;
                if (isContainId && SearchSmartFragment.this.mKeyCode == 22 && hasFocus) {
                    if (hasFocus) {
                        SearchSmartFragment.this.mFocusView = v;
                        ((TextView) v).setTextColor(colorFocus);
                        AnimationUtil.scaleAnimation(v, 1.0f, 1.1f, 200);
                    }
                    ((TextView) v).setTextColor(colorUnFocus);
                    ((TextView) v).setTextColor(colorNum);
                    AnimationUtil.scaleAnimation(v, 1.1f, 1.0f, 200);
                } else if (hasFocus) {
                    SearchSmartFragment.this.mFocusView = v;
                    ((TextView) v).setTextColor(colorFocus);
                    AnimationUtil.scaleAnimation(v, 1.0f, 1.1f, 200);
                } else {
                    ((TextView) v).setTextColor(colorUnFocus);
                    if (id >= 91 && id <= 100) {
                        ((TextView) v).setTextColor(colorNum);
                    }
                    AnimationUtil.scaleAnimation(v, 1.1f, 1.0f, 200);
                }
            }
        }
    };
    int mIndex = 0;
    private boolean mIsClickLayer;
    private boolean mIsRequestFocus;
    private Map<String, ExpandRalationItem> mKeyBorderMap = new HashMap();
    private String mKeyChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private int mKeyCode;
    private int mKeyItemWidth;
    private View mMainView;
    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == 1) {
                SearchSmartFragment.this.onClickAction(v);
            }
            return true;
        }
    };
    private PopupWindow mPopupWindow;
    private String mPreChar;
    private int mRelationKeyCount = 9;
    private RelationKeyItemListener mRelationKeyItemListener = new RelationKeyItemListener() {
        public void onItemClick(String curKeyChar) {
            Log.e(SearchSmartFragment.this.TAG, "===onItemClick===curKeyChar:" + curKeyChar);
            SearchSmartFragment.this.mIsRequestFocus = false;
            SearchSmartFragment.this.mPopupWindow.dismiss();
            if (!StringUtils.isEmpty((CharSequence) curKeyChar)) {
                SearchSmartFragment.this.mIsClickLayer = true;
                ExpandRalationItem relationKeyItem = (ExpandRalationItem) SearchSmartFragment.this.mKeyBorderMap.get(curKeyChar);
                if (relationKeyItem != null) {
                    TextView nextFocusView = relationKeyItem.mKeyItemView;
                    nextFocusView.requestFocus();
                    SearchSmartFragment.this.onClickAction(nextFocusView);
                }
            }
        }

        public void dismissRelaKeyBoard(char centerKeyChar, KeyDerect keyDerect) {
            TextView upLeftTextView = null;
            Log.e(SearchSmartFragment.this.TAG, "centerKeyChar:" + centerKeyChar);
            ExpandRalationItem curKeyItem = (ExpandRalationItem) SearchSmartFragment.this.mKeyBorderMap.get(String.valueOf(centerKeyChar));
            String rectionCharString = null;
            if (keyDerect == KeyDerect.left) {
                rectionCharString = curKeyItem.getLeftKeyChar();
            } else if (keyDerect == KeyDerect.right) {
                rectionCharString = curKeyItem.getRightKeyChar();
            } else if (keyDerect == KeyDerect.up) {
                rectionCharString = curKeyItem.getUpKeyChar();
            } else if (keyDerect == KeyDerect.down) {
                rectionCharString = curKeyItem.getDownKeyChar();
            }
            if (rectionCharString != null) {
                curKeyItem = (ExpandRalationItem) SearchSmartFragment.this.mKeyBorderMap.get(rectionCharString);
                if (curKeyItem != null) {
                    upLeftTextView = curKeyItem.mKeyItemView;
                }
            }
            if (upLeftTextView != null) {
                SearchSmartFragment.this.mIsRequestFocus = false;
                upLeftTextView.requestFocus();
            } else if (keyDerect != KeyDerect.right && keyDerect == KeyDerect.up) {
            }
            SearchSmartFragment.this.mPopupWindow.dismiss();
        }

        public void onConfirmClick() {
            if (SearchSmartFragment.this.mSearchEvent != null) {
                if (SearchSmartFragment.this.mSearchEvent.isSuggestDisplay()) {
                    SearchSmartFragment.this.mIsRequestFocus = false;
                }
                PingBackParams params = new PingBackParams();
                params.add(Keys.T, "20").add("rseat", "done").add("rpage", "srch_keyboard").add("block", "smart_suggest").add("rt", "i");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                SearchSmartFragment.this.mPopupWindow.dismiss();
                SearchSmartFragment.this.mSearchEvent.onRequestRightDefaultFocus();
            }
        }

        public void onInputAdd() {
            if (SearchSmartFragment.this.mSearchEvent != null) {
                SearchSmartFragment.this.mSearchEvent.onInputAdd();
            }
        }
    };
    private String mRelationkeys;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public void onDetach() {
        super.onDetach();
        if (this.mPopupWindow != null) {
            this.mPopupWindow.dismiss();
        }
    }

    public int getPageLocationType() {
        return 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onResume() {
        super.onResume();
        this.mRelationkeys = null;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_expand_keyboard, null);
        this.mRelationKeyCount = 8;
        Log.e("jaunce", "mRelationKeyCount:" + this.mRelationKeyCount);
        initKeyboard();
        if (mIsRequireFocus) {
            ((ExpandRalationItem) this.mKeyBorderMap.get("O")).mKeyItemView.post(new Runnable() {
                public void run() {
                    ((ExpandRalationItem) SearchSmartFragment.this.mKeyBorderMap.get("O")).mKeyItemView.requestFocus();
                }
            });
        }
        initPopupWindow();
        return this.mMainView;
    }

    private void initPopupWindow() {
        if (this.mContext != null) {
            this.mCustomGridView = new ExpandCustomView(this.mContext, this.mRelationKeyItemListener);
            this.mKeyItemWidth = (int) this.mContext.getResources().getDimension(R.dimen.dimen_85dp);
            int width = this.mKeyItemWidth * 3;
            this.mPopupWindow = new PopupWindow(this.mCustomGridView, width, width, true);
            this.mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
            this.mPopupWindow.setTouchable(false);
            this.mPopupWindow.setOutsideTouchable(true);
            this.mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_drawable));
            this.mPopupWindow.setOnDismissListener(new OnDismissListener() {
                public void onDismiss() {
                    if (SearchSmartFragment.this.mClickItem != null) {
                        SearchSmartFragment.this.mClickItem.setBackgroundResource(R.drawable.epg_full_keyboard_bg);
                        if (SearchSmartFragment.this.mIsRequestFocus) {
                            SearchSmartFragment.this.mClickItem.requestFocus();
                        }
                    }
                }
            });
        }
    }

    private void initKeyboard() {
        if (this.mContext != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(this.mContext);
            LinearLayout rootLayout = (LinearLayout) this.mMainView.findViewById(R.id.epg_keyboard_container);
            rootLayout.setClipChildren(false);
            rootLayout.setClipToPadding(false);
            int lineHeight = (int) this.mContext.getResources().getDimension(R.dimen.dimen_85dp);
            int itemId = 65;
            for (int i = 0; i < 6; i++) {
                View rowsLayout = new LinearLayout(this.mContext);
                rowsLayout.setGravity(17);
                rowsLayout.setOrientation(0);
                rowsLayout.setClipToPadding(false);
                rowsLayout.setClipChildren(false);
                LayoutParams llLayoutParams = new LayoutParams(-1, lineHeight);
                int dimenH = (int) this.mContext.getResources().getDimension(R.dimen.dimen_13dp);
                llLayoutParams.setMargins(-dimenH, -dimenH, -dimenH, -dimenH);
                rootLayout.addView(rowsLayout, llLayoutParams);
                for (int j = 0; j < 6; j++) {
                    TextView keyItemTextView = (TextView) layoutInflater.inflate(R.layout.epg_expand_keyitem, null);
                    keyItemTextView.setId(itemId);
                    keyItemTextView.setFocusable(true);
                    String curCharString = getKeyChar();
                    keyItemTextView.setText(curCharString);
                    keyItemTextView.setTag(curCharString);
                    keyItemTextView.setBackgroundResource(R.drawable.epg_full_keyboard_bg);
                    if (this.mIndex > 26) {
                        keyItemTextView.setTextColor(this.mContext.getResources().getColor(R.color.keyboard_num));
                    }
                    keyItemTextView.setOnFocusChangeListener(this.mGridFocusChangeListener);
                    keyItemTextView.setOnTouchListener(this.mOnTouchListener);
                    ExpandRalationItem bufferKeyItem = new ExpandRalationItem();
                    bufferKeyItem.mKeyItemView = keyItemTextView;
                    this.mKeyBorderMap.put(curCharString, bufferKeyItem);
                    Log.e(this.TAG, curCharString + SOAP.DELIM + itemId);
                    setKeyItemNextId(bufferKeyItem.mKeyItemView);
                    itemId++;
                    llLayoutParams = new LayoutParams(lineHeight, lineHeight);
                    llLayoutParams.gravity = 1;
                    int dimenV = (int) this.mContext.getResources().getDimension(R.dimen.dimen_10dp);
                    llLayoutParams.setMargins(-dimenV, -dimenV, -dimenV, -dimenV);
                    rowsLayout.addView(keyItemTextView, llLayoutParams);
                }
            }
        }
    }

    private void setKeyItemNextId(TextView curKeyTextView) {
        if (curKeyTextView != null) {
            StringBuffer bufferTestBuffer = new StringBuffer();
            int curViewId = curKeyTextView.getId();
            bufferTestBuffer.append(curViewId - 1);
            curKeyTextView.setNextFocusLeftId(curViewId - 1);
            bufferTestBuffer.append(",");
            if (curViewId < 65 || curViewId > 70) {
                bufferTestBuffer.append(curViewId - 6);
                curKeyTextView.setNextFocusUpId(curViewId - 6);
            } else {
                bufferTestBuffer.append(-1);
                curKeyTextView.setNextFocusUpId(-1);
            }
            bufferTestBuffer.append(",");
            if (curViewId < 95 || curViewId > 100) {
                bufferTestBuffer.append(curViewId + 6);
                curKeyTextView.setNextFocusDownId(curViewId + 6);
            } else {
                bufferTestBuffer.append(-1);
                curKeyTextView.setNextFocusDownId(-1);
            }
            bufferTestBuffer.append(",");
            if ((curViewId - 65) % 6 == 5) {
                bufferTestBuffer.append(-1);
                curKeyTextView.setNextFocusRightId(-1);
            } else {
                bufferTestBuffer.append(curViewId + 1);
                curKeyTextView.setNextFocusRightId(curViewId + 1);
            }
            Log.e(this.TAG, "curChar:" + ((char) curViewId) + "----LUDR:" + bufferTestBuffer.toString());
        }
    }

    private String getKeyChar() {
        if (this.mIndex >= this.mKeyChars.length()) {
            return "";
        }
        String curCharString = String.valueOf(this.mKeyChars.charAt(this.mIndex));
        this.mIndex++;
        return curCharString;
    }

    private void onClickAction(View v) {
        String curChar = ((TextView) v).getText().toString();
        Log.e(this.TAG, "curChar:" + curChar);
        if (!TextUtils.isEmpty(curChar) && SearchEnterUtils.checkNetWork(this.mContext) && this.mSearchEvent != null) {
            if (this.mSearchEvent.onInputText(curChar)) {
                this.mSearchEvent.onKeyBoardTextChanged();
            }
            String letterExist = "";
            if (!this.mIsClickLayer) {
                letterExist = getLetterExsit(curChar);
            }
            char[] chars = KeyboardManager.get().getKeyboard(this.mSearchEvent.onGetSearchText());
            if (chars == null) {
                chars = ISearchConstant.EXPAND_RELATION_DEFAULT;
            }
            this.mRelationkeys = ExpandUtils.getRelationString(curChar, chars);
            Log.e(this.TAG, "keysString：" + this.mRelationkeys);
            if (!TextUtils.isEmpty(this.mRelationkeys)) {
                this.mClickItem = v;
                this.mClickItem.setBackgroundDrawable(null);
                this.mIsRequestFocus = true;
                sendClickPingback(this.mIsClickLayer, curChar, letterExist);
                if (this.mIsClickLayer && this.mPreChar != null && !curChar.equals(this.mPreChar)) {
                    showPopWindow(v, this.mRelationkeys, (-this.mKeyItemWidth) + ((int) this.mContext.getResources().getDimension(R.dimen.dimen_15dp)), (this.mKeyItemWidth - ((int) this.mContext.getResources().getDimension(R.dimen.dimen_9dp))) * -2);
                } else if (this.mContext != null) {
                    showPopWindow(v, this.mRelationkeys, (-this.mKeyItemWidth) + ((int) this.mContext.getResources().getDimension(R.dimen.dimen_20dp)), (this.mKeyItemWidth - ((int) this.mContext.getResources().getDimension(R.dimen.dimen_10dp))) * -2);
                } else {
                    return;
                }
                this.mPreChar = curChar;
            }
        }
    }

    private void sendClickPingback(boolean mIsClickLayer, String curChar, String letterExist) {
        String block = mIsClickLayer ? "smart_suggest" : "smart_abc";
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "20").add("rseat", curChar).add("rpage", "srch_keyboard").add("block", block).add("rt", "i").add("letter_exist", letterExist);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private void showPopWindow(View v, String keysString, int xoff, int yoff) {
        this.mCustomGridView.setKeyChars(keysString);
        this.mPopupWindow.showAsDropDown(v, xoff, yoff);
        this.mIsClickLayer = false;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0 || this.mSearchEvent == null) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
                this.mSearchEvent.onInputAdd();
                break;
            case 23:
            case 66:
                this.mSearchEvent.onInputAdd();
                if (this.mFocusView != null && getView().hasFocus()) {
                    onClickAction(this.mFocusView);
                    return true;
                }
        }
        if (keyCode >= 7 && keyCode <= 16 && this.mSearchEvent != null) {
            if (this.mSearchEvent.onInputText((keyCode - 7) + "")) {
                this.mSearchEvent.onKeyBoardTextChanged();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private String getLetterExsit(String str) {
        if (StringUtils.isEmpty(this.mRelationkeys)) {
            return "NA";
        }
        String result = "0";
        int length = this.mRelationkeys.length();
        for (int i = 0; i < length; i++) {
            if (str.equals("" + this.mRelationkeys.charAt(i))) {
                return "1";
            }
        }
        return result;
    }
}
