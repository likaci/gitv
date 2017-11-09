package com.gala.video.app.epg.ui.setting.update;

import android.content.Context;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import java.util.ArrayList;
import java.util.List;

public class SettingPlayDisplayUpdate extends BaseSettingUpdate {
    private static final String[] JUMP_START_END_OPTIONS = new String[]{"开启", "关闭"};
    private static final String LOG_TAG = "EPG/setting/SettingPlayDisplayUpdate";
    private static final String[] SCALE_OPTIONS = new String[]{"原始比例", "强制全屏"};
    private static final String[] STREAMTYPE_OPTIONS = new String[]{"高清", "720P", "杜比 720P", "H.265 720P", "1080P", "杜比 1080P", "H.265 1080P", "4K", "杜比 4K", "H.265 4K", "流畅"};
    private SettingItem mBgSettingItem;
    private ISetting mISetting;
    private SettingItem mTitleItem;

    public SettingModel updateSettingModel(SettingModel model) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        List<SettingItem> items = model.getItems();
        String value = "";
        for (SettingItem item : items) {
            int id = item.getId();
            if (!SettingUtils.isCustomId(id)) {
                switch (id) {
                    case 257:
                        value = getStreamType(context);
                        item.setItemOptions(filterScreamType(item));
                        item.setItemLastState(value);
                        break;
                    case 258:
                        String[] se = SettingConstants.JUMP_START_END_OPTIONS;
                        item.setItemLastState(SettingPlayPreference.getJumpStartEnd(context) ? se[0] : se[1]);
                        break;
                    case 259:
                        String[] ratios = SettingConstants.SCALE_OPTIONS;
                        item.setItemLastState(SettingPlayPreference.getStretchPlaybackToFullScreen(context) ? ratios[1] : ratios[0]);
                        break;
                    case 260:
                        if (!isSettingNull()) {
                            item.setItemOptions(this.mISetting.getAudioOutputEntries());
                            item.setItemLastState(this.mISetting.getCurrAudioOutputMode());
                            break;
                        }
                        break;
                    case 261:
                        if (!isSettingNull()) {
                            item.setItemOptions(this.mISetting.getDRCEntries());
                            item.setItemLastState(this.mISetting.getCurrDRCMode());
                            break;
                        }
                        break;
                    case 769:
                        if (!isSettingNull()) {
                            item.setItemLastState(this.mISetting.getCurrOutput());
                            break;
                        }
                        break;
                    case 771:
                        this.mBgSettingItem = item;
                        break;
                    case 772:
                        this.mTitleItem = item;
                        break;
                    default:
                        break;
                }
            }
            setItemOptionAndLastState(item);
        }
        if (MemoryLevelInfo.isLowMemoryDevice()) {
            items.remove(this.mTitleItem);
            items.remove(this.mBgSettingItem);
        }
        model.setItems(items);
        return model;
    }

    private List<String> filterScreamType(SettingItem item) {
        List<String> options = item.getItemOptions();
        List<String> filterOptions = new ArrayList();
        for (String curOption : options) {
            if ((Project.getInstance().getConfig().isEnableDolby() || !curOption.contains("杜比")) && (Project.getInstance().getBuild().isEnableH265() || !curOption.contains("H.265"))) {
                filterOptions.add(curOption);
            }
        }
        LogUtils.e("TEST", "TEST --- options.size=" + options.size());
        return filterOptions;
    }

    private String getStreamType(Context context) {
        LogUtils.i(LOG_TAG, ">>>>>getStreamType() --- begin");
        String result = "";
        int definition = SettingPlayPreference.getStreamType(context);
        int audioType = SettingPlayPreference.getAudioType(context);
        if (definition == 2) {
            result = STREAMTYPE_OPTIONS[0];
        } else if (definition == 4 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[1];
        } else if (definition == 4 && audioType == 1) {
            result = STREAMTYPE_OPTIONS[2];
        } else if (definition == 5 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[4];
        } else if (definition == 5 && audioType == 1) {
            result = STREAMTYPE_OPTIONS[5];
        } else if (definition == 10 && audioType == 0) {
            result = STREAMTYPE_OPTIONS[7];
        } else if (definition == 10 && audioType == 1) {
            result = STREAMTYPE_OPTIONS[8];
        } else if (definition == 1) {
            result = STREAMTYPE_OPTIONS[10];
        }
        LogUtils.i(LOG_TAG, "getStreamType() ---end--- definition = " + definition + " --- result = " + result);
        return result;
    }

    private void saveStreamTypeSetting(Context context, String streamType) {
        types = new String[8][];
        types[0] = new String[]{"高清", "2"};
        types[1] = new String[]{"720P", "4"};
        types[2] = new String[]{"杜比 720P", "14"};
        types[3] = new String[]{"1080P", "5"};
        types[4] = new String[]{"杜比 1080P", "15"};
        types[5] = new String[]{"4K", "10"};
        types[6] = new String[]{"杜比 4K", Values.value16};
        types[7] = new String[]{"流畅", "1"};
        int definition = 2;
        int audiotype = 0;
        if (streamType.equals(types[0][0])) {
            definition = 2;
        } else if (streamType.equals(types[1][0])) {
            definition = 4;
        } else if (streamType.equals(types[2][0])) {
            definition = 4;
            audiotype = 1;
        } else if (streamType.equals(types[3][0])) {
            definition = 5;
        } else if (streamType.equals(types[4][0])) {
            definition = 5;
            audiotype = 1;
        } else if (streamType.equals(types[5][0])) {
            definition = 10;
        } else if (streamType.equals(types[6][0])) {
            definition = 10;
            audiotype = 1;
        } else if (streamType.equals(types[7][0])) {
            definition = 1;
        }
        SettingPlayPreference.setStreamType(context, definition);
        SettingPlayPreference.setAudioType(context, audiotype);
    }

    private void saveFullScreenSetting(Context context, String frameRatio) {
        if (SCALE_OPTIONS[0].equals(frameRatio)) {
            SettingPlayPreference.setStretchPlaybackToFullScreen(context, false);
        } else {
            SettingPlayPreference.setStretchPlaybackToFullScreen(context, true);
        }
    }

    private boolean isSettingNull() {
        return this.mISetting == null;
    }

    private void saveStartEndSetting(Context context, String withOutBeginEnd) {
        if (JUMP_START_END_OPTIONS[1].equals(withOutBeginEnd)) {
            SettingPlayPreference.setJumpStartEnd(context, false);
        } else {
            SettingPlayPreference.setJumpStartEnd(context, true);
        }
    }

    public String getLastStateByPos(SettingItem item) {
        String lastState = super.getLastStateByPos(item);
        int id = item == null ? -1 : item.getId();
        if (!Project.getInstance().getBuild().isHomeVersion() || id != 769 || isSettingNull()) {
            return lastState;
        }
        if (StringUtils.isEmpty(Project.getInstance().getConfig().getSystemSetting().getCurrOutput())) {
            return lastState;
        }
        return AppRuntimeEnv.get().getApplicationContext().getResources().getString(R.string.setting_resolution, new Object[]{Project.getInstance().getConfig().getSystemSetting().getCurrOutput()});
    }

    public void saveNewCacheByPos(SettingItem item) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        if (Project.getInstance().getBuild().isHomeVersion()) {
            this.mISetting = Project.getInstance().getConfig().getSystemSetting();
        }
        int itemId = item.getId();
        String lastState = item.getItemLastState();
        switch (itemId) {
            case 257:
                LogUtils.i(LOG_TAG, "saveNewCache()--- itemId - ", Integer.valueOf(itemId));
                saveStreamTypeSetting(context, lastState);
                return;
            case 258:
                LogUtils.i(LOG_TAG, "saveNewCache() --- itemId - ", Integer.valueOf(itemId));
                saveStartEndSetting(context, lastState);
                return;
            case 259:
                LogUtils.i(LOG_TAG, "saveNewCache() --- itemId - ", Integer.valueOf(itemId));
                saveFullScreenSetting(context, lastState);
                return;
            case 260:
                if (this.mISetting != null) {
                    LogUtils.i(LOG_TAG, "saveNewCache() --- itemId - ", Integer.valueOf(itemId));
                    this.mISetting.setAudioOutputMode(lastState);
                    return;
                }
                return;
            case 261:
                if (this.mISetting != null) {
                    LogUtils.i(LOG_TAG, "saveNewCache() --- itemId - ", Integer.valueOf(itemId));
                    this.mISetting.setDRCMode(lastState);
                    return;
                }
                return;
            case 769:
                if (this.mISetting != null) {
                    LogUtils.i(LOG_TAG, "saveNewCache() --- itemId - ", Integer.valueOf(itemId));
                    this.mISetting.setOutputDisplay(lastState);
                    return;
                }
                return;
            default:
                super.saveNewCacheByPos(item);
                return;
        }
    }
}
