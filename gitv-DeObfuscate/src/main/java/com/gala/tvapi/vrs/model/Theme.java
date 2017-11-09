package com.gala.tvapi.vrs.model;

import java.util.List;

public class Theme extends Model {
    private static final long serialVersionUID = 1;
    public List<Entity> entities;
    public int exclusive;
    public String id;
    public String name;
    public List<Reason> reason;
    public int source;
    public int type;
}
