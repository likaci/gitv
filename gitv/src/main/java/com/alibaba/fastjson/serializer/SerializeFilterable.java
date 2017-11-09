package com.alibaba.fastjson.serializer;

import java.util.ArrayList;
import java.util.List;

public abstract class SerializeFilterable {
    protected List<AfterFilter> afterFilters = null;
    protected List<BeforeFilter> beforeFilters = null;
    protected List<ContextValueFilter> contextValueFilters = null;
    protected List<LabelFilter> labelFilters = null;
    protected List<NameFilter> nameFilters = null;
    protected List<PropertyFilter> propertyFilters = null;
    protected List<PropertyPreFilter> propertyPreFilters = null;
    protected List<ValueFilter> valueFilters = null;
    protected boolean writeDirect = true;

    public List<BeforeFilter> getBeforeFilters() {
        if (this.beforeFilters == null) {
            this.beforeFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.beforeFilters;
    }

    public List<AfterFilter> getAfterFilters() {
        if (this.afterFilters == null) {
            this.afterFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.afterFilters;
    }

    public List<NameFilter> getNameFilters() {
        if (this.nameFilters == null) {
            this.nameFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.nameFilters;
    }

    public List<PropertyPreFilter> getPropertyPreFilters() {
        if (this.propertyPreFilters == null) {
            this.propertyPreFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.propertyPreFilters;
    }

    public List<LabelFilter> getLabelFilters() {
        if (this.labelFilters == null) {
            this.labelFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.labelFilters;
    }

    public List<PropertyFilter> getPropertyFilters() {
        if (this.propertyFilters == null) {
            this.propertyFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.propertyFilters;
    }

    public List<ContextValueFilter> getContextValueFilters() {
        if (this.contextValueFilters == null) {
            this.contextValueFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.contextValueFilters;
    }

    public List<ValueFilter> getValueFilters() {
        if (this.valueFilters == null) {
            this.valueFilters = new ArrayList();
            this.writeDirect = false;
        }
        return this.valueFilters;
    }

    public void addFilter(SerializeFilter filter) {
        if (filter != null) {
            if (filter instanceof PropertyPreFilter) {
                getPropertyPreFilters().add((PropertyPreFilter) filter);
            }
            if (filter instanceof NameFilter) {
                getNameFilters().add((NameFilter) filter);
            }
            if (filter instanceof ValueFilter) {
                getValueFilters().add((ValueFilter) filter);
            }
            if (filter instanceof ContextValueFilter) {
                getContextValueFilters().add((ContextValueFilter) filter);
            }
            if (filter instanceof PropertyFilter) {
                getPropertyFilters().add((PropertyFilter) filter);
            }
            if (filter instanceof BeforeFilter) {
                getBeforeFilters().add((BeforeFilter) filter);
            }
            if (filter instanceof AfterFilter) {
                getAfterFilters().add((AfterFilter) filter);
            }
            if (filter instanceof LabelFilter) {
                getLabelFilters().add((LabelFilter) filter);
            }
        }
    }
}
