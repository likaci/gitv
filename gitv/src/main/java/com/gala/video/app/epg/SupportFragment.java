package com.gala.video.app.epg;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SupportFragment extends Fragment {
    private boolean mBack;
    private Animation mBackAnimation;

    public void onAttach(Activity activity) {
        this.mBackAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.share_page_exit);
        super.onAttach(activity);
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.e("SupportFragment", "onCreateAnimation, enter = " + enter + ", " + this.mBack + ", " + this.mBackAnimation);
        if (enter || !this.mBack) {
            return null;
        }
        return this.mBackAnimation;
    }

    public void setBack(boolean flag) {
        this.mBack = flag;
    }
}
