package com.gala.video.cloudui.view.model;

import java.io.Serializable;
import java.util.List;

public class QCloudViewJsonModel implements Serializable {
    public String background;
    public String bgPaddingBottom;
    public String bgPaddingLeft;
    public String bgPaddingRight;
    public String bgPaddingTop;
    public boolean focusable = true;
    public List<QCloudImageJsonModel> images;
    public int order;
    public List<QCloudTextJsonModel> texts;
}
