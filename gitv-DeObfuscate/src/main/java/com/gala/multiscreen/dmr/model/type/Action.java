package com.gala.multiscreen.dmr.model.type;

public enum Action {
    NONE("none"),
    CLICK("click"),
    BACK("back"),
    SCREEN_SHOT("screen_shot");
    
    private String mValue;

    private Action(String value) {
        this.mValue = value;
    }

    public String getValue() {
        return this.mValue;
    }

    public static Action findAction(String control) {
        for (Action action : values()) {
            if (action.getValue().equals(control)) {
                return action;
            }
        }
        return NONE;
    }
}
