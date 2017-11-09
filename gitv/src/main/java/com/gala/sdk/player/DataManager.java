package com.gala.sdk.player;

import java.util.List;

public interface DataManager {

    public interface OnCarouselProgramListFetchedListener {
        void onDataReady(String str, List<CarouselProgram> list);
    }

    void fetchCarouselProgramList(String str, OnCarouselProgramListFetchedListener onCarouselProgramListFetchedListener);
}
