package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;

public class SearchHotAdapter extends BaseSearchAdapter<String> {
    private final String LOG_TAG = "EPG/search/SearchHotAdapter";

    public SearchHotAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    protected void fillData(ViewHolder holder, View convertView, int position) {
        super.fillData(holder, convertView, position);
        if (ListUtils.isEmpty(this.mDataList)) {
            LogUtils.m1571e("EPG/search/SearchHotAdapter", ">>>>>>>>>> search hot word list is null");
        } else {
            holder.searchTextView.setText("  " + ((String) this.mDataList.get(position)));
        }
    }

    protected void setViewParams(View convertView, int position) {
        super.setViewParams(convertView, position);
        convertView.setTag(ISearchConstant.SEARCH_TYPE_TAG_KEY, Integer.valueOf(0));
        TextView textView = (TextView) convertView;
        textView.setGravity(16);
        QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_24dp);
    }
}
