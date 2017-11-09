package com.gala.video.app.player.albumdetail.ui.card;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.gala.video.app.player.albumdetail.ui.card.BasicContract.Presenter;
import com.gala.video.app.player.albumdetail.ui.card.BasicContract.View;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;

public class TestViewContent extends TextView implements IViewLifecycle<Presenter>, View {
    public TestViewContent(Context context) {
        super(context);
    }

    public TestViewContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestViewContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public TestViewContent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void onBind(Presenter object) {
        Log.v("TestViewContent", "onBind");
        setText("asdasdasdasd");
    }

    public void onUnbind(Presenter object) {
    }

    public void onShow(Presenter object) {
    }

    public void onHide(Presenter object) {
    }
}
