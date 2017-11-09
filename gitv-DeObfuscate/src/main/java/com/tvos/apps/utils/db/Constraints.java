package com.tvos.apps.utils.db;

public enum Constraints {
    NOTNULL("NOT NULL"),
    UNIQUE("UNIQUE"),
    PRIMARYKEY("PRIMARY KEY"),
    FOREIGNKEY("FOREIGN KEY"),
    AUTOINCREASE("AUTOINCREASE");
    
    private String mName;

    private Constraints(String name) {
        this.mName = name;
    }

    public String getName() {
        return this.mName;
    }
}
