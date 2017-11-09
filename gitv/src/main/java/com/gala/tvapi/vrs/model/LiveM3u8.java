package com.gala.tvapi.vrs.model;

import java.util.List;

public class LiveM3u8 extends Model {
    private static final long serialVersionUID = 1;
    public String id = "";
    public String name = "";
    public LiveProgram program;
    public List<LiveStream> streams;
}
