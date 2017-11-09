package com.gala.video.lib.share.ifmanager.bussnessIF.openplay;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenapiReporterManager.Wrapper;

public class NullOpenapiReporterManager extends Wrapper {
    public void onAddPlayRecord(Album album) {
    }

    public void onAddFavRecord(Album album) {
    }

    public void onDeleteAllPlayRecord() {
    }

    public void onDeleteAllFavRecord() {
    }

    public void onDeleteSingleFavRecord(Album album) {
    }

    public void onDeleteSinglePlayRecord(Album album) {
    }
}
