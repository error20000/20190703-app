package com.jian.system.adapter;

import android.content.Context;
import android.view.View;

import com.jian.system.R;
import com.jian.system.model.StopwatchItem;

import java.util.ArrayList;
import java.util.List;

public class StopwatchItemAdapter extends CommonAdapter<StopwatchItem> {

    List<StopwatchItem> mData = new ArrayList<>();

    public StopwatchItemAdapter(Context context, List<StopwatchItem> data, int layoutId) {
        super(context, data, layoutId);
        mData = data;
    }

    @Override
    public void handleItem(View convertView, int position, StopwatchItem item, CommonViewHolder holder) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        holder.getTextView(R.id.item_date).setText(item.getDate());
        holder.getTextView(R.id.item_time).setText(item.getTime());
    }
}
