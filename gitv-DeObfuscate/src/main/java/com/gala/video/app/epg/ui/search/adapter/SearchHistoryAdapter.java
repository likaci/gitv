package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.ui.search.db.SearchHistoryBean;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class SearchHistoryAdapter extends BaseSearchAdapter<SearchHistoryBean> {
    private final String LOG_TAG = "EPG/search/SearchHistoryAdapter";

    public SearchHistoryAdapter(Context context, List<SearchHistoryBean> dataList) {
        super(context, dataList);
    }

    protected void fillData(ViewHolder holder, View convertView, int position) {
        super.fillData(holder, convertView, position);
        if (ListUtils.isEmpty(this.mDataList)) {
            LogUtils.m1571e("EPG/search/SearchHistoryAdapter", ">>>>>>>>>> search history list is null");
            return;
        }
        holder.searchTextView.setText("  " + ((SearchHistoryBean) this.mDataList.get(position)).getKeyword());
        holder.searchTextView.setTag(ISearchConstant.TAGKEY_SUGGEST_QPID, ((SearchHistoryBean) this.mDataList.get(position)).getQpid());
        holder.searchTextView.setTag(ISearchConstant.TAGKEY_SUGGEST_TYPE, ((SearchHistoryBean) this.mDataList.get(position)).getType());
    }

    protected void setViewParams(View convertView, int position) {
        super.setViewParams(convertView, position);
        convertView.setTag(ISearchConstant.SEARCH_TYPE_TAG_KEY, Integer.valueOf(1));
        TextView textView = (TextView) convertView;
        textView.setPadding(ResourceUtil.getDimen(C0508R.dimen.dimen_16dp), 0, ResourceUtil.getDimen(C0508R.dimen.dimen_19dp), 0);
        textView.setGravity(16);
        QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_24dp);
    }
}
