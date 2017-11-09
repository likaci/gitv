package com.gala.tvapi.vrs.model;

import com.gala.tvapi.type.SourceType;

public class VVScale extends Model {
    private static final long serialVersionUID = 1;
    public int f = 0;
    public int m = 0;
    public int p = 0;

    public SourceType getSourceType() {
        if (this.f == 1) {
            return SourceType.GALA_PPS;
        }
        if (this.f == 2) {
            return SourceType.GALA;
        }
        if (this.f == 3) {
            return SourceType.PPS;
        }
        return SourceType.GALA_PPS;
    }
}
