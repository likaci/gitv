package com.gala.video.app.player.albumdetail.ui.card;

import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView.LayoutParams;
import com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay;

public class BasicContract {

    public interface Presenter {
        DetailOverlay getDetailOverlay();

        void setDetailOverlay(DetailOverlay detailOverlay);

        void setView(View view);

        void setViewLayoutParams(LayoutParams layoutParams);
    }

    public interface View {
        ViewGroup.LayoutParams getLayoutParams();
    }
}
