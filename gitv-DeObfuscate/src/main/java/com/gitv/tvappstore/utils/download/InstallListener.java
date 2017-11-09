package com.gitv.tvappstore.utils.download;

public interface InstallListener {
    void onAdd(String str);

    void onRemove(String str);

    void onReplace(String str);
}
