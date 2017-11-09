package com.gala.report.core.upload.config;

import java.util.Map;

public interface UploadExtraInfo {
    void parseUploadExtraInfoMap(Map<String, Object> map);

    String toString();
}
