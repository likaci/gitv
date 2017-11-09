package com.gala.tvapi.tv2.model;

import java.util.ArrayList;
import java.util.List;

public class HotWords extends Model {
    private static final long serialVersionUID = 1;
    public List<String> hotwords;
    public String site = "";
    public List<Word> words;

    public List<Word> getWords() {
        List<Word> arrayList = new ArrayList();
        if (this.words != null && this.words.size() > 0) {
            for (Word word : this.words) {
                if (word.name != null) {
                    arrayList.add(word);
                }
            }
        }
        return arrayList;
    }
}
