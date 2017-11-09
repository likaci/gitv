package com.gala.video.lib.share.ifmanager.bussnessIF.epg.banner;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import java.util.List;

public class BannerAdResultModel {
    private List<BannerImageAdModel> models;
    private int resultId;

    public int getResultId() {
        return this.resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public List<BannerImageAdModel> getModels() {
        return this.models;
    }

    public void setModels(List<BannerImageAdModel> models) {
        this.models = models;
    }
}
