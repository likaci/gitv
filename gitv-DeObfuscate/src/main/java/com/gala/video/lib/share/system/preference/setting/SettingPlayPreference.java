package com.gala.video.lib.share.system.preference.setting;

import android.content.Context;
import com.gala.video.lib.share.system.preference.AppPreference;

public class SettingPlayPreference {
    private static final String KEY_AUDIO_TYPE = "audio_type";
    private static final String KEY_HAS_START_END = "has_jump_start_end";
    private static final String KEY_HDR_TYPE = "hdr_type";
    private static final String KEY_JUMP_START_END = "jump_start_end";
    private static final String KEY_MESS_DIALOG_OPEN = "message_dialog_open";
    private static final String KEY_NET_DOCTOR_AUDIO_TYPE = "net_doctor_audio_type";
    private static final String KEY_NET_DOCTOR_CODEC_TYPE = "net_doctor_codec_type";
    private static final String KEY_NET_DOCTOR_DEFINITION = "net_doctor_definition";
    private static final String KEY_NET_DOCTOR_DR_TYPE = "net_doctor_dr_type";
    private static final String KEY_OPEN_PLUGIN_IO_BALANCE = "open_plugin_io_balance";
    private static final String KEY_SCREEN_SCALE = "screen_scale";
    private static final String KEY_SELECTION_PANEL_SHOWN = "selection_panel_shown";
    private static final String KEY_SELECTION_PANEL_SHOWN_COUNT = "selection_panel_shown_count";
    private static final String KEY_STREAM_AUTO_CHANGE_TYPE = "auto_change_stream_type";
    private static final String KEY_STREAM_TYPE = "stream_type";
    private static final String NAME = "SETTINGS";
    static AppPreference mPreference;

    public static void setStreamType(Context ctx, int streamType) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_STREAM_TYPE, streamType);
    }

    public static void setOpenPluginIOBalance(Context ctx, boolean open) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save("open_plugin_io_balance", open);
    }

    public static boolean isOpenPluginIOBalance(Context ctx) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        return mPreference.getBoolean("open_plugin_io_balance", true);
    }

    public static void setAudioType(Context ctx, int audioType) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_AUDIO_TYPE, audioType);
    }

    public static int getStreamType(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_STREAM_TYPE, 2);
    }

    public static void setKeyNetDoctorBitStreamDefinition(Context ctx, int definition) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_NET_DOCTOR_DEFINITION, definition);
    }

    public static void setKeyNetDoctorCodecType(Context ctx, int codecType) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_NET_DOCTOR_CODEC_TYPE, codecType);
    }

    public static void setKeyNetDoctorAudioType(Context ctx, int audioType) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_NET_DOCTOR_AUDIO_TYPE, audioType);
    }

    public static void setKeyNetDoctorDRType(Context ctx, int drType) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_NET_DOCTOR_DR_TYPE, drType);
    }

    public static int getNetDoctorBitStreamDefinition(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_NET_DOCTOR_DEFINITION, 2);
    }

    public static int getNetDoctorAudioType(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_NET_DOCTOR_AUDIO_TYPE, 0);
    }

    public static int getNetDoctorCodecType(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_NET_DOCTOR_CODEC_TYPE, 0);
    }

    public static int getNetDoctorDRType(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_NET_DOCTOR_DR_TYPE, 0);
    }

    public static int getAudioType(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_AUDIO_TYPE, 0);
    }

    public static boolean isOpenHDR(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_HDR_TYPE, false);
    }

    public static void setIsOpenHDR(Context ctx, boolean openhdr) {
        if (mPreference == null) {
            mPreference = new AppPreference(ctx, NAME);
        }
        mPreference.save(KEY_HDR_TYPE, openhdr);
    }

    public static int getStreamTypeSetting(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(KEY_STREAM_TYPE, -1);
    }

    public static void setAutoChangeStreamType(Context ctx, boolean autoChangeStreamType) {
        new AppPreference(ctx, NAME).save(KEY_STREAM_AUTO_CHANGE_TYPE, autoChangeStreamType);
    }

    public static boolean getAutoChangeStreamType(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_STREAM_AUTO_CHANGE_TYPE, true);
    }

    public static void setJumpStartEnd(Context ctx, boolean jumpStartEnd) {
        new AppPreference(ctx, NAME).save(KEY_JUMP_START_END, jumpStartEnd);
    }

    public static boolean getJumpStartEnd(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_JUMP_START_END, true);
    }

    public static void setHasStartEnd(Context ctx, boolean jumpStartEnd) {
        new AppPreference(ctx, NAME).save(KEY_HAS_START_END, jumpStartEnd);
    }

    public static boolean getHasStartEnd(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_HAS_START_END, true);
    }

    public static void setStretchPlaybackToFullScreen(Context ctx, boolean isFullScreen) {
        new AppPreference(ctx, NAME).save(KEY_SCREEN_SCALE, isFullScreen);
    }

    public static boolean getStretchPlaybackToFullScreen(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_SCREEN_SCALE, false);
    }

    public static void setSelectionPanelShown(Context ctx, boolean shown) {
        new AppPreference(ctx, NAME).save(KEY_SELECTION_PANEL_SHOWN, shown);
    }

    public static boolean getSelectionPanelShown(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_SELECTION_PANEL_SHOWN, false);
    }

    public static void setSelectionPanelShownCount(Context ctx, String count) {
        new AppPreference(ctx, NAME).save(KEY_SELECTION_PANEL_SHOWN_COUNT, count);
    }

    public static String getSelectionPanelShownCount(Context ctx) {
        return new AppPreference(ctx, NAME).get(KEY_SELECTION_PANEL_SHOWN_COUNT, null);
    }

    public static void setMessDialogOpen(Context ctx, boolean isOpen) {
        new AppPreference(ctx, NAME).save(KEY_MESS_DIALOG_OPEN, isOpen);
    }

    public static boolean getMessDialogOpen(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(KEY_MESS_DIALOG_OPEN, true);
    }
}
