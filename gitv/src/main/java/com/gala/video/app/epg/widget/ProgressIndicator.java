package com.gala.video.app.epg.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gala.video.app.epg.R;

public class ProgressIndicator {
    private LinearLayout mCancelLayout;
    private TextView mCancelLayoutTextView;
    private Dialog mDialog;
    private ProgressBar mProgressBar;
    private TextView mProgressTextView;
    private TextView mUpdateTextMessage;

    public ProgressIndicator(Context context) {
        this.mDialog = new Dialog(context, R.style.download_processing_dialog);
        this.mDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        this.mDialog.setContentView(initUpdateProgressBarView(context));
        this.mDialog.setCancelable(false);
    }

    private View initUpdateProgressBarView(Context context) {
        LinearLayout progressBarView = (LinearLayout) ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.epg_update_progress_dialog, null);
        this.mProgressBar = (ProgressBar) progressBarView.findViewById(R.id.epg_update_progressbar_id);
        this.mCancelLayout = (LinearLayout) progressBarView.findViewById(R.id.epg_cancel_button_layout_id);
        this.mProgressTextView = (TextView) progressBarView.findViewById(R.id.epg_progress_percentage_id);
        this.mCancelLayoutTextView = (TextView) progressBarView.findViewById(R.id.epg_cancel_button_text);
        this.mUpdateTextMessage = (TextView) progressBarView.findViewById(R.id.epg_update_text_mssage_id);
        this.mCancelLayout.requestFocus();
        return progressBarView;
    }

    public void setCancelListener(OnClickListener onClick) {
        this.mCancelLayout.setOnClickListener(onClick);
    }

    public void show() {
        this.mDialog.show();
    }

    public void cancel() {
        this.mDialog.dismiss();
    }

    public void setDownloadProgress(int progress) {
        this.mProgressBar.setProgress(progress);
        this.mProgressTextView.setText(String.format("%d%%", new Object[]{Integer.valueOf(progress)}));
    }

    public boolean isShowing() {
        if (this.mDialog == null || !this.mDialog.isShowing()) {
            return false;
        }
        return true;
    }

    public void setCancelButtonText(String text) {
        if (this.mCancelLayoutTextView != null) {
            this.mCancelLayoutTextView.setText(text);
        }
    }

    public void setUpdateTextMessage(String text) {
        if (this.mUpdateTextMessage != null) {
            this.mUpdateTextMessage.setText(text);
        }
    }

    public void setCancelable(boolean flag) {
        if (this.mDialog != null) {
            this.mDialog.setCancelable(flag);
        }
    }

    public void setOnCanceledListener(OnCancelListener listener) {
        if (this.mDialog != null) {
            this.mDialog.setOnCancelListener(listener);
        }
    }
}
