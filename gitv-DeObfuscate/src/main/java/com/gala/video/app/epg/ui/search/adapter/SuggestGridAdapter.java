package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.tvapi.tv2.model.Word;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.util.List;

public class SuggestGridAdapter extends Adapter<SuggestHolder> {
    private Context mContext;
    private List<Word> mSuggests;

    class SuggestHolder extends ViewHolder {
        View sView;

        public SuggestHolder(View view) {
            super(view);
            this.sView = view;
        }
    }

    public SuggestGridAdapter(Context context, List<Word> list) {
        this.mContext = context;
        setSuggests(list);
    }

    public int getCount() {
        return ListUtils.isEmpty(getSuggests()) ? 0 : ListUtils.getCount(getSuggests());
    }

    public SuggestHolder onCreateViewHolder(ViewGroup group, int type) {
        TextView textView = new TextView(this.mContext);
        textView.setFocusable(true);
        textView.setEllipsize(TruncateAt.MARQUEE);
        return new SuggestHolder(textView);
    }

    public void onBindViewHolder(SuggestHolder holder, int position) {
        ((TextView) holder.itemView).setBackgroundResource(C0508R.drawable.share_keyboard_key_bg);
        ((TextView) holder.itemView).setSingleLine(true);
        ((TextView) holder.itemView).setEllipsize(TruncateAt.END);
        ((TextView) holder.itemView).setTextColor(this.mContext.getResources().getColor(C0508R.color.search_right_text_color));
        ((TextView) holder.itemView).setGravity(16);
        QSizeUtils.setTextSize((TextView) holder.itemView, C0508R.dimen.dimen_24dp);
        ((TextView) holder.itemView).setText("  " + ((Word) getSuggests().get(position)).name);
        holder.itemView.setTag(ISearchConstant.TAGKEY_SUGGEST_QPID, ((Word) getSuggests().get(position)).id);
        holder.itemView.setTag(ISearchConstant.TAGKEY_SUGGEST_TYPE, ((Word) getSuggests().get(position)).type);
    }

    private List<Word> getSuggests() {
        return this.mSuggests;
    }

    public void setSuggests(List<Word> suggests) {
        this.mSuggests = suggests;
    }
}
