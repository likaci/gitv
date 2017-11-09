package com.gala.video.app.epg.project.build;

enum BaseLineEdition {
    DEFAULT("gala", "3121"),
    GITV("gitv", "3125"),
    HD_PLUS("hdplus", "3122"),
    LITCHI("litchi", "3123"),
    NO_LOGO("nologo", "3126");
    
    private String mStyle;
    private String mValue;

    private BaseLineEdition(String style, String value) {
        this.mStyle = "gala";
        this.mValue = "3121";
        this.mStyle = style;
        this.mValue = value;
    }

    public String getEditionName() {
        return this.mStyle;
    }

    public String getValue() {
        return this.mValue;
    }

    public static String getPingbackP2(String style) {
        for (BaseLineEdition ui : values()) {
            if (ui.getEditionName().equalsIgnoreCase(style)) {
                return ui.getValue();
            }
        }
        return DEFAULT.getValue();
    }
}
