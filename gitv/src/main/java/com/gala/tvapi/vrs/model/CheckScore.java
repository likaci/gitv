package com.gala.tvapi.vrs.model;

import java.util.List;

public class CheckScore extends Model {
    private static final long serialVersionUID = 1;
    public String resourceId;
    public String resourceType;
    public List<String> score;
    public String sns_score;
}
