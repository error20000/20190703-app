package com.jian.system.adapter;

import android.content.Context;
import android.view.View;

import com.jian.system.R;
import com.jian.system.entity.Dict;

import java.util.ArrayList;
import java.util.List;

public class DialogSearchItemAdapter extends CommonAdapter<String> {

    List<String> mData = new ArrayList<>();

    public DialogSearchItemAdapter(Context context, List<String> data, int layoutId) {
        super(context, data, layoutId);
        mData = data;
    }

    @Override
    public void handleItem(View convertView, int position, String item, CommonViewHolder holder) {
        holder.getTextView(R.id.select_item_info).setText(item);
    }
}
