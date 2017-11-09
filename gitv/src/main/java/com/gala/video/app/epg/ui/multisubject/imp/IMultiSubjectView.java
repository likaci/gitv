package com.gala.video.app.epg.ui.multisubject.imp;

import android.graphics.Bitmap;
import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import java.util.ArrayList;

public interface IMultiSubjectView {
    void showData(Bitmap bitmap, ArrayList<CardModel> arrayList);

    void showExceptionView(ApiException apiException);
}
