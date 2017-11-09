package com.gala.video.lib.share.uikit.card;

import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.FlowLayout;

public class MatrixCard extends Card {
    public BlockLayout onCreateBlockLayout() {
        return new FlowLayout();
    }
}
