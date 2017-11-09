package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;

public class FullKeyBoardAdapter extends BaseSearchAdapter<String> {
    private final String LOG_TAG = "EPG/search/FullKeyBoardAdapter";

    public FullKeyBoardAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    protected void fillData(ViewHolder holder, View convertView, int position) {
        super.fillData(holder, convertView, position);
        if (ListUtils.isEmpty(this.mDataList)) {
            LogUtils.e("EPG/search/FullKeyBoardAdapter", ">>>>>>>>>> search suggest list is null");
        } else {
            holder.searchTextView.setText((CharSequence) this.mDataList.get(position));
        }
    }

    protected void setViewParams(View convertView, int position) {
        super.setViewParams(convertView, position);
        TextView textView = (TextView) convertView;
        textView.setBackgroundResource(R.drawable.epg_full_keyboard_bg);
        textView.setGravity(17);
        textView.setTextColor(this.mContext.getResources().getColor(R.color.keyboard_letter));
        QSizeUtils.setTextSize(textView, R.dimen.dimen_33dp);
        if (position > 25) {
            textView.setTextColor(this.mContext.getResources().getColor(R.color.keyboard_num));
        }
    }

    public void changeTextColor(View view) {
        ((TextView) view).setTextColor(this.mContext.getResources().getColor(R.color.gala_write));
    }
}
