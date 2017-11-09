package com.gala.video.app.player.ui.config;

import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;

public class ProjectVideoInfo {
    private Album mAlbum;
    private int mDefinition = -1;
    private int mEpisodeIndex = -1;
    private int mPosition = -1;
    private int mProgramId = -1;
    private SourceType mSourceType;

    public ProjectVideoInfo(int programId, int definition, int episodeIndex, SourceType sourceType) {
        this.mProgramId = programId;
        this.mDefinition = definition;
        this.mEpisodeIndex = episodeIndex;
        this.mSourceType = sourceType;
    }

    public void setDefinition(int definition) {
        this.mDefinition = definition;
    }

    public int getDefinition() {
        return this.mDefinition;
    }

    public void setProgramId(int programId) {
        this.mProgramId = programId;
    }

    public int getProgramId() {
        return this.mProgramId;
    }

    public void setmEpisodeIndex(int episodeIndex) {
        this.mEpisodeIndex = episodeIndex;
    }

    public int getmEpisodeIndex() {
        return this.mEpisodeIndex;
    }

    public void setSourceType(SourceType sourceType) {
        this.mSourceType = sourceType;
    }

    public SourceType getSourceType() {
        return this.mSourceType;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public void setAlbum(Album album) {
        this.mAlbum = album;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public String toString() {
        return "VideoInfo(mProgramId=" + this.mProgramId + ", mDefinition=" + this.mDefinition + ", mEpisodeIndex=" + this.mEpisodeIndex + ", mPosition=" + this.mPosition + ")";
    }
}
