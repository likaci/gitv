package com.gala.sdk.player;

import com.gala.sdk.player.data.IVideo;

public class TipExtra {
    private BitStream a;
    private IVideo f349a;
    private String f350a;

    public TipExtra setDefinition(BitStream stream) {
        this.a = stream;
        return this;
    }

    public BitStream getBitStream() {
        return this.a;
    }

    public TipExtra setNextVideo(IVideo video) {
        this.f349a = video;
        return this;
    }

    public IVideo getNextVideo() {
        return this.f349a;
    }

    public TipExtra setProgramName(String name) {
        this.f350a = name;
        return this;
    }

    public String getProgramNmae() {
        return this.f350a;
    }
}
