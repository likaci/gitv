package com.tvos.apps.utils;

import com.tvos.apps.utils.db.Constraints;
import com.tvos.apps.utils.db.ConstraintsAnnotation;

public class Info {
    @ConstraintsAnnotation(nullParamsConstraints = {Constraints.UNIQUE})
    public int id;
    public String title;
    public long versionCode;
}
