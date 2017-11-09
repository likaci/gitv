package com.gala.sdk.player;

import com.gala.sdk.player.data.IVideo;

public class TipExtra {
    private BitStream f682a;
    private IVideo f683a;
    private String f684a;

    public TipExtra setDefinition(BitStream stream) {
        this.f682a = stream;
        return this;
    }

    public BitStream getBitStream() {
        return this.f682a;
    }

    public TipExtra setNextVideo(IVideo video) {
        this.f683a = video;
        return this;
    }

    public IVideo getNextVideo() {
        return this.f683a;
    }

    public TipExtra setProgramName(String name) {
        this.f684a = name;
        return this;
    }

    public String getProgramNmae() {
        return this.f684a;
    }
}
