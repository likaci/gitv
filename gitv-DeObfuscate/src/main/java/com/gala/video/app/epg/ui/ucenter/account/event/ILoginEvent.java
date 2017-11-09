package com.gala.video.app.epg.ui.ucenter.account.event;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.gala.video.app.epg.ui.ucenter.account.ui.fragment.BaseLoginFragment;

public interface ILoginEvent {
    Drawable getContainerDrawable();

    void onPlaySound();

    void onSwitchFragment(BaseLoginFragment baseLoginFragment, Bundle bundle);
}
