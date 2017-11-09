package com.gala.video.app.epg.home.data.hdata.task;

public class TaskInput {
    private String mDataId = "";
    private String mDataType = "";
    private String mTemplateId = "";

    public static class Builder {
        private String mDataId = "";
        private String mDataType = "";
        private String mTemplateId = "";

        public Builder dataId(String dataId) {
            this.mDataId = dataId;
            return this;
        }

        public Builder dataType(String dataId) {
            this.mDataId = dataId;
            return this;
        }

        public Builder templateId(String templateId) {
            this.mTemplateId = templateId;
            return this;
        }

        public TaskInput build() {
            return new TaskInput(this);
        }
    }

    public TaskInput(Builder builder) {
        this.mDataId = builder.mDataId;
        this.mTemplateId = builder.mTemplateId;
        this.mDataType = builder.mDataType;
    }

    public String getDataId() {
        return this.mDataId;
    }

    public String getDataType() {
        return this.mDataType;
    }

    public String getTemplateId() {
        return this.mTemplateId;
    }

    public String toString() {
        return " dataType = " + this.mDataType + " data id = " + this.mDataId + " templateid = " + this.mTemplateId;
    }
}
