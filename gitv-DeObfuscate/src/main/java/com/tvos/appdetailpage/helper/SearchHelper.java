package com.tvos.appdetailpage.helper;

import com.tvos.appdetailpage.info.SearchResponse;
import com.tvos.appdetailpage.info.SearchResponse.SearchDocInfo;
import java.util.Iterator;

public class SearchHelper {
    public static String terms(SearchResponse resp) {
        StringBuilder builder = new StringBuilder();
        if (resp.terms != null) {
            Iterator it = resp.terms.iterator();
            while (it.hasNext()) {
                builder.append((String) it.next());
            }
        } else {
            builder.append("unknown");
        }
        return builder.toString();
    }

    public static String firstDocs(SearchResponse resp) {
        return docNum(resp);
    }

    public static String secondDocs(SearchResponse resp) {
        return "0";
    }

    public static String docNum(SearchResponse resp) {
        if (resp.docinfos == null) {
            return "0";
        }
        return Integer.toString(resp.docinfos.size());
    }

    public static String docID(SearchResponse resp, String pos) {
        if (resp.docinfos == null) {
            return "";
        }
        String[] strs = pos.split("-");
        String sort_str;
        if (strs.length == 0) {
            sort_str = "";
        } else {
            sort_str = strs[0];
        }
        String pos_str = strs.length <= 1 ? "" : strs[1];
        if (pos_str.equals("")) {
            return "";
        }
        try {
            return ((SearchDocInfo) resp.docinfos.get(Integer.valueOf(pos_str).intValue() - 1)).doc_id;
        } catch (Exception e) {
            return "";
        }
    }
}
