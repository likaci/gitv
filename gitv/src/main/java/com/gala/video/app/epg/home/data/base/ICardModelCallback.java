package com.gala.video.app.epg.home.data.base;

import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import java.util.ArrayList;

public interface ICardModelCallback {
    void onFailure(ApiException apiException);

    void onSuccess(ArrayList<CardModel> arrayList, String str);
}
