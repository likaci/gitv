package com.gala.video.app.epg.ui.search.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.ArrayList;
import java.util.List;

public class ExpandCustomView extends FrameLayout {
    private static final int EXPAND_NUM_8 = 8;
    private char mCenterChar;
    private OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            Log.e("CustomGridView", "======onClick======");
            if (ExpandCustomView.this.mRelationKeyItemListener != null) {
                TextView curKeyItem = (TextView) v;
                String keycharString = curKeyItem == null ? "" : (String) curKeyItem.getText();
                Log.e("CustomGridView", "-----onClick----keycharString:" + keycharString);
                ExpandCustomView.this.mRelationKeyItemListener.onItemClick(keycharString);
            }
        }
    };
    private OnClickListener mConfirmClickListener = new OnClickListener() {
        public void onClick(View arg0) {
            if (ExpandCustomView.this.mRelationKeyItemListener != null) {
                ExpandCustomView.this.mRelationKeyItemListener.onConfirmClick();
            }
        }
    };
    private RelativeLayout mConfirmView;
    private Context mContext;
    private OnFocusChangeListener mItemFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            ImageView confirm = null;
            if (v.getId() == R.id.epg_keyItem_confirm) {
                confirm = (ImageView) v.findViewById(R.id.keyitem_confirm_view);
            }
            if (hasFocus) {
                v.bringToFront();
                AnimationUtil.scaleAnimation(v, 1.0f, 1.2f, 200, true);
                if (confirm != null) {
                    confirm.setImageResource(R.drawable.expand_confirm2);
                    return;
                }
                return;
            }
            AnimationUtil.scaleAnimation(v, 1.2f, 1.0f, 200);
            if (confirm != null) {
                confirm.setImageResource(R.drawable.expand_confirm);
            }
        }
    };
    private List<View> mKeyItemList = new ArrayList();
    private RelationKeyItemListener mRelationKeyItemListener;

    public interface RelationKeyItemListener {

        public enum KeyDerect {
            left,
            up,
            right,
            down
        }

        void dismissRelaKeyBoard(char c, KeyDerect keyDerect);

        void onConfirmClick();

        void onInputAdd();

        void onItemClick(String str);
    }

    public ExpandCustomView(Context context, RelationKeyItemListener listener) {
        super(context);
        setRelationKeyItemListener(listener);
        this.mContext = context;
        initView();
        initKeyItem();
    }

    public ExpandCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        initKeyItem();
    }

    public void setRelationKeyItemListener(RelationKeyItemListener listener) {
        this.mRelationKeyItemListener = listener;
    }

    private void initView() {
        addView(LayoutInflater.from(this.mContext).inflate(R.layout.epg_expand_custom_layout, null));
    }

    private void initKeyItem() {
        for (int findViewById : new int[]{R.id.epg_keyItem01, R.id.epg_keyItem02, R.id.epg_keyItem03, R.id.epg_keyItem11, R.id.epg_keyItem12, R.id.epg_keyItem13, R.id.epg_keyItem21, R.id.epg_keyItem22}) {
            View bufView = findViewById(findViewById);
            bufView.setOnClickListener(this.mClickListener);
            bufView.setOnFocusChangeListener(this.mItemFocusChangeListener);
            this.mKeyItemList.add(bufView);
        }
        this.mConfirmView = (RelativeLayout) findViewById(R.id.epg_keyItem_confirm);
        this.mConfirmView.setOnClickListener(this.mConfirmClickListener);
        this.mConfirmView.setOnFocusChangeListener(this.mItemFocusChangeListener);
    }

    public void setKeyChars(String keysString) {
        if (!TextUtils.isEmpty(keysString)) {
            TextView itemBuffTextView;
            Log.e("CustomGridView", "----other---keysString:" + keysString);
            int[] itemIdArray = new int[]{4, 3, 1, 5, 7, 0, 2, 6};
            this.mCenterChar = keysString.charAt(0);
            int i = 0;
            while (i < 8) {
                itemBuffTextView = (TextView) this.mKeyItemList.get(itemIdArray[i]);
                if (itemBuffTextView != null) {
                    String keyStrBuff;
                    if (i <= 0 || keysString.length() == 8) {
                        keyStrBuff = String.valueOf(keysString.charAt(i));
                    } else {
                        keyStrBuff = "";
                    }
                    itemBuffTextView.setText(keyStrBuff);
                    itemBuffTextView.setTag(keyStrBuff);
                    itemBuffTextView.setVisibility(0);
                } else {
                    Log.e("CustomGridView", "itemBuffTextView===null");
                }
                i++;
            }
            int itemIdLength = itemIdArray.length;
            if (i < itemIdLength) {
                while (i < itemIdLength) {
                    itemBuffTextView = (TextView) this.mKeyItemList.get(itemIdArray[i]);
                    if (itemBuffTextView != null) {
                        itemBuffTextView.setTag(null);
                        itemBuffTextView.setText("");
                        itemBuffTextView.setVisibility(8);
                    }
                    i++;
                }
            }
            ((TextView) this.mKeyItemList.get(itemIdArray[0])).requestFocus();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            int curKeyCode = event.getKeyCode();
            if (curKeyCode == 21 || curKeyCode == 22 || curKeyCode == 19 || curKeyCode == 20 || curKeyCode == 23) {
                this.mRelationKeyItemListener.onInputAdd();
            }
            View curFocusView = findFocus();
            if (curFocusView != null) {
                int focusViewId = curFocusView.getId();
                switch (curKeyCode) {
                    case 19:
                        if (focusViewId == R.id.epg_keyItem01 || focusViewId == R.id.epg_keyItem02 || focusViewId == R.id.epg_keyItem03) {
                            this.mRelationKeyItemListener.dismissRelaKeyBoard(this.mCenterChar, KeyDerect.up);
                            return true;
                        }
                    case 20:
                        if (focusViewId == R.id.epg_keyItem_confirm || focusViewId == R.id.epg_keyItem22 || focusViewId == R.id.epg_keyItem21) {
                            this.mRelationKeyItemListener.dismissRelaKeyBoard(this.mCenterChar, KeyDerect.down);
                            return true;
                        }
                    case 21:
                        if (focusViewId == R.id.epg_keyItem01 || focusViewId == R.id.epg_keyItem11 || focusViewId == R.id.epg_keyItem21) {
                            this.mRelationKeyItemListener.dismissRelaKeyBoard(this.mCenterChar, KeyDerect.left);
                            return true;
                        }
                    case 22:
                        if (focusViewId == R.id.epg_keyItem03 || focusViewId == R.id.epg_keyItem13 || focusViewId == R.id.epg_keyItem_confirm) {
                            this.mRelationKeyItemListener.dismissRelaKeyBoard(this.mCenterChar, KeyDerect.right);
                            return true;
                        }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
