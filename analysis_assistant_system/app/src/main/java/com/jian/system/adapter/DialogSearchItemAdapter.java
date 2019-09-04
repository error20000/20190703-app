package com.jian.system.adapter;

import android.content.Context;
import android.view.View;

import com.jian.system.R;
import com.jian.system.entity.Dict;
import com.jian.system.view.SearchDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class DialogSearchItemAdapter extends CommonAdapter<SearchDialogBuilder.ItemEntity> {

    List<SearchDialogBuilder.ItemEntity> mData = new ArrayList<>();

    public DialogSearchItemAdapter(Context context, List<SearchDialogBuilder.ItemEntity> data, int layoutId) {
        super(context, data, layoutId);
        mData = data;
    }

    @Override
    public void handleItem(View convertView, int position, SearchDialogBuilder.ItemEntity item, CommonViewHolder holder) {
        holder.getTextView(R.id.select_item_info).setText(item.getName());
    }
}
