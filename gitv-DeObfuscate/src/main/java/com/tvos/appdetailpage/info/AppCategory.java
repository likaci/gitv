package com.tvos.appdetailpage.info;

public class AppCategory {
    public String category_big_icon;
    public String category_desc;
    public String category_icon;
    public String category_name;
    public String id;
    public String parent_id;

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        boolean result = true;
        if (obj == null || !(obj instanceof AppCategory)) {
            return true;
        }
        AppCategory appCategory = (AppCategory) obj;
        if (this.id != null) {
            result = true & this.id.equalsIgnoreCase(appCategory.id);
        }
        if (this.category_name != null) {
            result &= this.category_name.equalsIgnoreCase(appCategory.category_name);
        }
        if (this.category_desc != null) {
            result &= this.category_desc.equalsIgnoreCase(appCategory.category_desc);
        }
        if (this.category_icon != null) {
            result &= this.category_icon.equalsIgnoreCase(appCategory.category_icon);
        }
        if (this.category_big_icon != null) {
            result &= this.category_big_icon.equalsIgnoreCase(appCategory.category_big_icon);
        }
        if (this.parent_id != null) {
            return result & this.parent_id.equalsIgnoreCase(appCategory.parent_id);
        }
        return result;
    }
}
