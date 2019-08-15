package com.jian.system.adapter;

import android.content.Context;

import com.jian.system.R;
import com.jian.system.model.HomeRvItem;

import java.util.List;

public class HomeRvItemAdapter extends BaseRecyclerAdapter<HomeRvItem> {


    public HomeRvItemAdapter(Context ctx, List<HomeRvItem> data) {
        super(ctx, data, false);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_home_item_rv;
    }

    @Override
    public int getFootLayoutId(int viewType) {
        return 0;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, HomeRvItem item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        if (item.getIconRes() != 0) {
            holder.getImageView(R.id.item_icon).setImageResource(item.getIconRes());
        }
    }
}
