package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

public enum WidgetChangeStatus {
    InitChange,
    TabLayoutChange,
    TabDataChange,
    TabOrderChangeManual,
    TabChange,
    PageLayoutChange,
    CardChange,
    CardLayoutChange,
    ItemLayoutChange,
    DataChange,
    NoChange,
    NoData,
    NoCache,
    Default,
    TAB_FOCUS_RESET,
    TabLayoutChangeManual;

    public String toString() {
        switch (this) {
            case InitChange:
                return "InitChange";
            case TabLayoutChange:
                return "TabLayoutChange";
            case TabDataChange:
                return "TabDataChange";
            case PageLayoutChange:
                return "PageLayoutChange";
            case CardChange:
                return "CardChange";
            case CardLayoutChange:
                return "CardLayoutChange";
            case ItemLayoutChange:
                return "ItemLayoutChange";
            case DataChange:
                return "DataChange";
            case NoChange:
                return "NoChange";
            case NoData:
                return "NoData";
            case NoCache:
                return "NoCache";
            case Default:
                return "Default";
            case TabOrderChangeManual:
                return "TabOrderChangeManual";
            case TabLayoutChangeManual:
                return "TabLayoutChangeManual";
            default:
                return super.toString();
        }
    }
}
