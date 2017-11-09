package com.gala.tvapi.vrs.model;

import com.gala.tvapi.type.SourceType;

public class VVScale extends Model {
    private static final long serialVersionUID = 1;
    public int f1338f = 0;
    public int f1339m = 0;
    public int f1340p = 0;

    public SourceType getSourceType() {
        if (this.f1338f == 1) {
            return SourceType.GALA_PPS;
        }
        if (this.f1338f == 2) {
            return SourceType.GALA;
        }
        if (this.f1338f == 3) {
            return SourceType.PPS;
        }
        return SourceType.GALA_PPS;
    }
}
