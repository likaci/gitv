package com.gala.video.app.player.ui.widget.views;

public interface ISeekBar {

    public interface OnSeekBarChangeListener {
        void onProgressChanged(ISeekBar iSeekBar, int i, boolean z);

        void onStartTrackingTouch(ISeekBar iSeekBar);

        void onStopTrackingTouch(ISeekBar iSeekBar);
    }

    int getMax();

    int getProgress();

    int getProgressHeight();

    int getProgressTop();

    int getSecondProgress();

    void setMax(int i);

    void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener);

    void setProgress(int i);

    void setSecondaryProgress(int i);

    void setThumb(int i);

    void setThumbOffset(int i);
}
