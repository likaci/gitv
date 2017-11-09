package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C0165R;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QPLD;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.BaseAdData;
import com.gala.sdk.player.BitStream;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.overlay.OnAdStateListener;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.ui.widget.BitStreamGuideDialog;
import com.gala.video.app.player.ui.widget.BitStreamGuideDialog.OnHDRToggleListener;
import com.gala.video.app.player.ui.widget.BitStreamGuideDialog.OnHDRUserPlayListener;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDataUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.widget.MyRadioGroup;
import com.gala.video.widget.MyRadioGroup.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BitStreamContent extends AbsTabContent<List<BitStream>, BitStream> {
    private static final String HDR_4K_PINGBACK_TAG = "4k";
    public static final int HDR_GUIDE_ONPAUSE = 4;
    public static final int HDR_GUIDE_ONPLAY = 5;
    private static final String HDR_PINGBACK_TAG = "hdr";
    private static final int HDR_TOGGLE_CLICK_OPEN = 3;
    private static final int HDR_TOGGLE_CLOSE = 1;
    private static final List<Pair<Integer, Integer>> HDR_TOGGLE_LIST = new ArrayList();
    private static final int HDR_TOGGLE_OPEN = 0;
    private final String TAG = ("Player/Ui/BitStreamContent@" + Integer.toHexString(hashCode()));
    private BaseAdData mAdData;
    private RelativeLayout mAdView = null;
    private List<BitStream> mAllBitStreamList = new CopyOnWriteArrayList();
    private BitStreamGuideDialog mBitStreamDialog;
    private MyRadioGroup mBitStreamGroupView;
    private TextView mBitStreamTitle;
    private List<BitStream> mBitStreams = new CopyOnWriteArrayList();
    private int mCheckedDefIndex = -1;
    private int mCheckedIndex;
    private Context mContext;
    private BitStream mCurBitStream;
    private boolean mDataInited;
    private RelativeLayout mDefinitionPanel;
    private List<BitStream> mHDRBitStreamList = new CopyOnWriteArrayList();
    private LinearLayout mHDRLayout;
    private MyRadioGroup mHDRToggleGroupView;
    private boolean mIsShown;
    private IItemListener<BitStream> mItemListener;
    private OnCheckedChangeListener mNormalBitStreamCheckedListener = new C14812();
    private OnHDRToggleListener mOn4KToggleListener = new C14823();
    private OnHDRUserPlayListener mOn4KUserPlayListener = new C14878();
    private OnAdStateListener mOnAdStateListener;
    private OnHDRToggleListener mOnHDRToggleListener = new C14856();
    private OnHDRUserPlayListener mOnHDRUserPlayListener = new C14867();
    private OnHDRToggleListener mOnSDR1080PToggleListener = new C14845();
    private OnHDRToggleListener mOnSDR4KToggleListener = new C14834();
    private IPingbackContext mPingbackContext;
    private List<BitStream> mSDRBitStreamList = new CopyOnWriteArrayList();
    private String mTitle;
    private List<BitStream> mVipBitStreams = new CopyOnWriteArrayList();

    class C14801 implements OnCheckedChangeListener {
        C14801() {
        }

        public void onItemChecked(int index) {
            if (index == 0) {
                if (BitStreamContent.this.mHDRBitStreamList.size() > 0 && !PlayerAppConfig.isOpenHDR()) {
                    BitStreamParams params = new BitStreamParams();
                    params.setHdrSdrBitStream((BitStream) BitStreamContent.this.mHDRBitStreamList.get(0));
                    params.setCurrentBitStream(BitStreamContent.this.mCurBitStream);
                    BitStreamContent.this.mBitStreamDialog = null;
                    BitStreamContent.this.mBitStreamDialog = new BitStreamGuideDialog(BitStreamContent.this.mContext, params, 1);
                    BitStreamContent.this.mBitStreamDialog.setOnHDRToggleListener(BitStreamContent.this.mOnHDRToggleListener);
                    BitStreamContent.this.mBitStreamDialog.setOnHDRUserPlayListener(BitStreamContent.this.mOnHDRUserPlayListener);
                    BitStreamContent.this.mBitStreamDialog.show();
                    BitStreamContent.this.sendGuidePingBackShow(BitStreamContent.HDR_PINGBACK_TAG);
                    BitStreamContent.this.mItemListener.onItemSelected(null, 4);
                    BitStreamContent.this.mItemListener.onItemSelected(null, 3);
                }
            } else if (PlayerAppConfig.isOpenHDR()) {
                PlayerAppConfig.setIsOpenHDR(false);
                BitStreamContent.this.mItemListener.onItemSelected(null, 1);
                BitStreamContent.this.updateBitStreamList();
            }
            if (index >= 0) {
                BitStreamContent.this.mItemListener.onItemClicked(null, index);
            }
        }

        public void onCheckedChanged(int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamContent.this.TAG, "onCheckedChanged index " + index);
            }
            BitStreamContent.this.mItemListener.onItemClicked(null, index);
        }
    }

    class C14812 implements OnCheckedChangeListener {
        C14812() {
        }

        public void onCheckedChanged(int index) {
            boolean isHDR = true;
            int newDef = ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition();
            int savedDef = PlayerAppConfig.getDefaultStreamType();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamContent.this.TAG, "onCheckedChanged(def): index=" + index + ", saved def=" + savedDef + ", new def=" + newDef);
            }
            int size = BitStreamContent.this.mBitStreams.size();
            if (index >= 0 && index < size) {
                if (BitStreamContent.this.hasBitStreamHDRType()) {
                    if (!(((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDynamicRangeType() == 2 || ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDynamicRangeType() == 1)) {
                        isHDR = false;
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(BitStreamContent.this.TAG, "onCheckedChanged is HDR ?? " + isHDR);
                    }
                    if (!isHDR) {
                        PlayerAppConfig.setIsOpenHDR(false);
                    }
                }
                if (((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() == 10 && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).isVip()) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(BitStreamContent.this.TAG, "click 4k: index=" + index);
                    }
                } else if (((BitStream) BitStreamContent.this.mBitStreams.get(index)).getBenefitType() != 2 || ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDynamicRangeType() != 0) {
                    BitStreamContent.this.mItemListener.onItemSelected(BitStreamContent.this.mBitStreams.get(index), index);
                }
            }
        }

        public void onItemChecked(int index) {
            boolean is1080p;
            boolean is4K;
            LogUtils.m1568d(BitStreamContent.this.TAG, ">> onItemChecked getDefinition , mBitStreams.get(index)" + ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() + ", getPlayType " + ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getBenefitType());
            boolean isCurrent4K;
            if (BitStreamContent.this.mCurBitStream.getDefinition() == 10) {
                isCurrent4K = true;
            } else {
                isCurrent4K = false;
            }
            if (((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() == 10 && !isCurrent4K && PlayerAppConfig.isOpenHDR()) {
                BitStreamParams params = new BitStreamParams();
                params.setHdrSdrBitStream((BitStream) BitStreamContent.this.mBitStreams.get(index));
                params.setCurrentBitStream(BitStreamContent.this.mCurBitStream);
                BitStreamContent.this.mBitStreamDialog = null;
                BitStreamContent.this.mBitStreamDialog = new BitStreamGuideDialog(BitStreamContent.this.mContext, params, 2);
                BitStreamContent.this.mBitStreamDialog.setOnHDRToggleListener(BitStreamContent.this.mOn4KToggleListener);
                BitStreamContent.this.mBitStreamDialog.setOnHDRUserPlayListener(BitStreamContent.this.mOn4KUserPlayListener);
                BitStreamContent.this.mBitStreamDialog.show();
                BitStreamContent.this.sendGuidePingBackShow(BitStreamContent.HDR_4K_PINGBACK_TAG);
                BitStreamContent.this.mItemListener.onItemSelected(null, 4);
            }
            if (((BitStream) BitStreamContent.this.mBitStreams.get(index)).isVip() && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getBenefitType() == 2 && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() != 10 && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDynamicRangeType() == 0 && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() != BitStreamContent.this.mCurBitStream.getDefinition()) {
                is1080p = true;
            } else {
                is1080p = false;
            }
            if (((BitStream) BitStreamContent.this.mBitStreams.get(index)).isVip() && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() == 10 && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDynamicRangeType() == 0 && ((BitStream) BitStreamContent.this.mBitStreams.get(index)).getDefinition() != BitStreamContent.this.mCurBitStream.getDefinition()) {
                is4K = true;
            } else {
                is4K = false;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamContent.this.TAG, "onItemChecked is1080p : " + is1080p + ", is4K " + is4K);
            }
            if (is1080p) {
                params = new BitStreamParams();
                params.setHdrSdrBitStream((BitStream) BitStreamContent.this.mBitStreams.get(index));
                params.setCurrentBitStream(BitStreamContent.this.mCurBitStream);
                BitStreamContent.this.mBitStreamDialog = null;
                BitStreamContent.this.mBitStreamDialog = new BitStreamGuideDialog(BitStreamContent.this.mContext, params, 3);
                BitStreamContent.this.mBitStreamDialog.setOnHDRToggleListener(BitStreamContent.this.mOnSDR1080PToggleListener);
                BitStreamContent.this.mBitStreamDialog.setOnHDRUserPlayListener(BitStreamContent.this.mOn4KUserPlayListener);
                BitStreamContent.this.mBitStreamDialog.show();
                BitStreamContent.this.mItemListener.onItemSelected(null, 4);
            } else if (is4K) {
                params = new BitStreamParams();
                params.setHdrSdrBitStream((BitStream) BitStreamContent.this.mBitStreams.get(index));
                params.setCurrentBitStream(BitStreamContent.this.mCurBitStream);
                BitStreamContent.this.mBitStreamDialog = null;
                BitStreamContent.this.mBitStreamDialog = new BitStreamGuideDialog(BitStreamContent.this.mContext, params, 2);
                BitStreamContent.this.mBitStreamDialog.setOnHDRToggleListener(BitStreamContent.this.mOnSDR4KToggleListener);
                BitStreamContent.this.mBitStreamDialog.setOnHDRUserPlayListener(BitStreamContent.this.mOn4KUserPlayListener);
                BitStreamContent.this.mBitStreamDialog.show();
                BitStreamContent.this.sendGuidePingBackShow(BitStreamContent.HDR_4K_PINGBACK_TAG);
                BitStreamContent.this.mItemListener.onItemSelected(null, 4);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamContent.this.TAG, "onItemChecked(def): " + index);
            }
            int size = BitStreamContent.this.mBitStreams.size();
            if (index >= 0 && index < size) {
                BitStreamContent.this.mItemListener.onItemClicked(BitStreamContent.this.mBitStreams.get(index), index);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamContent.this.TAG, "onItemChecked(def): checked def index=" + BitStreamContent.this.mCheckedDefIndex);
            }
            BitStreamContent.this.mCheckedDefIndex = index;
        }
    }

    class C14823 implements OnHDRToggleListener {
        C14823() {
        }

        public void onToggle(boolean isOpen) {
            if (isOpen) {
                BitStreamContent.this.mItemListener.onItemSelected(null, 0);
                for (int i = 0; i < BitStreamContent.this.mBitStreams.size(); i++) {
                    if (((BitStream) BitStreamContent.this.mBitStreams.get(i)).getDefinition() == 10) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(BitStreamContent.this.TAG, "onToggle i " + i);
                        }
                        BitStreamContent.this.mItemListener.onItemSelected(BitStreamContent.this.mBitStreams.get(i), i);
                        BitStreamContent.this.sendGuidePingBackClick(BitStreamContent.HDR_4K_PINGBACK_TAG, "try");
                    }
                }
            }
        }
    }

    class C14834 implements OnHDRToggleListener {
        C14834() {
        }

        public void onToggle(boolean isOpen) {
            if (isOpen) {
                for (int i = 0; i < BitStreamContent.this.mBitStreams.size(); i++) {
                    if (((BitStream) BitStreamContent.this.mBitStreams.get(i)).getDefinition() == 10) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(BitStreamContent.this.TAG, "onToggle i " + i);
                        }
                        BitStreamContent.this.mItemListener.onItemSelected(BitStreamContent.this.mBitStreams.get(i), i);
                        BitStreamContent.this.sendGuidePingBackClick(BitStreamContent.HDR_4K_PINGBACK_TAG, "try");
                    }
                }
            }
            BitStreamContent.this.mItemListener.onItemSelected(null, 5);
        }
    }

    class C14845 implements OnHDRToggleListener {
        C14845() {
        }

        public void onToggle(boolean isOpen) {
            if (isOpen) {
                for (int i = 0; i < BitStreamContent.this.mBitStreams.size(); i++) {
                    if (((BitStream) BitStreamContent.this.mBitStreams.get(i)).getDefinition() == 5) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(BitStreamContent.this.TAG, "onToggle i " + i);
                        }
                        BitStreamContent.this.mItemListener.onItemSelected(BitStreamContent.this.mBitStreams.get(i), i);
                        BitStreamContent.this.sendGuidePingBackClick(BitStreamContent.HDR_4K_PINGBACK_TAG, "try");
                    }
                }
            }
            BitStreamContent.this.mItemListener.onItemSelected(null, 5);
        }
    }

    class C14856 implements OnHDRToggleListener {
        C14856() {
        }

        public void onToggle(boolean isOpen) {
            if (isOpen) {
                PlayerAppConfig.setIsOpenHDR(true);
                BitStreamContent.this.mItemListener.onItemSelected(null, 0);
                BitStreamContent.this.updateBitStreamList();
                BitStreamContent.this.sendGuidePingBackClick(BitStreamContent.HDR_PINGBACK_TAG, "try");
            }
        }
    }

    class C14867 implements OnHDRUserPlayListener {
        C14867() {
        }

        public void onPlay(String type) {
            int i = 0;
            PlayerAppConfig.setIsOpenHDR(false);
            if (BitStreamContent.this.hasBitStreamHDRType() && BitStreamContent.this.mHDRToggleGroupView != null) {
                MyRadioGroup access$1800 = BitStreamContent.this.mHDRToggleGroupView;
                if (!PlayerAppConfig.isOpenHDR()) {
                    i = 1;
                }
                access$1800.setSelection(i);
            }
            BitStreamContent.this.mItemListener.onItemSelected(null, 5);
            BitStreamContent.this.sendGuidePingBackClick(BitStreamContent.HDR_PINGBACK_TAG, type);
        }
    }

    class C14878 implements OnHDRUserPlayListener {
        C14878() {
        }

        public void onPlay(String type) {
            BitStreamContent.this.mItemListener.onItemSelected(null, 5);
            BitStreamContent.this.sendGuidePingBackClick(BitStreamContent.HDR_4K_PINGBACK_TAG, type);
        }
    }

    public class BitStreamParams {
        private BitStream currentBitStream;
        private BitStream hdrBitStream;

        public BitStream getCurrentBitStream() {
            return this.currentBitStream;
        }

        public void setCurrentBitStream(BitStream currentBitStream) {
            this.currentBitStream = currentBitStream;
        }

        public BitStream getHdrSdrBitStream() {
            return this.hdrBitStream;
        }

        public void setHdrSdrBitStream(BitStream hdrBitStream) {
            this.hdrBitStream = hdrBitStream;
        }
    }

    static {
        HDR_TOGGLE_LIST.add(new Pair(Integer.valueOf(1), Integer.valueOf(C1291R.string.hdr_open)));
        HDR_TOGGLE_LIST.add(new Pair(Integer.valueOf(4), Integer.valueOf(C1291R.string.hdr_close)));
    }

    public BitStreamContent(Context context, IPlayerMenuPanelUIStyle uiStyle, String title) {
        super(context, uiStyle);
        this.mTitle = title;
        this.mContext = context;
        this.mPingbackContext = (IPingbackContext) this.mContext;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView => inflate");
        }
        this.mContentView = LayoutInflater.from(this.mContext).inflate(C1291R.layout.player_tabpanel_hdr, null);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView <= inflate: result=" + this.mContentView);
        }
        initDefinitionWidget(this.mContentView);
    }

    private void initDefinitionWidget(View contentView) {
        initNormalBitStreamWidget(contentView);
        initHDRBitStreamWidget(contentView);
    }

    private void initNormalBitStreamWidget(View contentView) {
        HorizontalScrollView scrollView = (HorizontalScrollView) contentView.findViewById(C1291R.id.rg);
        this.mBitStreamTitle = (TextView) contentView.findViewById(C1291R.id.definition_txt);
        this.mBitStreamGroupView = (MyRadioGroup) contentView.findViewById(C1291R.id.rg_definition);
        this.mDefinitionPanel = (RelativeLayout) contentView.findViewById(C1291R.id.ll_definition);
        setupMyRadioGroupCommon(this.mBitStreamGroupView);
        this.mBitStreamGroupView.setCornerIconResId(this.mUiStyle.getDefCornerIconResId());
        LayoutParams cornerImgParams = this.mUiStyle.getDefCornerImgLayoutParams();
        if (cornerImgParams != null) {
            this.mBitStreamGroupView.setCornerImageParams(cornerImgParams);
        }
        this.mBitStreamGroupView.setAutoFocusOnSelection(true);
        scrollView.setClipChildren(false);
        this.mDefinitionPanel.setClipChildren(true);
        if (!IS_ZOOM_ENABLED) {
            Rect contentPadding = this.mBitStreamGroupView.getContentPadding();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "initDefinitionWidget: content padding=" + contentPadding);
            }
            MarginLayoutParams params = (MarginLayoutParams) this.mBitStreamGroupView.getLayoutParams();
            if (params != null) {
                params.leftMargin -= contentPadding.left;
                this.mBitStreamGroupView.setLayoutParams(params);
            }
        }
        this.mBitStreamGroupView.setOnCheckedChangedListener(this.mNormalBitStreamCheckedListener);
    }

    private void initHDRBitStreamWidget(View contentView) {
        if (hasBitStreamHDRType()) {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
            lp.topMargin = (int) this.mContext.getResources().getDimension(C1291R.dimen.player_radiogroup_margin_top);
            lp.leftMargin = (int) this.mContext.getResources().getDimension(C1291R.dimen.player_radiogroup_margin_left);
            this.mDefinitionPanel.setLayoutParams(lp);
            this.mHDRToggleGroupView = (MyRadioGroup) contentView.findViewById(C1291R.id.rg_definition2);
            this.mHDRLayout = (LinearLayout) contentView.findViewById(C1291R.id.ll_hdr_layout);
            this.mHDRLayout.setVisibility(0);
            this.mBitStreamTitle.setVisibility(0);
            setupMyRadioGroupCommon(this.mHDRToggleGroupView);
            this.mCheckedIndex = PlayerAppConfig.isOpenHDR() ? 0 : 1;
            Resources resources = this.mContext.getResources();
            List<String> list = new ArrayList();
            for (Pair<Integer, Integer> p : HDR_TOGGLE_LIST) {
                list.add(resources.getString(((Integer) p.second).intValue()));
            }
            this.mHDRToggleGroupView.setDataSource(list, this.mCheckedIndex);
            this.mHDRToggleGroupView.setCornerIconResId(this.mUiStyle.getDefCornerIconResId());
            LayoutParams cornerImgParams = this.mUiStyle.getDefCornerImgLayoutParams();
            if (cornerImgParams != null) {
                this.mHDRToggleGroupView.setCornerImageParams(cornerImgParams);
            }
            List<Integer> cornerList = new ArrayList();
            cornerList.add(Integer.valueOf(0));
            this.mHDRToggleGroupView.setCornerIconList(cornerList);
            this.mHDRToggleGroupView.setOnCheckedChangedListener(new C14801());
            this.mDefinitionPanel.setNextFocusDownId(C1291R.id.rg_definition2);
            return;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initHDRBitStreamWidget No HDR bitStream!!");
        }
        if (this.mHDRLayout != null) {
            this.mHDRLayout.setVisibility(8);
        }
        if (this.mBitStreamTitle != null) {
            this.mBitStreamTitle.setVisibility(8);
        }
        lp = (RelativeLayout.LayoutParams) this.mDefinitionPanel.getLayoutParams();
        lp.addRule(15);
        this.mDefinitionPanel.setLayoutParams(lp);
    }

    private void updateBitStreamList() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> updateBitStreamList hdr=" + PlayerAppConfig.isOpenHDR());
        }
        this.mBitStreams.clear();
        List<BitStream> filterSDRList = new ArrayList();
        if (PlayerAppConfig.isOpenHDR()) {
            this.mBitStreams.addAll(this.mSDRBitStreamList);
            for (int i = 0; i < this.mHDRBitStreamList.size(); i++) {
                for (int j = 0; j < this.mSDRBitStreamList.size(); j++) {
                    if (((BitStream) this.mHDRBitStreamList.get(i)).getDefinition() == ((BitStream) this.mSDRBitStreamList.get(j)).getDefinition()) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(this.TAG, ">> find " + j + ", will remove");
                        }
                        filterSDRList.add(this.mSDRBitStreamList.get(j));
                    }
                }
            }
            for (int k = 0; k < filterSDRList.size(); k++) {
                this.mBitStreams.remove(filterSDRList.get(k));
            }
            this.mBitStreams.addAll(this.mHDRBitStreamList);
        } else {
            this.mBitStreams.addAll(this.mSDRBitStreamList);
        }
        if (this.mContentView != null) {
            initHDRBitStreamWidget(this.mContentView);
        }
    }

    private boolean hasBitStreamHDRType() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> hasBitStreamHDRType all size " + this.mAllBitStreamList.size());
        }
        int i = 0;
        while (i < this.mAllBitStreamList.size()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, ">> hasBitStreamHDRType getDRType " + ((BitStream) this.mAllBitStreamList.get(i)).getDynamicRangeType());
            }
            if (((BitStream) this.mAllBitStreamList.get(i)).getDynamicRangeType() == 1 || ((BitStream) this.mAllBitStreamList.get(i)).getDynamicRangeType() == 2) {
                return true;
            }
            i++;
        }
        if (!PlayerDebugUtils.testHDRBitStreamData()) {
            return false;
        }
        LogRecordUtils.logd(this.TAG, " testHDRBitStreamData ");
        return true;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public View getView() {
        if (this.mContentView == null) {
            initViews();
        }
        return this.mContentView;
    }

    public View getFocusableView() {
        return this.mBitStreamGroupView;
    }

    public List<BitStream> getContentData() {
        return this.mBitStreams;
    }

    public void setData(List<BitStream> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setData, data=" + data);
        }
        this.mAllBitStreamList.clear();
        this.mAllBitStreamList.addAll(data);
        this.mBitStreams.clear();
        this.mHDRBitStreamList.clear();
        this.mSDRBitStreamList.clear();
        if (!ListUtils.isEmpty((List) data)) {
            for (int i = 0; i < data.size(); i++) {
                if (((BitStream) data.get(i)).getDynamicRangeType() != 0) {
                    this.mHDRBitStreamList.add(data.get(i));
                } else {
                    this.mSDRBitStreamList.add(data.get(i));
                }
                updateBitStreamList();
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setData, mBitStreams=" + this.mBitStreams);
        }
        if (this.mIsShown) {
            updateDataSelection();
        }
    }

    public void setVipData(List<BitStream> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setVipData, data=" + data);
        }
        this.mVipBitStreams.clear();
        if (!ListUtils.isEmpty((List) data)) {
            this.mVipBitStreams.addAll(data);
        }
        if (this.mIsShown) {
            updateDataSelection();
        }
    }

    public void setHDRData(List<BitStream> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setVipData, data=" + data);
        }
        this.mHDRBitStreamList.clear();
        if (!ListUtils.isEmpty((List) data)) {
            this.mVipBitStreams.addAll(data);
        }
        if (this.mIsShown) {
            updateDataSelection();
        }
    }

    public void setSelection(BitStream item) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setSelection, item=" + item);
        }
        if (item != null) {
            this.mCurBitStream = item;
            if (this.mCurBitStream.getDynamicRangeType() == 0) {
                PlayerAppConfig.setIsOpenHDR(false);
                updateBitStreamList();
            }
            if (this.mIsShown) {
                int selectedDefIndex = findSelectIndex(this.mBitStreams, this.mCurBitStream);
                if (selectedDefIndex < 0) {
                    selectedDefIndex = 0;
                }
                this.mCheckedDefIndex = selectedDefIndex;
                this.mBitStreamGroupView.setSelection(this.mCheckedDefIndex);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, " currentBitStream is null, why? video fetch currentBitStream is null");
        }
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> show");
        }
        if (!this.mIsShown) {
            if (!ListUtils.isEmpty(this.mBitStreams)) {
                this.mIsShown = true;
                if (this.mContentView == null) {
                    initViews();
                }
                initHDRBitStreamWidget(this.mContentView);
                this.mContentView.setVisibility(0);
                this.mDefinitionPanel.setVisibility(0);
                this.mBitStreamGroupView.setVisibility(0);
                showAd(this.mAdData);
                updateDataSelection();
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "show, mBitStreams is empty.");
            }
        }
    }

    private List<Integer> findVipIndex(List<BitStream> list, List<BitStream> viplist) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> findVipIndex, list=" + list + ", viplist=" + viplist);
        }
        List<Integer> vipList = new ArrayList();
        if (!(ListUtils.isEmpty((List) list) || ListUtils.isEmpty((List) viplist))) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (viplist.contains(list.get(i))) {
                    vipList.add(Integer.valueOf(i));
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< findVipIndex, vipList=" + vipList);
        }
        return vipList;
    }

    private int findSelectIndex(List<BitStream> list, BitStream bitStream) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> findSelectIndex, list=" + list + ", bitStream=" + bitStream);
        }
        int ret = -1;
        if (!ListUtils.isEmpty((List) list) && bitStream != null) {
            int i = 0;
            int size = list.size();
            while (i < size) {
                if (bitStream.getDynamicRangeType() != 0) {
                    if (((BitStream) list.get(i)).getDefinition() == bitStream.getDefinition() && ((BitStream) list.get(i)).getCodecType() == bitStream.getCodecType() && ((BitStream) list.get(i)).getAudioType() == bitStream.getAudioType()) {
                        ret = i;
                        break;
                    }
                } else if (((BitStream) list.get(i)).equal(bitStream)) {
                    ret = i;
                    break;
                }
                i++;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< findSelectIndex, ret=" + ret);
        }
        return ret;
    }

    private void updateDataSelection() {
        int i = 0;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> updateDataSelection mCurBitStream " + PlayerDataUtils.getBitStreamString(this.mContext, this.mCurBitStream));
        }
        int selectedDefIndex = findSelectIndex(this.mBitStreams, this.mCurBitStream);
        if (selectedDefIndex < 0) {
            selectedDefIndex = 0;
        }
        this.mCheckedDefIndex = selectedDefIndex;
        List<String> defList = convertBitstreamToDefinition();
        this.mBitStreamGroupView.setCornerIconList(findVipIndex(this.mBitStreams, this.mVipBitStreams));
        if (this.mDataInited) {
            this.mBitStreamGroupView.updateDataSource(defList, this.mCheckedDefIndex);
        } else {
            this.mBitStreamGroupView.setDataSource(defList, this.mCheckedDefIndex);
            this.mDataInited = true;
        }
        if (Project.getInstance().getConfig().isEnableHardwareAccelerated()) {
            int size = this.mBitStreamGroupView.getChildCount();
            for (int i2 = 0; i2 < size; i2++) {
                this.mBitStreamGroupView.getChildAt(i2).setLayerType(2, null);
            }
        }
        this.mBitStreamGroupView.setSelection(this.mCheckedDefIndex);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> updateDataSelection mCheckedDefIndex " + this.mCheckedDefIndex);
        }
        if (hasBitStreamHDRType() && this.mHDRToggleGroupView != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, ">> updateDataSelection hasBitStreamHDRType " + PlayerAppConfig.isOpenHDR());
            }
            MyRadioGroup myRadioGroup = this.mHDRToggleGroupView;
            if (!PlayerAppConfig.isOpenHDR()) {
                i = 1;
            }
            myRadioGroup.setSelection(i);
        }
    }

    private List<String> convertBitstreamToDefinition() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> convertBitstreamToDefinition");
        }
        List<String> defs = new ArrayList();
        if (!ListUtils.isEmpty(this.mBitStreams)) {
            for (BitStream each : this.mBitStreams) {
                defs.add(PlayerDataUtils.getBitStreamString(this.mContext, each));
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< convertBitstreamToDefinition, defs=" + defs);
        }
        return defs;
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hide()");
        }
        if (this.mIsShown) {
            this.mIsShown = false;
            this.mContentView.setVisibility(4);
            this.mDefinitionPanel.setVisibility(4);
            this.mBitStreamGroupView.setVisibility(4);
            hideAd();
        }
    }

    public void setItemListener(IItemListener<BitStream> listener) {
        this.mItemListener = listener;
    }

    public void setAdData(BaseAdData data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setAdData:" + data);
        }
        this.mAdData = data;
    }

    private void showAd(BaseAdData data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "-->>showAd:" + data + "/" + this.mOnAdStateListener);
        }
        if (data == null || data.getAdView() == null) {
            clearAd();
            notifyAdShow(0);
            return;
        }
        if (this.mAdView == null) {
            this.mAdView = data.getAdView();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "showAd adview=" + this.mAdView);
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_180dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_60dp));
            params.rightMargin = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_20dp);
            params.addRule(11);
            params.addRule(15);
            ((ViewGroup) this.mContentView).addView(this.mAdView, params);
            sendAdPingback();
        }
        notifyAdShow(this.mAdData.getID());
        this.mAdView.setVisibility(0);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "--<<showAd()");
        }
    }

    private void notifyAdShow(int id) {
        if (this.mOnAdStateListener != null) {
            this.mOnAdStateListener.onShow(101, Integer.valueOf(id));
        }
    }

    private void hideAd() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hideAd()");
        }
        if (this.mAdView != null) {
            this.mAdView.setVisibility(8);
        }
    }

    public void clearAd() {
        if (this.mContentView != null && this.mAdView != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "clearAd()");
            }
            ((ViewGroup) this.mContentView).removeView(this.mAdView);
            this.mAdView = null;
            this.mAdData = null;
        }
    }

    private void sendAdPingback() {
        if (this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(49).addItem(BTSPTYPE.BSTP_1).addItem(QTCURL.QTCURL_TYPE("ad_chgra_tab")).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE("ad_chgra_tab")).post();
        }
    }

    private boolean isVip() {
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext) && GetInterfaceTools.getIGalaAccountManager().isVip()) {
            return true;
        }
        return false;
    }

    private void sendGuidePingBackShow(String blockType) {
        String qtcurl = "";
        if (isVip()) {
            qtcurl = "hdr_guide_vip";
        } else {
            qtcurl = "hdr_guide_guest";
        }
        PingbackFactory.instance().createPingback(44).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE("")).addItem(QTCURL.QTCURL_TYPE(qtcurl)).addItem(QPLD.QPLD_TYPE("")).addItem(BLOCK.BLOCK_TYPE(blockType)).post();
    }

    private void sendGuidePingBackClick(String blockType, String rseatType) {
        String rpage = "";
        if (isVip()) {
            rpage = "hdr_guide_vip";
        } else {
            rpage = "hdr_guide_guest";
        }
        PingbackFactory.instance().createPingback(46).addItem(RPAGE.RPAGE_ID(rpage)).addItem(BLOCK.BLOCK_TYPE(blockType)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseatType)).addItem(C1.C1_TYPE("")).addItem(C0165R.R_TYPE("")).post();
    }

    public boolean isHasHDRandToggleOpen() {
        return hasBitStreamHDRType() && PlayerAppConfig.isOpenHDR();
    }

    public boolean isHasHDRandToggleClose() {
        return hasBitStreamHDRType() && !PlayerAppConfig.isOpenHDR();
    }

    public boolean isOnlySDRBitStream() {
        return !hasBitStreamHDRType();
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
        this.mOnAdStateListener = listener;
    }
}
