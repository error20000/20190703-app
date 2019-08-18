package com.jian.system.adapter;

import android.content.Context;
import android.graphics.Color;

import com.jian.system.R;
import com.jian.system.entity.Dict;
import com.jian.system.entity.Messages;
import com.jian.system.model.HomeRvItem;
import com.jian.system.utils.FormatUtils;
import com.jian.system.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class HomeMsgAdapter extends BaseRecyclerAdapter<Messages> {

    List<Dict> msgTypeData = new ArrayList<>();

    public HomeMsgAdapter(Context ctx, List<Messages> data,
                           List<Dict> msgTypeData) {
        super(ctx, data, false);
        this.msgTypeData = msgTypeData;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_home_item_msg;
    }

    @Override
    public int getFootLayoutId(int viewType) {
        return R.layout.fragment_list_foot;
    }

    @Override
    public void bindData(BaseRecyclerViewHolder holder, int position, Messages item) {
        holder.getTextView(R.id.item_title).setText(item.getsMsg_Title());

        String typeName = FormatUtils.formatDict(item.getsMsg_Type(), msgTypeData);
        holder.getTextView(R.id.item_type).setText(typeName);

        holder.getTextView(R.id.item_detail).setText(item.getsMsg_Describe());
        holder.getTextView(R.id.item_date).setText(FormatUtils.formatDate("yyyy-MM-dd HH:mm:ss", item.getdMsg_CreateDate()));
    }
}
