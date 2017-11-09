package com.gitv.tvappstore.model;

public class CategoryInfo {
    private String categoryIconUrl;
    private String categoryId;
    private String categoryName;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryIconUrl(String categoryIconUrl) {
        this.categoryIconUrl = categoryIconUrl;
    }

    public String getCategoryIconUrl() {
        return this.categoryIconUrl;
    }
}
