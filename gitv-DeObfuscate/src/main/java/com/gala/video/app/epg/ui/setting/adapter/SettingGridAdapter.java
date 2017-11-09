package com.gala.video.app.epg.ui.setting.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class SettingGridAdapter extends Adapter<SettingHolder> {
    private static final String LOG_TAG = "EPG/setting/SettingGridAdapter";
    protected Context mContext;
    protected List<SettingItem> mDataList;
    private int mSelectedPos = -1;

    class SettingHolder extends ViewHolder {
        TextView itemDescText;
        TextView itemLastText;
        View itemLastView;
        TextView itemNameText;
        View itemNameView;
        TextView itemOptionMaxText;
        View itemOptionMaxView;
        TextView itemOptionText;
        View itemOptionView;
        TextView itemTitleText;
        View itemTitleView;

        public SettingHolder(View view) {
            super(view);
            this.itemTitleView = view.findViewById(C0508R.id.epg_setting_item_title);
            this.itemTitleText = (TextView) view.findViewById(C0508R.id.epg_setting_item_titletext);
            this.itemNameView = view.findViewById(C0508R.id.epg_setting_item_left);
            this.itemNameText = (TextView) view.findViewById(C0508R.id.epg_setting_item_name);
            this.itemDescText = (TextView) view.findViewById(C0508R.id.epg_setting_item_desc);
            this.itemLastView = view.findViewById(C0508R.id.epg_setting_laststate);
            this.itemLastText = (TextView) view.findViewById(C0508R.id.epg_laststate_text);
            this.itemOptionView = view.findViewById(C0508R.id.epg_setting_item_options);
            this.itemOptionText = (TextView) view.findViewById(C0508R.id.epg_option_text);
            this.itemOptionMaxView = view.findViewById(C0508R.id.epg_setting_item_options_max);
            this.itemOptionMaxText = (TextView) view.findViewById(C0508R.id.epg_option_text_max);
        }
    }

    public SettingGridAdapter(Context context, List<SettingItem> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    public int getCount() {
        return ListUtils.isEmpty(this.mDataList) ? 0 : this.mDataList.size();
    }

    public void onBindViewHolder(SettingHolder holder, int position) {
        setViewParams(holder, position);
        fillData(holder, position);
    }

    private void fillData(SettingHolder holder, int position) {
        SettingItem item = (SettingItem) this.mDataList.get(position);
        if (item.isItemFocusable()) {
            holder.itemNameText.setText(item.getItemName());
            holder.itemDescText.setText(item.getItemDes());
            if (!ListUtils.isEmpty(item.getItemOptions())) {
                String optionTxt = item.getItemLastState();
                if ("max".equals(item.getItemOptionType())) {
                    holder.itemOptionMaxText.setText(optionTxt);
                    return;
                } else if ("min".equals(item.getItemOptionType())) {
                    holder.itemOptionText.setText(optionTxt);
                    return;
                } else {
                    return;
                }
            } else if (StringUtils.isEmpty(item.getItemLastState())) {
                holder.itemLastText.setText(" ");
                return;
            } else {
                holder.itemLastText.setText(item.getItemLastState() + " ");
                return;
            }
        }
        holder.itemTitleText.setText(item.getItemTitle() + " ");
    }

    private void setViewParams(SettingHolder holder, int position) {
        SettingItem item = (SettingItem) this.mDataList.get(position);
        if (!item.isItemFocusable()) {
            holder.itemView.setFocusable(false);
            holder.itemView.setBackgroundDrawable(null);
            holder.itemOptionView.setVisibility(8);
            holder.itemOptionMaxView.setVisibility(8);
            holder.itemLastView.setVisibility(8);
            holder.itemNameView.setVisibility(8);
            holder.itemTitleView.setVisibility(0);
        } else if (item.isItemFocusable()) {
            holder.itemView.setFocusable(true);
            holder.itemView.setBackgroundDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_setting_item_bg));
            if (StringUtils.isEmpty(item.getItemAction())) {
                holder.itemNameText.setMaxEms(30);
                holder.itemOptionView.setVisibility(8);
                holder.itemOptionMaxView.setVisibility(8);
                holder.itemLastView.setVisibility(8);
                holder.itemTitleView.setVisibility(8);
                holder.itemNameView.setVisibility(0);
                if (StringUtils.isEmpty(item.getItemDes())) {
                    holder.itemDescText.setVisibility(8);
                } else {
                    holder.itemDescText.setVisibility(0);
                }
                if (item.isSelected()) {
                    this.mSelectedPos = position;
                    holder.itemNameText.setTextColor(ResourceUtil.getColor(C0508R.color.item_option));
                    return;
                }
                holder.itemNameText.setTextColor(ResourceUtil.getColor(C0508R.color.item_name));
                return;
            }
            holder.itemTitleView.setVisibility(8);
            if (ListUtils.isEmpty(item.getItemOptions())) {
                holder.itemOptionView.setVisibility(8);
                holder.itemOptionMaxView.setVisibility(8);
                holder.itemLastView.setVisibility(0);
                holder.itemNameView.setVisibility(0);
            } else {
                holder.itemLastView.setVisibility(8);
                holder.itemNameView.setVisibility(0);
                if ("max".equals(item.getItemOptionType())) {
                    holder.itemOptionView.setVisibility(8);
                    holder.itemOptionMaxView.setVisibility(0);
                } else if ("min".equals(item.getItemOptionType())) {
                    holder.itemOptionMaxView.setVisibility(8);
                    holder.itemOptionView.setVisibility(0);
                }
            }
            if (StringUtils.isEmpty(item.getItemDes())) {
                holder.itemDescText.setVisibility(8);
            } else {
                holder.itemDescText.setVisibility(0);
            }
        } else {
            LogUtils.m1571e(LOG_TAG, "item's itemFocus maybe invalid");
        }
    }

    public SettingHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(this.mContext).inflate(C0508R.layout.epg_setting_item_view, parent, false);
        view.setFocusable(true);
        return new SettingHolder(view);
    }

    public String switchOptions(View convertView, int pos, KeyEvent event) {
        String str = null;
        List options = ((SettingItem) this.mDataList.get(pos)).getItemOptions();
        if (!ListUtils.isEmpty(options)) {
            View itemOptionView = convertView.findViewById(C0508R.id.epg_setting_item_options);
            TextView itemOptionText = (TextView) convertView.findViewById(C0508R.id.epg_option_text);
            TextView itemOptionMaxText = (TextView) convertView.findViewById(C0508R.id.epg_option_text_max);
            String curOption = "";
            if (itemOptionView.getVisibility() == 0) {
                curOption = itemOptionText.getText().toString();
            } else {
                curOption = itemOptionMaxText.getText().toString();
            }
            curOption = curOption.trim();
            int curIndex = -1;
            for (int i = 0; i < options.size(); i++) {
                if (((String) options.get(i)).trim().equals(curOption)) {
                    curIndex = i;
                    break;
                }
            }
            if (event.getKeyCode() == 21) {
                if (curIndex > 0 && curIndex < options.size()) {
                    curIndex--;
                } else if (curIndex == 0) {
                    curIndex = options.size() - 1;
                }
            } else if (event.getKeyCode() == 22) {
                if (curIndex >= 0 && curIndex < options.size() - 1) {
                    curIndex++;
                } else if (curIndex == options.size() - 1) {
                    curIndex = 0;
                }
            }
            if (curIndex >= 0) {
                str = (String) options.get(curIndex);
                if (itemOptionView.getVisibility() == 0) {
                    itemOptionText.setText(str);
                } else {
                    itemOptionMaxText.setText(str);
                }
            }
        }
        return str;
    }

    public void changeTextColor(View convertView, boolean hasFocus, int pos) {
        int nameColor;
        TextView itemNameText = (TextView) convertView.findViewById(C0508R.id.epg_setting_item_name);
        TextView itemDescText = (TextView) convertView.findViewById(C0508R.id.epg_setting_item_desc);
        TextView itemOptionText = (TextView) convertView.findViewById(C0508R.id.epg_option_text);
        TextView itemOptionMaxText = (TextView) convertView.findViewById(C0508R.id.epg_option_text_max);
        TextView itemLastText = (TextView) convertView.findViewById(C0508R.id.epg_laststate_text);
        int nameNormal = C0508R.color.item_name;
        if (this.mSelectedPos != -1 && this.mSelectedPos == pos) {
            nameNormal = C0508R.color.item_option;
        }
        if (hasFocus) {
            nameColor = C0508R.color.item_name_focus;
        } else {
            nameColor = nameNormal;
        }
        int descColor = hasFocus ? C0508R.color.item_desc_focus : C0508R.color.item_desc;
        int optionColor = hasFocus ? C0508R.color.item_name_focus : C0508R.color.item_option;
        int lastStateColor = hasFocus ? C0508R.color.item_name_focus : C0508R.color.albumview_yellow_color;
        itemNameText.setTextColor(ResourceUtil.getColor(nameColor));
        itemDescText.setTextColor(ResourceUtil.getColor(descColor));
        itemOptionText.setTextColor(ResourceUtil.getColor(optionColor));
        itemOptionMaxText.setTextColor(ResourceUtil.getColor(optionColor));
        itemLastText.setTextColor(ResourceUtil.getColor(lastStateColor));
    }

    public void refreshOptionsText(View convertView, String value) {
        TextView itemOptionMaxText = (TextView) convertView.findViewById(C0508R.id.epg_option_text_max);
        ((TextView) convertView.findViewById(C0508R.id.epg_option_text)).setText(value);
        itemOptionMaxText.setText(value);
    }

    public void refreshLastState(View convertView, String lastState) {
        LogUtils.m1570d(LOG_TAG, ">>>>> lastState: ", lastState);
        TextView itemLastText = (TextView) convertView.findViewById(C0508R.id.epg_laststate_text);
        itemLastText.setTextColor(ResourceUtil.getColor(C0508R.color.item_name_focus));
        itemLastText.setText(lastState + " ");
    }
}
